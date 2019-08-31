package com.kogero.levelcounter.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Date

data class Game (
    val id: Int,
    val time: Long,
    val dateTime: Date,
    val inGameUsers: List<InGameUser>,
    val hostingUserId: String,
    val isRunning: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        TODO("dateTime"),
        TODO("inGameUsers"),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(time)
        parcel.writeString(hostingUserId)
        parcel.writeByte(if (isRunning) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}