package dhbk.android.testgooglesearchreturn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import dhbk.android.testgooglesearchreturn.ClassHelp.DBConnection;
import dhbk.android.testgooglesearchreturn.ClassHelp.RecyclerViewItemClickListner;
import dhbk.android.testgooglesearchreturn.ClassHelp.RouteInfo;
import dhbk.android.testgooglesearchreturn.ClassHelp.SavedListAdapter;
import dhbk.android.testgooglesearchreturn.R;

public class SavedListTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    int itemId;
    List<RouteInfo> savedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list_trip);
        recyclerView = (RecyclerView) findViewById(R.id.saved_list);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        DBConnection db = new DBConnection(this);
        savedList = db.getAllList();
        RecyclerView.Adapter savedListAdapter = new SavedListAdapter(savedList);
        recyclerView.setAdapter(savedListAdapter);
        // Toast.makeText(SavedListTripActivity.this,  savedList.get(2).getTime(), Toast.LENGTH_SHORT).show();

        recyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListner(getApplicationContext(), new RecyclerViewItemClickListner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(SavedListTripActivity.this, action, Toast.LENGTH_SHORT).show();
                        itemId = position;
                    }



                })
        );

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getItemId();
        if (position == 0) {
            //Show map
            String route = savedList.get(position).getRoute();
            Intent sampleMapActivity = new Intent(getApplicationContext(), MapSampleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("route", route);
            sampleMapActivity.putExtras(bundle);
            startActivity(sampleMapActivity);
        } else {
            //edit
            String id = savedList.get(itemId).getId();
            Intent editActivity = new Intent(this, EditTripInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            editActivity.putExtras(bundle);
            startActivity(editActivity);
        }
        return super.onContextItemSelected(item);
    }
}
