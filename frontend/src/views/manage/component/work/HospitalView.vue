<template>
  <a-modal v-model="show" title="医院详情" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="hospitalData !== null">
      <div>
        <a-card :bordered="false" style="height: 400px">
          <div id="areas" style="width: 100%;height: 350px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
        </a-card>
      </div>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">医院信息</span></a-col>
        <a-col :span="8"><b>医院姓名：</b>
          {{ hospitalData.hospitalName }}
        </a-col>
        <a-col :span="8"><b>医院地区：</b>
          {{ hospitalData.hospitalArea }}
        </a-col>
        <a-col :span="8"><b>所属医院：</b>
          {{ hospitalData.hospitalDeanName != null ? hospitalData.hospitalDeanName : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>医院建院年份：</b>
          {{ hospitalData.hospitalYear != null ? hospitalData.hospitalYear : '- -' }}
        </a-col>
        <a-col :span="8"><b>医院类型：</b>
          {{ hospitalData.hospitalNature != null ? hospitalData.hospitalNature : '- -' }}
        </a-col>
        <a-col :span="8"><b>医院等级：</b>
          {{ hospitalData.hospitalGrade != null ? hospitalData.hospitalGrade : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>医院科室数量：</b>
          {{ hospitalData.hospitalOfficesNum != null ? hospitalData.hospitalOfficesNum : '- -' }}
        </a-col>
        <a-col :span="8"><b>医保人数：</b>
          {{ hospitalData.medicalInsuranceNum != null ? hospitalData.medicalInsuranceNum : '- -' }}
        </a-col>
        <a-col :span="8"><b>病床数量：</b>
          {{ hospitalData.hospitalBedNum != null ? hospitalData.hospitalBedNum : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>年门诊量：</b>
          {{ hospitalData.outpatientNum != null ? hospitalData.outpatientNum : '- -' }}
        </a-col>
        <a-col :span="8"><b>是否医保：</b>
          {{ hospitalData.isMedicalInsurance != null ? hospitalData.isMedicalInsurance : '- -' }}
        </a-col>
        <a-col :span="8"><b>医院荣誉：</b>
          {{ hospitalData.hospitalHonor != null ? hospitalData.hospitalHonor : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>医院电话：</b>
          {{ hospitalData.hospitalPhone != null ? hospitalData.hospitalPhone : '- -' }}
        </a-col>
        <a-col :span="8"><b>医院地址：</b>
          {{ hospitalData.hospitalAddress != null ? hospitalData.hospitalAddress : '- -' }}
        </a-col>
        <a-col :span="8"><b>医院邮编：</b>
          {{ hospitalData.hospitalPostCode != null ? hospitalData.hospitalPostCode : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>医院公交路线：</b>
          {{ hospitalData.hospitalBusRoute != null ? hospitalData.hospitalBusRoute : '- -' }}
        </a-col>
        <a-col :span="8"><b>是否开启挂号：</b>
          <span v-if="hospitalData.isOpen != null && hospitalData.isOpen == 1" style="color: green">开启</span>
          <span v-else style="color: red">关闭</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>医院设备介绍：</b>
          {{ hospitalData.hospitalEquipment }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>医院简介：</b>
          {{ hospitalData.hospitalAbout }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">医院图片</span></a-col>
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
  name: 'hospitalView',
  props: {
    hospitalShow: {
      type: Boolean,
      default: false
    },
    hospitalData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.hospitalShow
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
    hospitalShow: function (value) {
      if (value) {
        if (this.hospitalData.images !== null && this.hospitalData.images !== '') {
          this.imagesInit(this.hospitalData.images, 0)
        } else {
          this.imagesInit(this.hospitalData.hospitalUrl, 1)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.hospitalData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    local (pharmacy) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(pharmacy.longitude, pharmacy.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
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
