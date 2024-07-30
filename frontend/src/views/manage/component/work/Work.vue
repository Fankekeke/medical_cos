<template>
  <div style="background:#ECECEC;">
    <div style="height: 450px;">
      <div style="height: 350px;background-image: url(../static/img/house.jpg);padding: 50px;">
        <div style="font-size: 35px;font-weight: 500;color: white;font-family: SimHei">选择医院挂号</div>
        <div style="font-size: 22px;font-weight: 500;color: white;font-family: SimHei">医院列表</div>
        <div style="height: 150px;margin-top: 100px">
          <a-card :bordered="false" hoverable style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
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
          <a-card :bordered="false" hoverable style="box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff;margin-top: 100px">
            <a-row style="padding: 50px;margin: 0 auto">
              <a-col :span="24" style="font-size: 15px;font-family: SimHei">
                <div style="margin-top: 30px">
                  <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="hospitalList">
                    <div slot="footer"><b>ant design vue</b> footer part</div>
                    <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                      <a-list-item-meta :description="item.hospitalAddress">
                        <a-carousel autoplay style="height: 200px;" v-if="item.images">
                          <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                            <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 200px">
                          </div>
                        </a-carousel>
                        <a slot="title" >{{ item.hospitalName }}</a>
                        <a-avatar slot="avatar" :src="item.avatar" />
                      </a-list-item-meta>
                      {{ item.hospitalNature }} | {{ item.hospitalGrade }}
                    </a-list-item>
                  </a-list>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {mapState} from 'vuex'
export default {
  name: 'Work',
  data () {
    return {
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      pagination: {
        onChange: page => {
          console.log(page);
        },
        pageSize: 10,
      },
      key: '',
      hospitalList: [],
      roomTypeList: [],
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
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
