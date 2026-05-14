package com.folo.app.questions;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.folo.app.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.VH> {
    private final List<Question> questions;
    private final Map<String, String> answers = new HashMap<>();

    public QuestionAdapter(List<Question> questions) { this.questions = questions; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_question, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Question q = questions.get(pos);
        h.tvQ.setText((q.required ? "* " : "") + q.questionText);
        h.tvHelp.setText(q.helpText);
        h.tvHelp.setVisibility(q.helpText != null ? View.VISIBLE : View.GONE);
        h.container.removeAllViews();

        switch (q.answerType) {
            case "TEXT":
                EditText et = new EditText(h.itemView.getContext());
                styleEditText(et); et.setText(answers.get(q.id));
                et.addTextChangedListener(new SimpleWatcher(s -> answers.put(q.id, s.toString())));
                h.container.addView(et); break;
            case "NUMBER":
                EditText en = new EditText(h.itemView.getContext());
                styleEditText(en); en.setInputType(InputType.TYPE_CLASS_NUMBER);
                en.setText(answers.get(q.id));
                en.addTextChangedListener(new SimpleWatcher(s -> answers.put(q.id, s.toString())));
                h.container.addView(en); break;
            case "YESNO":
                RadioGroup rg = new RadioGroup(h.itemView.getContext());
                rg.setOrientation(RadioGroup.HORIZONTAL);
                RadioButton ry = new RadioButton(h.itemView.getContext()), rn = new RadioButton(h.itemView.getContext());
                ry.setText("Yes"); rn.setText("No");
                ry.setTextColor(h.itemView.getContext().getColor(R.color.text_primary));
                rn.setTextColor(h.itemView.getContext().getColor(R.color.text_primary));
                rg.addView(ry); rg.addView(rn);
                String ca = answers.get(q.id);
                if ("Yes".equals(ca)) ry.setChecked(true); if ("No".equals(ca)) rn.setChecked(true);
                rg.setOnCheckedChangeListener((g, cid) -> answers.put(q.id, cid == ry.getId() ? "Yes" : "No"));
                h.container.addView(rg); break;
            case "CHOICE":
                Spinner sp = new Spinner(h.itemView.getContext());
                ArrayAdapter<String> ad = new ArrayAdapter<>(h.itemView.getContext(), android.R.layout.simple_spinner_item, q.choices);
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(ad); sp.setBackgroundResource(R.drawable.bg_purple_edittext);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { answers.put(q.id, q.choices[pos]); }
                    public void onNothingSelected(AdapterView<?> p) {}
                });
                h.container.addView(sp); break;
            case "DATE":
                EditText ed = new EditText(h.itemView.getContext());
                styleEditText(ed); ed.setHint("YYYY-MM-DD"); ed.setText(answers.get(q.id));
                ed.addTextChangedListener(new SimpleWatcher(s -> answers.put(q.id, s.toString())));
                h.container.addView(ed); break;
        }
    }

    private void styleEditText(EditText et) {
        et.setBackgroundResource(R.drawable.bg_purple_edittext);
        et.setTextColor(et.getContext().getColor(R.color.text_primary));
        et.setHintTextColor(et.getContext().getColor(R.color.text_hint));
        et.setPadding(16, 16, 16, 16);
    }

    @Override public int getItemCount() { return questions.size(); }
    public Map<String, String> getAnswers() { return answers; }

    public boolean validateRequired() {
        for (Question q : questions) {
            if (q.required) {
                String a = answers.get(q.id);
                if (a == null || a.trim().isEmpty()) return false;
            }
        }
        return true;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvQ, tvHelp; LinearLayout container;
        VH(View v) { super(v); tvQ = v.findViewById(R.id.tvQuestion); tvHelp = v.findViewById(R.id.tvHelp); container = v.findViewById(R.id.answerContainer); }
    }

    interface SimpleCallback { void onText(String s); }
    static class SimpleWatcher implements android.text.TextWatcher {
        SimpleCallback cb; SimpleWatcher(SimpleCallback cb) { this.cb = cb; }
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
        public void onTextChanged(CharSequence s, int st, int b, int c) { cb.onText(s.toString()); }
        public void afterTextChanged(android.text.Editable s) {}
    }
}
