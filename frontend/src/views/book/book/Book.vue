<template>
  <a-row :gutter="20" style="width: 100%;margin-top: 20px">
<!--    <a-col :span="24" style="margin-top: 15px;margin-bottom: 35px;">-->
<!--      <a-input-search placeholder="搜索图书" style="width: 300px;margin: 0 auto" v-model="key" @search="selectDrugList" />-->
<!--    </a-col>-->
    <a-col :span="24"></a-col>
    <div v-if="bookList1.length === 0" style="text-align: center;">
      <a-icon type="smile" theme="twoTone" style="font-size: 75px;margin-top: 30px"/>
      <h1 style="margin-top: 20px">暂无图书信息</h1>
    </div>
    <a-col :span="6" v-for="(item, index) in bookList1" :key="index" style="margin-bottom: 15px">
      <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
        <a-card :bordered="false" hoverable>
          <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
            <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
              <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
            </div>
          </a-carousel>
          <a-card-meta :title="item.bookName" :description="item.bookName.slice(0, 18)+'...'" style="margin-top: 10px"></a-card-meta>
          <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
            <span>{{ item.auther }}</span> |
            <span>{{ item.press }}</span> |
            <span style="color: #f5222d; font-size: 13px;">￥{{ item.price }}</span>
            <a style="font-size: 13px;float: right" @click="handlebookAddOpen(item)">回收捐赠</a>
          </div>
        </a-card>
      </div>
    </a-col>
    <book-view
      @close="handlebookViewClose"
      :bookShow="bookView.visiable"
      :bookData="bookView.data">
    </book-view>
    <book-recycle-add
      @success="handlebookAddSuccess"
      @close="handlebookAddClose"
      :classAddVisiable="bookAdd.visiable"
      :classAddData="bookAdd.data">
    </book-recycle-add>
  </a-row>
</template>

<script>
import BookView from '../../manage/book/BookView.vue'
import BookRecycleAdd from './BookRecycleAdd.vue'
import {mapState} from 'vuex'

export default {
  name: 'Cart',
  components: {BookView, BookRecycleAdd},
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      drugList: [],
      bookView: {
        visiable: false,
        data: null
      },
      bookAdd: {
        visiable: false,
        data: null
      },
      cartView: {
        visiable: false,
        data: []
      },
      key: '',
      classList: [],
      bookList1: [],
      bookList2: []
    }
  },
  mounted () {
    this.selectUseBookList()
    this.selectBookList()
  },
  methods: {
    selectUseBookList () {
      this.$get(`/cos/student-info/use/book/list/${this.currentUser.userId}`).then((r) => {
        this.bookList1 = r.data.data
      })
    },
    selectBookList () {
      this.$get(`/cos/student-info/book/list/${this.currentUser.userId}`).then((r) => {
        this.bookList2 = r.data.data
      })
    },
    handlebookAddOpen (row) {
      this.bookAdd.visiable = true
      this.bookAdd.data = row
    },
    handlebookAddSuccess () {
      this.bookAdd.visiable = false
      this.$message.success('回收捐赠成功')
      this.selectUseBookList()
    },
    handlebookViewOpen (row) {
      this.bookView.data = row
      this.bookView.visiable = true
    },
    handlebookAddClose () {
      this.bookAdd.visiable = false
      this.selectUseBookList()
    },
    handlebookViewClose () {
      this.bookView.visiable = false
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
