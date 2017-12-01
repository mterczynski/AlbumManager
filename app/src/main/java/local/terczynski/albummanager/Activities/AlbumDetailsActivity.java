package local.terczynski.albummanager.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import local.terczynski.albummanager.Helpers.CustomImageView;
import local.terczynski.albummanager.Helpers.DatabaseManager;
import local.terczynski.albummanager.R;

public class AlbumDetailsActivity extends AppCompatActivity {

    private DatabaseManager db;

    private Bitmap betterImageDecode(String filePath) {

        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("albumDetails","onCreate");
        Bundle bundle = getIntent().getExtras();
        final File currentDir = (File)bundle.get("currentDir");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        LinearLayout images = (LinearLayout)findViewById(R.id.LinearLayoutInScrollView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView deleteFolderButton = (ImageView)findViewById(R.id.deleteAlbumButton);
        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumDetailsActivity.this);
                alert.setTitle("Usuwanie");
                alert.setMessage("Czy usunąć folder \"" + currentDir.getName() + "\"?");
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        currentDir.delete();
                        Intent intent = new Intent(AlbumDetailsActivity.this, AlbumsActivity.class);
                        startActivity(intent);
                    }

                });
                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width1 = (size.x)/3*2;
        int width2 = (size.x)/3;

        Log.d("albumDetails","Pliki w folderze " + currentDir.getName() + ":");
        for(File a : currentDir.listFiles()){
            Log.d("albumDetails",a.getName());
        }


        final File[] zdjeciaWFolderze = currentDir.listFiles();
        int iloscZdjec = zdjeciaWFolderze.length;

        for(int i=0; i<iloscZdjec; i+= 4){
            final CustomImageView imageView1 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i].getAbsolutePath());
            CustomImageView imageView2 = null;
            CustomImageView imageView3 = null;
            CustomImageView imageView4 = null;

            Bitmap bmp1 = betterImageDecode(zdjeciaWFolderze[i].getAbsolutePath());
            imageView1.setImageBitmap(bmp1);

            if(i +1 < iloscZdjec){
                imageView2 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+1].getAbsolutePath());
                imageView2.getLayoutParams().width = width2;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+1].getAbsolutePath());
                imageView2.setImageBitmap(bmp);
            }
            if(i +2 < iloscZdjec){
                imageView3 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+2].getAbsolutePath());
                imageView3.getLayoutParams().width = width2;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+2].getAbsolutePath());
                imageView3.setImageBitmap(bmp);
            }
            if(i +3 < iloscZdjec){
                imageView4 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+3].getAbsolutePath());
                imageView4.getLayoutParams().width = width1;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+3].getAbsolutePath());
                imageView4.setImageBitmap(bmp);
            }

            imageView1.getLayoutParams().width = width1;

            LinearLayout linearny = new LinearLayout(AlbumDetailsActivity.this);
            LinearLayout linearny2 = new LinearLayout(AlbumDetailsActivity.this);
            linearny.addView(imageView1);
            if(imageView2 != null){
                linearny.addView(imageView2);
            }
            if(imageView3 != null){
                linearny2.addView(imageView3);
            }
            if(imageView4 != null){
                linearny2.addView(imageView4);
            }

            final int finalI = i;
            if(imageView1 != null){
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            if(imageView2 != null){
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+1].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });

            }
            if(imageView3 != null){
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+2].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            if(imageView4 != null){
                imageView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+3].getAbsolutePath());
                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));
                    startActivity(newIntent);
                    }
                });
            }

            images.addView(linearny);
            images.addView(linearny2);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {

        db = new DatabaseManager(
            AlbumDetailsActivity.this, // activity z galerią zdjęć
            getString(R.string.dbName), // database name
            null,
            7 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );

        Bundle bundle = getIntent().getExtras();
        final File currentDir = (File)bundle.get("currentDir");
        super.onResume();
        setContentView(R.layout.activity_album_details);

        LinearLayout images = (LinearLayout)findViewById(R.id.LinearLayoutInScrollView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView deleteFolderButton = (ImageView)findViewById(R.id.deleteAlbumButton);

        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(AlbumDetailsActivity.this);
            alert.setTitle("Usuwanie");
            alert.setMessage("Czy usunąć folder \"" + currentDir.getName() + "\"?");
            alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    currentDir.delete();
                    Intent intent = new Intent(AlbumDetailsActivity.this, AlbumsActivity.class);
                    startActivity(intent);
                }

            });
            alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width1 = (size.x)/3*2;
        int width2 = (size.x)/3;

        Log.d("albumDetails","Pliki w folderze " + currentDir.getName() + ":");
        for(File a : currentDir.listFiles()){
            Log.d("albumDetails",a.getName());
        }


        final File[] zdjeciaWFolderze = currentDir.listFiles();
        int iloscZdjec = zdjeciaWFolderze.length;

        for(int i=0; i<iloscZdjec; i+= 4){
            final CustomImageView imageView1 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i].getAbsolutePath());
            CustomImageView imageView2 = null;
            CustomImageView imageView3 = null;
            CustomImageView imageView4 = null;

            Bitmap bmp1 = betterImageDecode(zdjeciaWFolderze[i].getAbsolutePath());
            imageView1.setImageBitmap(bmp1);

            if(i +1 < iloscZdjec){
                imageView2 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+1].getAbsolutePath());
                imageView2.getLayoutParams().width = width2;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+1].getAbsolutePath());
                imageView2.setImageBitmap(bmp);
            }
            if(i +2 < iloscZdjec){
                imageView3 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+2].getAbsolutePath());
                imageView3.getLayoutParams().width = width2;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+2].getAbsolutePath());
                imageView3.setImageBitmap(bmp);
            }
            if(i +3 < iloscZdjec){
                imageView4 = new CustomImageView(AlbumDetailsActivity.this, R.drawable.net_icon, db, zdjeciaWFolderze[i+3].getAbsolutePath());
                imageView4.getLayoutParams().width = width1;
                Bitmap bmp = betterImageDecode(zdjeciaWFolderze[i+3].getAbsolutePath());
                imageView4.setImageBitmap(bmp);
            }

            imageView1.getLayoutParams().width = width1;

            LinearLayout linearny = new LinearLayout(AlbumDetailsActivity.this);
            LinearLayout linearny2 = new LinearLayout(AlbumDetailsActivity.this);
            linearny.addView(imageView1);
            if(imageView2 != null){
                linearny.addView(imageView2);
            }
            if(imageView3 != null){
                linearny2.addView(imageView3);
            }
            if(imageView4 != null){
                linearny2.addView(imageView4);
            }

            final int finalI = i;
            if(imageView1 != null){
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            if(imageView2 != null){
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+1].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            if(imageView3 != null){
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+2].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            if(imageView4 != null){
                imageView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent newIntent = new Intent(AlbumDetailsActivity.this, PictureDetailsActivity.class);
                    newIntent.putExtra("pictureFullPath",zdjeciaWFolderze[finalI+3].getAbsolutePath());

                    newIntent.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));

                    startActivity(newIntent);
                    }
                });
            }
            images.addView(linearny);
            images.addView(linearny2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("albumDetails","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("albumDetails","onPause");
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("albumDetails","onDestroy");
        db.close();
    }
}
