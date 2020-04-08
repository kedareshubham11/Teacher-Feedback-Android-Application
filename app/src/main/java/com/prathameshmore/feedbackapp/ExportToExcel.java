package com.prathameshmore.feedbackapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExportToExcel extends AppCompatActivity {
    TextView tv;
    Button b,goback;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    private int x,y;
    public WritableWorkbook workbook;
    WorkbookSettings wbSettings;
    public WritableSheet sheet;
    Vector<String> v;
    int i;
    int r;
    DBHelper database;
    String rollno;
    String subject;
    String[] qna;
    long c;

    final static public String DATABASE_NAME="feedback1";
    final static public String TABLE_NAME="feedback";
    final static public String COLUMN_ROLL="ROLLNO";
    final static public String COLUMN_SUB="SUBJECT";
    final static public String COLUMN_QNA="QNA";
    final static public String COLUMN_QNB="QNB";
    final static public String COLUMN_QNC="QNC";
    final static public String COLUMN_QND="QND";
    final static public String COLUMN_QNE="QNE";



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_to_excel);
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4);
        }
        b=findViewById(R.id.button2);
        qna=new String[5];
        tv=findViewById(R.id.txt);
        goback=findViewById(R.id.goback);
        tv.setText("......................");
        v=new Vector();
        i=0;
        database=new DBHelper(this);


        firebaseAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Feedback");


        database.createTable();
        mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                rollno=dataSnapshot.getKey().toString();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    subject=ds.getKey().toString();

                    for (DataSnapshot dds : ds.getChildren()) {

                        qna[i]=dds.getValue().toString();
                       i++;



                    }
                    c=database.insertdata(rollno,subject,qna[0],qna[1],qna[2],qna[3],qna[4]);
                    i=0;
                }




                /*

                try {


                        String x = String.valueOf(i);
                        sheet.addCell(new Label(0, i, x));
                        String roll_no=dataSnapshot.getKey();
                        sheet.addCell(new Label(1, i,roll_no));

                        for (DataSnapshot ds:dataSnapshot.getChildren()) {
                            String subject =ds.getKey();
                            sheet.addCell(new Label(2,i, subject));
                            int c = 3;
                            for (DataSnapshot dds:ds.getChildren()) {

                                String Qns = dds.getValue().toString();
                                sheet.addCell(new Label(c, i, Qns));
                                c = c + 1;


                            }
                        }

                        i++;





                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }
                */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       // String[] array = v.toArray(new String[v.size()]);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToExcel();
                r=1;
                try {
                    SQLiteDatabase db=database.getDBReference();
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
                        Log.d("DB",roll+sub+qna+qnb+qnc+qnd+qne);
                        sheet.addCell(new Label(0, r,String.valueOf(r) )); // column and row
                        sheet.addCell(new Label(1, r,roll)); // column and row
                        sheet.addCell(new Label(2, r,sub));
                        sheet.addCell(new Label(3, r,qna));
                        sheet.addCell(new Label(4, r,qnb));
                        sheet.addCell(new Label(5, r,qnc));
                        sheet.addCell(new Label(6, r,qnd));
                        sheet.addCell(new Label(7, r,qne));
                        r++;

                    }
                    workbook.write();
                    workbook.close();



                } catch (SQLiteDatabaseLockedException e) {
                    e.printStackTrace();
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                b.setText("DATA EXPORTED");
                b.setEnabled(false);
                b.setTextColor(Color.GRAY);
                tv.setText("Feedback successfully stored in SDCard.");
                Toast.makeText(getApplicationContext(),"..DATA Exported..",Toast.LENGTH_SHORT).show();
            }

        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ResultMain.class);
                finish();
                startActivity(i);
            }
        });

        /*
            for(int a=0;a<5;a++) {
                String x = String.valueOf(i);
                sheet.addCell(new Label(0, i, x));
                sheet.addCell(new Label(1, i, x));

                for (int h=0;h<2;h++) {
                    String subject = "xyz";
                    sheet.addCell(new Label(2,i, subject));
                    int c = 3;
                    for (int k=0;k<5;k++) {

                        String Qns = "NNN";
                        sheet.addCell(new Label(c, i, Qns));
                        c = c + 1;


                    }
                }
            i++;
            }
            */






    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ResultMain.class);
        finish();
        startActivity(i);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void exportToExcel() {

        final String fileName = "Feedback.xls";

        //Saving file in external storage

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            sheet = workbook.createSheet("First sheet", 0);

            try {
                sheet.addCell(new Label(0, 0, "Sr.no")); // column and row
                sheet.addCell(new Label(1, 0, "Roll_No")); // column and row
                sheet.addCell(new Label(2, 0, "Subject"));
                sheet.addCell(new Label(3, 0, "Qn A"));
                sheet.addCell(new Label(4, 0, "Qn B"));
                sheet.addCell(new Label(5, 0, "Qn C"));
                sheet.addCell(new Label(6, 0, "Qn D"));
                sheet.addCell(new Label(7, 0, "Qn E"));
            }


            catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
