package com.wwn.data

import com.wwn.data.api.ApiConfig
import com.wwn.data.model.AppMessage
import com.wwn.data.model.Device
import com.wwn.data.model.DeviceStatus
import com.wwn.data.model.FaqItem
import com.wwn.data.model.UsageRecord
import com.wwn.data.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong

/**
 * 当前为本地占位实现。正式环境按 [ApiConfig] 地址替换为 HTTPS 请求。
 */
object AppRepository {
    private val idGen = AtomicLong(100)
    private val users = mutableListOf<User>()
    private val passwords = mutableMapOf<String, String>()
    private val devices = mutableListOf<Device>()
    private val records = mutableListOf<UsageRecord>()
    private val messages = mutableListOf<AppMessage>()

    init {
        devices += Device(
            id = 1,
            deviceSn = "IPL20260001",
            bleMac = "AA:BB:CC:DD:EE:01",
            model = "IPL-Pro-A1",
            firmwareVersion = "1.0.3",
            totalPulses = 1280,
            remainPulses = 88720,
            workStatus = "idle",
            rssi = -46,
            lastMode = "manual",
            lastGear = 3
        )
        devices += Device(
            id = 2,
            deviceSn = "IPL20260002",
            bleMac = "AA:BB:CC:DD:EE:02",
            model = "IPL-Mini-B2",
            firmwareVersion = "1.1.0",
            totalPulses = 320,
            remainPulses = 49680,
            workStatus = "idle",
            rssi = -62,
            lastMode = "auto",
            lastGear = 2
        )
        val demo = User(1, "13800000000", "Demo", null, 2, 28, "en")
        users += demo
        passwords["13800000000"] = "abc123"
        messages += AppMessage(1, "device", "Device connected", "IPL-Pro-A1 is ready.", false, now())
        messages += AppMessage(2, "usage", "Treatment reminder", "Time for this week's session.", false, now())
        messages += AppMessage(3, "system", "App update", "Version 1.0.0 is available.", true, now())
    }

    fun sendSms(phone: String, scene: String): Result<Int> {
        // POST ApiConfig.BASE_URL + ApiConfig.SMS_SEND
        if (phone.length < 6) return Result.failure(IllegalArgumentException("invalid_phone"))
        return Result.success(60)
    }

