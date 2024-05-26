<template>
  <a-modal v-model="show" title="班级图书绑定详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="recordData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">绑定信息</span></a-col>
        <a-col :span="8"><b>班级编号：</b>
          {{ recordData.code }}
        </a-col>
        <a-col :span="8"><b>班级名称：</b>
          {{ recordData.className }}
        </a-col>
        <a-col :span="8"><b>图书名称：</b>
          {{ recordData.bookName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>图书类型：</b>
          <span v-if="recordData.type == 1">医疗</span>
          <span v-if="recordData.type == 2">科技</span>
          <span v-if="recordData.type == 3">历史</span>
          <span v-if="recordData.type == 4">烹饪</span>
          <span v-if="recordData.type == 5">高数</span>
          <span v-if="recordData.type == 6">小说</span>
          <span v-if="recordData.type == 7">诗词</span>
        </a-col>
        <a-col :span="8"><b>授课老师：</b>
          {{ recordData.instructor }}
        </a-col>
        <a-col :span="8"><b>辅导员：</b>
          {{ recordData.counselor }}
        </a-col>
      </a-row>
    </div>
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
  name: 'recordView',
  props: {
    recordShow: {
      type: Boolean,
      default: false
    },
    recordData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.recordShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: [],
      studentInfo: null,
      classInfo: null,
      bookInfo: null
    }
  },
  watch: {
    recordShow: function (value) {
      if (value) {
        this.selectRecordInfo(this.recordData.id)
      }
    }
  },
  methods: {
    selectRecordInfo (id) {
      this.$get(`/cos/pay-record/recordDetail/${id}`).then((r) => {
        this.studentInfo = r.data.student
        this.classInfo = r.data.class
        this.bookInfo = r.data.book
      })
    },
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
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
