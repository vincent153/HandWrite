package com.vincent.handwrite;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String TAG = "HandWrite";
    private PaintBoard2 paintBoard2;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintBoard2 = findViewById(R.id.paint_board);

        requestPermissionLauncher.launch(PERMISSIONS);

    }

    public void onClearClicked(View view){
        paintBoard2.clear();
    }

    public void OnSaveClicked(View view) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator+"Pictures"+ File.separator,
                    System.currentTimeMillis() + ".png");
            OutputStream stream = new FileOutputStream(file);
            paintBoard2.saveBitmap(stream);
            stream.close();
            galleryAddPic(file.getAbsolutePath());
            Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "save failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void galleryAddPic(String currentPhotoPath) {
        /*
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
         */
        MediaScannerConnection.scanFile(getApplicationContext(),
                new String[]{currentPhotoPath},
                null, null);
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
                boolean granted = true;
                for (Map.Entry<String, Boolean> x : isGranted.entrySet())
                    if (!x.getValue()) granted = false;
                if (granted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d(TAG,"all permission granted");
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

}