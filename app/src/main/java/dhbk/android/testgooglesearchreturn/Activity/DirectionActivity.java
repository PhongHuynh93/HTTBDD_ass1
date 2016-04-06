package dhbk.android.testgooglesearchreturn.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import dhbk.android.testgooglesearchreturn.ClassHelp.Constant;
import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by huynhducthanhphong on 4/3/16.
 */
public class DirectionActivity extends BaseActivity {
    private static final int REQUEST_CODE_AUTOCOMPLETE_EDITTEXT_1 = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_EDITTEXT_2 = 2;
    private static final String TAG = DirectionActivity.class.getName();
    private EditText mStartPoint;
    private EditText mEndPoint;
    private BottomBar mBottomBar;
    private Toolbar mToolbar;
    private MapView mMapView;
    private Location destinationPlace;
    private Location startPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        makeMapDefaultSetting();
        mMapView = getMapView();

        declareFab();

        if (MainActivity.mPlace != null) {
            destinationPlace = new Location("destinationPlace");
            destinationPlace.setLatitude(MainActivity.mPlace.getLatLng().latitude);
            destinationPlace.setLongitude(MainActivity.mPlace.getLatLng().longitude);
        }
        startPlace = getLocation();
        Log.i(TAG, "onCreate: startPlace " + startPlace);

        declareBottomNavigation(savedInstanceState);

        // TODO: 4/6/16 phong - test trường hợp 2 thanh search
    }

    private void declareFab() {
        mStartPoint = (EditText) findViewById(R.id.start_point);
        assert mStartPoint != null;
        mStartPoint.setText(R.string.yourLocation);
        mStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call search activity
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_EDITTEXT_1);
            }
        });
        mEndPoint = (EditText) findViewById(R.id.end_point);
        if (MainActivity.mPlace != null) {
            assert mEndPoint != null;
            mEndPoint.setText(MainActivity.mPlace.getName());
        }
        mEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call search activity
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_EDITTEXT_2);
            }
        });

    }

    // phong - khung chứa 4 icons phương tiện.
    private void declareBottomNavigation(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(findViewById(R.id.map), savedInstanceState);

        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemRun) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot1));
                    drawNewPath(Constant.MODE_RUN);

                } else if (menuItemId == R.id.bottomBarItemBike) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot2));
                    drawNewPath(Constant.MODE_BIKE);


                } else if (menuItemId == R.id.bottomBarItemBus) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot3));
                    drawNewPath(Constant.MODE_BUS);


                } else if (menuItemId == R.id.bottomBarItemCar) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot4));
                    drawNewPath(Constant.MODE_CAR);

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemRun) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot1));
                    drawNewPath(Constant.MODE_RUN);

                } else if (menuItemId == R.id.bottomBarItemBike) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot2));
                    drawNewPath(Constant.MODE_BIKE);


                } else if (menuItemId == R.id.bottomBarItemBus) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot3));
                    drawNewPath(Constant.MODE_BUS);


                } else if (menuItemId == R.id.bottomBarItemCar) {
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.bot4));
                    drawNewPath(Constant.MODE_CAR);
                }
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, "#795548");//0xFF5D4037);
        mBottomBar.mapColorForTab(1, "#7B1FA2");//"#7B1FA2");
        mBottomBar.mapColorForTab(2, "#FF5252");//"#FF5252");
        mBottomBar.mapColorForTab(3, "#FF9800");//"#FF9800"  );
    }

    // xóa overlay + vẽ + phóng to
    private void drawNewPath(String mode) {
        // xóa overlay + vẽ + phóng to
        Log.i(TAG, "drawNewPath: startplace " + startPlace);
        Log.i(TAG, "drawNewPath: destplace " + destinationPlace);
        if (startPlace != null && destinationPlace != null) {
            GeoPoint userCurrentPoint = new GeoPoint(startPlace.getLatitude(), startPlace.getLongitude());
            mMapView.getController().setCenter(userCurrentPoint);
            mMapView.getController().zoomTo(Constant.ZOOM);
            mMapView.getOverlays().clear();
            drawPathOSM(startPlace, destinationPlace, mode, Constant.WIDTH_LINE);
        }
    }

    private void openAutocompleteActivity(int code) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, code);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_EDITTEXT_1) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());
                mStartPoint.setText(place.getName());

                // Format the place's details and display them in the TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
//
//                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } else {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());
                mEndPoint.setText(place.getName());

                // Format the place's details and display them in the TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
//
//                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

}
