package uqac.dim.uqac_scanner.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uqac.dim.uqac_scanner.Models.QrCodeModel;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String QR_TABLE = "QR_TABLE";
    public static final String QR_ID = "ID";
    public static final String COLUMN_QR_NAME = "QR_NAME";
    public static final String COLUMN_QR_URL = "QR_URL";
    public static final String COLUMN_QR_DESCRIPTION = "QR_DESCRIPTION";
    public static final String COLUMN_QR_IMAGE = "QR_IMAGE";
    public static final String COLUMN_QR_DATE_CREATE = "QR_DATE_CREATE";
    public static final String COLUMN_QR_DATE_EDIT = "QR_DATE_EDIT";
    public static final String COLUMN_QR_IS_SCANNED = "QR_IS_SCANNED";
    public DataBaseHelper(@Nullable Context context) {
        super(context, "qr.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + QR_TABLE + " ( " + QR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_QR_NAME + " TEXT, " + COLUMN_QR_URL + " TEXT, " +
                COLUMN_QR_DESCRIPTION + " TEXT, " + COLUMN_QR_IMAGE + " BLOB, " + COLUMN_QR_DATE_CREATE + " DATE, " + COLUMN_QR_DATE_EDIT + " DATE, " + COLUMN_QR_IS_SCANNED +" BOOL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + QR_TABLE;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean addCreatedQR(QrCodeModel qrCodeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_QR_NAME, qrCodeModel.getName());
        cv.put(COLUMN_QR_URL, qrCodeModel.getUrl());
        cv.put(COLUMN_QR_DESCRIPTION, qrCodeModel.getDescription());
        cv.put(COLUMN_QR_IMAGE, qrCodeModel.getCodeQR());
        cv.put(COLUMN_QR_DATE_CREATE, qrCodeModel.getDateCreation().getTime());
        cv.put(COLUMN_QR_DATE_EDIT, qrCodeModel.getDateEdit().getTime());
        cv.put(COLUMN_QR_IS_SCANNED, qrCodeModel.getIsScanned());

        long insert = db.insert(QR_TABLE, null, cv);

        return insert != -1;
    }


    public boolean edit(int id, QrCodeModel newQrCodeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QR_NAME, newQrCodeModel.getName());
        cv.put(COLUMN_QR_URL, newQrCodeModel.getUrl());
        cv.put(COLUMN_QR_DESCRIPTION, newQrCodeModel.getDescription());
        cv.put(COLUMN_QR_IMAGE, newQrCodeModel.getCodeQR());
        cv.put(COLUMN_QR_DATE_CREATE, newQrCodeModel.getDateCreation().getTime());
        cv.put(COLUMN_QR_DATE_EDIT, newQrCodeModel.getDateEdit().getTime());
        cv.put(COLUMN_QR_IS_SCANNED, newQrCodeModel.getIsScanned());

        // Update the row where the ID matches the provided id<
        long update = db.update(QR_TABLE, cv, QR_ID + " = " + id, null);

        return update != -1;
    }

    public List<QrCodeModel> getAllQR() {
        List<QrCodeModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + QR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int qrID = cursor.getInt(0);
                String qrName = cursor.getString(1);
                String qrUrl = cursor.getString(2);
                String qrDesc = cursor.getString(3);
                byte[] qrImage = cursor.getBlob(4);
                Date qrDateCreate = new Date(cursor.getLong(5)*1000);
                Date qrDateEdit = new Date(cursor.getLong(6)*1000);
                boolean qrIsScanned = cursor.getInt(7) == 1;


                returnList.add(new QrCodeModel(qrID,qrName,qrUrl,qrDesc, qrImage,qrDateCreate,qrDateEdit,qrIsScanned));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    //isScanned = 0 = False = The code QR was created
    //isScanned = 1 = True = The code QR was scanned
    public QrCodeModel getQR(Integer id) {
        QrCodeModel returnQR = new QrCodeModel();
        String query = "SELECT * FROM " + QR_TABLE + " WHERE " + QR_ID+ "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int qrID = cursor.getInt(0);
            String qrName = cursor.getString(1);
            String qrUrl = cursor.getString(2);
            String qrDesc = cursor.getString(3);
            byte[] qrImage = cursor.getBlob(4);
            Date qrDateCreate = new Date(cursor.getLong(5));
            Date qrDateEdit = new Date(cursor.getLong(6));
            boolean qrIsScanned = cursor.getInt(7) == 1;

            returnQR = new QrCodeModel(qrID,qrName,qrUrl,qrDesc, qrImage,qrDateCreate,qrDateEdit,qrIsScanned);
        }
        cursor.close();
        db.close();
        return  returnQR;
    }

    public List<QrCodeModel> getListQR(Integer isScanned) {
        List<QrCodeModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + QR_TABLE + " WHERE " + COLUMN_QR_IS_SCANNED + "=" + isScanned;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int qrID = cursor.getInt(0);
                String qrName = cursor.getString(1);
                String qrUrl = cursor.getString(2);
                String qrDesc = cursor.getString(3);
                byte[] qrImage = cursor.getBlob(4);
                Date qrDateCreate = new Date(cursor.getLong(5));
                Date qrDateEdit = new Date(cursor.getLong(6));
                boolean qrIsScanned = cursor.getInt(7) == 1;

                returnList.add(new QrCodeModel(qrID,qrName,qrUrl,qrDesc, qrImage,qrDateCreate,qrDateEdit,qrIsScanned));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteQRCode(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query  = "DELETE FROM "+ QR_TABLE + " WHERE " + QR_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
