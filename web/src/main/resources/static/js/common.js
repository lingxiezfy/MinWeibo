var serviceUrlBase = "http://localhost:8081/";

//登录 post application/json
var loginUrl = serviceUrlBase + "user/login";
//注册 post application/json
var registerUrl = serviceUrlBase + "user/register";
//登出 get ACCESS_TOKEN
var loginOutUrl = serviceUrlBase + "user/loginOut";
//用户信息 get ACCESS_TOKEN
var userInfoUrl = serviceUrlBase + "user/info";
//更新用户信息 post ACCESS_TOKEN
var updateUserInfoUrl = serviceUrlBase + "user/update";
//发表微博 post form-data
var postWeiboUrl = serviceUrlBase + "weibo/post";
// 自己的微博列表 post ACCESS_TOKEN
var selfWeiboListUrl = serviceUrlBase + "weibo/list";

var userTokenHeaderKey = "ACCESS_TOKEN";
var userTokenStorageKey = "MINIWeibo_token";

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
function loadUserInfo(afterLoad, ifError, timeOut) {
    $.ajax({
        url: userInfoUrl,
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

// 加载用户微博列表
function loadUserWeiBoList(pageIndex, beforeLoad, afterLoad, ifError, timeOut) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 5;

    postWithToken(selfWeiboListUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError, timeOut)
}

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


// 去微博主页
function goWeiBoIndexPage(message, timeOut) {
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
