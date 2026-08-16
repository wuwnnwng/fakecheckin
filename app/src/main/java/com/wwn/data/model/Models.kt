package com.wwn.data.model

data class User(
    val id: Long,
    val phone: String?,
    val nickname: String,
    val avatarUrl: String?,
    val gender: Int,
    val age: Int?,
    val locale: String
)

data class Device(
    val id: Long,
    val deviceSn: String,
    val bleMac: String,
    val model: String,
    val firmwareVersion: String,
    val totalPulses: Int,
    val remainPulses: Int,
    val workStatus: String,
    val rssi: Int,
    val lastMode: String?,
    val lastGear: Int?
)

data class DeviceStatus(
    val workStatus: String,
    val mode: String,
    val bodyPart: String,
    val gear: Int,
    val iceOn: Boolean,
    val pulseDone: Int,
    val pulseRemain: Int,
    val remainSeconds: Int?,
    val errorMessage: String?
)

data class UsageRecord(
    val id: Long,
    val recordNo: String,
    val useDate: String,
    val useTime: String,
    val workMode: String,
    val gear: Int,
    val bodyPart: String?,
    val durationSec: Int,
    val pulseCount: Int,
    val deviceModel: String,
    var remark: String
)

data class AppMessage(
    val id: Long,
    val category: String,
    val title: String,
    val content: String,
    var read: Boolean,
    val createdAt: String
)

data class FaqItem(
    val question: String,
    val answer: String
)

data class UserSettings(
    var deviceAlert: Boolean = true,
    var usageAlert: Boolean = true,
    var systemAlert: Boolean = true,
    var soundEnabled: Boolean = true,
    var vibrateEnabled: Boolean = true,
    var locale: String = "zh",
    var usageCycle: String = "weekly_2"
)
