<script setup lang="ts">
import { useMessage } from 'naive-ui';
import type { ClueSaveRequest } from '@/api/clue';

const props = defineProps<{
  show: boolean;
  data?: ClueSaveRequest | null;
}>();

const emit = defineEmits<{
  (e: 'update:show', val: boolean): void;
  (e: 'saved'): void;
}>();

const message = useMessage();
const loading = ref(false);
const formRef = ref();

const formModel = reactive<ClueSaveRequest>({
  id: undefined,
  name: '',
  phone: '',
  email: '',
  company: '',
  source: '',
  ownerId: '',
  remark: '',
});

/** 来源选项 */
const sourceOptions = [
  { label: '官网', value: 'website' },
  { label: '展会', value: 'exhibition' },
  { label: '转介绍', value: 'referral' },
  { label: '电话', value: 'phone' },
  { label: '邮件', value: 'email' },
  { label: '其他', value: 'other' },
];

/** 表单规则 */
const rules = {
  name: { required: true, message: '请输入线索名称', trigger: 'blur' },
  phone: { required: true, message: '请输入手机号', trigger: 'blur' },
  source: { required: true, message: '请选择来源', trigger: 'change' },
  ownerId: { required: true, message: '请选择负责人', trigger: 'change' },
};

/** 重置表单 */
const resetForm = () => {
  formModel.id = undefined;
  formModel.name = '';
  formModel.phone = '';
  formModel.email = '';
  formModel.company = '';
  formModel.source = '';
  formModel.ownerId = '';
  formModel.remark = '';
};

/** 监听编辑数据 */
watch(
  () => props.data,
  (val) => {
    if (val) {
      Object.assign(formModel, val);
    } else {
      resetForm();
    }
  },
  { immediate: true }
);

/** 提交 */
const handleSubmit = async () => {
  try {
    await formRef.value?.validate();
    loading.value = true;
    // Mock 保存
    setTimeout(() => {
      loading.value = false;
      message.success(props.data?.id ? '编辑成功' : '新增成功');
      emit('saved');
      emit('update:show', false);
      resetForm();
    }, 500);
  } catch {
    // 校验失败
  }
};

/** 关闭 */
const handleClose = () => {
  emit('update:show', false);
  resetForm();
};
</script>

<template>
  <n-modal
    :show="show"
    :title="data?.id ? '编辑线索' : '新增线索'"
    preset="card"
    style="width: 560px"
    :bordered="false"
    :mask-closable="false"
    @close="handleClose"
  >
    <n-form ref="formRef" :model="formModel" :rules="rules" label-width="80">
      <n-form-item label="名称" path="name">
        <n-input v-model:value="formModel.name" placeholder="请输入线索名称" />
      </n-form-item>
      <n-form-item label="手机" path="phone">
        <n-input v-model:value="formModel.phone" placeholder="请输入手机号" />
      </n-form-item>
      <n-form-item label="邮箱">
        <n-input v-model:value="formModel.email" placeholder="请输入邮箱" />
      </n-form-item>
      <n-form-item label="公司">
        <n-input v-model:value="formModel.company" placeholder="请输入公司名称" />
      </n-form-item>
      <n-form-item label="来源" path="source">
        <n-select v-model:value="formModel.source" :options="sourceOptions" placeholder="请选择来源" />
      </n-form-item>
      <n-form-item label="负责人" path="ownerId">
        <n-select v-model:value="formModel.ownerId" placeholder="请选择负责人" />
      </n-form-item>
      <n-form-item label="备注">
        <n-input v-model:value="formModel.remark" type="textarea" :rows="3" placeholder="请输入备注" />
      </n-form-item>
    </n-form>

    <template #footer>
      <n-space justify="end">
        <n-button @click="handleClose">取消</n-button>
        <n-button type="primary" :loading="loading" @click="handleSubmit">保存</n-button>
      </n-space>
    </template>
  </n-modal>
</template>
