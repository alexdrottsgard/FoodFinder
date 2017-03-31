package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {
    private Restaurant[] restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
