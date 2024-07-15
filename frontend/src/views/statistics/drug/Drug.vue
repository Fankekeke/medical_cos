<template>
  <div style="width: 100%">
    <a-row style="margin-top: 25px">
      <a-row :gutter="20">
        <a-col :span="24">
          <div style="background: #ECECEC; padding: 30px;">
            <a-row :gutter="20">
              <a-col :span="8">
                <a-month-picker placeholder="选择月份" @change="onChange" v-model="queryDate" style="width: 100%"/>
              </a-col>
              <a-col :span="1">
                <a-button type="primary" @click="selectHospitalRate">
                  查询
                </a-button>
              </a-col>
            </a-row>
          </div>
        </a-col>
        <a-col :span="24">
          <a-row :gutter="20">
            <a-col :span="6">
              <a-card hoverable :bordered="false" style="width: 100%">
                <a-skeleton active v-if="chartLoading" />
                <apexchart v-if="!chartLoading" type="pie" width="300" :options="chartOptions2" :series="series2"></apexchart>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable :bordered="false" style="width: 100%">
                <a-skeleton active v-if="chartLoading" />
                <apexchart v-if="!chartLoading" type="pie" width="300" :options="chartOptions3" :series="series3"></apexchart>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable :bordered="false" style="width: 100%">
                <a-skeleton active v-if="chartLoading" />
                <apexchart v-if="!chartLoading" type="donut" width="300" :options="chartOptions4" :series="series4"></apexchart>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable :bordered="false" style="width: 100%">
                <a-skeleton active v-if="chartLoading" />
                <apexchart v-if="!chartLoading" type="donut" width="300" :options="chartOptions5" :series="series5"></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-col>
        <a-col :span="24">
          <a-card hoverable :bordered="false" style="width: 100%">
            <a-skeleton active v-if="chartLoading" />
            <apexchart v-if="!chartLoading" type="area" height="300" :options="chartOptions1" :series="series1"></apexchart>
          </a-card>
        </a-col>
      </a-row>
    </a-row>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'House',
  data () {
    return {
      advanced: false,
      queryParams: {},
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 12,
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      titleData: {
        scheduleNum: 0,
        drugTotal: 0,
        registerNum: 0,
        registerTotal: 0,
      },
      dataList: [],
      loading: false,
      chartLoading: false,
      checkFlag: '1',
      series2: [42, 47, 52, 58, 65],
      chartOptions2: {
        chart: {
          width: 380,
          type: 'pie'
        },
        labels: ['Rose A', 'Rose B', 'Rose C', 'Rose D', 'Rose E'],
        fill: {
          opacity: 1
        },
        stroke: {
          width: 1,
          colors: undefined
        },
        yaxis: {
          show: false
        },
        legend: {
          position: 'bottom'
        },
        plotOptions: {
          polarArea: {
            rings: {
              strokeWidth: 0
            },
            spokes: {
              strokeWidth: 0
            },
          }
        },
        theme: {
          monochrome: {
            enabled: true,
            shadeTo: 'light',
            shadeIntensity: 0.6
          }
        }
      },
      series3: [42, 47, 52, 58, 65],
      chartOptions3: {
        chart: {
          width: 380,
          type: 'pie'
        },
        labels: ['Rose A', 'Rose B', 'Rose C', 'Rose D', 'Rose E'],
        fill: {
          opacity: 1
        },
        stroke: {
          width: 1,
          colors: undefined
        },
        yaxis: {
          show: false
        },
        legend: {
          position: 'bottom'
        },
        plotOptions: {
          polarArea: {
            rings: {
              strokeWidth: 0
            },
            spokes: {
              strokeWidth: 0
            },
          }
        },
        theme: {
          monochrome: {
            enabled: true,
            shadeTo: 'light',
            shadeIntensity: 0.6
          }
        }
      },
      series4: [44, 55, 41, 17, 15],
      chartOptions4: {
        chart: {
          type: 'donut',
        },
        legend: {
          position: 'bottom'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series5: [44, 55, 41, 17, 15],
      chartOptions5: {
        chart: {
          type: 'donut',
        },
        legend: {
          position: 'bottom'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series1: [{
        name: "挂号金额",
        data: []
      }, {
        name: "处方金额",
        data: []
      }],
      chartOptions1: {
        chart: {
          type: 'area',
          height: 350,
          zoom: {
            enabled: false
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight'
        },
        title: {
          text: '挂号金额与处方金额统计',
          align: 'left'
        },
        labels: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30],
        // xaxis: {
        //   type: 'datetime',
        // },
        yaxis: {
          opposite: true
        },
        legend: {
          horizontalAlign: 'left'
        }
      },
      queryDate: null
    }
  },
  watch: {
  },
  mounted () {
    this.queryDate = moment(new Date())
    this.selectRate()
  },
  methods: {
    onChange (date, dateString) {
      this.queryDate = dateString
    },
    search () {
      this.selectHospitalRate({
        ...this.queryParams
      })
    },
    selectRate () {
      let date = moment(this.queryDate).format('YYYY-MM')
      this.chartLoading = true
      this.$get(`/cos/order-info/selectDrugRate`, {
        date
      }).then((r) => {
        // let registerDay = r.data.series.registerDay
        // let scheduleDay = r.data.series.scheduleDay
        // this.series = [{
        //   name: '排班量',
        //   data: Array.from(registerDay, ({count}) => count)
        // }, {
        //   name: '挂号量',
        //   data: Array.from(scheduleDay, ({count}) => count)
        // }]
        // this.chartOptions.xaxis.categories = Array.from(registerDay, ({date}) => date)
        setTimeout(() => {
          this.chartLoading = false
        }, 200)
      })
    },
    pageChange (page, pageSize) {
      this.pagination.defaultCurrent = page
      this.search()
    },
    selectHospitalRate (params = {}) {
      this.loading = true
      console.log(this.pagination.defaultCurrent)
      params.current = this.pagination.defaultCurrent
      params.size = this.pagination.defaultPageSize
      this.$get(`/cos/doctor-info/page`, {
        ...params
      }).then((r) => {
        this.dataList = r.data.data.records
        this.pagination.defaultCurrent = r.data.data.current
        this.pagination.total = r.data.data.total
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-alert-message {
  font-size: 14px;
  font-family: SimHei;
}
</style>
