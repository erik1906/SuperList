package com.company.erde.superlist.Activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.company.erde.superlist.R;

public class ProductList extends AppCompatActivity {

    private int listid;
    private NumberPicker numberPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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

        listid = getIntent().getIntExtra("id",-1);
        Toast.makeText(this,"id: "+ listid,Toast.LENGTH_LONG).show();
    }
}
