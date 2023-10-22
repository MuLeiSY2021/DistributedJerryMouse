//获取主机节点
var main = document.getElementById("main");
var main_p = main.getElementsByTagName('p');

//定义数据
// var main_name='';
// var main_description='';
// var main_port='';
var slaves_name = [];
var webapps_name = [];
var slaves_status = [];
//模拟初始化页面加载数据
var msg = {
    "name": "mainJerryMouse",
    "description": "This is a distributed Jerry Mouse Master",
    "port": 10086,
    "slaves_status": [true, false, true, true, false, false],
    "slaves_name": [
        "Host1", "Host2", "Host1", "Host2", "Host1", "Host2",
    ],
    "webapps_name": [
        "Monitor",
        "Hello, World Application",
        "404 Error Processor",
        "Monitor",
        "Hello, World Application",
        "404 Error Processor",
        "Monitor",
        "Hello, World Application",
        "404 Error Processor",
        "Monitor",
        "Hello, World Application",
        "Monitor",
        "Hello, World Application",
        "404 Error Processor",
        "Monitor",
        "Hello, World Application",
    ]
}

//页面加载完成执行函数发请求拿数据
function take() {
    /* //模拟收发请求
    let init_value=msg;
    //接收数据
    // main_name=init_value.name;
    // main_port=init_value.port;
    // main_description=init_value.description;
    slaves_name=init_value.slaves_name;
    webapps_name=init_value.webapps_name;
    slaves_status=init_value.slaves_status;
    //渲染主机
    main_p[0].innerHTML='<i>主机名：</i>'+init_value.name;
    main_p[1].innerHTML='<i>port：</i>'+init_value.port;
    main_p[2].innerHTML='<i>主机描述：</i>'+init_value.description;
    //渲染webapp按钮和slave按钮
    webapp_slave_fun(); */


    let xhr = new XMLHttpRequest();
    // methods：GET/POST请求方式等，url：请求地址，true异步（可为false同步）
    xhr.open("GET", "http://localhost:10086/monitor/info", true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.send();                                            // 发送
    xhr.onreadystatechange = function () {
        if (xhr.status == 200) {
            let init_value = JSON.parse(xhr.response);
            //接收数据
            // main_name=init_value.name;
            // main_port=init_value.port;
            // main_description=init_value.description;
            slaves_name = init_value.slaves_name;
            webapps_name = init_value.webapps_name;
            slaves_status = init_value.slaves_status;
            //渲染主机
            main_p[0].innerHTML = '主机名：' + init_value.name;
            main_p[1].innerHTML = 'port：' + init_value.port;
            main_p[2].innerHTML = '主机描述：' + init_value.description;
            //渲染webapp按钮和slave按钮
            webapp_slave_fun();
            console.log('/monitor/info请求成功');
            console.log(xhr.response);
            //JSON.parse(xhr.response);                
        } else {
            alert('/monitor/info请求失败')
        }
    }
}

//模拟webapp数据
var servlet_msg = {
    "description": "服务描述测试数据",
    "servlet_name": [
        "MasterInfo",
        "HashInfo",
        "Monitor File Processor",
        "Monitor Index Processor"
    ],
    "servlet_class": [
        "pers.server.jerrymouse.monitor.MasterInfo",
        "pers.server.jerrymouse.monitor.HashInfo",
        "pers.server.jerrymouse.servlet.specialsevlet.FileProcessServlet",
        "pers.server.jerrymouse.servlet.specialsevlet.IndexProcessServlet"
    ],
    "servlet_mapping": [
        "/monitor/info",
        "/monitor/hash",
        "/monitor/FileProcessServlet",
        "/monitor"
    ]
}
var servlet_name = [];
var servlet_class = [];
var servlet_mapping = [];
var webapp_descript = document.getElementById('webapp_descript');
//模拟slave数据
var slave_msg = {
    "mapping_num": 3,
    "urls": [
        "/example",
        "/example/get_form.html",
        "/example/get/from"
    ],
    "cookies": [
        "639c03b4-bb0d-4e64-8926-e1d8a901a094",
        "573a2927-32e6-4f01-abc7-fc2a5d32088c",
        "e0342bcd-8ef4-44c7-8d3c-cb8c4b1cbe4b"
    ],
    "workdir": "MainJerrymouseDir/webapps/Monitor",
    "mapping": "/monitor/info",
    "name": "MasterInfo",
    "clazz": "pers.server.jerrymouse.monitor.MasterInfo"
}
var urls = [];
var cooikes = [];

//渲染webapp和slave
function webapp_slave_fun() {
    //获取web和slave节点
    var webapp_container = document.getElementById("webapp_container");
    var slave_container = document.getElementById("slave_container");
    var webapp_node = document.getElementById("webapp_node");
    var slave_node = document.getElementById("slave_node");
    //移除原本的那个节点
    webapp_node.parentNode.removeChild(webapp_node);
    var webapp_node_first = webapp_container.firstElementChild;
    while (webapp_node_first) {
        // console.log(first);
        webapp_node_first.remove();
        webapp_node_first = webapp_container.firstElementChild;
    }
    slave_node.parentNode.removeChild(slave_node);
    var slave_node_first = slave_container.firstElementChild;
    while (slave_node_first) {
        // console.log(first);
        slave_node_first.remove();
        slave_node_first = slave_container.firstElementChild;
    }
    //循环塞数据并复制节点进容器
    //先创建数组用于存储复制的节点,用于之后的点击事件
    var webapp_node_arr = [];
    var slave_node_arr = [];
    //复制webapp节点
    for (var i = 0; i < webapps_name.length; i++) {
        var webapp_node_clone = webapp_node.cloneNode(true);
        webapp_node_clone.innerHTML = webapps_name[i];
        //把复制的节点放进数组
        webapp_node_arr.push(webapp_node_clone);
        //复制的节点塞进容器
        webapp_container.appendChild(webapp_node_clone);
    }
    //复制slave节点
    for (var i = 0; i < slaves_name.length; i++) {
        var slave_node_clone = slave_node.cloneNode(true);
        var slave_node_p = slave_node_clone.getElementsByTagName('p');
        slave_node_p[0].innerHTML = slaves_name[i];
        if (slaves_status[i]) {
            slave_node_p[1].style = 'background-color:#27e887;';
            slave_node_p[2].innerHTML = 'is Alive';
        } else {
            slave_node_p[1].style = 'background-color:gray;';
            slave_node_p[2].innerHTML = 'is Dead';
        }


        // slave_node_p[1].innerHTML=slaves_name[i];
        //把复制的节点放进数组
        slave_node_arr.push(slave_node_clone);
        //复制的节点塞进容器
        slave_container.appendChild(slave_node_clone);
    }
    //监听webapp节点点击事件
    for (let i = 0; i < webapp_node_arr.length; i++) {
        webapp_node_arr[i].addEventListener('click', function (event) {
            //获取webapp详细信息的节点
            var webapp_name = document.getElementById('webapp_name');
            webapp_name.innerHTML = '<i>webapp名称：<i/>' + webapps_name[i]
            /*  //模拟请求成功
             servlet_name=servlet_msg.servlet_name;
             servlet_class=servlet_msg.servlet_class;
             servlet_mapping=servlet_msg.servlet_mapping;
             webapp_descript.innerHTML=servlet_msg.description;
             //渲染webapp
             webappMsgFun(); */

            // 点击后发请求拿数据
            let xhr = new XMLHttpRequest();
            // methods：GET/POST请求方式等，url：请求地址，true异步（可为false同步）
            xhr.open("GET", "http://localhost:10086/monitor/webapps?name=" + webapps_name[i], true);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.send();                                            // 发送
            xhr.onreadystatechange = function () {
                if (xhr.status == 200) {
                    //让隐藏的节点属性显示出来
                    let servlet_node = document.getElementById('servlet_node');
                    servlet_node.style = 'display:inline-flex';
                    let servlet_msg = JSON.parse(xhr.response);
                    servlet_name = servlet_msg.servlet_name;
                    servlet_class = servlet_msg.servlet_class;
                    servlet_mapping = servlet_msg.servlet_mapping;
                    webapp_descript.innerHTML = servlet_msg.description;
                    //渲染webapp
                    webappMsgFun();
                    console.log('/monitor/webapps请求成功');
                    console.log(xhr.response);
                    //JSON.parse(xhr.response);
                } else {
                    alert('/monitor/webapps请求失败');
                }
            }
        });
    }
    //监听slave节点点击事件
    for (let i = 0; i < slave_node_arr.length; i++) {
        slave_node_arr[i].addEventListener('click', function (event) {
            /* //模拟接收请求数据
            urls=slave_msg.urls;
            cooikes=slave_msg.cookies;
            //渲染url
            urlFun(); */

            // 点击后发请求拿数据
            let xhr = new XMLHttpRequest();
            // methods：GET/POST请求方式等，url：请求地址，true异步（可为false同步）
            xhr.open("GET", "http://localhost:10086/monitor/hash?name=" + slaves_name[i], true);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.send();                                            // 发送
            xhr.onreadystatechange = function () {
                if (xhr.status == 200) {
                    let slave_msg = JSON.parse(xhr.response);
                    urls = slave_msg.urls;
                    cooikes = slave_msg.cookies;
                    if (slave_msg.urls.length === 0) {
                        alert('暂无数据')
                        return;
                    }
                    //渲染url
                    urlFun();
                    console.log('/monitor/hash请求成功');
                    console.log(xhr.response);
                    //JSON.parse(xhr.response);
                } else {
                    alert('/monitor/hash请求失败');
                }
            }
        });
    }
}


// 渲染webapp详细信息
function webappMsgFun() {
    //获取web和slave节点
    var servlet_container = document.getElementById("servlet_container");
    var servlet_node = document.getElementById("servlet_node");
    //移除原本的那个节点
    servlet_node.parentNode.removeChild(servlet_node);
    var first = servlet_container.firstElementChild;
    while (first) {
        // console.log(first);
        first.remove();
        first = servlet_container.firstElementChild;
    }
    //循环塞数据并复制节点进容器
    //先创建数组用于存储复制的节点,用于之后的点击事件
    var servlet_node_arr = [];
    //复制webapp节点
    for (var i = 0; i < servlet_name.length; i++) {
        var servlet_node_clone = servlet_node.cloneNode(true);
        var servlet_node_p = servlet_node_clone.getElementsByTagName('p')
        servlet_node_p[0].innerHTML = servlet_name[i]
        servlet_node_p[1].innerHTML = servlet_class[i]
        servlet_node_p[2].innerHTML = servlet_mapping[i]
        //把复制的节点放进数组
        servlet_node_arr.push(servlet_node_clone);
        //复制的节点塞进容器
        servlet_container.appendChild(servlet_node_clone);
    }
}


//渲染url详细信息
function urlFun() {
    let url_container = document.getElementById('url_container');
    let url_node = document.getElementById('url_node');
    //塞数据
    //移除原本的那个节点
    url_node.parentNode.removeChild(url_node);
    var first = url_container.firstElementChild;
    while (first) {
        // console.log(first);
        first.remove();
        first = url_container.firstElementChild;
    }
    //循环塞数据并复制节点进容器
    //先创建一个数组用于存储复制的节点
    var url_node_arr = [];
    for (let i = 0; i < urls.length; i++) {
        let url_node_clone = url_node.cloneNode(true);
        let p_arr = url_node_clone.getElementsByTagName("p");
        p_arr[0].innerHTML = "url : " + urls[i];
        p_arr[1].innerHTML = "cooike : " + cooikes[i];
        //把复制的节点放进数组
        url_node_arr.push(url_node_clone);
        url_container.appendChild(url_node_clone);
    }
}