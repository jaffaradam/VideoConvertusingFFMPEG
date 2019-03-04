package com.examples.ffmpeg4android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.examples.ffmpeg4android_demo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private static final int RESULT_CODE_COMPRESS_VIDEO = 3;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button select_video = (Button)findViewById(R.id.select_Video);
        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, RESULT_CODE_COMPRESS_VIDEO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {


            if (requestCode == RESULT_CODE_COMPRESS_VIDEO) {
                Uri videoFileUri = data.getData();
try{
    String videoFileName = videoFileUri.getLastPathSegment();
    Log.e("deisplayname",videoFileName);

    File newfile;

    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
    FileInputStream in = videoAsset.createInputStream();

    File filepath = Environment.getExternalStorageDirectory();
    File dir = new File(filepath.getAbsolutePath() + "/" +"TempVideo" + "/");
    if (!dir.exists()) {
        dir.mkdirs();
    }

    newfile = new File(dir, "save_"+System.currentTimeMillis()+".mp4");

    if (newfile.exists()) newfile.delete();
    OutputStream out = new FileOutputStream(newfile);
    byte[] buf = new byte[1024];
    int len;

    while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
    }

    in.close();
    out.close();

    Bundle extras = new Bundle();
    extras.putString("tempfile", String.valueOf(newfile));
    Intent intent =  new Intent(MainActivity.this,SimpleExample.class);
    intent.putExtras(extras);
    startActivity(intent);

}catch (NullPointerException e){
    e.printStackTrace();
} catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

            }
        }
    }
}
