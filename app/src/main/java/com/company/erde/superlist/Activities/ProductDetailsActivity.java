package com.company.erde.superlist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.Realm.ProductCRUD;

import io.realm.Realm;

public class ProductDetailsActivity extends AppCompatActivity {

    Realm realm;

    private TextView tvName;
    private TextView tvPrice;
    private ImageView ivPicture;
    private Button bDelete;
    private int productid;

    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        productid = getIntent().getIntExtra("id",-1);

        realm = Realm.getDefaultInstance();
        product = ProductCRUD.select(realm, productid);

        tvName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        bDelete = findViewById(R.id.bDelete);

        tvName.setText(product.getName());
        tvPrice.setText(String.valueOf(product.getPrice()));


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateUpdateProductActivity.class);
                i.putExtra("id",getIntent().getIntExtra("id",-1) );
                startActivity(i);

            }
        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete confirm.");

                builder.setMessage("¿Esta sguro que desea eliminar este producto?")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ProductCRUD.delete(realm, productid);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        tvName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        bDelete = findViewById(R.id.bDelete);

        tvName.setText(product.getName());
        tvPrice.setText(String.valueOf(product.getPrice()));
    }
}
