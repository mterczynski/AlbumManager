package local.terczynski.albummanager.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import local.terczynski.albummanager.Helpers.CameraPreview;
import local.terczynski.albummanager.Helpers.Circle;
import local.terczynski.albummanager.Helpers.Miniature;
import local.terczynski.albummanager.R;

public class zdjecie extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout camera_frameLayout;

    // byte[] of taken picture
    private byte[] photoData;
    // camera options:
    private Camera.Parameters cameraOptions;
    private String[] whiteBalanceOptions;
    private String[]supportedColorEffects;
    private Camera.Size[] supportedPictureSizes;
    private ArrayList<String> exposureCompensationOptions;
    private Point screenSize;

    private List<Miniature> miniatures = new ArrayList<Miniature>();
//    private int miniatureCount = 0;

    private void initCamera() {
        camera_frameLayout = (FrameLayout) findViewById(R.id.camera_frameLayout);
        boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        if (!cam) {
            Log.d("camera","Brak kamery!");
        } else {
            // wykorzystanie danych zwróconych przez kolejną funkcję getCameraId():

            cameraId = getCameraId();
            // jest jakaś kamera!
            if (cameraId < 0) {
                Log.d("camera","Brak kamery z przodu!");
            } else {
                Log.d("camera","Camera id: " + cameraId);
                camera = Camera.open(cameraId);
            }
        }
    }

    private void refreshCameraOptions(){
        cameraOptions = camera.getParameters();
        whiteBalanceOptions = cameraOptions.getSupportedWhiteBalance().toArray(new String[0]);
        supportedColorEffects = cameraOptions.getSupportedColorEffects().toArray(new String[0]);
        supportedPictureSizes  = cameraOptions.getSupportedPictureSizes().toArray(new Camera.Size[0]);

        int minExposure = cameraOptions.getMinExposureCompensation();
        int maxExposure = cameraOptions.getMaxExposureCompensation();
        exposureCompensationOptions = new ArrayList<String>();
        for(int i = minExposure; i <= maxExposure; i++ ) {
            exposureCompensationOptions.add(i + "");
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            // zapisz dane zdjęcia w tablicy typu byte[]
            // do poźniejszego wykorzystania
            // ponieważ zapis zdjęcia w galerii powinien być dopiero po akceptacji butonem

            photoData = data;

            // odswież (lub nie) kamerę (zapobiega to przycięciu się kamery po zrobieniu zdjęcia)

            camera.startPreview();

// miniatures:
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // zmiana wielkości bitmapy - resize do przewidywanej wielkości miniatury:
            Bitmap smallBmp = Bitmap.createScaledBitmap(bitmap , 30, 50, false);
            // TODO: add miniature to list

            double angle = 2 * Math.PI/miniatures.size();
            double circleRadius = 50;

            int diffX = (int)(Math.cos(angle) * circleRadius);
            int diffY = (int)(circleRadius * Math.sin(angle));

            Miniature miniature = new Miniature(zdjecie.this, bitmap, new Point(screenSize.x/4 + diffX, screenSize.y/4 + diffY));
            camera_frameLayout.addView(miniature);
        }
    };

    private void addClickListeners(){
        // header buttons:
        ImageView colorsButton = (ImageView)findViewById(R.id.colorsButton);
        ImageView flashButton = (ImageView)findViewById(R.id.flashButton);
        ImageView sunButton = (ImageView)findViewById(R.id.sunButton);
        ImageView resizeButton = (ImageView)findViewById(R.id.resizeButton);
        // footer buttons:
        ImageView changeCameraButton = (ImageView)findViewById(R.id.changeCamera);
        ImageView takePictureButton = (ImageView)findViewById(R.id.takePicture);
        ImageView savePictureButton = (ImageView) findViewById(R.id.savePicture);

        // header buttons:
        colorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(zdjecie.this);
            alert.setTitle("Efekty kolorów:");
            alert.setItems(supportedColorEffects, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    cameraOptions.setColorEffect(supportedColorEffects[which]);
                    camera.setParameters(cameraOptions);
                }
            });
            alert.show();
            }
        });
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(zdjecie.this);
            //String title = getString(R.string.folder_choose);
            alert.setTitle("Balans bieli");
            alert.setItems(whiteBalanceOptions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    cameraOptions.setWhiteBalance(whiteBalanceOptions[which]);
                    camera.setParameters(cameraOptions);
                }
            });
            alert.show();
            }
        });
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(zdjecie.this);
            alert.setTitle("Wybierz kompensację naświetlenia:");
            alert.setItems(exposureCompensationOptions.toArray(new String[0]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    cameraOptions.setExposureCompensation(Integer.parseInt(exposureCompensationOptions.get(which)));
                    Log.d("camera","Current exposure: " + cameraOptions.getExposureCompensation());
                    camera.setParameters(cameraOptions);
                }
            });
            alert.show();
            }
        });
        resizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(zdjecie.this);


            String [] supportedPictureSizesAsStringArray = new String[supportedPictureSizes.length];
            for(int i=0; i<supportedPictureSizes.length; i++){
                supportedPictureSizesAsStringArray[i] = supportedPictureSizes[i].width + "x" + supportedPictureSizes[i].height;
            }

            alert.setTitle("Wybierz rozmiar zdjęcia:");
            alert.setItems(supportedPictureSizesAsStringArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    cameraOptions.setPictureSize(supportedPictureSizes[which].width, supportedPictureSizes[which].height);
                    camera.setParameters(cameraOptions);
                }
            });
            alert.show();
            }
        });

        // footer buttons:
        changeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NB: if you don't release the current camera before switching, you app will crash
                camera.release();

                //swap the id of the camera to be used
                if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                    cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                camera = Camera.open(cameraId);

                setCameraDisplayOrientation(zdjecie.this, cameraId, camera);
                try {
                    camera.setPreviewDisplay(_cameraPreview.getSurfaceHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }
        });
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, camPictureCallback);
            }
        });
        savePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String mainFolderName = getString(R.string.mainFolderName);
            File SYS_pictures = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
            final File mainDir = new File(SYS_pictures, mainFolderName);

            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            final String newPhotoName = dFormat.format(new Date());

            AlertDialog.Builder alert = new AlertDialog.Builder(zdjecie.this);
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
                    String pathToSave = mainDir.getAbsolutePath() + File.separator + folderArray[which] + File.separator + newPhotoName;
                    try {
                        FileOutputStream fs = new FileOutputStream(pathToSave);
                        fs.write(photoData);
                        fs.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            alert.show();
            }
        });
    }

    private int getCameraId(){
        int cid = 0;
        int camerasCount = Camera.getNumberOfCameras(); // gdy więcej niż jedna kamera

        for (int i = 0; i < camerasCount; i++) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                cid = i;
            }
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cid = i;
            }
        }

        return cid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_zdjecie);
    }

    private void initPreview() {
        _cameraPreview = new CameraPreview(zdjecie.this, camera);
        camera_frameLayout = (FrameLayout) findViewById(R.id.camera_frameLayout);
        camera_frameLayout.addView(_cameraPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("camera","onPause");
        if (camera != null) {
            camera.stopPreview();
            _cameraPreview.getHolder().removeCallback(_cameraPreview);
            camera.release();
            camera = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("camera","onResume");
        initCamera();
        initPreview();
        addClickListeners();
        refreshCameraOptions();
        camera.startPreview();

        // draw circle:

        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        Circle circle = new Circle(zdjecie.this, screenSize);
        camera_frameLayout.addView(circle);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("camera","onRestart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("camera","onStop");
        if (camera != null) {
            camera.stopPreview();
            _cameraPreview.getHolder().removeCallback(_cameraPreview);
            camera.release();
            camera = null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("camera","onStart");
    }
//    onPause
//    onResume
//    onRestart
//    onStop
//    onStart
}
