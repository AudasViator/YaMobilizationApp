package pro.audasviator.yamobilizationapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class Artist implements Comparable<Artist>, Parcelable, Serializable {

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("genres")
    private String[] mGenres;
    @SerializedName("tracks")
    private int mCountOfTracks;
    @SerializedName("albums")
    private int mCountOfAlbums;
    @SerializedName("link")
    private String mSiteUrl;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("cover")
    private Map<String, String> mCover;

    protected Artist(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mGenres = in.createStringArray();
        mCountOfTracks = in.readInt();
        mCountOfAlbums = in.readInt();
        mSiteUrl = in.readString();
        mDescription = in.readString();
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getGenres() {
        //Когда лень писать даже преобразование из массива
        return TextUtils.join(", ", mGenres);
    }

    public int getCountOfTracks() {
        return mCountOfTracks;
    }

    public int getCountOfAlbums() {
        return mCountOfAlbums;
    }

    public String getSiteUrl() {
        return mSiteUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrlOfBigCover() {
        return mCover.get("big");
    }

    public String getUrlOfSmallCover() {
        return mCover.get("small");
    }

    @Override
    public int compareTo(@NonNull Artist another) {
        return getName().compareTo(another.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeStringArray(mGenres);
        dest.writeInt(mCountOfTracks);
        dest.writeInt(mCountOfAlbums);
        dest.writeString(mSiteUrl);
        dest.writeString(mDescription);
    }
}
// И прокоментировать нечего
