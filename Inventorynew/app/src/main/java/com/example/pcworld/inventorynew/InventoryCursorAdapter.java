package com.example.pcworld.inventorynew;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcworld.inventorynew.database.InventoryContract;


public class InventoryCursorAdapter extends CursorAdapter {
    public static int Quantity;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.itme_design, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView pName1 = (TextView) view.findViewById(R.id.name1);
        TextView price1 = (TextView) view.findViewById(R.id.price1);
        final TextView quantity1 = (TextView) view.findViewById(R.id.quantity1);
        Button sale = (Button) view.findViewById(R.id.sale_main);
        final int id = cursor.getColumnIndex(InventoryContract.InventoryData._ID);
        int proName = cursor.getColumnIndex(InventoryContract.InventoryData.PRODUCT_NAME);
        int pri = cursor.getColumnIndex(InventoryContract.InventoryData.PRICE);
        int quan = cursor.getColumnIndex(InventoryContract.InventoryData.QUANTITY);

        final int productID = cursor.getInt(id);
        String productName = cursor.getString(proName);
        int productPrice = cursor.getInt(pri);
        int productQuantity = cursor.getInt(quan);
        Quantity = productQuantity;
        pName1.setText("name: ");
        pName1.append(productName);
        price1.setText("Price: ");
        price1.append(String.valueOf(productPrice));
        quantity1.setText("Quantity: ");
        quantity1.append(String.valueOf(productQuantity));

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quantity--;
                if (checkQuantityToSale(productID, context)) {
                    quantity1.setText(String.valueOf(Quantity));
                } else {
                    Quantity++;
                }
            }
        });
    }

    private boolean checkQuantityToSale(int ID, Context context) {
        Uri UriSend = ContentUris.withAppendedId(InventoryContract.InventoryData.CONTENT_URI, ID);
        if (Quantity <= 0) {
            Toast.makeText(context.getApplicationContext(), "quantity should be +VE", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventoryContract.InventoryData.QUANTITY, Quantity);
            if (UriSend != null) {
                int rowUpdate = context.getContentResolver().update(UriSend, contentValues, null, null);
                if (rowUpdate == 0) {
                    Toast.makeText(context.getApplicationContext(), "Quantity Update error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Quantity Update Okay", Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(context.getApplicationContext(), "you Sale it", Toast.LENGTH_SHORT).show();
            return true;
        }


    }
}
