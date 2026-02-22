package fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdmn.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapters.TaskAdapter;
import helpers.DatabaseHelper;
import models.Task;
import ui.AddEditTaskActivity;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Task> taskList;
    private List<Task> filteredList;
    private EditText searchEditText;
    private MaterialButton btnFilterPriority, btnFilterStatus, btnClearFilters;
    private FloatingActionButton fabAddTask;

    private String currentPriorityFilter = null;
    private Boolean currentStatusFilter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        taskList = new ArrayList<>();
        filteredList = new ArrayList<>();

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadTasks();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        searchEditText = view.findViewById(R.id.searchEditText);
        btnFilterPriority = view.findViewById(R.id.filterPriority);
        btnFilterStatus = view.findViewById(R.id.filterStatus);
        btnClearFilters = view.findViewById(R.id.clearFilters);
        fabAddTask = view.findViewById(R.id.fabAddTask);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(filteredList, getContext(),
                new TaskAdapter.OnTaskClickListener() {
                    @Override
                    public void onTaskClick(Task task) {
                        editTask(task);
                    }

                    @Override
                    public void onTaskLongClick(Task task) {
                        showDeleteDialog(task);
                    }
                },
                new TaskAdapter.OnTaskCheckedChangeListener() {
                    @Override
                    public void onTaskCheckedChanged(Task task, boolean isChecked) {
                        updateTaskStatus(task);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEditTaskActivity.class));
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnFilterPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriorityFilterDialog();
            }
        });

        btnFilterStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusFilterDialog();
            }
        });

        btnClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilters();
            }
        });
    }

    private void loadTasks() {
        taskList = databaseHelper.getAllTasks();
        filteredList.clear();
        filteredList.addAll(taskList);
        adapter.updateList(filteredList);
    }

    private void filterTasks(String query) {
        filteredList.clear();
        if (query.isEmpty() && currentPriorityFilter == null && currentStatusFilter == null) {
            filteredList.addAll(taskList);
        } else {
            for (Task task : taskList) {
                boolean matchesSearch = query.isEmpty() ||
                        task.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        task.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        task.getCategory().toLowerCase().contains(query.toLowerCase());

                boolean matchesPriority = currentPriorityFilter == null ||
                        task.getPriority().name().equals(currentPriorityFilter);

                boolean matchesStatus = currentStatusFilter == null ||
                        task.isCompleted() == currentStatusFilter;

                if (matchesSearch && matchesPriority && matchesStatus) {
                    filteredList.add(task);
                }
            }
        }
        adapter.updateList(filteredList);
    }

    private void showPriorityFilterDialog() {
        String[] priorities = {"Tous", "Haute", "Moyenne", "Basse"};
        new AlertDialog.Builder(getContext())
                .setTitle("Filtrer par priorité")
                .setItems(priorities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                currentPriorityFilter = null;
                                break;
                            case 1:
                                currentPriorityFilter = "HIGH";
                                break;
                            case 2:
                                currentPriorityFilter = "MEDIUM";
                                break;
                            case 3:
                                currentPriorityFilter = "LOW";
                                break;
                        }
                        filterTasks(searchEditText.getText().toString());
                    }
                })
                .show();
    }

    private void showStatusFilterDialog() {
        String[] statuses = {"Tous", "Terminées", "Non terminées"};
        new AlertDialog.Builder(getContext())
                .setTitle("Filtrer par statut")
                .setItems(statuses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                currentStatusFilter = null;
                                break;
                            case 1:
                                currentStatusFilter = true;
                                break;
                            case 2:
                                currentStatusFilter = false;
                                break;
                        }
                        filterTasks(searchEditText.getText().toString());
                    }
                })
                .show();
    }

    private void clearFilters() {
        currentPriorityFilter = null;
        currentStatusFilter = null;
        searchEditText.setText("");
        filterTasks("");
        Toast.makeText(getContext(), "Filtres réinitialisés", Toast.LENGTH_SHORT).show();
    }

    private void editTask(Task task) {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }

    private void showDeleteDialog(final Task task) {
        new AlertDialog.Builder(getContext())
                .setTitle("Supprimer la tâche")
                .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(task);
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteTask(Task task) {
        databaseHelper.deleteTask(task.getId());
        loadTasks();
        Toast.makeText(getContext(), "Tâche supprimée", Toast.LENGTH_SHORT).show();
    }

    private void updateTaskStatus(Task task) {
        databaseHelper.updateTask(task);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
}