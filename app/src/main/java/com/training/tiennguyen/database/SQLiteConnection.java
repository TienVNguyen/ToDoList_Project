/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.training.tiennguyen.constants.DatabaseConstants;
import com.training.tiennguyen.model.ToDoElement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class supports to execute any actions related to database <br>
 * Such as: Insert, Edit, Remove, Query, etc.
 *
 * @author TienNguyen
 */
public class SQLiteConnection extends SQLiteOpenHelper {
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public SQLiteConnection(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public SQLiteConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     * <p/>
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context      to use to open or create the database
     * @param name         of the database file, or null for an in-memory database
     * @param factory      to use for creating cursor objects, or null for the default
     * @param version      number of the database (starting at 1); if the database is older,
     *                     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     */
    public SQLiteConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query:
        // CREATE TABLE TODOLIST ( TITLE PRIMARY KEY, DETAILS KEY, PRIORITY INTEGER KEY ) ;
        StringBuilder queryCreateTable = new StringBuilder();
        queryCreateTable.append(DatabaseConstants.CREATE_TABLE);
        queryCreateTable.append(DatabaseConstants.TABLE_TODOLIST);
        queryCreateTable.append(DatabaseConstants.OPEN_BRACKETS);
        queryCreateTable.append(DatabaseConstants.KEY_TITLE);
        queryCreateTable.append(DatabaseConstants.PRIMARY_STRING_KEY);
        queryCreateTable.append(DatabaseConstants.COMMA);
        queryCreateTable.append(DatabaseConstants.KEY_DETAILS);
        queryCreateTable.append(DatabaseConstants.STRING_KEY);
        queryCreateTable.append(DatabaseConstants.COMMA);
        queryCreateTable.append(DatabaseConstants.KEY_PRIORITY);
        queryCreateTable.append(DatabaseConstants.INTEGER_KEY);
        queryCreateTable.append(DatabaseConstants.CLOSE_BRACKETS);
        queryCreateTable.append(DatabaseConstants.SEMICOLON);
        db.execSQL(queryCreateTable.toString());
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table query
        // DROP TABLE IF EXISTS TODOLIST;
        StringBuilder queryDropTable = new StringBuilder();
        queryDropTable.append(DatabaseConstants.DROP_TABLE_EXISTED);
        queryDropTable.append(DatabaseConstants.TABLE_TODOLIST);
        queryDropTable.append(DatabaseConstants.SEMICOLON);
        db.execSQL(queryDropTable.toString());

        // Create table
        onCreate(db);
    }

    /**
     * This function will check if that element's existed or already registered
     *
     * @param toDoObject the model
     * @return existedFlag boolean
     */
    public boolean checkElementExists(ToDoElement toDoObject) {
        // Get the lock
        SQLiteDatabase db = this.getReadableDatabase();

        // Select table columns
        String[] whereArgs = new String[]{ DatabaseConstants.KEY_TITLE };

        // Generate selection
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(DatabaseConstants.KEY_TITLE);
        whereClause.append(DatabaseConstants.SELECTION_IS);

        // Set value to selection
        String[] selectionArgs = new String[]{toDoObject.getTitle()};

        // Get the result
        Cursor cursor = db.query(DatabaseConstants.TABLE_TODOLIST, whereArgs, whereClause.toString(), selectionArgs, null, null, null, null);

        // Return existedFlag for notifying
        return cursor != null && cursor.getCount() > 0;
    }

    /**
     * This function will insert a new record to a table inside of database
     *
     * @param toDoObject the model
     * @return insertFlag long. If there is an issue, it will return -1.
     */
    public long insertElement(ToDoElement toDoObject) {
        // Get the lock
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Prepare the value
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.KEY_TITLE, toDoObject.getTitle());
        values.put(DatabaseConstants.KEY_DETAILS, toDoObject.getDetails());
        values.put(DatabaseConstants.KEY_PRIORITY, toDoObject.getPriority());

