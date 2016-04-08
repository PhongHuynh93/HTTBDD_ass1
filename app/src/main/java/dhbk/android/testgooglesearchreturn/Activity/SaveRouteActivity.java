package dhbk.android.testgooglesearchreturn.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import dhbk.android.testgooglesearchreturn.ClassHelp.RecyclerViewItemClickListner;
import dhbk.android.testgooglesearchreturn.ListImageAdapter;
import dhbk.android.testgooglesearchreturn.R;

public class SaveRouteActivity extends AppCompatActivity {
    private static final String GALLERY_FOLDER = "tripGallery";
    Button btnSave;
    EditText nameRoute, descript;
    RecyclerView recyclerView;
    ArrayList<String> arraySelectedFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);
        btnSave = (Button) findViewById(R.id.btnSave);
        nameRoute = (EditText) findViewById(R.id.edtNameRoute);
        descript = (EditText) findViewById(R.id.edtDescription);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        arraySelectedFile = new ArrayList<String>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter imageAdapter = new ListImageAdapter(sortFile());
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
            }
        });

    }

    public String getSelectedFileName(int position) {
        File[] files = sortFile();
        List<File> listFile = Arrays.asList(files);
        String name = listFile.get(position).getName();
        return name;
    }

    private File[] sortFile() {

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
        return files;
    }

    private void saveData() {
        String name = nameRoute.getText().toString();
        String description = descript.getText().toString();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(arraySelectedFile);
        Log.d("@@", jsonFavorites);

    }

}
