package pro.audasviator.yamobilizationapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private static final String ARG_ARTIST_ID = "artist_id";

    private Artist mArtist;

    private ImageView mCoverImageView;
    private TextView mTextView;
    private TextView mDescriptionTextView;

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

        mTextView = (TextView) view.findViewById(R.id.fragment_detail_name_text_box);
        mTextView.setText(mArtist.getName());

        mCoverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image_view);
        mDescriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_description_text_view);
        mDescriptionTextView.setText(mArtist.getDescription());

        Picasso.with(getContext()).load(mArtist.getUrlOfSmallCover())
                .networkPolicy(NetworkPolicy.OFFLINE).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(new BitmapDrawable(getResources(), bitmap)).into(mCoverImageView);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Picasso.with(getContext()).load(mArtist.getUrlOfBigCover()).placeholder(R.drawable.the_place_holder).into(mCoverImageView);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        return view;
    }
}