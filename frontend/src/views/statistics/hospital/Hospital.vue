<template>
  <div style="width: 100%">
    <a-row style="margin-top: 25px">
      <a-col :span="8">
        <a-radio-group button-style="solid" v-model="checkFlag" style="width: 100%">
          <a-radio-button value="1" style="width: 25%;text-align: center">
            各省统计
          </a-radio-button>
          <a-radio-button value="2" style="width: 25%;text-align: center">
            房源状态
          </a-radio-button>
          <a-radio-button value="3" style="width: 25%;text-align: center">
            员工任务
          </a-radio-button>
        </a-radio-group>
      </a-col>
      <a-col :span="24" style="margin-top: 25px">
        <div style="background:#ECECEC; padding:30px" v-if="checkFlag === '1'">
          <a-alert message="此数据根据已分配销售人员进行统计" type="info" close-text="Close Now" style="margin-bottom: 15px"/>
          <a-row :gutter="15">
            <a-col :span="4" v-for="(item, index) in provinceList" style="margin-bottom: 15px" :key="index">
              <a-card :bordered="false" hoverable>
                <span slot="title">
                  <a-badge v-if="item.rent === undefined || item.rent === null" status="default" />
                  <a-badge v-else status="processing"/>
                  {{ item.name }}
                </span>
                <a-row>
                  <a-col :span="12" style="text-align: center;font-size: 18px;font-family: SimHei;">
                    <a-icon type="arrow-up" style="color: red;margin-right: 5px"/>{{ item.rent === undefined ? 0 : item.rent.length }}
                    <span style="font-size: 10px">套</span>
                  </a-col>
                  <a-col :span="12" style="text-align: center;font-size: 18px;font-family: SimHei;">
                    <a-icon type="arrow-down" style="color: green;margin-right: 5px"/>{{ item.rent === undefined ? 0 : item.rent.filter(e => e.flag == 3).length }}
                    <span style="font-size: 10px">套</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <div style="background:#ECECEC; padding:30px" v-if="checkFlag === '2'">
          <a-row :gutter="15">
            <a-col :span="6" v-for="(item, index) in rentList" style="margin-bottom: 15px" :key="index">
              <a-card :bordered="false" hoverable>
                <span slot="title">
                  {{ item.title.slice(0, 18) }}...
                </span>
                <span slot="extra" style="font-size: 13px;font-family: SimHei">
                  <span v-if="item.flag == 1 && item.staffCode !== null" style="color: green">上架</span>
                  <span v-if="item.staffCode == null" style="color: #722ed1;">未分配</span>
                  <span v-if="item.flag == 2" style="color: #f5222d;">下架</span>
                  <span v-if="item.flag == 3" style="color: #2eabff;">已被出租</span>
                </span>
                <a-row>
                  <a-col :span="24">
                    <div v-if="item.roomPictures !== null">
                      <a-popover v-for="(item1, index1) in item.roomPictures.split(',')" :key="index1">
                        <template slot="content">
                          <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                        </template>
                        <a-avatar shape="square" :size="50" icon="user" style="margin-bottom: 15px;margin-right: 10px" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                      </a-popover>
                    </div>
                  </a-col>
                  <a-col :span="24" style="font-size: 13px;font-family: SimHei;">
                    <span>{{ item.towards }}</span> |
                    <span style="margin-left: 2px">{{ item.roomNumber }}室{{ item.livingRoomNumber }}厅</span> |
                    <span style="margin-left: 2px" v-if="item.rentType == 1">整租</span>
                    <span style="margin-left: 2px" v-if="item.rentType == 2">合租</span>
                    <span style="color: #f5222d; font-size: 13px;float: right">{{ item.rentPrice }}元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <div style="background:#ECECEC; padding:30px" v-if="checkFlag === '3'">
          <a-row :gutter="25">
            <a-col :span="4" v-for="(item, index) in staffList" style="margin-bottom: 15px" :key="index">
              <a-card hoverable>
                <img
                  slot="cover"
                  alt="example"
                  style="object-fit: cover"
                  height="180"
                  :src="'http://127.0.0.1:9527/imagesWeb/'+item.staff.avatar.split(',')[0]"
                />
                <a-card-meta>
                  <template slot="title">
                    <span style="font-size: 13px;font-family: SimHei">{{ item.staff.staffName }}</span>
                  </template>
                  <template slot="description">
                    <a-row style="margin-top: 15px">
                      <a-col :span="12" style="text-align: center;font-size: 18px;font-family: SimHei;">
                        <a-icon type="container" style="margin-right: 5px"/>{{ item.rent === undefined ? 0 : item.rent.length }}
                        <span style="font-size: 10px">套</span>
                      </a-col>
                      <a-col :span="12" style="text-align: center;font-size: 18px;font-family: SimHei;">
                        <a-icon type="arrow-down" style="color: green;margin-right: 5px"/>{{ item.rent === undefined ? 0 : item.rent.filter(e => e.flag == 3).length }}
                        <span style="font-size: 10px">套</span>
                      </a-col>
                    </a-row>
                  </template>
                </a-card-meta>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <!--          <a-col :span="24">-->
        <!--            <apexchart type="radar" height="350" :options="chartOptions" :series="series"></apexchart>-->
        <!--          </a-col>-->
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'House',
  data () {
    return {
      provinceList: [],
      rentList: [],
      staffList: [],
      checkFlag: '1',
      series: [{
        name: 'Series 1',
        data: [80, 50, 30, 40, 100, 20],
      }],
      chartOptions: {
        chart: {
          height: 350,
          type: 'radar',
        },
        title: {
          text: 'Basic Radar Chart'
        },
        xaxis: {
          categories: ['January', 'February', 'March', 'April', 'May', 'June']
        }
      },
    }
  },
  mounted() {
    this.selectHouseInfoByProvince()
    this.selectRent()
    this.selectRentChargeByStaff()
  },
  methods: {
    selectHouseInfoByProvince () {
      this.$get('/cos/rent-charge/rentCharge/province').then((r) => {
        this.provinceList = r.data.data
      })
    },
    selectRent () {
      this.$get('/cos/rent-info/page', {size: 999}).then((r) => {
        this.rentList = r.data.data.records
      })
    },
    selectRentChargeByStaff () {
      this.$get('/cos/rent-charge/rentCharge/staff').then((r) => {
        this.staffList = r.data.data
        console.log(JSON.stringify(this.staffList))
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
