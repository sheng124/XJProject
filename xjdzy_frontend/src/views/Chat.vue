<template>
  <div>
    <article
      class="message column is-two-fifths"
      v-bind:class="{ ' is-info': message.code == 1 }"
      v-for="(message, index) in messageList"
      :key="index"
    >
      <div class="message-body">
        <div>
          <strong>code:{{ message.code }}</strong>
        </div>
        <div>
          <strong>userId:{{ message.userId }}</strong>
        </div>
        <div>
          <strong>content:{{ message.content }}</strong>
        </div>
        <div>
          <strong>contentType:{{ message.contentType }}</strong>
        </div>
        <div>
          <strong>sendTime:{{ message.sendTime }}</strong>
        </div>
        <div>
          <strong>isread:{{ message.read }}</strong>
        </div>
      </div>
    </article>
    <el-input v-model="sendMsg" placeholder="发送的消息"></el-input>
    <el-input v-model="sendUserId" placeholder="对方ID"></el-input>
    <el-button type="primary" @click="sendMessage(sendUserId, sendMsg)"
      >提交</el-button
    >
  </div>
</template>
  
<script>
import { mapGetters } from "vuex";

export default {
    name: "Chat",
    data() {
        return {
            ws: null,
            messageList:[],
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
    destroyed(){
        console.log("用户下线！")
        // 关闭连接
        this.ws.close()
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
                var msgInfo = JSON.parse(message.data)
                console.log(msgInfo)
                console.log(this.ws.readyState)
                if(msgInfo.code === 0){
                    // 修改已经收到的消息状态
                    for(var i=0;i<this.messageList.length;i++){
                        if(this.messageList[i].code === 1 && this.messageList[i].userId === msgInfo.userId){
                            this.messageList[i].read = true
                        }
                    }
                } else{
                    this.messageList.push(msgInfo)
                }
                console.log(this.messageList)
            }
            this.ws.onerror = (error) => {
                console.log("websocket错误!")
                console.log(error)
                console.log(this.ws.readyState)
            }
        },
        
        sendMessage(userId,message){
            var msg = {
                "code":1,
                "userId":Number(userId),
                "content":message,
                "contentType":0,
                "read":false,
                "sendTime":this.getNowTime(),
            }
            this.ws.send(JSON.stringify(msg))
            this.messageList.push(msg)
        },
        getNowTime: function () {
            let dateTime;
            let yy = new Date().getFullYear();
            let mm =
                new Date().getMonth() + 1 < 10
                ? "0" + (new Date().getMonth() + 1)
                : new Date().getMonth() + 1;
            let dd =
                new Date().getDate() < 10
                ? "0" + new Date().getDate()
                : new Date().getDate();
            let hh =
                new Date().getHours() < 10
                ? "0" + new Date().getHours()
                : new Date().getHours();
            let mf =
                new Date().getMinutes() < 10
                ? "0" + new Date().getMinutes()
                : new Date().getMinutes();
            let ss =
                new Date().getSeconds() < 10
                ? "0" + new Date().getSeconds()
                : new Date().getSeconds();
            dateTime =
                yy + "-" + mm + "-" + dd + "T" + hh + ":" + mf + ":" + ss + ".000Z";
            console.log(dateTime);
            return dateTime;
        },
    }
}
</script>
  