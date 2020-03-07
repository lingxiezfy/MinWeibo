var socketServerUrl = "ws://localhost:8081/ws/server";

var sendAllUrl = serviceUrlBase +"ws/sendAll";
var sendOneUrl = serviceUrlBase +"ws/sendOne";
var sendGroupUrl = serviceUrlBase +"ws/sendGroup";

var socket;
if (typeof (WebSocket) == "undefined") {
    console.log("遗憾：您的浏览器不支持WebSocket");
} else {
    console.log("恭喜：您的浏览器支持WebSocket");

    //实现化WebSocket对象
    //指定要连接的服务器地址与端口建立连接
    //注意ws、wss使用不同的端口。我使用自签名的证书测试，
    //无法使用wss，浏览器打开WebSocket时报错
    //ws对应http、wss对应https。
    socket = new WebSocket(socketServerUrl);
    //连接打开事件
    socket.onopen = function() {
        console.log("Socket 已打开");
    };
    //收到消息事件
    socket.onmessage = function(msg) {
        var json = JSON.parse(msg.data);
        if(json){
            addOneMessage(json);
        }else {
            console.log(msg.data);
        }

    };
    //连接关闭事件
    socket.onclose = function() {
        console.log("Socket已关闭");
    };
    //发生了错误事件
    socket.onerror = function() {
        toastr.error("Socket发生了错误");
    };

    //窗口关闭时，关闭连接
    window.unload=function() {
        socket.close();
    };
}

function registerToServer(groupId,userId) {
    sendToServer('group;'+groupId+';in;'+userId);
}

function sendMessage(groupId,message) {
    sendToServer('group;'+groupId+';msg;'+message);
}

function sendToServer(message) {
    if(message){
        socket.send(message);
    }
}


function sendAll(message) {
    if(message){
        getWithToken(sendAllUrl+'?message='+message,function () {

        },function () {
            toastr.info('发送成功')
        },function (errorMessage) {
            toastr.error(errorMessage);
        });
    }else {
        toastr.error('消息不能为空！');
    }

}

function sendOne(userId,message) {
    if(!userId){
        toastr.error('消息接收人不能为空！');
        return;
    }
    if(!message){
        toastr.error('消息不能为空！');
        return;
    }
    getWithToken(sendOne()+'?message='+message+'&userId='+userId,function () {

    },function () {
        toastr.info('发送成功')
    },function (errorMessage) {
        toastr.error(errorMessage);
    })
}

function sendGroup(discussionId,message) {
    if(!discussionId){
        toastr.error('消息接收组不能为空！');
        return;
    }
    if(!message){
        toastr.error('消息不能为空！');
        return;
    }
    getWithToken(sendOne()+'?message='+message+'&discussionId='+discussionId,function () {

    },function () {
        toastr.info('发送成功')
    },function (errorMessage) {
        toastr.error(errorMessage);
    })
}

