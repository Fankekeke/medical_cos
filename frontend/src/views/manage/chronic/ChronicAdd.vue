<template>
  <a-modal v-model="show" title="新增慢性病档案" @cancel="onClose" :width="950">
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
        <a-col :span="12">
          <a-form-item label='用户姓名' v-bind="formItemLayout">
            <a-input v-decorator="[
            'userName',
            { rules: [{ required: true, message: '请输入用户姓名!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='性别' v-bind="formItemLayout">
            <a-select v-decorator="[
              'sex',
              { rules: [{ required: true, message: '请输入性别!' }] }
              ]">
              <a-select-option value="男">男</a-select-option>
              <a-select-option value="女">女</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='出生日期' v-bind="formItemLayout">
            <a-date-picker style="width: 100%;" v-decorator="[
            'birthdayDate',
            { rules: [{ required: true, message: '请输入出生日期!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='身份证号' v-bind="formItemLayout">
            <a-input v-decorator="[
            'idCard',
            { rules: [{ required: true, message: '请输入身份证号!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='联系方式' v-bind="formItemLayout">
            <a-input v-decorator="[
            'phone',
            { rules: [{ required: true, message: '请输入联系方式!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24"></a-col>
        <a-col :span="6">
          <a-form-item label='是否高血压' v-bind="formItemLayout">
            <a-radio-group button-style="solid" v-decorator="[
              'hypertensionFlag',
              { rules: [{ required: true, message: '请选择是否高血压!' }] }
              ]">
              <a-radio-button value="0">
                否
              </a-radio-button>
              <a-radio-button value="1">
                是
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='是否糖尿病' v-bind="formItemLayout">
            <a-radio-group button-style="solid" v-decorator="[
              'diabetesFlag',
              { rules: [{ required: true, message: '请选择是否糖尿病!' }] }
              ]">
              <a-radio-button value="0">
                否
              </a-radio-button>
              <a-radio-button value="1">
                是
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='是否精神疾病' v-bind="formItemLayout">
            <a-radio-group button-style="solid" v-decorator="[
              'mentalDisordersFlag',
              { rules: [{ required: true, message: '请选择是否精神疾病!' }] }
              ]">
              <a-radio-button value="0">
                否
              </a-radio-button>
              <a-radio-button value="1">
                是
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='是否慢阻肺' v-bind="formItemLayout">
            <a-radio-group button-style="solid" v-decorator="[
              'copdFlag',
              { rules: [{ required: true, message: '请选择是否慢阻肺!' }] }
              ]">
              <a-radio-button value="0">
                否
              </a-radio-button>
              <a-radio-button value="1">
                是
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='服务机构' v-bind="formItemLayout">
            <a-input v-decorator="[
            'serivceDept',
            { rules: [{ required: true, message: '请输入服务机构!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='服务医生' v-bind="formItemLayout">
            <a-input v-decorator="[
            'serviceDoctor',
            { rules: [{ required: true, message: '请输入服务医生!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='现住址' v-bind="formItemLayout">
            <a-input v-decorator="[
            'address',
            { rules: [{ required: true, message: '请输入现住址!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24"></a-col>
        <a-col :span="8">
          <a-form-item label='空腹血糖' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'fastingBloodSugar'
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='餐后2小时血糖' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'postprandialBloodSuga'
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='血压' v-bind="formItemLayout">
            <a-input-number :min="0" style="width: 100%" v-decorator="[
              'bloodPressure'
              ]"
            />
          </a-form-item>
        </a-col>
<!--        <a-col :span="24">-->
<!--          <a-form-item label='病例内容' v-bind="formItemLayout">-->
<!--            <a-textarea :rows="6" v-decorator="[-->
<!--            'content',-->
<!--             { rules: [{ required: true, message: '请输入病例内容!' }] }-->
<!--            ]"/>-->
<!--          </a-form-item>-->
<!--        </a-col>-->
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
import moment from "moment";
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
  name: 'BulletinAdd',
  props: {
    bulletinAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
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
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          values.birthdayDate = moment(values.birthdayDate).format('YYYY-MM-DD')
          this.$post('/cos/chronic-info', {
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
