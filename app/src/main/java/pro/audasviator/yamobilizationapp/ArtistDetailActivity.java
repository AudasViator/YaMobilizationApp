package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ArtistDetailActivity extends AppCompatActivity {
    private static final String ARG_ARTIST_ID = "artist_id";

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

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_detail_container);

        if (fragment == null) {
            fragment = ArtistDetailFragment.newInstance(artistId);
            fm.beginTransaction()
                    .add(R.id.fragment_detail_container, fragment)
                    .commit();
        }
    }
}