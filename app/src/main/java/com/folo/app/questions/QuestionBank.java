package com.folo.app.questions;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    // ============================================
    // STANDALONE: Add/remove questions here ONLY
    // ============================================
    public static List<Question> getAllQuestions() {
        List<Question> q = new ArrayList<>();

        // DEMOGRAPHICS
        q.add(new Question("demo_name", "DEMOGRAPHICS", "Full name", "TEXT", null, true, "As on ID card"));
        q.add(new Question("demo_age", "DEMOGRAPHICS", "Age (years)", "NUMBER", null, true, "Current age"));
        q.add(new Question("demo_phone", "DEMOGRAPHICS", "Phone number", "TEXT", null, true, "Primary contact"));
        q.add(new Question("demo_marital", "DEMOGRAPHICS", "Marital status", "CHOICE",
            new String[]{"Single","Married","Divorced","Widowed"}, true, "Select one"));

        // PREGNANCY
        q.add(new Question("preg_gravida", "PREGNANCY", "Gravida (total pregnancies)", "NUMBER", null, true, "Including current"));
        q.add(new Question("preg_para", "PREGNANCY", "Para (live births)", "NUMBER", null, true, "After 24 weeks"));
        q.add(new Question("preg_edd", "PREGNANCY", "Expected delivery date", "DATE", null, true, "YYYY-MM-DD"));
        q.add(new Question("preg_prev_comp", "PREGNANCY", "Previous complications?", "YESNO", null, false, "Any past issues"));

        // HEALTH
        q.add(new Question("health_bp", "HEALTH", "Blood pressure", "TEXT", null, true, "e.g. 120/80"));
        q.add(new Question("health_weight", "HEALTH", "Weight (kg)", "NUMBER", null, true, "Current weight"));
        q.add(new Question("health_symptoms", "HEALTH", "Symptoms", "CHOICE",
            new String[]{"None","Bleeding","Headache","Fever","Swelling","Blurred vision"}, false, "Select all"));
        q.add(new Question("health_conditions", "HEALTH", "Conditions", "CHOICE",
            new String[]{"None","Hypertension","Diabetes","HIV","Anemia"}, false, "Select all"));

        // RISK
        q.add(new Question("risk_teenage", "RISK", "Teenage pregnancy (<20)?", "YESNO", null, true, "Age at conception"));
        q.add(new Question("risk_late", "RISK", "Late registration (>12 weeks)?", "YESNO", null, true, "Late ANC"));
        q.add(new Question("risk_transport", "RISK", "Emergency transport available?", "YESNO", null, true, "For emergencies"));

        // EMERGENCY
        q.add(new Question("emergency_contact", "EMERGENCY", "Emergency contact", "TEXT", null, true, "Name and phone"));
        q.add(new Question("emergency_facility", "EMERGENCY", "Nearest facility", "TEXT", null, true, "For referral"));

        return q;
    }

    public static List<Question> getByCategory(String category) {
        List<Question> filtered = new ArrayList<>();
        for (Question qu : getAllQuestions()) {
            if (qu.category.equals(category)) filtered.add(qu);
        }
        return filtered;
    }
}
