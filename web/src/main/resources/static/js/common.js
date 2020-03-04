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
var relationListUrl = serviceUrlBase +"user/relation/list";

//发表微博 post form-data
var postWeiboUrl = serviceUrlBase + "weibo/post";
// 转发微博 post ACCESS_TOKEN
var rePostWeiboUrl = serviceUrlBase + "weibo/rePost";
// 用户的微博列表 post ACCESS_TOKEN
var selfWeiboListUrl = serviceUrlBase + "weibo/list";
// 所有微博列表 post
var weiboListUrl = serviceUrlBase + "weibo/listAll";
// 根据id加载一条微博 get ACCESS_TOKEN
// var weiBoInfoUrl = serviceUrlBase + "weibo/";
// 根据id删除一条微博 get ACCESS_TOKEN
var deleteWeiBoUrl = serviceUrlBase + "weibo/delete/";
var searchWeiBoUrl = serviceUrlBase + "weibo/search";

// 根据id收藏一条微博 get ACCESS_TOKEN
var collectWeiBoUrl = serviceUrlBase + "weibo/collect/";
var cancelCollectUrl = serviceUrlBase + "weibo/collect/cancel/";
var userCollectWeiBoUrl = serviceUrlBase + "weibo/collect/list";

// 根据id给一条微博点赞 get ACCESS_TOKEN
var likesWeiBoUrl = serviceUrlBase + "weibo/likes/";
var cancelLikesUrl = serviceUrlBase + "weibo/likes/cancel/";

// 评论列表
var commentListUrl = serviceUrlBase + "comment/list";
// 添加评论
var addCommentUrl = serviceUrlBase + "comment/add";
// 评论点赞
var likeCommentUrl = serviceUrlBase + "comment/likes/";
// 取消点赞
var cancelLikeCommentUrl = serviceUrlBase + "comment/cancelLikes/";

//讨论组信息
var discussionInfoUrl = serviceUrlBase + "discussion/info/";

var userTokenHeaderKey = "ACCESS_TOKEN";
var userTokenStorageKey = "MINIWeiBo_token";
var userIdStorageKey = "MINIWeiBo_userId";
var adminStorageKey = "MINIWeiBo_admin";

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
        //跳转登录页
        Swal.fire({
            icon: 'error',
            title: message,
            showConfirmButton: false,
            timer: 1000
        }).then(function () {
            window.location.href = 'login.html'
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

function toDiscussion(ele,discussionId){
    Swal.fire({
        icon: 'success',
        title: '即将进入趣味讨论!',
        showConfirmButton: false,
        timer: 1500
    }).then(function () {
        window.open('chat.html?discussionId='+discussionId
            ,'discussion'+discussionId);
    });
}

// 关联用户（1,关注；2，取消关注；3：拉黑；4：取消拉黑）
function toRelationUser(userId,relationType,after){
    var relationCode = 0;
    var desc;
    switch (relationType) {
        case 1:
            relationCode = 1;
            desc = "关注";
            break;
        case 3:
            relationCode = 2;
            desc = "拉黑";
            break;
        case 2:
        case 4:
            relationCode = 0;
            desc = "取消";
            break;
        default:
            toastr.error("错误的操作");
            return;
    }
    relationUser(userId,relationCode,
        function () {},
        function () {
            toastr.info(desc + "成功");
            after();
        },
        function (errorMessage) {
            toastr.error(errorMessage);
        }
    );
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
// 加载微博列表
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

function searchCollectList(query,searchType,pageIndex, beforeLoad, afterLoad, ifError) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 5;
    postWithToken(userCollectWeiBoUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError)
}

function searchUserList(query,searchType,pageIndex, beforeLoad, afterLoad, ifError) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 10;
    obj["query"] = query;

    postWithToken(searchUserUrl, JSON.stringify(obj), beforeLoad, afterLoad, ifError)
}

//relationState 1 关注，2拉黑
function searchRelationList(relationState,searchType,pageIndex, before, after, error) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载5条
    obj["pageSize"] = 10;
    obj["relationState"] = relationState;
    postWithToken(relationListUrl,JSON.stringify(obj),before,after,error);
}


