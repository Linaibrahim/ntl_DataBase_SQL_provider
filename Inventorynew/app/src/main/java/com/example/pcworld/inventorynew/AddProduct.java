package com.example.pcworld.inventorynew;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pcworld.inventorynew.database.InventoryContract;

public class AddProduct extends AppCompatActivity {
    static EditText name;
    static EditText price;
    static EditText quantity;
    static EditText sName;
    static EditText sPhone;
    static String Name;
    static String Price;
    static String Quantity;
    static String SName;
    static String SPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        name = (EditText) findViewById(R.id.editNameEdit);
        price = (EditText) findViewById(R.id.editPriceEdit);
        quantity = (EditText) findViewById(R.id.editQuantityEdit);
        sName = (EditText) findViewById(R.id.editSupplierNameEdit);
        sPhone = (EditText) findViewById(R.id.editSupplierPhoneAdd);
        Button add = (Button) findViewById(R.id.AddProduct);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = String.valueOf(name.getText());
                Price = String.valueOf(price.getText());
                Quantity = String.valueOf(quantity.getText());
                SName = String.valueOf(sName.getText());
                SPhone = String.valueOf(sPhone.getText());

                if (checkPrice(Price) && checkName(Name) && checkQuantity(Quantity) && checkSName(SName) && checkSphone(SPhone)) {
                    insertTODataBase();
                    Toast.makeText(AddProduct.this, "Product add", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProduct.this, MainActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(AddProduct.this, "enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkSphone(String sPhone) {
        if (sPhone.isEmpty()) {
            Toast.makeText(AddProduct.this, "enter phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkSName(String sName) {
        if (sName.isEmpty()) {
            Toast.makeText(AddProduct.this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPrice(String price) {

        if (price.isEmpty()) {
            Toast.makeText(AddProduct.this, "price should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }
        int P = Integer.valueOf(price);
        if (P < 0) {
            Toast.makeText(AddProduct.this, "price should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkQuantity(String quantity) {
        if (quantity.isEmpty()) {
            Toast.makeText(AddProduct.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }
        int q = Integer.valueOf(quantity);
        if (q < 0) {
            Toast.makeText(AddProduct.this, "quantity should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkName(String name) {
        if (name.isEmpty()) {
            Toast.makeText(AddProduct.this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void insertTODataBase() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryData.PRODUCT_NAME, Name);
        contentValues.put(InventoryContract.InventoryData.PRICE, Price);
        contentValues.put(InventoryContract.InventoryData.QUANTITY, Quantity);
        contentValues.put(InventoryContract.InventoryData.SUPPLIER_NAME, SName);
        contentValues.put(InventoryContract.InventoryData.SUPPLIER_PHONE, SPhone);
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryData.CONTENT_URI, contentValues);
        if (newUri == null) {
            Toast.makeText(this, "Insert Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insert Done", Toast.LENGTH_SHORT).show();
        }

    }
}
