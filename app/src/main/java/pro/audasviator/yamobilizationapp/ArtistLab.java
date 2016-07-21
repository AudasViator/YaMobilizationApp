package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ArtistLab {
    private static final String TAG = "ArtistLab";
    private static final String ARTIST_LIST_NAME = "artistList";

    private static ArtistLab sArtistLab;

    private Context mContext;
    private List<Artist> mArtists;

    private ArtistLab(Context context) {
        mContext = context.getApplicationContext();
        mArtists = new ArrayList<>();
    }

    public static ArtistLab get(Context context) {
        if (sArtistLab == null) {
            sArtistLab = new ArtistLab(context);
        }
        return sArtistLab;
    }

    public List<Artist> getArtists() {
        List<Artist> tempArtists = loadArtists();

        if (tempArtists != null) {
            mArtists = tempArtists;
        } else {
            mArtists = new ArrayList<>();
        }
        return mArtists;
    }

    public void setArtists(List<Artist> artists) {
        mArtists = artists;
        saveArtists(mArtists);
    }

    public Artist getArtist(int position) {
        return mArtists.get(position);
    }

    // В БД нет смысла, слишком простой случай
    public void saveArtists(List<Artist> artists) {
        try {
            FileOutputStream fos = mContext.openFileOutput(ARTIST_LIST_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(artists);
            oos.close();
        } catch (IOException ioe) {
            Log.w(TAG, "Can't save artists: " + ioe.getMessage());
        }
    }

    public List<Artist> loadArtists() {
        try {
            FileInputStream fis;
            fis = mContext.openFileInput(ARTIST_LIST_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Artist> artists = (ArrayList<Artist>) ois.readObject();
            ois.close();
            return artists;
        } catch (Exception e) {
            Log.w(TAG, "Can't load artists: " + e.getMessage());
            File file = new File(mContext.getFilesDir().getAbsolutePath() + "/" + ARTIST_LIST_NAME);
            if (!file.delete()) {
                Log.e(TAG, "Can't delete file!");
            }
            return null;
        }
    }
}