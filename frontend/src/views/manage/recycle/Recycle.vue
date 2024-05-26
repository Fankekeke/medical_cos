<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="图书名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.bookName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="学生名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.studentName"/>
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
      <div class="operator">
<!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
        <a-button @click="batchDelete">删除</a-button>
      </div>
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
          <a-icon type="cloud" @click="handlerecordViewOpen(record)" title="详 情" style="margin-right: 10px"></a-icon>
        </template>
      </a-table>
    </div>
    <record-view
      @close="handlerecordViewClose"
      :recordShow="recordView.visiable"
      :recordData="recordView.data">
    </record-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import recordView from './RecycleView.vue'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'record',
  components: {RangeDate, recordView},
  data () {
    return {
      advanced: false,
      recordAdd: {
        visiable: false
      },
      recordEdit: {
        visiable: false
      },
      recordView: {
        visiable: false,
        data: null
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      recordList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '学号',
        dataIndex: 'code'
      }, {
        title: '学生姓名',
        dataIndex: 'studentName'
      }, {
        title: '学生照片',
        dataIndex: 'studentImages',
        customRender: (text, record, index) => {
          if (!record.studentImages) return <a-avatar shape="square" icon="record" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="record" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.studentImages.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="record" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.studentImages.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '图书名称',
        dataIndex: 'bookName'
      }, {
        title: '作者',
        dataIndex: 'auther'
      }, {
        title: '图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="record" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="record" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="record" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '图书类型',
        dataIndex: 'type',
        customRender: (text, row, index) => {
          switch (text) {
            case '1':
              return <a-tag>医疗</a-tag>
            case '2':
              return <a-tag>科技</a-tag>
            case '3':
              return <a-tag>历史</a-tag>
            case '4':
              return <a-tag>烹饪</a-tag>
            case '5':
              return <a-tag>高数</a-tag>
            case '6':
              return <a-tag>小说</a-tag>
            case '7':
              return <a-tag>诗词</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '回收地址',
        dataIndex: 'address',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '创建时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
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
    handlerecordViewOpen (row) {
      this.recordView.data = row
      this.recordView.visiable = true
    },
    handlerecordViewClose () {
      this.recordView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.recordAdd.visiable = true
    },
    handlerecordAddClose () {
      this.recordAdd.visiable = false
    },
    handlerecordAddSuccess () {
      this.recordAdd.visiable = false
      this.$message.success('新增记录成功')
      this.search()
    },
    edit (record) {
      this.$refs.recordEdit.setFormValues(record)
      this.recordEdit.visiable = true
    },
    handlerecordEditClose () {
      this.recordEdit.visiable = false
    },
    handlerecordEditSuccess () {
      this.recordEdit.visiable = false
      this.$message.success('修改记录成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/recycle-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
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
      params.recucleType = 2
      if (params.type === undefined) {
        delete params.type
      }
      this.$get('/cos/recycle-info/page', {
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
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
