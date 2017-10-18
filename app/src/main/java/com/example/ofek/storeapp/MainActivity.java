package com.example.ofek.storeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import Constants.Constants;
import Entities.Product;
import SQLiteHelper.DAL;
import adapters.ProductsAdapterRV;

public class MainActivity extends AppCompatActivity{
    private static final int MANAGER_REQUEST = 111;
    static ArrayList<Product> products;
    DAL db;
    RecyclerView list;
    SharedPreferences preferences;
    public static boolean isManager=false;
    Toolbar toolbar;
    ImageButton checkoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DAL(this);
        initProducts();
        setList();
        setAdapter();
        setToolbar();
        preferences=getSharedPreferences("settings",MODE_PRIVATE);
        preferences.edit().putString(Constants.MANAGER_ID_TAG,Constants.MANAGER_ID).commit();
        preferences.edit().putString(Constants.MANAGER_PASS_TAG,Constants.MANAGER_PASS).commit();
        setCheckoutBtn();
    }

    private void setCheckoutBtn() {
        checkoutBtn= (ImageButton) findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),CartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initProducts();
        setAdapter();

    }

    private void setAdapter() {
        ProductsAdapterRV adapterRV=new ProductsAdapterRV(this,products);
        list.setAdapter(adapterRV);
    }

    private void setList() {
        list= (RecyclerView) findViewById(R.id.listView);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initProducts() {
        products=db.getAllProducts(DAL.ALL);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spinner spinner=toolbar.findViewById(R.id.sortSpinner);
        final String[] sortBy={"All","Cellular","Computers","Else"};
        final ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,sortBy);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                products=db.getAllProducts(i);
                setAdapter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(4);
            }
        });
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.menuItem){
                    if (MainActivity.isManager==false){
                        Intent intent=new Intent(MainActivity.this,ManagerAuth.class);
                        startActivityForResult(intent,1);
                        return false;
                    }
                    Intent intent=new Intent(MainActivity.this,ManagerActivity.class);
                    intent.putExtra(Constants.MODE_STRING,Constants.ADD_MODE);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK){
            isManager=true;
            Menu menu=toolbar.getMenu();
            menu.getItem(0).setTitle("Add Product");
        }
    }
}
