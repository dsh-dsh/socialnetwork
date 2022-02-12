var stompClient = null;
var user_id = 1;
var dialog_id = 51;
var token_user_1 = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwMUBtYWlsLnJ1IiwiZXhwIjoxNjQ1ODAyMzg4fQ.2_7nfi3fkFT1nYKNUDGTpvT8q-7HB8vF3ZnRklZrMho';
var token_user_2 = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwMkBtYWlsLnJ1IiwiZXhwIjoxNjQ1ODAyNDM3fQ.cOqAslAFkvlWJPPwz9kF2MAlihmO9ku8vFhUENrWUDQ';

$(function () {
    connect();
    $("#send").click(function() {sendMessage(); });
    $("#subscribeUnread").click(function() {subscribeUnreadCount(); });
    $("#subscribeMessages").click(function() {subscribeMessages(); });
});

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/ws/topic/notification/' + user_id, function (data) {
            getNotifications(JSON.parse(data.body));
        });
    });
}

function getNotifications(data_body) {
    console.log(data_body);
}

function subscribeUnreadCount() {
    console.log("subscribeUnreadCount()");
    stompClient.subscribe('/ws/topic/message/unread/' + user_id, function (data) {
        getUnreadCount();
    });
}

function subscribeMessages() {
    console.log("subscribeMessages()");
    stompClient.subscribe('/ws/topic/message/' + user_id, function () {
        getDialogs();
        getMessages();
    });
}

function getUnreadCount(){
    $.ajax({
        url: '/api/v1/dialogs/unreaded',
        headers: {'Authorization': token_user_1},
        method: 'get',
        dataType: 'json',
        success: function(data){
            console.log("get unread count");
            console.log(data);
        }
    });
}

function getDialogs(){
    $.ajax({
        url: '/api/v1/dialogs/',
        headers: {'Authorization': token_user_1},
        method: 'get',
        dataType: 'json',
        success: function(data){
            console.log("get dialogs");
            console.log(data);
        }
    });
}

function getMessages(){
    $.ajax({
        url: '/api/v1/dialogs/' + dialog_id + '/messages',
        headers: {'Authorization': token_user_1},
        method: 'get',
        dataType: 'json',
        success: function(data){
            console.log("get messages");
            console.log(data);
        }
    });
}

function sendMessage() {
    messageRequestDTO = JSON.stringify({message_text: "test"});
    $.post({
        url: '/api/v1/dialogs/' + dialog_id + '/messages',
        headers: {'Authorization': token_user_2},
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: messageRequestDTO,
        success: function(data){
            console.log("send message from user 2 to user 1");
            console.log(data);
        }
    });
}

//function sendMessage() {
//    var end_point = "/ws/app/message/" + dialog_id + "/" + user_id;
//    stompClient.send(end_point, {}, JSON.stringify({'text': 'test message text'}));
//}