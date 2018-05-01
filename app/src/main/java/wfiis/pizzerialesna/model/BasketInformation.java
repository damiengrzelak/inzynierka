package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BasketInformation {
    private String city;
    private String flatNumber;
    private boolean inCache;
    private boolean isPersonal;
    private String phone;
    private String street;
    private String zipCode;

    public BasketInformation() {
    }

    public BasketInformation(String city, String flatNumber, boolean inCache, boolean isPersonal, String phone, String street, String zipCode) {
        this.city = city;
        this.flatNumber = flatNumber;
        this.inCache = inCache;
        this.isPersonal = isPersonal;
        this.phone = phone;
        this.street = street;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public boolean isInCache() {
        return inCache;
    }

    public void setInCache(boolean inCache) {
        this.inCache = inCache;
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public void setPersonal(boolean personal) {
        isPersonal = personal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
