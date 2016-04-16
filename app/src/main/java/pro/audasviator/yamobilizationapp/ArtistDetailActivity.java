package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

public class ArtistDetailActivity extends AppCompatActivity {
    private static final String ARG_ARTIST_ID = "artist_id";

    private ViewPager mViewPager;
    private List<Artist> mArtists;

    public static Intent newIntent(Context context, int artistId) {
        Intent intent = new Intent(context, ArtistDetailActivity.class);
        intent.putExtra(ARG_ARTIST_ID, artistId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        int artistId = getIntent().getIntExtra(ARG_ARTIST_ID, -1);
        mViewPager = (ViewPager) findViewById(R.id.fragment_detail_container_view_pager);
        mArtists = ArtistLab.get(this).getArtists();

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Artist artist = mArtists.get(position);
                return ArtistDetailFragment.newInstance(artist.getId());
            }

            @Override
            public int getCount() {
                return mArtists.size();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(mViewPager,
                new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        insets = ViewCompat.onApplyWindowInsets(v, insets);
                        if (insets.isConsumed()) {
                            return insets;
                        }

                        boolean consumed = false;
                        for (int i = 0, count = mViewPager.getChildCount(); i < count; i++) {
                            ViewCompat.dispatchApplyWindowInsets(mViewPager.getChildAt(i), insets);
                            if (insets.isConsumed()) {
                                consumed = true;
                            }
                        }
                        return consumed ? insets.consumeSystemWindowInsets() : insets;
                    }
                });

        for (int i = 0; i < mArtists.size(); i++) {
            if (mArtists.get(i).getId() == artistId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }


    }
}