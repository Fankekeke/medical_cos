<template>
  <a-modal v-model="show" title="新增医生信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
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
          <a-form-item label='所属医院' v-bind="formItemLayout">
            <a-select
              show-search
              option-filter-prop="children"
              :filter-option="false"
              :not-found-content="fetching ? undefined : null"
              @search="fetchUser"
              @change="hospitalCheck" v-decorator="[
              'hospitalId',
              { rules: [{ required: true, message: '请输入所属医院!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in hospitalList" :key="index">{{ item.hospitalName }}</a-select-option>
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
    </a-form>
  </a-modal>
</template>

<script>
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
  name: 'doctorAdd',
  props: {
    doctorAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.doctorAddVisiable
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
      officesInfo: null,
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
        values.hospitalName = this.hospitalInfo.hospitalName
        values.officesName = this.officesInfo.officesName
        if (!err) {
          this.loading = true
          this.$post('/cos/doctor-info', {
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
