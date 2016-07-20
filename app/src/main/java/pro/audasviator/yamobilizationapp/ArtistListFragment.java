package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ArtistListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArtistLab mArtistLab;
    private List<Artist> mArtistList;
    private SwipeRefreshLayout mRefreshLayout;
    private int mScrollTo; // Костыль :(


    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArtistLab = ArtistLab.get(getActivity());
        mArtistList = mArtistLab.getArtists();
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

        // Скроллим RecyclerView до того же места, что и ViewPager
        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mScrollTo);

        // Обновим за пользователя для первого раза
        if (mArtistList.size() == 0) {
            new FetchArtistTask().execute(getContext());
        }

        return view;
    }

    public void scrollTo(int position) {
        mScrollTo = position;
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

                    ArtistViewPagerFragment artistViewPagerFragment = ArtistViewPagerFragment.newInstance(mPosition);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list_container, artistViewPagerFragment)
                            .addToBackStack(null)
                            .commit();
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

            // Кушает кучу памяти (1/7th heap size)
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

        // RecyclerView по умолчанию не перерабатывает вьюхи с неоконченной анимацией
        @Override
        public boolean onFailedToRecycleView(ArtistHolder holder) {
            return true;
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
