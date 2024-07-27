<template>
  <a-modal v-model="show" title="资讯详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="medicalData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">资讯信息</span></a-col>
        <a-col :span="8"><b>标题：</b>
          {{ medicalData.title ? medicalData.title : '- -' }}
        </a-col>
        <a-col :span="8"><b>发布时间：</b>
          {{ medicalData.date ? medicalData.date : '- -' }}
        </a-col>
        <a-col :span="8"><b>资讯名称：</b>
          <span style="color: red" v-if="medicalData.rackUp == 0">下架</span>
          <span style="color: green" v-if="medicalData.rackUp == 1">发布</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>发布人：</b>
          {{ medicalData.publisher ? medicalData.publisher : '- -' }}
        </a-col>
        <a-col :span="8"><b>浏览量：</b>
          {{ medicalData.views }}
        </a-col>
        <a-col :span="8"><b>消息类型：</b>
          {{ medicalData.type ? medicalData.type : '- -' }}
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
