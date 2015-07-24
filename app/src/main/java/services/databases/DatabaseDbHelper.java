package services.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Slawek on 2015-07-15.
 */
public class DatabaseDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SZIOL.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CONFIGURATIONS =
            "CREATE TABLE IF NOT EXISTS " + DatabaseContract.ConfigurationEntry.TABLE_NAME + " (" +
                    DatabaseContract.ConfigurationEntry.COLUMN_KEY + TEXT_TYPE + " PRIMARY KEY " + COMMA_SEP +
                    DatabaseContract.ConfigurationEntry.COLUMN_VALUE + TEXT_TYPE + ")";

    private static final String SQL_CREATE_TICKETS =
            "CREATE TABLE IF NOT EXISTS " + DatabaseContract.TicketEntry.TABLE_NAME + " (" +
                    DatabaseContract.TicketEntry.COLUMN_ID + INT_TYPE + " PRIMARY KEY " + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_STATUS + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_CUSTOMER_ID + INT_TYPE + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_EXECUTOR_ID + INT_TYPE + COMMA_SEP +
                    DatabaseContract.TicketEntry.COLUMN_CREATOR_ID + INT_TYPE + ")";

    private static final String SQL_DELETE_CONFIGURATIONS =
            "DROP TABLE IF EXISTS " + DatabaseContract.ConfigurationEntry.TABLE_NAME;

    private static final String SQL_DELETE_TICKETS =
            "DROP TABLE IF EXISTS " + DatabaseContract.TicketEntry.TABLE_NAME;

    public DatabaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONFIGURATIONS);
        db.execSQL(SQL_CREATE_TICKETS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONFIGURATIONS);
        db.execSQL(SQL_DELETE_TICKETS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
