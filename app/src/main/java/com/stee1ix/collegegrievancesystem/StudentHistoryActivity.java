package com.stee1ix.collegegrievancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class StudentHistoryActivity extends AppCompatActivity {

    ArrayList<Complaint> complaints = new ArrayList<>();
    ListView lvHistory;

    Map<String, Object> complaintMap;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_history);

        db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        DocumentReference documentReference = db.document("complaints/" + userId);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                complaintMap = Objects.requireNonNull(task.getResult()).getData();
                                Log.d("Complaints", String.valueOf(complaintMap));
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

                                ProgressBar progressBar = findViewById(R.id.progressBar);
                                progressBar.setVisibility(View.GONE);

                                lvHistory = findViewById(R.id.lvHistory);
                                lvHistory.setVisibility(View.VISIBLE);
                                ComplaintAdapter complaintAdapter = new ComplaintAdapter();
                                lvHistory.setAdapter(complaintAdapter);
                            } else {
                                TextView noComplaints = findViewById(R.id.noComplaints);
                                noComplaints.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.w("Complaints", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    class ComplaintAdapter extends BaseAdapter {

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