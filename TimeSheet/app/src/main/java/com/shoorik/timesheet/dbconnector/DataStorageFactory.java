package com.shoorik.timesheet.dbconnector;

import android.content.ContextWrapper;

import com.shoorik.timesheet.interfaces.IDataStorage;
import com.shoorik.timesheet.dbconnector.sharedpreferences.SharedPreferencesDataStorage;
import com.shoorik.timesheet.dbconnector.sqlite.SQLiteDataStorage;

import java.security.InvalidParameterException;

public class DataStorageFactory {

    public static IDataStorage GetDataStorage(ContextWrapper context, DataStorageType type) {

        if (type == DataStorageType.SharedPreferences) {
            return new SharedPreferencesDataStorage(context);
        }

        if (type == DataStorageType.SQLite) {
            return new SQLiteDataStorage(context);
        }

        throw new InvalidParameterException(type.toString());
    }
}
