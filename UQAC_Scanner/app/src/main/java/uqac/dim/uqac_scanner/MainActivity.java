package uqac.dim.uqac_scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import uqac.dim.uqac_scanner.CreateFolder.Create;
import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
//import uqac.dim.uqac_scanner.Helpers.OnSwipeTouchListener;
import uqac.dim.uqac_scanner.HistoryFolder.History;
import uqac.dim.uqac_scanner.ScannerFolder.Scanner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;


public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((BottomNavigationView)findViewById(R.id.bottom_Navigation_View))
                .setOnItemSelectedListener(this::onNavigationItemSelected);

        ((FloatingActionButton)findViewById(R.id.but_Create))
                .setOnClickListener(this::onClickCreate);
        
        // Initialisation de la base de données
        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        //loadFragment(new Scanner(), true, R.anim.fade_in, R.anim.fade_out);
    }
    // Gestionnaire d'événements pour la navigation inférieure
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            Log.i("LOG", "Enter the try in onNavigationItemSelected");
            int menuId = item.getItemId();

            //Do not use switch case, see reason here
            //https://stackoverflow.com/questions/9092712/switch-case-statement-error-case-expressions-must-be-constant-expression
            if(menuId == R.id.nav_Scanner) {
                //loadFragment(new Scanner(), false, R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
            } else if(menuId == R.id.nav_History) {
                //loadFragment(new History(), false, R.anim.slide_in_right_to_center, R.anim.slide_out_center_to_left);
            }
            return true;
        }
        catch (Exception e)
        {
            Log.i("LOG", "An error has occured");
            Log.i("LOG", e.getMessage());
            return false;
        }
    }

    // Gestionnaire d'événements pour le clic sur le bouton flottant
    public void onClickCreate(View view) {
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
