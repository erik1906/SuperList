package com.company.erde.superlist.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.company.erde.superlist.Adapters.ListContentRecyclerViewAdapter;
import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.History;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.Realm.HistoryCRUD;
import com.company.erde.superlist.Realm.SuperListCRUD;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class HistoryProductActivity extends AppCompatActivity {

    private int listid;
    private TextView tvTotal;

    private Realm realm;

    private ListContentRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Product product;
    private History his;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_product);

        realm = Realm.getDefaultInstance();

        listid = getIntent().getIntExtra("id",-1);

        his = HistoryCRUD.select(realm, listid);
        tvTotal = findViewById(R.id.tvTotal);



        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("List: "+ his.getName());
        setSupportActionBar(toolbar);


        recyclerView = findViewById(R.id.rvProductList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



        tvTotal.setText("$"+ his.getTotal());


        final OrderedRealmCollection data = HistoryCRUD.orderedRealmCollectionProduct(realm,listid);
        adapter= new ListContentRecyclerViewAdapter(data, true, new ListContentRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                //Toast.makeText(view.getContext(),"Seleccion "+ position,Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapter);
    }
}
