# 假打卡（fakecheckin）

一个可运行的 Android Demo，用来演示「打卡」界面流程。打开应用后可以看到模拟地点、当前状态，点击按钮完成一次打卡记录。

这是界面演示，**不会读取真实定位，也不会修改系统位置**。

## 功能

- 展示固定的演示地点：`演示园区 · 模拟位置`
- 显示打卡时间、当前状态（待打卡 / 已打卡）
- 点击「立即打卡」：写入当前时间，状态变为已打卡，并弹出提示
- 点击「重置演示」：清空本次记录，回到待打卡

## 运行环境

- Android 7.0（API 24）及以上
- 构建需要 JDK 17、Android SDK（compileSdk 35）
- 包名：`com.wwn`

## 如何运行

1. 用 IntelliJ IDEA / Android Studio 打开本项目
2. Gradle JVM 选择 JDK 17，等待 Sync 完成
3. 启动模拟器，或用 USB 连接已开启调试的手机
4. 运行 `app`

没有设备时会出现 `No target device found`，需要先打开 Device Manager 创建并启动模拟器。

## 如何打安装包

在项目根目录执行：

```powershell
.\gradlew.bat :app:assembleDebug
```

生成的文件：

`app/build/outputs/apk/debug/app-debug.apk`

把该 APK 发给其他人，对方允许「安装未知来源应用」后即可安装。对方系统需 Android 7.0 及以上。

这是 debug 包，适合试用，不能上应用商店。

## 项目结构

```
fakecheckin/
├── settings.gradle.kts      # 仓库与模块
├── app/build.gradle.kts     # 应用依赖
└── app/src/main/
    ├── AndroidManifest.xml
    ├── java/com/wwn/MainActivity.kt
    └── res/layout/activity_main.xml
```

依赖从阿里云 Maven 镜像拉取（Google / Central / Gradle Plugin），Gradle 发行包使用腾讯云镜像。
