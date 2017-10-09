package com.company.erde.superlist;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.company.erde.superlist.Real.ProductCRUD;
import com.company.erde.superlist.RealModels.Product;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager =  findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout =  findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        //showToast("One");
                        break;
                    case 1:
                        //showToast("Two");
                        break;
                    case 2:
                        //showToast("Three");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        realm = Realm.getDefaultInstance();

        ProductCRUD.read(realm);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new ListFragment(),"Product");
        viewPager.setAdapter(adapter);
        adapter.addFrag(new ProductFragments(),"Product");
        viewPager.setAdapter(adapter);
        adapter.addFrag(new HistoryFragment(),"Product");
        viewPager.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
