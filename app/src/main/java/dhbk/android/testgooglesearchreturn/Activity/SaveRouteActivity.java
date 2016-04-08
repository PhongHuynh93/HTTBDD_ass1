package dhbk.android.testgooglesearchreturn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dhbk.android.testgooglesearchreturn.R;

public class SaveRouteActivity extends AppCompatActivity {
    Button btnSave;
    EditText nameRoute, descript;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);
        btnSave = (Button) findViewById(R.id.btnSave);
        nameRoute = (EditText) findViewById(R.id.edtNameRoute);
        descript = (EditText) findViewById(R.id.edtDescription);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    private void saveData() {
        String name = nameRoute.getText().toString();
        String description = descript.getText().toString();

    }

}
