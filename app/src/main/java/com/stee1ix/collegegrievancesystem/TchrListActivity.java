package com.stee1ix.collegegrievancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class TchrListActivity extends AppCompatActivity {

    ArrayList<Complaint> complaints = new ArrayList<>();
    ListView lvTchr;

    Map<String, Object> complaintMap;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchr_list);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        CollectionReference collectionReference = db.collection("complaints");

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                documentSnapshot.getId();
                                Log.d("DocumentComp", String.valueOf(documentSnapshot.getData()));
                                complaintMap = documentSnapshot.getData();
                                for (Map.Entry<String, Object> entry : complaintMap.entrySet()) {
                                    Map<String, String> map = (Map<String, String>) entry.getValue();
                                    Complaint complaint = new Complaint(map.get("subject"), map.get("message"), map.get("reply"), map.get("studentId"), map.get("name"));
                                    complaint.setSubject(map.get("subject"));
                                    complaint.setMessage(map.get("message"));
                                    complaint.setReply(map.get("reply"));
                                    complaint.setStudentId(map.get("studentId"));
                                    complaint.setStudentName(map.get("name"));
                                    complaints.add(complaint);
                                }
                            }

                            ProgressBar progressBar = findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);

                            lvTchr = findViewById(R.id.lvTchr);
                            lvTchr.setVisibility(View.VISIBLE);
                            TeacherAdapter teacherAdapter = new TeacherAdapter();
                            lvTchr.setAdapter(teacherAdapter);


                            lvTchr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView reply = view.findViewById(R.id.tvReply);
                                    String replyString = reply.getText().toString();

                                    if (!replyString.equalsIgnoreCase("No reply yet")) {
                                        Toast.makeText(TchrListActivity.this, "Already Replied", Toast.LENGTH_SHORT).show();
                                    } else {
                                        TextView name = view.findViewById(R.id.tvStudentName);
                                        String nameString = name.getText().toString();

                                        TextView subject = view.findViewById(R.id.tvSubject);
                                        String subjectString = subject.getText().toString();

                                        TextView message = view.findViewById(R.id.tvMessage);
                                        String messageString = message.getText().toString();

                                        TextView studentId = view.findViewById(R.id.tvStudentId);
                                        String studentIdString = studentId.getText().toString();

                                        Intent intent = new Intent(TchrListActivity.this, ReplyActivity.class);
                                        intent.putExtra("name", nameString);
                                        intent.putExtra("subject", subjectString);
                                        intent.putExtra("message", messageString);
                                        intent.putExtra("studentId", studentIdString);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Log.w("DocumentComp", "Error in getting document", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        complaints.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        db = FirebaseFirestore.getInstance();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(TchrListActivity.this, TypeChooseActivity.class);
        startActivity(intent);
    }

    class TeacherAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return complaints.size();
        }

        @Override
        public Complaint getItem(int position) {
            return complaints.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = getLayoutInflater().inflate(R.layout.complaint_view, parent, false);

            TextView tvSubject = itemView.findViewById(R.id.tvSubject);
            TextView tvMessage = itemView.findViewById(R.id.tvMessage);
            TextView tvReply = itemView.findViewById(R.id.tvReply);
            TextView tvStudentId = itemView.findViewById(R.id.tvStudentId);
            TextView tvStudentName = itemView.findViewById(R.id.tvStudentName);

            tvSubject.setText(getItem(position).getSubject());
            tvMessage.setText(getItem(position).getMessage());
            tvReply.setText(getItem(position).getReply());
            tvStudentId.setText(getItem(position).getStudentId());
            tvStudentName.setText(getItem(position).getStudentName());

            return itemView;
        }
    }
}