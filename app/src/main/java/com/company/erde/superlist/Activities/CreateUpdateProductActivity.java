package com.company.erde.superlist.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class CreateUpdateProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ImageButton ibPicture;
    private EditText etName;
    private EditText etPrice;
    private boolean create;
    private Product product;
    private int idproduct;
    private String uri;


    private static final String FILE_PROVIDER = "com.company.erde.superlist.fileprovider";

    private static  final int REQUEST_INTERNAL_STORAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 100;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_product);

        ibPicture = findViewById(R.id.ibPicture);
        etName = findViewById(R.id.tvProductName);
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
                Picasso.with(this).load(R.drawable.no_img).into(ibPicture);
            }else{
                Picasso.with(this).load(product.getPhotoUrl()).into(ibPicture);
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
            public void onClick(final View view) {
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(CreateUpdateProductActivity.this);
                String[] options = {"Take photo", "Choose photo"};
                builder.setTitle("Change photo")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        if(checkPermission(REQUEST_IMAGE_CAPTURE)){
                                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                File photoFile = null;
                                                try {
                                                    photoFile = createImageFile();
                                                } catch (IOException ex) {
                                                    // Error creando archivo
                                                }
                                                // Si salió bien
                                                if (photoFile != null) {
                                                    Uri photoURI = FileProvider.getUriForFile(CreateUpdateProductActivity.this, FILE_PROVIDER, photoFile);
                                                    // Mandamos llamar el intent
                                                    uri = photoURI.toString();
                                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                }
                                            }
                                        }else{
                                            requestPermissions(REQUEST_IMAGE_CAPTURE);
                                        }
                                        break;
                                    case 1:
                                        if(checkPermission(REQUEST_INTERNAL_STORAGE)){
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            startActivityForResult(Intent.createChooser(intent, "Select"), REQUEST_INTERNAL_STORAGE);
                                        }else{
                                            requestPermissions(REQUEST_INTERNAL_STORAGE);
                                        }
                                        break;
                                }
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
                    product.setPhotoUrl(uri);

                    ProductCRUD.insert(realm, product);
                }else {
                    Product product = new Product();
                    product.setPrice(Float.valueOf(etPrice.getText().toString()));
                    product.setName(etName.getText().toString());
                    product.setPhotoUrl(uri);
                    product.setId(idproduct);
                    ProductCRUD.update(realm, product);
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                Picasso.with(this).load(uri).into(ibPicture);
                break;

            case REQUEST_INTERNAL_STORAGE:
                uri = data.getData().toString();
                if (null != uri) {
                    Picasso.with(this).load(uri).into(ibPicture);
                }
                break;
        }

    }

    private boolean checkPermission(int requestCode) {
        switch (requestCode){
            case REQUEST_INTERNAL_STORAGE:
                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

                return permission == PackageManager.PERMISSION_GRANTED;

            case REQUEST_IMAGE_CAPTURE:

                int permissionCamera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

                return permissionCamera == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }



    private void requestPermissions(int requestCode) {
        switch (requestCode){
            case REQUEST_INTERNAL_STORAGE:
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this,"No se dieron permisos",Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_INTERNAL_STORAGE);
                }

            case REQUEST_IMAGE_CAPTURE:

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    Toast.makeText(this,"No se dieron permisos",Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
                }
        }

    }
    private File createImageFile() throws IOException {
        // Creamos el archivo
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                nombreImagen,  /* prefijp */
                ".jpg",         /* sufijo */
                storageDir      /* directorio */
        );

        // Obtenemos la URL
        String urlName = "file://" + image.getAbsolutePath();
        return image;
    }


}
