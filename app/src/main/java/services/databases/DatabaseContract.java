package services.databases;

import android.provider.BaseColumns;

/**
 * Created by Slawek on 2015-07-15.
 */
public class DatabaseContract {

    public DatabaseContract() {
    }

    public static abstract class ConfigurationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Configurations";
        public static final String COLUMN_KEY = "Key";
        public static final String COLUMN_VALUE = "Value";
    }

    public static abstract class TicketEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tickets";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_CUSTOMER_ID = "CustomerId";
        public static final String COLUMN_EXECUTOR_ID = "ExecutorId";
        public static final String COLUMN_CREATOR_ID = "CreatorId";
    }
}