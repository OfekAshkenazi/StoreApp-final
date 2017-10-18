package Entities;

import com.example.ofek.storeapp.R;

import java.util.Comparator;

/**
 * Created by Ofek on 01-Oct-117.
 */

public class Product {
    long id;
    String title;
    String description;
    double price;
    int type;

    public Product(String title, String description, double price, int type) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
    }

    public Product(long id, String title, String description, double price, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public int getTypeImageRes(){
        switch (type) {
            case 0:
                return R.mipmap.phone_icon;
            case 1:return R.mipmap.computer_icon;
            default:return R.mipmap.usb_photo;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (Double.compare(product.getPrice(), getPrice()) != 0) return false;
        if (getType() != product.getType()) return false;
        if (getTitle() != null ? !getTitle().equals(product.getTitle()) : product.getTitle() != null)
            return false;
        return getDescription() != null ? getDescription().equals(product.getDescription()) : product.getDescription() == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getType();
        return result;
    }

    @Override
    public String toString() {
        return title;
    }
}
class SortByType implements Comparator<Product>{

    @Override
    public int compare(Product product, Product t1) {
        if (product.getType()>t1.getType()){
            return 1;
        }
        if (product.getType()<t1.getType()){
            return -1;
        }
        return 0;
    }
}

