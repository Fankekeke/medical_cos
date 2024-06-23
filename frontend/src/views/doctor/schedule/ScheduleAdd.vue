<template>
  <a-modal v-model="show" title="新增排班" @cancel="onClose" :width="800">
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
          <a-form-item label='排班名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入排班名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='作业医生' v-bind="formItemLayout">
            <a-select v-decorator="[
              'staffIds',
              { rules: [{ required: true, message: '请输入作业医生!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in doctorList" :key="index">{{
                  item.doctorName
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='排班开始时间' v-bind="formItemLayout">
            <a-time-picker :default-open-value="moment('00:00:00', 'HH:mm:ss')" style="width: 100%" v-decorator="[
            'startDate',
            { rules: [{ required: true, message: '请输入排班开始时间!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='排班结束时间' v-bind="formItemLayout">
            <a-time-picker style="width: 100%" v-decorator="[
            'endDate',
            { rules: [{ required: true, message: '请输入排班结束时间!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='排班时间' v-bind="formItemLayout">
            <a-date-picker style="width: 100%;" v-decorator="[
            'taskDate',
            { rules: [{ required: true, message: '请输入排班时间!' }] }
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import debounce from 'lodash/debounce';
import moment from 'moment'
moment.locale('zh-cn')
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
  name: 'scheduleAdd',
  props: {
    scheduleAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.scheduleAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    this.lastFetchId = 0;
    this.fetchUser = debounce(this.fetchUser, 800);
    return {
      moment,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fetching: false,
      hospitalInfo: null,
      hospitalList: [],
      fileList: [],
      previewVisible: false,
      previewImage: '',
      pharmacyList: [],
      doctorList: []
    }
  },
  mounted () {
    this.getDoctorList()
  },
  methods: {
    hospitalCheck (value) {
      if (value) {
        this.hospitalList.forEach(e => {
          if (e.id === value) {
            this.hospitalInfo = e
          }
        })
      }
    },
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
    getDoctorList () {
      this.$get(`/cos/doctor-info/list/hospital/user/${this.currentUser.userId}`).then((r) => {
        this.doctorList = r.data.data
      })
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
      this.form.validateFields((err, values) => {
        // 获取图片List
        let images = []
        this.fileList.forEach(image => {
          images.push(image.response)
        })
        if (!err) {
          values.images = images.length > 0 ? images.join(',') : null
          values.taskDate = moment(values.taskDate).format('YYYY-MM-DD')
          values.startDate = moment(values.startDate).format('HH:mm:ss')
          values.endDate = moment(values.endDate).format('HH:mm:ss')
          values.status = 1
          this.loading = true
          this.$post('/cos/schedule-info', {
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
