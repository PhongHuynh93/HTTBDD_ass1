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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import dhbk.android.testgooglesearchreturn.ClassHelp.DBConnection;
import dhbk.android.testgooglesearchreturn.ClassHelp.ListImageAdapter;
import dhbk.android.testgooglesearchreturn.ClassHelp.RecyclerViewItemClickListner;
import dhbk.android.testgooglesearchreturn.ClassHelp.RouteInfo;
import dhbk.android.testgooglesearchreturn.R;

public class EditTripInfoActivity extends AppCompatActivity {
    private static final String GALLERY_FOLDER = "tripGallery";
    Button btnSave;
    EditText nameRoute, descript;
    RecyclerView recyclerView;
    ArrayList<String> arraySelectedFile;
    String route;
    RouteInfo routeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);
        btnSave = (Button) findViewById(R.id.btnSave);
        nameRoute = (EditText) findViewById(R.id.edtNameRoute);
        descript = (EditText) findViewById(R.id.edtDescription);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        arraySelectedFile = new ArrayList<String>();

        String id = getTripId();
        routeInfo = getTripInfo(id);
        nameRoute.setText(routeInfo.getName());
        descript.setText(routeInfo.getDescription());
        initRecycleView();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                Intent shareAcvitivy = new Intent(getApplicationContext(), SavedListTripActivity.class);
                startActivity(shareAcvitivy);
            }
        });

    }

    private String getTripId() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        return id;
    }

    private RouteInfo getTripInfo(String id) {
        DBConnection dbConnection = new DBConnection(this);
        RouteInfo routeInfo = dbConnection.getTripInfo(id);
        return routeInfo;
    }

    private List<File> getSavedImage() {
        // lay danh sach hinh đã chọn
        String imgList = routeInfo.getImg();
        Gson gson = new Gson();
        String[] img = gson.fromJson(imgList, String[].class);
        List<File> list = new ArrayList<File>();
        for (String each : img) {
            String path = "/storage/emulated/0/DCIM/Camera/tripGallery/";
            File file = new File(path + each);
            list.add(file);
        }
        return list;
    }

    private void initRecycleView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter imageAdapter = new ListImageAdapter(sortFile(), getSavedImage());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListner(getApplicationContext(), new RecyclerViewItemClickListner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder.itemView.getAlpha() == (float) 0.5) {
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

    private void update() {
        String name = nameRoute.getText().toString();
        String description = descript.getText().toString();
        if (arraySelectedFile.size() > 0) {
            Gson gson = new Gson();
            String img = gson.toJson(arraySelectedFile);
            routeInfo.setImg(img);
        }

        DBConnection dbConnection = new DBConnection(this);
        routeInfo.setName(name);
        routeInfo.setDescription(description);


        dbConnection.updateInfO(routeInfo);
        Toast.makeText(EditTripInfoActivity.this, "Update complete", Toast.LENGTH_SHORT).show();


    }
}
