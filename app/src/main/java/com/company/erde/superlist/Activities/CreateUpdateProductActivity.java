package com.company.erde.superlist.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.Realm.ProductCRUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;

public class CreateUpdateProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ImageButton ibPicture;
    private EditText etName;
    private EditText etPrice;
    private boolean create;
    private Product product;
    private int idproduct;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_product);

        ibPicture = findViewById(R.id.ibPicture);
        etName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etPrice);

        realm = Realm.getDefaultInstance();

        toolbar = findViewById(R.id.toolbar);



        int id = getIntent().getIntExtra("id",-1);
        if(id == -1) {
            toolbar.setTitle("Agregar nuevo producto");
            create=true;
        }else{
            create=false;
            toolbar.setTitle("Modificar producto");
            idproduct = id;
            product = ProductCRUD.select(realm, id);
            etPrice.setText( String.valueOf(product.getPrice()));
            etName.setText( product.getName());
            if(product.getPhotoUrl().equals("")){

            }else{
                Picasso.with(this).load(R.drawable.no_img).into(ibPicture);
            }
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(CreateUpdateProductActivity.this);
                String[] options = {"Take photo", "Choose photo"};
                builder.setTitle("Change photo")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Click aqui", Toast.LENGTH_SHORT).show();
                                //TODO: agregar funcion camara y galleria
                            }
                        });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.doneProduct:
                if(create) {
                    //Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                    Product product = new Product();
                    product.setPrice(Float.valueOf(etPrice.getText().toString()));
                    product.setName(etName.getText().toString());
                    product.setPhotoUrl("");

                    ProductCRUD.insert(realm, product);
                }else {
                    Product product = new Product();
                    product.setPrice(Float.valueOf(etPrice.getText().toString()));
                    product.setName(etName.getText().toString());
                    product.setPhotoUrl("");
                    product.setId(idproduct);
                    ProductCRUD.update(realm, product);
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
