package uqac.dim.uqac_scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        Date today =  Calendar.getInstance().getTime();
        byte[] image = new byte[6];

        //boolean result  = dataBaseHelper.addCreatedQR(new QrCodeModel("mommyy","https://youtu.be/aOLxQGLJouI?si=GfudDEP9MVLdZjQA","desc", image,today, today, false));
        //Log.e("DIM", "QR Code Created " + result);

        List<QrCodeModel> allQR = dataBaseHelper.getAllQR();
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("DIM", allQR.get(x).getName());
            Log.e("DIM", allQR.get(x).getDescription());
            Log.e("DIM", String.valueOf(allQR.get(x).getIsScanned()));
        }*/

        List<QrCodeModel> scanned = dataBaseHelper.getListQR(true);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("DIM", scanned.get(x).getName());
            Log.e("DIM", scanned.get(x).getDescription());
            Log.e("DIM", String.valueOf(scanned.get(x).getIsScanned()));
        }*/

        List<QrCodeModel> created = dataBaseHelper.getListQR(false);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("DIM", created.get(x).getName());
            Log.e("DIM", created.get(x).getDescription());
            Log.e("DIM", String.valueOf(created.get(x).getIsScanned()));
        }*/
        ((BottomNavigationView)findViewById(R.id.bottom_Navigation_View))
                .setOnItemSelectedListener(this::onNavigationItemSelected);

        ((FloatingActionButton)findViewById(R.id.but_Create))
                .setOnClickListener(this::onClickCreate);

        loadFragment(new Scanner(), true);
    }

    public boolean onNavigationItemSelected(MenuItem item)
    {
        try {
            Log.i("LOG", "Enter the try in onNavigationItemSelected");
            int menuId = item.getItemId();

            //Do not use switch case, see reason here
            //https://stackoverflow.com/questions/9092712/switch-case-statement-error-case-expressions-must-be-constant-expression
            if(menuId == R.id.nav_Scanner) {
                loadFragment(new Scanner(), false);
            } else if(menuId == R.id.nav_History) {
                loadFragment(new History(), false);
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

    public void onClickCreate(View view)
    {
        loadFragment(new Create(), false);
    }

    private void loadFragment(Fragment fragmentToLoad, boolean initializeApp)
    {
        //TODO test if creating those two fucker before improve the speed of the app
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (initializeApp)
        {
            fragmentTransaction.add(R.id.frame_layout, fragmentToLoad);
        }
        else
        {
            fragmentTransaction.replace(R.id.frame_layout, fragmentToLoad);
        }

        fragmentTransaction.commit();
    }
}