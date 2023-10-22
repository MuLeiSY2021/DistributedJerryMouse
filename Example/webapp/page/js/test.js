//请求测试按钮
/* var request_btn=document.getElementById("request_btn");
request_btn.style.boxShadow='none';
request_btn.addEventListener('click', ()=>{
    request_btn.style.boxShadow='0 0 5px white';
    request_btn.style.border='none';
    request_btn.style.backgroundColor='#00BFBF';
    window.location.href='request.html';
}); */
var msg = {
    "name": "mainJerryMouse",
    "description": "This is a distributed Jerry Mouse Master",
    "port": 10086,
    "slave_num": 1,
    "slave_name": [
        "Host1",
    ],
    "webapps_num": 3,
    "names": [
        "A jerry mouse dashboard",
        "Hello, World Application",
        "404 Error Processor"
    ],
    "descriptions": [
        "\n            This is a simple web application with a source code organization\n            based on the recommendations of the Application Developer's Guide.\n        ",
        "\n            This is a simple web application with a source code organization\n            based on the recommendations of the Application Developer's Guide.\n        ",
        "A 404 Error Response Page\nIt will return a 404 Error Response"
    ],
    "servlet_num": [
        4,
        6,
        1
    ],
    "servlet_info": [
        [
            [
                "MasterInfo",
                "pers.server.jerrymouse.monitor.MasterInfo",
                "/monitor/info"
            ],
            [
                "HashInfo",
                "pers.server.jerrymouse.monitor.HashInfo",
                "/monitor/hash"
            ],
            [
                "A jerry mouse dashboard File Processor",
                "pers.server.jerrymouse.servlet.specialsevlet.FileProcessServlet",
                "/monitor/FileProcessServlet"
            ],
            [
                "A jerry mouse dashboard Index Processor",
                "pers.server.jerrymouse.servlet.specialsevlet.IndexProcessServlet",
                "/monitor"
            ]
        ],
        [
            [
                "GetCheckServlet",
                "org.example.GetCheckServlet",
                "/example/get/check_box"
            ],
            [
                "GetFormServlet",
                "org.example.GetFormServlet",
                "/example/get/from"
            ],
            [
                "PostCheckServlet",
                "org.example.PostCheckServlet",
                "/example/post/check_box"
            ],
            [
                "PostFormServlet",
                "org.example.PostFormServlet",
                "/example/post/from"
            ],
            [
                "Hello, World Application File Processor",
                "pers.server.jerrymouse.servlet.specialsevlet.FileProcessServlet",
                "/example/FileProcessServlet"
            ],
            [
                "Hello, World Application Index Processor",
                "pers.server.jerrymouse.servlet.specialsevlet.IndexProcessServlet",
                "/example"
            ]
        ],
        [
            [
                "404 Processor",
                "pers.server.jerrymouse.servlet.specialsevlet.Status404Servlet",
                "Status404Servlet"
            ]
        ]
    ]
}
//先拿数据
var main = document.getElementById("main");
var main_p = main.getElementsByTagName('p');
var slave_name = [];
//servlet
var servlet_name = [];
var servlet_class = [];
var servlet_mapping = [];
//webapp
var names = [];
var descriptions = [];

