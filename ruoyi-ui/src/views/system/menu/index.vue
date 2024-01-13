<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="菜单名称" prop="menuName">
                  <el-input
                    v-model="queryParams.menuName"
                    placeholder="请输入菜单名称"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="queryParams.status" placeholder="菜单状态" @change="handleQuery" clearable style="width: 100%">
                    <el-option
                      v-for="dict in sys_normal_disable"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
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
              <el-button disabled type="text">菜单信息</el-button>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right;">
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="primary"
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['system:menu:add']"
              >新增</el-button>
              <el-button 
                class="filter-item"
                style="margin-left: 8px;"
                icon="Sort"
                @click="toggleExpandAll"
              >展开/折叠</el-button>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="menuList"
        stripe 
        ref="tableRef"
        row-key="menuId"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        highlight-current-row 
        style="width: 100%;" 
        :height="tableHeight"
      >
        <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160"></el-table-column>
        <el-table-column prop="icon" label="图标" align="center" width="100">
          <template #default="scope">
            <svg-icon :icon-class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="60"></el-table-column>
        <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:menu:edit']"
            >修改</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              @click="handleAdd(scope.row)"
              v-hasPermi="['system:menu:add']"
            >新增</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              style="color: red;"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:menu:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px;" />
          <p>暂无数据</p>
        </template>
      </el-table>

      <!-- 添加或修改菜单对话框 -->
      <el-dialog :title="title" v-model="open" width="720px" :before-close="handleClose" append-to-body>
        <div class="dialog_box" style="height: 502px;">
          <el-form ref="menuRef" :model="form" :rules="rules" label-position="top">
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="上级菜单">
                  <el-tree-select style="width: 100%;"
                    v-model="form.parentId"
                    :data="menuOptions"
                    :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                    value-key="menuId"
                    placeholder="选择上级菜单"
                    check-strictly
                    />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="菜单类型" prop="menuType">
                  <el-radio-group v-model="form.menuType">
                    <el-radio label="M">目录</el-radio>
                    <el-radio label="C">菜单</el-radio>
                    <el-radio label="F">按钮</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="form.menuType != 'F'">
                <el-form-item label="菜单图标" prop="icon">
                  <el-popover
                    placement="bottom-start"
                    :width="540"
                    v-model:visible="showChooseIcon"
                    trigger="click"
                    @show="showSelectIcon"
                    class="popover-box"
                   >
                    <template #reference>
                       <el-input v-model="form.icon" placeholder="点击选择图标" @click="showSelectIcon" readonly>
                          <template #prefix >
                             <svg-icon
                                v-if="form.icon"
                                :icon-class="form.icon"
                                class="el-input__icon"
                                style="height: 32px;width: 16px;"
                             />
                             <el-icon v-else style="height: 32px;width: 16px;"><search /></el-icon>
                          </template>
                       </el-input>
                    </template>
                    <icon-select ref="iconSelectRef" @selected="selected" />
                  </el-popover>
                </el-form-item>
               </el-col>
               <el-col :span="12">
                <el-form-item label="菜单名称" prop="menuName">
                  <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
                </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="显示排序" prop="orderNum">
                    <el-input-number v-model="form.orderNum" controls-position="right" :step="10" style="width: 100%;" :min="10" />
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item>
                     <template #label>
                        <span>
                          是否外链
                          <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                            <el-icon><question-filled /></el-icon>
                          </el-tooltip>
                        </span>
                     </template>
                     <el-radio-group v-model="form.isFrame">
                        <el-radio label="0">是</el-radio>
                        <el-radio label="1">否</el-radio>
                     </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item prop="path">
                    <template #label>
                      <span>
                        路由地址
                        <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                      </span>
                    </template>
                    <el-input v-model="form.path" placeholder="请输入路由地址" />
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item prop="component">
                    <template #label>
                      <span>
                        组件路径
                        <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                      </span>
                    </template>
                    <el-input v-model="form.component" placeholder="请输入组件路径" />
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType != 'M'">
                  <el-form-item>
                    <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100" />
                    <template #label>
                      <span>
                        权限字符
                        <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                      </span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item>
                    <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255" />
                    <template #label>
                      <span>
                         路由参数
                         <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                            <el-icon><question-filled /></el-icon>
                         </el-tooltip>
                      </span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item>
                    <template #label>
                      <span>
                        是否缓存
                        <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                      </span>
                    </template>
                    <el-radio-group v-model="form.isCache">
                      <el-radio-button label="0">缓存</el-radio-button>
                      <el-radio-button label="1">不缓存</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item>
                    <template #label>
                      <span>
                        显示状态
                        <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                      </span>
                    </template>
                    <el-radio-group v-model="form.visible">
                      <el-radio-button
                         v-for="dict in sys_show_hide"
                         :key="dict.value"
                         :label="dict.value"
                      >{{ dict.label }}</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item>
                    <template #label>
                      <span>
                        <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                          <el-icon><question-filled /></el-icon>
                        </el-tooltip>
                        菜单状态
                      </span>
                    </template>
                    <el-radio-group v-model="form.status">
                      <el-radio-button
                        v-for="dict in sys_normal_disable"
                        :key="dict.value"
                        :label="dict.value"
                      >{{ dict.label }}</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
             <el-button type="primary" @click="submitForm">确 定</el-button>
             <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup name="Menu">
import { addMenu, delMenu, getMenu, listMenu, updateMenu } from "@/api/system/menu";
import SvgIcon from "@/components/SvgIcon";
import IconSelect from "@/components/IconSelect";
import { ClickOutside as vClickOutside } from 'element-plus'

const { proxy } = getCurrentInstance();
const { sys_show_hide, sys_normal_disable } = proxy.useDict("sys_show_hide", "sys_normal_disable");

const tableHeight = proxy.getInitTableHeight(47);
const menuList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const menuOptions = ref([]);
const isExpandAll = ref(false);
const refreshTable = ref(true);
const showChooseIcon = ref(false);
const iconSelectRef = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    menuName: undefined,
    visible: undefined
  },
  rules: {
    menuName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
    orderNum: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
    path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
  },
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

/** 查询菜单列表 */
function getList() {
  loading.value = true;
  listMenu(queryParams.value).then(response => {
    menuList.value = proxy.handleTree(response.data, "menuId");
    loading.value = false;
  });
}
/** 查询菜单下拉树结构 */
function getTreeselect() {
  menuOptions.value = [];
  listMenu().then(response => {
    const menu = { menuId: 0, menuName: "主类目", children: [] };
    menu.children = proxy.handleTree(response.data, "menuId");
    menuOptions.value.push(menu);
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: "M",
    orderNum: undefined,
    isFrame: "1",
    isCache: "0",
    visible: "0",
    status: "0"
  };
  proxy.resetForm("menuRef");
}
/** 展示下拉图标 */
function showSelectIcon() {
  iconSelectRef.value.reset();
  showChooseIcon.value = true;
}
/** 选择图标 */
function selected(name) {
  form.value.icon = name;
  showChooseIcon.value = false;
}
/** 图标外层点击隐藏下拉列表 */
function hideSelectIcon() {
  showChooseIcon.value = false;
}
/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加菜单";
}
/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}
/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  await getTreeselect();
  getMenu(row.menuId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜单";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["menuRef"].validate(valid => {
    if (valid) {
      if (form.value.menuId != undefined) {
        updateMenu(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addMenu(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除名称为"' + row.menuName + '"的数据项?').then(function() {
    return delMenu(row.menuId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>
