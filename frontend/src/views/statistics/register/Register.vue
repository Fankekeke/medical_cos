<template>
  <div style="width: 100%">
    <a-row style="margin-top: 25px">
      <a-col :span="24">
        <div style="background: #ECECEC; padding: 30px;">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月排班量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.scheduleNum }}
                    <span style="font-size: 20px;margin-top: 3px">次</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月总挂号</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.registerNum }}
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
                    {{ titleData.drugTotal }}
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
                    {{ titleData.registerTotal }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-col>
      <a-row :gutter="20">
        <a-col :span="24">
          <a-card hoverable :bordered="false" style="width: 100%">
            <a-skeleton active v-if="chartLoading" />
            <apexchart v-if="!chartLoading" type="bar" height="300" :options="chartOptions" :series="series"></apexchart>
          </a-card>
        </a-col>
        <a-col :span="24">
          <a-card hoverable :bordered="false" style="width: 100%">
            <a-skeleton active v-if="chartLoading" />
            <apexchart v-if="!chartLoading" type="area" height="300" :options="chartOptions1" :series="series1"></apexchart>
          </a-card>
        </a-col>
      </a-row>
      <a-col :span="24" style="margin-top: 25px">
        <div style="background:#ECECEC; padding:30px">
          <div :class="advanced ? 'search' : null">
            <!-- 搜索区域 -->
            <a-form layout="horizontal">
              <a-row :gutter="15">
                <div :class="advanced ? null: 'fold'">
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="医生姓名"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.doctorName"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="科室名称"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.officesName"/>
                    </a-form-item>
                  </a-col>
                </div>
                <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
          </span>
              </a-row>
            </a-form>
          </div>
          <a-skeleton :loading="loading" active :paragraph="{ rows: 10 }"/>
          <a-alert v-if="!loading" message="此数据根据医院信息进行统计" type="info" close-text="Close Now" style="margin-bottom: 15px"/>
          <a-row :gutter="15" v-if="!loading">
            <a-col :span="4" v-for="(item, index) in dataList" style="margin-bottom: 15px" :key="index" @click="selectRateByDoctorId(item.id)">
              <a-card :bordered="false" hoverable>
                <span slot="title">
                  <a-badge status="processing"/>
                  {{ item.doctorName }}
                </span>
                <a-row>
                  <a-col :span="24" style="text-align: center;font-size: 13px;font-family: SimHei;">
                    {{ item.hospitalName == null ? '- -' : item.hospitalName }}
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
          <a-pagination v-model="pagination.defaultCurrent" :pageSize="pagination.defaultPageSize" :total="pagination.total" show-less-items style="margin-top: 15px" @change="pageChange"/>
        </div>
        <!--          <a-col :span="24">-->
        <!--            <apexchart type="radar" height="350" :options="chartOptions" :series="series"></apexchart>-->
        <!--          </a-col>-->
      </a-col>
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
      series: [{
        name: '排班量',
        data: []
      }, {
        name: '挂号量',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'bar',
          height: 350
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
          categories: [],
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
          y: {}
        }
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
    }
  },
  watch: {
  },
  mounted () {
    this.search()
  },
  methods: {
    search () {
      this.selectHospitalRate({
        ...this.queryParams
      })
    },
    selectRateByDoctorId (doctorId) {
      this.chartLoading = true
      this.$get(`/cos/doctor-info/selectRateByDoctorId/${doctorId}`).then((r) => {
        this.titleData.scheduleNum = r.data.scheduleNum
        this.titleData.drugTotal = r.data.drugTotal
        this.titleData.registerNum = r.data.registerNum
        this.titleData.registerTotal = r.data.registerTotal
        let registerDay = r.data.series.registerDay
        let scheduleDay = r.data.series.scheduleDay
        this.series = [{
          name: '排班量',
          data: Array.from(registerDay, ({count}) => count)
        }, {
          name: '挂号量',
          data: Array.from(scheduleDay, ({count}) => count)
        }]
        this.chartOptions.xaxis.categories = Array.from(registerDay, ({date}) => date)

        let registerPriceDay = r.data.series1.registerPriceDay
        let drugPriceDay = r.data.series1.drugPriceDay
        this.series1 = [{
          name: "挂号金额",
          data: Array.from(registerPriceDay, ({price}) => price)
        }, {
          name: "处方金额",
          data: Array.from(drugPriceDay, ({price}) => price)
        }]
        this.chartOptions1.labels = Array.from(registerPriceDay, ({date}) => date)
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
