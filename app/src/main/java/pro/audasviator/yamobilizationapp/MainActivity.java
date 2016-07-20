package pro.audasviator.yamobilizationapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ArtistViewPagerFragment.Callbacks {
    private ArtistListFragment mArtistListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        setTitle(R.string.fragment_list_name);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_list_container);

        mArtistListFragment = (ArtistListFragment) fm.findFragmentByTag("list");

        if (fragment == null) {
            fragment = ArtistListFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_list_container, fragment, "list")
                    .commit();
        }

    }

    @Override
    public void onDetach(int currentPosition) {
        if (mArtistListFragment != null) {
            mArtistListFragment.scrollTo(currentPosition);
        }
    }
}