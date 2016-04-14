package pro.audasviator.yamobilizationapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ArtistDetailFragment extends Fragment {
    private static final String ARG_ARTIST_ID = "artist_id";

    private int mId;
    private ImageView mCoverImageView;
    private TextView mTextView;
    private TextView mDescriptionTextView;
    private Toolbar mToolbar;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        String name = getActivity().getIntent().getStringExtra("Name");
        String url = getActivity().getIntent().getStringExtra("Cover");
        Artist artist = (Artist) getActivity().getIntent().getSerializableExtra("Artist");
        Drawable cover = new BitmapDrawable(getResources(), (Bitmap) getActivity().getIntent().getParcelableExtra("Image"));

        mTextView = (TextView) view.findViewById(R.id.fragment_detail_name_text_box);
        mTextView.setText(name);

        mCoverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image_view);
        mDescriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_description_text_view);
        mDescriptionTextView.setText(artist.getDescription());

        mToolbar = (Toolbar) view.findViewById(R.id.fragment_detail_toolbar);
        mToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        Picasso.with(getActivity()).load(url).placeholder(cover).into(mCoverImageView);

        return view;
    }
}