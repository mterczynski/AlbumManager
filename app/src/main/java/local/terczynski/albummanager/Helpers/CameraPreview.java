package local.terczynski.albummanager.Helpers;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements Callback {
    private Camera _camera;
    private SurfaceHolder _surfaceHolder;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        Log.d("camera","Utworzono obiekt klasy CameraPreview");

        this._camera = camera;
        this._surfaceHolder = this.getHolder();
        this._surfaceHolder.addCallback(this);

        _camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            _camera.setPreviewDisplay(_surfaceHolder);
            _camera.startPreview();
            Log.d("camera","Wywołano metodę surfaceCreated");
        } catch(Exception ex){
            ex.printStackTrace();
            Log.d("camera", ex.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            _camera.setPreviewDisplay(_surfaceHolder);
            _camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public SurfaceHolder getSurfaceHolder(){
        return this._surfaceHolder;
    }
}
