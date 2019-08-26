package com.kogero.levelcounter.model

import android.os.Parcel
import android.os.Parcelable

class Statistics(
    var statisticsId: Int,
    var wins: Int,
    var gamesPlayed: Int,
    var roundsPlayed: Int,
    var playTime: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(statisticsId)
        parcel.writeInt(wins)
        parcel.writeInt(gamesPlayed)
        parcel.writeInt(roundsPlayed)
        parcel.writeInt(playTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Statistics> {
        override fun createFromParcel(parcel: Parcel): Statistics {
            return Statistics(parcel)
        }

        override fun newArray(size: Int): Array<Statistics?> {
            return arrayOfNulls(size)
        }
    }
}