function take() {
    /* let init_value=msg;
    //拿servlet
        for(var i=0;i<init_value.servlet_info.length;i++){
            for(var j=0;j<init_value.servlet_info[i].length;j++){
                servlet_name.push(init_value.servlet_info[i][j][0]);
                servlet_class.push(init_value.servlet_info[i][j][1]);
                servlet_mapping.push(init_value.servlet_info[i][j][2]);
            }
        }
        slave_name=init_value.slave_name;
        names=init_value.names;
        descriptions=init_value.descriptions;
        //渲染主机
        main_p[0].innerHTML='主机名：'+init_value.name;
        main_p[1].innerHTML='主机描述：'+init_value.description;
        main_p[2].innerHTML='port：'+init_value.port;
        //从机渲染页面
        masterFun(); 
        //渲染servlet
        servletFun(); 
        //渲染webapp
        webappFun(); */
    let xhr = new XMLHttpRequest();
    // methods：GET/POST请求方式等，url：请求地址，true异步（可为false同步）
    xhr.open("GET", "http://localhost:10086/monitor/info", true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.send();                                            // 发送
    xhr.onreadystatechange = function () {
        if (xhr.status == 200) {
            let init_value = JSON.parse(xhr.response);
            //拿servlet
            for (var i = 0; i < init_value.servlet_info.length; i++) {
                for (var j = 0; j < init_value.servlet_info[i].length; j++) {
                    servlet_name.push(init_value.servlet_info[i][j][0]);
                    servlet_class.push(init_value.servlet_info[i][j][1]);
                    servlet_mapping.push(init_value.servlet_info[i][j][2]);
                }
            }
            slave_name = init_value.slave_name;
            names = init_value.names;
            descriptions = init_value.descriptions;
            //渲染主机
            main_p[0].innerHTML = '主机名：' + init_value.name;
            main_p[1].innerHTML = '主机描述：' + init_value.description;
            main_p[2].innerHTML = 'port：' + init_value.port;
            //从机渲染页面
            masterFun();
            //渲染servlet
            servletFun();
            //渲染webapp
            webappFun();
            console.log(xhr.response);                 // 查看返回的数据(可输出 xhr 哈)
            //JSON.parse(xhr.response);                // 如果数据为字符串的对象，可转换一下
        } else if (xhr.status == 404) {                  // 失败，页面未找到
        }
    }

}

//分机

var urls = [
    'url1', 'url2', 'url3',
];
var cooikes = [
    'cooike1', 'cooike2', 'cooike3',
];

function masterFun() {
    //获取节点和容器
    var master_node = document.getElementById('master_node');
    var master_container = document.getElementById('master_container');
    //master_node
    //移除原本的那个节点
    master_node.parentNode.removeChild(master_node)
    //循环塞数据并复制节点进容器
    //先创建一个数组用于存储复制的节点
    var master_node_arr = [];
    for (var i = 0; i < slave_name.length; i++) {
        var master_node_clone = master_node.cloneNode(true);
        //把复制的节点放进数组
        master_node_arr.push(master_node_clone);
        var subs = master_node_clone.getElementsByTagName('p');
        subs[0].innerHTML = slave_name[i];
        master_container.appendChild(master_node_clone);
    }
    //分机点击获取url
    //监听从机节点的点击事件
    for (let i = 0; i < master_node_arr.length; i++) {
        master_node_arr[i].addEventListener('click', function (event) {
            alert(i)
            // 点击后发请求拿数据
            let xhr = new XMLHttpRequest();
            // methods：GET/POST请求方式等，url：请求地址，true异步（可为false同步）
            xhr.open("GET", "http://localhost:10086/monitor/hash?name=" + servlet_name[i], true);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.send();                                            // 发送
            xhr.onreadystatechange = function () {
                if (xhr.status == 200) {
                    let url_msg = JSON.parse(xhr.response);
                    urls = url_msg.urls;
                    cooikes = url_msg.cookies;
                    console.log('获取到urls' + url_msg.urls);
                    //渲染url
                    urlFun();
                    console.log(xhr.response);                 // 查看返回的数据(可输出 xhr 哈)
                    //JSON.parse(xhr.response);                // 如果数据为字符串的对象，可转换一下
                } else if (xhr.status == 404) {                  // 失败，页面未找到
                }
            }
        });
    }
}


//servlet
function servletFun() {
    //获取servlet节点和容器、以及各种元素
    var servlet_container = document.getElementById('servlet_container');
    var servlet_node = document.getElementById('servlet_node');
    // var servlet_name=[
    //     'servlet1','servlet2','servlet3',
    // ];
    // var servlet_class=[
    //     'class1','class2','class3',
    // ];
    // var servlet_mapping=[
    //     'mapping1','mapping2','mapping3',
    // ];
    //移除原本的那个节点
    servlet_node.parentNode.removeChild(servlet_node);
    //循环塞数据并复制节点进容器
    //先创建一个数组用于存储复制的节点
    var servlet_node_arr = [];
    for (var i = 0; i < servlet_name.length; i++) {
        var servlet_node_clone = servlet_node.cloneNode(true);
        var p_arr = servlet_node_clone.getElementsByTagName("p");
        p_arr[0].innerHTML = servlet_name[i];
        p_arr[1].innerHTML = servlet_class[i];
        //暂弃
        //p_arr[2].innerHTML=servlet_rootUrl[i];
        p_arr[2].innerHTML = servlet_mapping[i];
        //把复制的节点放进数组
        servlet_node_arr.push(servlet_node_clone);
        servlet_container.appendChild(servlet_node_clone);
    }
}


//webapp
function webappFun() {
    //获取webapp节点和容器、以及各种元素
    var webapp_container = document.getElementById('webapp_container');
    var webapp_node = document.getElementById('webapp_node');
    var webapp_desc = document.getElementById('webapp_desc');
    // var names=[
    //     'webapp1','webapp2','webapp3',
    // ];
    // var descriptions=[
    //     'descriptions1','descriptions2','descriptions3',
    // ];
    //移除原本的那个节点
    webapp_node.parentNode.removeChild(webapp_node);
    //循环塞数据并复制节点进容器
    //先创建一个数组用于存储复制的节点
    var webapp_node_arr = [];
    for (var i = 0; i < names.length; i++) {
        var webapp_node_clone = webapp_node.cloneNode(true);
        var p_arr = webapp_node_clone.getElementsByTagName("button");
        p_arr[0].innerHTML = names[i];
        //把复制的节点放进数组
        webapp_node_arr.push(webapp_node_clone);
        webapp_container.appendChild(webapp_node_clone);
    }
    for (let i = 0; i < webapp_node_arr.length; i++) {
        webapp_node_arr[i].addEventListener('click', function (event) {
            webapp_desc.innerHTML = descriptions[i]
        });
    }
}


//url页面
function urlFun() {
    var url_container = document.getElementById('url_container');
    var url_node = document.getElementById('url_node');
    // var urls=[
    //     'url1','url2','url3',
    // ];
    // var cooikes=[
    //     'cooike1','cooike2','cooike3',
    // ];
    //塞数据
    //移除原本的那个节点
    url_node.parentNode.removeChild(url_node);
    //循环塞数据并复制节点进容器
    //先创建一个数组用于存储复制的节点
    var url_node_arr = [];
    for (var i = 0; i < urls.length; i++) {
        var url_node_clone = url_node.cloneNode(true);
        let p_arr = url_node_clone.getElementsByTagName("p");
        p_arr[0].innerHTML = "url : " + urls[i];
        p_arr[1].innerHTML = "cooike : " + cooikes[i];
        //把复制的节点放进数组
        url_node_arr.push(url_node_clone);
        url_container.appendChild(url_node_clone);
    }
}