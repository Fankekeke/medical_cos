<template>
  <a-modal v-model="show" title="章节详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="medicalData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">章节信息</span></a-col>
        <a-col :span="8"><b>书籍编号：</b>
          {{ medicalData.code ? medicalData.code : '- -' }}
        </a-col>
        <a-col :span="8"><b>书籍名称：</b>
          {{ medicalData.medicalName ? medicalData.medicalName : '- -' }}
        </a-col>
        <a-col :span="8"><b>章节名称：</b>
          {{ medicalData.name ? medicalData.name : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>排序：</b>
          {{ medicalData.indexNo ? medicalData.indexNo : '- -' }}
        </a-col>
        <a-col :span="8"><b>浏览量：</b>
          {{ medicalData.views ? medicalData.views : '- -' }}
        </a-col>
        <a-col :span="8"><b>是否需要会员：</b>
          <span v-if="medicalData.checkFlag == 0">否</span>
          <span v-if="medicalData.checkFlag == 1" style="color: green">是</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>本章字数：</b>
          {{ medicalData.words ? medicalData.words : '- -' }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ medicalData.createDate }}
        </a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
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
    medicalShow: {
      type: Boolean,
      default: false
    },
    medicalData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.medicalShow
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
      repairInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    medicalShow: function (value) {
      if (value) {
        if (this.medicalData.images) {
          this.imagesInit(this.medicalData.images)
        }
      }
    }
  },
  methods: {
    local (medicalData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(medicalData.longitude, medicalData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
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
