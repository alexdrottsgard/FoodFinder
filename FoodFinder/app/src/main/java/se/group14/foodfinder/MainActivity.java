package se.group14.foodfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner priceSpinner;
    private int chosenPrice, chosenDistance;
    private Button searchButton;
    private EditText distanceField;
    private static final String[] priceClass = {"Prisklass 1", "Prisklass 2", "Prisklass 3", "Prisklass 4"};
    private Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceSpinner = (Spinner) findViewById(R.id.priceSpinner);
        searchButton = (Button) findViewById(R.id.searchButton);
        distanceField = (EditText) findViewById(R.id.distance);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, priceClass);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(adapter);
        priceSpinner.setOnItemSelectedListener(this);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 1:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 2:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 3:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        v = searchButton;
        chosenDistance = Integer.parseInt(distanceField.getText().toString());
        new SearchController(chosenDistance, chosenPrice, this);
    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(str)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();

    }

}
