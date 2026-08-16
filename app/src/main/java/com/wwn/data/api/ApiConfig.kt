package com.wwn.data.api

import com.wwn.BuildConfig

object ApiConfig {
    val BASE_URL: String = BuildConfig.API_BASE_URL

    const val SMS_SEND = "/auth/sms/send"
    const val REGISTER = "/auth/register"
    const val LOGIN = "/auth/login"
    const val LOGIN_OAUTH = "/auth/login/oauth"
    const val PASSWORD_RESET = "/auth/password/reset"
    const val LOGOUT = "/auth/logout"

    const val USER_PROFILE = "/user/profile"
    const val USER_PHONE = "/user/phone"
    const val USER_PASSWORD = "/user/password"
    const val USER_SETTINGS = "/user/settings"

    const val DEVICE_COMPATIBLE = "/devices/compatible"
    const val DEVICE_BIND = "/devices/bind"
    const val DEVICE_UNBIND = "/devices/unbind"
    const val DEVICE_MINE = "/devices/mine"
    const val DEVICE_SWITCH = "/devices/switch"
    fun deviceDetail(id: Long) = "/devices/$id"
    fun deviceCommand(id: Long) = "/devices/$id/command"
    fun deviceStatus(id: Long) = "/devices/$id/status"

    const val RECORDS = "/records"
    fun recordRemark(id: Long) = "/records/$id/remark"
    fun recordDetail(id: Long) = "/records/$id"

    const val MESSAGES = "/messages"
    fun messageRead(id: Long) = "/messages/$id/read"

    const val FAQS = "/faqs"
    const val GUIDES = "/guides"
    const val FEEDBACKS = "/feedbacks"
    const val ABOUT = "/app/about"
    const val AGREEMENTS = "/app/agreements"
}
