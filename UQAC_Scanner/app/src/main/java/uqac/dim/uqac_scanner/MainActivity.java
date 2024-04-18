package uqac.dim.uqac_scanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uqac.dim.uqac_scanner.CreateFolder.Create;
import uqac.dim.uqac_scanner.HistoryFolder.History;
import uqac.dim.uqac_scanner.ScannerFolder.Scanner;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues et des écouteurs d'événements
        initViews();

        // Initialisation de la base de données
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
    }

    private void initViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation_View);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        FloatingActionButton createButton = findViewById(R.id.but_Create);
        createButton.setOnClickListener(this::onClickAction);
    }

    // Gestionnaire d'événements pour la navigation inférieure
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            int menuId = item.getItemId();
            Fragment fragment = null;
            if (menuId == R.id.nav_Scanner) {
                fragment = new Scanner();
            } else if (menuId == R.id.nav_History) {
                fragment = new History();
            }
            return loadFragment(fragment, false);
        } catch (Exception e) {
            return false;
        }
    }

    // Gestionnaire d'événements pour le clic sur le bouton flottant
    public void onClickAction(View view) {
        loadFragment(new Create(), false);
    }

    // Méthode pour charger un fragment dans le conteneur
    private boolean loadFragment(Fragment fragment, boolean initializeApp) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (initializeApp) {
                fragmentTransaction.add(R.id.frame_layout, fragment);
            } else {
                fragmentTransaction.replace(R.id.frame_layout, fragment);
            }
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}
