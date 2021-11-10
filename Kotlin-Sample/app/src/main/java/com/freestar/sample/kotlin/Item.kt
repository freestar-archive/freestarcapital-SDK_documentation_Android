package com.freestar.sample.kotlin

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel

class Item : Parcelable {
    @SerializedName("albumId")
    @Expose
    var albumId: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("thumbnailUrl")
    @Expose
    var thumbnailUrl: String? = null

    protected constructor(`in`: Parcel) {
        albumId = `in`.readValue(Int::class.java.classLoader) as Int?
        id = `in`.readValue(Int::class.java.classLoader) as Int?
        title = `in`.readValue(String::class.java.classLoader) as String?
        url = `in`.readValue(String::class.java.classLoader) as String?
        thumbnailUrl = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(albumId)
        dest.writeValue(id)
        dest.writeValue(title)
        dest.writeValue(url)
        dest.writeValue(thumbnailUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    /*companion object {
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item?> {
            override fun createFromParcel(`in`: Parcel): Item? {
                return Item(`in`)
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }
    }*/

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}