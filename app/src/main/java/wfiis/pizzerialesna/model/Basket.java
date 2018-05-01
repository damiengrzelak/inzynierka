package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;


@IgnoreExtraProperties
public class Basket {
    private String name;
    private ArrayList<String> ingredients;
    private double prize;
    private double priceIngredients;
    private int number;
    private Boolean isPizza;
    private String size;
    private String meat;
    private int type;
    private String key;

    public Basket() {
    }

    public Basket(String name, ArrayList<String> ingredients, double prize, double priceIngredients, int number, Boolean isPizza, String size, String meat, int type, String key) {
        this.name = name;
        this.ingredients = ingredients;
        this.prize = prize;
        this.priceIngredients = priceIngredients;
        this.number = number;
        this.isPizza = isPizza;
        this.size = size;
        this.meat = meat;
        this.type = type;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public double getPriceIngredients() {
        return priceIngredients;
    }

    public void setPriceIngredients(double priceIngredients) {
        this.priceIngredients = priceIngredients;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Boolean getIsPizza() {
        return isPizza;
    }

    public void setIsPizza(Boolean isPizza) {
        this.isPizza = isPizza;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
