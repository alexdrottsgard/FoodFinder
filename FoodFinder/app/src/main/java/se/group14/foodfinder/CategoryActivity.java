package se.group14.foodfinder;

import android.os.Bundle;
import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;



public class CategoryActivity extends Activity {

    ListView CategoryList;
    Button getChoice;

    String[] categorylistContent = { "Asiatiskt", "Café", "Hamburgare"};



    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        CategoryList = (ListView)findViewById(R.id.categoryList);

        getChoice = (Button)findViewById(R.id.getchoice);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, categorylistContent);
        CategoryList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        CategoryList.setAdapter(adapter);
        getChoice.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {

                String selected = "";
                int cntChoice = CategoryList.getCount();

                SparseBooleanArray sparseBooleanArray = CategoryList.getCheckedItemPositions();

                for(int i = 0; i < cntChoice; i++){
                    if(sparseBooleanArray.get(i)) {
                        selected += CategoryList.getItemAtPosition(i).toString() + "\n";

                    }

                }

                Toast.makeText(CategoryActivity.this, selected, Toast.LENGTH_LONG).show();

            }});



    }

}