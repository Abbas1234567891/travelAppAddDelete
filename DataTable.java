package com.tcs.sqlitedatabaseexample;

import android.provider.BaseColumns;

/**
 * Created by 1256088 on 6/22/2016.
 */
public class DataTable implements BaseColumns {

    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "password";

    public static final String TABLE_NAME = "table_name";

    public static final String CREATE = "create table "+TABLE_NAME+ "("
            +_ID+ " integer primary key autoincrement ,"
            +MOBILE+ " text not null , "
            +PASSWORD+ " text not null );";
}
