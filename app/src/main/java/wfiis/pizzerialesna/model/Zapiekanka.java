package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Zapiekanka{
    private String name;
    private double price;
    private int type;

    public Zapiekanka() {
    }

    public Zapiekanka(String name, double price, int type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
