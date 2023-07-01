<template>
    <div>
        <article class="message column is-two-fifths" v-for="(message,index) in recMessageList" :key="index" >
            <div class="message-body">
                <div><strong>来自：{{ message.userId }}</strong></div>
                <div><strong>消息内容{{ message.content }}</strong></div>
                <div><strong>发送时间{{ message.sendTime }}</strong></div>
            </div>
        </article>
        <article class="message is-info column is-two-fifths" v-for="(message,index) in sendMessageList" :key="index" >
            <div class="message-body">
                <strong>{{ message.userId }}</strong>
                <strong>{{ message.content }}</strong>
            </div>
        </article>
        <el-input v-model="sendMsg" placeholder="发送的消息"></el-input>
        <el-input v-model="sendUserId" placeholder="对方ID"></el-input>
        <el-button type="primary" @click="sendMessage(sendUserId,sendMsg)">提交</el-button>
    </div>
</template>
  
<script>
import { mapGetters } from "vuex";

export default {
    name: "Chat",
    data() {
        return {
            ws: null,
            recMessageList:[],
            sendMessageList:[],
            sendMsg:'',
            sendUserId:-1
        }
    },
    computed: {
    ...mapGetters(["user", "token"]),
    },
    created() {
        //初始化websocket
        this.ws = new WebSocket("ws://localhost:8080/chat/"+this.user.userId);
        this.wsInit();
    },
    methods: {
        wsInit() {
            this.ws.onopen = () => {
                console.log("服务器连接")
                console.log(this.ws.readyState)
            }
            this.ws.onclose = () => {
                console.log("服务器关闭")
                console.log(this.ws.readyState)
            }
            this.ws.onmessage = (message) => {
                console.log("收到服务器消息")
                console.log(JSON.parse(message.data))
                console.log(this.ws.readyState)
                this.recMessageList.push(JSON.parse(message.data))
            }
            this.ws.onerror = (error) => {
                console.log("websocket错误!")
                console.log(error)
                console.log(this.ws.readyState)
            }
        },
        sendMessage(userId,message){
            var msg = {
                "userId":userId,
                "content":message
            }
            this.ws.send(JSON.stringify(msg))
            this.sendMessageList.push(msg)
        }
    }
}
</script>
  