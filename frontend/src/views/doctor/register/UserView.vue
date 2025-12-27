<template>
  <a-modal v-model="show" title="用户病例" @cancel="onClose" :width="1600">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <a-row v-if="userData !== null">
      <a-col :span="16">
        <div class="user-info-container">
          <div class="section-header">
            <a-icon type="user" class="section-icon" />
            <span class="section-title">用户信息</span>
          </div>
          <a-row :gutter="16" class="info-content">
            <a-col :span="8">
              <div class="info-item">
                <span class="info-label">用户名称：</span>
                <span class="info-value">{{ userData.name }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="info-label">用户编号：</span>
                <span class="info-value">{{ userData.code }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="info-label">联系方式：</span>
                <span class="info-value">{{ userData.phone }}</span>
              </div>
            </a-col>
          </a-row>

          <div class="section-header" style="margin-top: 30px;">
            <a-icon type="file-text" class="section-icon" />
            <span class="section-title">病例记录</span>
          </div>
          <div class="table-container">
            <a-table :columns="columns" :data-source="durgList" class="custom-table">
              <template slot="contentShow" slot-scope="text, record">
                <template>
                  <a-tooltip>
                    <template slot="title">
                      {{ record.cause }}
                    </template>
                    <span class="cause-text">{{ record.cause.slice(0, 15) }} ...</span>
                  </a-tooltip>
                </template>
              </template>
            </a-table>
          </div>
        </div>
      </a-col>
      <a-col :span="8">
        <div class="ai-analysis-section">
          <div class="ai-header">
            <a-icon type="robot" class="ai-icon" />
            <span class="ai-title">AI病例分析</span>
          </div>
          <a-spin :spinning="aiLoading" tip="AI分析中..." class="ai-spin">
            <div v-if="aiAnalysisResult" class="ai-content">
              <a-alert type="info" show-icon class="ai-result-alert">
                <template slot="message">
                  <div v-html="formatAiResult(aiAnalysisResult)" class="ai-result-content"></div>
                </template>
              </a-alert>
            </div>
            <div v-else class="ai-placeholder">
              <a-empty description="暂无AI分析结果" class="ai-empty">
                <a-button type="primary" @click="generateAiAnalysis" :loading="aiLoading" class="ai-generate-btn">
                  <a-icon type="thunderbolt" /> 生成AI分析
                </a-button>
              </a-empty>
            </div>
          </a-spin>
        </div>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'userView',
  props: {
    userShow: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.userShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '处方单号',
        dataIndex: 'code'
      }, {
        title: '病因',
        dataIndex: 'cause',
        scopedSlots: { customRender: 'contentShow' }
      }, {
        title: '出具人',
        dataIndex: 'doctorName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '症状',
        dataIndex: 'symptom',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '发布时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      aiLoading: false, // 添加 AI 加载状态
      aiAnalysisResult: '', // 添加 AI 分析结果
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: []
    }
  },
  watch: {
    userShow: function (value) {
      if (value) {
        this.$get(`/cos/medication-info/list/byUser/${this.userData.userId}`).then((r) => {
          this.durgList = r.data.data
          let message = ''
          if (this.durgList.length !== 0) {
            this.durgList.forEach((item) => {
              message += '时间：' + item.createDate + ', 症状：' + item.symptom + ', 病因：' + item.cause + '\n'
            })
            // this.$post(`/cos/ai/aliTyqw`, {
            //   content: message
            // }).then((r) => {
            //
            // })
          }
        })
      }
    }
  },
  methods: {
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    generateAiAnalysis () {
      if (this.durgList.length === 0) {
        this.$message.warning('暂无病例记录可供分析')
        return
      }

      this.aiLoading = true
      this.aiAnalysisResult = ''

      let message = '请对以下病例进行分析并给出建议（300字内）：\n'
      this.durgList.forEach((item) => {
        message += '时间：' + item.createDate + ', 症状：' + item.symptom + ', 病因：' + item.cause + '\n'
      })

      this.$post(`/cos/ai/aliTyqw`, {
        content: message
      }).then((r) => {
        this.aiAnalysisResult = r.data.msg
      }).catch((error) => {
        this.$message.error('AI分析失败，请稍后重试')
        console.error('AI分析错误:', error)
      }).finally(() => {
        this.aiLoading = false
      })
    },

    // 添加格式化 AI 结果的方法
    formatAiResult (result) {
      if (!result) return ''
      // 简单的换行符替换为 HTML 换行
      return result.replace(/\n/g, '<br>')
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
.ai-analysis-section {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  height: 100%;
  min-height: 500px;
}

.ai-title {
  font-size: 16px;
  font-weight: 600;
  color: #000c17;
  margin-bottom: 20px;
  text-align: center;
}

.ai-content {
  margin-top: 20px;
}

.ai-placeholder {
  text-align: center;
  padding: 40px 20px;
}

>>> .ant-alert {
  border-radius: 6px;
}

>>> .ant-alert-info {
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
}

@media (max-width: 1200px) {
  .ai-analysis-section {
    min-height: 300px;
  }
}

.user-info-container {
  font-family: 'Microsoft YaHei', SimHei;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

/* 区块标题 */
.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
}

.section-icon {
  font-size: 18px;
  color: #1890ff;
  margin-right: 10px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #000c17;
}

/* 信息项样式 */
.info-content {
  margin-top: 15px;
}

.info-item {
  display: flex;
  margin-bottom: 12px;
}

.info-label {
  font-weight: 500;
  color: #666;
  min-width: 80px;
}

.info-value {
  color: #333;
  flex: 1;
}

/* 表格容器 */
.table-container {
  margin-top: 15px;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.custom-table >>> .ant-table-thead > tr > th {
  background: #f0f5ff;
  font-weight: 600;
}

.cause-text {
  color: #1890ff;
  cursor: pointer;
}

.cause-text:hover {
  color: #40a9ff;
  text-decoration: underline;
}

/* AI分析区域 */
.ai-analysis-section {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
  border-radius: 8px;
  height: 100%;
  min-height: 600px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}

.ai-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #d1e3ff;
}

.ai-icon {
  font-size: 20px;
  color: #1890ff;
  margin-right: 10px;
}

.ai-title {
  font-size: 18px;
  font-weight: 600;
  color: #000c17;
}

.ai-spin {
  min-height: 400px;
}

.ai-result-alert {
  border-radius: 6px;
  background: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.ai-result-content {
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap;
}

.ai-placeholder {
  text-align: center;
  padding: 60px 20px;
}

.ai-empty >>> .ant-empty-description {
  color: #999;
  font-size: 14px;
}

.ai-generate-btn {
  margin-top: 15px;
  height: 36px;
  font-size: 14px;
}

/* 响应式优化 */
@media (max-width: 1200px) {
  .ai-analysis-section {
    min-height: 400px;
  }

  .info-item {
    flex-direction: column;
  }

  .info-label {
    margin-bottom: 4px;
  }
}

/* 滚动条优化 */
.custom-table >>> .ant-table-body {
  overflow-x: auto;
}

.custom-table >>> ::-webkit-scrollbar {
  height: 6px;
}

.custom-table >>> ::-webkit-scrollbar-thumb {
  background: #d9d9d9;
  border-radius: 3px;
}
</style>
