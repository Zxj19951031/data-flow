import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import VueRouter from './plugins/router.js'
import Http from "@/api/http";

Vue.config.productionTip = false
Vue.prototype.$http = Http

const vue = new Vue({
    render: h => h(App),
    router: VueRouter
}).$mount('#app');
Vue.prototype.$http.initContext(vue)

