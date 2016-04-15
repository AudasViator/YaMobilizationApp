package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArtistLab mArtistLab;
    private List<Artist> mArtistList;
    private Callbacks mCallbacks;

    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtistLab = ArtistLab.get(getActivity());
        mArtistList = mArtistLab.getArtists();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_artists_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));

        new FetchArtistTask().execute();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks {
        void onArtistSelected(Artist artist, @Nullable View animatedView);
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
                    mCallbacks.onArtistSelected(mArtist, mCoverImageView);
                }
            });
        }

        public void bindHolder(Artist artist) {
            mArtist = artist;
            String coverUrl = artist.getUrlOfSmallCover();
            String name = artist.getName();
            mNameTextView.setText(name);

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
                mArtistList = artistList;
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
            mRecyclerView.setAdapter(new ArtistAdapter(mArtistList));
        }
    }
}
