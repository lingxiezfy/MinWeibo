var serviceUrlBase = "http://localhost:8081/";

//登录 post application/json
var loginUrl = serviceUrlBase + "user/login";
//注册 post application/json
var registerUrl = serviceUrlBase + "user/register";
//登出 get ACCESS_TOKEN
var loginOutUrl = serviceUrlBase + "user/loginOut";
//用户信息 get ACCESS_TOKEN
var userInfoUrl = serviceUrlBase + "user/info/";
//更新用户信息 post ACCESS_TOKEN
var updateUserInfoUrl = serviceUrlBase + "user/update";
//用户更新关系 post ACCESS_TOKEN
var relationUserUrl = serviceUrlBase + "user/relation";
//查找用户 post ACCESS_TOKEN
var searchUserUrl = serviceUrlBase + "user/search";

//发表微博 post form-data
var postWeiboUrl = serviceUrlBase + "weibo/post";
// 用户的微博列表 post ACCESS_TOKEN
var selfWeiboListUrl = serviceUrlBase + "weibo/list";
// 所有微博列表 post
var weiboListUrl = serviceUrlBase + "weibo/listAll";
// 根据id加载一条微博 get ACCESS_TOKEN
var weiBoInfoUrl = serviceUrlBase + "weibo/";
// 根据id删除一条微博 get ACCESS_TOKEN
var deleteWeiBoUrl = serviceUrlBase + "weibo/delete/";
var searchWeiBoUrl = serviceUrlBase + "weibo/search";

var userTokenHeaderKey = "ACCESS_TOKEN";
var userTokenStorageKey = "MINIWeiBo_token";
var userIdStorageKey = "MINIWeiBo_userId";

// 表单转json对象（如果需要json字符串，需要使用JSON.stringify()在转一次）
$.fn.serializeObject = function () {
    var ct = this.serializeArray();
    var obj = {};
    $.each(ct, function () {
        if (obj[this.name] !== undefined) {
            if (!obj[this.name].push) {
                obj[this.name] = [obj[this.name]];
            }
            obj[this.name].push(this.value || "");
        } else {
            obj[this.name] = this.value || "";
        }
    });
    return obj;
};

//获取Url参数
function getQueryVariable(variable) {
    var query = decodeURIComponent(window.location.search.substring(1));
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] === variable){return pair[1];}
    }
    return(false);
}

// 操作 localStorage
function handleLocalStorage(method, key, value) {
    switch (method) {
        case 'get' : {
            let temp = window.localStorage.getItem(key);
            if (temp) {
                return temp
            } else {
                return false
            }
        }
        case 'set' : {
            window.localStorage.setItem(key, value);
            break
        }
        case 'remove': {
            window.localStorage.removeItem(key);
            break
        }
        default : {
            return false
        }
    }
}

function loginExpeirFilter(response) {
    if (response && response.code && "301" === response.code) {
        var message = response.message ? response.message : "登录信息已过期，请重新登录";
        setTimeout(function () {
            window.location.href = 'login.html'
        }, 1500);
        //跳转登录页
        swal(message, {
            buttons: false,
            timer: 2000,
        });
        return false;
    }
    return true;
}

// 刷新用户信息
function loadUserInfo(targetUserId,afterLoad, ifError, timeOut) {
    if(!targetUserId){
        targetUserId = 0;
    }
    $.ajax({
        url: userInfoUrl+''+targetUserId,
        type: "get",
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(userTokenHeaderKey, handleLocalStorage("get", userTokenStorageKey))
        },
        success: function (response) {
            if (loginExpeirFilter(response) && response.success && response.result) {
                delayOrimmediatelyOp(function () {
                    afterLoad(response.result);
                }, timeOut);
            } else {
                delayOrimmediatelyOp(function () {
                    ifError(response.message);
                }, timeOut);
            }
        },
        error: function (e) {
            delayOrimmediatelyOp(function () {
                ifError("访问远程服务器失败！");
            }, timeOut);
        }
    });
}
// 关联用户（relation,1:关注，2:拉黑）
function relationUser(userId,relation,before,after,error) {
    var obj = {};
    obj["userId"] = userId;
    obj["relation"] = relation;
    postWithToken(relationUserUrl,JSON.stringify(obj),before,after,error);
}
// 查找用户
function toSearchUser(){
    var query = $("#mini-search-btn").val();
    if(query){
        toPage("search.html?query="+encodeURIComponent(query)+"&searchType=1")
    }else {
        toastr.error("请输入查询的用户名或昵称");
    }

}

