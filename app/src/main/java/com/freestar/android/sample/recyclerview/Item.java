package com.freestar.android.sample.recyclerview;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

   @SerializedName("albumId")
   @Expose
   private Integer albumId;
   @SerializedName("id")
   @Expose
   private Integer id;
   @SerializedName("title")
   @Expose
   private String title;
   @SerializedName("url")
   @Expose
   private String url;
   @SerializedName("thumbnailUrl")
   @Expose
   private String thumbnailUrl;
   public final static Creator<Item> CREATOR = new Creator<Item>() {

      @SuppressWarnings({"unchecked"})
      public Item createFromParcel(Parcel in) {
         return new Item(in);
      }

      public Item[] newArray(int size) {
         return (new Item[size]);
      }

   };

   protected Item(Parcel in) {
      this.albumId = ((Integer) in.readValue((Integer.class.getClassLoader())));
      this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
      this.title = ((String) in.readValue((String.class.getClassLoader())));
      this.url = ((String) in.readValue((String.class.getClassLoader())));
      this.thumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
   }

   public Item() {
   }

   public Integer getAlbumId() {
      return albumId;
   }

   public void setAlbumId(Integer albumId) {
      this.albumId = albumId;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getThumbnailUrl() {
      return thumbnailUrl;
   }

   public void setThumbnailUrl(String thumbnailUrl) {
      this.thumbnailUrl = thumbnailUrl;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeValue(albumId);
      dest.writeValue(id);
      dest.writeValue(title);
      dest.writeValue(url);
      dest.writeValue(thumbnailUrl);
   }

   public int describeContents() {
      return 0;
   }

}
