package charlezz.servicetest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Charles on 24/04/2017.
 */

public class MusicItem implements Parcelable {
    public String name;
    public String path;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
    }

    public MusicItem() {
    }

    public MusicItem(String name, String path) {
        this.name = name;
        this.path = path;
    }

    protected MusicItem(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<MusicItem> CREATOR = new Parcelable.Creator<MusicItem>() {
        @Override
        public MusicItem createFromParcel(Parcel source) {
            return new MusicItem(source);
        }

        @Override
        public MusicItem[] newArray(int size) {
            return new MusicItem[size];
        }
    };
}
