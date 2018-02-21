package com.example.savss.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "expensetrakerDB.db";
    private static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONENUMBER = "phonenumber";
    public static final String COLUMN_PASSWORD = "password";

    public static enum IDType { Email, PhoneNumber }

    public LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userTableCreationQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AND AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLE_USERS, COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONENUMBER, COLUMN_PASSWORD);
        sqLiteDatabase.execSQL(userTableCreationQuery);
    }

    public void addUser(String name, String email, String phoneNumber, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PHONENUMBER, phoneNumber);
        contentValues.put(COLUMN_PASSWORD, password);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
        sqLiteDatabase.close();
    }

    public String getPassword(String id, IDType idType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String getPassword = "";

        if (idType == IDType.Email) {
            getPassword = String.format("SELECT %S FROM %S WHERE %S = '%S'", COLUMN_PASSWORD, TABLE_USERS, COLUMN_EMAIL, id);
        }
        else if (idType == IDType.PhoneNumber) {
            getPassword = String.format("SELECT %S FROM %S WHERE %S = '%S'", COLUMN_PASSWORD, TABLE_USERS, COLUMN_PHONENUMBER, id);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(getPassword, null);
        cursor.moveToFirst();

        String password = "";

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)) != null) {
                password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            }
            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return password;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_USERS;
        sqLiteDatabase.execSQL(dropTableQuery);
        onCreate(sqLiteDatabase);
    }
}
