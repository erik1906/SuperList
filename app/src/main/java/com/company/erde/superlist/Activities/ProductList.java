package com.company.erde.superlist.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.company.erde.superlist.Adapters.ListContentRecyclerViewAdapter;
import com.company.erde.superlist.Adapters.ProductRecyclerViewAdapter;
import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.ListProducts;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.RealModels.SuperList;
import com.company.erde.superlist.Realm.ListProductsCRUD;
import com.company.erde.superlist.Realm.ProductCRUD;
import com.company.erde.superlist.Realm.SuperListCRUD;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

import static android.content.ContentValues.TAG;

public class ProductList extends AppCompatActivity {

    private int listid;
    private NumberPicker numberPicker;
    private Button bSelect;
    private ImageButton bAdd;
    private TextView tvTotal;

    private int SELECT = 105;
    private Realm realm;

    private ListContentRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        realm = Realm.getDefaultInstance();

        listid = getIntent().getIntExtra("id",-1);

        final SuperList superList = SuperListCRUD.select(realm, listid);

        bSelect = findViewById(R.id.bSelect);
        bAdd = findViewById(R.id.ibAdd);
        tvTotal = findViewById(R.id.tvTotal);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("List: "+superList.getName());
        setSupportActionBar(toolbar);



        numberPicker = findViewById(R.id.npQuantity);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);



        numberPicker.setWrapSelectorWheel(true);

        recyclerView = findViewById(R.id.rvProductList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



        tvTotal.setText("$"+superList.getTotal());
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

            }
        });

        bSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SelectActivity.class);
                startActivityForResult(i, SELECT);
            }
        });

        final OrderedRealmCollection data = SuperListCRUD.orderedRealmCollectionProduct(realm,listid);
        adapter= new ListContentRecyclerViewAdapter(data, true, new ListContentRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                //Toast.makeText(view.getContext(),"Seleccion "+ position,Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapter);



        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product == null){
                    Snackbar.make(view, "Se necesita seleccionar un producto", Snackbar.LENGTH_LONG).show();
                }else {

                    ListProducts listProducts = new ListProducts();
                    listProducts.setListId(listid);
                    listProducts.setQuantity(numberPicker.getValue());
                    listProducts.setProduct(product);

                    SuperListCRUD.insertItem(realm, listProducts);

                    float total = superList.getTotal() + (product.getPrice() * numberPicker.getValue());

                    SuperListCRUD.updateTotal(realm, superList, total);

                    tvTotal.setText("$" + total);


                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SELECT) {
            if (resultCode == RESULT_OK) {
                int i =data.getIntExtra("id",-1);
                product = ProductCRUD.select(realm, i);
                bSelect.setText(product.getName());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.buyList:
                Snackbar.make(this.findViewById(android.R.id.content),"Toca buy",Snackbar.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;

        try {
            position = adapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        SuperList product = (SuperList) adapter.getData().get(position);
        switch (item.getItemId()) {
            case 0:
                realm = Realm.getDefaultInstance();
                SuperListCRUD.delete(realm, product.getId());
                //Toast.makeText(getContext(),position+" Delete",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

}
