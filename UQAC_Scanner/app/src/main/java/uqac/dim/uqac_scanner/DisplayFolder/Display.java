package uqac.dim.uqac_scanner.DisplayFolder;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import uqac.dim.uqac_scanner.EditFolder.Edit;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
        ImageView qrCodeImageView = findViewById(R.id.affichageqr);

        // Obtenir le Bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeImageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Enregistrer le Bitmap
        saveBitmapToGallery(bitmap);
    }

    private void onClickAccess(View view) {
        Intent intent = new Intent(Display.this, Edit.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
        finish();
    }

    private void displayDetailsFromHistory(int qrIdSelected) {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        thisCodeQr = dbHelper.getQR(qrIdSelected);

        // Afficher les détails dans les TextViews
        TextView nameTextView = findViewById(R.id.label_name);
        TextView dateTextView = findViewById(R.id.label_date);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(thisCodeQr.getName());
        displayDate(thisCodeQr.getDateCreation(), dateTextView); // Appeler displayDate() avec le TextView pour afficher la date
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


    private void saveBitmapToGallery(Bitmap bitmap) {
        String galleryPath = "/mnt/sdcard/Screenshot.png";

        try {
            File file = new File(galleryPath);
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.close();

            Toast.makeText(this, "Image enrigistrée", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Nooooo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

    private void displayNewQRDetails(String newName, String newDesc) {
        // Afficher les détails du nouveau QR code si jamais
        TextView nameTextView = findViewById(R.id.label_name);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(newName);
        descriptionTextView.setText(newDesc);
    }

}