        // Execute insert
        long insertFlag = sqLiteDatabase.insert(DatabaseConstants.TABLE_TODOLIST, null, values);

        // Close connection
        sqLiteDatabase.close();

        // Return insertFlag for notifying
        return insertFlag;
    }

    /**
     * This function will delete an old record from a table inside of database
     *
     * @param toDoObject the model
     * @return deleteFlag int. If there is an issue, it will return -1.
     */
    public int deleteElement(ToDoElement toDoObject) {
        // Get the lock
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Select table columns
        String[] whereArgs = new String[]{toDoObject.getTitle()};

        // Generate condition
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(DatabaseConstants.KEY_TITLE);
        whereClause.append(DatabaseConstants.SELECTION_IS);

        // Execute delete
        int deleteFlag = sqLiteDatabase.delete(DatabaseConstants.TABLE_TODOLIST, whereClause.toString(), whereArgs);

        // Close connection
        sqLiteDatabase.close();

        // Return deleteFlag for notifying
        return deleteFlag;
    }

    /**
     * This function will update an old record from a table inside of database
     *
     * @param toDoObject the model
     * @return updateFlag int. If there is an issue, it will return -1.
     */
    public int updateElement(ToDoElement toDoObject) {
        // Get the lock
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Prepare the value
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.KEY_TITLE, toDoObject.getTitle());
        contentValues.put(DatabaseConstants.KEY_DETAILS, toDoObject.getDetails());
        contentValues.put(DatabaseConstants.KEY_PRIORITY, toDoObject.getPriority());

        // Generate condition
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(DatabaseConstants.KEY_TITLE);
        whereClause.append(DatabaseConstants.SELECTION_IS);

        // Select table columns
        String[] whereArgs = new String[]{toDoObject.getTitle()};

        // Execute insert
        int updateFlag = sqLiteDatabase.update(DatabaseConstants.TABLE_TODOLIST, contentValues, whereClause.toString(), whereArgs);

        // Close connection
        sqLiteDatabase.close();

        // Return insertFlag for notifying
        return updateFlag;
    }

    /**
     * This function will select count(*) of record(s) of table inside of database
     *
     * @return resultCount. If there is no record, it will return -1.
     */
    public int selectCountToDoObjects() {
        // Get the lock
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // Select table query
        StringBuilder querySelect = new StringBuilder();
        querySelect.append(DatabaseConstants.SELECT_COUNT);
        querySelect.append(DatabaseConstants.TABLE_TODOLIST);
        querySelect.append(DatabaseConstants.SEMICOLON);

        // Get the result
        Cursor cursor = sqLiteDatabase.rawQuery(querySelect.toString(), null);
        int resultCount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            resultCount = cursor.getInt(0);
        } else {
            resultCount = -1;
        }

        // Close connection
        cursor.close();
        sqLiteDatabase.close();

        // Return resultCount for notifying
        return resultCount;
    }

    /**
     * This function will select all record(s) of table inside of database
     *
     * @return resultSelect List<ToDoElement>. A list of ToDoElement in the database.
     */
    public List<ToDoElement> selectAllToDoObjects() {
        // Get the lock
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // Select table columns
        String[] columns = new String[]{ DatabaseConstants.KEY_TITLE, DatabaseConstants.KEY_DETAILS, DatabaseConstants.KEY_PRIORITY };

        // Get the result through querying database
        Cursor cursor = sqLiteDatabase.query(DatabaseConstants.TABLE_TODOLIST, columns, null, null, null, null, null);
        List<ToDoElement> resultSelect = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Assign value
                ToDoElement toDoObjectTemp = new ToDoElement();
                toDoObjectTemp.setTitle(cursor.getString(0));
                toDoObjectTemp.setDetails(cursor.getString(1));
                toDoObjectTemp.setPriority(cursor.getInt(2));
                resultSelect.add(toDoObjectTemp);
            } while (cursor.moveToNext());
        }

        // Close connection
        cursor.close();
        sqLiteDatabase.close();

        // Return resultSelect for notifying
        return resultSelect;
    }
}
