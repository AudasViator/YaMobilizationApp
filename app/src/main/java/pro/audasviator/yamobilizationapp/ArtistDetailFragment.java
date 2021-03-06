package pro.audasviator.yamobilizationapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ArtistDetailFragment extends Fragment {
    private static final String ARG_STARTING_POSITION = "startingPosition";
    private static final String ARG_CURRENT_POSITION = "currentPosition";

    private Artist mArtist;

    private ImageView mCoverImageView;

    private int mStartingPosition;
    private int mCurrentPosition;
    private boolean mIsExpanded; // Toolbar

    public static ArtistDetailFragment newInstance(int currentPosition, int startingPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, currentPosition);
        args.putInt(ARG_STARTING_POSITION, startingPosition);

        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartingPosition = getArguments().getInt(ARG_STARTING_POSITION);
        mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
        mArtist = ArtistLab.get(getContext()).getArtist(mCurrentPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_detail_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.fragment_detail_collapsing_toolbar);
        collapsingToolbar.setTitle(mArtist.getName());

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_detail_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mIsExpanded = (verticalOffset == 0); // Если оффсет == 0, значит тулбар развёрнут
            }
        });

        TextView countTextView = (TextView) view.findViewById(R.id.fragment_detail_count_text_box);
        int countOfAlbums = mArtist.getCountOfAlbums();
        int countOfSongs = mArtist.getCountOfTracks();
        String count = getResources().getQuantityString(R.plurals.count_of_albums, countOfAlbums, countOfAlbums)
                + "  " + getResources().getQuantityString(R.plurals.count_of_songs, countOfSongs, countOfSongs);
        countTextView.setText(count); // Один ТекстВью, вместо двух. Экономия

        mCoverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image_view);
        ViewCompat.setTransitionName(mCoverImageView, String.valueOf(mArtist.getId()));

        TextView genresTextView = (TextView) view.findViewById(R.id.fragment_detail_genres_text_box);
        genresTextView.setText(mArtist.getGenres());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_description_text_view);
        descriptionTextView.setText(mArtist.getDescription());

        // Пробуем загрузить из кеша маленькую обложку
        // Затем в любом случае скачиваем большую обложку
        Picasso.with(getContext()).load(mArtist.getUrlOfSmallCover())
                .noFade().networkPolicy(NetworkPolicy.OFFLINE).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                startPostponedEnterTransition();
                Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(new BitmapDrawable(getResources(), bitmap)).into(mCoverImageView);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                startPostponedEnterTransition();
                Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(R.drawable.the_place_holder)
                        .priority(Picasso.Priority.HIGH).into(mCoverImageView);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        return view;
    }

    private void startPostponedEnterTransition() {
        // Проверка, что анимация нужна
        if (mCurrentPosition == mStartingPosition) {
            mCoverImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mCoverImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    getActivity().supportStartPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    @Nullable
    public ImageView getCoverImage() {
        if (mIsExpanded) {
            return mCoverImageView;
        }
        return null;
    }
}