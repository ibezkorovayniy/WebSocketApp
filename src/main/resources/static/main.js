$(function() {
    'use strict';

    var client;
    var respTime;

    function showMessage(mesg)
    {
        $('#messages').append('<tr>' +
            '<td>' + mesg.url + '</td>' +
            '<td>' + mesg.respCode + '</td>' +
            '<td>' + mesg.respTime + '</td>' +
            '<td>' + mesg.respLength + '</td>' +
            '<td>' + mesg.contains + '</td>' +
            '</tr>');
    }

    function myFunction() {
        if(respTime.length != 0) {
            var table = document.getElementById("conversation");
            var rowlist = table.getElementsByTagName("tr");
            for(var i=0; i < rowlist.length; i++) {
                var cellVal = parseInt(rowlist[i].cells[2].innerHTML);
                if(cellVal > respTime){
                    rowlist[i].style.background="#fa0";
                }
            }
        }
    }

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        $('#keyword').prop('disabled', !connected);
        $('#url').prop('disabled', !connected);
        $('#resptime').prop('disabled', !connected);
        $("#messages").html("");
    }

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

     $('#keyword,#disconnect,#url, #resptime').prop('disabled', true);

    $('#connect').click(function() {
        client = Stomp.over(new SockJS('/listen'));
        client.connect({}, function (frame) {
            setConnected(true);
            client.subscribe('/topic/messages', function (message) {

                showMessage(JSON.parse(message.body));
                myFunction();
            });
        });

    });
    $('#unsubscribe').click(function () {
        client.send("/app/stop");
    });

    $('#disconnect').click(function() {
        if (client != null) {
            client.disconnect();
            setConnected(false);
        }
        client = null;
    });

    $('#send').click(function() {
        var keyword = $('#keyword').val();
        if(keyword.length == 0) {
            keyword = "Not provided";
        }
        client.send("/app/listen/" + keyword, {}, JSON.stringify({
            url: $('#url').val()
        }));
        respTime = $('#resptime').val();
    });
});