function toSearchWeiBo(){
    var query = $("#mini-search-btn").val();
    if(query){
        toPage("search.html?query="+encodeURIComponent(query)+"&searchType=2")
    }else {
        toastr.error("请输入内容查询微博");
    }
}

function toMyRelationLike(){
    toPage("search.html?query=1&searchType=6")
}

function toMyCollect(){
    toPage("search.html?query=&searchType=5")
}

function toSecondhand(){
    toPage("search.html?query="+encodeURIComponent("二手交易")+"&searchType=3")
}
function toFunnyChat(){
    toPage("search.html?query="+encodeURIComponent("趣味讨论")+"&searchType=4")
}
// 去用户主页
function toUserIndex(userId){
    toPage('home.html?userId='+userId);
}
// 删除微博
function toDeleteWeiBoById(weiBoId){
    Swal.fire({
        icon:'warning',
        title:"警告",
        text:"删除后将无法恢复，请谨慎操作！",
        confirmButtonText: '确认删除',
        showCloseButton: true,
        showCancelButton: true,
        cancelButtonText:'取消',
    }).then((result)=> {
        if(result.value){
            deleteWeiBoById(weiBoId
                ,function () {

                },function () {
                    toastr.info("删除成功");
                    refreshUserInfo(true);
                },function (errorMessage) {
                    toastr.error(errorMessage);
                });
        }
    });

}

// 删除一条微博
function deleteWeiBoById(weiBoId,before,after,error) {
    getWithToken(deleteWeiBoUrl+weiBoId,before,after,error)
}

// 评论模态框 打开监听 加载数据
$('#commentModal').on('show.bs.modal',function (event) {
    var a = $(event.relatedTarget);
    var weiBoId = a.data('weiboid');
    $('#commentBtn').attr('onclick','addComment('+weiBoId+')');
    loadOnePageCommentToModal(weiBoId,1);
});
// 评论模态框 关闭监听 重置模态框
$('#commentModal').on('hidden.bs.modal',function (event) {
    $('#commentBtn').removeAttr('onclick');
    $('#commentList').empty();
    $('#commentContent').val('');
    $('#commentPageBox .totalCount').html('0');
    $('#commentPageBox .pageIndex').html('0');
    $('#commentPageBox .totalPage').html('0');
    $('#commentPageBox .prePage').attr('disabled','disabled');
    $('#commentPageBox .nextPage').attr('disabled','disabled');
    $('#commentPageBox .prePage').removeAttr('onclick');
    $('#commentPageBox .nextPage').removeAttr('onclick');
    modalMessage('')
});
$('#commentModal').on('hide.bs.modal',function (event) {
    refreshWeiBoList();
});

function loadOnePageCommentToModal(weiBoId,pageIndex) {
    commentList(
        weiBoId,
        pageIndex,
        function () {
            modalMessage('warring','加载中...')
        },
        function (response) {
            if(response.list.length > 0){
                $('#commentList').empty();
                for (var comment of response.list) {
                    addOneCommentToModal(comment);
                }
                modalMessage('success','加载成功');
            }else {
                modalMessage('');
                $('#commentList').append('<p class="col-xs-12 text-center" style="color: red">暂无评论，来发表你的意见吧！</p>');
            }
            $('#commentPageBox .totalCount').html(response.totalCount);
            $('#commentPageBox .pageIndex').html(response.pageIndex);
            $('#commentPageBox .totalPage').html(response.totalPage);

            if(response.pageIndex <= 1){
                $('#commentPageBox .prePage').attr('disabled','disabled');
                $('#commentPageBox .prePage').removeAttr('onclick');
            }else {
                $('#commentPageBox .prePage').removeAttr('disabled');
                $('#commentPageBox .prePage').attr('onclick','loadOnePageCommentToModal('+weiBoId+','+(response.pageIndex-1)+')');
            }
            if(response.pageIndex >= response.totalPage){
                $('#commentPageBox .nextPage').attr('disabled','disabled');
                $('#commentPageBox .nextPage').removeAttr('onclick');
            }else {
                $('#commentPageBox .nextPage').removeAttr('disabled');
                $('#commentPageBox .nextPage').attr('onclick','loadOnePageCommentToModal('+weiBoId+','+(response.pageIndex+1)+')');
            }
        },function (errorMessage) {
            modalMessage('error',errorMessage);
        });
}

