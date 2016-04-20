package pro.audasviator.yamobilizationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ArtistListActivity extends AppCompatActivity {
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

    // ...is to let the called Activity send a hint about its state
    //    so that this underlying Activity can prepare to be exposed...
    // Добавляет в shared elements обложку из текущего положения ViewPager
    // для обратной анимации. Заодно двигает лист к тому месту, куда доскролил пользователь
    // Вызывается лишь в SDK>21, до него анимации нет
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mArtistListFragment.onActivityReenter(data);
        }
    }


    // Просто двигает список к нужному месту
    // На послеЛолипопах взывается уже после анимации. Слишком поздно
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}