package dhbk.android.testgooglesearchreturn;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by huynhducthanhphong on 3/30/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = BaseActivity.class.getName();

    // Phong - after onCreate() get called, onPostCreate was called to declare nav.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    // TODO: 3/30/16 Hiếu: fix lại nội dung navigation drawer
    // Phong - called when select 1 item in nav.
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the Direction Activity
            // Handle the Share Activity
            Log.i(TAG, "onNavigationItemSelected: Đã chọn activity MainActivity");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Log.i(TAG, "onNavigationItemSelected: Đã chọn activity ShareActivity");
            Log.i(TAG, "onNavigationItemSelected: Đã chọn activity MainActivity");
            Intent intent = new Intent(this, ShareActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            // TODO: 3/30/16 Hiếu: Khi click vào thì hiện thực Facebook log in.

        } else if (id == R.id.nav_manage) {
            // TODO: 3/30/16 Hiếu: Khi click vào thì hiện thực Facebook log out.

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
}