function commentList(weiBoId,pageIndex,before, after, error) {
    var obj = {};
    obj["pageIndex"] = pageIndex;
    // 一次加载10条
    obj["pageSize"] = 10;
    obj["weiBoId"] = weiBoId;

    postWithToken(commentListUrl, JSON.stringify(obj), before, after, error)
}

function addComment(weiBoId) {
    var content =  $("#commentContent").val();
    if(content){
        var obj = {};
        obj["weiBoId"] = weiBoId;
        obj["content"] = content;
        postWithToken(addCommentUrl,JSON.stringify(obj),
            function () {
                modalMessage('warring','正在回复...');
            },
            function (response) {
                if(response){
                    modalMessage('success','评论成功');
                    $('#commentContent').val('');
                    addOneCommentToModal(response,'prepend');
                }else {
                    modalMessage('error','评论失败！请稍后再试');
                }
            },
            function (errorMessage) {
                $('#commentPageBox .errorMessage').html(errorMessage);
            });
    }else {
        modalMessage('error','请输入评论内容！');
    }
}

function addOneCommentToModal(comment,position) {
    var faceUrl = "img/icon.png";
    if (comment.author && comment.author.face && comment.author.face.length > 0) {
        faceUrl = "upload/" + comment.author.face;
    }
    var div = '<div class="col-xs-12 comment-item">' +
            '<div class="col-xs-2 comment-author-header">' +
                '<img src="'+faceUrl+'" alt="头像" class="img-responsive img-circle author-face">' +
            '</div>' +
            '<div class="col-xs-10 comment-content">' +
                '<div>' +
                    '<a onclick="toUserIndex('+comment.author.userId+')"><strong><span>'+comment.author.nickname+'</span></strong></a>:&nbsp;<span>'+comment.commentContent+'</span>' +
                '</div>' +
                '<div class="clearfix comment-tools" >' +
                    '<div class="pull-right">' +
                        '<a ' +
                            ' data-commentid="'+comment.commentId+'" ' +
                            'onclick="commentLikes(this)"' +
                            '>赞('+comment.likesCount+')</a>' +
                    '</div>' +
                    '<div class="pull-left">' +
                        '<p class="text-muted"><small class="comment-time">'+comment.createTime+'</small></p>'+
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>';
    if(position && position === 'prepend'){
        $('#commentList').prepend(div);
    }else {
        $('#commentList').append(div);
    }
    $('#commentCollapse'+comment.commentId).on('show.bs.collapse',function (event) {
        var a = $(event.relatedTarget);
        var nickname = a.data('nickname');
        var commentId = a.data('commentid');
        var commentUserId = a.data('commentuserid');
        console.log(nickname+" "+commentId+" "+" "+commentUserId);
    });
}

function commentLikes(ele) {
    var a = $(ele);
    var commentId = a.data('commentid');
    getWithToken(likeCommentUrl+commentId,
        function () {
            a.attr('disabled','disabled');
            modalMessage('warring','点赞中...')
        },
        function (result) {
            if(result){
               a.html('取消赞('+result+')');
               a.attr('onclick','cancelCommentLikes(this)');
               a.removeAttr('disabled');
                modalMessage('success','点赞成功')
            }
        },function (errorMessage) {
            modalMessage('error',errorMessage)
        });
}
function cancelCommentLikes(ele) {
    var a = $(ele);
    var commentId = a.data('commentid');
    getWithToken(cancelLikeCommentUrl+commentId,
        function () {
            a.attr('disabled','disabled');
            modalMessage('warring','取消中...');
        },
        function (result) {
            a.html('赞('+(result?result:0)+')');
            a.attr('onclick','commentLikes(this)');
            a.removeAttr('disabled');
            modalMessage('success','取消成功')
        },function (errorMessage) {
            modalMessage('error',errorMessage)
        });
}

function getCommentBtn(name) {
    return '<div class="row">' +
        '<div class="col-xs-12">' +
        '<div class="input-group">' +
        '<input class="form-control commentContent" placeholder="回复@'+name+'" type="text" >' +
        '<span class="input-group-btn">' +
        '<button class="btn btn-warning commentBtn" type="button">回复</button>' +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
}

