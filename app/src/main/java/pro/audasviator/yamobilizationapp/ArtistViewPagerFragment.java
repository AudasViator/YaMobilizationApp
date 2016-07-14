package pro.audasviator.yamobilizationapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by prier on 14.07.2016.
 */
public class ArtistViewPagerFragment extends Fragment {
    private static final String ARG_CURRENT_POSITION = "currentPosition";
    private static final String STATE_CURRENT_PAGE_POSITION = "currentPage";

    private static final String EXTRA_CURRENT_POSITION = "currentPosition";
    private static final String EXTRA_STARTING_POSITION = "startingPosition";

    private ViewPager mViewPager;
    private List<Artist> mArtists;
    private ArtistDetailFragment mCurrentArtistDetailFragment;
    private int mCurrentPosition;
    private int mStartingPosition;
    private boolean mIsReturning;

    public static ArtistViewPagerFragment newInstance(int currentPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, currentPosition);

        ArtistViewPagerFragment fragment = new ArtistViewPagerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_artist_detail, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.fragment_detail_container_view_pager);
        mArtists = ArtistLab.get(getContext()).getArtists();

        FragmentManager fm = getFragmentManager();

        mViewPager.setAdapter(new ArtistDetailFragmentPagerAdapter(fm));
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer(mViewPager));

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

        mViewPager.setCurrentItem(mCurrentPosition);

        return view;
    }

    private static class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;

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