package pro.audasviator.yamobilizationapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ArtistDetailFragment extends Fragment {
    private static final String ARG_CURRENT_POSITION = "currentPosition";

    private Artist mArtist;

    private ImageView mCoverImageView;

    private int mCurrentPosition;
    private TextView mCountTextView;
    private TextView mGenresTextView;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mDescriptionTextView;


    public static ArtistDetailFragment newInstance(int currentPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, currentPosition);

        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
        mArtist = ArtistLab.get(getContext()).getArtist(mCurrentPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        mCollapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.fragment_detail_collapsing_toolbar);
        mCountTextView = (TextView) view.findViewById(R.id.fragment_detail_count_text_box);
        mCoverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image_view);
        mGenresTextView = (TextView) view.findViewById(R.id.fragment_detail_genres_text_box);
        mDescriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_description_text_view);

        int countOfAlbums = mArtist.getCountOfAlbums();
        int countOfSongs = mArtist.getCountOfTracks();
        String count = getResources().getQuantityString(R.plurals.count_of_albums, countOfAlbums, countOfAlbums)
                + "  " + getResources().getQuantityString(R.plurals.count_of_songs, countOfSongs, countOfSongs);
        mCountTextView.setText(count); // Один ТекстВью, вместо двух. Экономия

        mGenresTextView.setText(mArtist.getGenres());
        mDescriptionTextView.setText(mArtist.getDescription());

        // Пробуем загрузить из кеша маленькую обложку
        // Затем в любом случае скачиваем большую обложку
        Picasso.with(getContext()).load(mArtist.getUrlOfSmallCover())
                .noFade().networkPolicy(NetworkPolicy.OFFLINE).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (isAdded()) {
                    Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(new BitmapDrawable(getResources(), bitmap)).into(mCoverImageView);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                if (isAdded()) {
                    Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(R.drawable.the_place_holder)
                            .priority(Picasso.Priority.HIGH).into(mCoverImageView);
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        return view;
    }
}