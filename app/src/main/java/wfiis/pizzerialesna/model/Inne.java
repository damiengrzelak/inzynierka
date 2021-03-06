package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Inne {
    private String ingredients;
    private String name;
    private int number;
    private double price;
    private int type;

    public Inne() {
    }

    public Inne(String ingredients, String name, int number, double price, int type) {
        this.ingredients = ingredients;
        this.name = name;
        this.number = number;
        this.price = price;
        this.type = type;
    }

    public String getIngredients() { return ingredients; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
