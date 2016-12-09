package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.kovalenkovolodymyr.kidcard.MainActivity.PREFERENCE_FILE_KEY;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] entries = getResources().getStringArray(R.array.langList);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final SharedPreferences sharedPreferences =
                getApplication().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        setDefaultSpinnerValue(spinner,sharedPreferences,entries);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lang = "";
                switch (position){
                    case 0: //english
                        lang = "en";
                        break;
                    case 1: //ukrainian
                        lang = "uk";
                        break;
                    case 2: //russian
                        lang = "ru";
                        break;
                    default:
                        break;
                }

                Toast.makeText(SettingsActivity.this,
                        lang,Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MainActivity.STRING_LANG_KEY,lang);
                editor.apply();

                setDefaultLocale(SettingsActivity.this,lang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        String selected = spinner.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
    }

    private void setDefaultSpinnerValue(Spinner spinner, SharedPreferences sharedPreferences, String[] entries) {
        String savedLang = sharedPreferences.getString(MainActivity.STRING_LANG_KEY,
                Locale.getDefault().getDisplayLanguage());

        Log.d("mylogs","setDefaultSpinnerValue() savedLang: " + savedLang);
        switch (savedLang){
            case "uk":
            case "українська":
                spinner.setSelection(1);
                break;
            case "ru":
            case "русский":
                spinner.setSelection(2);
                break;
            default:
                spinner.setSelection(0);
        }
    }

    public static void setDefaultLocale(Context context,String locale) {
        Locale locJa = new Locale(locale);
        Locale.setDefault(locJa);
        Log.d("mylogs","setDefaultLocale() locJa:" + locJa);
//        Configuration config = new Configuration();
        Configuration config = new Configuration();
        config.locale = locJa;

        context.getResources().updateConfiguration(config, context.getResources()
                .getDisplayMetrics());

        locJa = null;
        config = null;
    }
}
