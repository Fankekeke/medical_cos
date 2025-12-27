<template>
  <div class="ai-container">
    <div class="header">
      <h1>AI智能问诊</h1>
      <p class="subtitle">为您提供专业的健康咨询服务</p>
    </div>

    <div class="chat-box">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.type]">
        <div class="avatar">
          <img :src="message.avatar" :alt="message.type === 'bot' ? '医生头像' : '用户头像'" />
        </div>
        <div class="content">
          <vue-markdown :source="message.text" class="message-text"></vue-markdown>
          <span class="timestamp">{{ message.timestamp }}</span>
        </div>
      </div>
    </div>

    <div class="input-area">
      <div class="input-wrapper">
        <input
          v-model="userInput"
          @keyup.enter="sendMessage"
          placeholder="描述您的症状，如：头疼、发烧..."
          class="message-input"
        />
        <button @click="sendMessage" class="send-button">
          <span>发送</span>
          <i class="send-icon">➤</i>
        </button>
      </div>
      <div class="quick-tips">
        <span class="tip-title">常见问题：</span>
        <span class="quick-question" @click="selectQuestion('头疼怎么办？')">头疼</span>
        <span class="quick-question" @click="selectQuestion('发烧了怎么处理？')">发烧</span>
        <span class="quick-question" @click="selectQuestion('咳嗽有痰如何缓解？')">咳嗽</span>
      </div>
    </div>
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
export default {
  components: {
    VueMarkdown
  },
  data () {
    return {
      userInput: '',
      messages: [
        {
          type: 'bot',
          avatar: 'http://127.0.0.1:9527/imagesWeb/SA1675604990128.jpg',
          text: '您好！有什么我可以帮您的吗？',
          timestamp: this.getFormattedTime()
        }
      ]
    }
  },
  methods: {
    selectQuestion(question) {
      this.userInput = question
      this.sendMessage()
    },
    sendMessage () {
      if (this.userInput.trim() === '') return

      // 用户消息
      this.messages.push({
        type: 'user',
        avatar: 'http://127.0.0.1:9527/imagesWeb/SA1675604212612.jpg',
        text: this.userInput,
        timestamp: this.getFormattedTime()
      })
      // 模拟AI回复
      this.generateResponse(this.userInput)

      // 清空输入框
      this.userInput = ''
      // setTimeout(() => {
      //   this.messages.push({
      //     type: 'bot',
      //     avatar: 'http://127.0.0.1:9527/imagesWeb/SA1675604990128.jpg',
      //     text: '这是AI的回复：' + this.generateResponse(),
      //     timestamp: this.getFormattedTime()
      //   })
      // }, 1000)
    },
    getFormattedTime () {
      const now = new Date()
      return now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    },
    generateResponse (content) {
      this.messages.push({
        type: 'bot',
        avatar: 'http://127.0.0.1:9527/imagesWeb/SA1675604990128.jpg',
        text: '请稍后 正在加载中😋',
        timestamp: this.getFormattedTime()
      })
      this.$post(`/cos/ai/aliTyqw`, {
        content: content
      }).then((r) => {
        this.messages.push({
          type: 'bot',
          avatar: 'http://127.0.0.1:9527/imagesWeb/SA1675604990128.jpg',
          text: r.data.msg,
          timestamp: this.getFormattedTime()
        })
      })
      // const responses = [
      //   '好的，我会帮您查找相关信息。',
      //   '请问您需要了解哪方面的内容？',
      //   '感谢您的提问，我会尽力提供帮助。'
      // ]
      // return responses[Math.floor(Math.random() * responses.length)]
    }
  }
}
</script>

<style scoped>.ai-container {
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  min-height: 100vh;
  box-sizing: border-box;
}

.header {
  text-align: center;
  margin-bottom: 25px;
  padding: 15px 0;
}

.header h1 {
  color: #1890ff;
  margin-bottom: 10px;
  font-weight: 600;
  font-size: 32px;
  position: relative;
  display: inline-block;
}

.header h1::after {
  content: '';
  display: block;
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #1890ff, #40a9ff);
  margin: 10px auto 0;
  border-radius: 2px;
}

.subtitle {
  color: #69c0ff;
  font-size: 16px;
  margin-top: 8px;
  font-weight: 400;
}

