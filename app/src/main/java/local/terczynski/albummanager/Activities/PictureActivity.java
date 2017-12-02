package local.terczynski.albummanager.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
import java.util.UUID;

import local.terczynski.albummanager.Helpers.CameraPreview;
import local.terczynski.albummanager.Helpers.Circle;
import local.terczynski.albummanager.Helpers.Miniature;
import local.terczynski.albummanager.Helpers.PictureSaver;
import local.terczynski.albummanager.R;

public class PictureActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout camera_frameLayout;

    // byte[] of taken pictures
    private ArrayList<byte[]> photosData = new ArrayList<byte[]>() ;
    // camera options:
    private Camera.Parameters cameraOptions;
    private String[] whiteBalanceOptions;
    private String[]supportedColorEffects;
    private Camera.Size[] supportedPictureSizes;
    private ArrayList<String> exposureCompensationOptions;
    private Point screenSize;

    // header buttons:
    private ImageView colorsButton;
    private ImageView flashButton;
    private ImageView sunButton;
    private ImageView resizeButton;
    // footer buttons:
    private ImageView changeCameraButton;
    private ImageView takePictureButton;
    private ImageView savePictureButton;

    private double circleDiameter = 125;

    private List<Miniature> miniatures = new ArrayList<Miniature>();
    private OrientationEventListener orientationEventListener;

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

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
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
            // refresh camera (zapobiega to przycięciu się kamery po zrobieniu zdjęcia)
            camera.startPreview();
// miniatures:
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap , 100, 100, false);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            Bitmap rotatedSmallBitmap = Bitmap.createBitmap(smallBitmap, 0, 0, smallBitmap.getWidth(), smallBitmap.getHeight(), matrix, true);
            Miniature newMiniature = new Miniature(PictureActivity.this, rotatedSmallBitmap, new Point(100,100));

            newMiniature.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(PictureActivity.this);
                    String[] options = {"podgląd zdjęcia", "usuń bieżące", "zapisz bieżące"};
                    alert.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0){ // preview this picture

                            } else if(i == 1){ // delete this picture

                            } else if(i == 2){ // save this picture
//                                PictureSaver.savePicture("", );

                            }
                        }
                    });
                    alert.show();
                    return false;
                }
            });

            miniatures.add(newMiniature);

            photosData.add(data);

            for(int i=0; i<miniatures.size(); i++){ // display miniatures
                double angle = 2 * Math.PI/(miniatures.size()) * i;

                int diffX = (int)(Math.cos(angle) * circleDiameter) - Miniature.size.x/4;
                int diffY = (int)(circleDiameter * Math.sin(angle)) - Miniature.size.y/4;

                Miniature miniature = miniatures.get(i);
                miniature.setX(screenSize.x/4 + diffX);
                miniature.setY(screenSize.y/4 + diffY);

                Log.d("miniaturePosition", miniature.getX() + "," + miniature.getY());

                camera_frameLayout.removeView(miniature);
                camera_frameLayout.addView(miniature);
            }
        }
    };

    private void addClickListeners(){


        // header buttons:
        colorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(PictureActivity.this);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(PictureActivity.this);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(PictureActivity.this);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(PictureActivity.this);


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

                setCameraDisplayOrientation(PictureActivity.this, cameraId, camera);
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

            final SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            AlertDialog.Builder alert = new AlertDialog.Builder(PictureActivity.this);
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

                        for(byte[] data : photosData){
                            String newPhotoName = UUID.randomUUID().toString();
                            String pathToSave = mainDir.getAbsolutePath() + File.separator + folderArray[which] + File.separator + newPhotoName;

                            new PictureSaver(PictureActivity.this).savePicture(data, pathToSave);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(-90);
//                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//                            FileOutputStream fs = new FileOutputStream(pathToSave);
//                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fs);
//                            fs.close();
                        }

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
        setContentView(R.layout.activity_picture);

        orientationEventListener = new OrientationEventListener(PictureActivity.this) {
            int previousOrientation = 0; // {0, 90, 180, 270}

            private void runAnim(int newOrientation, ImageView[] buttons){
                if(newOrientation == previousOrientation){
                    return;
                }
                if(previousOrientation == 270 && newOrientation == 0){
                    newOrientation = 360;
                } else if(previousOrientation == 0 && newOrientation == 270){
                    previousOrientation = 360;
                }

                for(ImageView miniature : miniatures){
                    Animation anim = new RotateAnimation(-previousOrientation, -newOrientation,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    anim.setRepeatCount(0);
                    anim.setDuration(1000);
                    anim.setFillAfter(true);

                    miniature.startAnimation(anim);
                }
                for(ImageView button : buttons){
                    ObjectAnimator.ofFloat(button, View.ROTATION, -previousOrientation, -newOrientation)
                            .setDuration(300)
                            .start();
                }
                Log.d("rotateAnim", "from " + previousOrientation + "to " + newOrientation);
                previousOrientation = newOrientation;
            }

            @Override
            public void onOrientationChanged(int angle) {
                ImageView[] buttons = {colorsButton, flashButton, sunButton, resizeButton, changeCameraButton, takePictureButton, savePictureButton};
                int inaccuracy = 20; // must be (0-90)
                if(angle > 360 - inaccuracy || angle < inaccuracy){
                    runAnim(0, buttons);
                    previousOrientation = 0;
                } else if(angle < 90 + inaccuracy && angle > 90 - inaccuracy){
                    runAnim(90, buttons);
                    previousOrientation = 90;
                } else if(angle < 180 + inaccuracy && angle > 180 - inaccuracy){
                    runAnim(180, buttons);
                    previousOrientation = 180;
                } else if(angle < 270 + inaccuracy && angle > 270 - inaccuracy){
                    runAnim(270, buttons);
                    previousOrientation = 270;
                }
                //Log.d("orientation","angle: " + angle);
                // angle zwraca kąt 0 - 360 stopni podczas obracania ekranem w osi Z
                // tutaj wykonaj animacje butonów i miniatur zdjęć
            }
        };
    }

    private void initPreview() {
        _cameraPreview = new CameraPreview(PictureActivity.this, camera);
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
        orientationEventListener.disable();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("camera","onResume");
        initCamera();
        initPreview();
        refreshCameraOptions();
        camera.startPreview();

        // header buttons:
        colorsButton = (ImageView)findViewById(R.id.colorsButton);
        flashButton = (ImageView)findViewById(R.id.flashButton);
        sunButton = (ImageView)findViewById(R.id.sunButton);
        resizeButton = (ImageView)findViewById(R.id.resizeButton);
        // footer buttons:
        changeCameraButton = (ImageView)findViewById(R.id.changeCamera);
        takePictureButton = (ImageView)findViewById(R.id.takePicture);
        savePictureButton = (ImageView) findViewById(R.id.savePicture);

        addClickListeners();
        // draw circle:

        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        Circle circle = new Circle(PictureActivity.this, screenSize);
        camera_frameLayout.addView(circle);
        // resume orientationEventListener
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
            // Log - listener działa
            Log.d("accel", "working");
        } else {
            // Log - listener działa
            Log.d("accel", "not working");
        }
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
