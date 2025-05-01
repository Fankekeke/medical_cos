<template>
  <div>
    <a-row v-if="user.roleId == 75" style="width: 100%;margin-top: 30px;margin-bottom: 50px">
<!--      <div>-->
<!--        <a-col :span="22" v-if="newsList.length > 0">-->
<!--          <a-alert-->
<!--            banner-->
<!--            :message="newsContent"-->
<!--            type="info"-->
<!--          />-->
<!--        </a-col>-->
<!--        <a-col :span="2">-->
<!--          <a-button type="primary" style="margin-top: 2px;margin-left: 10px" @click="newsNext">下一页</a-button>-->
<!--        </a-col>-->
<!--      </div>-->
<!--      <br/>-->
<!--      <br/>-->
      <a-col :span="12" v-if="doctorInfo != null" style="margin-top: 30px">
        <a-card :bordered="false">
          <a-row>
            <a-col :span="5">
              <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + doctorInfo.images" shape="square" style="width: 100px;height: 100px;"/>
            </a-col>
            <a-col :span="12">
              <div style="font-size: 20px;font-family: SimHei">{{ doctorInfo.doctorName }}</div>
              <p style="font-size: 13px;font-family: SimHei">{{ doctorInfo.hospitalName }}</p>
              <p style="font-size: 13px;font-family: SimHei">{{ doctorInfo.officesName }}</p>
              <p style="font-size: 13px;font-family: SimHei">{{ doctorInfo.doctorTitle }}</p>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="24" style="padding-left: 10px">
        <div style="font-size: 20px;font-family: SimHei;margin-top: 10px">我的排班</div>
        <div style="background:#ECECEC; padding:30px;margin-top: 30px">
          <a-card :bordered="false">
            <a-spin :spinning="dataLoading">
              <a-calendar>
                <ul slot="dateCellRender" slot-scope="value" class="events">
                  <li v-for="item in getListData(value)" :key="item.content">
                    <a-badge :status="item.type" :text="item.content" />
                  </li>
                </ul>
              </a-calendar>
            </a-spin>
          </a-card>
        </div>
      </a-col>
      <br/>
      <br/>
      <a-col :span="24" style="padding-left: 10px">
        <div style="font-size: 20px;font-family: SimHei;margin-top: 30px;margin-bottom: 18px">待挂号</div>
        <a-row>
          <a-col :span="6" v-for="(item, index) in registerList" :key="index">
            <a-card :bordered="true" hoverable>
<!--              <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images">-->
<!--                <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">-->
<!--                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">-->
<!--                </div>-->
<!--              </a-carousel>-->
              <a-card-meta :title="item.name + ' - ' + item.registerDate" :description="item.hospitalName + ' - ' + item.officesName" style="margin-top: 10px"></a-card-meta>
              <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
                <span>{{ item.startDate }} ~ {{ item.endDate }} </span> |
                <span>联系方式 - {{ item.phone }}</span> |
                <span style="color: #f5222d; font-size: 13px;">{{ item.doctorName }}</span>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
    <a-row style="margin-top: 15px" v-if="user.roleId == 74 || user.roleId == 77">
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart  v-if="!loading" type="line" height="300" :options="chartOptions" :series="series"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
    </a-row>
    <a-row style="margin-top: 15px">
      <a-col :span="24">
        <div style="background: ghostwhite; padding: 30px;" v-if="user.roleId == 74 || user.roleId == 77">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">
                    <span v-if="user.roleId == 74">本月挂号量</span>
                    <span v-if="user.roleId == 77">本月挂号数量</span>
                  </a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.monthOrderNum }}
                    <span style="font-size: 20px;margin-top: 3px">单</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月收益</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.monthOrderPrice }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">
                    <span v-if="user.roleId == 74">本年挂号量</span>
                    <span v-if="user.roleId == 77">本年挂号数量</span>
                  </a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.yearOrderNum }}
                    <span style="font-size: 20px;margin-top: 3px">单</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本年收益</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.yearOrderPrice }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <div style="background: #ECECEC; padding: 30px;" v-if="user.roleId == 75">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">排班量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ studentTitleData.scheduleNum }}
                    <span style="font-size: 20px;margin-top: 3px">次</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">总挂号</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ studentTitleData.registerNum }}
                    <span style="font-size: 20px;margin-top: 3px">次</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月处方金额</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ studentTitleData.totalCost }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月挂号金额</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ studentTitleData.totalRegisterCost }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>
    <a-row style="margin-top: 15px">
