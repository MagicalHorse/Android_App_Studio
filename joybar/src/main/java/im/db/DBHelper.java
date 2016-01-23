package im.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shenma.yueba.application.MyApplication;


/**
 * Created by Administrator on 2016/1/20.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper() {
        super(MyApplication.getInstance(), DBStaticInfo.DBName, null, DBStaticInfo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table IF NOT EXISTS " + DBStaticInfo.IM_DBTABLE + "(" + DBStaticInfo.key_ID + " integer primary key autoincrement," +
                DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_FromUserID + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_FromUser_Name + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_FromUser_icon + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_Message_Type + DBStaticInfo.field_int + "," +
                DBStaticInfo.IM_DBTABLE_Message_status + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_Message_body + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_Message_productID + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_Message_shareUrl + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_toUser_ID + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_toUser_Name + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_toUser_icon + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_Chat_Type + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_MESSAGE_id + DBStaticInfo.field_varchar100 + "," +
                DBStaticInfo.IM_DBTABLE_FromUserType + DBStaticInfo.field_varchar100 +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
