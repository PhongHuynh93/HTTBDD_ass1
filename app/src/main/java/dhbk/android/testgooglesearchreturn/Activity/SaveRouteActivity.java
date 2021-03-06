package dhbk.android.testgooglesearchreturn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dhbk.android.testgooglesearchreturn.ClassHelp.DBConnection;
import dhbk.android.testgooglesearchreturn.ClassHelp.ListImageAdapter;
import dhbk.android.testgooglesearchreturn.ClassHelp.RecyclerViewItemClickListner;
import dhbk.android.testgooglesearchreturn.ClassHelp.RouteInfo;
import dhbk.android.testgooglesearchreturn.R;

public class SaveRouteActivity extends AppCompatActivity {
    private static final String GALLERY_FOLDER = "tripGallery";
    Button btnSave;
    EditText nameRoute, descript;
    RecyclerView recyclerView;
    ArrayList<String> arraySelectedFile;
    String route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);
        btnSave = (Button) findViewById(R.id.btnSave);
        nameRoute = (EditText) findViewById(R.id.edtNameRoute);
        descript = (EditText) findViewById(R.id.edtDescription);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        arraySelectedFile = new ArrayList<String>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        route = bundle.getString("routeJSON");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter imageAdapter = new ListImageAdapter(sortFile(), null);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListner(getApplicationContext(), new RecyclerViewItemClickListner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder.itemView.getAlpha() == 0.5) {
                            String deselectedFileName = getSelectedFileName(position);
                            arraySelectedFile.remove(deselectedFileName);
                            viewHolder.itemView.setAlpha((float) 1.0); // deselect item
                        } else {
                            String selectedFileName = getSelectedFileName(position);
                            arraySelectedFile.add(selectedFileName);
                            viewHolder.itemView.setAlpha((float) 0.5); //select item
                        }

                    }


                })
        );
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent shareAcvitivy = new Intent(getApplicationContext(), ShareActivity.class);
                startActivity(shareAcvitivy);
            }
        });

    }

    public String getSelectedFileName(int position) {
        List<File> listFile = sortFile();
        String name = listFile.get(position).getName();
        return name;
    }

    private List<File> sortFile() {

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera"); //path to folder gallery
        File galleryFoler = new File(storageDirectory, GALLERY_FOLDER); // prepare create folder
        if (!galleryFoler.exists()) {
            galleryFoler.mkdirs(); //create folder
        }

        File[] files = galleryFoler.listFiles();

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(Long.valueOf(lhs.lastModified()));
            }
        });
        List<File> list = Arrays.asList(files);
        return list;
    }

    private void saveData() {

        String name = nameRoute.getText().toString();
        String description = descript.getText().toString();
        Gson gson = new Gson();
        String img = gson.toJson(arraySelectedFile);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        String time = df.format(dateobj);
        DBConnection dbConnection = new DBConnection(this);
        dbConnection.addTrip(new RouteInfo(name, description, img, route, time));
        Toast.makeText(SaveRouteActivity.this, "Save complete", Toast.LENGTH_SHORT).show();
    }

}
