package se.group14.foodfinder;

import java.io.Serializable;

/**
 * Created by filipheidfors on 2017-03-31.
 * Klassen representerar en restaurang
 * @author Filip Heidfors, Alexander J. Drottsgård
 */

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String category;
    private String website;
    private String open;
    private double rating;
    private double latitude;
    private double longitude;
    private int price;
    private int distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOpen(String str) {
        open = str;
    }

    public double getRating() {
        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getWebsite() {
        return website;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getOpen() {
        return open;
    }

}
