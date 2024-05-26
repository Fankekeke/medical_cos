<template>
  <a-row :gutter="20" style="width: 100%;margin-top: 20px">
    <a-col :span="24" style="margin-top: 15px;margin-bottom: 35px;">
      所属班级：
      <a-select @change="handleChange" style="width: 300px">
        <a-select-option :value="item.id" v-for="(item, index) in classList" :key="index">{{ item.className }}</a-select-option>
      </a-select>
    </a-col>
    <a-col :span="24"></a-col>
    <div v-if="studentList.length === 0" style="text-align: center;">
      <a-icon type="smile" theme="twoTone" style="font-size: 75px;margin-top: 30px"/>
      <h1 style="margin-top: 20px">暂无学生信息</h1>
    </div>
    <a-col :span="6" v-for="(item, index) in studentList" :key="index" style="margin-bottom: 15px">
      <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
        <a-card :bordered="false" @click="handleUserViewOpen(item)" hoverable>
          <a-carousel autoplay style="height: 150px;" v-if="item.images">
            <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
              <a-avatar
                shape="square" :size="100"
                :src="'http://127.0.0.1:9527/imagesWeb/' + item"
              />
            </div>
          </a-carousel>
          <a-card-meta :title="item.studentName" :description="'学号：' + item.code" style="margin-top: 10px"></a-card-meta>
          <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
            <span>出生日期：{{ item.birthday }}</span> |
            <span style="color: #f5222d; font-size: 13px;float: right">{{ item.major }}</span>
          </div>
        </a-card>
      </div>
    </a-col>
    <student-view
      @close="handleUserViewClose"
      :userShow="userView.visiable"
      :userData="userView.data">
    </student-view>
  </a-row>
</template>

<script>
import StudentView from '../../manage/student/StudentView'

export default {
  name: 'Cart',
  components: {StudentView},
  data () {
    return {
      drugList: [],
      userView: {
        visiable: false,
        data: null
      },
      drugView: {
        visiable: false,
        data: null
      },
      cartView: {
        visiable: false,
        data: []
      },
      key: '',
      classList: [],
      studentList: []
    }
  },
  mounted () {
    this.selectClassList()
  },
  methods: {
    selectClassList () {
      this.$get('/cos/class-info/list').then((r) => {
        this.classList = r.data.data
      })
    },
    handleChange (value) {
      if (value) {
        this.selectStudentList(value)
      }
    },
    selectStudentList (classId) {
      this.$get(`/cos/student-info/selectStudentByClass/${classId}`).then((r) => {
        console.log(r.data.data)
        this.studentList = r.data.data
      })
    },
    handleUserViewOpen (row) {
      this.userView.data = row
      this.userView.visiable = true
    },
    handleUserViewClose () {
      this.userView.visiable = false
    },
    cartOpen () {
      this.cartView.visiable = true
    },
    handleDrugViewClose () {
      this.drugView.visiable = false
    },
    handleCartViewClose () {
      this.cartView.visiable = false
    },
    handleCartViewSuccess () {
      this.cartView.visiable = false
      this.cartView.data = []
      this.$message.success('添加订单成功')
    },
    handleDrugViewSuccess (drugData) {
      let check = false
      this.cartView.data.forEach(e => {
        if (e.id === drugData.id) {
          e.total = drugData.total
          check = true
        }
      })
      if (!check) {
        this.cartView.data.push(drugData)
      }
      this.drugView.visiable = false
    },
    drugDetail (row) {
      this.drugView.visiable = true
      this.drugView.data = row
    },
    selectDrugList () {
      this.$get(`/cos/commodity-info/commodity/list`, {key: this.key}).then((r) => {
        this.drugList = r.data.data
      })
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