function modalMessage(type,message) {
    switch (type) {
        case 'success':
            $('#commentPageBox .errorMessage').html('');
            $('#commentPageBox .successMessage').html(message);
            $('#commentPageBox .warringMessage').html('');
            break;
        case 'warring':
            $('#commentPageBox .errorMessage').html('');
            $('#commentPageBox .successMessage').html('');
            $('#commentPageBox .warringMessage').html(message);
            break;
        case 'error':
            $('#commentPageBox .errorMessage').html(message);
            $('#commentPageBox .successMessage').html('');
            $('#commentPageBox .warringMessage').html('');
            break;
        default:
            $('#commentPageBox .errorMessage').html(message);
            $('#commentPageBox .successMessage').html('');
            $('#commentPageBox .warringMessage').html('');

    }
}

//打开转发界面
function openWeiboRePost(ele,weiBoId) {
    Swal.fire({
        title: '转发理由',
        input: "textarea",
        inputPlaceholder:"转发了",
        showCancelButton: true,
        cancelButtonText:'取消',
        confirmButtonText: '转发',
        showCloseButton: true,
    }).then((result) => {
        if(!result.dismiss){
            weiBoRePost(weiBoId,
                result.value,
                function () {},
                function (response) {
                    if(response){
                        toastr.info("转发成功");
                        refreshUserInfo(true);
                    }else {
                        toastr.error(errorMessage);
                    }
                },
                function (errorMessage) {
                    toastr.error(errorMessage);
                });
        }

    })
}
function weiBoRePost(weiBoId,content,before,after,error) {
    var obj = {};
    obj["weiBoId"] = weiBoId;
    if(content){
        obj['content'] = content;
    }
    postWithToken(rePostWeiboUrl,JSON.stringify(obj),before,after,error)
}

//收藏
function weiboCollect(ele,weiBoId) {
    var jqEle = $(ele);
    collectWeiBoById(weiBoId,
        function () {
        },function (result) {
            jqEle.attr('onclick','cancelCollect(this,'+weiBoId+')');
            jqEle.children("em").html('已收藏');
            jqEle.children(".countNumber").html(result);
            toastr.info("收藏成功");
        },function (errorMessage) {
            toastr.error(errorMessage);
        });
}
//取消收藏
function cancelCollect(ele,weiBoId,refreshList) {
    var jqEle = $(ele);
    getWithToken(cancelCollectUrl+weiBoId,
        function () {},
        function (result) {
            if(refreshList){
                refreshWeiBoList();
            }
            jqEle.attr('onclick','weiboCollect(this,'+weiBoId+')');
            jqEle.children("em").html('收藏');
            jqEle.children(".countNumber").html(result?result:0);
            toastr.info("移除成功");
        },function (errorMessage) {
            toastr.error(errorMessage);
        });
}

// 收藏一条微博
function collectWeiBoById(weiBoId,before,after,error) {
    getWithToken(collectWeiBoUrl+weiBoId,before,after,error)
}

//点赞
function weiboLikes(ele,weiBoId) {
    var jqEle = $(ele);
    likesWeiBoById(weiBoId,
        function () {
        },function (result) {
            jqEle.attr('onclick','cancelLikes(this,'+weiBoId+')');
            jqEle.children("em").html('取消赞');
            jqEle.children(".countNumber").html(result);
            toastr.info("点赞成功");
        },function (errorMessage) {
            toastr.error(errorMessage);
        });
}
//取消点赞
function cancelLikes(ele,weiBoId,refreshList) {
    var jqEle = $(ele);
    getWithToken(cancelLikesUrl+weiBoId,
        function () {},
        function (result) {
            if(refreshList){
                refreshWeiBoList();
            }
            jqEle.attr('onclick','weiboLikes(this,'+weiBoId+')');
            jqEle.children("em").html('点赞');
            jqEle.children(".countNumber").html(result?result:0);
            toastr.info("取消赞成功");
        },function (errorMessage) {
            toastr.error(errorMessage);
        });
}

