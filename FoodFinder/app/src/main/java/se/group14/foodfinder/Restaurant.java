package se.group14.foodfinder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filipheidfors on 2017-03-31.
 */

public class Restaurant implements Parcelable {
    private String id;
    private String name;
    private String address;
    private String phone;

    public Restaurant(Parcel source) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeDouble(rating);
        dest.writeInt(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(website);
        dest.writeString(category);
        dest.writeString(hours);
        dest.writeInt(distance);


    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {

        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
