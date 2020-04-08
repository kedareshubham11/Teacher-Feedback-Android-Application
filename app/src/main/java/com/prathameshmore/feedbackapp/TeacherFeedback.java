package com.prathameshmore.feedbackapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TeacherFeedback extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    private ListView datalist;
    private ArrayList<String> data=new ArrayList();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feedback);
        firebaseAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Feedback");


        datalist=(ListView) findViewById(R.id.data_list);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);


        arrayAdapter.notifyDataSetChanged();

        datalist.setAdapter(arrayAdapter);
        mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                data.add(dataSnapshot.getKey());
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        data.add(ds.getKey());
                        for (DataSnapshot dds : ds.getChildren()) {
                            data.add(dds.getValue().toString());


                        }
                    }
                arrayAdapter.notifyDataSetChanged();
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

    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ResultMain.class);
        finish();
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this,AuthActivity.class));
                Toast.makeText(this, "Sign outed", Toast.LENGTH_SHORT).show();
                break;


            case R.id.refresh:
                finish();
                startActivity(new Intent(this,TeacherFeedback.class));
                break;


            case R.id.about:

                AlertDialog.Builder ad=new AlertDialog.Builder(this);
                ad.setTitle("About...");
                ad.setMessage("FEEDBACK APP\nVersion 1.0.3 \nDeveloped by KD ");
                ad.setCancelable(false);
                ad.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

