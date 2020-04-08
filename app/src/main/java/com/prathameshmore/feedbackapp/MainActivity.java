package com.prathameshmore.feedbackapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int SPLASHSCREEN_TIMEOUT=4000;

    private FirebaseAuth firebaseAuth;
    private Spinner subjects;
    private DatabaseReference mDatabaseReference;
    private Button btnSumbit;
    private EditText rollNumber;
    private String rollNum;
    String selectedSubject;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;
    RadioButton radioButtonD;
    RadioButton radioButtonE;
    RadioGroup radioGroupA;
    RadioGroup radioGroupB;
    RadioGroup radioGroupC;
    RadioGroup radioGroupD;
    RadioGroup radioGroupE;
    TextView tt;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSumbit = findViewById(R.id.btn_submit);
        rollNumber = findViewById(R.id.et_rollno);

        radioGroupA = findViewById(R.id.rg_a);
        radioGroupB = findViewById(R.id.rg_b);
        radioGroupC = findViewById(R.id.rg_c);
        radioGroupD = findViewById(R.id.rg_d);
        radioGroupE = findViewById(R.id.rg_e);
        firebaseAuth = FirebaseAuth.getInstance();

        subjects = findViewById(R.id.spinner_subjects);
        List<String> subjectsList = new ArrayList<String>();
        subjectsList.add("Advance Java");
        subjectsList.add("Software Engineering");
        subjectsList.add("Design and Analysis Of Algorithm");
        subjectsList.add("System Programming");
        subjectsList.add("Network Security");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,subjectsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjects.setAdapter(arrayAdapter);


        subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedRG_A = radioGroupA.getCheckedRadioButtonId();
                int selectedRG_B = radioGroupB.getCheckedRadioButtonId();
                int selectedRG_C = radioGroupC.getCheckedRadioButtonId();
                int selectedRG_D = radioGroupD.getCheckedRadioButtonId();
                int selectedRG_E = radioGroupE.getCheckedRadioButtonId();

                radioButtonA = findViewById(selectedRG_A);
                radioButtonB = findViewById(selectedRG_B);
                radioButtonC = findViewById(selectedRG_C);
                radioButtonD = findViewById(selectedRG_D);
                radioButtonE = findViewById(selectedRG_E);

                rollNum = rollNumber.getText().toString();
                if (TextUtils.isEmpty(rollNum)) {
                    rollNumber.setError("Please enter roll number");
                }
                else if(radioGroupA.getCheckedRadioButtonId()==-1 || radioGroupB.getCheckedRadioButtonId() == -1 || radioGroupC.getCheckedRadioButtonId() == -1 || radioGroupD.getCheckedRadioButtonId() == -1 || radioGroupE.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getApplicationContext(),"Answer All Questions...",Toast.LENGTH_SHORT).show();
                }
                else {

                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(rollNum);
                    DatabaseReference subDatabaseReference = mDatabaseReference.child(selectedSubject);
                    subDatabaseReference.child("A").setValue(radioButtonA.getText());
                    subDatabaseReference.child("B").setValue(radioButtonB.getText());
                    subDatabaseReference.child("C").setValue(radioButtonC.getText());
                    subDatabaseReference.child("D").setValue(radioButtonD.getText());
                    subDatabaseReference.child("E").setValue(radioButtonE.getText());
                    Toast.makeText(MainActivity.this, "Feedback is updated", Toast.LENGTH_LONG).show();
                }



            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Intent x =getIntent();

        String flag="";
        flag=x.getStringExtra("flag");
        if ((firebaseUser == null) && (flag == null)) {
            Intent startAuthActivity = new Intent(MainActivity.this,AuthActivity.class);
            startActivity(startAuthActivity);
            finish();
        }


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
                startActivity(new Intent(this,AuthActivity.class));
                finish();
                Toast.makeText(this, "Sign outed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.refresh:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.about:
                AlertDialog.Builder ad=new AlertDialog.Builder(this);
                ad.setTitle("About...");
                ad.setMessage("FEEDBACK APP\nVersion 1.0.3 \n Develop by KD");
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
}
