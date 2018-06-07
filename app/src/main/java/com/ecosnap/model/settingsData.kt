package com.ecosnap.model

import android.graphics.drawable.Drawable
import java.io.Serializable

class Settings (val data: Array<SettingsItem>) : Serializable

class SettingsItem(val title: String, val img: Drawable) : Serializable