package com.stee1ix.collegegrievancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypeChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_choose);
    }

    public void openTeacherLogin(View view) {
        Intent intent = new Intent(this,TeacherLoginActivity.class);
        startActivity(intent);

    }

    public void openStudentLogin(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}