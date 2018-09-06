package com.example.pcworld.inventorynew;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcworld.inventorynew.database.InventoryContract;

public class ProductDetails extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    TextView namep;
    TextView pricep;
    TextView quantityp;
    TextView sNamep;
    TextView sPhonep;
    Button increase1;
    Button increase2;
    Button decrease1;
    Button decrease2;
    Button delete;
    Button order;
    EditText number;
    Button editSameProduct;
    private Uri currentUri;
    public static int TempId;
    public static int flexibleQuantity = 0;
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private static boolean flagChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        currentUri = intent.getData();
        namep = (TextView) findViewById(R.id.textNameDetails);
        pricep = (TextView) findViewById(R.id.textPriceDetails);
        quantityp = (TextView) findViewById(R.id.textQuantityDetails);
        sNamep = (TextView) findViewById(R.id.textSupNameDetails);
        sPhonep = (TextView) findViewById(R.id.textSupPhoneDetails);
        number = (EditText) findViewById(R.id.number);
        increase1 = (Button) findViewById(R.id.increase1);
        increase2 = (Button) findViewById(R.id.increase2);
        decrease1 = (Button) findViewById(R.id.decrease1);
        decrease2 = (Button) findViewById(R.id.decrease2);
        order = (Button) findViewById(R.id.OrderDetails);
        delete = (Button) findViewById(R.id.deleteDetails);
        Button backToMain1 = (Button) findViewById(R.id.goList1);
        editSameProduct = (Button) findViewById(R.id.editSameProduct);
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        increase1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flexibleQuantity++;
                quantityp.setText(String.valueOf(flexibleQuantity));
                flagChange = true;
            }
        });
        decrease1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flexibleQuantity < 0) {
                    Toast.makeText(ProductDetails.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
                } else {
                    flexibleQuantity--;
                    quantityp.setText(String.valueOf(flexibleQuantity));
                    flagChange = true;
                }
            }
        });
        increase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText() != null) {
                    int x = Integer.valueOf(String.valueOf(number.getText()));
                    flexibleQuantity += x;
                    quantityp.setText(String.valueOf(flexibleQuantity));
                    flagChange = true;
                } else {
                    Toast.makeText(ProductDetails.this, "Enter quantity +VE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        decrease2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText() != null) {
                    int x = Integer.valueOf(String.valueOf(number.getText()));
                    flexibleQuantity = flexibleQuantity - x;
                    if (flexibleQuantity < 0) {
                        Toast.makeText(ProductDetails.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
                        flexibleQuantity = flexibleQuantity + x;
                    } else {
                        quantityp.setText(String.valueOf(flexibleQuantity));
                        flagChange = true;
                    }
                } else {
                    Toast.makeText(ProductDetails.this, "Enter quantity +VE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editSameProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagChange) {
                    updateQuantity();
                }
                Intent intent = new Intent(ProductDetails.this, EditProduct.class);
                intent.setData(currentUri);
                startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = (String) sPhonep.getText();
                String uri = "tel:" + call.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri1 = Uri.parse(uri);
                intent.setData(uri1);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        if (flagChange) {
            updateQuantity();
        }
        backToMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagChange) {
                    updateQuantity();
                }
                Intent intent = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateQuantity() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryData.QUANTITY, flexibleQuantity);
        if (currentUri != null) {
            int rowUpdate = getContentResolver().update(currentUri, contentValues, null, null);
            if (rowUpdate == 0) {
                Toast.makeText(this, "Quantity Update error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quantity Update Okay", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Alarm, are you sure ?!");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delete() {
        if (currentUri != null) {
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "error in delete try again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deleted ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryContract.InventoryData._ID, InventoryContract.InventoryData.PRODUCT_NAME
                , InventoryContract.InventoryData.PRICE, InventoryContract.InventoryData.QUANTITY
                , InventoryContract.InventoryData.SUPPLIER_NAME, InventoryContract.InventoryData.SUPPLIER_PHONE};

        return new CursorLoader(this,
                currentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryData._ID));
            int proName = cursor.getColumnIndex(InventoryContract.InventoryData.PRODUCT_NAME);
            int pri = cursor.getColumnIndex(InventoryContract.InventoryData.PRICE);
            int quan = cursor.getColumnIndex(InventoryContract.InventoryData.QUANTITY);
            int supName = cursor.getColumnIndex(InventoryContract.InventoryData.SUPPLIER_NAME);
            int supPhone = cursor.getColumnIndex(InventoryContract.InventoryData.SUPPLIER_PHONE);
            TempId = cursor.getInt(id);
            String name = cursor.getString(proName);
            int price = cursor.getInt(pri);
            int quantity = cursor.getInt(quan);
            String sName = cursor.getString(supName);
            String sPhone = cursor.getString(supPhone);
            namep.setText(name);
            quantityp.setText(String.valueOf(quantity));
            pricep.setText(String.valueOf(price));
            sNamep.setText(sName);
            sPhonep.setText(sPhone);
            flexibleQuantity = quantity;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        namep.setText("");
        pricep.setText("");
        quantityp.setText("");
        sNamep.setText("");
        sPhonep.setText("");
    }

}
