package ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.activity.OnBackPressedCallback; // Ajout

import com.example.projectdmn.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import fragments.StatisticsFragment;
import fragments.TasksFragment;
import utils.DarkModePrefs;
import utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private SharedPrefManager sharedPrefManager;
    private DarkModePrefs darkModePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Appliquer le thème avant setContentView
        darkModePrefs = new DarkModePrefs(this);
        darkModePrefs.applyDarkMode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);
        darkModePrefs = new DarkModePrefs(this);

        initViews();
        setupToolbar();
        setupDrawer();
        setupBottomNavigation();
        loadFragment(new TasksFragment()); // Fragment par défaut

        // Mettre à jour le nom d'utilisateur dans le drawer
        updateNavHeader();

        // Remplacer onBackPressed par OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Vérifier si on est sur le fragment principal
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        finish();
                    }
                }
            }
        });
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Smart Task Manager");
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupBottomNavigation() {
        // Utiliser setOnItemSelectedListener au lieu de setOnNavigationItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();

                        if (itemId == R.id.nav_tasks) {
                            selectedFragment = new TasksFragment();
                            toolbar.setTitle("Mes Tâches");
                        } else if (itemId == R.id.nav_statistics) {
                            selectedFragment = new StatisticsFragment();
                            toolbar.setTitle("Statistiques");
                        }

                        if (selectedFragment != null) {
                            loadFragment(selectedFragment);
                        }
                        return true;
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_header_username);
        TextView navEmail = headerView.findViewById(R.id.nav_header_email);

        String username = sharedPrefManager.getUsername();
        navUsername.setText(username);
        navEmail.setText(username + "@example.com");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_tasks) {
            loadFragment(new TasksFragment());
            toolbar.setTitle("Mes Tâches");
            bottomNavigationView.setSelectedItemId(R.id.nav_tasks);
        } else if (itemId == R.id.nav_statistics) {
            loadFragment(new StatisticsFragment());
            toolbar.setTitle("Statistiques");
            bottomNavigationView.setSelectedItemId(R.id.nav_statistics);
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (itemId == R.id.nav_about) {
            showAboutDialog();
        } else if (itemId == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("À propos")
                .setMessage("Smart Task Manager v1.0\n\n" +
                        "Application de gestion de tâches avec :\n" +
                        "• Authentification locale\n" +
                        "• Gestion complète des tâches\n" +
                        "• Recherche et filtrage\n" +
                        "• Statistiques détaillées\n" +
                        "• Mode sombre\n" +
                        "• Interface Material Design")
                .setPositiveButton("OK", null)
                .show();
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.logout();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    // Supprimez ou commentez cette méthode car nous utilisons OnBackPressedDispatcher
    /*
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    */
}