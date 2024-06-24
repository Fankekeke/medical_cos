<template>
  <a-row :gutter="8" style="width: 100%">
    <a-col :span="10">
      <div style="background:#ECECEC; padding:30px;margin-top: 30px">
        <a-card :bordered="false">
          <b style="font-size: 15px">我的信息</b>
        </a-card>
        <a-card :bordered="false">
          <a-form :form="form" layout="vertical">
            <a-spin :spinning="dataLoading">
              <a-row :gutter="20">
                <a-col :span="8">
                  <a-form-item label='医生姓名' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'doctorName',
            { rules: [{ required: true, message: '请输入医生姓名!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='性别' v-bind="formItemLayout">
                    <a-select v-decorator="[
              'doctorSex',
              { rules: [{ required: true, message: '请输入性别!' }] }
              ]">
                      <a-select-option value="男">男</a-select-option>
                      <a-select-option value="女">女</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='医生照片' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'doctorImg'
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='医生职称' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'doctorTitle',
            { rules: [{ required: true, message: '请输入医生职称!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='教学支职称' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'teachTitle',
            { rules: [{ required: true, message: '请输入教学支职称!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='行政职位' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'doctorAdministrative',
            { rules: [{ required: true, message: '请输入行政职位!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label='学位' v-bind="formItemLayout">
                    <a-input v-decorator="[
            'doctorDegree',
            { rules: [{ required: true, message: '请输入学位!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="24">
                  <a-form-item label='医生特长' v-bind="formItemLayout">
                    <a-textarea placeholder="Basic usage" :rows="4" v-decorator="[
            'doctorForte',
            { rules: [{ required: true, message: '请输入医生特长!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="24">
                  <a-form-item label='信息' v-bind="formItemLayout">
                    <a-textarea placeholder="Basic usage" :rows="4" v-decorator="[
            'doctorAbout',
            { rules: [{ required: true, message: '请输入信息!' }] }
            ]"/>
                  </a-form-item>
                </a-col>
                <a-col :span="24">
                  <a-form-item label='医生图片' v-bind="formItemLayout">
                    <a-upload
                      name="avatar"
                      action="http://127.0.0.1:9527/file/fileUpload/"
                      list-type="picture-card"
                      :file-list="fileList"
                      @preview="handlePreview"
                      @change="picHandleChange"
                    >
                      <div v-if="fileList.length < 8">
                        <a-icon type="plus" />
                        <div class="ant-upload-text">
                          Upload
                        </div>
                      </div>
                    </a-upload>
                    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                      <img alt="example" style="width: 100%" :src="previewImage" />
                    </a-modal>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-spin>
          </a-form>
          <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
            修改
          </a-button>
          <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
        </a-card>
      </div>
    </a-col>
    <a-col :span="14">
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
  </a-row>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'User',
  components: {
    drawerMap
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      courseInfo: [],
      dataLoading: false,
      classList: [],
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false,
      checked: true
    }
  },
  mounted () {
    this.dataInit()
  },
  methods: {
    onChange (checked) {
      this.checked = checked
    },
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint !== null && localPoint !== undefined) {
        this.localPoint = localPoint
        let address = baiduMap.getAddress(localPoint)
        address.getLocation(localPoint, (rs) => {
          if (rs != null) {
            if (rs.address !== undefined && rs.address.length !== 0) {
              this.stayAddress = rs.address
            }
            if (rs.surroundingPois !== undefined) {
              if (rs.surroundingPois.address !== undefined && rs.surroundingPois.address.length !== 0) {
                this.stayAddress = rs.surroundingPois.address
              }
            }
            let obj = {}
            obj['hospitalAddress'] = this.stayAddress
            obj['longitude'] = localPoint.lng
            obj['latitude'] = localPoint.lat
            this.form.setFieldsValue(obj)
          }
        })
      }
    },
    addPoint (point) {
      this.localPoint = point
    },
    onSearch () {
      let localData = []
      var options = {
        onSearchComplete: (results) => {
          // 判断状态是否正确
          // eslint-disable-next-line no-undef,eqeqeq
          if (local.getStatus() == BMAP_STATUS_SUCCESS) {
            for (var i = 0; i < results.getCurrentNumPois(); i++) {
              if (i === 0) {
                setTimeout(() => {
                  baiduMap.findPoint(results.getPoi(0).point, 15)
                }, 10)
              }
              localData.push(results.getPoi(i))
              if (results.getPoi(i).point !== undefined) {
                baiduMap.localPointAdd(results.getPoi(i))
              }
            }
            this.localData = localData
            this.cardShow = true
          }
        }
      }
      var local = new BMap.LocalSearch(baiduMap.rMap(), options)
      local.search(this.local)
    },
    showChildrenDrawer () {
      this.childrenDrawer = true
    },
    onChildrenDrawerClose () {
      this.childrenDrawer = false
    },
    moment,
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    selectClassList () {
      this.$get('/cos/class-info/list').then((r) => {
        this.classList = r.data.data
      })
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.createDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: "✔"})
        }
      })
      return listData || []
    },
    dataInit () {
      this.dataLoading = true
      this.$get(`/cos/doctor-info/user/detail/${this.currentUser.userId}`).then((r) => {
        console.log(r.data.doctor)
        this.rowId = r.data.doctor.id
        this.setFormValues(r.data.doctor)
        this.courseInfo = r.data.order
        this.dataLoading = false
      })
    },
    setFormValues ({...user}) {
      this.rowId = user.id
      let fields = ['doctorName', 'doctorSex', 'doctorImg', 'doctorTitle', 'teachTitle', 'doctorAdministrative', 'doctorDegree', 'doctorForte', 'doctorAbout', 'hospitalId', 'officesId']
      let obj = {}
      Object.keys(user).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(user['images'])
        }
        if (key === 'isOpen' && user[key] != null) {
          this.checked = user[key].toString() === '1'
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = user[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/doctor-info', {
            ...values
          }).then((r) => {
            this.$message.success('修改信息成功')
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
