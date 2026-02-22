package adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdmn.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final Context context;
    private final OnTaskClickListener listener;
    private final OnTaskCheckedChangeListener checkedChangeListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskLongClick(Task task);
    }

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(Task task, boolean isChecked);
    }

    public TaskAdapter(List<Task> taskList, Context context,
                       OnTaskClickListener listener,
                       OnTaskCheckedChangeListener checkedChangeListener) {
        this.taskList = taskList;
        this.context = context;
        this.listener = listener;
        this.checkedChangeListener = checkedChangeListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateList(List<Task> newList) {
        this.taskList = newList;
        notifyDataSetChanged();
    }

    // La classe DOIT être publique ou package-private (sans modificateur)
    // pour être accessible depuis TaskAdapter
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvDate, tvPriority, tvCategory, tvStatus;
        private CheckBox checkBox;
        private CardView cardView;
        private View statusIndicator;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.taskTitle);
            tvDescription = itemView.findViewById(R.id.taskDescription);
            tvDate = itemView.findViewById(R.id.taskDate);
            tvPriority = itemView.findViewById(R.id.taskPriority);
            tvCategory = itemView.findViewById(R.id.taskCategory);
            tvStatus = itemView.findViewById(R.id.taskStatus);
            checkBox = itemView.findViewById(R.id.taskCheckbox);
            cardView = itemView.findViewById(R.id.cardView);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);

            // Gestion des clics sur la carte
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onTaskClick(taskList.get(position));
                    }
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onTaskLongClick(taskList.get(position));
                        return true;
                    }
                    return false;
                }
            });

            // Listener pour la checkbox
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && checkedChangeListener != null) {
                        boolean isChecked = ((CheckBox) v).isChecked();
                        Task task = taskList.get(position);
                        task.setCompleted(isChecked);
                        checkedChangeListener.onTaskCheckedChanged(task, isChecked);

                        // Mettre à jour l'affichage de manière sécurisée
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemChanged(position);
                            }
                        });
                    }
                }
            });
        }

        public void bind(Task task) {
            tvTitle.setText(task.getTitle());
            tvDescription.setText(task.getDescription());
            tvCategory.setText(task.getCategory());

            // Formater la date avec icône
            tvDate.setText("📅 " + task.getDate());

            // Éviter les boucles infinies
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(task.isCompleted());

            // Vérifier si la tâche est en retard
            boolean isOverdue = false;
            if (!task.isCompleted()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date taskDate = sdf.parse(task.getDate());
                    Date today = new Date();
                    if (taskDate != null && taskDate.before(today)) {
                        isOverdue = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Gestion de la priorité
            String priorityText = "";
            int priorityColor = 0;

            switch (task.getPriority()) {
                case HIGH:
                    priorityText = "🔴 Haute";
                    priorityColor = ContextCompat.getColor(context, R.color.high_priority);
                    break;
                case MEDIUM:
                    priorityText = "🟠 Moyenne";
                    priorityColor = ContextCompat.getColor(context, R.color.medium_priority);
                    break;
                case LOW:
                    priorityText = "🟢 Basse";
                    priorityColor = ContextCompat.getColor(context, R.color.low_priority);
                    break;
            }

            tvPriority.setText(priorityText);
            tvPriority.setBackgroundColor(priorityColor);

            // Gestion visuelle selon le statut
            if (task.isCompleted()) {
                // ✅ TÂCHE TERMINÉE
                tvTitle.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                tvDescription.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                tvCategory.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));

                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.completed_task_bg));

                if (statusIndicator != null) {
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.success));
                }

                if (tvStatus != null) {
                    tvStatus.setText("✅ Terminée");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.success));
                    tvStatus.setVisibility(View.VISIBLE);
                }

            } else if (isOverdue) {
                // ⚠️ TÂCHE EN RETARD
                tvTitle.setTextColor(ContextCompat.getColor(context, R.color.error));
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                tvDescription.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                tvDate.setTextColor(ContextCompat.getColor(context, R.color.error));

                cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

                if (statusIndicator != null) {
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.error));
                }

                if (tvStatus != null) {
                    tvStatus.setText("⚠️ En retard");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.error));
                    tvStatus.setVisibility(View.VISIBLE);
                }

            } else {
                // ⏳ TÂCHE EN COURS
                tvTitle.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                tvDescription.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));

                cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

                if (statusIndicator != null) {
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
                }

                if (tvStatus != null) {
                    tvStatus.setText("⏳ En cours");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.warning));
                    tvStatus.setVisibility(View.VISIBLE);
                }
            }

            // Restaurer le listener après le binding
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && checkedChangeListener != null) {
                        boolean isChecked = ((CheckBox) v).isChecked();
                        Task task = taskList.get(position);
                        task.setCompleted(isChecked);
                        checkedChangeListener.onTaskCheckedChanged(task, isChecked);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemChanged(position);
                            }
                        });
                    }
                }
            });
        }
    }
}