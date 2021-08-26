package com.delta_inductions.cacheapp;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://random.responsiveimages.io/v1/docs";
    private ImageView imageView;
    private Button load;
    private ProgressBar loader;
    private static final String TAG = "MainActivity";
    private String fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = findViewById(R.id.loader);
        imageView = findViewById(R.id.imageView);
        load = findViewById(R.id.load);
        Picasso.get().load(new File("/storage/emulated/0/Pictures/Picasso/Cacheimage.jpg")).fit().centerInside().into(imageView);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Picasso.get().load(URL).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File folder = new File(sd, "/Picasso/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdir();
                                }
                            }
                            fileUri = folder.getAbsolutePath() + File.separator + "Cacheimage.jpg";
                            Log.d(TAG, "onBitmapLoaded: "+fileUri);
                            FileOutputStream ostream = new FileOutputStream(fileUri);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageBitmap(bitmap);
                            loader.setVisibility(View.GONE);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }});
}
}
