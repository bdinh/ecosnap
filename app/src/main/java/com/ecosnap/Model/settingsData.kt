package com.ecosnap.Model

import java.io.Serializable

class Settings (val data: Array<SettingsItem>) : Serializable

class SettingsItem(val title: String, val img: Int) : Serializable