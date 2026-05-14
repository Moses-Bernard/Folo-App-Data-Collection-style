package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Woman;
import com.folo.app.role.RoleManager;
import java.util.List;
import java.util.function.Consumer;

public class WomanListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woman_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RoleManager roleManager = new RoleManager(this);

        AppDatabase.getInstance(this).womanDao().getAllWomen()
            .observe(this, women -> {
                recyclerView.setAdapter(new WomanAdapter(women, roleManager, this::onWomanClick));
            });
    }

    private void onWomanClick(Woman woman) {
        Intent intent = new Intent(this, WomanDetailActivity.class);
        intent.putExtra("woman_id", woman.id);
        startActivity(intent);
    }

    public static class WomanAdapter extends RecyclerView.Adapter<WomanAdapter.ViewHolder> {
        private final List<Woman> women;
        private final RoleManager roleManager;
        private final Consumer<Woman> onClick;

        WomanAdapter(List<Woman> women, RoleManager roleManager, Consumer<Woman> onClick) {
            this.women = women;
            this.roleManager = roleManager;
            this.onClick = onClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_woman, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Woman w = women.get(position);
            holder.tvName.setText(w.fullName);
            holder.tvPhone.setText(w.phone);
            holder.tvEdd.setText("EDD: " + (w.edd != null ? w.edd : "N/A"));
            holder.tvRisk.setText(w.riskLevel);
            holder.tvRisk.setTextColor(holder.itemView.getContext().getColor(
                "HIGH".equals(w.riskLevel) ? R.color.danger_red : R.color.success_green));

            boolean canEdit = roleManager.hasPermission("EDIT") ||
                (roleManager.hasPermission("EDIT_OWN") &&
                 w.registeredBy.equals(roleManager.getCurrentRole().getDisplayName()));
            holder.btnEdit.setVisibility(canEdit ? View.VISIBLE : View.GONE);

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), EditWomanActivity.class);
                intent.putExtra("woman_id", w.id);
                holder.itemView.getContext().startActivity(intent);
            });

            holder.itemView.setOnClickListener(v -> onClick.accept(w));
        }

        @Override
        public int getItemCount() {
            return women.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPhone, tvEdd, tvRisk;
            Button btnEdit;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvPhone = itemView.findViewById(R.id.tvPhone);
                tvEdd = itemView.findViewById(R.id.tvEdd);
                tvRisk = itemView.findViewById(R.id.tvRisk);
                btnEdit = itemView.findViewById(R.id.btnEdit);
            }
        }
    }
}
