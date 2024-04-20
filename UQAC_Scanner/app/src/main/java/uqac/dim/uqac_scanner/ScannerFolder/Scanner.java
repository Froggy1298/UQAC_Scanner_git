package uqac.dim.uqac_scanner.ScannerFolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;

@ExperimentalGetImage
public class Scanner extends Fragment {

    private PreviewView previewView; // Vue pour prévisualiser la caméra
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture; // Future pour la fourniture de la caméra
    private static final int REQUEST_GALLERY = 1002; // Identifiant pour la demande de galerie

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        previewView = view.findViewById(R.id.previewView); // Récupération de la vue de prévisualisation de la caméra
        Button fromLibraryButton = view.findViewById(R.id.from_library_button); // Bouton pour ouvrir la galerie
        fromLibraryButton.setOnClickListener(v -> openGallery()); // Définition de l'action du bouton

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext()); // Obtention de la future fourniture de la caméra
        cameraProviderFuture.addListener(() -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCamera(); // Démarrage de la caméra si la permission est accordée
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001); // Demande de permission si elle n'est pas accordée
            }
        }, ContextCompat.getMainExecutor(requireContext()));

        return view;
    }

    // Méthode pour ouvrir la galerie d'images
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // Méthode appelée après la sélection d'une image depuis la galerie
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            analyzeImageFromUri(imageUri); // Analyse de l'image sélectionnée
        }
    }

    // Méthode pour analyser une image depuis l'URI
    private void analyzeImageFromUri(Uri imageUri) {
        try {
            InputImage image = InputImage.fromFilePath(requireContext(), imageUri); // Création d'une InputImage à partir de l'URI
            BarcodeScanner scanner = BarcodeScanning.getClient(); // Création d'un scanner de codes-barres

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            String rawValue = barcode.getRawValue();
                            handleResult(rawValue); // Traitement du résultat du code-barres
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to analyze image.", Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to read image.", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour démarrer la caméra
    private void startCamera() {
        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get(); // Obtention du fournisseur de caméra
            setupCamera(cameraProvider); // Configuration de la caméra
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Toast.makeText(getContext(), "Error starting camera", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour configurer la caméra
    private void setupCamera(ProcessCameraProvider cameraProvider) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options); // Création d'un scanner de codes-barres

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build(); // Prévisualisation de la caméra
        preview.setSurfaceProvider(previewView.getSurfaceProvider()); // Définition du fournisseur de surface pour la prévisualisation

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build(); // Analyse d'image

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getContext()), imageProxy -> {
            @NonNull Image image = imageProxy.getImage();
            if (image != null) {
                InputImage inputImage = InputImage.fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
                scanner.process(inputImage)
                        .addOnSuccessListener(barcodes -> {
                            for (Barcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                handleResult(rawValue); // Traitement du résultat du code-barres
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to process barcode", Toast.LENGTH_SHORT).show())
                        .addOnCompleteListener(task -> imageProxy.close());
            } else {
                imageProxy.close();
            }
        });

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis); // Liaison des composants de la caméra au cycle de vie du fragment
    }

    // Méthode pour traiter le résultat du code-barres
    private void handleResult(String resultText) {
        Toast.makeText(getContext(), "QR Code result: " + resultText, Toast.LENGTH_LONG).show(); // Affichage du résultat du code-barres

        // Création d'un nouveau modèle QR Code
        QrCodeModel qrCode = new QrCodeModel();
        qrCode.setName("Scanned QR Code");  // Nom du code QR
        qrCode.setUrl(resultText);  // L'URL ou le texte du QR
        qrCode.setDescription("Scanned QR code from app");  // Description
        qrCode.setDateCreation(new Date());  // Date de création
        qrCode.setDateEdit(new Date());  // Date d'édition
        qrCode.setIsScanned(true);  // Marqué comme scanné

        // Instance de DataBaseHelper pour accéder à la base de données
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());

        // Enregistrement du QR code dans la base de données
        if (dbHelper.addCreatedQR(qrCode)) {
            Toast.makeText(getContext(), "QR Code saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save QR Code.", Toast.LENGTH_SHORT).show();
        }
    }
}
