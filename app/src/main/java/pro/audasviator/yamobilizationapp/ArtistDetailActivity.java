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

    // Вызывается после кнопки назад, но перед анимацией
    // Добавляет текущее изображение в shared elements
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                // Если CollapsingBar свёрнут, то будет null
                ImageView sharedElement = mCurrentArtistDetailFragment.getCoverImage();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartingPosition != mCurrentPosition) {
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
        mArtists = ArtistLab.get(getApplicationContext()).getArtists();

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new ArtistDetailFragmentPagerAdapter(fm));
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer(mViewPager));
        mViewPager.setClipChildren(false);
        mViewPager.setClipToPadding(false);

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

    @Override
    public void onBackPressed() {
        mIsReturning = true;
        setResult(RESULT_OK, ArtistListFragment.makeIntentForAnimation(mCurrentPosition, mStartingPosition));
        super.onBackPressed();
    }

    private static class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.8f;

        private ViewPager mViewPager;
        private float mPositionFixer;
        private boolean isSetFixer = false;

        public ZoomOutPageTransformer(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        public void transformPage(View view, float position) {
            final int pageWidth = view.getWidth();
            final int pageHeight = view.getHeight();

            if (!isSetFixer) {
                final int mClientWidth = mViewPager.getMeasuredWidth() -
                        mViewPager.getPaddingLeft() - mViewPager.getPaddingRight();
                mPositionFixer = ((float) ViewCompat.getPaddingStart(mViewPager)) / mClientWidth;
                isSetFixer = true;
            }

            position -= mPositionFixer;

            if (position <= 1) {
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                final float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        }
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