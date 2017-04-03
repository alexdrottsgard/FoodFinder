package se.group14.foodfinder;

/**
 * Created by filipheidfors on 2017-03-31.
 */

public class Restaurant {
    private String name;
    private String address;
    private String phone;
    private double rating;
    private double latitude;
    private double longitude;
    private String website;
    private int price;
    private String category;
    private String hours;
    private int distance;

    public Restaurant(String name, String address, String phone, double rating, double latitude, double longitude, String website, int price, String category, String hours) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.website = website;
        this.price = price;
        this.category = category;
        this.hours = hours;
    }

    public Restaurant() {

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

    public void setHours(String hours) {
        this.hours = hours;
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

    public String getHours() {
        return hours;
    }

    public String getName() {
        return name;
    }


}
