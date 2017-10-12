package com.company.erde.superlist.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.Realm.ProductCRUD;

import io.realm.Realm;

public class ProductList extends AppCompatActivity {

    private int listid;
    private NumberPicker numberPicker;
    private Button bSelect;
    private int SELECT = 105;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        bSelect = findViewById(R.id.bSelect);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        numberPicker = findViewById(R.id.npQuantity);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);

        numberPicker.setWrapSelectorWheel(true);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker

            }
        });

        bSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SelectActivity.class);
                startActivityForResult(i, SELECT);
            }
        });

        listid = getIntent().getIntExtra("id",-1);
        Toast.makeText(this,"id: "+ listid,Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SELECT) {
            if (resultCode == RESULT_OK) {
                int i =data.getIntExtra("id",-1);
                Product product = ProductCRUD.select(realm, i);
                bSelect.setText(product.getName());
            }
        }
    }
}
