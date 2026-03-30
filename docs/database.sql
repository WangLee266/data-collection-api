-- =====================================================
-- 开源数据采集系统 数据库设计
-- Database: data_collection
-- Version: 1.0.0
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS data_collection 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE data_collection;

-- =====================================================
-- 1. 用户管理模块
-- =====================================================

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色(admin/operator/user)',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:禁用)',
    ukey_serial VARCHAR(100) COMMENT 'UKey序列号',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_ukey (ukey_serial)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 验证码表
DROP TABLE IF EXISTS sys_captcha;
CREATE TABLE sys_captcha (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    uuid VARCHAR(50) NOT NULL UNIQUE COMMENT '验证码UUID',
    code VARCHAR(10) NOT NULL COMMENT '验证码',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_uuid (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

-- =====================================================
-- 2. 信源管理模块
-- =====================================================

-- 人物表
DROP TABLE IF EXISTS source_person;
CREATE TABLE source_person (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '人物ID',
    name_cn VARCHAR(100) COMMENT '中文名',
    name_en VARCHAR(100) COMMENT '英文名',
    name_origin VARCHAR(100) COMMENT '原名',
    country VARCHAR(50) COMMENT '国家',
    role VARCHAR(30) COMMENT '身份(政界/媒体/学术/商界)',
    avatar VARCHAR(255) COMMENT '头像URL',
    tags TEXT COMMENT '标签(JSON数组)',
    website_count INT DEFAULT 0 COMMENT '关联网站数',
    account_count INT DEFAULT 0 COMMENT '关联账号数',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark TEXT COMMENT '备注',
    INDEX idx_country (country),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人物表';

-- 组织表
DROP TABLE IF EXISTS source_organization;
CREATE TABLE source_organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '组织ID',
    name_cn VARCHAR(100) COMMENT '中文名',
    name_en VARCHAR(100) COMMENT '英文名',
    name_origin VARCHAR(100) COMMENT '原名',
    short_name VARCHAR(50) COMMENT '简称',
    country VARCHAR(50) COMMENT '国家',
    type VARCHAR(30) COMMENT '类型(政府/企业/媒体/智库/NGO)',
    industry VARCHAR(50) COMMENT '行业',
    website VARCHAR(255) COMMENT '官网',
    tags TEXT COMMENT '标签(JSON数组)',
    website_count INT DEFAULT 0 COMMENT '关联网站数',
    account_count INT DEFAULT 0 COMMENT '关联账号数',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark TEXT COMMENT '备注',
    INDEX idx_country (country),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织表';

-- 网站信源表
DROP TABLE IF EXISTS source_website;
CREATE TABLE source_website (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '网站ID',
    url VARCHAR(255) NOT NULL UNIQUE COMMENT '网址URL',
    name_cn VARCHAR(100) COMMENT '中文名',
    name_en VARCHAR(100) COMMENT '英文名',
    name_origin VARCHAR(100) COMMENT '原名',
    language VARCHAR(20) COMMENT '语种',
    type VARCHAR(20) COMMENT '类型(图文/视频/音频/综合)',
    domain VARCHAR(20) COMMENT '领域(政治/财经/社会/文化/综合)',
    owner_id BIGINT COMMENT '所属人物/组织ID',
    owner_name VARCHAR(100) COMMENT '所属名称',
    owner_type VARCHAR(20) COMMENT '所属类型(person/organization)',
    source_type VARCHAR(20) DEFAULT 'media' COMMENT '信源类型(media/government/thinktank/enterprise)',
    version INT DEFAULT 1 COMMENT '版本号',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark TEXT COMMENT '备注',
    INDEX idx_url (url),
    INDEX idx_language (language),
    INDEX idx_domain (domain),
    INDEX idx_source_type (source_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站信源表';

-- 网站栏目表
DROP TABLE IF EXISTS source_website_channel;
CREATE TABLE source_website_channel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '栏目ID',
    website_id BIGINT NOT NULL COMMENT '网站ID',
    url VARCHAR(255) NOT NULL COMMENT '栏目URL',
    name_cn VARCHAR(100) COMMENT '中文名',
    name_en VARCHAR(100) COMMENT '英文名',
    domain VARCHAR(20) COMMENT '领域',
    collect_level INT DEFAULT 2 COMMENT '采集层级',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_website_id (website_id),
    FOREIGN KEY (website_id) REFERENCES source_website(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站栏目表';

-- 社交账号表
DROP TABLE IF EXISTS source_social_account;
CREATE TABLE source_social_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账号ID',
    platform VARCHAR(30) NOT NULL COMMENT '平台(X/Facebook/Instagram/YouTube/TikTok/LinkedIn)',
    url VARCHAR(255) NOT NULL UNIQUE COMMENT '账号URL',
    account_id VARCHAR(100) COMMENT '账号ID',
    name VARCHAR(100) COMMENT '账号名称',
    display_name VARCHAR(100) COMMENT '显示名称',
    owner_id BIGINT COMMENT '所属人物/组织ID',
    owner_name VARCHAR(100) COMMENT '所属名称',
    owner_type VARCHAR(20) COMMENT '所属类型(person/organization)',
    domain VARCHAR(20) COMMENT '领域',
    version INT DEFAULT 1 COMMENT '版本号',
    status TINYINT DEFAULT 1 COMMENT '状态(1:正常 0:删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark TEXT COMMENT '备注',
    INDEX idx_platform (platform),
    INDEX idx_owner (owner_id, owner_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社交账号表';

-- =====================================================
-- 3. 任务管理模块
-- =====================================================

-- 采集任务表
DROP TABLE IF EXISTS collect_task;
CREATE TABLE collect_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    task_id VARCHAR(30) NOT NULL UNIQUE COMMENT '任务编号(TASK-YYYY-XXXX)',
    name VARCHAR(100) NOT NULL COMMENT '任务名称',
    type VARCHAR(20) NOT NULL COMMENT '任务类型(website/thinktank/social)',
    source_id BIGINT COMMENT '信源ID',
    source_name VARCHAR(100) COMMENT '信源名称',
    platform VARCHAR(30) COMMENT '平台',
    strategy VARCHAR(50) COMMENT '执行策略',
    cron_expression VARCHAR(50) COMMENT 'Cron表达式',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态(RUNNING/PENDING/SUCCESS/ERROR/WARNING)',
    progress INT DEFAULT 0 COMMENT '进度(0-100)',
    collected_count BIGINT DEFAULT 0 COMMENT '已采集数量',
    total_count BIGINT COMMENT '总数量',
    node_id VARCHAR(30) COMMENT '执行节点ID',
    next_run_time DATETIME COMMENT '下次执行时间',
    last_run_time DATETIME COMMENT '上次执行时间',
    priority VARCHAR(20) DEFAULT 'NORMAL' COMMENT '优先级(HIGH/NORMAL/LOW)',
    collect_images TINYINT DEFAULT 1 COMMENT '采集图片(1:是 0:否)',
    collect_videos TINYINT DEFAULT 0 COMMENT '采集视频(1:是 0:否)',
    collect_comments TINYINT DEFAULT 0 COMMENT '采集评论(1:是 0:否)',
    start_time DATETIME COMMENT '启动时间',
    creator VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark TEXT COMMENT '备注',
    INDEX idx_task_id (task_id),
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_node (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集任务表';

-- 任务日志表
DROP TABLE IF EXISTS collect_task_log;
CREATE TABLE collect_task_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    task_id VARCHAR(30) NOT NULL COMMENT '任务编号',
    node_id VARCHAR(30) COMMENT '节点ID',
    log_type VARCHAR(20) COMMENT '日志类型',
    log_level VARCHAR(20) COMMENT '日志级别(INFO/WARN/ERROR)',
    message TEXT COMMENT '日志内容',
    log_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_task_id (task_id),
    INDEX idx_log_time (log_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务日志表';

-- 任务与信源关联表
DROP TABLE IF EXISTS collect_task_source;
CREATE TABLE collect_task_source (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    task_id VARCHAR(30) NOT NULL COMMENT '任务编号',
    source_type VARCHAR(20) NOT NULL COMMENT '信源类型(website/channel/social_account)',
    source_id BIGINT NOT NULL COMMENT '信源ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_task (task_id),
    UNIQUE KEY uk_task_source (task_id, source_type, source_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务信源关联表';

-- =====================================================
-- 4. 节点管理模块
-- =====================================================

-- 采集节点表
DROP TABLE IF EXISTS collect_node;
CREATE TABLE collect_node (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '节点ID',
    node_id VARCHAR(30) NOT NULL UNIQUE COMMENT '节点编号(NODE-XXX)',
    ip VARCHAR(50) NOT NULL COMMENT 'IP地址',
    region VARCHAR(50) COMMENT '地区',
    isp VARCHAR(50) COMMENT '运营商',
    type VARCHAR(20) COMMENT '类型(采集节点/调度节点)',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态(NORMAL/WARNING/ERROR)',
    cpu INT COMMENT 'CPU使用率(%)',
    memory INT COMMENT '内存使用率(%)',
    disk INT COMMENT '磁盘使用率(%)',
    task_count INT DEFAULT 0 COMMENT '任务数量',
    last_heartbeat DATETIME COMMENT '最后心跳时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_node_id (node_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集节点表';

-- =====================================================
-- 5. 数据中心模块
-- =====================================================

-- 采集数据表
DROP TABLE IF EXISTS collect_data;
CREATE TABLE collect_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID',
    data_id VARCHAR(50) NOT NULL UNIQUE COMMENT '数据编号',
    task_id VARCHAR(30) COMMENT '任务编号',
    node_id VARCHAR(30) COMMENT '节点ID',
    main_tab VARCHAR(20) COMMENT '主分类(news/social)',
    title VARCHAR(500) COMMENT '标题',
    content MEDIUMTEXT COMMENT '内容',
    source VARCHAR(255) COMMENT '来源',
    source_type VARCHAR(20) COMMENT '信源类型(media/government/thinktank/enterprise)',
    platform VARCHAR(30) COMMENT '平台',
    country VARCHAR(50) COMMENT '国家',
    language VARCHAR(30) COMMENT '语言',
    content_type VARCHAR(20) COMMENT '内容类型(article/post/video/image)',
    publish_time DATETIME COMMENT '发布时间',
    collect_time DATETIME COMMENT '采集时间',
    url VARCHAR(500) COMMENT '原文URL',
    author VARCHAR(100) COMMENT '作者',
    image_count INT DEFAULT 0 COMMENT '图片数',
    video_count INT DEFAULT 0 COMMENT '视频数',
    word_count INT DEFAULT 0 COMMENT '字数',
    likes BIGINT DEFAULT 0 COMMENT '点赞数',
    comments BIGINT DEFAULT 0 COMMENT '评论数',
    shares BIGINT DEFAULT 0 COMMENT '转发数',
    views BIGINT DEFAULT 0 COMMENT '浏览数',
    category VARCHAR(30) COMMENT '分类',
    sentiment VARCHAR(20) COMMENT '情感倾向(positive/neutral/negative)',
    tags TEXT COMMENT '标签(JSON数组)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_data_id (data_id),
    INDEX idx_task (task_id),
    INDEX idx_main_tab (main_tab),
    INDEX idx_platform (platform),
    INDEX idx_publish_time (publish_time),
    INDEX idx_collect_time (collect_time),
    FULLTEXT INDEX ft_content (title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集数据表';

-- =====================================================
-- 6. 监控预警模块
-- =====================================================

-- 预警表
DROP TABLE IF EXISTS monitor_alert;
CREATE TABLE monitor_alert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预警ID',
    alert_id VARCHAR(30) NOT NULL UNIQUE COMMENT '预警编号(ALT-XXX)',
    source_id BIGINT COMMENT '信源ID',
    source VARCHAR(100) COMMENT '信源名称',
    platform VARCHAR(30) COMMENT '平台',
    alert_type VARCHAR(50) COMMENT '预警类型',
    level VARCHAR(20) COMMENT '预警级别(CRITICAL/SERIOUS/NORMAL)',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态(PENDING/PROCESSING/RESOLVED)',
    detail TEXT COMMENT '详情',
    alert_time DATETIME COMMENT '预警时间',
    duration_minutes INT COMMENT '持续分钟数',
    resolve_time DATETIME COMMENT '解决时间',
    resolve_user VARCHAR(50) COMMENT '处理人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_alert_id (alert_id),
    INDEX idx_status (status),
    INDEX idx_level (level),
    INDEX idx_alert_time (alert_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警表';

-- 预警规则表
DROP TABLE IF EXISTS monitor_alert_rule;
CREATE TABLE monitor_alert_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规则ID',
    name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(50) COMMENT '规则类型',
    threshold VARCHAR(50) COMMENT '阈值',
    level VARCHAR(20) COMMENT '预警级别',
    notify_channels VARCHAR(100) COMMENT '通知渠道(JSON)',
    enabled TINYINT DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警规则表';

-- =====================================================
-- 7. 订阅账号模块 (新增)
-- =====================================================

-- 网站订阅账号表
DROP TABLE IF EXISTS subscription_website_account;
CREATE TABLE subscription_website_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账号ID',
    website_id BIGINT COMMENT '关联网站ID(来自信源管理)',
    website_name VARCHAR(100) COMMENT '网站名称',
    website_url VARCHAR(255) COMMENT '网站URL',
    account_name VARCHAR(100) NOT NULL COMMENT '账号名称/登录账号',
    password VARCHAR(255) COMMENT '密码(加密存储)',
    expire_date DATE COMMENT '有效截止日期',
    charge_info VARCHAR(100) COMMENT '收费信息(如:年费 $299)',
    status VARCHAR(20) DEFAULT '有效' COMMENT '状态(有效/已过期/即将到期)',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_website_id (website_id),
    INDEX idx_status (status),
    INDEX idx_expire_date (expire_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站订阅账号表';

-- 社交平台订阅账号表
DROP TABLE IF EXISTS subscription_social_account;
CREATE TABLE subscription_social_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账号ID',
    platform VARCHAR(30) NOT NULL COMMENT '平台标识(x/facebook/instagram/telegram/youtube/tiktok/linkedin/reddit/vk/whatsapp)',
    platform_label VARCHAR(50) COMMENT '平台显示名称',
    account_name VARCHAR(100) NOT NULL COMMENT '账号名称/用户名',
    password VARCHAR(255) COMMENT '密码(加密存储)',
    email VARCHAR(100) COMMENT '绑定邮箱地址',
    email_password VARCHAR(255) COMMENT '邮箱密码(加密存储)',
    two_fa VARCHAR(100) COMMENT '二步验证码/密钥',
    status VARCHAR(20) DEFAULT '有效' COMMENT '状态(有效/受限/封禁/已过期)',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_platform (platform),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社交平台订阅账号表';

-- =====================================================
-- 8. 预警监测模块 (新增)
-- =====================================================

-- 预警监测任务表
DROP TABLE IF EXISTS alert_monitor_task;
CREATE TABLE alert_monitor_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    target_type VARCHAR(20) COMMENT '监测目标类型(website/social)',
    target_ids TEXT COMMENT '监测目标ID列表(逗号分隔)',
    frequency VARCHAR(10) COMMENT '监测频率(15m/30m/1h/2h/6h/12h/24h)',
    monitor_time_start VARCHAR(10) COMMENT '监测时间段开始',
    monitor_time_end VARCHAR(10) COMMENT '监测时间段结束',
    no_data_threshold INT COMMENT '无数据超时阈值',
    no_data_unit VARCHAR(5) COMMENT '无数据超时单位(m/h/d)',
    success_rate_threshold INT COMMENT '采集成功率阈值(百分比)',
    data_change_threshold INT COMMENT '数据量异常波动阈值(百分比)',
    structure_change_detect TINYINT DEFAULT 0 COMMENT '是否启用页面结构变化检测',
    rate_limit_detect TINYINT DEFAULT 1 COMMENT '是否启用账号限流检测',
    notify_channels VARCHAR(100) COMMENT '通知方式(逗号分隔:sys/email/wechat/sms)',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态(pending/running/paused/completed)',
    version INT DEFAULT 1 COMMENT '版本号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_execute_time DATETIME COMMENT '最后执行时间',
    INDEX idx_status (status),
    INDEX idx_target_type (target_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警监测任务表';

-- =====================================================
-- 9. 通知配置模块 (新增)
-- =====================================================

-- 通知配置表
DROP TABLE IF EXISTS notification_config;
CREATE TABLE notification_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_type VARCHAR(20) NOT NULL COMMENT '配置类型(sys/email/wechat/sms)',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用',
    alert_level VARCHAR(20) DEFAULT 'all' COMMENT '通知级别(all/serious/critical)',
    config_name VARCHAR(100) COMMENT '配置名称',
    config_value TEXT COMMENT '配置值(邮箱地址/手机号/webhook URL等)',
    extra_config TEXT COMMENT '扩展配置(JSON格式)',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_type (config_type),
    INDEX idx_is_enabled (is_enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知配置表';

-- =====================================================
-- 10. 初始化数据
-- =====================================================

-- 初始化管理员用户
INSERT INTO sys_user (username, password, real_name, email, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'admin@datacollection.com', 'admin', 1);

-- 初始化节点
INSERT INTO collect_node (node_id, ip, region, isp, type, status, cpu, memory, disk, task_count) VALUES
('NODE-001', '103.24.18.12', '美国-纽约', 'AWS', '采集节点', 'NORMAL', 45, 62, 38, 28),
('NODE-002', '185.76.12.44', '德国-法兰克福', 'Hetzner', '采集节点', 'NORMAL', 72, 81, 55, 45),
('NODE-003', '52.198.44.11', '日本-东京', 'AWS', '采集节点', 'WARNING', 91, 88, 72, 62),
('NODE-004', '47.120.18.99', '中国-上海', '阿里云', '调度节点', 'NORMAL', 34, 48, 29, 0);

-- 初始化预警规则
INSERT INTO monitor_alert_rule (name, rule_type, threshold, level, notify_channels, enabled) VALUES
('无数据超时', 'no_data_timeout', '>=3小时', 'SERIOUS', '["system","email","wechat"]', 1),
('成功率阈值', 'success_rate', '<80%', 'NORMAL', '["system","email"]', 1),
('节点离线', 'node_offline', '心跳>5分钟', 'CRITICAL', '["system","email","sms"]', 1),
('波动预警', 'fluctuation', '±50%', 'NORMAL', '["system"]', 1);

-- 初始化网站订阅账号示例数据
INSERT INTO subscription_website_account (website_id, website_name, website_url, account_name, password, expire_date, charge_info, status, remark) VALUES
(1, '路透社', 'reuters.com', 'user_reuters@email.com', 'Rtr$2024#Pass', '2025-12-31', '年费 $299', '有效', '新闻采集专用账号'),
(2, '纽约时报', 'nytimes.com', 'nyt_sub_user', 'NYT@secure789', '2024-06-30', '月费 $17', '已过期', '政治板块采集'),
(3, '金融时报', 'ft.com', 'ft_datacollect', 'FT_pass#2024', '2025-03-01', '年费 $339', '有效', '财经数据专用'),
(4, '经济学人', 'economist.com', 'eco_reader_01', 'Eco!Reader2024', '2025-09-15', '年费 $189', '有效', '经济分析订阅');

-- 初始化社交平台订阅账号示例数据
INSERT INTO subscription_social_account (platform, platform_label, account_name, password, email, email_password, two_fa, status, remark) VALUES
('x', 'X (Twitter)', '@datacollect_bot', 'X_Bot#2024', 'xbot@proton.me', 'Mail$Secure01', 'JBSWY3DPEHPK3PXP', '有效', '自动采集账号'),
('facebook', 'Facebook', 'fb.data.collector', 'FB_Pass!789', 'fbcollect@gmail.com', 'GmailPwd#001', '', '有效', '公开主页监控'),
('telegram', 'Telegram', '+8613800138000', 'TG_secure2024', 'tg.monitor@email.com', 'Tg$Email#123', 'ABCD1234EFGH5678', '有效', '频道内容监控'),
('instagram', 'Instagram', 'ig_news_watcher', 'IG@Watch2024!', 'ig.watcher@mail.com', 'IgMailPass#', '', '受限', '已受限，待处理'),
('youtube', 'YouTube', 'yt.monitor@gmail.com', 'YT_Monitor#2024', 'yt.monitor@gmail.com', 'YtGmail$Pass99', 'YTFA2024KEYCODE', '有效', '视频内容采集');

-- 初始化通知配置示例数据
INSERT INTO notification_config (config_type, is_enabled, alert_level, config_name, config_value, sort_order) VALUES
('sys', 1, 'all', '系统消息通知', NULL, 1),
('email', 1, 'serious', 'admin@example.com', 'admin@example.com', 2),
('email', 1, 'serious', 'ops@example.com', 'ops@example.com', 3),
('wechat', 1, 'all', '运维群机器人', 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxxxx', 4),
('sms', 0, 'critical', '138****8888', '138****8888', 5);

-- 初始化预警监测任务示例数据
INSERT INTO alert_monitor_task (task_name, target_type, target_ids, frequency, monitor_time_start, monitor_time_end, no_data_threshold, no_data_unit, success_rate_threshold, data_change_threshold, structure_change_detect, rate_limit_detect, notify_channels, status) VALUES
('主流媒体监测', 'website', 'w1,w2,w3,w4', '1h', '00:00', '23:59', 3, 'h', 80, 50, 0, 1, 'sys,email', 'running'),
('社交平台账号监测', 'social', 's1,s2,s3', '30m', '00:00', '23:59', 2, 'h', 80, 50, 0, 1, 'sys,email,wechat', 'running');
