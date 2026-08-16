# IPL APP 接口占位合约

Base URL：`https://api.example-ipl.com/v1`  
后续只改 `ApiConfig.BASE_URL` 和各 path。统一响应：

```json
{ "code": 0, "message": "ok", "data": {} }
```

`code != 0` 为失败。需登录接口 Header：`Authorization: Bearer {token}`

## 认证

| 方法 | 地址 | 说明 |
|---|---|---|
| POST | `/auth/sms/send` | 发短信 |
| POST | `/auth/register` | 手机号注册 |
| POST | `/auth/login` | 手机号+密码登录 |
| POST | `/auth/login/oauth` | 第三方登录 |
| POST | `/auth/password/reset` | 重置密码 |
| POST | `/auth/logout` | 退出 |

### POST `/auth/sms/send`
入参：`{ "phone": "13800000000", "scene": "register|reset_password|change_phone" }`  
出参：`{ "expireSeconds": 60 }`

### POST `/auth/register`
入参：`{ "phone": "", "smsCode": "", "password": "", "agreeAgreement": true }`  
出参：`{ "token": "", "user": { "id": 1, "phone": "", "nickname": "", "avatarUrl": "", "gender": 0, "age": 0, "locale": "zh" } }`

### POST `/auth/login`
入参：`{ "phone": "", "password": "", "rememberLogin": true }`  
出参：同注册

### POST `/auth/login/oauth`
入参：`{ "provider": "wechat|alipay|apple|google", "authCode": "" }`  
出参：同注册

### POST `/auth/password/reset`
入参：`{ "phone": "", "smsCode": "", "newPassword": "" }`  
出参：`{ "success": true }`

## 用户

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/user/profile` | 个人信息 |
| PUT | `/user/profile` | 编辑资料 |
| PUT | `/user/phone` | 换绑手机 |
| PUT | `/user/password` | 改密 |
| GET | `/user/settings` | 通知/语言设置 |
| PUT | `/user/settings` | 更新设置 |

### PUT `/user/profile`
入参：`{ "nickname": "", "avatarUrl": "", "gender": 2, "age": 28 }`  
出参：user 对象

### PUT `/user/phone`
入参：`{ "phone": "", "smsCode": "" }`  
出参：user 对象

### PUT `/user/password`
入参：`{ "oldPassword": "", "newPassword": "" }`  
出参：`{ "success": true }`

### GET/PUT `/user/settings`
入参/出参：`{ "deviceAlert": true, "usageAlert": true, "systemAlert": true, "soundEnabled": true, "vibrateEnabled": true, "locale": "zh", "usageCycle": "weekly_2" }`

## 设备

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/devices/compatible` | 可匹配型号列表 |
| POST | `/devices/bind` | 绑定（搜索连接或扫码） |
| POST | `/devices/unbind` | 解绑 |
| GET | `/devices/mine` | 我的设备 |
| POST | `/devices/switch` | 切换当前设备 |
| GET | `/devices/{id}` | 设备详情 |
| POST | `/devices/{id}/command` | 下发控制指令 |
| GET | `/devices/{id}/status` | 实时状态 |

### POST `/devices/bind`
入参：`{ "deviceSn": "", "bleMac": "", "model": "" }`  
出参：device 对象

### POST `/devices/unbind`
入参：`{ "deviceId": 1 }`  
出参：`{ "success": true }`

### POST `/devices/switch`
入参：`{ "deviceId": 1 }`  
出参：device 对象

### device 对象
`{ "id": 1, "deviceSn": "IPL20260001", "bleMac": "AA:BB:CC:DD:EE:FF", "model": "IPL-Pro-A1", "firmwareVersion": "1.0.3", "totalPulses": 1200, "remainPulses": 88000, "workStatus": "idle", "rssi": -48, "lastMode": "manual", "lastGear": 3 }`

### POST `/devices/{id}/command`
入参：`{ "action": "set_mode|set_part|set_gear|set_ice|start|pause|stop|set_timer", "mode": "manual|auto|skin_tone", "bodyPart": "arm|underarm|leg|face|back|bikini", "gear": 5, "iceOn": true, "timerMinutes": 10 }`  
出参：status 对象

### GET `/devices/{id}/status`
出参：`{ "workStatus": "running", "mode": "manual", "bodyPart": "leg", "gear": 5, "iceOn": true, "pulseDone": 80, "pulseRemain": 87920, "remainSeconds": 420, "errorCode": null, "errorMessage": null }`

## 使用记录

| 方法 | 地址 | 说明 |
|---|---|---|
| POST | `/records` | 结束后自动生成 |
| PUT | `/records/{id}/remark` | 补充备注 |
| GET | `/records` | 筛选查询 |
| GET | `/records/{id}` | 详情 |

### POST `/records`
入参：`{ "deviceId": 1, "workMode": "manual", "gear": 5, "bodyPart": "leg", "durationSec": 300, "pulseCount": 80 }`  
出参：record 对象

### GET `/records`
Query：`from=2026-08-01&to=2026-08-16&mode=manual&bodyPart=leg&page=1&pageSize=20`  
出参：`{ "total": 1, "list": [record] }`

### record 对象
`{ "id": 1, "recordNo": "R20260816001", "useDate": "2026-08-16", "useTime": "2026-08-16 10:00:00", "workMode": "manual", "gear": 5, "bodyPart": "leg", "durationSec": 300, "pulseCount": 80, "deviceModel": "IPL-Pro-A1", "remark": "" }`

### PUT `/records/{id}/remark`
入参：`{ "remark": "", "skinFeeling": "" }`  
出参：record 对象

## 消息 / 反馈 / 内容

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/messages` | 消息列表 `?category=device\|usage\|system` |
| PUT | `/messages/{id}/read` | 已读 |
| GET | `/faqs?locale=en` | FAQ |
| GET | `/guides?locale=en` | 操作指南 |
| POST | `/feedbacks` | 意见反馈 |
| GET | `/app/about` | 关于 |
| GET | `/app/agreements?type=user_agreement\|privacy_policy&locale=en` | 协议 |

### POST `/feedbacks`
入参：`{ "content": "", "contact": "", "imageUrls": [] }`  
出参：`{ "id": 1, "status": "open" }`

### GET `/app/about`
出参：`{ "versionName": "1.0.0", "versionCode": 1, "forceUpdate": false, "downloadUrl": "" }`
