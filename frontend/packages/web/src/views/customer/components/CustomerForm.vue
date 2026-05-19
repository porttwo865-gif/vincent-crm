<script setup lang="ts">
import { useMessage } from 'naive-ui';
import type { CustomerSaveRequest } from '@/api/customer';

const props = defineProps<{
  show: boolean;
  data?: CustomerSaveRequest | null;
}>();

const emit = defineEmits<{
  (e: 'update:show', val: boolean): void;
  (e: 'saved'): void;
}>();

const message = useMessage();
const loading = ref(false);
const formRef = ref();

const formModel = reactive<CustomerSaveRequest>({
  id: undefined, name: '', phone: '', email: '', company: '',
  industry: '', source: '', ownerId: '', level: '', address: '', remark: '',
});

const sourceOptions = [
  { label: '官网', value: 'website' },
  { label: '展会', value: 'exhibition' },
  { label: '转介绍', value: 'referral' },
  { label: '线索转化', value: 'clue' },
  { label: '其他', value: 'other' },
];

const levelOptions = [
  { label: 'A级（重要）', value: 'A' },
  { label: 'B级（普通）', value: 'B' },
  { label: 'C级（一般）', value: 'C' },
];

const industryOptions = [
  { label: '互联网', value: 'internet' },
  { label: '金融', value: 'finance' },
  { label: '制造', value: 'manufacturing' },
  { label: '教育', value: 'education' },
  { label: '医疗', value: 'medical' },
  { label: '其他', value: 'other' },
];

const rules = {
  name: { required: true, message: '请输入客户名称', trigger: 'blur' },
  source: { required: true, message: '请选择来源', trigger: 'change' },
  ownerId: { required: true, message: '请选择负责人', trigger: 'change' },
  level: { required: true, message: '请选择等级', trigger: 'change' },
};

const resetForm = () => {
  Object.assign(formModel, { id: undefined, name: '', phone: '', email: '', company: '',
    industry: '', source: '', ownerId: '', level: '', address: '', remark: '' });
};

watch(() => props.data, (val) => {
  if (val) Object.assign(formModel, val);
  else resetForm();
}, { immediate: true });

const handleSubmit = async () => {
  try {
    await formRef.value?.validate();
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
      message.success(props.data?.id ? '编辑成功' : '新增成功');
      emit('saved');
      emit('update:show', false);
      resetForm();
    }, 500);
  } catch {}
};

const handleClose = () => {
  emit('update:show', false);
  resetForm();
};
</script>

<template>
  <n-modal :show="show" :title="data?.id ? '编辑客户' : '新增客户'" preset="card" style="width: 600px" :bordered="false" :mask-closable="false" @close="handleClose">
    <n-form ref="formRef" :model="formModel" :rules="rules" label-width="80">
      <n-grid :cols="2" :x-gap="16">
        <n-gi><n-form-item label="名称" path="name"><n-input v-model:value="formModel.name" placeholder="请输入客户名称" /></n-form-item></n-gi>
        <n-gi><n-form-item label="公司"><n-input v-model:value="formModel.company" placeholder="请输入公司名称" /></n-form-item></n-gi>
        <n-gi><n-form-item label="手机"><n-input v-model:value="formModel.phone" placeholder="请输入手机号" /></n-form-item></n-gi>
        <n-gi><n-form-item label="邮箱"><n-input v-model:value="formModel.email" placeholder="请输入邮箱" /></n-form-item></n-gi>
        <n-gi><n-form-item label="行业"><n-select v-model:value="formModel.industry" :options="industryOptions" placeholder="请选择行业" clearable /></n-form-item></n-gi>
        <n-gi><n-form-item label="等级" path="level"><n-select v-model:value="formModel.level" :options="levelOptions" placeholder="请选择等级" /></n-form-item></n-gi>
        <n-gi><n-form-item label="来源" path="source"><n-select v-model:value="formModel.source" :options="sourceOptions" placeholder="请选择来源" /></n-form-item></n-gi>
        <n-gi><n-form-item label="负责人" path="ownerId"><n-select v-model:value="formModel.ownerId" placeholder="请选择负责人" /></n-form-item></n-gi>
      </n-grid>
      <n-form-item label="地址"><n-input v-model:value="formModel.address" placeholder="请输入地址" /></n-form-item>
      <n-form-item label="备注"><n-input v-model:value="formModel.remark" type="textarea" :rows="3" placeholder="请输入备注" /></n-form-item>
    </n-form>
    <template #footer><n-space justify="end"><n-button @click="handleClose">取消</n-button><n-button type="primary" :loading="loading" @click="handleSubmit">保存</n-button></n-space></template>
  </n-modal>
</template>
