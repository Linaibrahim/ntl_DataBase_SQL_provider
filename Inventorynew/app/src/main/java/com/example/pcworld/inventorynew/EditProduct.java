package com.example.pcworld.inventorynew;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pcworld.inventorynew.database.InventoryContract;

public class EditProduct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText newName;
    EditText newPrice;
    EditText newQuantity;
    EditText newSName;
    EditText newsPhone;
    Button newData;
    Button backToMain;
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private boolean productChange = false;
    private Uri currentUri1;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productChange = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        newName = (EditText) findViewById(R.id.editNameEdit);
        newPrice = (EditText) findViewById(R.id.editPriceEdit);
        newQuantity = (EditText) findViewById(R.id.editQuantityEdit);
        newSName = (EditText) findViewById(R.id.editSupplierNameEdit);
        newsPhone = (EditText) findViewById(R.id.editSupplierPhoneEdit);
        newData = (Button) findViewById(R.id.changeProduct);
        backToMain = (Button) findViewById(R.id.goList);
        Intent intent = getIntent();
        currentUri1 = intent.getData();
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        newName.setOnTouchListener(touchListener);
        newPrice.setOnTouchListener(touchListener);
        newQuantity.setOnTouchListener(touchListener);
        newSName.setOnTouchListener(touchListener);
        newsPhone.setOnTouchListener(touchListener);

        newData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllData();
            }
        });
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProduct.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateAllData() {
        String tempName = String.valueOf(newName.getText());
        String tempPrice = String.valueOf(newPrice.getText());
        String tempQuantity = String.valueOf(newQuantity.getText());
        String tempSName = String.valueOf(newSName.getText());
        String tempSPhone = String.valueOf(newsPhone.getText());
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryData.PRODUCT_NAME, tempName);
        contentValues.put(InventoryContract.InventoryData.PRICE, tempPrice);
        contentValues.put(InventoryContract.InventoryData.QUANTITY, tempQuantity);
        contentValues.put(InventoryContract.InventoryData.SUPPLIER_NAME, tempSName);
        contentValues.put(InventoryContract.InventoryData.SUPPLIER_PHONE, tempSPhone);

        if (checkName(tempName) && checkPrice(tempPrice) && checkQuantity(tempQuantity) && checkSName(tempSName) && checkSphone(tempSPhone)) {
            int rowUpdate = getContentResolver().update(currentUri1, contentValues, null, null);
            if (rowUpdate == 0) {
                Toast.makeText(this, "Quantity Update error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quantity Update Okay", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProduct.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(EditProduct.this, "enter all fields", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkSphone(String sPhone) {
        if (sPhone.isEmpty()) {
            Toast.makeText(EditProduct.this, "enter phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkSName(String sName) {
        if (sName.isEmpty()) {
            Toast.makeText(EditProduct.this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPrice(String price) {

        if (price.isEmpty()) {
            Toast.makeText(EditProduct.this, "price should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }
        int P = Integer.valueOf(price);
        if (P < 0) {
            Toast.makeText(EditProduct.this, "price should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkQuantity(String quantity) {
        if (quantity.isEmpty()) {
            Toast.makeText(EditProduct.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }
        int q = Integer.valueOf(quantity);
        if (q < 0) {
            Toast.makeText(EditProduct.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkName(String name) {
        if (name.isEmpty()) {
            Toast.makeText(EditProduct.this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryData._ID, InventoryContract.InventoryData.PRODUCT_NAME
                , InventoryContract.InventoryData.PRICE, InventoryContract.InventoryData.QUANTITY
                , InventoryContract.InventoryData.SUPPLIER_NAME, InventoryContract.InventoryData.SUPPLIER_PHONE};

        return new CursorLoader(this,
                currentUri1,
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

            String name = cursor.getString(proName);
            int price = cursor.getInt(pri);
            int quantity = cursor.getInt(quan);
            String sName = cursor.getString(supName);
            String sPhone = cursor.getString(supPhone);
            newName.setText(name);
            newPrice.setText(String.valueOf(price));
            newQuantity.setText(String.valueOf(quantity));
            newSName.setText(sName);
            newsPhone.setText(sPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        newName.setText("");
        newPrice.setText("");
        newQuantity.setText("");
        newSName.setText("");
        newsPhone.setText("");
    }
}
