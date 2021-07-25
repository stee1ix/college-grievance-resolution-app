package com.stee1ix.collegegrievancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TchrRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchr_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private String getEmail() {
        EditText regEmail = findViewById(R.id.regEmail);
        String email = regEmail.getText().toString();
        return email;
    }

    private String getPassword() {
        EditText password = findViewById(R.id.regPassword);
        String passwd = password.getText().toString();
        return passwd;
    }

    private void createTchrAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignUp", "createTchrWithEmail:success");

                            //update db with user
                            Map<String, String> teacher = new HashMap<>();
                            teacher.put("email", email);
//                            student.put("name", name)    put a name editText and import here

                            DocumentReference documentReference = db.collection("teacher").document(mAuth.getUid());

                            documentReference
                                    .set(teacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("addUserData", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("addUserData", "Error adding document", e);
                                        }
                                    });
                            //update UI with user
                            Intent intent = new Intent(TchrRegisterActivity.this, TchrListActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(TchrRegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //update UI with null
                        }
                    }
                });
    }

    public void tchrRegister(View view) {
        String email = getEmail();
        String password = getPassword();

        createTchrAccount(email, password);
    }
}