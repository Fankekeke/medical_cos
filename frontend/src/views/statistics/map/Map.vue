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
            <div v-if="rentShow" class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden">
              <a-carousel autoplay style="height: 250px;" v-if="rentData.roomPictures !== undefined && rentData.roomPictures !== ''">
                <div style="width: 100%;height: 250px" v-for="(item, index) in rentData.roomPictures.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 100%">
                </div>
              </a-carousel>
              <a-card :title="rentData.houseAddress" :bordered="false">
                <a slot="extra" @click="rentNavigation" style="margin-right: 10px">导航</a>
                <a slot="extra" @click="rentBack">返回</a>
              </a-card>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="24"><b>出租标题：</b>
                    {{ rentData.title !== null ? rentData.title : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="8"><b>租金每月：</b>
                    {{ rentData.rentPrice !== null ? rentData.rentPrice : '- -' }}元
                  </a-col>
                  <a-col :span="8"><b>出租状态：</b>
                    <span v-if="rentData.flag === 1">上架</span>
                    <span v-if="rentData.flag === 2">下架</span>
                    <span v-if="rentData.flag === 2">已被出租</span>
                  </a-col>
                  <a-col :span="8"><b>合租类型：</b>
                    <span v-if="rentData.rentType === 1">整租</span>
                    <span v-if="rentData.rentType === 2">合租</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="8"><b>访问量：</b>
                    {{ rentData.views !== null ? rentData.views : '- -' }}次
                  </a-col>
                  <a-col :span="8"><b>房间类型：</b>
                    <span v-if="rentData.roomType === 1">主卧</span>
                    <span v-if="rentData.roomType === 2">次卧</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>发布时间：</b>
                    {{ rentData.createDate !== null ? rentData.createDate : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row :gutter="10" style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">包含设施</span></a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasBed == 1 ? 'green' : 'red'}">床</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/chuang.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasRefrigerator == 1 ? 'green' : 'red'}">冰箱</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/dankaimenbingxiang.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasAirConditioner == 1 ? 'green' : 'red'}">空调</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/kongtiao.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasBroadband == 1 ? 'green' : 'red'}">宽带</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/kuandai.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasBalcony == 1 ? 'green' : 'red'}">阳台</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/loutianyangtai.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasGasStoves == 1 ? 'green' : 'red'}">燃气灶</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/ranqizao.png">
                  </a-col>
                  <a-col :span="24" style="margin: 20px 0px"></a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasWaterHeater == 1 ? 'green' : 'red'}">热水器</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/reshuiqi.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasSofa == 1 ? 'green' : 'red'}">沙发</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/shafa_1.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasCook == 1 ? 'green' : 'red'}">做饭</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/weibolu.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasBathroom == 1 ? 'green' : 'red'}">卫生间</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/weiyu.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasTelevision == 1 ? 'green' : 'red'}">电视</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/dianshi.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasWashingMachine == 1 ? 'green' : 'red'}">洗衣机</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/xiyiji.png">
                  </a-col>
                  <a-col :span="24" style="margin: 20px 0px"></a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasWardrobe == 1 ? 'green' : 'red'}">衣柜</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/yigui.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasRangeHood == 1 ? 'green' : 'red'}">油烟机</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/youyanji.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasHeating == 1 ? 'green' : 'red'}">暖气</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/zaotai.png">
                  </a-col>
                  <a-col :span="4" style="text-align: center">
                    <div :style="{color: rentData.hasDoorLock == 1 ? 'green' : 'red'}">智能门锁</div>
                    <img src="http://127.0.0.1:9527/imagesWeb/zhinengmensuo.png">
                  </a-col>
                </a-row>
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
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">租金备注</span></a-col>
                  {{ rentData.rentRemark !== null ? rentData.rentRemark : '- -' }}元
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">出租要求</span></a-col>
                  {{ rentData.rentalRequest !== null ? rentData.rentalRequest : '- -' }}元
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">房间信息</span></a-col>
                  <a-col :span="12"><b>房间数量：</b>
                    {{ houseInfo.roomNumber !== null ? houseInfo.roomNumber : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>客厅数量：</b>
                    {{ houseInfo.livingRoomNumber !== null ? houseInfo.livingRoomNumber : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>卫生间数量：</b>
                    {{ houseInfo.bathroomNumber !== null ? houseInfo.bathroomNumber : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>装修类型：</b>
                    <span v-if="houseInfo.decorationType === 1">精装修</span>
                    <span v-if="houseInfo.decorationType === 2">普通装修</span>
                    <span v-if="houseInfo.decorationType === 3">暂无装修</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>小区编号：</b>
                    {{ community.code !== null ? community.code : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>所在地：</b>
                    {{ houseInfo.address !== null ? houseInfo.address : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>房间面积：</b>
                    {{ houseInfo.roomSize !== null ? houseInfo.roomSize : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>楼层：</b>
                    {{ houseInfo.floor !== null ? houseInfo.floor : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>经度：</b>
                    {{ houseInfo.longitude !== null ? houseInfo.longitude : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>纬度：</b>
                    {{ houseInfo.latitude !== null ? houseInfo.latitude : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>房屋类型：</b>
                    <span v-if="houseInfo.rentType === 1">普通住宅</span>
                    <span v-if="houseInfo.rentType === 2">高层楼</span>
                    <span v-if="houseInfo.rentType === 3">别墅</span>
                    <span v-if="houseInfo.rentType === 3">大平层</span>
                  </a-col>
                  <a-col :span="12"><b>朝向：</b>
                    {{ houseInfo.towards !== null ? houseInfo.towards : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>标签：</b>
                    {{ houseInfo.tags !== null ? houseInfo.tags : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>所属小区：</b>
                    {{ community.communityName !== null ? community.communityName : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="community != null">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">所属物业配套</span></a-col>
                  <a-col :span="12"><b>绿化率：</b>
                    {{ community.greeningRate !== null ? community.greeningRate : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>建筑类型：</b>
                    <span v-if="community.buildingType === 1">居住建筑</span>
                    <span v-if="community.buildingType === 2">公共建筑</span>
                    <span v-if="community.buildingType === 3">工业建筑</span>
                    <span v-if="community.buildingType === 4">农业建筑</span>
                    <span v-if="community.buildingType === 5">大量性建筑</span>
                    <span v-if="community.buildingType == null">- -</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="community != null">
                  <a-col :span="12"><b>统一供暖：</b>
                    <span v-if="community.unifiedHeating === 1" style="color: green">是</span>
                    <span v-if="community.unifiedHeating === 2" style="color: red">否</span>
                    <span v-if="community.unifiedHeating == null">- -</span>
                  </a-col>
                  <a-col :span="12"><b>供水供电：</b>
                    <span v-if="community.waterSupply === 1">民用</span>
                    <span v-if="community.waterSupply === 2">商用</span>
                    <span v-if="community.waterSupply == null">- -</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="community != null">
                  <a-col :span="12"><b>停车位：</b>
                    {{ community.parkingSpace !== null ? community.parkingSpace : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>所属商圈：</b>
                    {{ community.businessDistrict !== null ? community.businessDistrict : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="community != null">
                  <a-col :span="12"><b>物业费：</b>
                    {{ community.propertyCosts !== null ? community.propertyCosts : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>停车费：</b>
                    {{ community.parkingFee !== null ? community.parkingFee : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="community != null">
                  <a-col :span="12"><b>物业公司：</b>
                    {{ community.propertyCompany !== null ? community.propertyCompany : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>开发商：</b>
                    {{ community.developer !== null ? community.developer : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;margin-bottom: 25px" v-if="community != null">
                  <a-col :span="12"><b>车位管理费：</b>
                    {{ community.parkingManagementFee !== null ? community.parkingManagementFee : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>位置：</b>
                    {{  rentData.province + rentData.city + rentData.area }}
                  </a-col>
                </a-row>
              </div>
            </div>
            <div v-if="communityShow" class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden">
              <a-card :title="communityData.communityName" :bordered="false">
                <a slot="extra" @click="rentNavigation" style="margin-right: 10px">导航</a>
                <a slot="extra" @click="communityBack">返回</a>
              </a-card>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
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
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">小区信息</span></a-col>
                  <a-col :span="12"><b>绿化率：</b>
                    {{ communityData.greeningRate !== null ? communityData.greeningRate : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>建筑类型：</b>
                    <span v-if="communityData.buildingType === 1">居住建筑</span>
                    <span v-if="communityData.buildingType === 2">公共建筑</span>
                    <span v-if="communityData.buildingType === 3">工业建筑</span>
                    <span v-if="communityData.buildingType === 4">农业建筑</span>
                    <span v-if="communityData.buildingType === 5">大量性建筑</span>
                    <span v-if="communityData.buildingType == null">- -</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="12"><b>统一供暖：</b>
                    <span v-if="communityData.unifiedHeating === 1" style="color: green">是</span>
                    <span v-if="communityData.unifiedHeating === 2" style="color: red">否</span>
                    <span v-if="communityData.unifiedHeating == null">- -</span>
                  </a-col>
                  <a-col :span="12"><b>供水供电：</b>
                    <span v-if="communityData.waterSupply === 1">民用</span>
                    <span v-if="communityData.waterSupply === 2">商用</span>
                    <span v-if="communityData.waterSupply == null">- -</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="12"><b>停车位：</b>
                    {{ communityData.parkingSpace !== null ? communityData.parkingSpace : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>所属商圈：</b>
                    {{ communityData.businessDistrict !== null ? communityData.businessDistrict : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="24"><b>物业费：</b>
                    {{ communityData.propertyCosts !== null ? communityData.propertyCosts : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="24"><b>停车费：</b>
                    {{ communityData.parkingFee !== null ? communityData.parkingFee : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="24"><b>物业公司：</b>
                    {{ communityData.propertyCompany !== null ? communityData.propertyCompany : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="24"><b>开发商：</b>
                    {{ communityData.developer !== null ? communityData.developer : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col :span="12"><b>车位管理费：</b>
                    {{ communityData.parkingManagementFee !== null ? communityData.parkingManagementFee : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>产权年限：</b>
                    {{ communityData.propertyTenure !== null ? communityData.propertyTenure : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;margin-bottom: 25px" v-if="communityData != null">
                  <a-col :span="12"><b>总户数：</b>
                    {{ communityData.totalHouses !== null ? communityData.totalHouses : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>总建面积：</b>
                    {{ communityData.totalConstructionArea !== null ? communityData.totalConstructionArea : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col><span style="font-size: 14px;font-weight: 650;color: #000c17">房价走势-{{ new Date().getFullYear() }}年</span></a-col>
                </a-row>
                <a-card v-if="echartsShow" hoverable :bordered="false" style="width: 100%">
                  <a-skeleton active v-if="checkLoading" />
                  <apexchart v-if="!checkLoading" type="bar" height="180" :options="chartOptions" :series="series"></apexchart>
                </a-card>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="communityData != null">
                  <a-col><span style="font-size: 14px;font-weight: 650;color: #000c17">出租情况</span></a-col>
                </a-row>
                <br/>
                <div style="text-align: center">
                  <a-icon type="smile" theme="twoTone" style="font-size: 75px"/>
                  <p style="margin-top: 25px;font-size: 20px;font-family: SimHei">当前小区共有套{{ communityRent }}房源!</p>
                </div>
              </div>
            </div>
            <a-tabs default-active-key="1" v-if="!rentShow && !communityShow" style="text-align: center;font-size: 13px">
              <a-tab-pane key="1" tab="房源" style="height: 93vh; overflow-y: auto;padding: 15px">
                <div style="width: 100%;margin-bottom: 15px;text-align: left" v-for="(item, index) in rentList" :key="index">
                  <a-divider orientation="left">
                    <span style="font-size: 12px;font-family: SimHei;">{{item.province}}{{item.city}}{{item.area}} - {{item.communityName}}</span>
                  </a-divider>
                  <a-card :bordered="false" @click="rentDetail(item)" hoverable>
                    <div v-if="item.roomPictures !== null">
                      <a-popover v-for="(item1, index1) in item.roomPictures.split(',')" :key="index1">
                        <template slot="content">
                          <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                        </template>
                        <a-avatar shape="square" :size="50" icon="user" style="margin-bottom: 15px;margin-right: 10px" :src="'http://127.0.0.1:9527/imagesWeb/'+item1"/>
                      </a-popover>
                    </div>
                    <a-card-meta :title="item.houseAddress" :description="item.rentalRequest.slice(0, 25)+'...'"></a-card-meta>
                    <div style="font-size: 12px;font-family: SimHei;margin-top: 5px">
                      <span>{{ item.towards }}</span> |
                      <span style="margin-left: 2px">{{ item.roomNumber }}室{{ item.livingRoomNumber }}厅</span> |
                      <span style="margin-left: 2px" v-if="item.rentType == 1">整租</span>
                      <span style="margin-left: 2px" v-if="item.rentType == 2">合租</span>
                      <span style="color: #f5222d; font-size: 13px;float: right">{{ item.rentPrice }}元</span>
                    </div>
                  </a-card>
                </div>
              </a-tab-pane>
              <a-tab-pane key="2" tab="小区" style="padding: 15px">
                <div style="width: 100%;margin-bottom: 15px;text-align: left" v-for="(item, index) in communityList" :key="index">
                  <a-divider orientation="left">
                    <span style="font-size: 12px;font-family: SimHei;">{{item.province}}{{item.city}}{{item.area}} - {{item.code}}</span>
                  </a-divider>
                  <a-card :bordered="false" @click="communityDetail(item)" hoverable>
                    <a-card-meta :title="item.communityName" :description="item.address.slice(0, 50)+'...'"></a-card-meta>
                    <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
                      <span v-if="item.propertyType == 1">公寓住宅 |</span>
                      <span v-if="item.propertyType == 2">商业物业 |</span>
                      <span v-if="item.propertyType == 3">工业物业 |</span>
                      <span style="margin-left: 2px" v-if="item.tenureCategory == 1">商品房住宅 |</span>
                      <span style="margin-left: 2px" v-if="item.tenureCategory == 2">央产房 |</span>
                      <span style="margin-left: 2px" v-if="item.tenureCategory == 3">军产房 |</span>
                      <span style="margin-left: 2px" v-if="item.tenureCategory == 4">小产权房 |</span>
                      <span style="margin-left: 2px" v-if="item.tenureCategory == 5">自建房 |</span>
                      {{ item.propertyTenure }}年
                      <a style=" font-size: 13px;float: right">详情</a>
                    </div>
                  </a-card>
                </div>
              </a-tab-pane>
            </a-tabs>
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
      communityRent: 0,
      rentShow: false,
      rentData: null,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      houseInfo: null,
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
    this.getRentList()
    this.getCommunityList()
    setTimeout(() => {
      baiduMap.initMap('areas')
      this.getLocal()
    }, 500)
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
        baiduMap.searchNear(this.rentData.longitude, this.rentData.latitude, key)
      } else {
        baiduMap.searchNear(this.communityData.longitude, this.communityData.latitude, key)
      }
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
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
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
      this.navigation(this.rentData)
    },
    home () {
      this.$router.push('/home')
    },
    rentDetail (row) {
      this.rentData = row
      this.rentShow = true
      this.getHouseInfo(this.rentData.houseCode)
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
    getHouseInfo (houseCode) {
      this.$get(`/cos/house-info/detail/${houseCode}`).then((r) => {
        this.houseInfo = r.data.data
        this.getCommunity(this.houseInfo.communityCode)
      })
    },
    getRentList () {
      this.$get('/cos/rent-info/page').then((r) => {
        this.rentList = r.data.data.records
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