.chat-box {
  background-color: #ffffff;
  border-radius: 20px;
  padding: 25px;
  height: 65vh;
  overflow-y: auto;
  margin-bottom: 25px;
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.15);
  border: 1px solid #e8f4ff;
}

.message {
  display: flex;
  margin-bottom: 25px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message.user {
  justify-content: flex-end;
}

.message.bot {
  justify-content: flex-start;
}

.avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
  box-shadow: 0 3px 8px rgba(0,0,0,0.12);
  border: 2px solid #ffffff;
}

.message.user .avatar {
  order: 1;
  margin-right: 0;
  margin-left: 15px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content {
  background-color: #f0f8ff;
  border-radius: 20px;
  padding: 16px 20px;
  max-width: 75%;
  box-shadow: 0 3px 8px rgba(0,0,0,0.08);
  position: relative;
  word-wrap: break-word;
  line-height: 1.6;
  border: 1px solid #e6f7ff;
}

.message.bot .content {
  background-color: #ffffff;
  border: 1px solid #e8f4ff;
  border-top-left-radius: 8px;
  color: #333;
}

.message.user .content {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  color: white;
  border-top-right-radius: 8px;
}

.message.bot .content::before {
  content: '';
  position: absolute;
  left: -10px;
  top: 15px;
  width: 0;
  height: 0;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
  border-right: 10px solid #ffffff;
}

.message.user .content::before {
  content: '';
  position: absolute;
  right: -10px;
  top: 15px;
  width: 0;
  height: 0;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
  border-left: 10px solid #1890ff;
}

.timestamp {
  font-size: 0.75em;
  color: #888;
  display: block;
  margin-top: 8px;
  text-align: right;
  opacity: 0.8;
}

.input-area {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
  background: white;
  border-radius: 22px;
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.2);
  border: 1px solid #e8f4ff;
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: center;
}

.input-area input {
  flex-grow: 1;
  padding: 14px 22px;
  border: 2px solid #d9f0ff;
  border-radius: 28px;
  outline: none;
  font-size: 15px;
  background-color: #f8fdff;
  transition: all 0.3s;
  height: 50px;
  box-sizing: border-box;
}

.input-area input:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.25);
  background-color: #ffffff;
}

.input-area button {
  padding: 10px 24px;
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
  box-shadow: 0 4px 10px rgba(24, 144, 255, 0.35);
  height: 50px;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 15px;
}

.input-area button:hover {
  background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
  box-shadow: 0 6px 14px rgba(24, 144, 255, 0.45);
  transform: translateY(-2px);
}

.input-area button:active {
  transform: translateY(0);
  box-shadow: 0 2px 6px rgba(24, 144, 255, 0.3);
}

.send-icon {
  font-size: 14px;
  font-style: normal;
  font-weight: bold;
}

.quick-tips {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  padding-left: 10px;
}

.tip-title {
  font-size: 14px;
  color: #666;
  margin-right: 12px;
  white-space: nowrap;
  font-weight: 500;
}

.quick-question {
  font-size: 13px;
  background: #e6f7ff;
  color: #1890ff;
  padding: 6px 14px;
  border-radius: 16px;
  margin-right: 10px;
  margin-bottom: 6px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #d9f0ff;
  font-weight: 400;
}

.quick-question:hover {
  background: #1890ff;
  color: white;
  border-color: #1890ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(24, 144, 255, 0.2);
}

.chat-box::-webkit-scrollbar {
  width: 8px;
}

.chat-box::-webkit-scrollbar-track {
  background: #f1f9ff;
  border-radius: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-container {
    padding: 15px;
  }

  .header h1 {
    font-size: 26px;
  }

  .chat-box {
    height: 60vh;
    padding: 15px;
  }

  .content {
    max-width: 85%;
    padding: 12px 16px;
  }

  .input-area {
    padding: 15px;
  }

  .input-area input {
    padding: 12px 16px;
    height: 45px;
    font-size: 14px;
  }

  .input-area button {
    padding: 8px 20px;
    height: 45px;
    font-size: 14px;
  }

  .quick-question {
    font-size: 12px;
    padding: 5px 12px;
  }
}
</style>
