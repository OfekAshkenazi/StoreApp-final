package com.example.ofek.storeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import Constants.Constants;
import Entities.Product;
import SQLiteHelper.DAL;

public class ManagerActivity extends AppCompatActivity {
    EditText titleET,priceET,descET;
    Spinner typeSpinner;
    Button addBtn,saveBtn;
    DAL db;
    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        db=new DAL(this);
        setSpinner();
        setViews();
        setMode();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleET.getText().toString().equals("")||priceET.getText().toString().equals("")){
                    Toast.makeText(ManagerActivity.this, "please make sure you entered all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Product product=new Product(titleET.getText().toString(),descET.getText().toString(),Double.parseDouble(priceET.getText().toString()),typeSpinner.getSelectedItemPosition());
                db.insertProduct(product);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode==Constants.ADD_MODE){
            addBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
        }
        if (mode==Constants.EDIT_MODE){
            saveBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().putExtra(Constants.MODE_STRING,Constants.ADD_MODE);
    }

    private void setMode() {
        mode=getIntent().getExtras().getInt(Constants.MODE_STRING,Constants.ADD_MODE);
        if (mode==Constants.ADD_MODE)return;
        long pID=getIntent().getExtras().getLong(Constants.ID_STRING,-1);
        final Product product=db.getProductByID(pID);
        if (mode==Constants.EDIT_MODE){
            saveBtn.setVisibility(View.VISIBLE);
            titleET.setText(product.getTitle());
            descET.setText(product.getDescription());
            priceET.setText(""+product.getPrice());
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setTitle(titleET.getText().toString());
                product.setDescription(descET.getText().toString());
                product.setPrice(Double.parseDouble(priceET.getText().toString()));
                product.setType(typeSpinner.getSelectedItemPosition());
                boolean flg=db.updateProduct(product);
                if (!flg)
                    Toast.makeText(ManagerActivity.this,"failed" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        typeSpinner.setSelection(product.getType());
    }

    private void setViews() {
        saveBtn= (Button) findViewById(R.id.saveBtn);
        titleET= (EditText) findViewById(R.id.titleET);
        priceET= (EditText) findViewById(R.id.priceET);
        descET= (EditText) findViewById(R.id.descET);
        addBtn=(Button)findViewById(R.id.addBtn);
    }

    private void setSpinner() {
        typeSpinner= (Spinner) findViewById(R.id.typeSpinner);
        String[] types={"Cellular","Computers","Else"};
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,types);
        typeSpinner.setAdapter(adapter1);
    }
}
