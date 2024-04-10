package uqac.dim.uqac_scanner.DisplayFolder;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.OnSwipeTouchListener;
import uqac.dim.uqac_scanner.R;


public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("QR_ID_SELECTED")) {
                // Afficher les détails du QR code sélectionné depuis l'historique
                int qrIdSelected = intent.getIntExtra("QR_ID_SELECTED", -1);
                displayDetailsFromHistory(qrIdSelected);
            } else if (intent.hasExtra("NEW_QR_NAME") && intent.hasExtra("NEW_QR_DESC")) {
                // Afficher les détails du nouveau QR code créé
                String newName = intent.getStringExtra("NEW_QR_NAME");
                String newDesc = intent.getStringExtra("NEW_QR_DESC");
                displayNewQRDetails(newName, newDesc);
            }
        }

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

    private void displayDetailsFromHistory(int qrIdSelected) {
        // Utiliser qrIdSelected pour récupérer les détails du QR code depuis la base de données
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        QrCodeModel qrCodeModel = dbHelper.getQR(qrIdSelected);

        // Afficher les détails dans les TextViews
        TextView nameTextView = findViewById(R.id.label_name);
        TextView dateTextView = findViewById(R.id.label_date);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(qrCodeModel.getName());
        displayDate(qrCodeModel.getDateCreation(), dateTextView); // Appeler displayDate() avec le TextView pour afficher la date
        descriptionTextView.setText(qrCodeModel.getDescription());

        // Afficher le code QR dans l'ImageView
        displayQRCode(qrCodeModel.getCodeQR());
    }


    private void displayQRCode(byte[] qrCodeBytes) {
        // Convertir le tableau de bytes en une image bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(qrCodeBytes, 0, qrCodeBytes.length);

        // Afficher l'image bitmap dans l'ImageView
        ImageView qrCodeImageView = findViewById(R.id.affichageqr);
        qrCodeImageView.setImageBitmap(bitmap);
    }


    private void displayDate(Date date, TextView textView) {
        // Formater la date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        // Afficher
        textView.setText(dateString);
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

    private void displayNewQRDetails(String newName, String newDesc) {
        // Afficher les détails du nouveau QR code si jamais
        TextView nameTextView = findViewById(R.id.label_name);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(newName);
        descriptionTextView.setText(newDesc);
    }

}