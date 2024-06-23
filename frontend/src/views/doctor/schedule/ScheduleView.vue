<template>
  <a-modal v-model="show" title="排班详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="scheduleData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">排班信息</span></a-col>
        <a-col :span="8"><b>排班名称：</b>
          {{ scheduleData.name }}
        </a-col>
        <a-col :span="8"><b>排班编号：</b>
          {{ scheduleData.code }}
        </a-col>
        <a-col :span="8"><b>作业日期：</b>
          {{ scheduleData.taskDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>医院名称：</b>
          {{ scheduleData.hospitalName }}
        </a-col>
        <a-col :span="8"><b>所属科室：</b>
          {{ scheduleData.officesName }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ scheduleData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>作业医生：</b>
          {{ scheduleData.doctorName }}
        </a-col>
        <a-col :span="8"><b>排班开始时间：</b>
          {{ scheduleData.startDate }}
        </a-col>
        <a-col :span="8"><b>排班结束时间：</b>
          {{ scheduleData.endDate }}
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
  name: 'scheduleView',
  props: {
    scheduleShow: {
      type: Boolean,
      default: false
    },
    scheduleData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.scheduleShow
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
    scheduleShow: function (value) {
      if (value) {
        if (this.scheduleData.images !== null && this.scheduleData.images !== '') {
          this.imagesInit(this.scheduleData.images)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.scheduleData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    local (schedule) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(schedule.longitude, schedule.latitude)
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
