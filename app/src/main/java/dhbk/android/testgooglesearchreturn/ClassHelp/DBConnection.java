package dhbk.android.testgooglesearchreturn.ClassHelp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thien Nhan on 4/7/2016.
 */
public class DBConnection extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "map";
    public static final String DATABASE_TABLE = "list_trip";
    public static final int DATABASE_VERSION = 1;

    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table " + DATABASE_NAME +
                "(id integer primary key AUTOINCREMENT , name text, description text, image text)";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS list_trip");
        onCreate(db);
    }

    public void addTrip(RouteInfo info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", info.getName());
        values.put("description", info.getDescription());
        values.put("image", info.getImg());
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public List<RouteInfo> getAllContact() {
        List<RouteInfo> list = new ArrayList<RouteInfo>();
        String sql = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                RouteInfo person = new RouteInfo();
                person.setName(cursor.getString(0));
                person.setDescription(cursor.getString(1));
                person.setImg(cursor.getString(2));
                list.add(person);
            } while (cursor.moveToNext());
        }
        return list;


    }
}
