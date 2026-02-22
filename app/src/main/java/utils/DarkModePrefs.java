package utils;


import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class DarkModePrefs {
    private static final String PREF_NAME = "dark_mode_prefs";
    private static final String KEY_IS_DARK_MODE = "is_dark_mode";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public DarkModePrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(KEY_IS_DARK_MODE, isDarkMode);
        editor.apply();

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false);
    }

    public void applyDarkMode() {
        if (isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}