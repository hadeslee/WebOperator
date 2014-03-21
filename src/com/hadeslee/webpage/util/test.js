String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/,"")
};
var sys={
    $:function(A){
        return document.getElementById(A)
    },
    onload:function(B,C){
        var A=window.onload;
        window.onload=function(){
            if(C){
                if(typeof (B)=="function"){
                    B()
                }
                if(typeof (A)=="function"){
                    A()
                }
            }
            else{
                if(typeof (A)=="function"){
                    A()
                }
                if(typeof (B)=="function"){
                    B()
                }
            }
        }
    },
    getQueryValue:function(B,E){
        var D="&"+window.location.search.replace(/^\?/g,"");
        if(E){
            D="&"+Eo
        }
        var A="";
        var C="&"+B+"=";
        if(D.length>0){
            offset=D.indexOf(C);
            if(offset!=-1){
                offset+=C.length;
                end=D.indexOf("&",offset);
                if(end==-1){
                    end=D.length
                }
                A=unescape(D.substring(offset,end))
            }
        }
        return A
    }
};
function getCookie(B){
    var A=document.cookie.match(new RegExp("(^| )"+B+"=([^;]*)(;|$)"));
    if(A!=null){
        return A[2]
    }
    return null
}
function setCookie(C,E){
    var A=setCookie.arguments;
    var H=setCookie.arguments.length;
    var B=(2<H)?A[2]:null;
    var G=(3<H)?A[3]:null;
    var D=(4<H)?A[4]:null;
    var F=(5<H)?A[5]:null;
    document.cookie=C+"="+escape(E)+((B==null)?" ":(";expires ="+B.toGMTString()))+((G==null)?"  ":(";path = "+G))+((D==null)?" ":(";domain ="+D))+((F==true)?";secure":" ")
}
var pt={
    isHttps:false,
    err_m:null,
    mibao_css:"",
    init:function(){
        try{
            pt.isHttps=(/^https/g.test(window.parent.location+""))
        }catch(A){}
        sys.onload(function(){
            pt.err_m=sys.$("err_m")
        });
        pt.mibao_css=sys.getQueryValue("mibao_css")
    },
    show_err:function(A){
        if(pt.err_m&&(typeof ptui_notifySize=="function"))

        {
            pt.err_m.innerHTML=A;
            pt.err_m.style.display="block";
            ptui_notifySize("login");
            return
        }
        else{
            alert(A)
        }
    },
    chkUin:function(qquin){
        qquin=qquin.trim();
        if(qquin.length==0){
            return false
        }
        if(window.location.hostname.match(/paipai.com$/))
        {
            if(qquin.length<64&&(
                new RegExp(/^[A-Za-z0-9]+@{1}[A-Za-z0-9]+$/).test(qquin)))
                {
                return true
            }
        }
        if((new RegExp(/^[a-zA-Z]{1}([a-zA-Z0-9]|[-_]){0,19}$/).test(qquin)))
        {
            if(t_appid==g_appid){
                return true
            }else{
                $("u").value=$("u").value.trim()+"@qq.com";
                qquin=$("u").value
            }
        }
        if(!(new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(qquin)))
        {
            if(qquin.length<5||qquin.length>12||parseInt(qquin)<1000){
                return false
            }
            var exp=eval("/^[0-9]*$/");
            return exp.test(qquin)
        }
        return true
    }
};
pt.init();
var vc_type="";
var lastUin=1;
var t_appid=46000101;
var g_changeNum=0;
var g_checkTime=0;
var g_imgTime=0;
var first=true;
var changeimg=false;
var defaultuin="";
var login_param=g_href.substring(g_href.indexOf("?")+1);
function ptui_onEnableLLogin(B){
    var A=B.low_login_enable;
    var C=B.low_login_hour;
    if(A!=null&&C!=null){
        C.disabled=!A.checked
    }
}
function ptui_setDefUin(C,B){
    if(B==""||B==null){
        B=getCookie("ptui_loginuin");
        if(B){
            defaultuin=B
        }
    }
    if(g_appid==t_appid){
        var A=getCookie("ptui_loginuin2");
        if(A){
            B=A;
            defaultuin=B
        }
    }
    if(B!=""&&B!=null){
        C.u.value=B
    }
}
var g_ptredirect=-1;
var g_xmlhttp;
var g_loadcheck=true;
var g_submitting=false;
function ptui_needVC(C,D){
    if(t_appid==D){
        if((C.indexOf("@")<0)&&isNaN(C)){
            C="@"+C
        }
    }
    var B="";
    if(pt.isHttps){
        ptui_checkVC("1","");
        return
    }
    else{
        B="http://ptlogin2."+g_domain+"/check?uin="+C+"&appid="+D+"&r="+Math.random()
    }
    var A=document.createElement("script");
    g_imgTime=new Date();
    A.src=B;
    document.body.appendChild(A);
    g_loadcheck=true;
    return
}
function ptui_checkVC(A,B){
    g_loadcheck=false;
    g_checkTime=new Date().getTime()-g_checkTime;
    if(g_submitting){
        return
    }
    var D=new Date();
    if(defaultuin!=""&&g_changeNum<=1){
        g_time.time7=D;
        var C={
            "12":g_time.time7-g_time.time6
        };
        if(defaultuin!=""){
            C["16"]=g_time.time6-g_time.time3,
            C["17"]=g_time.time7-g_time.time3
        }
        if(!xuiFrame){
            ptui_speedReport(C)
        }
    }else{
        g_time.time10=D;
        var C={
            "13":g_time.time10-g_time.time9
        };
        ptui_speedReport(C)
    }
    if(pt.isHttps){
        A=1
    }
    if(A=="0"){
        $("verifycode").value=B;
    }
}
function ptui_onLoginEx(B,C){
    g_time.time12=new Date();
    if(ptui_onLogin(B)){
        var A=new Date();
        A.setHours(A.getHours()+24*30);
        if((g_appid==t_appid)&&isNaN(B.u.value)&&(B.u.value.indexOf("@")<0)){
            setCookie("ptui_loginuin2",B.u.value,A,"/","ui.ptlogin2."+C)
            }
        else{
            setCookie("ptui_loginuin",B.u.value,A,"/","ui.ptlogin2."+C)
        }
        if(pt.isHttps){
            return true
        }
    }
    return false
    }
