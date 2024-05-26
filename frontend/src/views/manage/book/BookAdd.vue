<template>
  <a-modal v-model="show" title="新增图书" @cancel="onClose" :width="800">
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
          <a-form-item label='图书名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'bookName',
            { rules: [{ required: true, message: '请输入图书名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='作者' v-bind="formItemLayout">
            <a-input v-decorator="[
            'auther',
            { rules: [{ required: true, message: '请输入作者!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="价格（元）">
            <a-input-number style="width: 100%" v-decorator="[
              'price', { rules: [{ required: true, message: '请填写价格!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='出版社' v-bind="formItemLayout">
            <a-input v-decorator="[
            'press',
            { rules: [{ required: true, message: '请输入出版社!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='图书类型' v-bind="formItemLayout">
            <a-select v-decorator="[
              'type',
              { rules: [{ required: true, message: '请输入图书类型!' }] }
              ]">
              <a-select-option value="1">医疗</a-select-option>
              <a-select-option value="2">科技</a-select-option>
              <a-select-option value="3">历史</a-select-option>
              <a-select-option value="4">烹饪</a-select-option>
              <a-select-option value="5">高数</a-select-option>
              <a-select-option value="6">小说</a-select-option>
              <a-select-option value="7">诗词</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='关键词' v-bind="formItemLayout">
            <a-input v-decorator="[
            'keyWord',
            { rules: [{ required: true, message: '请输入关键词!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='摘要' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入摘要!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
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
  name: 'bookAdd',
  props: {
    bookAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bookAddVisiable
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
      previewImage: '',
      bookList: []
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
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/cos/book-info', {
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