// 给一条微博点赞
function likesWeiBoById(weiBoId,before,after,error) {
    getWithToken(likesWeiBoUrl+weiBoId,before,after,error)
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
        Swal.fire({
            icon: 'success',
            title: message,
            showConfirmButton: false,
            timer: 1500
        }).then(function () {
            window.location.href = 'login.html'
        });
    }else {
        window.location.href = 'login.html'
    }
}


// 去主页
function goWeiBoIndex(message, timeOut) {
    // 去主页
    if (message) {
        Swal.fire({
            icon: 'success',
            title: message,
            showConfirmButton: false,
            timer: timeOut
        }).then(function () {
            window.location.href = 'index.html'
        });
    }else {
        delayOrimmediatelyOp(function () {
            window.location.href = 'index.html'
        }, timeOut);
    }


}

// 页面跳转
function toPage(url, message, timeOut) {
    if (message) {
        Swal.fire({
            icon:"info",
            title: message,
            showConfirmButton: false,
            timer: timeOut
        }).then(function () {
            window.location.href = url
        });
    }else {
        delayOrimmediatelyOp(function () {
            window.location.href = url
        }, timeOut);
    }

}

//添加一条微博
function addOneWeiBo(data,type) {
    var div = '<div class="col-sm-12 col-xs-12 message" id="weiBoItem'+data.weiboId+'">' +
        '<div class="row">';
    div += '<div class="col-md-1 col-sm-2 col-xs-12" onclick="toUserIndex('+data.author.userId+')">\n' +
        '<img class="user-face" src="';
    div += data.author.face ? "upload/"+data.author.face : "img/icon.png";
    div +='" style="border-radius: 50%">\n' +
        '</div>\n' +
        '<div class="col-md-11 col-sm-10 col-xs-12">' +
            '<div class="row">\n' +
                '<div class="col-sm-6 col-xs-6">' +
                    '<span style="font-weight: bold;">'+data.author.nickname+'</span>' +
                '</div>';
        div +=  '<div class="col-sm-6 col-xs-6">\n' +
                    '<div class="pull-right">';
            if(data.author.userId === parseInt(handleLocalStorage("get",userIdStorageKey))){
                div +=  '<button type="button" onclick="toDeleteWeiBoById('+data.weiboId+')" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="删除">\n' +
                                '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>';
            }else if(handleLocalStorage("get",adminStorageKey)){
                div +=  '<button type="button" onclick="toDeleteWeiBoById('+data.weiboId+')" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="封禁此微博">\n' +
                                '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>封禁</button>';
            }
            if(type === 'collect'){
                div +=  '<button type="button" onclick="cancelCollect(this,'+data.weiboId+',true)" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="移除收藏夹">\n' +
                    '<span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>&nbsp;移除</button>';
            }
            if(data.discussionId){
                div +=  '<button type="button" onclick="toDiscussion(this,'+data.discussionId+')" class="btn btn-warring btn-xs" data-toggle="tooltip" data-placement="bottom" title="进入讨论">\n' +
                    '<span class="glyphicon glyphicon-fire" aria-hidden="true"></span>&nbsp;</button>';
            }
            div += '</div>'+
                '</div>'+
            '</div>\n' +
            '<div class="row">\n' +
                '<div class="col-sm-12 col-xs-12">' +
                    '<small class="date" style="color:#999">'+data.postTime+'</small>' +
                '</div>\n' +
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
                            '<small class="date" style="color:#999"><a onclick="weiboCollect(this,'+data.repostWeiBo.weiboId+')" class="first_item"><span class="glyphicon glyphicon-star-empty"></span><em>收藏</em>(<span class="countNumber">'+data.repostWeiBo.collectCount+'</span>)</a></small>' +
                            '<small class="date" style="color:#999"><a onclick="openWeiboRePost(this,'+data.repostWeiBo.weiboId+')"><span class="glyphicon glyphicon-share-alt"></span><em>转发</em>(<span class="countNumber">'+data.repostWeiBo.repostCount+'</span>)</a></small>\n' +
                            '<small class="date" style="color:#999"><a data-toggle="modal" data-target="#commentModal" data-weiboid="'+data.repostWeiBo.weiboId+'" data-backdrop="static" data-keyboard="false"><span class="glyphicon glyphicon-comment"></span><em>评论</em>(<span class="countNumber">'+data.repostWeiBo.commentCount+'</span>)</a></small>\n' +
                            '<small class="date" style="color:#999"><a onclick="weiboLikes(this,'+data.repostWeiBo.weiboId+')"><span class="glyphicon glyphicon-thumbs-up"></span><em>点赞</em>(<span class="countNumber">'+data.repostWeiBo.likesCount+'</span>)</a></small>\n' +
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
        '                                    <li class=""><a onclick="weiboCollect(this,'+data.weiboId+')" class="first_item"><span\n' +
        '                                            class="glyphicon glyphicon-star-empty"></span><em>收藏</em>(<span class="countNumber">'+data.collectCount+'</span>)</a></li>\n' +
        '                                    <li class=""><a onclick="openWeiboRePost(this,'+data.weiboId+')"><span\n' +
        '                                            class="glyphicon glyphicon-share-alt"></span><em>转发</em>(<span class="countNumber">'+data.repostCount+'</span>)</a></li>\n' +
        '                                    <li class=""><a data-toggle="modal" data-target="#commentModal" data-weiboid="'+data.weiboId+'" data-backdrop="static" data-keyboard="false"><span\n' +
        '                                            class="glyphicon glyphicon-comment"></span><em>评论</em>(<span class="countNumber">'+data.commentCount+'</span>)</a></li>\n' +
        '                                    <li class=""><a onclick="weiboLikes(this,'+data.weiboId+')"><span\n' +
        '                                            class="glyphicon glyphicon-thumbs-up"></span><em>点赞</em>(<span class="countNumber">'+data.likesCount+'</span>)</a></li>\n' +
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

//添加一个用户
function addOneUser(user){
    var div = '<div class="col-sm-4 col-xs-6 userOther">\n' +
        '                    <div class="thumbnail">\n' +
        '                        <div class="row">\n' +
        '                            <div class="col-xs-4 face" onclick="toUserIndex('+user.userId+')">\n';
    var faceUrl = "img/icon.png";
    if (user.face && user.face.length > 0) {
        faceUrl = "upload/" + user.face;
    }
    div+='                                <img class="img-thumbnail img-circle" src="'+faceUrl+'">'+
                                    '</div>\n' +
        '                            <div class="col-xs-8 userInfo">\n' +
        '                                <div class="nickname"> <span>'+user.username+'('+user.nickname+')</span></div>\n' +
        '                                <div class="user-tools">\n';
    var concernBtn = '<button type="button" onclick="toRelationUser('+user.userId+',1,function() {refreshUserInfo(true)})" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="关注" ><span class="glyphicon glyphicon-heart" aria-hidden="true"></span></button>';
    var cancelConcernBtn = '<button type="button" onclick="toRelationUser('+user.userId+',2,function() {refreshUserInfo(true)})" class="btn btn-warning btn-xs" data-toggle="tooltip" data-placement="bottom" title="取消关注"><span class="glyphicon glyphicon-heart-empty" aria-hidden="true"></span></button>\n';
    var blackListBtn = '<button type="button" onclick="toRelationUser('+user.userId+',3,function() {refreshUserInfo(true)})" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="bottom" title="拉黑"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>\n';
    var cancelBlackBtn = '<button type="button" onclick="toRelationUser('+user.userId+',4,function() {refreshUserInfo(true)})" class="btn btn-warning btn-xs" data-toggle="tooltip" data-placement="bottom" title="取消拉黑"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>\n';
    if(user.userId === parseInt(handleLocalStorage("get",userIdStorageKey))){
        div += "用户本人"
    }else {
        if(user.currentToThisRelation === 0){
            div += (concernBtn + blackListBtn);
        }else if(user.currentToThisRelation === 1){
            div += (cancelConcernBtn+blackListBtn);
        }else if(user.currentToThisRelation === 2){
            div += cancelBlackBtn;
        }
    }

    div+=
        '                                </div>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>';
    $("#weiBoItem").append(div);

    // 按钮提示插件
    $('[data-toggle="tooltip"]').tooltip();
}