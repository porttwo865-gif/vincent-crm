<script setup lang="ts">
import { useMessage } from 'naive-ui';
import type { OpportunitySaveRequest } from '@/api/opportunity';

const props = defineProps<{ show: boolean; data?: OpportunitySaveRequest | null }>();
const emit = defineEmits<{ (e: 'update:show', val: boolean): void; (e: 'saved'): void }>();

const message = useMessage();
const loading = ref(false);
const formRef = ref();

const formModel = reactive<OpportunitySaveRequest>({ id: undefined, name: '', customerId: '', amount: 0, stage: '', expectedCloseDate: undefined, ownerId: '', source: '', remark: '' });

const stageOptions = [{ label: '初步接触', value: 's1' }, { label: '需求确认', value: 's2' }, { label: '方案报价', value: 's3' }, { label: '商务谈判', value: 's4' }];
const sourceOptions = [{ label: '官网', value: 'website' }, { label: '展会', value: 'exhibition' }, { label: '转介绍', value: 'referral' }];

const rules = {
  name: { required: true, message: '请输入商机名称', trigger: 'blur' },
  customerId: { required: true, message: '请选择客户', trigger: 'change' },
  amount: { required: true, message: '请输入金额', trigger: 'blur', type: 'number' },
  stage: { required: true, message: '请选择阶段', trigger: 'change' },
  ownerId: { required: true, message: '请选择负责人', trigger: 'change' },
};

watch(() => props.data, (val) => {
  if (val) Object.assign(formModel, val);
  else Object.assign(formModel, { id: undefined, name: '', customerId: '', amount: 0, stage: '', expectedCloseDate: undefined, ownerId: '', source: '', remark: '' });
}, { immediate: true });

const handleSubmit = async () => {
  try { await formRef.value?.validate(); loading.value = true;
    setTimeout(() => { loading.value = false; message.success(props.data?.id ? '编辑成功' : '新增成功'); emit('saved'); emit('update:show', false); }, 500);
  } catch {}
};
const handleClose = () => { emit('update:show', false); };
</script>

<template>
  <n-modal :show="show" :title="data?.id ? '编辑商机' : '新增商机'" preset="card" style="width: 560px" :bordered="false" :mask-closable="false" @close="handleClose">
    <n-form ref="formRef" :model="formModel" :rules="rules" label-width="90">
      <n-form-item label="商机名称" path="name"><n-input v-model:value="formModel.name" placeholder="请输入商机名称" /></n-form-item>
      <n-form-item label="客户" path="customerId"><n-select v-model:value="formModel.customerId" placeholder="请选择客户" /></n-form-item>
      <n-grid :cols="2" :x-gap="16">
        <n-gi><n-form-item label="金额" path="amount"><n-input-number v-model:value="formModel.amount" :min="0" placeholder="请输入金额" style="width: 100%" /></n-form-item></n-gi>
        <n-gi><n-form-item label="阶段" path="stage"><n-select v-model:value="formModel.stage" :options="stageOptions" placeholder="请选择阶段" /></n-form-item></n-gi>
      </n-grid>
      <n-form-item label="预计成交"><n-date-picker v-model:value="formModel.expectedCloseDate" type="date" placeholder="请选择预计成交日期" style="width: 100%" /></n-form-item>
      <n-form-item label="负责人" path="ownerId"><n-select v-model:value="formModel.ownerId" placeholder="请选择负责人" /></n-form-item>
      <n-form-item label="来源"><n-select v-model:value="formModel.source" :options="sourceOptions" placeholder="请选择来源" clearable /></n-form-item>
      <n-form-item label="备注"><n-input v-model:value="formModel.remark" type="textarea" :rows="3" placeholder="请输入备注" /></n-form-item>
    </n-form>
    <template #footer><n-space justify="end"><n-button @click="handleClose">取消</n-button><n-button type="primary" :loading="loading" @click="handleSubmit">保存</n-button></n-space></template>
  </n-modal>
</template>
