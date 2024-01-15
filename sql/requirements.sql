use ruoyi;
create table login_information (
    id int primary key auto_increment comment '自增uuid',
    phone_number varchar(255) not null comment '手机号',
    openID varchar(255) not null comment '微信OpenID',
    usertype tinyint not null comment '用户类型',
    teacher_id int comment '家教ID',
    parent_id int comment '家长ID',
    created datetime not null default current_timestamp comment '创建时间，必填',
    updated datetime default current_timestamp comment '修改时间'
);
create table dealer_information (
    id int primary key auto_increment comment '自增uuid',
    dealer_name varchar(255) unique comment '经销商名称',
    dealer_address varchar(255) comment '经销商地址',
    dealer_introduction varchar(255) comment '经销商简介',
    created datetime not null default current_timestamp comment '创建时间，必填',
    updated datetime default current_timestamp comment '修改时间'
);
create table user_information (
    id int primary key auto_increment comment '自增uuid',
    user_type tinyint not null comment '1.家教 2.家长',
    real_name varchar(255) not null comment '真实姓名',
    id_number varchar(255) not null comment '身份证号',
    id_front varchar(255) comment '身份证正面照片',
    id_back varchar(255) comment '身份证背面照片',
    teacher_license varchar(255) comment '教师资格证照片，多张图片用;号分隔',
    sex tinyint not null comment '性别',
    residential_address varchar(255) comment '居住地址，省-市-区',
    teacher_type tinyint comment '1.在校 2.在职',
    student_id int comment '学生ID',
    degree varchar(255) comment '学位',
    education varchar(255) comment '学历',
    university varchar(255) comment '毕业院校',
    state tinyint comment '用户状态，0.保存 1.提交 2.通过 3.打回',
    affiliated_dealer int comment '家教所属经销商，可为null',
    created datetime not null default current_timestamp comment '创建时间，必填',
    updated datetime default current_timestamp comment '修改时间',
    constraint dealer_fk foreign key (affiliated_dealer) references dealer_information(id)
);



create table dealer_commission (
    id int auto_increment primary key comment '自增uuid',
    phone_number varchar(255) not null comment '手机号',
    dealer_id int comment '经销商ID',
    teacher_id int comment '教师ID',
    commission_type tinyint comment '佣金类型，0.年费 1.每单固定金额 2.每单固定比例',
    commission_calculation int comment '佣金计算方式',
    long_term tinyint comment '长期标识，0.长期 1.有限期间',
    start_time datetime comment '开始时间',
    end_time datetime comment '结束时间',
    status tinyint comment '状态，0.可用 1.已失效',
    invalid_reason varchar(255) comment '失效原因',
    contract varchar(255) comment '合同',
    commission_category tinyint comment '佣金类别，0.经销商佣金 1.家教佣金',
    created datetime not null default current_timestamp comment '创建时间，必填',
    updated datetime default current_timestamp comment '修改时间',
    constraint dealer_fk_2 foreign key (dealer_id) references dealer_information(id)
);

create table audit_information (
    id int auto_increment primary key comment '自增uuid',
    tutor_parent_id int comment '家教/家长id',
    audit_status varchar(255) comment '审核状态',
    audit_opinion text comment '审核意见',
    auditor varchar(255) comment '审核人',
    created_at datetime not null comment '创建时间，必填',
    updated_at datetime comment '修改时间'
);
create table demand_information (
     id int auto_increment primary key comment '自增uuid',
     title varchar(255) not null comment '标题，必填',
     description text comment '描述',
     demand_status int comment '需求状态，0.保存 1.发布 2.接单 3.完成 4.删除',
     parent_id int comment '家长ID',
     created_time datetime default current_timestamp  comment '创建时间',
     published_time datetime comment '发布时间'
);
create table demand_tag_information (
     id int auto_increment primary key comment '自增uuid',
     demand_id int comment '需求ID',
     tag_id int comment '标签ID',
     created_time datetime default current_timestamp  comment '创建时间',
     published_time datetime comment '发布时间'
);

create table receiving_information (
     id int auto_increment primary key comment '自增uuid',
     demand_id int not null comment '需求ID，必填',
     tutor_id int comment '家教ID',
     receiving_status int comment '接单状态，0.接单 1.签合同 2.完成 3.取消 4.删除',
     created_time datetime default current_timestamp  comment '创建时间',
     updated_time datetime default current_timestamp  comment '修改时间'
);
create table order_information (
     id int auto_increment primary key comment '自增uuid',
     parent_id int not null comment '家长ID，必填',
     tutor_id int comment '家教ID',
     demand_id int comment '需求ID',
     contract text comment '合同',
     order_amount decimal(10,2) comment '订单金额',
     order_status int comment '订单状态，0.未支付 1.已支付 2.已取消 3.已删除',
     created_time datetime default current_timestamp  comment '创建时间',
     updated_time datetime default current_timestamp  comment '修改时间'
);
create table payment_information (
     id int auto_increment primary key comment '自增uuid',
     order_id int not null comment '订单ID，必填',
     payment_method int comment '支付方式，1.微信 2.支付宝 3.银行卡',
     payment_info text comment '支付信息',
     payment_amount decimal(10,2) comment '支付金额',
     created_time datetime default current_timestamp  comment '创建时间',
     updated_time datetime default current_timestamp  comment '修改时间'
);
create table category_information (
     id int auto_increment primary key comment '自增uuid',
     category_name varchar(255) not null comment '分类名，必填',
     sort_order int comment '排序',
     parent_category_id int comment '父类分类ID'
);
create table tag_information (
     id int auto_increment primary key comment '自增uuid',
     tag_name varchar(255) not null comment '标签名，必填',
     category_id int not null comment '分类ID，必填，微信openID',
     sort_order int not null comment '排序，1.家教 2.家长'
);
create table tutor_tag_information (
     id int auto_increment primary key comment '自增uuid',
     tutor_id int not null comment '家教ID，必填',
     tag_id int not null comment '标签ID，必填，微信openID'
);
create table demand_tag_information (
     id int auto_increment primary key comment '自增uuid',
     demand_id int not null comment '需求ID，必填',
     tag_id int not null comment '标签ID，必填，微信openID'
);
create table schedule_information (
     id int auto_increment primary key comment '自增uuid',
     parent_tutor_id int not null comment '家长/家教ID，必填',
     class_date date comment '上课日期',
     start_time time comment '上课开始时间',
     end_time time comment '上课结束时间',
     class_location varchar(255) comment '上课地点',
     order_id int comment '订单ID'
);
