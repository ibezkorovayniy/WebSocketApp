package app;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Controller
public class UrlListenerController {

    @Autowired
    private SimpMessagingTemplate template;
    private Request request;
    private OkHttpClient client = new OkHttpClient();
    private String keyword;
    private String URL;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
    private Logger logger = LoggerFactory.getLogger(UrlListenerController.class);

    @MessageMapping("/listen/{keyword}")
    public void send(@DestinationVariable("keyword") String keyword, Message message) throws Exception {

        this.keyword = keyword;
        URL = message.getUrl();
        request = new Request.Builder()
                .url(message.getUrl())
                .build();
        schedule();
    }

    private void schedule() {
        service.scheduleAtFixedRate(new ScheduledTask(),2, 2, TimeUnit.SECONDS);
    }
    @MessageMapping("/stop")
    public void stopExecution() {
        service.shutdown();
        service = Executors.newScheduledThreadPool(5);
    }

    class ScheduledTask implements Runnable{
        Request req;
        Response resp;
        OutputMessage message;

        {
            req = request;
            message = new OutputMessage();
            message.setKeyword(keyword);
            message.setUrl(URL);

        }
        void scheduledListener() {
            try {
                resp = client.newCall(req).execute();
                String respBody = resp.body().string();
                message.setRespCode(resp.code());
                message.setRespTime((int)(resp.receivedResponseAtMillis() - resp.sentRequestAtMillis()));
                message.setRespLength(respBody.length());
                if(message.getKeyword().equalsIgnoreCase("Not provided")) {
                    message.setContains(false);
                } else {
                    message.setContains(respBody.contains(message.getKeyword()));
                }
                template.convertAndSend("/topic/messages", message);
            } catch (NullPointerException e) {
                logger.info("No message found");
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("Could not get the response");
            }
        }

        @Override
        public void run() {
            scheduledListener();
        }
    }
}
