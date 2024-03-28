package uqac.dim.uqac_scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uqac.dim.uqac_scanner.CreateFolder.Create;
import uqac.dim.uqac_scanner.HistoryFolder.History;
import uqac.dim.uqac_scanner.R;
import uqac.dim.uqac_scanner.ScannerFolder.Scanner;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scanner);

        // Initialisation des vues et des écouteurs d'événements
        initViews();

        // Initialisation de la base de données
        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        // Chargement du fragment Scanner dès que l'activité est créée
        loadScannerFragment();
    }

    // Initialisation des vues et des écouteurs d'événements
    private void initViews() {
        // Gestion de la navigation inférieure
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation_View);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Gestion du bouton flottant
        FloatingActionButton createButton = findViewById(R.id.but_Create);
        createButton.setOnClickListener(this::onClickAction);

        // Gestion du bouton "De ma bibliothèque"
        Button fromLibraryButton = findViewById(R.id.from_library_button);
        fromLibraryButton.setOnClickListener(this::onFromLibraryButtonClick);
    }

    // Méthode pour charger le fragment Scanner
    private void loadScannerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new Scanner());
        fragmentTransaction.commit();
    }

    // Gestionnaire d'événements pour la navigation inférieure
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            int menuId = item.getItemId();
            Fragment fragment = null;
            if (menuId == R.id.nav_Scanner) {
                fragment = new Scanner();
            } else if (menuId == R.id.nav_History) {
                fragment = new History();
            }
            return loadFragment(fragment, false);
        } catch (Exception e) {
            return false;
        }
    }

    // Gestionnaire d'événements pour le clic sur le bouton flottant
    public void onClickAction(View view) {
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

    // Gestionnaire d'événements pour le clic sur le bouton "De ma bibliothèque"
    private void onFromLibraryButtonClick(View view) {
        // Créer un Intent pour ouvrir la galerie de photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    // Méthode pour gérer le résultat de la sélection d'image depuis la bibliothèque de photos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // L'utilisateur a sélectionné une image avec succès
            Uri selectedImageUri = data.getData();
            // Faites quelque chose avec l'URI de l'image sélectionnée, par exemple affichez-la dans une ImageView
            ImageView imageView = findViewById(R.id.click_image);
            imageView.setImageURI(selectedImageUri);

            // Appel de la méthode pour scanner le code QR à partir de l'image sélectionnée
            scanQRCodeFromImage(selectedImageUri);
        }
    }

    // Méthode pour scanner un code QR à partir d'une image
    private void scanQRCodeFromImage(Uri imageUri) {
        // Ajoutez le code nécessaire pour scanner le code QR à partir de l'image ici
        // Vous pouvez utiliser le code fourni dans les réponses précédentes pour implémenter cette fonctionnalité
    }
}
