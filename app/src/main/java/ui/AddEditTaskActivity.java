package ui;



import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectdmn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import helpers.DatabaseHelper;
import models.Task;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate, etCategory;
    private Spinner spinnerPriority;
    private Button btnSave, btnCancel;
    private FloatingActionButton fabDatePicker;

    private DatabaseHelper databaseHelper;
    private Task currentTask;
    private boolean isEditMode = false;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        databaseHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        initViews();
        setupSpinner();
        setupListeners();
        checkEditMode();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etCategory = findViewById(R.id.etCategory);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        fabDatePicker = findViewById(R.id.fabDatePicker);

        // Set current date
        etDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
    }

    private void setupListeners() {
        fabDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void checkEditMode() {
        if (getIntent().hasExtra("task")) {
            isEditMode = true;
            currentTask = (Task) getIntent().getSerializableExtra("task");
            fillData();

            // Changer le titre de l'activity
            setTitle("Modifier la tâche");
        } else {
            setTitle("Nouvelle tâche");
        }
    }

    private void fillData() {
        etTitle.setText(currentTask.getTitle());
        etDescription.setText(currentTask.getDescription());
        etDate.setText(currentTask.getDate());
        etCategory.setText(currentTask.getCategory());

        // Set spinner position
        int spinnerPosition = 0;
        switch (currentTask.getPriority()) {
            case HIGH:
                spinnerPosition = 0;
                break;
            case MEDIUM:
                spinnerPosition = 1;
                break;
            case LOW:
                spinnerPosition = 2;
                break;
        }
        spinnerPriority.setSelection(spinnerPosition);
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String priorityStr = spinnerPriority.getSelectedItem().toString();

        // Validation
        if (title.isEmpty()) {
            etTitle.setError("Le titre est requis");
            return;
        }

        if (date.isEmpty()) {
            etDate.setError("La date est requise");
            return;
        }

        // Convertir la priorité
        Task.Priority priority;
        switch (priorityStr) {
            case "Haute":
                priority = Task.Priority.HIGH;
                break;
            case "Moyenne":
                priority = Task.Priority.MEDIUM;
                break;
            default:
                priority = Task.Priority.LOW;
                break;
        }

        if (isEditMode) {
            // Mise à jour
            currentTask.setTitle(title);
            currentTask.setDescription(description);
            currentTask.setDate(date);
            currentTask.setCategory(category);
            currentTask.setPriority(priority);

            int result = databaseHelper.updateTask(currentTask);
            if (result > 0) {
                Toast.makeText(this, "Tâche modifiée avec succès", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Création
            Task newTask = new Task(title, description, date, priority, category);
            long id = databaseHelper.addTask(newTask);

            if (id != -1) {
                Toast.makeText(this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
            }
        }
    }
}