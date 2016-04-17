package pro.audasviator.yamobilizationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

public class ArtistDetailActivity extends AppCompatActivity {
    private static final String STATE_CURRENT_PAGE_POSITION = "currentPage";

    private static final String EXTRA_CURRENT_POSITION = "currentPosition";
    private static final String EXTRA_STARTING_POSITION = "startingPosition";

    private ViewPager mViewPager;
    private List<Artist> mArtists;
    private ArtistDetailFragment mCurrentArtistDetailFragment;
    private int mCurrentPosition;
    private int mStartingPosition;
    private boolean mIsReturning;

    // Вызывается после кнопки назад в деталях, но перед анимацией
    // Добавляет текущее изображение в shared elements
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = mCurrentArtistDetailFragment.getCoverImage();
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (mStartingPosition != mCurrentPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    String transactionName = ViewCompat.getTransitionName(sharedElement);
                    names.add(transactionName);
                    sharedElements.clear();
                    sharedElements.put(transactionName, sharedElement);
                }
            }
        }
    };

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, ArtistDetailActivity.class);
        intent.putExtra(EXTRA_CURRENT_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        supportPostponeEnterTransition();
        setEnterSharedElementCallback(mCallback);

        mStartingPosition = getIntent().getIntExtra(EXTRA_STARTING_POSITION, 0);
        if (savedInstanceState == null) {
            mCurrentPosition = mStartingPosition;
        } else {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
        }

        mViewPager = (ViewPager) findViewById(R.id.fragment_detail_container_view_pager);
        mArtists = ArtistLab.get(this).getArtists();

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new ArtistDetailFragmentPagerAdapter(fm));
        mViewPager.setCurrentItem(mCurrentPosition);

        // Отслеживаем текущую страницу для изменения изображения для анимации
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });

        // Исправление бага у ViewPager, когда тулбар рисовался под статус баром
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
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE_POSITION, mCurrentPosition);
    }

    // Перед анимацией, отправляем в ArtistListFragment
    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        setResult(RESULT_OK, ArtistListFragment.intentForReenter(mCurrentPosition, mStartingPosition));
        super.finishAfterTransition();
    }

    private class ArtistDetailFragmentPagerAdapter extends FragmentStatePagerAdapter {
        public ArtistDetailFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ArtistDetailFragment.newInstance(position, mStartingPosition);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentArtistDetailFragment = (ArtistDetailFragment) object;
        }

        @Override
        public int getCount() {
            return mArtists.size();
        }
    }
}