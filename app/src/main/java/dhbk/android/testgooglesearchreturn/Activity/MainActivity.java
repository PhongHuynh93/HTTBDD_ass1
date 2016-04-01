package dhbk.android.testgooglesearchreturn.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
<<<<<<< HEAD:app/src/main/java/dhbk/android/testgooglesearchreturn/Activity/MainActivity.java
import android.text.Html;
=======
>>>>>>> master:app/src/main/java/dhbk/android/testgooglesearchreturn/MainActivity.java
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.osmdroid.views.MapView;

import dhbk.android.testgooglesearchreturn.ClassHelp.PhotoTask;
import dhbk.android.testgooglesearchreturn.R;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainApp";
    // Map
    private MapView mMapView;
    private GoogleApiClient mGoogleApiClient;

    //    declare bottom sheet
    private BottomSheetBehavior mBottomSheetBehavior;
    private FrameLayout mBottomSheetDetailPlace;
    private TextView mPlaceName;
    private TextView mAddressName;
    private TextView mPhoneName;
    private TextView mWebsiteName;
    private ImageView mImagePlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Phong - show the map + add 2 zoom button + zoom at a default view point
        makeMapDefaultSetting();
        mMapView = getMapView();
        mGoogleApiClient = getmGoogleApiClient();

        // TODO: 3/30/16 Hiếu - khi mở, app sẽ xét xem mình có mở GPS chưa, nếu chưa thì app sẽ hiện 1 hộp thoại "Dialog" yêu cầu người dùng mở GPS, ông sẽ hiện thực hộp thoại này

        // Phong - when click fab, zoom to user's location
        declareFAB();

        // search view
        declareSearchView();

        // bottom sheet
        declareBottomSheet();
    }

    private void declareBottomSheet() {
        // place details
        mPlaceName = (TextView) findViewById(R.id.place_name);
        mAddressName = (TextView) findViewById(R.id.address_name);
        mPhoneName = (TextView) findViewById(R.id.phone_name);
        mWebsiteName = (TextView) findViewById(R.id.website_name);
        // place image
        mImagePlace = (ImageView) findViewById(R.id.image_place);
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
                    // add place details
                    if (place.getName() != null) {
                        mPlaceName.setText(place.getName());
                    }
                    if (place.getAddress() != null) {
                        mAddressName.setText(place.getAddress());
                    }
                    if (place.getPhoneNumber() != null) {
                        mPhoneName.setText(place.getPhoneNumber());
                    }
                    if (place.getWebsiteUri() != null) {
                        mWebsiteName.setText(place.getWebsiteUri() + "");
                    }

                    // add place photos
                    addPhotoToBottomSheet(place.getId(), mGoogleApiClient);

                    mBottomSheetBehavior.setPeekHeight(369);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                // remove marker on the map, center at that point and add marker.
                mMapView.getOverlays().clear();
                Location placeLocation = new Location("Test");
                placeLocation.setLatitude(place.getLatLng().latitude);
                placeLocation.setLongitude(place.getLatLng().longitude);
                setMarkerAtLocation(placeLocation, R.drawable.ic_face_black_24dp);
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
                    // when click, remove old maker, add new marker
                    mMapView.getOverlays().clear();

                    Location userCurrentLocation = getLocation();
                    setMarkerAtLocation(userCurrentLocation, R.drawable.ic_face_black_24dp);
                } else {
                    Log.i(TAG, "onClick: GoogleApi not connect");
                }
            }
        });
    }

    // add a google photo to google image view.
    private void addPhotoToBottomSheet(String id, GoogleApiClient mGoogleApiClient) {
        Log.i(TAG, "addPhotoToBottomSheet: Hàm này đã được goi");
        new PhotoTask(mImagePlace.getWidth(), mImagePlace.getHeight()) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                mImagePlace.setImageResource(R.drawable.ic_face_black_24dp);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    Log.i(TAG, "onPostExecute: Image được tải về là: " + attributedPhoto.bitmap);
                    mImagePlace.setImageBitmap(attributedPhoto.bitmap);
                }
            }
        }.execute(new PhotoTask.MyTaskParams(id, mGoogleApiClient));
    }

}
