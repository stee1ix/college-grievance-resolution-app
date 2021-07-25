package com.stee1ix.collegegrievancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReplyActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String name, subject, message, studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        name = getIntent().getStringExtra("name");
        subject = getIntent().getStringExtra("subject");
        message = getIntent().getStringExtra("message");
        studentId = getIntent().getStringExtra("studentId");

        TextView tvCompleteSub = findViewById(R.id.tvCompleteSub);
        tvCompleteSub.setText(subject);

        TextView tvCompleteMsg = findViewById(R.id.tvCompleteMsg);
        tvCompleteMsg.setText(message);

        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(name);

        db = FirebaseFirestore.getInstance();
    }

    public void submitReply() {
        EditText etReply = findViewById(R.id.etReply);

        String replyString = etReply.getText().toString();


        Map<String, String> reply = new HashMap<>();
        reply.put("reply", replyString);


        //update db with complaint
        DocumentReference documentReference = db.collection("complaints").document(studentId);
        documentReference
                .update(subject + ".reply", replyString)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("reply", "Reply added");
                        etReply.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("reply", "Error adding reply", e);
                    }
                });
    }

    public void confirmReply(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReplyActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setCancelable(false);
        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitReply();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}