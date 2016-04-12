package dhbk.android.testgooglesearchreturn.ClassHelp;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by huynhducthanhphong on 4/5/16.
 */
public final class Constant {
    // url request for direction
    public static final String GOOGLE_SERVER_KEY = "AIzaSyAH9us0uue6rltRozrzN4Kf43nEL2oX5ds";
    public static final String MODE_RUN = "walking";
    public static final String MODE_BIKE = "bicycling";
    public static final String MODE_CAR = "driving";
    public static final String MODE_BUS = "transit";

    // draw on map
    public static final Float WIDTH_LINE = 20.0f;
    public static final int COLOR = 0x8003A9F4;
    public static final String LANGUAGE = "vi";
    public static final int ICON_INSTRUCTION = R.drawable.ic_place_black_24dp;
    public static final int MARKER = R.drawable.ic_add_location_black_36dp;
    public static final int ZOOM = 14;

    // service to reverse location to address
    public static final String PACKAGE_NAME = "dhbk.android.testgooglesearchreturn.Activity";
    public static final String RECEIVER = PACKAGE_NAME + ".MainActivity";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final int FAILURE_RESULT = 1;
    public static final int SUCCESS_RESULT = 0;


}
