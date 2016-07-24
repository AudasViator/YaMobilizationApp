package pro.audasviator.yamobilizationapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements ArtistViewPagerFragment.Callbacks {
    private ArtistListFragment mArtistListFragment;
    private RelativeLayout mHeadsetLayout;
    private ImageView mRadioImageView;
    private ImageView mMusicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        mHeadsetLayout = (RelativeLayout) findViewById(R.id.activity_headset_layout);
        mRadioImageView = (ImageView) findViewById(R.id.activity_headset_button_radio);
        mMusicImageView = (ImageView) findViewById(R.id.activity_headset_button_music);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_list_container);

        mArtistListFragment = (ArtistListFragment) fm.findFragmentByTag("list");

        if (fragment == null) {
            fragment = ArtistListFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_list_container, fragment, "list")
                    .commit();
            mArtistListFragment = (ArtistListFragment) fragment;
        }

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("EEE", "ROCK");
                mHeadsetLayout.setVisibility(View.VISIBLE);
            }
        }, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        mRadioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp("ru.yandex.radio");
            }
        });

        mMusicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp("ru.yandex.music");
            }
        });
    }

    @Override
    public void onDetach(int currentPosition) {
        if (mArtistListFragment != null) {
            mArtistListFragment.scrollTo(currentPosition);
        }
    }

    private void openApp(String packageName) {
        PackageManager manager = getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            String url = "https://play.google.com/store/apps/details?id=" + packageName;
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } else {
            i.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        startActivity(i);
    }
}