package pro.audasviator.yamobilizationapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prier on 13.04.2016.
 */
public class ArtistLab {
    private static ArtistLab sArtistLab;

    private List<Artist> mArtists;

    private ArtistLab(Context context) {
        mArtists = new ArrayList<>();
    }

    public static ArtistLab get(Context context) {
        if (sArtistLab == null) {
            sArtistLab = new ArtistLab(context);
        }
        return sArtistLab;
    }

    public List<Artist> getArtists() {
        return mArtists;
    }

    public void setArtists(List<Artist> artists) {
        mArtists = artists;
    }

    public Artist getArtist(int id) {
        for (Artist artist : mArtists) {
            if (artist.getId() == id) {
                return artist;
            }
        }
        return null;
    }
}
