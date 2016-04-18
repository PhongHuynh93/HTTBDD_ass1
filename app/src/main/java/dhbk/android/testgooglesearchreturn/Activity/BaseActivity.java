package dhbk.android.testgooglesearchreturn.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dhbk.android.testgooglesearchreturn.ClassHelp.Constant;
import dhbk.android.testgooglesearchreturn.R;


/**
 * Created by huynhducthanhphong on 3/30/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapEventsReceiver {
    private static final String TAG = BaseActivity.class.getName();
    private MapView mMapView;
    private IMapController mIMapController;

    public MapView getMapView() {
        return mMapView;
    }

    public IMapController getIMapController() {
        return mIMapController;
    }

    // Phong - after onCreate() get called, onPostCreate was called to declare nav.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        // add event overlay
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        mMapView.getOverlays().add(0, mapEventsOverlay);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constant.EXTRA_SHARED_PREF, MODE_PRIVATE);
        String url = sharedPreferences.getString(Constant.EXTRA_PROFILE_URL, null);
        if (url != null){
            ImageView image = (ImageView)findViewById(R.id.profile_pic);
            Picasso.with(getApplicationContext()).load(url).into(image);
        }
    }

    // Phong - called when select 1 item in nav.
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_tracking) {
            Intent intent = new Intent(this, ShareActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SavedListTripActivity.class);
            startActivity(intent);

        } else if (id == R.id.facebook) {
            Intent intent = new Intent(this, FacebookLoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    // Phong - called when swiped nav to left.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(MainActivity.mGoogleApiClient);
    }

    // phong - make default map when opening the activity.
    public void makeMapDefaultSetting() {
        mMapView = (MapView) findViewById(R.id.map); // map
        if (mMapView != null) {
            mMapView.setTileSource(TileSourceFactory.MAPNIK);
            mMapView.setMultiTouchControls(true);
            mIMapController = mMapView.getController(); // map controller
            mIMapController.setZoom(Constant.ZOOM);
            GeoPoint startPoint = new GeoPoint(10.772241, 106.657676);
            mIMapController.setCenter(startPoint);
        }
    }

    //phong - add marker at a location
    public void setMarkerAtLocation(Location userCurrentLocation, int icon) {
        if (userCurrentLocation != null) {
            GeoPoint userCurrentPoint = new GeoPoint(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
            mIMapController.setCenter(userCurrentPoint);
            mIMapController.zoomTo(mMapView.getMaxZoomLevel());
            Marker hereMarker = new Marker(mMapView);
            hereMarker.setPosition(userCurrentPoint);
            hereMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            hereMarker.setIcon(ContextCompat.getDrawable(getApplication(), icon));
//                        hereMarker.setTitle("You here");
            mMapView.getOverlays().add(hereMarker);
            mMapView.invalidate();
        } else {
            Log.i(TAG, "onClick: Not determine your current location");
        }
    }

    // phong - add marker with title
    public void setMarkerAtLocation(Location userCurrentLocation, int icon, String title) {
        if (userCurrentLocation != null) {
            GeoPoint userCurrentPoint = new GeoPoint(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
//            mIMapController.setCenter(userCurrentPoint);
//            mIMapController.zoomTo(mMapView.getMaxZoomLevel());
            Marker hereMarker = new Marker(mMapView);
            hereMarker.setPosition(userCurrentPoint);
//            hereMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            hereMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            hereMarker.setIcon(ContextCompat.getDrawable(getApplication(), icon));
            hereMarker.setTitle(title);
            mMapView.getOverlays().add(hereMarker);
            mMapView.invalidate();
        } else {
            Log.i(TAG, "onClick: Not determine your current location");
        }
    }

    // phong - draw path
    public void drawPathOSM(Location startPoint, Location destPoint, String travelMode, float width) {
        String url = makeURL(startPoint.getLatitude(), startPoint.getLongitude(), destPoint.getLatitude(), destPoint.getLongitude(), travelMode);
        new GetDirection(startPoint, destPoint, url, width).execute();
    }

    // phong - draw path with instruction on that path.
    public void drawPathOSMWithInstruction(Location startPoint, Location destPoint, String travelMode, float width) {
        String url = makeURL(startPoint.getLatitude(), startPoint.getLongitude(), destPoint.getLatitude(), destPoint.getLongitude(), travelMode);
        new GetDirectionInstruction(startPoint, destPoint, url, width).execute();
    }


    // phong - make a URL to Google to get direction.
    @NonNull
    private String makeURL(double sourcelat, double sourcelog, double destlat, double destlog, String travelMode) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&mode=" + travelMode);
        urlString.append("&language=" + Constant.LANGUAGE);
        urlString.append("&key=" + Constant.GOOGLE_SERVER_KEY);
        Log.i(TAG, "makeURL: " + urlString.toString());
        return urlString.toString();
    }

    // phong - get JSON reponse from a URL
    private String getJSONFromUrl(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        // request
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            assert url1 != null;
            connection = (HttpURLConnection) url1.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert connection != null;
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // read the response
        try {
            if (connection.getResponseCode() == 201 || connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error in request");
        }

        return stringBuilder.toString();
    }

    // phong - draw path from JSON reponse
    private void drawPath(String result, Location startPlace, Location destPlace, float width) {
        ArrayList<GeoPoint> waypoints = new ArrayList<>(); // tao 1 array cac toạ dộ
        GeoPoint startPoint = new GeoPoint(startPlace.getLatitude(), startPlace.getLongitude());
        waypoints.add(startPoint);
        boolean isReturnOK = true;

        try {
            final JSONObject json = new JSONObject(result); // lưu JSON mà server trả
            JSONArray routeArray = json.getJSONArray("routes");

            if (json.getString("status").equals("OK")) {
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes
                        .getJSONObject("overview_polyline"); // duong di cua google

                String encodedString = overviewPolylines.getString("points"); // lấy value với key là "point"
                List<GeoPoint> list = decodePoly(encodedString); // hàm này return 1 list Geopoint doc  đường đi


                for (int z = 0; z < list.size() - 1; z++) {
                    GeoPoint src = list.get(z);
                    waypoints.add(src);
                }
            } else {
                isReturnOK = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isReturnOK) {
            GeoPoint destPoint = new GeoPoint(destPlace.getLatitude(), destPlace.getLongitude());
            waypoints.add(destPoint);

            Road road = new Road(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Constant.COLOR, width, getApplicationContext());
            mMapView.getOverlays().add(roadOverlay);
        }
        mMapView.invalidate();
    }


    // phong - draw path from JSON reponse
    private void drawPathWithInstruction(String result, Location startPlace, Location destPlace, float width) {
        ArrayList<GeoPoint> waypoints = new ArrayList<>(); // tao 1 array cac toạ dộ
        GeoPoint startPoint = new GeoPoint(startPlace.getLatitude(), startPlace.getLongitude());
        waypoints.add(startPoint);
        ArrayList<JSONObject> stepsArrayObject = null;
        boolean isReturnOK = true;

        try {
            final JSONObject json = new JSONObject(result); // lưu JSON mà server trả
            JSONArray routeArray = json.getJSONArray("routes");

            if (json.getString("status").equals("OK")) {

                JSONObject routes = routeArray.getJSONObject(0);

                JSONObject overviewPolylines = routes
                        .getJSONObject("overview_polyline"); // duong di cua google

                // retrieve step
                JSONArray legsArray = routes.getJSONArray("legs");
                JSONObject leg = legsArray.getJSONObject(0);
                JSONArray stepsArray = leg.getJSONArray("steps");
                stepsArrayObject = new ArrayList<>();
                for (int i = 0; i < stepsArray.length(); i++) {
                    stepsArrayObject.add(stepsArray.getJSONObject(i));
                }

                String encodedString = overviewPolylines.getString("points"); // lấy value với key là "point"
                List<GeoPoint> list = decodePoly(encodedString); // hàm này return 1 list Geopoint doc  đường đi


                for (int z = 0; z < list.size() - 1; z++) {
                    GeoPoint src = list.get(z);
                    waypoints.add(src);
                }
            } else {
                isReturnOK = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isReturnOK) {
            // draw path
            GeoPoint destPoint = new GeoPoint(destPlace.getLatitude(), destPlace.getLongitude());
            waypoints.add(destPoint);

            Road road = new Road(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Constant.COLOR, width, getApplicationContext());
            mMapView.getOverlays().add(roadOverlay);


            // draw marker on the road
            for (JSONObject step: stepsArrayObject) {
                try {
                    // get lat/long of a step
                    JSONObject startLocation = step.getJSONObject("start_location");
                    double lat = Double.parseDouble(startLocation.getString("lat"));
                    double lng = Double.parseDouble(startLocation.getString("lng"));
                    Location stepLocation = new Location("stepLocation");
                    stepLocation.setLatitude(lat);
                    stepLocation.setLongitude(lng);
                    // get instruction
                    String instruction = step.getString("html_instructions");
                    // add marker
                    setMarkerAtLocation(stepLocation, Constant.ICON_INSTRUCTION, instruction);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        mMapView.invalidate();

    }


    // phong - method to return a list of point from JSON.
    private List<GeoPoint> decodePoly(String encoded) {
        List<GeoPoint> poly = new ArrayList<GeoPoint>(); // list geopoint
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            GeoPoint p = new GeoPoint((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void centerMap(Location startPoint) {
        mMapView.getController().setCenter(new GeoPoint(startPoint.getLatitude(), startPoint.getLongitude()));
    }

    // event touch
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    // clear map, but add eventlocation
    public void clearMap() {
        mMapView.getOverlays().clear();
        // add event overlay
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        mMapView.getOverlays().add(0, mapEventsOverlay);
    }

    // phong - get json from URL
    private class GetDirection extends AsyncTask<Void, Void, String> {
        private final Location startPoint;
        private final Location destPoint;
        private String url;
        private float width;

        public GetDirection(Location startPoint, Location destPoint, String url, float width) {
            this.startPoint = startPoint;
            this.destPoint = destPoint;
            this.url = url;
            this.width = width;
        }

        @Override
        protected String doInBackground(Void... params) {
            String json = getJSONFromUrl(this.url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                drawPath(result, this.startPoint, this.destPoint, this.width);
                centerMap(this.startPoint);
            }
        }
    }

    // phong - get json from URL
    private class GetDirectionInstruction extends AsyncTask<Void, Void, String> {
        private final Location startPoint;
        private final Location destPoint;
        private String url;
        private float width;

        public GetDirectionInstruction(Location startPoint, Location destPoint, String url, float width) {
            this.startPoint = startPoint;
            this.destPoint = destPoint;
            this.url = url;
            this.width = width;
        }

        @Override
        protected String doInBackground(Void... params) {
            String json = getJSONFromUrl(this.url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                drawPathWithInstruction(result, this.startPoint, this.destPoint, this.width);
                centerMap(this.startPoint);
            }
        }
    }
}
