package uqac.dim.uqac_scanner.ScannerFolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;

@ExperimentalGetImage
public class Scanner extends Fragment {

    private PreviewView previewView; // Vue pour prévisualiser la caméra
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture; // Future pour obtenir l'instance du fournisseur de caméra
    private static final int REQUEST_GALLERY = 1002; // Identifiant pour la demande de sélection d'image depuis la galerie
    private String lastScannedCode = null; // Stocke le dernier code QR scanné pour éviter les doublons

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        previewView = view.findViewById(R.id.previewView);
        Button fromLibraryButton = view.findViewById(R.id.from_library_button);
        fromLibraryButton.setOnClickListener(v -> openGallery());

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
            }
        }, ContextCompat.getMainExecutor(requireContext()));

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            analyzeImageFromUri(imageUri);
        }
    }

    private void startCamera() {
        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            setupCamera(cameraProvider);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Toast.makeText(getContext(), "Error starting camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCamera(ProcessCameraProvider cameraProvider) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getContext()), imageProxy -> {
            @NonNull Image image = imageProxy.getImage();
            if (image != null && (lastScannedCode == null || !lastScannedCode.equals(lastScannedCode))) {
                InputImage inputImage = InputImage.fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
                scanner.process(inputImage)
                        .addOnSuccessListener(barcodes -> {
                            for (Barcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                if (!rawValue.equals(lastScannedCode)) {
                                    lastScannedCode = rawValue;
                                    handleResult(rawValue);
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to process barcode", Toast.LENGTH_SHORT).show())
                        .addOnCompleteListener(task -> imageProxy.close());
            } else {
                imageProxy.close();
            }
        });

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    private void handleResult(String resultText) {
        Toast.makeText(getContext(), "QR Code result: " + resultText, Toast.LENGTH_LONG).show();

        Bitmap tempBitmap = BitMapHelper.CreateBitMapFromString(resultText);
        byte[] tempBitmapByteArray = BitMapHelper.getBytes(tempBitmap);

        QrCodeModel qrCode = new QrCodeModel();
        qrCode.setName("Scanned " + GeneralHelper.getCurrentTimeString());
        qrCode.setCodeQR(tempBitmapByteArray);
        qrCode.setUrl(resultText);
        qrCode.setDescription("Scanned QR code from app");
        qrCode.setDateCreation(GeneralHelper.getCurrentTimeDate());
        qrCode.setDateEdit(GeneralHelper.getCurrentTimeDate());
        qrCode.setIsScanned(true);

        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        if (dbHelper.addCreatedQR(qrCode)) {
            Toast.makeText(getContext(), "QR Code saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save QR Code.", Toast.LENGTH_SHORT).show();
        }
    }
}
