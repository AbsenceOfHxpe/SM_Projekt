package com.example.sm_project.Helper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorytable")
public class CategoryTable implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int imgPath;

    public CategoryTable() {
    }

    public CategoryTable(String name, int imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    protected CategoryTable(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imgPath = in.readInt();
    }

    public static final Creator<CategoryTable> CREATOR = new Creator<CategoryTable>() {
        @Override
        public CategoryTable createFromParcel(Parcel in) {
            return new CategoryTable(in);
        }

        @Override
        public CategoryTable[] newArray(int size) {
            return new CategoryTable[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(imgPath);
    }
}