// 加载用户微博列表
function loadUserWeiBoList(targetUserId,pageIndex, beforeLoad, afterLoad, ifError, timeOut) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 5;
    if(targetUserId){
        obj['targetUserId'] = targetUserId;
    }

    postWithToken(selfWeiboListUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError, timeOut)
}
// 加载用户微博列表
function loadAllWeiBoList(pageIndex, beforeLoad, afterLoad, ifError, timeOut) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 5;

    postWithToken(weiboListUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError, timeOut)
}
function searchWeiBoList(query,searchType,pageIndex, beforeLoad, afterLoad, ifError, timeOut) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 5;
    if(query && searchType && searchType === '2'){
        obj["query"] = query;
    }else if(searchType){
        obj["topic"] = query;
    }

    postWithToken(searchWeiBoUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError, timeOut)
}

function searchUserList(query,pageIndex, beforeLoad, afterLoad, ifError, timeOut) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 10;
    obj["query"] = query;

    postWithToken(searchUserUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError, timeOut)
}
function toSearchWeiBo(){
    var query = $("#mini-search-btn").val();
    if(query){
        toPage("search.html?query="+encodeURIComponent(query)+"&searchType=2")
    }else {
        toastr.error("请输入内容查询微博");
    }
}

function toSecondhand(){
    toPage("search.html?query="+encodeURIComponent("二手交易")+"&searchType=3")
}
function toFunnyChat(){
    toPage("search.html?query="+encodeURIComponent("趣味讨论")+"&searchType=4")
}
// 去用户主页
function toUserIndex(userId){
    console.log(userId);
    toPage('home.html?userId='+userId);
}

// 根据Id删除一条微博
function deleteWeiBoById(weiBoId,before,after,error) {
    getWithToken(deleteWeiBoUrl+weiBoId,before,after,error)
}
// 根据Id获取一条微博
function weiBoInfoById(weiBoId,before,after,error) {
    getWithToken(weiBoInfoUrl+weiBoId,before,after,error)
}

// 执行需要Token验证的post请求
function postWithToken(url, postData, beforeLoad, afterLoad, ifError, timeOut) {
    $.ajax({
        url: url,
        type: "post",
        cache: false,
        data: postData,
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(userTokenHeaderKey, handleLocalStorage("get", userTokenStorageKey))
            //返回true才会继续往下执行，否则中断请求
            return beforeLoad();
        },
        success: function (response) {
            if (loginExpeirFilter(response) && response.success) {
                delayOrimmediatelyOp(function () {
                    if (response.result) {
                        afterLoad(response.result);
                    } else {
                        afterLoad();
                    }
                }, timeOut);
            } else {
                delayOrimmediatelyOp(function () {
                    ifError(response.message);
                }, timeOut);
            }
        },
        error: function (e) {
            delayOrimmediatelyOp(function () {
                ifError("访问远程服务器失败！");
            }, timeOut);
        }
    });
}

// 执行需要Token验证的get请求
function getWithToken(url, beforeLoad, afterLoad, ifError, timeOut) {
    if(false === beforeLoad()){
        return;
    }
    $.ajax({
        url: url,
        type: "get",
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(userTokenHeaderKey, handleLocalStorage("get", userTokenStorageKey))
        },
        success: function (response) {
            if (loginExpeirFilter(response) && response.success) {
                delayOrimmediatelyOp(function () {
                    if (response.result) {
                        afterLoad(response.result);
                    } else {
                        afterLoad();
                    }
                }, timeOut);
            } else {
                delayOrimmediatelyOp(function () {
                    ifError(response.message);
                }, timeOut);
            }
        },
        error: function (e) {
            delayOrimmediatelyOp(function () {
                ifError("访问远程服务器失败！");
            }, timeOut);
        }
    });
}

