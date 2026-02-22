package ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectdmn.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;



import java.util.List;

import helpers.DatabaseHelper;
import models.Task;

public class StatisticsActivity extends AppCompatActivity {

    private TextView totalTasksCount, completedTasksCount, progressText;
    private LinearProgressIndicator progressBar;
    private DatabaseHelper databaseHelper;

    private View highBar, mediumBar, lowBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        loadStatistics();
    }

    private void initViews() {
        totalTasksCount = findViewById(R.id.totalTasksCount);
        completedTasksCount = findViewById(R.id.completedTasksCount);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        highBar = findViewById(R.id.highBar);
        mediumBar = findViewById(R.id.mediumBar);
        lowBar = findViewById(R.id.lowBar);
    }

    private void loadStatistics() {
        int total = databaseHelper.getTotalTasks();
        int completed = databaseHelper.getCompletedTasks();

        totalTasksCount.setText(String.valueOf(total));
        completedTasksCount.setText(String.valueOf(completed));

        int progress = total > 0 ? (completed * 100 / total) : 0;
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");

        // Charger les données pour le graphique
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
}