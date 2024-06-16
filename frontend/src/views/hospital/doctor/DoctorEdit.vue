<template>
  <a-modal v-model="show" title="修改医生信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
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
            <a-form-item label='所属科室' v-bind="formItemLayout">
              <a-select @change="officesCheck" v-decorator="[
                'officesId',
                { rules: [{ required: true, message: '请输入所属科室!' }] }
                ]">
                <a-select-option :value="item.id" v-for="(item, index) in officeList" :key="index">{{ item.officesName }}</a-select-option>
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
  name: 'doctorEdit',
  props: {
    doctorEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.doctorEditVisiable
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
      officesInfo: null,
      hospitalList: [],
      officeList: [],
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  mounted () {
    this.selectOfficeList(this.currentUser.userId)
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
      this.$get(`/cos/office-info/list/byhospital/user/${hospitalId}`).then((r) => {
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
    officesCheck (value) {
      if (value) {
        this.officeList.forEach(e => {
          if (e.id === value) {
            this.officesInfo = e
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
    setFormValues ({...doctor}) {
      this.dataLoading = true
      this.rowId = doctor.id
      let fields = ['doctorName', 'doctorSex', 'doctorImg', 'doctorTitle', 'teachTitle', 'doctorAdministrative', 'doctorDegree', 'doctorForte', 'doctorAbout', 'hospitalId', 'officesId']
      let obj = {}
      Object.keys(doctor).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(doctor['images'])
        }
        // if ((key === 'hospitalId' || key === 'officesId') && doctor[key] != null) {
        //   doctor[key] = doctor[key].toString()
        // }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = doctor[key]
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
        if (this.officesInfo != null) {
          values.officesName = this.officesInfo.officesName
        }
        if (!err) {
          this.loading = true
          this.$put('/cos/doctor-info', {
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
