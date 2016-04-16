package pro.audasviator.yamobilizationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

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

    public void onArtistSelected(Artist artist) {
        if (findViewById(R.id.fragment_detail_container) == null) {
            Intent intent = ArtistDetailActivity.newIntent(this, artist.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = ArtistDetailFragment.newInstance(artist.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, newDetail)
                    .commit();
        }
    }
}