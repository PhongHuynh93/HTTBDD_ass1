package dhbk.android.testgooglesearchreturn.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dhbk.android.testgooglesearchreturn.R;

public class MapSampleActivity extends BaseActivity {
    RoadManager roadManager = new OSRMRoadManager(this);
    MapView mMap;
    Marker mStart, mEnd;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sample);
        makeMapDefaultSetting();
        mMap = getMapView();
        mapController = getIMapController();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String routeJson = bundle.getString("route");
        ArrayList<GeoPoint> route = getRoute(routeJson);
        Road road = new Road(route);
        Polyline line = roadManager.buildRoadOverlay(road, getBaseContext());
        mMap.getOverlays().add(line);

        mapController.setCenter(route.get(0));
        mapController.setZoom(18);
        mStart = new Marker(mMap);
        Drawable startIcon = getResources().getDrawable(R.drawable.start);
        mStart.setIcon(startIcon);
        mStart.setPosition(route.get(0));
        mMap.getOverlays().add(mStart);

        mEnd = new Marker(mMap);
        Drawable end = getResources().getDrawable(R.drawable.end);
        mEnd.setIcon(end);
        mEnd.setPosition(route.get(route.size() - 1));
        mMap.getOverlays().add(mEnd);

        mMap.invalidate();
    }

    private ArrayList<GeoPoint> getRoute(String routeJson) {
        SharedPreferences settings;
        List<GeoPoint> favorites;
        String jsonFavorites = routeJson;
        Gson gson = new Gson();
        GeoPoint[] favoriteItems = gson.fromJson(jsonFavorites,
                GeoPoint[].class);

        favorites = Arrays.asList(favoriteItems);
        favorites = new ArrayList<GeoPoint>(favorites);
        return (ArrayList<GeoPoint>) favorites;

    }
}
