package ui;


import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectdmn.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import utils.DarkModePrefs;


public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial switchDarkMode;
    private DarkModePrefs darkModePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkModePrefs = new DarkModePrefs(this);

        initViews();
        setupToolbar();
        loadSettings();
        setupListeners();
    }

    private void initViews() {
        switchDarkMode = findViewById(R.id.switch_dark_mode);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Paramètres");
        }
    }

    private void loadSettings() {
        switchDarkMode.setChecked(darkModePrefs.isDarkMode());
    }

    private void setupListeners() {
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                darkModePrefs.setDarkMode(isChecked);
                // Recreate l'activité pour appliquer le changement
                recreate();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}