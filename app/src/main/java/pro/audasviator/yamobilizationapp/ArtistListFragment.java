package pro.audasviator.yamobilizationapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ArtistListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArtistLab mArtistLab;
    private List<Artist> mArtistList;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_artists_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ArtistAdapter());

        new FetchArtistTask().execute();

        return view;
    }

    private class ArtistHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private ImageView mCoverImageView;
        private Artist mArtist;

        public ArtistHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.artist_list_item_name_text_view);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.artist_list_item_image_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ArtistDetailActivity.newIntent(getActivity(), mArtist.getId());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), mCoverImageView, "cover_image");
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });
        }

        public void bindHolder(Artist artist) {
            mArtist = artist;
            String coverUrl = artist.getUrlOfSmallCover();
            String name = artist.getName();
            mNameTextView.setText(name);

            Picasso.with(getActivity()).load(coverUrl).into(mCoverImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) mCoverImageView.getDrawable()).getBitmap();
                    mArtist.setSmallCoverBitmap(bitmap);
                }

                @Override
                public void onError() {

                }
            });
        }

        private class SmallCoverTarget implements Target {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mCoverImageView.setImageBitmap(bitmap);
                mArtist.setSmallCoverBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                mCoverImageView.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mCoverImageView.setImageDrawable(placeHolderDrawable);
            }
        }
    }

    private class ArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {
        // TODO: Think about private list

        @Override
        public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.artist_list_item, parent, false);
            return new ArtistHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistHolder holder, int position) {
            Artist artist = mArtistList.get(position);
            holder.bindHolder(artist);
        }

        @Override
        public int getItemCount() {
            return mArtistList.size();
        }
    }

    private class FetchArtistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ArtistFetcher artistFetcher = new ArtistFetcher();
                String json = artistFetcher.getJson();
                List<Artist> artistList = artistFetcher.getArtistsFromJson(json);
                mArtistLab.setArtists(artistList);
                mArtistList = mArtistLab.getArtists();
            } catch (Exception e) {
                Log.e("TEST", "" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.getAdapter().notifyDataSetChanged();

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(new ArtistAdapter());
        }
    }
}
