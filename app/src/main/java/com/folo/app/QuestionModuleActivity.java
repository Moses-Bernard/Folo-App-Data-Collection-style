package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.questions.QuestionBank;
import com.folo.app.questions.Question;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    private LinearLayout layoutQuestions;
    private Button btnSave;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        layoutQuestions = findViewById(R.id.layoutQuestions);
        btnSave = findViewById(R.id.btnSaveQuestions);

        questions = QuestionBank.getQuestions();
        renderQuestions();

        btnSave.setOnClickListener(v -> saveAnswers());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void renderQuestions() {
        for (Question q : questions) {
            TextView tv = new TextView(this);
            tv.setText(q.text);
            tv.setTextColor(getResources().getColor(R.color.text_primary));
            tv.setTextSize(16);
            tv.setPadding(0, 16, 0, 8);
            layoutQuestions.addView(tv);

            // Simple text answer for now - can be expanded based on answer type
            android.widget.EditText et = new android.widget.EditText(this);
            et.setHint("Answer here...");
            et.setBackgroundResource(R.drawable.bg_purple_edittext);
            et.setPadding(16, 16, 16, 16);
            et.setTextColor(getResources().getColor(R.color.text_primary));
            et.setTag(q.id);
            layoutQuestions.addView(et);
        }
    }

    private void saveAnswers() {
        Toast.makeText(this, "Answers saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
