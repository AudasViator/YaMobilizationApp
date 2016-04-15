package pro.audasviator.yamobilizationapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ArtistListActivity extends AppCompatActivity
        implements ArtistListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_and_detail);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_list_container);

        if (fragment == null) {
            fragment = ArtistListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_list_container, fragment)
                    .commit();
        }
    }

    public void onArtistSelected(Artist artist, View animatedView) {
        if (findViewById(R.id.fragment_detail_container) == null) {
            Intent intent = ArtistDetailActivity.newIntent(this, artist.getId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animatedView, "cover_image");
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else {
            Fragment newDetail = ArtistDetailFragment.newInstance(artist.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, newDetail)
                    .commit();
        }
    }
}