package com.example.pcworld.inventorynew;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcworld.inventorynew.database.InventoryContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PRODUCT_LOADER_ID = 0;
    private InventoryCursorAdapter mCursorAdapter;
    public static long tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView1 = (ListView) findViewById(R.id.list1);
        View Test0 = findViewById(R.id.test0);
        TextView error = (TextView) findViewById(R.id.error);
        listView1.setEmptyView(error);
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        listView1.setAdapter(mCursorAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tempID = id;
                Log.i(MainActivity.class.getSimpleName(), "insertDataeeeeeeee " + position);
                Toast.makeText(MainActivity.this, "list was clicked  ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProductDetails.class);
                Uri currentUriSend = ContentUris.withAppendedId(InventoryContract.InventoryData.CONTENT_URI, id);
                intent.setData(currentUriSend);
                startActivity(intent);
            }
        });

        ImageView add_product = (ImageView) findViewById(R.id.add_button);
        Button insert_product = (Button) findViewById(R.id.insert22);
        insert_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNew();
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);
    }

    private void insertNew() {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryData.PRODUCT_NAME, "car");
        values.put(InventoryContract.InventoryData.PRICE, 300000);
        values.put(InventoryContract.InventoryData.QUANTITY, 5);
        values.put(InventoryContract.InventoryData.SUPPLIER_NAME, "sai");
        values.put(InventoryContract.InventoryData.SUPPLIER_PHONE, "0300");
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryData.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryData._ID, InventoryContract.InventoryData.PRODUCT_NAME
                , InventoryContract.InventoryData.PRICE, InventoryContract.InventoryData.QUANTITY
                , InventoryContract.InventoryData.SUPPLIER_NAME, InventoryContract.InventoryData.SUPPLIER_PHONE};

        return new CursorLoader(this,
                InventoryContract.InventoryData.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
