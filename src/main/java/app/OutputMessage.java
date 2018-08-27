package app;

public class OutputMessage implements Cloneable{
    private String keyword;
    private String url;
    private String respCode;
    private String respTime;
    private String respLength;
    private boolean contains;

    public OutputMessage() {}

    public OutputMessage(String keyword, String url, String respCode, String respTime, String respLength) {
        this.keyword = keyword;
        this.url = url;
        this.respCode = respCode;
        this.respTime = respTime;
        this.respLength = respLength;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = String.valueOf(respCode);
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(int respTime) {
        this.respTime = String.valueOf(respTime);
    }

    public String getRespLength() {
        return respLength;
    }

    public void setRespLength(int respLength) {
        this.respLength = String.valueOf(respLength);
    }

    public boolean isContains() {
        return contains;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OutputMessage message = null;
        try {
            message = (OutputMessage)super.clone();
        } catch (CloneNotSupportedException e) {
            message = new OutputMessage(this.getKeyword(), this.getUrl(), this.getRespCode(), this.getRespTime(), this.getRespLength());

        }
        return message;
    }
}