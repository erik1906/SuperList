package com.company.erde.superlist.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.company.erde.superlist.Fragments.HistoryFragment;
import com.company.erde.superlist.Fragments.ListFragment;
import com.company.erde.superlist.Fragments.ProductFragments;
import com.company.erde.superlist.R;
import com.company.erde.superlist.Realm.ProductCRUD;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private FloatingActionButton fbProduct;
    private FloatingActionButton fbList;
    private FloatingActionButton fbHistory;

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

        fbProduct = findViewById(R.id.fbProduct);
        fbHistory = findViewById(R.id.fbHistory);
        fbList = findViewById(R.id.fbList);
        fbProduct.hide();
        fbHistory.hide();
        fbList.show();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        fbProduct.hide();
                        fbHistory.hide();
                        fbList.show();
                        break;
                    case 1:
                        fbProduct.show();
                        fbHistory.hide();
                        fbList.hide();
                        break;
                    case 2:
                        fbProduct.hide();
                        fbList.hide();
                        fbHistory.show();
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

        //ProductCRUD.dropTable(realm);

        fbList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fbProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateUpdateProductActivity.class);
                startActivity(i);
                //Toast.makeText(view.getContext(),"product float",Toast.LENGTH_SHORT).show();
            }
        });

        fbHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"history float",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new ListFragment(),"Lists");
        viewPager.setAdapter(adapter);
        adapter.addFrag(new ProductFragments(),"Product");
        viewPager.setAdapter(adapter);
        adapter.addFrag(new HistoryFragment(),"History");
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