package uqac.dim.uqac_scanner.DisplayFolder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import java.util.Locale;
import java.io.File;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.R;


public class Display extends AppCompatActivity {

    QrCodeModel thisCodeQr;

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

        //les buttons du activity
        Button downloadButton = findViewById(R.id.download);
        downloadButton.setOnClickListener(this::onClickDownload);

        ((Button)findViewById(R.id.btn_return))
                .setOnClickListener(this::onClickReturn);

        ((Button)findViewById(R.id.access))
                .setOnClickListener(this::onClickAccess);
    }

    private void onClickDownload(View view) {
        if (thisCodeQr != null) {
            // Récupérer l'ImageView
            ImageView qrCodeImageView = findViewById(R.id.affichageqr);

            // Obtenir le drawable
            BitmapDrawable drawable = (BitmapDrawable) qrCodeImageView.getDrawable();

            if (drawable != null) {
                // Convertir le drawable en bitmap
                Bitmap bitmap = drawable.getBitmap();

                // Enregistrer le bitmap dans la mémoire externe
                saveImageQrCode(bitmap);
            } else {
                Toast.makeText(this, "Aucune image à télécharger", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Aucun QR code sélectionné", Toast.LENGTH_SHORT).show();
        }
    }


    private void onClickAccess(View view) {
        if (thisCodeQr == null)
        {
            Toast.makeText(this, "Aucun QR code sélectionné", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer URL
        String url = thisCodeQr.getUrl();

        if (url == null && url.isEmpty())
        {
            Toast.makeText(this, "Aucun QR code sélectionné", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent;

        if(url.contains("https://"))
        {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        else{
            String tempURL = "https://www.google.ca/search?q=" + url;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempURL));
        }
        startActivity(intent);
    }

    private void displayDetailsFromHistory(int qrIdSelected) {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        thisCodeQr = dbHelper.getQR(qrIdSelected);

        // Afficher les détails dans les TextViews
        TextView nameTextView = findViewById(R.id.label_name);
        TextView dateTextView = findViewById(R.id.label_date);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(thisCodeQr.getName());
        displayDate(thisCodeQr.getDateCreation(), dateTextView);
        descriptionTextView.setText(thisCodeQr.getDescription());

        // Afficher le code QR dans l'ImageView
        displayQRCode(thisCodeQr.getCodeQR());
    }


    private void displayQRCode(byte[] qrCodeBytes) {
        // Convertir le tableau de bytes en une image bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(qrCodeBytes, 0, qrCodeBytes.length);

        // Afficher l'image bitmap dans l'ImageView
        ImageView qrCodeImageView = findViewById(R.id.affichageqr);
        qrCodeImageView.setImageBitmap(bitmap);
    }


    private void saveImageQrCode(Bitmap bitmap) {
        ContentResolver contentResolver = getContentResolver();
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //Valeur Spécifique aux photos
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(images, contentValues);
        //Essaie de sauvegarder
        try{
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            Toast.makeText(this, "Image enregistrée avec succès", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveGallery(Bitmap bitmap) {
        ContentResolver contentResolver = getContentResolver();
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(images, contentValues);

        try{
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
        }catch (Exception e){
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDate(Date date, TextView textView) {
        // Formater la date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        // Afficher
        textView.setText(getString(R.string.date)+ ' ' + dateString);
    }


    private void onClickReturn(View view) {
        finish();
        this.overridePendingTransition(R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
    }

    private void displayNewQRDetails(String newName, String newDesc) {
        // Afficher les détails du nouveau QR code si jamais
        TextView nameTextView = findViewById(R.id.label_name);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(newName);
        descriptionTextView.setText(newDesc);
    }

}