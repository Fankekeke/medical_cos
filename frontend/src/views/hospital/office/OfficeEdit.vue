<template>
  <a-modal v-model="show" title="修改科室信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="8">
          <a-form-item label='科室名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'officesName',
            { rules: [{ required: true, message: '请输入科室名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='医生人数' v-bind="formItemLayout">
            <a-input v-decorator="[
            'doctorNum',
            { rules: [{ required: true, message: '请输入医生人数!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='荣誉资质' v-bind="formItemLayout">
            <a-input v-decorator="[
            'officesHonor',
            { rules: [{ required: true, message: '请输入荣誉资质!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='设备' v-bind="formItemLayout">
            <a-input v-decorator="[
            'officesEquipment',
            { rules: [{ required: true, message: '请输入设备!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='关于我们' v-bind="formItemLayout">
            <a-textarea placeholder="Basic usage" :rows="4" v-decorator="[
            'officesAbout',
            { rules: [{ required: true, message: '请输入关于我们!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='诊疗范围' v-bind="formItemLayout">
            <a-textarea placeholder="Basic usage" :rows="4" v-decorator="[
            'officesDiagnosisScope',
            { rules: [{ required: true, message: '请输入信息!' }] }
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
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
  name: 'officeEdit',
  props: {
    officeEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.officeEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      dataLoading: false,
      fetching: false,
      hospitalInfo: null,
      hospitalList: [],
      officeList: [],
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  mounted () {
  },
  methods: {
    fetchUser (value) {
      if (value) {
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
      }
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
            console.log(this.hospitalInfo)
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...office}) {
      this.dataLoading = true
      this.rowId = office.id
      let fields = ['officesName', 'hospitalId', 'doctorNum', 'officesHonor', 'officesEquipment', 'officesAbout', 'officesDiagnosisScope']
      let obj = {}
      Object.keys(office).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(office['images'])
        }
        // if ((key === 'hospitalId' || key === 'officesId') && office[key] != null) {
        //   office[key] = office[key].toString()
        // }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = office[key]
        }
        if (key === 'hospitalId' && office['hospitalId'] != null && office['hospitalName'] != null) {
          this.fetchUser(office['hospitalName'])
        }
      })
      setTimeout(() => {
        this.form.setFieldsValue(obj)
        this.dataLoading = false
      }, 500)
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
          this.$put('/cos/office-info', {
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
