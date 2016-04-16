package pro.audasviator.yamobilizationapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ArtistDetailFragment extends Fragment {
    private static final String ARG_ARTIST_ID = "artist_id";

    private Artist mArtist;

    private ImageView mCoverImageView;
    private TextView mDescriptionTextView;
    private TextView mCountTextView;

    public static ArtistDetailFragment newInstance(int artistId) {
        Bundle args = new Bundle();
        args.putInt(ARG_ARTIST_ID, artistId);

        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getArguments().getInt(ARG_ARTIST_ID);
        mArtist = ArtistLab.get(getActivity()).getArtist(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mArtist.getName());

        int countOfAlbums = mArtist.getCountOfAlbums();
        int countOfSongs = mArtist.getCountOfTracks();
        String count = getResources().getQuantityString(R.plurals.count_of_albums, countOfAlbums, countOfAlbums)
                + ", " + getResources().getQuantityString(R.plurals.count_of_songs, countOfSongs, countOfSongs);
        mCountTextView = (TextView) view.findViewById(R.id.fragment_detail_count_text_box);
        mCountTextView.setText(count);

        mCoverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image_view);
        mDescriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_description_text_view);
        mDescriptionTextView.setText(mArtist.getDescription());

        Glide.with(this).load(mArtist.getUrlOfSmallCover()).into(mCoverImageView);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}