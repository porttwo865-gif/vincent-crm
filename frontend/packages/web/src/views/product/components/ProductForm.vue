<script setup lang="ts">
import { useMessage } from 'naive-ui';
import type { ProductSaveRequest } from '@/api/product';

const props = defineProps<{ show: boolean; data?: ProductSaveRequest | null }>();
const emit = defineEmits<{ (e: 'update:show', val: boolean): void; (e: 'saved'): void }>();

const message = useMessage();
const loading = ref(false);
const formRef = ref();

const formModel = reactive<ProductSaveRequest>({ id: undefined, name: '', code: '', category: '', price: 0, unit: '', description: '', spec: '' });

const categoryOptions = [{ label: '软件', value: 'software' }, { label: '硬件', value: 'hardware' }, { label: '服务', value: 'service' }];

const rules = {
  name: { required: true, message: '请输入产品名称', trigger: 'blur' },
  code: { required: true, message: '请输入产品编码', trigger: 'blur' },
  price: { required: true, message: '请输入单价', trigger: 'blur', type: 'number' },
};

watch(() => props.data, (val) => {
  if (val) Object.assign(formModel, val);
  else Object.assign(formModel, { id: undefined, name: '', code: '', category: '', price: 0, unit: '', description: '', spec: '' });
}, { immediate: true });

const handleSubmit = async () => {
  try { await formRef.value?.validate(); loading.value = true;
    setTimeout(() => { loading.value = false; message.success(props.data?.id ? '编辑成功' : '新增成功'); emit('saved'); emit('update:show', false); }, 500);
  } catch {}
};
const handleClose = () => { emit('update:show', false); };
</script>

<template>
  <n-modal :show="show" :title="data?.id ? '编辑产品' : '新增产品'" preset="card" style="width: 500px" :bordered="false" :mask-closable="false" @close="handleClose">
    <n-form ref="formRef" :model="formModel" :rules="rules" label-width="80">
      <n-form-item label="名称" path="name"><n-input v-model:value="formModel.name" placeholder="请输入产品名称" /></n-form-item>
      <n-form-item label="编码" path="code"><n-input v-model:value="formModel.code" placeholder="请输入产品编码" /></n-form-item>
      <n-grid :cols="2" :x-gap="16">
        <n-gi><n-form-item label="分类"><n-select v-model:value="formModel.category" :options="categoryOptions" placeholder="请选择分类" clearable /></n-form-item></n-gi>
        <n-gi><n-form-item label="单位"><n-input v-model:value="formModel.unit" placeholder="如：套、台" /></n-form-item></n-gi>
      </n-grid>
      <n-form-item label="单价" path="price"><n-input-number v-model:value="formModel.price" :min="0" placeholder="请输入单价" style="width: 100%" /></n-form-item>
      <n-form-item label="规格"><n-input v-model:value="formModel.spec" placeholder="请输入规格" /></n-form-item>
      <n-form-item label="描述"><n-input v-model:value="formModel.description" type="textarea" :rows="3" placeholder="请输入描述" /></n-form-item>
    </n-form>
    <template #footer><n-space justify="end"><n-button @click="handleClose">取消</n-button><n-button type="primary" :loading="loading" @click="handleSubmit">保存</n-button></n-space></template>
  </n-modal>
</template>
