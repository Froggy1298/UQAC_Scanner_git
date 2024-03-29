package uqac.dim.uqac_scanner.DisplayFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import uqac.dim.uqac_scanner.Helpers.OnSwipeTouchListener;
import uqac.dim.uqac_scanner.R;

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);



        Button downloadButton = findViewById(R.id.download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture de l'écran et enregistrement de l'image
                captureAndSaveScreen();
            }
        });

        Button returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(this::onClickReturn);

        ((LinearLayout)findViewById(R.id.theBigOne))
                .setOnTouchListener(new OnSwipeTouchListener(this) {
                    public void onSwipeRight() {
                        onClickReturn(null);
                    }
                });

        Button accessButton = findViewById(R.id.access);
        accessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Pour access
                Intent intent = new Intent(Affichage.this, NouvelleActivite.class); // Remplacez NouvelleActivite.class par le nom de votre nouvelle activité
                startActivity(intent);

                 */
            }
        });
    }


    private void onClickReturn(View view) {
        finish();
        this.overridePendingTransition(R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
    }


    private void captureAndSaveScreen() {
        //Interface de l'utilisateur
        View rootView = getWindow().getDecorView().getRootView();

        //Create Bitmap
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        // Enregistrer dans le stockage externe
        saveBitmapToStorage(bitmap);
    }

    private void saveBitmapToStorage(Bitmap bitmap) {
        //Dans la photothèque de l'utilisatuer
        String filePath = "/mnt/sdcard/Screenshot.png";

        try {
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            Toast.makeText(this, "Yayyy", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,"Nooo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}