<template>
  <a-drawer
    title="新增医院"
    :maskClosable="false"
    width=750
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='医院名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalName',
            { rules: [{ required: true, message: '请输入医院名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院地址'>
            <a-input-search
              v-decorator="[
              'hospitalAddress'
              ]"
              enter-button="选择"
              @search="showChildrenDrawer"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='经度'>
            <a-input disabled v-decorator="[
            'longitude'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='纬度'>
            <a-input disabled v-decorator="[
            'latitude'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院地区'>
            <a-input v-decorator="[
            'hospitalArea',
            { rules: [{ required: true, message: '请输入医院图片!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院图片' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalImg',
            { rules: [{ required: true, message: '请输入医院图片!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院院长姓名' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalDeanName',
            { rules: [{ required: true, message: '请输入医院院长姓名!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院建院年份' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalYear',
            { rules: [{ required: true, message: '请输入医院建院年份!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院类型' v-bind="formItemLayout">
            <a-select v-decorator="[
              'hospitalNature',
              { rules: [{ required: true, message: '请输入医院类型!' }] }
              ]">
              <a-select-option value="综合医院">综合医院</a-select-option>
              <a-select-option value="专科医院">专科医院</a-select-option>
              <a-select-option value="中医医院">中医医院</a-select-option>
              <a-select-option value="妇幼保健医院">妇幼保健医院</a-select-option>
              <a-select-option value="传染病医院">传染病医院</a-select-option>
              <a-select-option value="骨科医院">骨科医院</a-select-option>
              <a-select-option value="骨伤医院">骨伤医院</a-select-option>
              <a-select-option value="门诊">门诊</a-select-option>
              <a-select-option value="其他">其他</a-select-option>
              <a-select-option value="眼科医院">眼科医院</a-select-option>
              <a-select-option value="口腔医院">口腔医院</a-select-option>
              <a-select-option value="未知">未知</a-select-option>
              <a-select-option value="肿瘤医院">肿瘤医院</a-select-option>
              <a-select-option value="儿童医院">儿童医院</a-select-option>
              <a-select-option value="心血管医院">心血管医院</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院等级' v-bind="formItemLayout">
            <a-select v-decorator="[
              'hospitalGrade',
              { rules: [{ required: true, message: '请输入医院等级!' }] }
              ]">
              <a-select-option value="三级甲等">三级甲等</a-select-option>
              <a-select-option value="一级甲等">一级甲等</a-select-option>
              <a-select-option value="二级乙等">二级乙等</a-select-option>
              <a-select-option value="二级甲等">二级甲等</a-select-option>
              <a-select-option value="未知">未知</a-select-option>
              <a-select-option value="一级乙等">一级乙等</a-select-option>
              <a-select-option value="三级乙等">三级乙等</a-select-option>
              <a-select-option value="民营医院">民营医院</a-select-option>
              <a-select-option value="未评级">未评级</a-select-option>
              <a-select-option value="三级医院">三级医院</a-select-option>
              <a-select-option value="二级医院">二级医院</a-select-option>
              <a-select-option value="一级医院">一级医院</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院科室数量' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'hospitalOfficesNum', { rules: [{ required: true, message: '请输入医院科室数量!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医保人数' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'medicalInsuranceNum', { rules: [{ required: true, message: '请输入医保人数!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='病床数量' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'hospitalBedNum', { rules: [{ required: true, message: '请输入病床数量!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='年门诊量' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'outpatientNum', { rules: [{ required: true, message: '请输入年门诊量!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='是否医保' v-bind="formItemLayout">
            <a-radio-group default-value="a" button-style="solid" v-decorator="[
              'isMedicalInsurance',
              { rules: [{ required: true, message: '请输入是否医保!' }] }
              ]">
              <a-radio-button value="医保">
                医保
              </a-radio-button>
              <a-radio-button value="非医">
                非医
              </a-radio-button>
              <a-radio-button value="非医保">
                非医保
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='医院设备介绍' v-bind="formItemLayout">
            <a-textarea :rows="3" v-decorator="[
            'hospitalEquipment',
            { rules: [{ required: true, message: '请输入医院设备介绍!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='医院简介' v-bind="formItemLayout">
            <a-textarea :rows="3" v-decorator="[
            'hospitalAbout',
            { rules: [{ required: true, message: '请输入医院简介!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院荣誉' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalHonor',
            { rules: [{ required: true, message: '请输入医院荣誉!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院网址' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalUrl',
            { rules: [{ required: true, message: '请输入医院网址!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院电话' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalPhone',
            { rules: [{ required: true, message: '请输入医院电话!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院邮编' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalPostCode',
            { rules: [{ required: true, message: '请输入医院邮编!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='医院公交路线' v-bind="formItemLayout">
            <a-input v-decorator="[
            'hospitalBusRoute',
            { rules: [{ required: true, message: '请输入医院公交路线!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='是否开启挂号' v-bind="formItemLayout">
            <a-switch default-checked v-decorator="[
              'isOpen',
              { rules: [{ required: true, message: '请输入是否开启挂号!' }] }
              ]">
              <a-icon slot="checkedChildren" type="check" />
              <a-icon slot="unCheckedChildren" type="close" />
            </a-switch>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='医院图片' v-bind="formItemLayout">
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
    </a-form>
    <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import debounce from 'lodash/debounce';
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'hospitalAdd',
  props: {
    hospitalAddVisiable: {
      default: false
    }
  },
  components: {
    drawerMap
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.hospitalAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    this.lastFetchId = 0;
    this.fetchUser = debounce(this.fetchUser, 800);
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fetching: false,
      hospitalInfo: null,
      hospitalList: [],
      officeList: [],
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false
    }
  },
  mounted () {
  },
  methods: {
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint !== null && localPoint !== undefined) {
        this.localPoint = localPoint
        console.log(this.localPoint)
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
    showChildrenDrawer () {
      this.childrenDrawer = true
    },
    onChildrenDrawerClose () {
      this.childrenDrawer = false
    },
    fetchUser (value) {
      this.lastFetchId += 1;
      const fetchId = this.lastFetchId;
      this.data = [];
      this.fetching = true;
      this.$get(`/cos/hospital-info/list/key/${value}`).then((r) => {
        this.hospitalList = r.data.data

        if (fetchId !== this.lastFetchId) {
          // for fetch callback order
          return;
        }
        const data = body.results.map(item => ({
          text: `${item.hospitalName} ${item.hospitalNature}`,
          value: item.id,
        }));
        this.hospitalList = data;
        this.fetching = false;
      })
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    selectHospitalList () {
      this.$get('/cos/hospital-info/list').then((r) => {
        this.hospitalList = r.data.data
      })
    },
    selectOfficeList (hospitalId) {
      this.$get(`/cos/office-info/list/byhospital/${hospitalId}`).then((r) => {
        this.officeList = r.data.data
      })
    },
    hospitalCheck (value) {
      if (value) {
        this.hospitalList.forEach(e => {
          if (e.id === value) {
            this.hospitalInfo = e
            this.selectOfficeList(e.id)
          }
        })
      }
    },
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        values.isOpen = this.checked ? 1 : 0
        if (!err) {
          this.loading = true
          this.$post('/cos/hospital-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
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