    fun register(phone: String, smsCode: String, password: String): Result<User> {
        // POST ApiConfig.REGISTER
        if (smsCode != "123456") return Result.failure(IllegalArgumentException("invalid_sms"))
        if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$"))) {
            return Result.failure(IllegalArgumentException("invalid_password"))
        }
        if (users.any { it.phone == phone }) return Result.failure(IllegalArgumentException("phone_exists"))
        val user = User(idGen.incrementAndGet(), phone, "User${phone.takeLast(4)}", null, 0, null, "en")
        users += user
        passwords[phone] = password
        Session.user = user
        Session.token = "mock-token-${user.id}"
        Session.isGuest = false
        return Result.success(user)
    }

    fun login(phone: String, password: String, remember: Boolean): Result<User> {
        // POST ApiConfig.LOGIN
        val user = users.find { it.phone == phone }
            ?: return Result.failure(IllegalArgumentException("user_not_found"))
        if (passwords[phone] != password) return Result.failure(IllegalArgumentException("bad_password"))
        Session.user = user
        Session.token = "mock-token-${user.id}"
        Session.isGuest = false
        return Result.success(user)
    }

    fun oauthLogin(provider: String): Result<User> {
        // POST ApiConfig.LOGIN_OAUTH
        val user = User(idGen.incrementAndGet(), null, provider.replaceFirstChar { it.uppercase() } + " User", null, 0, null, "en")
        users += user
        Session.user = user
        Session.token = "mock-oauth-${provider}"
        Session.isGuest = false
        return Result.success(user)
    }

    fun resetPassword(phone: String, smsCode: String, newPassword: String): Result<Boolean> {
        // POST ApiConfig.PASSWORD_RESET
        if (smsCode != "123456") return Result.failure(IllegalArgumentException("invalid_sms"))
        passwords[phone] = newPassword
        return Result.success(true)
    }

    fun enterGuest() {
        Session.isGuest = true
        Session.user = User(0, null, "Guest", null, 0, null, "en")
        Session.token = null
    }

    fun scanDevices(): List<Device> {
        // 本地 BLE 扫描 + POST ApiConfig.DEVICE_BIND 校验型号
        return devices.toList()
    }

    fun bindDevice(device: Device): Device {
        // POST ApiConfig.DEVICE_BIND
        Session.currentDevice = device
        Session.deviceStatus = DeviceStatus(
            workStatus = "idle",
            mode = device.lastMode ?: "manual",
            bodyPart = "leg",
            gear = device.lastGear ?: 3,
            iceOn = false,
            pulseDone = 0,
            pulseRemain = device.remainPulses,
            remainSeconds = null,
            errorMessage = null
        )
        return device
    }

    fun unbind(deviceId: Long) {
        // POST ApiConfig.DEVICE_UNBIND
        if (Session.currentDevice?.id == deviceId) {
            Session.currentDevice = null
            Session.deviceStatus = null
        }
    }

    fun sendCommand(
        action: String,
        mode: String? = null,
        bodyPart: String? = null,
        gear: Int? = null,
        iceOn: Boolean? = null,
        timerMinutes: Int? = null
    ): DeviceStatus {
        // POST ApiConfig.deviceCommand(id)
        val current = Session.deviceStatus ?: DeviceStatus("idle", "manual", "leg", 3, false, 0, 88000, null, null)
        val next = when (action) {
            "set_mode" -> current.copy(mode = mode ?: current.mode)
            "set_part" -> current.copy(bodyPart = bodyPart ?: current.bodyPart)
            "set_gear" -> current.copy(gear = gear ?: current.gear)
            "set_ice" -> current.copy(iceOn = iceOn ?: current.iceOn)
            "start" -> current.copy(workStatus = "running", remainSeconds = (timerMinutes ?: 10) * 60)
            "pause" -> current.copy(workStatus = "paused")
            "stop" -> current.copy(workStatus = "idle", pulseDone = 0, remainSeconds = null)
            "set_timer" -> current.copy(remainSeconds = (timerMinutes ?: 10) * 60)
            "error_stop" -> current.copy(workStatus = "idle", errorMessage = "Overheat")
            else -> current
        }
        Session.deviceStatus = next
        Session.currentDevice = Session.currentDevice?.copy(workStatus = next.workStatus)
        return next
    }

    fun finishSessionAndRecord(durationSec: Int, pulseCount: Int): UsageRecord? {
        // POST ApiConfig.RECORDS ；游客不保存
        if (Session.isGuest) return null
        val status = Session.deviceStatus ?: return null
        val device = Session.currentDevice ?: return null
        val record = UsageRecord(
            id = idGen.incrementAndGet(),
            recordNo = "R${System.currentTimeMillis()}",
            useDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
            useTime = now(),
            workMode = status.mode,
            gear = status.gear,
            bodyPart = status.bodyPart,
            durationSec = durationSec,
            pulseCount = pulseCount,
            deviceModel = device.model,
            remark = ""
        )
        records.add(0, record)
        return record
    }

    fun queryRecords(mode: String?, part: String?): List<UsageRecord> {
        // GET ApiConfig.RECORDS
        return records.filter {
            (mode == null || mode == "all" || it.workMode == mode) &&
                (part == null || part == "all" || it.bodyPart == part)
        }
    }

    fun updateRemark(id: Long, remark: String): UsageRecord? {
        // PUT ApiConfig.recordRemark(id)
        val item = records.find { it.id == id } ?: return null
        item.remark = remark
        return item
    }

    fun messages(category: String?): List<AppMessage> {
        // GET ApiConfig.MESSAGES
        return if (category.isNullOrBlank() || category == "all") messages else messages.filter { it.category == category }
    }

    fun faqs(): List<FaqItem> = listOf(
        FaqItem("How to connect the device?", "Turn on Bluetooth and the IPL device, then tap Connect."),
        FaqItem("What do the modes mean?", "Manual, Auto, and Skin-tone flash modes."),
        FaqItem("Why can guests not save records?", "Sign in to keep treatment history.")
    )

    fun submitFeedback(content: String): Boolean {
        // POST ApiConfig.FEEDBACKS
        return content.isNotBlank()
    }

    fun updateProfile(nickname: String, gender: Int, age: Int?): User? {
        // PUT ApiConfig.USER_PROFILE
        val user = Session.user ?: return null
        val next = user.copy(nickname = nickname, gender = gender, age = age)
        Session.user = next
        return next
    }

    private fun now(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
}
