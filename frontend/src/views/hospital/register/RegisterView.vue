<template>
  <a-modal v-model="show" title="挂号详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="registerData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">挂号信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>
          {{ registerData.code }}
        </a-col>
        <a-col :span="8"><b>价格：</b>
          {{ registerData.price }} 元
        </a-col>
        <a-col :span="8"><b>预约时间：</b>
          {{ registerData.registerDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>开始时间：</b>
          {{ registerData.startDate }}
        </a-col>
        <a-col :span="8"><b>结束时间：</b>
          {{ registerData.endDate }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          <span v-if="registerData.status == 0">未支付</span>
          <span v-if="registerData.status == 1">进行中</span>
          <span v-if="registerData.status == 2">填写处方</span>
          <span v-if="registerData.status == 3">待支付</span>
          <span v-if="registerData.status == 4">已完成</span>
          <span v-if="registerData.status == 5">已退号</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属用户：</b>
          {{ registerData.name }}
        </a-col>
        <a-col :span="8"><b>医院名称：</b>
          {{ registerData.hospitalName }}
        </a-col>
        <a-col :span="8"><b>所属科室：</b>
          {{ registerData.officesName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>备注：</b>
          {{ registerData.remark == null ? '- -' : registerData.remark }}
        </a-col>
        <a-col :span="8"><b>用药参考：</b>
          {{ registerData.reference == null ? '- -' : registerData.reference }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ registerData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>联系方式：</b>
          {{ registerData.phone }}
        </a-col>
        <a-col :span="8"><b>联系地址：</b>
          {{ registerData.userAddress }}
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import baiduMap from '@/utils/map/baiduMap'
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
  name: 'registerView',
  props: {
    registerShow: {
      type: Boolean,
      default: false
    },
    registerData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.registerShow
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
    registerShow: function (value) {
      if (value) {
        if (this.registerData.images !== null && this.registerData.images !== '') {
          this.imagesInit(this.registerData.images)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.registerData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    local (register) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(register.longitude, register.latitude)
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
