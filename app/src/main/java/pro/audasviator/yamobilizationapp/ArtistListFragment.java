package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArtistListFragment extends Fragment {
    public static final String EXTRA_STARTING_POSITION = "startingPosition";
    public static final String EXTRA_CURRENT_POSITION = "currentPosition";

    private RecyclerView mRecyclerView;
    private ArtistLab mArtistLab;
    private List<Artist> mArtistList;
    private SwipeRefreshLayout mRefreshLayout;

    private Bundle mTmpReenterState;
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mTmpReenterState != null) {
                int startingPosition = mTmpReenterState.getInt(EXTRA_STARTING_POSITION);
                int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_POSITION);
                if (startingPosition != currentPosition) {
                    // If startingPosition != currentPosition the user must have swiped to a
                    // different page in the DetailsActivity. We must update the shared element
                    // so that the correct one falls into place.
                    String newTransitionName = String.valueOf(mArtistList.get(currentPosition).getId());
                    View newSharedElement = mRecyclerView.findViewWithTag(newTransitionName);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(newTransitionName);
                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                }

                mTmpReenterState = null;
            }
        }
    };
    private boolean mIsDetailsActivityStarted;

    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    public static Intent intentForReenter(int currentPosition, int startingPosition) {
        Intent data = new Intent();
        data.putExtra(EXTRA_STARTING_POSITION, startingPosition);
        data.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        return data;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setExitSharedElementCallback(mCallback);

        mArtistLab = ArtistLab.get(getActivity());
        mArtistList = mArtistLab.getArtists();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsDetailsActivityStarted = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_artists_list_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                new FetchArtistTask().execute();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_artist_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        if (mArtistList.size() == 0) {
            mRefreshLayout.setRefreshing(true);
            new FetchArtistTask().execute();
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // После возвращения из деталей проверяем, скролил ли пользователь
    // Если да, то скролим список и пускаем анимацию
    public void onActivityReenter(Intent data) {
        mTmpReenterState = new Bundle(data.getExtras());
        int startingPosition = mTmpReenterState.getInt(EXTRA_STARTING_POSITION);
        int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_POSITION);
        if (startingPosition != currentPosition) {
            mRecyclerView.scrollToPosition(currentPosition);
        }

        getActivity().supportPostponeEnterTransition();
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                // Сначала должны забиндиться вьюхи (дабы понять, куда двигать изображение)
                mRecyclerView.requestLayout();
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    private class ArtistHolder extends RecyclerView.ViewHolder {
        private ImageView mCoverImageView;
        private TextView mNameTextView;
        private TextView mGenresTextView;
        private TextView mCountTextView;
        private int mPosition;

        public ArtistHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.artist_list_item_name_text_view);
            mGenresTextView = (TextView) itemView.findViewById(R.id.artist_list_item_genres_text_view);
            mCountTextView = (TextView) itemView.findViewById(R.id.artist_list_item_count_text_view);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.artist_list_item_image_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ArtistDetailActivity.newIntent(getContext(), mPosition);
                    intent.putExtra(EXTRA_STARTING_POSITION, mPosition);

                    if (!mIsDetailsActivityStarted) {
                        mIsDetailsActivityStarted = true;
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mCoverImageView, ViewCompat.getTransitionName(mCoverImageView));
                        startActivity(intent, options.toBundle());
                    }
                }
            });
        }

        public void bindHolder(Artist artist, int position) {
            String coverUrl = artist.getUrlOfSmallCover();
            String name = artist.getName();
            String genres = artist.getGenres();
            int countOfAlbums = artist.getCountOfAlbums();
            int countOfSongs = artist.getCountOfTracks();
            String count = getResources().getQuantityString(R.plurals.count_of_albums, countOfAlbums, countOfAlbums)
                    + "\n" + getResources().getQuantityString(R.plurals.count_of_songs, countOfSongs, countOfSongs);

            mNameTextView.setText(name);
            mGenresTextView.setText(genres);
            mCountTextView.setText(count);

            mPosition = position;

            // Используем id в качестве тега (для поиска вьюхи) и имени для анимации
            String tagAndName = String.valueOf(artist.getId());
            ViewCompat.setTransitionName(mCoverImageView, tagAndName);
            mCoverImageView.setTag(tagAndName);

            Picasso.with(getContext()).load(coverUrl).placeholder(R.drawable.the_place_holder).into(mCoverImageView);
        }
    }

    private class ArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {
        private List<Artist> mArtists;

        public ArtistAdapter(List<Artist> artists) {
            mArtists = artists;
        }

        @Override
        public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.artist_list_item, parent, false);
            return new ArtistHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistHolder holder, int position) {
            Artist artist = mArtists.get(position);
            holder.bindHolder(artist, position);
        }

        @Override
        public int getItemCount() {
            return mArtistList.size();
        }
    }

    private class FetchArtistTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "AsyncTask";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ArtistFetcher artistFetcher = new ArtistFetcher();
                String json = artistFetcher.getJson();
                List<Artist> artistList = artistFetcher.getArtistsFromJson(json);
                Collections.sort(artistList);
                mArtistLab.setArtists(artistList);
                mArtistList = artistList;
            } catch (IOException ioe) {
                Log.e(TAG, "Can't get json", ioe);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mRefreshLayout.setRefreshing(false);
            mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));
        }
    }

    // Просто рисуем разделители между вьюхами в списке
    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
