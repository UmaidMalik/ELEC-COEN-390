package com.elec_coen_390.uvme;

public class Config {
    public static  int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME =  "uv-reading.db";

    public static final String UV_TABLE_NAME = "uv_data";  /** UV Data Table */
    public static String UV_TABLE_NAME_MAX = "uv_data_max";
    public static String UV_TABLE_NAME_GRAPH = "uv_data_graph";

    public static String COLUMN_ID = "uv_id";
    public static String COLUMN_DAY = "day";
    public static String COLUMN_MONTH = "month";
    public static String COLUMN_YEAR = "year";

    public static String COLUMN_HOUR = "hour";
    public static String COLUMN_MIN = "min";
    public static String COLUMN_SEC = "sec";
    public static String COLUMN_UV_VALUE = "uv_value";


    public static final String COLUMN_UV_MAX_VALUE = "uv_max_value";


    public static final String COLUMN_UV_MAX_VALUE_GRAPH = "uv_max_value_graph";
    public static final String COLUMN_UV_AVERAGE_VALUE_GRAPH = "uv_average_value_graph";

}
