package dhbk.android.testgooglesearchreturn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainApp";
    private MapView mMapView;
    private IMapController mIMapController;
    //    declare bottom sheet
    private BottomSheetBehavior mBottomSheetBehavior;
    private FrameLayout mBottomSheetDetailPlace;
    private TextView mPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 3/30/16 Hiếu - khi mở, app sẽ xét xem mình có mở GPS chưa, nếu chưa thì app sẽ hiện 1 hộp thoại "Dialog" yêu cầu người dùng mở GPS, ông sẽ hiện thực hộp thoại này

        // Phong - show the map + add 2 zoom button + zoom at a default view point
        makeMapDefaultSetting();

        // Phong - when click fab, zoom to user's location
        declareFAB();


        // search view
        declareSearchView();

        // bottom sheet
        declareBottomSheet();
    }

    private void declareBottomSheet() {
        mPlaceName = (TextView) findViewById(R.id.place_name);
        mBottomSheetDetailPlace = (FrameLayout) findViewById(R.id.map_bottom_sheets);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetDetailPlace);

    }

    private void declareSearchView() {
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            // khi return search place, make bottom sheets appear and set place details to it.
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place Selected: " + place.getName());
                Log.i(TAG, "Place Selected: " + place.getAddress());
                Log.i(TAG, "Place Selected: " + place.getPhoneNumber());
                Log.i(TAG, "Place Selected: " + place.getWebsiteUri());
                // Format the returned place's details and display them in the TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
//                        place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mPlaceName.setText(place.getName());
                    mBottomSheetBehavior.setPeekHeight(369);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "onError: Status = " + status.toString());
                Toast.makeText(getApplication(), "Place selection failed: " + status.getStatusMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // when click delete button, collapse bottom sheets (peekHeight = 0)
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText("");
                view.setVisibility(View.GONE);
                mBottomSheetBehavior.setPeekHeight(0);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }


    private void declareFAB() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_my_location);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGoogleConnected()) {
                    if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location userCurrentLocation = getLocation();
                    if (userCurrentLocation != null) {
                        GeoPoint userCurrentPoint = new GeoPoint(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                        mIMapController.setCenter(userCurrentPoint);
                        mIMapController.zoomTo(mMapView.getMaxZoomLevel());
                        Marker hereMarker = new Marker(mMapView);
                        hereMarker.setPosition(userCurrentPoint);
                        hereMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        hereMarker.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.ic_face_black_24dp));
                        hereMarker.setTitle("You here");
                        mMapView.getOverlays().add(hereMarker);
                        mMapView.invalidate();
                    } else {
                        Log.i(TAG, "onClick: Not determine your current location");
                    }
                } else {
                    Log.i(TAG, "onClick: GoogleApi not connect");
                }
            }
        });
    }

    private void makeMapDefaultSetting() {
        mMapView = (MapView) findViewById(R.id.map); // map
        if (mMapView != null) {
            mMapView.setTileSource(TileSourceFactory.MAPNIK);
            mMapView.setMultiTouchControls(true);
            mIMapController = mMapView.getController(); // map controller
            mIMapController.setZoom(10);
            GeoPoint startPoint = new GeoPoint(10.772241, 106.657676);
            mIMapController.setCenter(startPoint);
        }
    }

}
