package com.goodow.web.android.persist;

import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import roboguice.util.Ln;

public class DatabasePersistor implements Persistor {

  private static class DatabaseHelper extends SQLiteOpenHelper {

    @Inject
    DatabaseHelper(final Context context) {
      super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
      db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _KEY + " TEXT, " + _VALUE + " TEXT);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
      Ln.w("Upgrading database from version " + oldVersion + " to " + newVersion
          + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
    }
  }

  private static final String DATABASE_NAME = "reader.db";

  private static final String TABLE_NAME = "map";

  private static final String _KEY = "_key";

  private static final String _VALUE = "_value";

  private final Provider<DatabaseHelper> databaseHelperProvider;

  @Inject
  DatabasePersistor(final Provider<DatabaseHelper> databaseHelperProvider) {
    this.databaseHelperProvider = databaseHelperProvider;
  }

  @Override
  public void clear() {
    DatabaseHelper helper = databaseHelperProvider.get();
    SQLiteDatabase db = helper.getWritableDatabase();
    db.delete(TABLE_NAME, null, null);
    helper.close();
  }

  @Override
  public boolean containsKey(final String key) {
    return get(key) != null;
  }

  @Override
  public String get(final String key) {
    String value = null;
    DatabaseHelper helper = databaseHelperProvider.get();
    SQLiteDatabase db = helper.getReadableDatabase();
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(TABLE_NAME);
    Cursor cur = qb.query(db, null, _KEY + " = '" + key + "'", null, null, null, null);
    if (cur.moveToFirst()) {
      value = cur.getString(cur.getColumnIndex(_VALUE));
    }
    cur.close();
    helper.close();
    return value;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public String put(final String key, final String value) {
    String previousValue = get(key);
    if (value == null) {
      if (previousValue != null) {
        remove(key);
      }
    } else {
      DatabaseHelper helper = databaseHelperProvider.get();
      SQLiteDatabase db = helper.getWritableDatabase();
      if (previousValue == null) {
        ContentValues values = new ContentValues();
        values.put(_KEY, key);
        values.put(_VALUE, value);
        db.insert(TABLE_NAME, null, values);
      } else {
        ContentValues values = new ContentValues();
        values.put(_VALUE, value);
        db.update(TABLE_NAME, values, _KEY + " = '" + key + "'", null);
      }
      helper.close();
    }
    return previousValue;
  }

  @Override
  public String remove(final String key) {
    String previousValue = get(key);
    DatabaseHelper helper = databaseHelperProvider.get();
    SQLiteDatabase db = helper.getWritableDatabase();
    db.delete(TABLE_NAME, _KEY + " = '" + key + "'", null);
    helper.close();
    return previousValue;
  }

  @Override
  public long size() {
    DatabaseHelper helper = databaseHelperProvider.get();
    SQLiteDatabase db = helper.getReadableDatabase();
    long size = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    helper.close();
    return size;
  }

}
