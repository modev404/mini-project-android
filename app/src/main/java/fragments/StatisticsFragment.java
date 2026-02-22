package fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectdmn.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;



import java.util.List;

import helpers.DatabaseHelper;
import models.Task;

public class StatisticsFragment extends Fragment {

    private TextView totalTasksCount, completedTasksCount, progressText;
    private LinearProgressIndicator progressBar;
    private DatabaseHelper databaseHelper;

    private View highBar, mediumBar, lowBar;
    private TextView highCount, mediumCount, lowCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        initViews(view);
        loadStatistics();

        return view;
    }

    private void initViews(View view) {
        totalTasksCount = view.findViewById(R.id.totalTasksCount);
        completedTasksCount = view.findViewById(R.id.completedTasksCount);
        progressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.progressText);

        highBar = view.findViewById(R.id.highBar);
        mediumBar = view.findViewById(R.id.mediumBar);
        lowBar = view.findViewById(R.id.lowBar);

        highCount = view.findViewById(R.id.highCount);
        mediumCount = view.findViewById(R.id.mediumCount);
        lowCount = view.findViewById(R.id.lowCount);
    }

    private void loadStatistics() {
        int total = databaseHelper.getTotalTasks();
        int completed = databaseHelper.getCompletedTasks();

        totalTasksCount.setText(String.valueOf(total));
        completedTasksCount.setText(String.valueOf(completed));

        int progress = total > 0 ? (completed * 100 / total) : 0;
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");

        // Charger les statistiques par priorité
        loadPriorityStats();
    }

    private void loadPriorityStats() {
        List<Task> allTasks = databaseHelper.getAllTasks();

        int highCount = 0, mediumCount = 0, lowCount = 0;

        for (Task task : allTasks) {
            switch (task.getPriority()) {
                case HIGH:
                    highCount++;
                    break;
                case MEDIUM:
                    mediumCount++;
                    break;
                case LOW:
                    lowCount++;
                    break;
            }
        }

        // Afficher les compteurs
        this.highCount.setText(String.valueOf(highCount));
        this.mediumCount.setText(String.valueOf(mediumCount));
        this.lowCount.setText(String.valueOf(lowCount));

        // Ajuster les barres du graphique
        int total = allTasks.size();
        if (total > 0) {
            updateBarWidth(highBar, highCount, total);
            updateBarWidth(mediumBar, mediumCount, total);
            updateBarWidth(lowBar, lowCount, total);
        }
    }

    private void updateBarWidth(View bar, int count, int total) {
        float percentage = (float) count / total;
        // Convertir en pixels (max 200dp)
        int maxWidth = (int) (200 * getResources().getDisplayMetrics().density);
        int width = (int) (maxWidth * percentage);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bar.getLayoutParams();
        params.width = width;
        bar.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }
}
