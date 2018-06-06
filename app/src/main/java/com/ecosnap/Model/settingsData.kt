package com.ecosnap.Model

import android.graphics.drawable.Drawable
import java.io.Serializable

class Settings (val data: Array<SettingsItem>) : Serializable

class SettingsItem(val title: String, val img: Drawable) : Serializable