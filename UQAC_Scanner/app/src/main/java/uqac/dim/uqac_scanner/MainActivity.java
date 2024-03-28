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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import uqac.dim.uqac_scanner.CreateFolder.Create;
import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
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

        Log.i("LOG", "Weee");
        dataBaseHelper = new DataBaseHelper(MainActivity.this);


        //Date today =  Calendar.getInstance().getTime();
        //byte[] image = new byte[6];
        //boolean result  = dataBaseHelper.addCreatedQR(new QrCodeModel("mommyy","https://youtu.be/aOLxQGLJouI?si=GfudDEP9MVLdZjQA","desc", image,today, today, false));
        //Log.e("DIM", "QR Code Created " + result);

        //TODO TEST ICI POUR LES DATE ET ID
        List<QrCodeModel> allQR = dataBaseHelper.getAllQR();
        for (int x = 0;x<allQR.size();x++)
        {
            Log.e("LOG", String.valueOf(allQR.get(x).getID()));
            Log.e("LOG", GeneralHelper.DateToString(allQR.get(x).getDateCreation()));
            Log.e("LOG", GeneralHelper.DateToString(allQR.get(x).getDateEdit()));
            Log.e("LOG", allQR.get(x).getName());
            Log.e("LOG", allQR.get(x).getDescription());
            Log.e("LOG", allQR.get(x).getUrl());
            Bitmap test = BitMapHelper.getImage(allQR.get(x).getCodeQR());
            Log.e("LOG", "----------");
        }

        //TODO TEST ICI POUR MES FONCTION DATE
        Date test1 = GeneralHelper.getCurrentTimeDate();
        String test2 = GeneralHelper.getCurrentTimeString();
        String test3 = GeneralHelper.DateToString(test1);



        //List<QrCodeModel> scanned = dataBaseHelper.getListQR(1);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("LOG", scanned.get(x).getName());
            Log.e("LOG", scanned.get(x).getDescription());
            Log.e("LOG", String.valueOf(scanned.get(x).getIsScanned()));
        }*/

        //List<QrCodeModel> created = dataBaseHelper.getListQR(0);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("LOG", created.get(x).getName());
            Log.e("LOG", created.get(x).getDescription());
            Log.e("LOG", String.valueOf(created.get(x).getIsScanned()));
        }*/


        ((BottomNavigationView)findViewById(R.id.bottom_Navigation_View))
                .setOnItemSelectedListener(this::onNavigationItemSelected);

        ((FloatingActionButton)findViewById(R.id.but_Create))
                .setOnClickListener(this::onClickCreate);

        loadFragment(new Scanner(), true, R.anim.fade_in, R.anim.fade_out);
    }
    
    public boolean onNavigationItemSelected(MenuItem item)
    {
        try {
            Log.i("LOG", "Enter the try in onNavigationItemSelected");
            int menuId = item.getItemId();

            //Do not use switch case, see reason here
            //https://stackoverflow.com/questions/9092712/switch-case-statement-error-case-expressions-must-be-constant-expression
            if(menuId == R.id.nav_Scanner) {
                loadFragment(new Scanner(), false, R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
            } else if(menuId == R.id.nav_History) {
                loadFragment(new History(), false, R.anim.slide_in_right_to_center, R.anim.slide_out_center_to_left);
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
        loadFragment(new Create(), false, R.anim.fade_in, R.anim.fade_out);
    }

    private void loadFragment(Fragment fragmentToLoad, boolean initializeApp, int enter, int exit)
    {
        //TODO test if creating those two fucker before improve the speed of the app
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(
                        enter, // enter
                        exit   // exit
        );

        if (initializeApp)
        {
            fragmentTransaction.add(R.id.frame_layout, fragmentToLoad);
        }
        else
        {
            fragmentTransaction.replace(R.id.frame_layout, fragmentToLoad);
        }
        fragmentTransaction.commit(
        );
    }
}