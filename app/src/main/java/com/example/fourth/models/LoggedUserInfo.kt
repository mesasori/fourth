package com.example.fourth.models

import android.os.Parcel
import android.os.Parcelable

data class LoggedUserInfo(
        var id: String? = "",
        var name: String? = "",
        var surname: String? = "",
        var email: String? = "",
        var image: String? = "",
        var password: String ?= "",
        var birth: String ?= "",
        var phone: String ?= ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(email)
        parcel.writeString(image)
        parcel.writeString(password)
        parcel.writeString(birth)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoggedUserInfo> {
        override fun createFromParcel(parcel: Parcel): LoggedUserInfo {
            return LoggedUserInfo(parcel)
        }

        override fun newArray(size: Int): Array<LoggedUserInfo?> {
            return arrayOfNulls(size)
        }
    }
}
