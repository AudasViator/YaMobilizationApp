package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArtistListFragment extends Fragment {
    public static final String EXTRA_STARTING_POSITION = "startingPosition";
    public static final String EXTRA_CURRENT_POSITION = "currentPosition";

    private static final int REQUEST_CODE = 42;

    private RecyclerView mRecyclerView;
    private ArtistLab mArtistLab;
    private List<Artist> mArtistList;
    private SwipeRefreshLayout mRefreshLayout;

    private boolean mIsDetailsActivityStarted; // Вдруг пользователь просто свернул активти с этим фрагментом?
    private Bundle mTmpReenterState; // Информация о shared elements (id|tag вьюхи, показывающей обложку)

    // Вызывается перед началом анимации в другое активити
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mTmpReenterState != null) {
                int startingPosition = mTmpReenterState.getInt(EXTRA_STARTING_POSITION); // Откуда начали скоролить ViewPager
                int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_POSITION); // Куда доскролили
                if (startingPosition != currentPosition) {
                    // Добавляем новую обложку из ViewPager`а в shared elements
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

    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    // Взывается DetailFragment, дабы передать откуда и куда доскролился ViewPager
    public static Intent makeIntentForAnimation(int currentPosition, int startingPosition) {
        Intent data = new Intent();
        data.putExtra(EXTRA_STARTING_POSITION, startingPosition);
        data.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        return data;
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
                new FetchArtistTask().execute(getContext());
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_artist_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));
        mRecyclerView.addItemDecoration(new amazingDividerItemDecoration(getContext()));

        // Обновим за пользователя для первого раза
        if (mArtistList.size() == 0) {
            new FetchArtistTask().execute(getContext());
        }

        return view;
    }

    // Подробности в ArtistDetailActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTmpReenterState = new Bundle(data.getExtras());
        int startingPosition = mTmpReenterState.getInt(EXTRA_STARTING_POSITION);
        int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_POSITION);
        if (startingPosition != currentPosition) {
            mRecyclerView.scrollToPosition(currentPosition);
        }
    }

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

                // Сначала должны забиндиться вьюхи (дабы понять, какую обложку двигать)
                mRecyclerView.requestLayout();

                // А уже потом должна сработать анимация
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

                    if (!mIsDetailsActivityStarted) { // Дабы не тыкали дважды
                        mIsDetailsActivityStarted = true;
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mCoverImageView, ViewCompat.getTransitionName(mCoverImageView));
                        startActivityForResult(intent, REQUEST_CODE, options.toBundle());
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

            // Используем id в качестве имени для анимации и тега (для поиска вьюхи)
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

    private class FetchArtistTask extends AsyncTask<Context, Void, Boolean> {
        private static final String TAG = "AsyncTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Context... params) {
            if (!isDeviceOnline(params[0])) {
                return false;
            }

            try {
                ArtistFetcher artistFetcher = new ArtistFetcher();
                String json = artistFetcher.getJson();
                List<Artist> artistList = artistFetcher.getArtistsFromJson(json);
                Collections.sort(artistList); // Гайдлайны по Material дизайну настоятельно рекоменуют отсортировать
                mArtistLab.setArtists(artistList);
                mArtistList = artistList;
                return true;
            } catch (IOException ioe) {
                Log.e(TAG, "Can't get json", ioe);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            super.onPostExecute(param);

            mRefreshLayout.setRefreshing(false);
            // Троичный bool великолепен
            if (param == null) {
                Toast.makeText(getContext(), getString(R.string.fragment_list_downloading_error_toast), Toast.LENGTH_SHORT).show();
            } else if (param) {
                mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));
            } else {
                Toast.makeText(getContext(), getString(R.string.fragment_list_no_internet_toast), Toast.LENGTH_SHORT).show();
            }


        }

        private boolean isDeviceOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }

    // Просто рисуем разделители между вьюхами в списке
    private class amazingDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public amazingDividerItemDecoration(Context context) {
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
