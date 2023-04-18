package com.acmefyblue.statussaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;

import com.acmefyblue.statussaver.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class ImageView extends AppCompatActivity {
    public android.widget.ImageView myImage;
    private ScaleGestureDetector scaleGestureDetector;
    private Float aFloat = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageButton img_btn = findViewById(R.id.imageButton2);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        Intent intent = getIntent();
        String file1 = intent.getStringExtra("file");
        File file = new File(file1);

        if (file.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            myImage = findViewById(R.id.image_view1);

            myImage.setImageBitmap(myBitmap);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            aFloat *= scaleGestureDetector.getScaleFactor();
            aFloat = Math.max(1f, Math.min(aFloat, 10.0f));
            myImage.setScaleX(aFloat);
            myImage.setScaleY(aFloat);
            return true;
        }

    }
}