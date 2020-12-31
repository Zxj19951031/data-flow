import axios from "axios";
import GETAWAY_API from "@/api/serviceApi";

// service 循环遍历输出不同的请求方法
let instance = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
    withCredentials: false,
    headers: {
        "Content-Type": "application/json;charset=utf-8",
    },
});

//请求拦截器
instance.interceptors.request.use(
    (config) => {
        //发起请求前做什么
        return config;
    },
    (err) => {
        //请求错误
        console.log(err)
    }
);

//响应拦截器
instance.interceptors.response.use(
    (res) => {
        //请求成功
        return res.data;
    },
    (err) => {
        // 请求错误
        const res = err.response;
        if (res) {
            switch (res.status) {
                case 401:
                    //认证错误处理  清除token信息并跳转到登录页面
                    localStorage.clear();
                    window.location.replace("/#/login")
                    break;
                case 502:
                    context.$message({
                        message: '服务器内部错误',
                        type: 'error'
                    });
                    break;
                default:
                    context.$message({
                        message: res.data.message,
                        type: 'error'
                    });
            }
        }
        return Promise.reject(err)
    }
);


let context = null;

const Http = {
    initContext(vue) {
        context = vue
    }
}; //包裹请求方法的容器

for (let key in GETAWAY_API) {
    let api = GETAWAY_API[key]; //url methods

    // async 避免进入回调地域
    Http[key] = async function ({
                                    params, // 请求参数  get：url ， put post patch (data), delete :url
                                    isFormData = false, // 是否是form-data请求
                                    path = {},//路径参数
                                }) {

        let newParams = {};
        let url = api.url;
        // content-type 是否是 form-data 的判断
        if (params && isFormData) {
            newParams = new FormData();
            for (let i in params) {
                newParams.append(i, params[i]);
            }
        } else {
            newParams = params;
        }
        if (path) {
            for (let pKey in path) {
                url = api.url.replace('{{' + pKey + '}}', path[pKey])
            }
        }

        // 不同请求的判断
        let response = {};
        if (
            api.method === "PUT" ||
            api.method === "POST" ||
            api.method === "PATCH"
        ) {
            response = await instance({
                method: api.method,
                url: url,
                data: newParams
            });
            return response;
        } else if (api.method === "DELETE" || api.method === "GET") {
            response = await instance({
                method: api.method,
                url: url,
                params: newParams
            });
            return response;

        }
    };
}

export default Http
