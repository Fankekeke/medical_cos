<template>
  <a-modal v-model="show" title="科室详情" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="officeData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">科室信息</span></a-col>
        <a-col :span="8"><b>科室名称：</b>
          {{ officeData.officesName }}
        </a-col>
        <a-col :span="8"><b>医生人数：</b>
          {{ officeData.doctorNum == null ? '未知' : officeData.doctorNum }}
        </a-col>
        <a-col :span="8"><b>所属医院：</b>
          {{ officeData.hospitalName == null ? '未知' : officeData.hospitalName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>荣誉资质：</b>
          {{ officeData.officesHonor == null ? '未知' : officeData.officesHonor }}
        </a-col>
        <a-col :span="8"><b>设备：</b>
          {{ officeData.officesEquipment == null ? '未知' : officeData.officesEquipment }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>关于我们：</b>
          {{ officeData.officesAbout }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>诊疗范围：</b>
          {{ officeData.officesDiagnosisScope }}
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
  name: 'officeView',
  props: {
    officeShow: {
      type: Boolean,
      default: false
    },
    officeData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.officeShow
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
    officeShow: function (value) {
      if (value) {
        if (this.officeData.images !== null && this.officeData.images !== '') {
          this.imagesInit(this.officeData.images, 0)
        } else {
          this.imagesInit(this.officeData.officeImg, 1)
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
