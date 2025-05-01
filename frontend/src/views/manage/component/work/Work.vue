<template>
  <div style="background:#ECECEC;">
    <div style="height: 450px;">
      <div style="height: 350px;background-image: url(../static/img/house.jpg);">
        <div style="padding: 50px">
          <div style="font-size: 35px;font-weight: 500;color: white;font-family: SimHei">选择医院挂号</div>
          <div style="font-size: 22px;font-weight: 500;color: white;font-family: SimHei">医院列表</div>
        </div>
        <div style="height: 150px;margin-top: 100px">
          <div style="padding: 50px 50px 0 50px">
            <a-card :bordered="false" hoverable style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff;">
              <a-row style="padding: 50px;margin: 0 auto">
                <a-col :span="16">
                  <a-row>
                    <a-col :span="18">
                      <a-input v-model="key" placeholder="查找医院"/>
                    </a-col>
                    <a-col :span="4" :offset="2">
                      <a-button type="primary" @click="fetch">
                        查找
                      </a-button>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-card>
          </div>
          <a-card :bordered="false" hoverable style="box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff;margin-top: 100px;width: 100%">
            <a-row style="padding: 10px;margin: 0 auto">
              <a-col :span="24" style="font-size: 15px;font-family: SimHei">
                <a-breadcrumb style="margin-bottom: 15px">
                  <a-breadcrumb-item v-for="(item, index) in menuList" :key="index" @click.native="changeMenu(item.type)">{{ item.name }}</a-breadcrumb-item>
                </a-breadcrumb>
                <div v-if="checkStage == 1">
                  <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 4 }" :pagination="pagination" :data-source="hospitalList">
                    <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                      <a-card hoverable>
                        <img
                          v-if="item.images"
                          height="150"
                          slot="cover"
                          alt="example"
                          :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]"
                        />
                        <img
                          v-else
                          height="150"
                          slot="cover"
                          alt="example"
                          src="/static/img/9370437.png"
                        />
                        <template slot="actions" class="ant-card-actions">
                          <a-icon key="apartment" type="apartment" @click="selectOfficeList(item)"/>
                          <a-icon key="ellipsis" type="ellipsis" @click="handlehospitalViewOpen(item)"/>
                        </template>
                        <a-card-meta :title="item.hospitalName" :description="item.hospitalAddress"></a-card-meta>
                        <div style="margin-top: 10px;font-size: 14px;font-family: SimHei">{{ item.hospitalNature }} | {{ item.hospitalGrade }}</div>
                      </a-card>
                    </a-list-item>
                  </a-list>
                </div>
                <div v-if="checkStage == 2">
                  <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 4 }" :pagination="pagination" :data-source="officeList">
                    <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                      <a-card hoverable>
                        <template slot="actions" class="ant-card-actions">
                          <a-icon key="apartment" type="apartment" @click="selectScheduleList(item)"/>
                        </template>
                        <a-card-meta :title="item.officesName" :description="item.officesDiagnosisScope"></a-card-meta>
                      </a-card>
                    </a-list-item>
                  </a-list>
                </div>
                <div v-if="checkStage == 3">
                  <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 4 }" :pagination="pagination" :data-source="scheduleList">
                    <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                      <a-card hoverable>
                        <template slot="actions" class="ant-card-actions">
                          <div style="font-size: 13px;font-family: SimHei" v-if="item.registerId == null" @click="reserveDoctor(item)">预约</div>
                          <div style="font-size: 13px;font-family: SimHei;cursor:not-allowed" v-else>已被预约</div>
                        </template>
                        <a-card-meta :description="item.name">
                          <template slot="title">
                            <div style="font-size: 15px;margin-bottom: 5px;text-align: center;margin-top: 5px"><a-icon type="schedule" /> {{ item.taskDate }}</div>
                            <div><a-icon type="user" /> {{ item.doctorName }}</div>
                          </template>
                        </a-card-meta>
                        <div style="margin-top: 10px;font-size: 14px;font-family: SimHei">{{ item.startDate }} ~ {{ item.endDate }}</div>
                      </a-card>
                    </a-list-item>
                  </a-list>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
    <hospital-view
      @close="handlehospitalViewClose"
      :hospitalShow="hospitalView.visiable"
      :hospitalData="hospitalView.data">
    </hospital-view>
  </div>
</template>

<script>

import {mapState} from 'vuex'
import hospitalView from './HospitalView.vue'
export default {
  name: 'Work',
  components: {hospitalView},
  data () {
    return {
      menuList: [],
      scheduleList: [],
      checkStage: 1,
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      hospitalView: {
        visiable: false,
        data: null
      },
      pagination: {
        onChange: page => {
          console.log(page);
        },
        pageSize: 12,
      },
      key: '',
      hospitalList: [],
      officeList: [],
      loading: false,
      rentView: {
        visiable: false,
        data: null
      },
      startDate: null,
      endDate: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
    reserveDoctor (item) {
      console.log(item)
      let data = {
        userId: this.currentUser.userId,
        scheduleId: item.id,

      }
      this.$post(`/cos/register-info/registerOrderAdd`, data).then((r) => {
        let resultData = r.data.data
        console.log(JSON.stringify(resultData))
        let payData = {
          outTradeNo: resultData.code,
          subject: resultData.code,
          totalPrice: resultData.price
        }
        this.pay(payData)
      })
    },
    pay (row) {
      let data = { outTradeNo: row.outTradeNo, subject: `${row.subject}`, totalAmount: row.totalPrice, body: '' }
      this.$post('/cos/pay/alipay', data).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    changeMenu (type) {
      if (type === 2) {
        this.menuList.pop()
      }
      this.checkStage = type
    },
    selectScheduleList (office) {
      this.$get(`/cos/doctor-info/selectDoctorByOfficeId/${office.id}`).then((r) => {
        this.scheduleList = r.data.data
        this.menuList.push({name: office.officesName, type: 2})
        this.checkStage = 3
      })
    },
    selectOfficeList (hospital) {
      this.$get(`/cos/office-info/list/byhospital/${hospital.id}`).then((r) => {
        this.officeList = r.data.data
        this.menuList = [{name: hospital.hospitalName, type: 1}]
        this.checkStage = 2
      })
    },
    handlehospitalViewOpen (row) {
      this.hospitalView.data = row
      this.hospitalView.visiable = true
    },
    handlehospitalViewClose () {
      this.hospitalView.visiable = false
    },
    handlevehicleViewClose () {
      this.vehicleView.visiable = false
    },
    handlevehicleViewSuccess () {
      this.vehicleView.visiable = false
      this.$message.success('添加订单成功')
    },
    orderSuccess () {
      this.rentView.visiable = false
      this.$message.success('添加订单成功')
      this.fetch()
    },
    view (record) {
      // if (record.currentStatus === '0') {
      //   this.$message.warn('此商家不在营业时间内')
      //   return false
      // }
      this.orderMapView.merchantInfo = record
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    getWorkStatusList () {
      this.$get(`/cos/hospital-info/query/like/list`, { key: this.key }).then((r) => {
        this.hospitalList = r.data.data
        console.log(this.hospitalList.length)
      })
    },
    fetch () {
      this.getWorkStatusList()
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 14px;
  font-family: SimHei;
  margin-top: 8px;
}
>>> .ant-card-meta-description {
  font-size: 14px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
