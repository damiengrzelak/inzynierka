package wfiis.pizzerialesna.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Pizza {

    private String name;
    private String ingredients;
    private int number;
    private double price28;
    private double price34;
    private double price44;
    private int type;


    public Pizza(){

    }

    public Pizza(String name, String ingredients, int number, double price28, double price34, double price44, int type) {
        this.name = name;
        this.ingredients = ingredients;
        this.number = number;
        this.price28 = price28;
        this.price34 = price34;
        this.price44 = price44;
        this.type = type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("ingredients", ingredients);
        result.put("price28", price28);
        result.put("price34", price34);
        result.put("price44", price44);
        result.put("type", type);

        return result;
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

    public double getPrice28() {
        return price28;
    }

    public void setPrice28(double price28) {
        this.price28 = price28;
    }

    public double getPrice34() {
        return price34;
    }

    public void setPrice34(double price34) {
        this.price34 = price34;
    }

    public double getPrice44() {
        return price44;
    }

    public void setPrice44(double price44) {
        this.price44 = price44;
    }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
