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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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