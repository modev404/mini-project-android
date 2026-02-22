package ui;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectdmn.R;

import utils.SharedPrefManager;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefManager = new SharedPrefManager(this);

        // Vérifier si l'utilisateur est déjà connecté
        if (sharedPrefManager.isLoggedIn()) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        // Reset errors
        etUsername.setError(null);
        etPassword.setError(null);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation des champs
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Le nom d'utilisateur est requis");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Le mot de passe est requis");
            return;
        }

        if (password.length() < 4) {
            etPassword.setError("Le mot de passe doit contenir au moins 4 caractères");
            return;
        }

        // Simuler une connexion réussie
        performLogin(username, password);
    }

    private void performLogin(String username, String password) {
        // Ici vous pouvez ajouter une logique de validation plus complexe
        // Pour l'exemple, on accepte n'importe quel utilisateur

        sharedPrefManager.saveUser(username, password);
        Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}