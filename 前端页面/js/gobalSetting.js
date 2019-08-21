toastr.options = {
    closeButton: false,
    debug: false,
    progressBar: false,
    positionClass: "toast-top-center",
    onclick: null,
    showDuration: "9000",
    hideDuration: "300",
    timeOut: "10000",
    extendedTimeOut: "2000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};  
window.instanceAxios = null;
function redt() {
    toastr.error("您当前未登录~");
    setTimeout(function () {
        window.location.href = '/';
    }, 1000);
}
function getAtRqUrl() {
    return "/";
    //return "https://at.lovelywhite.cn/server/";
}
function getAxios() {
    return axios.create(
        {
            withCredentials: true,
            validateStatus: function (status) {
                if (status === 400) {
                    redt();
                }
                return status >= 200 && status < 300;
            },
        });
}
function getWsAddress() {
    // return "ws://192.168.123.65/webSocket/"
    return "ws://localhost/webSocket/"
}
function logout()
{
    getAxios().get(getAtRqUrl() + 'logout').then((response) => {
        toastr.error("退出成功~")
        setTimeout(function () {
            window.location.href = '/';
        }, 2000);
    });
}
function rad(d)
{
    return d * Math.PI / 180.0;
}
function GetDistance( lat1,  lng1,  lat2,  lng2)
{
    var radLat1 = rad(lat1);
    var radLat2 = rad(lat2);
    var a = radLat1 - radLat2;
    var  b = rad(lng1) - rad(lng2);
    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
    s = s *6378.137 ;
    s = Math.round(s * 10000) / 10;
    return s;
    //m
}
