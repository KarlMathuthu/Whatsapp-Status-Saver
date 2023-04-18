package com.acmefyblue.statussaver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acmefyblue.statussaver.BtnClick;
import com.acmefyblue.statussaver.R;
import com.acmefyblue.statussaver.model.Status;
import com.acmefyblue.statussaver.ui.ImageView;
import com.acmefyblue.statussaver.utils.Common;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final List<Status> imagesList;
    private Context context;
    BtnClick btnClick;


    public ImageAdapter(List<Status> imagesList,BtnClick btnClick) {
        this.imagesList = imagesList;
        this.btnClick = btnClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        final Status status = imagesList.get(position);
        Picasso.get().load(status.getFile()).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            final String path = imagesList.get(position).getPath();

            Intent intent = new Intent(context, ImageView.class);
            intent.putExtra("file", path);
            context.startActivity(intent);
        });
        holder.save.setOnClickListener(v -> {
            btnClick.btn_click();
            checkFolder();
            final String path = imagesList.get(position).getPath();
            final File file = new File(path);
            String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.APP_DIR;
            final File destFile = new File(destPath);
            try {
                FileUtils.copyFileToDirectory(file, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(context, new String[]{destPath + status.getTitle()},
                    new String[]{"*/*"}, new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {

                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
            new MaterialAlertDialogBuilder(context,R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
                    .setTitle("Image Saved")
                    .setMessage("Image has been saved to " + destPath + status.getTitle() + " .You can check the image on Saved Files")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {

                    })
                    .show();

        });

        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });


    }

    private void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.APP_DIR;
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();

        } else {
            Log.d("status saver", "created");
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
