package com.prathameshmore.feedbackapp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultMain extends AppCompatActivity {
    Button viewfeedback,clearfeedback,logout,export,exit;
    private ProgressDialog dialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    int i=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_main);
        firebaseAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Feedback");


        viewfeedback=findViewById(R.id.viewfeedback);
        clearfeedback=findViewById(R.id.clearfeedback);
        export=findViewById(R.id.exportfeedback);
        logout=findViewById(R.id.logout);
        exit=findViewById(R.id.exit);

        viewfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),TeacherFeedback.class);
                finish();
                startActivity(i);
            }
        });
        clearfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPopup();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Intent i =new Intent(getApplicationContext(),AuthActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(),"Loged Out Successfully..",Toast.LENGTH_SHORT).show();
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ExportToExcel.class);
                finish();
                startActivity(i);
               // exportToExcel();
               // Toast.makeText(getApplicationContext(),"Feedback Excel Stored in SDCard",Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                finish();
            }
        });
    }
    private void clearPopup() {

        final Dialog popupDialog = new Dialog(this);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.global_dialog);
        popupDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView tvClose = (TextView) popupDialog.findViewById(R.id.tvClose);

        Button tvClearFeedback = (Button)popupDialog.findViewById(R.id.tvClearFeedback);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });



        tvClearFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mdatabase.removeValue();
                Toast.makeText(getApplicationContext(),"Feedback Deleted...",Toast.LENGTH_SHORT).show();
                popupDialog.dismiss();
            }
        });

        try{
            popupDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupDialog.setCancelable(false);
        popupDialog.setCanceledOnTouchOutside(false);


}


    @Override
    public void onBackPressed() {
        firebaseAuth.signOut();
        finish();


    }

}
