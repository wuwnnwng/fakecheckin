package com.wwn.data

import com.wwn.data.model.Device
import com.wwn.data.model.DeviceStatus
import com.wwn.data.model.User
import com.wwn.data.model.UserSettings

object Session {
    var token: String? = null
    var user: User? = null
    var isGuest: Boolean = false
    var currentDevice: Device? = null
    var deviceStatus: DeviceStatus? = null
    var settings: UserSettings = UserSettings()
    var firstLaunch: Boolean = true

    fun isLoggedIn(): Boolean = user != null && !isGuest

    fun logout() {
        token = null
        user = null
        isGuest = false
        currentDevice = null
        deviceStatus = null
    }
}
