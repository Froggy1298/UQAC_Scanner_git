package uqac.dim.uqac_scanner.ScannerFolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import uqac.dim.uqac_scanner.R;

public class Scanner extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ouvrir automatiquement la caméra lorsque le fragment est créé
        dispatchTakePictureIntent();

        // Utilisez la vue fragment_scanner comme layout pour ce fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        imageView = view.findViewById(R.id.click_image);

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == requireActivity().RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            scanQRCodeFromImage(imageBitmap);
        }
    }

    private void scanQRCodeFromImage(Bitmap bitmap) {
        try {
            Reader reader = new MultiFormatReader();
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap)));
            Result result = reader.decode(binaryBitmap);
            handleResult(result.getText());
        } catch (Exception e) {
            Log.e("SCAN", "Error scanning QR code: " + e.getMessage());
        }
    }

    private void handleResult(String resultText) {
        Log.i("SCAN_RESULT", "QR Code result: " + resultText);
        // Implement actions based on the QR code result here
    }

    public static class BitmapLuminanceSource extends com.google.zxing.LuminanceSource {
        private final byte[] luminances;

        public BitmapLuminanceSource(Bitmap bitmap) {
            super(bitmap.getWidth(), bitmap.getHeight());

            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            luminances = new byte[bitmap.getWidth() * bitmap.getHeight()];
            for (int i = 0; i < pixels.length; i++) {
                int pixel = pixels[i];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r == g && g == b) {
                    luminances[i] = (byte) r;
                } else {
                    luminances[i] = (byte) ((r + g + g + b) >> 2);
                }
            }
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            if (row == null || row.length < getWidth()) {
                row = new byte[getWidth()];
            }
            System.arraycopy(luminances, y * getWidth(), row, 0, getWidth());
            return row;
        }

        @Override
        public byte[] getMatrix() {
            return luminances;
        }
    }
}
