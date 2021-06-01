package com.example.fourth.models

import android.os.Parcel
import android.os.Parcelable

data class FriendsUser(
        var id: String ?= "",
        var name: String ?= "",
        var surname: String ?= "",
        var image: String ?= "",
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(name)
                parcel.writeString(surname)
                parcel.writeString(image)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FriendsUser> {
                override fun createFromParcel(parcel: Parcel): FriendsUser {
                        return FriendsUser(parcel)
                }

                override fun newArray(size: Int): Array<FriendsUser?> {
                        return arrayOfNulls(size)
                }
        }
}

