package uqac.dim.uqac_scanner.Helpers;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class BitMapHelper {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap CreateBitMapFromString(String information)
    {
        Bitmap tempBitmap;
        QRGEncoder qrgEncoder = new QRGEncoder(information, null, QRGContents.Type.TEXT,512);
        try {
            tempBitmap = qrgEncoder.getBitmap();
            return tempBitmap;
        } catch (Error e) {
            Log.e("LOG", e.toString());
        }
        return null;
    }
}
