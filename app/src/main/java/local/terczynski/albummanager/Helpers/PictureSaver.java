package local.terczynski.albummanager.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PictureSaver {
    private PictureSaver(){
        // this class contains only static methods
    }
    public static void savePicture(String path, byte[] imageData) throws FileNotFoundException, IOException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        FileOutputStream fs = new FileOutputStream(path);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fs);
        fs.close();
    }
}
