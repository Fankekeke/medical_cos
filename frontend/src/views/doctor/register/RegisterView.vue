<template>
  <a-modal v-model="show" title="挂号详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back1" @click="commit">
        提交
      </a-button>
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="registerData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">挂号信息</span></a-col>
        <a-col :span="6"><b>订单编号：</b>
          {{ registerData.code }}
        </a-col>
        <a-col :span="6"><b>挂号价格：</b>
          {{ registerData.price }} 元
        </a-col>
        <a-col :span="6"><b>预约时间：</b>
          {{ registerData.registerDate }}
        </a-col>
        <a-col :span="6"><b>状态：</b>
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
        <a-col :span="6"><b>开始时间：</b>
          {{ registerData.startDate }}
        </a-col>
        <a-col :span="6"><b>结束时间：</b>
          {{ registerData.endDate }}
        </a-col>
        <a-col :span="6"><b>医院名称：</b>
          {{ registerData.hospitalName }}
        </a-col>
        <a-col :span="6"><b>所属科室：</b>
          {{ registerData.officesName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>所属用户：</b>
          {{ registerData.name }}
        </a-col>
        <a-col :span="6"><b>创建时间：</b>
          {{ registerData.createDate }}
        </a-col>
        <a-col :span="6"><b>联系方式：</b>
          {{ registerData.phone }}
        </a-col>
        <a-col :span="6"><b>联系地址：</b>
          {{ registerData.userAddress }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="20">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注/用药参考</span></a-col>
        <a-col :span="12">
          <a-textarea
            v-model="remark"
            placeholder="备注"
            :auto-size="{ minRows: 5, maxRows: 5 }"
          />
        </a-col>
        <a-col :span="12">
          <a-textarea
            v-model="reference"
            placeholder="用药参考"
            :auto-size="{ minRows: 5, maxRows: 5 }"
          />
        </a-col>
        <a-col :span="24">
          <a-table :columns="columns" :rowKey="record => record.id" :data-source="dataList" :pagination="false">
            <template slot="nameShow" slot-scope="text, record">
              <a-select style="width: 100%" @change="handleChange($event, record)">
                <a-select-option v-for="(item, index) in drugList" :key="index" :value="item.id">{{ item.drugName }}</a-select-option>
              </a-select>
            </template>
            <template slot="brandShow" slot-scope="text, record">
              <span>{{ record.brand }}</span>
            </template>
            <template slot="typeIdShow" slot-scope="text, record">
              <span v-if="record.classification == 1">中药材</span>
              <span v-if="record.classification == 2">中药饮片</span>
              <span v-if="record.classification == 3">中西成药</span>
              <span v-if="record.classification == 4">化学原料药</span>
              <span v-if="record.classification == 5">抗生素</span>
              <span v-if="record.classification == 6">生化药品</span>
              <span v-if="record.classification == 7">放射性药品</span>
              <span v-if="record.classification == 8">血清</span>
              <span v-if="record.classification == 9">诊断药品</span>
            </template>
            <template slot="dosageFormShow" slot-scope="text, record">
              <span>{{ record.dosageForm }}</span>
            </template>
            <template slot="reserveShow" slot-scope="text, record">
              <a-input-number v-model="record.reserve" :min="1" :max="record.reserveAll" :step="1"/>
            </template>
            <template slot="priceShow" slot-scope="text, record">
              <span>{{ record.unitPrice }}元</span>
            </template>
          </a-table>
          <a-button @click="dataAdd" type="primary" ghost style="margin-top: 10px;width: 100%">
            新增物品
          </a-button>
        </a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import baiduMap from '@/utils/map/baiduMap'
import {mapState} from "vuex";
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
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.registerShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '药品名称',
        dataIndex: 'drugName',
        scopedSlots: {customRender: 'nameShow'}
      }, {
        title: '数量',
        dataIndex: 'reserve',
        scopedSlots: {customRender: 'reserveShow'}
      }, {
        title: '所属品牌',
        dataIndex: 'brand',
        scopedSlots: {customRender: 'brandShow'}
      }, {
        title: '药品类别',
        dataIndex: 'classification',
        scopedSlots: {customRender: 'typeIdShow'}
      }, {
        title: '剂型',
        dataIndex: 'dosageForm',
        scopedSlots: {customRender: 'dosageFormShow'}
      }, {
        title: '单价',
        dataIndex: 'unitPrice',
        scopedSlots: {customRender: 'priceShow'}
      }]
    }
  },
  data () {
    return {
      remark: '',
      reference: '',
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      dataList: [],
      drugList: []
    }
  },
  watch: {
    registerShow: function (value) {
      if (value) {
      }
    }
  },
  mounted () {
    this.getDrug()
  },
  methods: {
    commit () {
      if (!this.reference) {
        this.$message.error('请填写用药参考')
        return false
      }
      if (!this.remark) {
        this.$message.error('请填写备注')
        return false
      }
      if (this.dataList.length === 0) {
        this.$message.error('请填写用药')
        return false
      }
      let orderItem = []
      this.dataList.forEach(e => {
        orderItem.push({
          drugId: e.drugId,
          quantity: e.reserve,
          unitPrice: e.unitPrice
        })
      })
      let data = {
        userId: this.registerData.userId,
        staffId: this.currentUser.userId,
        registerId: this.registerData.id,
        orderItem: JSON.stringify(orderItem)
      }
      this.$post('/cos/register-info/register/order', data).then((r) => {
        this.$emit('success')
      })
    },
    dataAdd () {
      this.dataList.push({drugId: null, reserve: 1, brand: '', classification: '', dosageForm: '', unitPrice: ''})
    },
    getDrug () {
      this.$get(`/cos/pharmacy-inventory/detail/doctor/${this.currentUser.userId}`).then((r) => {
        this.drugList = r.data.data
        console.log(this.drugList)
      })
    },
    handleChange (value, record) {
      if (value) {
        this.drugList.forEach(e => {
          if (e.id === value) {
            record.reserveAll = e.reserve
            record.brand = e.brand
            record.classification = e.classification
            record.dosageForm = e.dosageForm
            record.unitPrice = e.unitPrice
            record.drugId = e.id
            console.log(record)
          }
        })
      }
    },
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
