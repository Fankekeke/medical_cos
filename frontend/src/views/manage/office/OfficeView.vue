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
        <a-col :span="8"><b>科室姓名：</b>
          {{ officeData.officeName }}
        </a-col>
        <a-col :span="8"><b>性别：</b>
          {{ officeData.officeSex }}
        </a-col>
        <a-col :span="8"><b>所属医院：</b>
          {{ officeData.hospitalName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属科室：</b>
          {{ officeData.officesName }}
        </a-col>
        <a-col :span="8"><b>科室职称：</b>
          {{ officeData.officeTitle }}
        </a-col>
        <a-col :span="8"><b>教学支职称：</b>
          {{ officeData.teachTitle }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>行政职位：</b>
          {{ officeData.officeAdministrative }}
        </a-col>
        <a-col :span="8"><b>学位：</b>
          {{ officeData.officeDegree }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>行政职位：</b>
          {{ officeData.officeAbout }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>科室特长：</b>
          {{ officeData.officeForte }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">科室图片</span></a-col>
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
