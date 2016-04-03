package dhbk.android.testgooglesearchreturn.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by huynhducthanhphong on 4/3/16.
 */
public class DirectionActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        makeMapDefaultSetting();

        EditText startPoint = (EditText)findViewById(R.id.start_point);
        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call search activity
            }
        });
        EditText endPoint = (EditText)findViewById(R.id.end_point);
        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call search activity
            }
        });
    }
}
