package se.group14.foodfinder;

/**
 * Created by AlexanderJD on 2017-03-17.
 */

public class SearchController {
    private int distance, price;

    public SearchController(int distance, int price) {
        this.distance=distance;
        this.price=price;
        System.out.println(distance + " " + price);
    }
}
