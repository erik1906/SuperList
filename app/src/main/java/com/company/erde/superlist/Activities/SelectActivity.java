package com.company.erde.superlist.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.company.erde.superlist.Adapters.ProductRecyclerViewAdapter;
import com.company.erde.superlist.Fragments.ProductFragments;
import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.Realm.ProductCRUD;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmObject;

public class SelectActivity extends AppCompatActivity {

    private Realm realm;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProductRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        registerForContextMenu(recyclerView);
        final OrderedRealmCollection productData = ProductCRUD.orderedRealmCollection(realm);
        adapter= new ProductRecyclerViewAdapter(productData, true, new ProductRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Product p = (Product) productData.get(position);
                Intent intent = new Intent();
                intent.putExtra("id", p.getId());
                setResult(RESULT_OK, intent);
                finish();
                //Toast.makeText(getContext(),"Seleccion "+ position,Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapter);
    }
}
