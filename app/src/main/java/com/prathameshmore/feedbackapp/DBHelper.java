package com.prathameshmore.feedbackapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class DBHelper extends SQLiteOpenHelper {
    final static public String DATABASE_NAME="feedback1";
    final static public String TABLE_NAME="feedback";
    final static public String COLUMN_ROLL="ROLLNO";
    final static public String COLUMN_SUB="SUBJECT";
    final static public String COLUMN_QNA="QNA";
    final static public String COLUMN_QNB="QNB";
    final static public String COLUMN_QNC="QNC";
    final static public String COLUMN_QND="QND";
    final static public String COLUMN_QNE="QNE";
    WritableWorkbook workbook;
    WorkbookSettings wbSettings;
    WritableSheet sheet;
    int i;


    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
        Log.d("inconstructor","DATABASE CREATED..");



    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE="CREATE TABLE feedback(ROLLNO TEXT ,SUBJECT TEXT,QNA TEXT,QNB TEXT,QNC TEXT,QND TEXT,QNE TEXT)";
            db.execSQL(CREATE_TABLE);
        Log.d("ONCREATE","TABLE CREATED...");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS feedback");
            onCreate(db);
    }

    public long insertdata(String roll, String sub,String qna,String qnb,String qnc,String qnd,String qne) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ROLL,roll);
        cv.put(COLUMN_SUB,sub);
        cv.put(COLUMN_QNA,qna);
        cv.put(COLUMN_QNB,qnb);
        cv.put(COLUMN_QNC,qnc);
        cv.put(COLUMN_QND,qnd);
        cv.put(COLUMN_QNE,qne);

        long c=db.insert(TABLE_NAME,null,cv);
        db.close();
        return c;
    }

    public String getdata()
    {

        SQLiteDatabase db=getWritableDatabase();
        String[] cols={COLUMN_ROLL,COLUMN_SUB,COLUMN_QNA,COLUMN_QNB,COLUMN_QNC,COLUMN_QND,COLUMN_QNE};
        Cursor c=db.query(TABLE_NAME,cols,null,null,null,null,null);


        while(c.moveToNext())
        {
            int roll=c.getInt(c.getColumnIndex(COLUMN_ROLL));
            String sub=c.getString(c.getColumnIndex(COLUMN_SUB));
            String qna=c.getString(c.getColumnIndex(COLUMN_QNA));
            String qnb=c.getString(c.getColumnIndex(COLUMN_QNB));
            String qnc=c.getString(c.getColumnIndex(COLUMN_QNC));
            String qnd=c.getString(c.getColumnIndex(COLUMN_QND));
            String qne=c.getString(c.getColumnIndex(COLUMN_QNE));




        }
        return "x";
    }

    public void exportToExcel(WritableSheet sheet) {

        try {
            SQLiteDatabase db=getWritableDatabase();
            String[] cols={COLUMN_ROLL,COLUMN_SUB,COLUMN_QNA,COLUMN_QNB,COLUMN_QNC,COLUMN_QND,COLUMN_QNE};
            Cursor c=db.query(TABLE_NAME,cols,null,null,null,null,null);


            while(c.moveToNext())
            {
                String roll=c.getString(c.getColumnIndex(COLUMN_ROLL));
                String sub=c.getString(c.getColumnIndex(COLUMN_SUB));
                String qna=c.getString(c.getColumnIndex(COLUMN_QNA));
                String qnb=c.getString(c.getColumnIndex(COLUMN_QNB));
                String qnc=c.getString(c.getColumnIndex(COLUMN_QNC));
                String qnd=c.getString(c.getColumnIndex(COLUMN_QND));
                String qne=c.getString(c.getColumnIndex(COLUMN_QNE));

                sheet.addCell(new Label(0, i,String.valueOf(i) )); // column and row
                sheet.addCell(new Label(1, i,roll)); // column and row
                sheet.addCell(new Label(2, i,sub));
                sheet.addCell(new Label(3, i,qna));
                sheet.addCell(new Label(4, i,qnb));
                sheet.addCell(new Label(5, i,qnc));
                sheet.addCell(new Label(6, i,qnd));
                sheet.addCell(new Label(7, i,qne));
                i++;


            }


        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }


    }

    public void createTable()
    {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS feedback");
        String CREATE_TABLE="CREATE TABLE feedback(ROLLNO TEXT ,SUBJECT TEXT,QNA TEXT,QNB TEXT,QNC TEXT,QND TEXT,QNE TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    public SQLiteDatabase getDBReference()
    {
        SQLiteDatabase db=getWritableDatabase();
        return db;

    }


}
