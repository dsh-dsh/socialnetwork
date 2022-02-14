var stompClient = null;
var user_id = 1;

$(function () {
    connect();
    $("#send").click(function() {sendMessage(); });
});

function connect() {
    var socket = new SockJS('/ws/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/ws/topic/notification/' + user_id, function (data) {
            getNotifications(JSON.parse(data.body));
        });
        stompClient.subscribe('/ws/topic/message/' + user_id, function (data) {
            getMessages(JSON.parse(data.body));
        });
    });
}

function getNotifications(data_body) {
    console.log(data_body);
}

function getMessages(data_body) {
    console.log(data_body);
}

function sendMessage() {
    var end_point = "/ws/app/message/" + user_id;
    stompClient.send(end_point, {}, JSON.stringify({'text': 'test message text'}));
}