// 延时操作
function delayOrimmediatelyOp(fun, timeOut) {
    if (timeOut && timeOut > 0) {
        setTimeout(fun(), timeOut);
    } else {
        fun();
    }
}

// 登出
function logout() {
    $.ajax({
        url: loginOutUrl,
        type: "get",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(userTokenHeaderKey, handleLocalStorage("get", userTokenStorageKey))
        },
        success: function (response) {
            goLogin("退出成功，请重新登录!");
        },
        error: function () {
            toastr.warn('访问服务器失败！');
            goLogin("本地退出,即将返回登录界面！");
        }
    });
}

// 去登录界面
function goLogin(message) {
    handleLocalStorage("remove", userTokenStorageKey);
    //跳转登录页
    if (message) {
        swal(message, {
            buttons: false,
            timer: 3000,
        });
    }

    setTimeout(function () {
        window.location.href = 'login.html'
    }, 2000);
}


// 去主页
function goWeiBoIndex(message, timeOut) {
    //跳转登录页
    if (message) {
        swal(message, {
            buttons: false,
            timer: timeOut,
        });
    }

    delayOrimmediatelyOp(function () {
        window.location.href = 'index.html'
    }, timeOut);
}

// 页面跳转
function toPage(url, message, timeOut) {
    if (message) {
        swal(message, {
            buttons: false,
            timer: timeOut,
        });
    }
    delayOrimmediatelyOp(function () {
        window.location.href = url
    }, timeOut);
}

