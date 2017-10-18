package SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ofek.storeapp.CartItem;

import java.util.ArrayList;
import java.util.HashMap;

import Entities.Product;

import static SQLiteHelper.DAL.CartEntry.CART_COL1;
import static SQLiteHelper.DAL.CartEntry.CART_COL2;
import static SQLiteHelper.DAL.CartEntry.CART_COL3;
import static SQLiteHelper.DAL.CartEntry.CART_TABLE_NAME;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_COL1;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_COL2;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_COL3;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_COL4;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_COL5;
import static SQLiteHelper.DAL.StoreEntry.PRODUCTS_TABLE_NAME;

/**
 * Created by Ofek on 04-Oct-17.
 */
public class DAL {
    Context context;
    MySQLiteHelper instance;

    public DAL(Context context) {
        this.context = context;
        instance=new MySQLiteHelper(context);
    }

    public void insertProduct(Product product){
        SQLiteDatabase db=instance.getWritableDatabase();
        ContentValues values=putValues(product);
        long insertResult=db.insert(PRODUCTS_TABLE_NAME,null,values);
        product.setId(insertResult);
    }
    public static final int CELL=1;
    public static final int COMPUTERS=2;
    public static final int ELSE=3;
    public static final int ALL=0;
    public ArrayList<Product> getAllProducts(int whichType){
        ArrayList<Product> products=new ArrayList<>();
        SQLiteDatabase db=instance.getReadableDatabase();
        Cursor cursor;
        switch (whichType){
            case CELL:cursor=db.query(false,PRODUCTS_TABLE_NAME,null,PRODUCTS_COL5.substring(1,PRODUCTS_COL5.length()-1)+"=0",null,null,null,null,null);
                break;
            case COMPUTERS:cursor=db.query(false,PRODUCTS_TABLE_NAME,null,PRODUCTS_COL5.substring(1,PRODUCTS_COL5.length()-1)+"=1",null,null,null,null,null);
                break;
            case ELSE:cursor=db.query(false,PRODUCTS_TABLE_NAME,null,PRODUCTS_COL5.substring(1,PRODUCTS_COL5.length()-1)+"=2",null,null,null,null,null);
                break;
            default: ALL:cursor=db.query(false,PRODUCTS_TABLE_NAME,null,null,null,null,null,null,null);
                break;
        }
        if (cursor.getCount()==0){
            return products;
        }
        while (cursor.moveToNext()){
            long id;
            String title;
            String description;
            double price;
            int type;
            id=cursor.getLong(cursor.getColumnIndex(PRODUCTS_COL1.substring(1,PRODUCTS_COL1.length()-1)));
            title=cursor.getString(cursor.getColumnIndex(PRODUCTS_COL2.substring(1,PRODUCTS_COL2.length()-1)));
            description=cursor.getString(cursor.getColumnIndex(PRODUCTS_COL3.substring(1,PRODUCTS_COL3.length()-1)));
            price=Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COL4.substring(1,PRODUCTS_COL4.length()-1))));
            type=cursor.getInt(cursor.getColumnIndex(PRODUCTS_COL5.substring(1,PRODUCTS_COL5.length()-1)));
            products.add(new Product(id,title,description,price,type));
        }
        return products;

    }
    public Product getProductByID(long id){
        SQLiteDatabase db=instance.getReadableDatabase();
        Cursor cursor;
        cursor=db.query(false,PRODUCTS_TABLE_NAME,null,PRODUCTS_COL1.substring(1,PRODUCTS_COL1.length()-1)+"="+id,null,null,null,null,null);
        cursor.moveToFirst();
        String title;
        String description;
        double price;
        int type;
        title=cursor.getString(cursor.getColumnIndex(PRODUCTS_COL2.substring(1,PRODUCTS_COL2.length()-1)));
        description=cursor.getString(cursor.getColumnIndex(PRODUCTS_COL3.substring(1,PRODUCTS_COL3.length()-1)));
        price=Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COL4.substring(1,PRODUCTS_COL4.length()-1))));
        type=cursor.getInt(cursor.getColumnIndex(PRODUCTS_COL5.substring(1,PRODUCTS_COL5.length()-1)));
        return new Product(id,title,description,price,type);
    }

    public boolean updateProduct(Product product){
        ContentValues values=putValues(product);
        SQLiteDatabase db=instance.getWritableDatabase();
        try{
            db.update(PRODUCTS_TABLE_NAME,values,PRODUCTS_COL1+"= "+product.getId(),null);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void updateAllProducts(ArrayList<Product> products){
        SQLiteDatabase read=instance.getReadableDatabase();
        for (Product product: products) {
            updateProduct(product);
        }
    }
    public void deleteProduct(long pID) throws Exception {
        SQLiteDatabase db=instance.getReadableDatabase();
        if (db.delete(PRODUCTS_TABLE_NAME,PRODUCTS_COL1+"= "+pID,null)==0){
            throw new Exception("delete not succeeded");
        }
    }
    public ContentValues putValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(PRODUCTS_COL2, product.getTitle());
        values.put(PRODUCTS_COL3, product.getDescription());
        values.put(PRODUCTS_COL4, product.getPrice());
        values.put(PRODUCTS_COL5, product.getType());
        return values;
    }



    // CART--------------------------------------
    public long addToCart(long pID){
        SQLiteDatabase db=instance.getWritableDatabase();
        ContentValues values=new ContentValues();
        int count=getCartCount(pID);
        values.put(CART_COL2,pID);
        values.put(CART_COL3,++count);
        if ((count-1)!=0){
            db.update(CART_TABLE_NAME,values,CART_COL2+" = "+pID,null);
            return -111;
        }
        return db.insert(CART_TABLE_NAME,null,values);
    }

    private Integer getCartCount(long pID) {
        HashMap<Long,Integer> map=getCartProductsCount();
        if (!map.containsKey(pID))return 0;
        return  map.get(pID);
    }

    public int removeProductFromCart(long pID,int currentCount) {
        SQLiteDatabase db = instance.getReadableDatabase();
        ContentValues values = new ContentValues();
        int count=currentCount-1;
        if (currentCount > 1) {
            values.put(CART_COL1,pID);
            values.put(CART_COL3, count);
            int result=db.update(CART_TABLE_NAME, values, CART_COL1 + " = " + pID, null);
            Log.v("update status","result: "+result);
            return result;
        } else {
            return db.delete(CART_TABLE_NAME, CART_COL1 + "= " + pID, null);
        }
    }

    private HashMap<Long,Integer> getCartProductsCount(){
        SQLiteDatabase db=instance.getReadableDatabase();
        HashMap<Long,Integer> cart=new HashMap<>();
        Cursor cursor=db.query(false,CART_TABLE_NAME,null,null,null,null,null,null,null);
        if (!cursor.moveToFirst()){
            return cart;
        }
        do {
            cart.put(cursor.getLong(cursor.getColumnIndex(CART_COL2)),cursor.getInt(cursor.getColumnIndex(CART_COL3)));
        }while (cursor.moveToNext());
        return cart;
    }
    public ArrayList<CartItem> getAllProductsInCart(){
        SQLiteDatabase db=instance.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + PRODUCTS_TABLE_NAME+"a" + " INNER JOIN " + CART_TABLE_NAME+" b"
                + " ON " +"a._id" + " = b." + CART_COL2;
        Cursor cursor=db.rawQuery(rawQuery,null);
        ArrayList<CartItem> items=new ArrayList<>();
        if (!cursor.moveToFirst())return items;
        do {
            int count=cursor.getInt(cursor.getColumnIndex(CART_COL3));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            double price=cursor.getDouble(cursor.getColumnIndex("price"));
            CartItem item = new CartItem(title, price, count);
            item.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            items.add(item);
        }while (cursor.moveToNext());

        return items;
    }

class StoreEntry {
    protected static final String PRODUCTS_TABLE_NAME=" Products ";
    protected static final String PRODUCTS_COL1=" _id ";
    protected static final String PRODUCTS_COL2=" title ";
    protected static final String PRODUCTS_COL3=" description ";
    protected static final String PRODUCTS_COL4=" price ";
    protected static final String PRODUCTS_COL5=" type ";

    protected static final String CREATE_PRODUCTS_TABLE_QUERY="CREATE TABLE"+PRODUCTS_TABLE_NAME+" ("
            +PRODUCTS_COL1+"INTEGER PRIMARY KEY AUTOINCREMENT,"
            +PRODUCTS_COL2+"TEXT,"
            +PRODUCTS_COL3+"TEXT,"
            +PRODUCTS_COL4+"REAL,"
            +PRODUCTS_COL5+"INTEGER)";
}
class CartEntry{
    protected static final String CART_TABLE_NAME="CART";
    protected static final String CART_COL1="_id";
    protected static final String CART_COL2="pID";
    protected static final String CART_COL3="Count";

    protected static final String CREATE_CART_TABLE_QUERY ="CREATE TABLE "+CART_TABLE_NAME+" ("
            +CART_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CART_COL2+" INTEGER, "
            +CART_COL3+" INTEGER, "
            +"FOREIGN KEY("+CART_COL2+") REFERENCES "+PRODUCTS_TABLE_NAME+"("+PRODUCTS_COL1+"))";
}

class MySQLiteHelper extends SQLiteOpenHelper {
    Context context;
    protected static final String DB_NAME = "StoreDatabase";
    protected static final int DB_VER = 2;

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StoreEntry.CREATE_PRODUCTS_TABLE_QUERY);
        db.execSQL(CartEntry.CREATE_CART_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + PRODUCTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE_NAME);
        onCreate(db);
    }
}
}
