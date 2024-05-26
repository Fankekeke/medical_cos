<template>
  <a-modal v-model="show" title="缴费录详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="recordData !== null">
      <div style="font-size: 13px;font-family: SimHei" v-if="studentInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">学生信息</span></a-col>
          <a-col :span="8"><b>学生名称：</b>
            {{ studentInfo.studentName }}
          </a-col>
          <a-col :span="8"><b>学生学号：</b>
            {{ studentInfo.code }}
          </a-col>
          <a-col :span="8"><b>联系方式：</b>
            {{ studentInfo.phone }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>省份：</b>
            {{ studentInfo.province }}
          </a-col>
          <a-col :span="8"><b>城市：</b>
            {{ studentInfo.city }}
          </a-col>
          <a-col :span="8"><b>区：</b>
            {{ studentInfo.area }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>专业：</b>
            {{ studentInfo.address }}
          </a-col>
          <a-col :span="8"><b>出生日期：</b>
            {{ studentInfo.birthday }}
          </a-col>
        </a-row>
      </div>
      <br/>
      <div style="font-size: 13px;font-family: SimHei" v-if="classInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">班级信息</span></a-col>
          <a-col :span="8"><b>班级名称：</b>
            {{ classInfo.className }}
          </a-col>
          <a-col :span="8"><b>班级编号：</b>
            {{ classInfo.code }}
          </a-col>
          <a-col :span="8"><b>创建时间：</b>
            {{ classInfo.createDate }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>授课老师：</b>
            {{ classInfo.instructor }}
          </a-col>
          <a-col :span="8"><b>助理：</b>
            {{ classInfo.assistant }}
          </a-col>
          <a-col :span="8"><b>辅导员：</b>
            {{ classInfo.counselor }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="24"><b>备注：</b>
            {{ classInfo.remark }}
          </a-col>
        </a-row>
        <br/>
      </div>
      <div style="font-size: 13px;font-family: SimHei" v-if="bookInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图书信息</span></a-col>
          <a-col :span="8"><b>图书名称：</b>
            {{ bookInfo.bookName }}
          </a-col>
          <a-col :span="8"><b>图书编号：</b>
            {{ bookInfo.code }}
          </a-col>
          <a-col :span="8"><b>作者：</b>
            {{ bookInfo.auther }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>出版社：</b>
            {{ bookInfo.press }}
          </a-col>
          <a-col :span="8"><b>关键词：</b>
            {{ bookInfo.keyWord }}
          </a-col>
          <a-col :span="8"><b>图书类型：</b>
            <span v-if="bookInfo.type == 1">医疗</span>
            <span v-if="bookInfo.type == 2">科技</span>
            <span v-if="bookInfo.type == 3">历史</span>
            <span v-if="bookInfo.type == 4">烹饪</span>
            <span v-if="bookInfo.type == 5">高数</span>
            <span v-if="bookInfo.type == 6">小说</span>
            <span v-if="bookInfo.type == 7">诗词</span>
          </a-col>
        </a-row>
        <br/>
      </div>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">缴费信息</span></a-col>
        <a-col :span="8"><b>缴费状态：</b>
          <span v-if="recordData.status == 0" style="color: red">未缴费</span>
          <span v-if="recordData.status == 1" style="color: green">已缴费</span>
        </a-col>
        <a-col :span="8"><b>价格：</b>
          {{ recordData.price }} 元
        </a-col>
        <a-col :span="8"><b>缴费时间：</b>
          {{ recordData.payDate }}
        </a-col>
      </a-row>
      <br/>
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
