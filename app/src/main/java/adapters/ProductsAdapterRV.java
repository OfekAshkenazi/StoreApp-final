package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofek.storeapp.MainActivity;
import com.example.ofek.storeapp.ManagerActivity;
import com.example.ofek.storeapp.R;

import java.util.ArrayList;

import Constants.Constants;
import Entities.Product;
import SQLiteHelper.DAL;

/**
 * Created by Ofek on 03-Oct-17.
 */

public class ProductsAdapterRV extends RecyclerView.Adapter<ProductVH> {
    Context context;
    ArrayList<Product> products;
    public ProductsAdapterRV(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.product_card_layout,parent,false);
        return new ProductVH(view,this);

    }

    @Override
    public void onBindViewHolder(ProductVH holder, final int position) {
        Product product=products.get(position);
        holder.title.setText(product.getTitle());
        holder.price.setText(""+product.getPrice());
        holder.type.setImageResource(product.getTypeImageRes());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public void notifyProductsChanged(ArrayList<Product> products){
        this.products=products;
        notifyDataSetChanged();
    }
}
class ProductVH extends RecyclerView.ViewHolder implements View.OnLongClickListener{
    private ArrayList<Product> products;
    ProductsAdapterRV adapter;
    TextView title,price;
    ImageView cart,type;
    View itemView;
    public ProductVH(View view, final ProductsAdapterRV adapter) {
        super(view);
        this.adapter=adapter;
        this.products=adapter.products;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isManager==false)return;
                Product product=products.get(getAdapterPosition());
                Intent intent = new Intent(view.getContext(), ManagerActivity.class);
                intent.putExtra(Constants.MODE_STRING, 1);
                intent.putExtra(Constants.ID_STRING, product.getId());
                view.getContext().startActivity(intent);
            }
        });
        view.setOnLongClickListener(this);
        View itemView=view;
        title=itemView.findViewById(R.id.titleTV_adapter);
        cart=itemView.findViewById(R.id.cartIV_adapter);
        type=itemView.findViewById(R.id.imageType_adapter);
        price=itemView.findViewById(R.id.priceTV_adapter);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product=products.get(getAdapterPosition());
                DAL dal=new DAL(adapter.context);
                dal.addToCart(product.getId());
            }
        });
    }



    @Override
    public boolean onLongClick(View view) {
        if (MainActivity.isManager==false)return true;
        AlertDialog dialog=getDelAlertDialog(view.getContext(),getAdapterPosition());
        dialog.show();
        return true;
    }
    private AlertDialog getDelAlertDialog(final Context context, final int adapterPosition){
        AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.setMessage("Are you sure you would like to delete this product?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (MainActivity.isManager==false)return;
                deleteProduct(context,adapterPosition);
                products.remove(adapterPosition);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return dialog;
    }
    private void deleteProduct(Context context, int adapterPosition){
        DAL dal=new DAL(context);
        if (products.isEmpty()){
            Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Product product=products.get(adapterPosition);
        try {
            dal.deleteProduct(product.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
