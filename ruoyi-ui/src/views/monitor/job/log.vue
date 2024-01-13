<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="任务名称" prop="jobName">
                  <el-input
                    v-model="queryParams.jobName"
                    placeholder="请输入任务名称"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="任务组名" prop="jobGroup">
                  <el-select
                    v-model="queryParams.jobGroup"
                    placeholder="请选择任务组名"
                    clearable
                    @change="handleQuery"
                     
                  >
                    <el-option
                      v-for="dict in sys_job_group"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="执行状态" prop="status">
                  <el-select
                    v-model="queryParams.status"
                    placeholder="请选择执行状态"
                    clearable
                    @change="handleQuery"
                     
                  >
                  <el-option
                    v-for="dict in sys_common_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="6" v-if="advanced">
                <el-form-item label="执行时间">
                  <el-date-picker
                    style="width: 100%"
                    v-model="dateRange"
                    value-format="YYYY-MM-DD"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :md="6" align="right" style="margin-left: auto;">
                <el-form-item class="search_btn_box">
                  <el-button class="filter-item" type="primary" @click="handleQuery">搜索</el-button>
                  <el-button class="filter-item" style="margin-left: 8px;" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" >
      <template #header>
        <el-row>
          <el-col :span="8">
            <div class="card-header">
              <el-button disabled type="text">调度日志</el-button>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right;">
            <el-button
              class="filter-item"
              style="margin-left: 8px;"
              type="danger"
              icon="Delete"
              v-if="!multiple"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['monitor:job:remove']"
            >删除</el-button>
            <el-button
              class="filter-item"
              style="margin-left: 8px;"
              type="danger"
              icon="Delete"
              @click="handleClean"
              v-hasPermi="['monitor:job:remove']"
            >清空</el-button>
            <el-button
              class="filter-item"
              style="margin-left: 8px;"
              icon="Download"
              @click="handleExport"
              v-hasPermi="['monitor:job:export']"
            >导出</el-button>
            <el-button
              class="filter-item"
              style="margin-left: 8px;"
              icon="Close"
              @click="handleClose"
            >关闭</el-button>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table stripe ref="tableRef" v-loading="loading" :data="jobLogList" @selection-change="handleSelectionChange" highlight-current-row style="width: 100%;" :height="tableHeight">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="日志编号" width="80" align="center" prop="jobLogId" />
        <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true" />
        <el-table-column label="任务组名" align="center" prop="jobGroup" :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag :options="sys_job_group" :value="scope.row.jobGroup" />
          </template>
        </el-table-column>
        <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
        <el-table-column label="日志信息" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
        <el-table-column label="执行状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_common_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="执行时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
              v-hasPermi="['monitor:job:query']"
            >详细</el-button>
          </template>
        </el-table-column>
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px;" />
            <p>暂无数据</p>
          </template>
      </el-table>

      <pagination
         v-show="total > 0"
         :total="total"
         v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize"
         @pagination="getList"
      />

      <!-- 调度日志详细 -->
      <el-dialog :title="title" v-model="open" width="720px" append-to-body>
        <div class="dialog_box">
          <el-form :model="form" :rules="rules" label-position="top">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="日志序号：">
                  <el-input v-model="form.jobLogId" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="任务名称：">
                  <el-input v-model="form.jobName" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="任务分组：">
                  <el-input v-model="form.jobGroup" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="执行时间：">
                  <el-input v-model="form.createTime" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="调用方法：">
                  <el-input v-model="form.invokeTarget" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="日志信息：">
                  <el-input v-model="form.jobMessage" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="执行状态：">
                   <el-input v-if="form.status == 0" placeholder="正常" disabled></el-input>
                   <el-input v-if="form.status == 1" placeholder="失败" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="异常信息：" v-if="form.status == 1">
                  <el-input v-model="form.exceptionInfo" disabled/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
             <el-button @click="open = false">关 闭</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>


<script setup name="JobLog">
import { getJob } from "@/api/monitor/job";
import { listJobLog, delJobLog, cleanJobLog } from "@/api/monitor/jobLog";

const { proxy } = getCurrentInstance();
const { sys_common_status, sys_job_group } = proxy.useDict("sys_common_status", "sys_job_group");

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const jobLogList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref([]);
const route = useRoute();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    status: undefined
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 隐藏搜索按钮操作 */
watch(showSearch, (value) => {
  let oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  if (value) {
    oldHeight = oldHeight - 12
  } else {
     oldHeight = oldHeight + 12
  }
  nextTick(() => (
    tableHeight.value = proxy.$refs.tableRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ))
})

/** 展开按钮操作 */
function toggleAdvanced() {
  const oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  advanced.value = !advanced.value
  nextTick(() => (
    tableHeight.value = proxy.$refs.tableRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ));
}

/** 查询调度日志列表 */
function getList() {
  loading.value = true;
  listJobLog(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    jobLogList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
// 返回按钮
function handleClose() {
  const obj = { path: "/monitor/job" };
  proxy.$tab.closeOpenPage(obj);
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.jobLogId);
  multiple.value = !selection.length;
}
/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  form.value = row;
}
/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除调度日志编号为"' + ids.value + '"的数据项?').then(function () {
    return delJobLog(ids.value);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm("是否确认清空所有调度日志数据项?").then(function () {
    return cleanJobLog();
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("清空成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/jobLog/export", {
    ...queryParams.value,
  }, `job_log_${new Date().getTime()}.xlsx`);
}

(() => {
  const jobId = route.query.jobId;
  if (jobId !== undefined && jobId != 0) {
    getJob(jobId).then(response => {
      queryParams.value.jobName = response.data.jobName;
      queryParams.value.jobGroup = response.data.jobGroup;
      getList();
    });
  } else {
    getList();
  }
})();

getList();
</script>
