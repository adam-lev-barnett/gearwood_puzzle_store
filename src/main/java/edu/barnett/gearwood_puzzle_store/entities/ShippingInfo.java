package edu.barnett.gearwood_puzzle_store.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShippingInfo {

    private String name;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zip;

    public ShippingInfo() {}

    public ShippingInfo(String name, String address, String address2, String city, String state, String zip) {
        this.name = name;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }
}
