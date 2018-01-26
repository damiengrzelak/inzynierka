package wfiis.pizzerialesna.model;

public class User {

    public String uid;
    public String name;
    public String surnmale;
    public String email;
    public String password;
    public String phone;
    public String street;
    public String houseNr;
    public String city;
    public String zipCode;
    public boolean permission;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnmale() {
        return surnmale;
    }

    public void setSurnmale(String surnmale) {
        this.surnmale = surnmale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public User() {
    }

    public User(String name, String surnmale, String email, String password, String phone, String street, String houseNr, String city, String zipCode, boolean permission) {
        this.name = name;
        this.surnmale = surnmale;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.street = street;
        this.houseNr = houseNr;
        this.city = city;
        this.zipCode = zipCode;
        this.permission = permission;
    }

}
