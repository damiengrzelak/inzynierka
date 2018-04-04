package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Damian on 04.04.2018.
 */

@IgnoreExtraProperties
public class Extras {
    private double highPrice;
    private double lowPrice;
    private double mediumPrice;
    private String name;
    private int number;
    private List<Warianty> variants;

    public Extras() {
    }

    public Extras(double highPrice, double lowPrice, double mediumPrice, String name, int number, List<Warianty> variants) {
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.mediumPrice = mediumPrice;
        this.name = name;
        this.number = number;
        this.variants = variants;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(double mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

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

    public List<Warianty> getVariants() {
        return variants;
    }

    public void setVariants(List<Warianty> variants) {
        this.variants = variants;
    }
}
