<template>
  <el-card shadow="hover">
    <template #header>
      <div class="card-header">
        <span>公告</span>
      </div>
    </template>
    <div class="box">
      <el-tabs v-model="activeName">
        <el-tab-pane label="公司" name="first">
          <ul class="list" style="height: 250px;">
            <li v-for="(item, index) in tableData" :key="index">
              <span>【{{ item.type }}】</span> <span>{{ item.data }}</span>
            </li>
          </ul>
        </el-tab-pane>
        <el-tab-pane label="平台" name="second">
          <span>平台</span>
        </el-tab-pane>
        <el-tab-pane label="项目" name="third">
          <span>项目</span>
        </el-tab-pane>
        <el-tab-pane label="活动" name="fourth">
          <span>活动</span>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-card>
</template>
<script setup name="Notice">
const { proxy } = getCurrentInstance();
const activeName = ref('first')
const data = reactive({
  tableData: [{
        data: '公司为什么需要建立一套统一的开发框架？',
        type: '公司'
      },
      {
        data: ' 进行技术沉淀——提升公司整体技术能力，避免陷入一个人的能力决定一个项目',
        type: '公司'
      },
      {
        data: 'LOGO要这样排，你得PPT颜值不用愁',
        type: '公司'
      },
      {
        data: '文艺青年最后的音乐据点：网易云音乐评论区',
        type: '公司'
      },
      {
        data: '长在火山泥里的夏威夷果，不用剥壳好好吃。 ',
        type: '公司'
      },
      {
        data: '产品经理如何处理工作中的突发事件',
        type: '公司'
      },
      {
        data: '统一开发框架（平台）的建设思路',
        type: '公司'
      }

      ],
      toplist: [{
        top: '1',
        data: 'CRM工作台解决方案',
        icon: 'arrow-up-fill',
        number: '1080',
        type: ''
      },
      {
        top: '2',
        data: '项目管理工作台解决方案',
        icon: 'arrow-up-fill',
        number: '1080',
        type: ''

      },
      {
        top: '3',
        data: '任务管理工作台解决方案',
        icon: '',
        number: '1080',
        type: ''
      },
      {
        top: '4',
        data: '流程审批页解决方案',
        icon: '',
        number: '1080',
        type: 'info'
      },
      {
        top: '5',
        data: '通用表格页解决方案',
        icon: '',
        number: '1080',
        type: 'info'
      },
      {
        top: '6',
        data: '树组件解决方案',
        icon: '',
        number: '1080',
        type: 'info'
      },
      {
        top: '7',
        data: '财务管理工作台解决方案',
        icon: '',
        number: '1080',
        type: 'info'
      }
  ]
});
const { tableData } = toRefs(data);
</script>
<style lang="scss" scope>
 .todo_top {
    .todo_top_box{
      background: #F7F9FA;
      border-radius:4px ;
      padding:12px  16px;
      span{
        font-size: 24px;
        line-height: 32px;

        color:#5584fd ;
      }
      p{
        font-size: 14px;
        color:#383838 ;
        margin: 0;
        line-height: 20px;
      }
    }
   }
   .todo_list{
     margin: 0 ;
     padding: 0;
     li{
      list-style: none;
      background: #F7F9FA;
      line-height: 20px;
      padding: 8px;
      margin:6px 0;
      cursor: pointer;
      font-size: 14px;
      p{
        margin:  0;
        display: inline-block;
        margin: 0 4px;
        vertical-align: middle;
      }
      span{
        display: inline-block;
        margin: 0 4px;
        width: calc(100% - 120px);
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
        vertical-align: middle;
      }
     }
     li:hover{color: #5584fd;
     }
 }
</style>
