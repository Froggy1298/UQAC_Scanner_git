package uqac.dim.uqac_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        List<QrCodeModel> scanned = dataBaseHelper.getListQR(0);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("DIM", scanned.get(x).getName());
            Log.e("DIM", scanned.get(x).getDescription());
            Log.e("DIM", String.valueOf(scanned.get(x).getIsScanned()));
        }*/

        List<QrCodeModel> created = dataBaseHelper.getListQR(1);
        /*for (int x = 0;x<allQR.size();x++)
        {
            Log.e("DIM", created.get(x).getName());
            Log.e("DIM", created.get(x).getDescription());
            Log.e("DIM", String.valueOf(created.get(x).getIsScanned()));
        }*/
    }
}