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

import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.OnSwipeTouchListener;
import uqac.dim.uqac_scanner.R;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.internal.ContextUtils;


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

        Button downloadButton = findViewById(R.id.download);
        downloadButton.setOnClickListener(this::onClickDownload);

        ((Button)findViewById(R.id.btn_return))
                .setOnClickListener(this::onClickReturn);

        ((Button)findViewById(R.id.access))
                .setOnClickListener(this::onClickAccess);
    }

    private void onClickDownload(View view) {
    }

    private void onClickAccess(View view) {
    }

    private void displayDetailsFromHistory(int qrIdSelected) {
        // Utiliser qrIdSelected pour récupérer les détails du QR code depuis la base de données
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