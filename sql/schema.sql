-- 家用强脉冲光脱毛仪 APP 表结构
-- 字符集建议 utf8mb4

CREATE TABLE users (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone           VARCHAR(20) UNIQUE,
    password_hash   VARCHAR(128) NOT NULL,
    nickname        VARCHAR(64),
    avatar_url      VARCHAR(512),
    gender          TINYINT COMMENT '0未知 1男 2女',
    age             INT,
    locale          VARCHAR(16) DEFAULT 'zh',
    remember_login  TINYINT DEFAULT 0,
    created_at      DATETIME NOT NULL,
    updated_at      DATETIME NOT NULL
);

CREATE TABLE user_third_party (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    provider        VARCHAR(32) NOT NULL COMMENT 'wechat/alipay/apple/google',
    open_id         VARCHAR(128) NOT NULL,
    union_id        VARCHAR(128),
    created_at      DATETIME NOT NULL,
    UNIQUE KEY uk_provider_openid (provider, open_id),
    KEY idx_user (user_id)
);

CREATE TABLE sms_codes (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone           VARCHAR(20) NOT NULL,
    scene           VARCHAR(32) NOT NULL COMMENT 'register/reset_password/change_phone',
    code            VARCHAR(8) NOT NULL,
    expired_at      DATETIME NOT NULL,
    used            TINYINT DEFAULT 0,
    created_at      DATETIME NOT NULL,
    KEY idx_phone_scene (phone, scene)
);

CREATE TABLE user_agreements (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    agreement_type  VARCHAR(32) NOT NULL COMMENT 'user_agreement/privacy_policy',
    version         VARCHAR(16) NOT NULL,
    accepted_at     DATETIME NOT NULL
);

CREATE TABLE devices (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_sn       VARCHAR(64) NOT NULL UNIQUE,
    ble_mac         VARCHAR(32),
    model           VARCHAR(64) NOT NULL,
    firmware_version VARCHAR(32),
    total_pulses    INT DEFAULT 0,
    remain_pulses   INT DEFAULT 0,
    work_status     VARCHAR(32) DEFAULT 'idle' COMMENT 'idle/running/paused/error',
    last_mode       VARCHAR(32),
    last_gear       INT,
    created_at      DATETIME NOT NULL,
    updated_at      DATETIME NOT NULL
);

CREATE TABLE user_devices (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    device_id       BIGINT NOT NULL,
    alias           VARCHAR(64),
    bound_at        DATETIME NOT NULL,
    unbound_at      DATETIME,
    is_current      TINYINT DEFAULT 0,
    UNIQUE KEY uk_user_device_active (user_id, device_id, unbound_at)
);

CREATE TABLE usage_records (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no       VARCHAR(64) NOT NULL UNIQUE,
    user_id         BIGINT,
    device_id       BIGINT NOT NULL,
    device_model    VARCHAR(64),
    use_date        DATE NOT NULL,
    use_time        DATETIME NOT NULL,
    work_mode       VARCHAR(32) NOT NULL,
    gear            INT NOT NULL,
    body_part       VARCHAR(32),
    duration_sec    INT NOT NULL,
    pulse_count     INT NOT NULL,
    remark          VARCHAR(500),
    skin_feeling    VARCHAR(128),
    created_at      DATETIME NOT NULL,
    KEY idx_user_date (user_id, use_date),
    KEY idx_mode_part (work_mode, body_part)
);

CREATE TABLE notifications (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    category        VARCHAR(32) NOT NULL COMMENT 'device/usage/system',
    title           VARCHAR(128) NOT NULL,
    content         VARCHAR(512) NOT NULL,
    extra_json      VARCHAR(1024),
    read_flag       TINYINT DEFAULT 0,
    created_at      DATETIME NOT NULL,
    KEY idx_user_cat (user_id, category, created_at)
);

CREATE TABLE notification_settings (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL UNIQUE,
    device_alert    TINYINT DEFAULT 1,
    usage_alert     TINYINT DEFAULT 1,
    system_alert    TINYINT DEFAULT 1,
    sound_enabled   TINYINT DEFAULT 1,
    vibrate_enabled TINYINT DEFAULT 1,
    usage_cycle     VARCHAR(32) DEFAULT 'weekly_2',
    updated_at      DATETIME NOT NULL
);

CREATE TABLE usage_reminders (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    cycle           VARCHAR(32) NOT NULL,
    remind_time     TIME NOT NULL,
    enabled         TINYINT DEFAULT 1,
    created_at      DATETIME NOT NULL
);

CREATE TABLE feedbacks (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    content         VARCHAR(1000) NOT NULL,
    contact         VARCHAR(64),
    status          VARCHAR(16) DEFAULT 'open' COMMENT 'open/replied',
    reply           VARCHAR(1000),
    created_at      DATETIME NOT NULL
);

CREATE TABLE feedback_images (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    feedback_id     BIGINT NOT NULL,
    image_url       VARCHAR(512) NOT NULL
);

CREATE TABLE faqs (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    locale          VARCHAR(16) NOT NULL,
    question        VARCHAR(256) NOT NULL,
    answer          TEXT NOT NULL,
    sort_no         INT DEFAULT 0
);

CREATE TABLE operation_logs (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    action          VARCHAR(64) NOT NULL,
    detail          VARCHAR(512),
    created_at      DATETIME NOT NULL
);

CREATE TABLE error_logs (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    stack           TEXT,
    created_at      DATETIME NOT NULL
);
