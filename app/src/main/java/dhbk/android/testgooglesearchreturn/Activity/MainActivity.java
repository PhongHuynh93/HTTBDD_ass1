package dhbk.android.testgooglesearchreturn.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

import dhbk.android.testgooglesearchreturn.ClassHelp.Constant;
import dhbk.android.testgooglesearchreturn.ClassHelp.FetchAddressIntentService;
import dhbk.android.testgooglesearchreturn.ClassHelp.ImagePagerAdapter;
import dhbk.android.testgooglesearchreturn.ClassHelp.PhotoTask;
import dhbk.android.testgooglesearchreturn.R;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainApp";
    // Map
    private MapView mMapView;
    public static GoogleApiClient mGoogleApiClient;

    // contain Google photo
    public static ArrayList<PhotoTask.AttributedPhoto> mArrayListAttributedPhoto;
    private ViewPager viewPager;
    private boolean showFAB = true;

    // place id obtains when search
    public static Place mPlace;

    private AddressResultReceiver mResultReceiver;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Phong - show the map + add 2 zoom button + zoom at a default view point
        makeMapDefaultSetting();
        mMapView = getMapView();

        viewPager = (ViewPager) findViewById(R.id.imageSlider);

        // receive address at a location.
        mResultReceiver = new AddressResultReceiver(new Handler());

        // declare google api
        buildGoogleApiClient();

        // TODO: 3/30/16 Hiếu - khi mở, app sẽ xét xem mình có mở GPS chưa, nếu chưa thì app sẽ hiện 1 hộp thoại "Dialog" yêu cầu người dùng mở GPS, ông sẽ hiện thực hộp thoại này

        declareView();
    }

    private void declareView() {
        // fab
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_my_location);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    // when click, remove old maker, add new marker
//                    mMapView.getOverlays().clear();
                    clearMap();

                    Location userCurrentLocation = getLocation();
                    setMarkerAtLocation(userCurrentLocation, Constant.MARKER);
                } else {
                    Log.i(TAG, "onClick: GoogleApi not connect");
                }
            }
        });

        // if click, go to another activity
        final FloatingActionButton floatingActionButtonDirection = (FloatingActionButton) findViewById(R.id.fab_direction);
        floatingActionButtonDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to Direction activity with place id.
                Intent intent = new Intent(getApplication(), DirectionActivity.class);
                // pass place id

                startActivity(intent);
            }
        });

        // To handle FAB animation upon entrance and exit
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);
        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // bottom sheet
        View bottomSheetDetailPlace = findViewById(R.id.map_bottom_sheets);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDetailPlace);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, "onStateChanged: goi bottom sheet");
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i(TAG, "onStateChanged: drag");
                        if (showFAB)
                            floatingActionButton.startAnimation(shrinkAnimation);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i(TAG, "onStateChanged: collapsed");
                        showFAB = true;
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.startAnimation(growAnimation);
                        floatingActionButtonDirection.startAnimation(growAnimation);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i(TAG, "onStateChanged: expanded");
                        showFAB = false;
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
        // place details
        placeName = (TextView) findViewById(R.id.place_name);
        final TextView addressName = (TextView) findViewById(R.id.address_name);
        final TextView phoneName = (TextView) findViewById(R.id.phone_name);
        final TextView websiteName = (TextView) findViewById(R.id.website_name);

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
                mPlace = place;
                // Format the returned place's details and display them in the TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
//                        place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    // add place details
                    if (place.getName() != null) {
                        placeName.setText(place.getName());
                    }
                    if (place.getAddress() != null) {
                        addressName.setText(place.getAddress());
                    }
                    if (place.getPhoneNumber() != null) {
                        phoneName.setText(place.getPhoneNumber());
                    }
                    if (place.getWebsiteUri() != null) {
                        websiteName.setText(place.getWebsiteUri() + "");
                    }

                    // add place photos
                    addPhotoToBottomSheet(place.getId(), mGoogleApiClient);

                    bottomSheetBehavior.setPeekHeight(369);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                // remove marker on the map, center at that point and add marker.
//                mMapView.getOverlays().clear();
                clearMap();
                Location placeLocation = new Location("Test");
                placeLocation.setLatitude(place.getLatLng().latitude);
                placeLocation.setLongitude(place.getLatLng().longitude);
                setMarkerAtLocation(placeLocation, Constant.MARKER);
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
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    // add a google photo to google image view.
    private void addPhotoToBottomSheet(String id, GoogleApiClient mGoogleApiClient) {
        new PhotoTask(viewPager.getWidth(), viewPager.getHeight()) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
            }

            @Override
            protected void onPostExecute(ArrayList<AttributedPhoto> attributedPhotos) {

                // load image on viewpager, remove old images and add new ones.
                if (attributedPhotos.size() > 0) {
                    mArrayListAttributedPhoto = attributedPhotos;
                    ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), attributedPhotos.size());
                    viewPager.setAdapter(imagePagerAdapter);
                }
            }
        }.execute(new PhotoTask.MyTaskParams(id, mGoogleApiClient));
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(mMapView);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        clearMap();
        // reverse location into address


        // make search bar and bottom sheet contain address

        // draw marker
        Location touchLocation = new Location("touchLocation");
        touchLocation.setLatitude(p.getLatitude());
        touchLocation.setLongitude(p.getLongitude());
        setMarkerAtLocation(touchLocation, Constant.MARKER);

        // chuyen thanh address
        startIntentService(touchLocation);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Static google client " + " onStart: " + mGoogleApiClient);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Static google client " + "onStop: " +mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location touchLocation) {
        if (mGoogleApiClient.isConnected()) {
            // Create an intent for passing to the intent service responsible for fetching the address.
            Intent intent = new Intent(this, FetchAddressIntentService.class);

            // Pass the result receiver as an extra to the service.
            intent.putExtra(Constant.RECEIVER, mResultReceiver);

            // Pass the location data as an extra to the service.
            intent.putExtra(Constant.LOCATION_DATA_EXTRA, touchLocation);

            // Start the service. If the service isn't already running, it is instantiated and started
            // (creating a process for it if needed); if it is running then it remains running. The
            // service kills itself automatically once all intents are processed.
            startService(intent);
        }
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            String addressOutput = resultData.getString(Constant.RESULT_DATA_KEY);
            displayAddressOutput(addressOutput);

            // Show a toast message if an address was found.
            if (resultCode == Constant.SUCCESS_RESULT) {
                Log.i(TAG, "onReceiveResult: " + R.string.address_found);
            }
        }
    }

    private void displayAddressOutput(String addressOutput) {
        Log.i(TAG, "displayAddressOutput: " + addressOutput);
        // TODO: 4/12/16 phong - make address on bottom bar and on search view
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            // add place details
            placeName.setText(addressOutput);
            bottomSheetBehavior.setPeekHeight(369);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}
