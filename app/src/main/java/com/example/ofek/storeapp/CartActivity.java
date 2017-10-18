package com.example.ofek.storeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import SQLiteHelper.DAL;

public class CartActivity extends AppCompatActivity {
    ListView listView;
    CartListAdapter adapter;
    DAL db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db=new DAL(this);
        listView= (ListView) findViewById(R.id.cartLV);
        adapter=new CartListAdapter(this);
        listView.setAdapter(adapter);
    }
}

class CartListAdapter extends BaseAdapter{
    Context context;
    ArrayList<CartItem> items;

    public CartListAdapter(Context context) {
        this.context = context;
        this.items = initialCartItems();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private ArrayList<CartItem> initialCartItems() {
        DAL db=new DAL(context);
        return db.getAllProductsInCart();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final CartItem item=items.get(i);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view1=inflater.inflate(R.layout.product_in_cart_layout,viewGroup,false);
        TextView title=view1.findViewById(R.id.titleTV_cartA);
        TextView price=view1.findViewById(R.id.priceTV_cartA);
        final TextView count=view1.findViewById(R.id.countTV_cartA);
        TextView finalPrice=view1.findViewById(R.id.finalPriceTV_cartA);
        ImageButton delete=view1.findViewById(R.id.deleteBtn_cartA);
        title.setText(item.getTitle());
        price.setText(""+item.getPrice());
        count.setText(""+item.getCount());
        finalPrice.setText(""+item.getTotalPrice());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DAL dal=new DAL(context);
                dal.removeProductFromCart(item.getId());
                items=initialCartItems();
                notifyDataSetChanged();
            }
        });
        return view1;
    }
}