//添加一条微博
function addOneWeiBo(data) {
    var div = '<div class="col-sm-12 col-xs-12 message" id="weiBoItem'+data.weiboId+'">' +
        '<div class="row">';
    div += '<div class="col-md-1 col-sm-2 col-xs-12" onclick="toUserIndex('+data.author.userId+')">\n' +
        '<img class="user-face" src="';
    div += data.author.face ? "upload/"+data.author.face : "img/icon.png";
    div +='" style="border-radius: 50%">\n' +
        '</div>\n' +
        '<div class="col-md-11 col-sm-10 col-xs-12">' +
        '<div class="row">\n' +
        '<div class="col-sm-6 col-xs-6"><span style="font-weight: bold;">'+data.author.nickname+'</span></div>';
    if(data.author.userId === parseInt(handleLocalStorage("get",userIdStorageKey))){
        div +=  '<div class="col-sm-6 col-xs-6">\n' +
            '<div class="pull-right">\n' +
            '<button type="button" onclick="toDeleteWeiBoById('+data.weiboId+')" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="删除">\n' +
            '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>\n' +
            '<button type="button" onclick="toEditWeiBoById('+data.weiboId+')" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="编辑">\n' +
            '<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>\n' +
            '</div>'+
            '</div>';
    }
    div += '</div>\n' +
        '<div class="row">\n' +
        '<div class="col-sm-12 col-xs-12"><small class="date" style="color:#999">'+data.postTime+'</small></div>\n' +
        '</div>\n' +
        '<div class="row">\n' +
        '<div class="col-sm-12 col-xs-12">\n' +
        '<div class="msg_content">'+data.content+'</div>';
    if (data.pic && data.pic.length > 0) {
        div += "<div class=\"msg_pic pic_view\"><table><tbody>";
        var rowCount = 0;
        for (var i = 1; i <= data.pic.length; i++) {
            if (rowCount === 0) {
                div += "<tr>";
            }
            div += "<td><img class=\"mypic\" src=\"upload/" + data.pic[i - 1] + "\"></td>\n";
            rowCount++;
            if (rowCount === 3) {
                div += "</tr>"
                rowCount = 0;
            }
        }
        div += "</tbody></table></div>";
    }
    div += '</div>\n' +
        '</div>\n' +
        '</div>' +
        '</div>';
    //转发体
    if(!data.original){
        div+='<div class="row repost_weiBo">\n' +
            '<div class="col-md-1 col-sm-2 col-xs-12"></div>\n' +
            '<div class="col-md-11 col-sm-10 col-xs-12">\n' +
                '<div class="row">\n' +
                    '<div class="col-sm-12 col-xs-12">';
                    if(data.repostWeiBo){
                        div += '<div class="msg_author"><strong><a href="javascript:toUserIndex('+data.repostWeiBo.author.userId+')">@'+data.repostWeiBo.author.nickname+'</a></strong></div>\n' +
                        '<div class="msg_content">'+data.repostWeiBo.content+'</div>';
                        if (data.repostWeiBo.pic && data.repostWeiBo.pic.length > 0) {
                        div += "<div class=\"msg_pic pic_view\"><table><tbody>";
                        var rowCount = 0;
                        for (var i = 1; i <= data.repostWeiBo.pic.length; i++) {
                            if (rowCount === 0) {
                                div += "<tr>";
                            }
                            div += "<td><img class=\"mypic\" src=\"upload/" + data.repostWeiBo.pic[i - 1] + "\"></td>\n";
                            rowCount++;
                            if (rowCount === 3) {
                                div += "</tr>"
                                rowCount = 0;
                            }
                        }
                        div += "</tbody></table></div>";
                    }
                    div += '</div>\n' +
                '</div>\n' +
                '<div class="row">\n' +
                    '<div class="col-sm-12 col-xs-12">\n' +
                        '<small class="date" style="color:#999">'+data.repostWeiBo.postTime+'</small>\n' +
                        '<div class="pull-right repost_weiBo_tools">\n' +
                            '<small class="date" style="color:#999"><a href="javascript:weiboCollect('+data.repostWeiBo.weiboId+')" class="first_item"><span class="glyphicon glyphicon-star-empty"></span><em>收藏</em>('+data.repostWeiBo.collectCount+')</a></small>' +
                            '<small class="date" style="color:#999"><a href="javascript:openWeiboRePost('+data.repostWeiBo.weiboId+')"><span class="glyphicon glyphicon-share-alt"></span><em>转发</em>('+data.repostWeiBo.repostCount+')</a></small>\n' +
                            '<small class="date" style="color:#999"><a href="javascript:openWeiboComment('+data.repostWeiBo.weiboId+')"><span class="glyphicon glyphicon-comment"></span><em>评论</em>('+data.repostWeiBo.commentCount+')</a></small>\n' +
                            '<small class="date" style="color:#999"><a href="javascript:weiboLikes('+data.repostWeiBo.weiboId+')"><span class="glyphicon glyphicon-thumbs-up"></span><em>点赞</em>('+data.repostWeiBo.likesCount+')</a></small>\n' +
                        '</div>';
                    }else {
                        div += '<p class="text-center"><h1></h1>抱歉，此微博已被作者删除。</p>';
                    }
                    div += '</div>'+
                '</div>\n' +
            '</div>\n' +
        '</div>';
    }
    //工具条
    div += '<div class="row">\n' +
        '                        <div class="col-sm-12 col-xs-12">\n' +
        '                            <div class="weibo_tools_list">\n' +
        '                                <ul class="list-inline">\n' +
        '                                    <li class=""><a href="javascript:weiboCollect('+data.weiboId+')" class="first_item"><span\n' +
        '                                            class="glyphicon glyphicon-star-empty"></span><em>收藏</em>('+data.collectCount+')</a></li>\n' +
        '                                    <li class=""><a href="javascript:openWeiboRePost('+data.weiboId+')"><span\n' +
        '                                            class="glyphicon glyphicon-share-alt"></span><em>转发</em>('+data.collectCount+')</a></li>\n' +
        '                                    <li class=""><a href="javascript:openWeiboComment('+data.weiboId+')"><span\n' +
        '                                            class="glyphicon glyphicon-comment"></span><em>评论</em>('+data.collectCount+')</a></li>\n' +
        '                                    <li class=""><a href="javascript:weiboLikes('+data.weiboId+')"><span\n' +
        '                                            class="glyphicon glyphicon-thumbs-up"></span><em>点赞</em>('+data.collectCount+')</a></li>\n' +
        '                                </ul>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                    </div>';
    div += '</div>';
    $("#weiBoItem").append(div);
    $(".msg_pic").viewer();
    // 按钮提示插件
    $('[data-toggle="tooltip"]').tooltip();
};