<!--      <a-col :span="9" v-if="user.roleId == 74">-->
<!--        <a-card hoverable :bordered="false" style="width: 100%">-->
<!--          <a-skeleton active v-if="loading" />-->
<!--          <apexchart v-if="!loading" type="donut" height="270" :options="chartOptions2" :series="series2"></apexchart>-->
<!--        </a-card>-->
<!--      </a-col>-->
      <a-col :span="15" v-show="false">
        <a-card hoverable :loading="loading" :bordered="false" title="公告信息" style="margin-top: 15px">
          <div style="padding: 0 22px">
            <a-list item-layout="vertical" :pagination="pagination" :data-source="bulletinList">
              <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                <template slot="actions">
                  <span key="message">
                    <a-icon type="message" style="margin-right: 8px" />
                    {{ item.date }}
                  </span>
                </template>
                <a-list-item-meta :description="item.content" style="font-size: 14px">
                  <a slot="title">{{ item.title }}</a>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import moment from "moment";
export default {
  name: 'Home',
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    })
  },
  data () {
    return {
      doctorInfo: null,
      registerList: [],
      scheduleInfo: [],
      dataLoading: false,
      newsPage: 0,
      newsContent: '',
      pagination: {
        onChange: page => {
          console.log(page)
        },
        pageSize: 2
      },
      oweBookList: [],
      messageList: [],
      newsList: [],
      student: null,
      bulletinList: [],
      titleData: {
        orderCode: 0,
        orderPrice: 0,
        pharmacyNum: 0,
        staffNum: 0
      },
      studentTitleData: {
        scheduleNum: 0,
        registerNum: 0,
        totalCost: 0,
        totalRegisterCost: 0
      },
      loading: false,
      series: [{
        name: '收益',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'line',
          height: 300
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'stepline'
        },
        dataLabels: {
          enabled: false
        },
        title: {
          text: '收入统计',
          align: 'left'
        },
        markers: {
          hover: {
            sizeOffset: 4
          }
        }
      },
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '挂号单统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
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
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 单'
            }
          }
        }
      },
      series2: [],
      chartOptions2: {
        chart: {
          type: 'donut',
          height: 300
        },
        labels: [],
        title: {
          text: '类型统计',
          align: 'left'
        },
        responsive: [{
          breakpoint: 380,
          options: {
            chart: {
              width: 200,
              fillColor: '#EB8C87'
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series3: [{
        name: 'SAMPLE A',
        data: [
          [16.4, 5.4], [21.7, 2], [25.4, 3], [19, 2], [10.9, 1], [13.6, 3.2], [10.9, 7.4], [10.9, 0], [10.9, 8.2], [16.4, 0], [16.4, 1.8], [13.6, 0.3], [13.6, 0], [29.9, 0], [27.1, 2.3], [16.4, 0], [13.6, 3.7], [10.9, 5.2], [16.4, 6.5], [10.9, 0], [24.5, 7.1], [10.9, 0], [8.1, 4.7], [19, 0], [21.7, 1.8], [27.1, 0], [24.5, 0], [27.1, 0], [29.9, 1.5], [27.1, 0.8], [22.1, 2]]
      }, {
        name: 'SAMPLE B',
        data: [
          [36.4, 13.4], [1.7, 11], [5.4, 8], [9, 17], [1.9, 4], [3.6, 12.2], [1.9, 14.4], [1.9, 9], [1.9, 13.2], [1.4, 7], [6.4, 8.8], [3.6, 4.3], [1.6, 10], [9.9, 2], [7.1, 15], [1.4, 0], [3.6, 13.7], [1.9, 15.2], [6.4, 16.5], [0.9, 10], [4.5, 17.1], [10.9, 10], [0.1, 14.7], [9, 10], [12.7, 11.8], [2.1, 10], [2.5, 10], [27.1, 10], [2.9, 11.5], [7.1, 10.8], [2.1, 12]]
      }, {
        name: 'SAMPLE C',
        data: [
          [21.7, 3], [23.6, 3.5], [24.6, 3], [29.9, 3], [21.7, 20], [23, 2], [10.9, 3], [28, 4], [27.1, 0.3], [16.4, 4], [13.6, 0], [19, 5], [22.4, 3], [24.5, 3], [32.6, 3], [27.1, 4], [29.6, 6], [31.6, 8], [21.6, 5], [20.9, 4], [22.4, 0], [32.6, 10.3], [29.7, 20.8], [24.5, 0.8], [21.4, 0], [21.7, 6.9], [28.6, 7.7], [15.4, 0], [18.1, 0], [33.4, 0], [16.4, 0]]
      }],
      chartOptions3: {
        chart: {
          height: 350,
          type: 'scatter',
          zoom: {
            enabled: true,
            type: 'xy'
          }
        },
        title: {
          text: '近十天收入统计',
          align: 'left'
        },
        xaxis: {
          tickAmount: 10,
          labels: {
            formatter: function (val) {
              return parseFloat(val).toFixed(1)
            }
          }
        },
        yaxis: {
          tickAmount: 7
        }
      }
    }
  },
  mounted () {
    console.log(this.user)
    this.loading = true
    this.selectHomeData()
    setTimeout(() => {
      this.loading = false
    }, 200)
  },
  methods: {
    getListData (value) {
      let listData = []
      this.scheduleInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.taskDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: item.name})
        }
      })
      return listData || []
    },
    newsNext () {
      if (this.newsPage + 1 === this.newsList.length) {
        this.newsPage = 0
      } else {
        this.newsPage += 1
      }
      this.newsContent = `《${this.newsList[this.newsPage].title}》 ${this.newsList[this.newsPage].content}`
    },
    selectHomeData () {
      if (this.user.roleId.toString() === '74') {
        this.$get('/cos/order-info/home/data').then((r) => {
          let titleData = { registerNum: r.data.registerNum, orderPrice: r.data.orderPrice, doctorNum: r.data.doctorNum, hospitalNum: r.data.hospitalNum }
          this.$emit('setTitle', titleData)
          this.titleData.monthOrderNum = r.data.monthOrderNum
          this.titleData.monthOrderPrice = r.data.monthOrderPrice
          this.titleData.yearOrderNum = r.data.yearOrderNum
          this.titleData.yearOrderPrice = r.data.yearOrderPrice
          this.bulletinList = r.data.bulletin
          let values = []
          if (r.data.orderNumWithinDays !== null && r.data.orderNumWithinDays.length !== 0) {
            if (this.chartOptions1.xaxis.categories.length === 0) {
              this.chartOptions1.xaxis.categories = Array.from(r.data.orderNumWithinDays, ({days}) => days)
            }
            let itemData = { name: '订单数', data: Array.from(r.data.orderNumWithinDays, ({count}) => count) }
            values.push(itemData)
            this.series1 = values
          }
          this.series[0].data = Array.from(r.data.orderPriceWithinDays, ({price}) => price)
          this.chartOptions.xaxis.categories = Array.from(r.data.orderPriceWithinDays, ({days}) => days)
          if (r.data.orderDrugType.length !== 0) {
            let series = []
            let chartOptions = []
            r.data.orderDrugType.forEach(e => {
              series.push(e.count)
              chartOptions.push(e.name)
            })
            this.series2 = series
            this.chartOptions2.labels = chartOptions
            console.log(this.chartOptions2.labels)
          }
        })
      } else if (this.user.roleId.toString() === '77') {
        this.$get(`/cos/order-info/home/data/hospital/${this.user.userId}`).then((r) => {
          let titleData = { registerNum: r.data.registerNum, orderPrice: r.data.orderPrice, doctorNum: r.data.doctorNum, hospitalNum: r.data.hospitalNum }
          this.$emit('setTitle', titleData)
          this.titleData.monthOrderNum = r.data.monthOrderNum
          this.titleData.monthOrderPrice = r.data.monthOrderPrice
          this.titleData.yearOrderNum = r.data.yearOrderNum
          this.titleData.yearOrderPrice = r.data.yearOrderPrice
          this.bulletinList = r.data.bulletin
          let values = []
          if (r.data.orderNumWithinDays !== null && r.data.orderNumWithinDays.length !== 0) {
            if (this.chartOptions1.xaxis.categories.length === 0) {
              console.log(r.data.orderNumWithinDays)
              this.chartOptions1.xaxis.categories = Array.from(r.data.orderNumWithinDays, ({days}) => days)
            }
            let itemData = { name: '订单数', data: Array.from(r.data.orderNumWithinDays, ({count}) => count) }
            values.push(itemData)
            this.series1 = values
          }
          this.series[0].data = Array.from(r.data.orderPriceWithinDays, ({price}) => price)
          this.chartOptions.xaxis.categories = Array.from(r.data.orderPriceWithinDays, ({days}) => days)
          if (r.data.orderDrugType.length !== 0) {
            let series = []
            let chartOptions = []
            r.data.orderDrugType.forEach(e => {
              series.push(e.count)
              chartOptions.push(e.name)
            })
            this.series2 = series
            this.chartOptions2.labels = chartOptions
            console.log(this.chartOptions2.labels)
          }
        })
      } else if (this.user.roleId.toString() === '75') {
        this.$get(`/cos/order-info/home/data/doctor/${this.user.userId}`).then((r) => {
          this.studentTitleData.scheduleNum = r.data.scheduleNum
          this.studentTitleData.totalRegisterCost = r.data.totalRegisterCost
          this.studentTitleData.registerNum = r.data.registerNum
          this.studentTitleData.totalCost = r.data.totalCost
          console.log(JSON.stringify(this.studentTitleData))
          this.scheduleInfo = r.data.scheduleList
          this.registerList = r.data.registerList
          // this.studentTitleData = titleData
          // this.newsList = r.data.bulletin
          // this.messageList = r.data.message
          // this.oweBookList = r.data.oweBook
          // this.student = r.data.student
          // if (this.newsList.length !== 0) {
          //   this.newsContent = `《${this.newsList[0].title}》 ${this.newsList[0].content}`
          // }
          // this.$emit('imagesInit', this.student.images)
        })
      }
    }
  }
}
</script>

<style scoped>
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
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 150px;
  line-height: 150px;
  overflow: hidden;
}
</style>
