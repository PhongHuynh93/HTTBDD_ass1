package dhbk.android.testgooglesearchreturn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import dhbk.android.testgooglesearchreturn.ClassHelp.SavedListAdapter;
import dhbk.android.testgooglesearchreturn.R;

public class SavedListTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list_trip);
        recyclerView = (RecyclerView) findViewById(R.id.saved_list);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        String[] img = {"1.jpg", "2.jpg", "3.jpg"};
        String[] name = {"bill", "ba", "bon"};
        String[] date = {"1.jpg", "2.jpg", "3.jpg"};
        RecyclerView.Adapter imageAdapter = new SavedListAdapter(img, name, date);
        recyclerView.setAdapter(imageAdapter);


    }
}
