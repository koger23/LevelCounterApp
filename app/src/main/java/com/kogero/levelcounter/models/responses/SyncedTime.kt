package com.kogero.levelcounter.models.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SyncedTime (
    @SerializedName("startMills")
    var startMills: Long,
    @SerializedName("totalSecs")
    var totalSecs: Long,
    @SerializedName("additionalSecs")
    var additionalSecs: Long
) : Serializable