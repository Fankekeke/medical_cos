<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="visible"
    @close="handleOk"
    wrapClassName="aa"
    :getContainer="false"
  >
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="18" style="height: 100%;">
          <div id="areas" style="width: 100%;height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
        </a-col>
        <a-col :span="6" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div v-if="rentShow && hospitalInfo" class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden">
              <a-carousel autoplay style="height: 250px;" v-if="hospitalInfo.images">
                <div style="width: 100%;height: 250px" v-for="(item, index) in hospitalInfo.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 100%">
                </div>
              </a-carousel>
              <a-card :title="hospitalInfo.hospitalName" :bordered="false">
                <a slot="extra" @click="rentNavigation" style="margin-right: 10px">导航</a>
                <a slot="extra" @click="rentBack">返回</a>
              </a-card>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">周边设施</span></a-col>
                  <a-col :span="24">
                    <a-radio-group button-style="solid" style="width: 100%" @change="gisOnChange">
                      <a-radio-button value="1" style="width: 25%;text-align: center">
                        交通
                      </a-radio-button>
                      <a-radio-button value="2" style="width: 25%;text-align: center">
                        餐饮
                      </a-radio-button>
                      <a-radio-button value="3" style="width: 25%;text-align: center">
                        教育
                      </a-radio-button>
                      <a-radio-button value="4" style="width: 25%;text-align: center">
                        医疗
                      </a-radio-button>
                    </a-radio-group>
                  </a-col>
                </a-row>
                <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 20px">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">医院信息</span></a-col>
                  <a-col :span="12"><b>医院姓名：</b>
                    {{ hospitalInfo.hospitalName }}
                  </a-col>
                  <a-col :span="12"><b>医院地区：</b>
                    {{ hospitalInfo.hospitalArea }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>院长：</b>
                    {{ hospitalInfo.hospitalDeanName != null ? hospitalInfo.hospitalDeanName : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医院建院年份：</b>
                    {{ hospitalInfo.hospitalYear != null ? hospitalInfo.hospitalYear : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>医院类型：</b>
                    {{ hospitalInfo.hospitalNature != null ? hospitalInfo.hospitalNature : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医院等级：</b>
                    {{ hospitalInfo.hospitalGrade != null ? hospitalInfo.hospitalGrade : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>医院科室数量：</b>
                    {{ hospitalInfo.hospitalOfficesNum != null ? hospitalInfo.hospitalOfficesNum : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医保人数：</b>
                    {{ hospitalInfo.medicalInsuranceNum != null ? hospitalInfo.medicalInsuranceNum : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>病床数量：</b>
                    {{ hospitalInfo.hospitalBedNum != null ? hospitalInfo.hospitalBedNum : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>年门诊量：</b>
                    {{ hospitalInfo.outpatientNum != null ? hospitalInfo.outpatientNum : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>是否医保：</b>
                    {{ hospitalInfo.isMedicalInsurance != null ? hospitalInfo.isMedicalInsurance : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医院荣誉：</b>
                    {{ hospitalInfo.hospitalHonor != null ? hospitalInfo.hospitalHonor : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>医院电话：</b>
                    {{ hospitalInfo.hospitalPhone != null ? hospitalInfo.hospitalPhone : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医院地址：</b>
                    {{ hospitalInfo.hospitalAddress != null ? hospitalInfo.hospitalAddress : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>医院邮编：</b>
                    {{ hospitalInfo.hospitalPostCode != null ? hospitalInfo.hospitalPostCode : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>医院公交路线：</b>
                    {{ hospitalInfo.hospitalBusRoute != null ? hospitalInfo.hospitalBusRoute : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>是否开启挂号：</b>
                    <span v-if="hospitalInfo.isOpen != null && hospitalInfo.isOpen == 1" style="color: green">开启</span>
                    <span v-else style="color: red">关闭</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="24"><b>医院设备介绍：</b>
                    {{ hospitalInfo.hospitalEquipment }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="24"><b>医院简介：</b>
                    {{ hospitalInfo.hospitalAbout }}
                  </a-col>
                </a-row>
                <br/>
              </div>
            </div>
            <div v-if="!rentShow" style="text-align: center;font-size: 13px;height: 100vh; overflow-y: auto;padding: 15px;overflow-x: hidden">
              <a-input-search placeholder="请输入关键字搜索..." style="margin-bottom: 15px;margin-top: 15px" @search="onSearch" />
              <a-skeleton :loading="loading" active />
              <div v-show="!loading" style="width: 100%;margin-bottom: 15px;text-align: left" v-for="(item, index) in rentList" :key="index">
                <a-divider orientation="left">
                  <span style="font-size: 12px;font-family: SimHei;">{{item.hospitalName}} - {{item.hospitalAddress}}</span>
                </a-divider>
                <a-card :bordered="false" @click="rentDetail(item)" hoverable>
                  <div v-if="item.images !== null">
                    <a-popover v-for="(item1, index1) in item.images.split(',')" :key="index1">
                      <template slot="content">
                        <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                      </template>
                      <a-avatar shape="square" :size="50" icon="user" style="margin-bottom: 15px;margin-right: 10px" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                    </a-popover>
                  </div>
                  <a-card-meta :title="item.hospitalAddress" :description="item.hospitalAddress.slice(0, 25)+'...'"></a-card-meta>
                  <div style="font-size: 12px;font-family: SimHei;margin-top: 5px">
                    <span>{{ item.hospitalGrade }}</span> |
                    <span>{{ item.hospitalArea }}</span> |
                    <span style="color: #f5222d; font-size: 13px;float: right">{{ item.hospitalNature }}</span>
                  </div>
                </a-card>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
export default {
  name: 'Map',
  data () {
    return {
      loading: false,
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      rentListBack: [],
      communityList: [],
      community: null,
      hospitalInfo: null,
      nowPoint: null,
      roadData: [],
      checkLoading: false,
      echartsShow: false,
      series: [{
        name: '',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'bar',
          height: 180
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          },
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          show: false
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return '均价： ' + val + ' 元'
            }
          }
        }
      }
    }
  },
  mounted () {
    this.visible = true
    setTimeout(() => {
      baiduMap.initMap('areas')
      this.getLocal()
    }, 500)
    this.getRentList()
  },
  methods: {
    selectRentCountByCommunity (communityCode) {
      this.$get(`/cos/rent-charge/rentCharge/community/${communityCode}`).then((r) => {
        this.communityRent = r.data.data
      })
    },
    gisOnChange (e) {
      let key = ''
      switch (e.target.value) {
        case '1':
          key = '公交站'
          break
        case '2':
          key = '餐饮'
          break
        case '3':
          key = '教育'
          break
        case '4':
          key = '医疗'
          break
      }
      if (this.rentShow) {
        baiduMap.searchNear(this.hospitalInfo.longitude, this.hospitalInfo.latitude, key)
      } else {
        baiduMap.searchNear(this.communityData.longitude, this.communityData.latitude, key)
      }
    },
    onSearch (value) {
      this.loading = true
      if (value) {
        this.rentList = this.rentListBack.filter( v => v.hospitalAddress.indexOf(value) > -1 || v.hospitalName.indexOf(value) > -1)
      } else {
        this.rentList = this.rentListBack
      }
      setTimeout(() => {
        this.loading = false
      }, 500)
    },
    selectHousePriceTrend (row) {
      if (!this.echartsShow) {
        this.echartsShow = true
      }
      this.checkLoading = true
      this.$get('/cos/house-price-trend/trend/community', {
        communityCode: row.code,
        year: new Date().getFullYear(),
        month: new Date().getMonth()
      }).then((r) => {
        let data = r.data.data
        let labels = []
        let values = []
        if (data !== undefined && data !== null) {
          Object.keys(data).forEach(e => {
            labels.push(e + '月')
            values.push(data[e])
          })
          this.series[0].name = row.communityName
          this.series[0].data = values
          this.chartOptions.xaxis.categories = labels
        }
        setTimeout(() => {
          this.checkLoading = false
        }, 500)
      })
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    },
    navigation (data) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(data.longitude, data.latitude))
      this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.hospitalInfo.longitude, this.hospitalInfo.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    rentBack () {
      this.rentShow = false
    },
    communityBack () {
      this.communityShow = false
    },
    rentNavigation () {
      this.navigation(this.hospitalInfo)
    },
    home () {
      this.$router.push('/home')
    },
    rentDetail (row) {
      this.rentShow = true
      this.getHospitalInfo(row.id)
      this.local(row)
    },
    communityDetail (row) {
      this.communityData = row
      this.communityShow = true
      this.selectHousePriceTrend(row)
      this.selectRentCountByCommunity(row.code)
      this.local(row)
    },
    local (data) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      baiduMap.pointAdd(new BMap.Point(data.longitude, data.latitude))
      baiduMap.findLine(new BMap.Point(data.longitude, data.latitude), 15)
    },
    getCommunity (communityCode) {
      this.$get(`/cos/community-info/detail/${communityCode}`).then((r) => {
        this.community = r.data.data
      })
    },
    getHospitalInfo (hospitalId) {
      this.$get(`/cos/hospital-info/${hospitalId}`).then((r) => {
        this.hospitalInfo = r.data.data
        console.log(this.hospitalInfo)
      })
    },
    getRentList (key) {
      this.$get('/cos/hospital-info/hospital/map/').then((r) => {
        let data = r.data.data
        this.rentList = data
        this.rentListBack = data
      })
    },
    getCommunityList () {
      this.$get('/cos/community-info/page').then((r) => {
        this.communityList = r.data.data.records
      })
    },
    handleOk (e) {
      this.visible = false
    },
    onChange (checkedList) {
      this.indeterminate = !!this.checkedList.length && this.checkedList.length < this.plainOptions.length
      this.checkAll = this.checkedList.length === this.plainOptions.length
    },
    onCheckAllChange (e) {
      Object.assign(this, {
        checkedList: e.target.checked ? this.plainOptions : [],
        indeterminate: false,
        checkAll: e.target.checked
      })
    }
  }
}
</script>

<style scoped>
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 12px;
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
  >>> .ant-radio-button-wrapper {
    border-radius: 0;
  }
</style>
