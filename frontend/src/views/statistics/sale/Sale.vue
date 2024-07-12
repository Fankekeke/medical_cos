<template>
  <a-card :bordered="false" class="card-area">
    <a-row :gutter="20">
      <a-col :span="24">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="24">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions2" :series="series2"></apexchart>
        </a-card>
      </a-col>
    </a-row>
    <div style="margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="24">
          <div :class="advanced ? 'search' : null">
            <!-- 搜索区域 -->
            <a-form layout="horizontal">
              <a-row :gutter="15">
                <div :class="advanced ? null: 'fold'">
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="医院名称"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.hospitalName"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="医院地区"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.hospitalArea"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="医院类型"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.hospitalNature"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="24">
                    <a-form-item
                      label="医院等级"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}">
                      <a-input v-model="queryParams.hospitalGrade"/>
                    </a-form-item>
                  </a-col>
                </div>
                <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
              </a-row>
            </a-form>
          </div>
          <div>
<!--            <div class="operator">-->
<!--            </div>-->
            <!-- 表格区域 -->
            <a-table ref="TableInfo"
                     :columns="columns"
                     :rowKey="record => record.id"
                     :dataSource="dataSource"
                     :pagination="pagination"
                     :loading="loading"
                     :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                     :scroll="{ x: 900 }"
                     @change="handleTableChange">
              <template slot="titleShow" slot-scope="text, record">
                <template>
                  <a-tooltip>
                    <template slot="title">
                      {{ record.title }}
                    </template>
                    {{ record.title.slice(0, 8) }} ...
                  </a-tooltip>
                </template>
              </template>
              <template slot="operation" slot-scope="text, record">
                <a-icon type="cloud" @click="selectOrderRateByHospital(record.id)" title="详 情"></a-icon>
              </template>
            </a-table>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'Work',
  data () {
    return {
      advanced: false,
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      workStatusList: [],
      loading: false,
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '药品数量统计',
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
          type: 'bar',
          height: 300
        },
        title: {
          text: '药品收益统计',
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
              return val + ' 元'
            }
          }
        }
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '医院编号',
        dataIndex: 'code',
        customRender: (text, row, index) => {
          if (text) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '医院名称',
        dataIndex: 'hospitalName'
      }, {
        title: '医院地区',
        dataIndex: 'hospitalArea'
      }, {
        title: '医院院长姓名',
        dataIndex: 'hospitalDeanName',
        customRender: (text, row, index) => {
          if (text !== null && text !== '0') {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '建院年份',
        dataIndex: 'hospitalYear',
        customRender: (text, row, index) => {
          if (text !== null && text !== '0') {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '医院类别',
        dataIndex: 'hospitalNature'
      }, {
        title: '医院图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    selectOrderRateByHospital (hospitalId) {
      this.loading = true
      this.$get(`/cos/order-info/selectOrderRateByHospital/` + hospitalId).then((r) => {
        this.series1 = []
        this.chartOptions1.xaxis.categories = []
        if (r.data.num.length !== 0) {
          this.chartOptions1.xaxis.categories = Array.from(r.data.num, ({name}) => name)
          this.series1[0] = { name: Array.from(r.data.num, ({name}) => name), data: Array.from(r.data.num, ({value}) => value) }
          console.log(this.series1)
          console.log(this.chartOptions1.xaxis.categories)
        }
        if (r.data.price.length !== 0) {
          this.chartOptions2.xaxis.categories = Array.from(r.data.price, ({name}) => name)
          this.series2[0] = { name: Array.from(r.data.price, ({name}) => name), data: Array.from(r.data.price, ({value}) => value) }
          console.log(this.series2)
          console.log(this.chartOptions2.xaxis.categories)
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.type === undefined) {
        delete params.type
      }
      this.$get('/cos/hospital-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
