package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.folo.app.questions.QuestionBank;
import com.folo.app.questions.QuestionAdapter;

public class QuestionModuleActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_question_module);
        RecyclerView rv = findViewById(R.id.rvQuestions);
        rv.setLayoutManager(new LinearLayoutManager(this));
        QuestionAdapter adapter = new QuestionAdapter(QuestionBank.getAllQuestions());
        rv.setAdapter(adapter);
        Button btnSubmit = findViewById(R.id.btnSubmitAnswers);
        btnSubmit.setOnClickListener(v -> {
            if (adapter.validateRequired()) {
                Toast.makeText(this, "Answers submitted: " + adapter.getAnswers().size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please answer all required questions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
