package vn.edu.greenwich.cw_1_sample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.models.Trip;

public class ResimaDAO {
    protected ResimaDbHelper resimaDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public ResimaDAO(Context context) {
        resimaDbHelper = new ResimaDbHelper(context);

        dbRead = resimaDbHelper.getReadableDatabase();
        dbWrite = resimaDbHelper.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        resimaDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // Trip.

    public long insertTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        return dbWrite.insert(TripEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Trip> getTripList(Trip trip, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (trip.getName() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_NAME + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }

            if (trip.getDestination() != null && !trip.getDestination().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DESTINATION + " = ?";
                conditionList.add(trip.getDestination());
            }

            if (trip.getDescription() != null && !trip.getDescription().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DESCRIPTION + " = ?";
                conditionList.add(trip.getDescription());
            }

            if (trip.getStartDate() != null && !trip.getStartDate().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_START_DATE + " = ?";
                conditionList.add(trip.getStartDate());
            }

            if (trip.getOwner() != -1) {
                selection += " AND " + TripEntry.COL_OWNER + " = ?";
                conditionList.add(String.valueOf(trip.getOwner()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Trip getTripById(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(trip.getId())};

        return dbWrite.update(TripEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteTrip(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(TripEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }

    protected ContentValues getTripValues(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(TripEntry.COL_NAME, trip.getName());
        values.put(TripEntry.COL_START_DATE, trip.getStartDate());
        values.put(TripEntry.COL_OWNER, trip.getOwner());
        values.put(TripEntry.COL_DESTINATION, trip.getDestination());
        values.put(TripEntry.COL_DESCRIPTION, trip.getDescription());

        return values;
    }

    protected ArrayList<Trip> getTripFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Trip> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Trip tripItem = new Trip();

            tripItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripEntry.COL_ID)));
            tripItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_NAME)));
            tripItem.setOwner(cursor.getInt(cursor.getColumnIndexOrThrow(TripEntry.COL_OWNER)));
            tripItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESTINATION)));
            tripItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESCRIPTION)));
            tripItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_START_DATE)));

            list.add(tripItem);
        }

        cursor.close();

        return list;
    }

    // Request.

    public long insertRequest(Request request) {
        ContentValues values = getRequestValues(request);

        return dbWrite.insert(RequestEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Request> getRequestList(Request request, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (request != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_CONTENT + " LIKE ?";
                conditionList.add("%" + request.getContent() + "%");
            }

            if (request.getAmount() != null && !request.getAmount().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_AMOUNT + " LIKE ?";
                conditionList.add("%" + request.getAmount() + "%");
            }

            if (request.getDate() != null && !request.getDate().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_DATE + " = ?";
                conditionList.add(request.getDate());
            }

            if (request.getTime() != null && !request.getTime().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_TIME + " = ?";
                conditionList.add(request.getTime());
            }

            if (request.getType() != null && !request.getType().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_TYPE + " = ?";
                conditionList.add(request.getType());
            }

            if (request.getTripId() != -1) {
                selection += " AND " + RequestEntry.COL_TRIP_ID + " = ?";
                conditionList.add(String.valueOf(request.getTripId()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getRequestFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Request getRequestById(long id) {
        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getRequestFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateRequest(Request request) {
        ContentValues values = getRequestValues(request);

        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(request.getId())};

        return dbWrite.update(RequestEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteRequest(long id) {
        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(RequestEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected ContentValues getRequestValues(Request request) {
        ContentValues values = new ContentValues();

        values.put(RequestEntry.COL_AMOUNT, request.getAmount());
        values.put(RequestEntry.COL_CONTENT, request.getContent());
        values.put(RequestEntry.COL_DATE, request.getDate());
        values.put(RequestEntry.COL_TIME, request.getTime());
        values.put(RequestEntry.COL_TYPE, request.getType());
        values.put(RequestEntry.COL_TRIP_ID, request.getTripId());

        return values;
    }

    protected ArrayList<Request> getRequestFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Request> list = new ArrayList<>();

        Cursor cursor = dbRead.query(RequestEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Request requestItem = new Request();

            requestItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RequestEntry.COL_ID)));
            requestItem.setContent(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_CONTENT)));
            requestItem.setAmount(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_AMOUNT)));
            requestItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_DATE)));
            requestItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_TIME)));
            requestItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_TYPE)));
            requestItem.setTripId(cursor.getLong(cursor.getColumnIndexOrThrow(RequestEntry.COL_TRIP_ID)));

            list.add(requestItem);
        }

        cursor.close();

        return list;
    }
}