package local.terczynski.albummanager.Helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import local.terczynski.albummanager.R;

public class PictureSaver {
    private Context context;
    private String mainFolderName = context.getResources().getString(R.string.mainFolderName);
    private File SYS_pictures = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
    private final File mainDir = new File(SYS_pictures, mainFolderName);

    public PictureSaver(Context context){
        this.context = context;
    }
    public void savePicture(byte[] imageData) throws FileNotFoundException, IOException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        FileOutputStream fs = new FileOutputStream(mainDir.getAbsolutePath());
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fs);
        fs.close();
    }
    public void savePicture(byte[] imageData, String path) throws FileNotFoundException, IOException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        FileOutputStream fs = new FileOutputStream(path);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fs);
        fs.close();
    }
    public void savePictureWithDialog(final byte[] imageData) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Wybierz album:");

        ArrayList<String> foldersList = new ArrayList<String>();
        for(File file: mainDir.listFiles()) {
            if(file.isDirectory()) {
                foldersList.add(file.getName());
            }
        }
        final String[] folderArray = foldersList.toArray(new String[0]);
        alert.setItems(folderArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String newPhotoName = UUID.randomUUID().toString();
                String pathToSave = mainDir.getAbsolutePath() + File.separator + folderArray[which] + File.separator + newPhotoName;
                try{
                    savePicture(imageData, pathToSave);
                } catch (IOException e){

                }
            }
        });
        alert.show();
    }
    public void savePicturesWithDialog(final List<byte[]> imagesData){
        for(byte[] imageData : imagesData){
            String mainFolderName = context.getResources().getString(R.string.mainFolderName);
            File SYS_pictures = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
            final File mainDir = new File(SYS_pictures, mainFolderName);


            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Wybierz album:");

            ArrayList<String> foldersList = new ArrayList<String>();
            for(File file: mainDir.listFiles()) {
                if(file.isDirectory()) {
                    foldersList.add(file.getName());
                }
            }
            final String[] folderArray = foldersList.toArray(new String[0]);
            alert.setItems(folderArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        // save all pictures
                        for(byte[] data : imagesData){
                            String newPhotoName = UUID.randomUUID().toString();
                            String pathToSave = mainDir.getAbsolutePath() + File.separator + folderArray[which] + File.separator + newPhotoName;
                            savePicture(data, pathToSave);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            alert.show();
        }
    }
}
