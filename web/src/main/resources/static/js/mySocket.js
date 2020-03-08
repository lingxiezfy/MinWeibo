var socketServerUrl = "ws://localhost:8081/ws/server";

var sendAllUrl = serviceUrlBase +"ws/sendAll";

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
        console.log("收到消息：%s", msg.data);
        var json = JSON.parse(msg.data);
        if(json){
            addOneMessage(json);
        }else {
            console.log("消息格式有误,未解析:%s", msg.data);
        }
    };
    //连接关闭事件
    socket.onclose = function() {
        console.log("Socket已关闭");
    };
    //发生了错误事件
    socket.onerror = function() {
        console.log("Socket发生了错误");
    };

    //窗口关闭时，关闭连接
    window.unload=function() {
        socket.close();
    };
}

// 处理一条解析后的消息（统一的处理方法，特别的处理方式可在页面中重载）
function addOneMessage(message){
    var span = $('.userNotice.badge');
    //添加徽章通知
    if(span){
        var n = span.html();
        if(!n){
            n = 0;
        }
        span.html(parseInt(n)+1)
    }
    toastr.options.timeOut=5000;
    toastr.options.onclick = function () {
        toPage("notice.html?messageId="+(message.third?message.third:0));
    };
    toastr.options.extendedTimeOut = 2000;
    toastr.options.progressBar = true;
    toastr.options.positionClass = 'toast-top-right';
    if(message.messageType === "SystemNotice"){
        toastr.warning("系统公告："+"<br/>"+message.content+"&nbsp;前往查看>>>");
    }else {
        toastr.info(message.author.nickname+"<br/>"+message.content+"&nbsp;前往查看>>>");
    }
    toastr.options.timeOut=1200;
    toastr.options.onclick = null;
    toastr.options.progressBar = false;
    toastr.options.extendedTimeOut = 1000;
    toastr.options.positionClass = 'toast-bottom-right';
}

// 向服务器注册身份
function registerToServer(groupId,userId) {
    sendToServer('group;'+groupId+';in;'+userId);
}
// 向服务器发送组内群发消息
function sendMessage(groupId,message) {
    sendToServer('group;'+groupId+';msg;'+message);
}
// 向服务器发送群发消息
function sendToServer(message) {
    if(message){
        socket.send(message);
    }
}


