<template>
  <a-modal v-model="show" title="医生详情" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="doctorData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">医生信息</span></a-col>
        <a-col :span="8"><b>医生姓名：</b>
          {{ doctorData.doctorName }}
        </a-col>
        <a-col :span="8"><b>性别：</b>
          {{ doctorData.doctorSex }}
        </a-col>
        <a-col :span="8"><b>所属医院：</b>
          {{ doctorData.hospitalName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属科室：</b>
          {{ doctorData.officesName }}
        </a-col>
        <a-col :span="8"><b>医生职称：</b>
          {{ doctorData.doctorTitle }}
        </a-col>
        <a-col :span="8"><b>教学支职称：</b>
          {{ doctorData.teachTitle }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>行政职位：</b>
          {{ doctorData.doctorAdministrative }}
        </a-col>
        <a-col :span="8"><b>学位：</b>
          {{ doctorData.doctorDegree }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>行政职位：</b>
          {{ doctorData.doctorAbout }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>医生特长：</b>
          {{ doctorData.doctorForte }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">医生图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
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
  name: 'doctorView',
  props: {
    doctorShow: {
      type: Boolean,
      default: false
    },
    doctorData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.doctorShow
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
      previewImage: ''
    }
  },
  watch: {
    doctorShow: function (value) {
      if (value) {
        if (this.doctorData.images !== null && this.doctorData.images !== '') {
          this.imagesInit(this.doctorData.images, 0)
        } else {
          this.imagesInit(this.doctorData.doctorImg, 1)
        }
      }
    }
  },
  methods: {
    imagesInit (images, type) {
      if (images !== null && images !== '') {
        let imageList = []
        if (type === 0) {
          images.split(',').forEach((image, index) => {
            imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
          })
        } else {
          images.split(',').forEach((image, index) => {
            imageList.push({uid: index, name: image, status: 'done', url: image})
          })
        }
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
