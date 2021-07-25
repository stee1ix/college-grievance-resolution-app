package com.stee1ix.collegegrievancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TeacherLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        mAuth = FirebaseAuth.getInstance();
    }

    private String getTchrEmail() {
        EditText email = findViewById(R.id.etTchrEmail);
        String emailString = email.getText().toString();
        return emailString;
    }

    private String getTchrPassword() {
        EditText password = findViewById(R.id.etTchrPassword);
        String passwd = password.getText().toString();
        return passwd;
    }


    private void signInTchr(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed users info
                            Log.d("LogIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI with user
                            Intent intent = new Intent(TeacherLoginActivity.this, TchrListActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LogIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(TeacherLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //update UI with null
                        }
                    }
                });
    }

    public void loginTeacher(View view) {
        Button loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getTchrEmail();
                String password = getTchrPassword();
                signInTchr(email, password);
            }
        });
    }

    public void RegisterTchr(View view) {
        Intent intent = new Intent(TeacherLoginActivity.this, TchrRegisterActivity.class);
        startActivity(intent);
    }
}