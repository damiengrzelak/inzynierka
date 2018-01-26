package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Salatka{
    private String name;
    private String ingredients;
    private double price;
    private int type;

    public Salatka() {
    }

    public Salatka(String name, String ingredients, double price, int type) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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
