package pro.audasviator.yamobilizationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ArtistListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 42;

    private ArtistListFragment mArtistListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        setTitle(R.string.fragment_list_name);

        FragmentManager fm = getSupportFragmentManager();
        mArtistListFragment = (ArtistListFragment) fm.findFragmentById(R.id.fragment_list_container);
        if (mArtistListFragment == null) {
            mArtistListFragment = ArtistListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_list_container, mArtistListFragment)
                    .commit();
        }
    }

    // После возвращения из деталей, передаёт во фрагмент куда пользователь доскролил
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mArtistListFragment.onActivityReenter(data);
    }
}