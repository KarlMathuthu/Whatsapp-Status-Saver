package com.acmefyblue.statussaver.adapter;

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
import com.acmefyblue.statussaver.BtnClick2;
import com.acmefyblue.statussaver.R;
import com.acmefyblue.statussaver.model.Status;
import com.acmefyblue.statussaver.ui.VideoView;
import com.acmefyblue.statussaver.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final List<Status> videoList;
    private Context context;
    BtnClick2 btnClick;

    public VideoAdapter(List<Status> videoList, BtnClick2 btnClick) {
        this.videoList = videoList;
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

        final Status status = videoList.get(position);
        Glide.with(context).asBitmap().load(status.getFile()).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            final String path = videoList.get(position).getPath();

            Intent intent = new Intent(context, VideoView.class);
            intent.putExtra("file", path);
            context.startActivity(intent);
        });
        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/mp4");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share Video"));

        });

        LayoutInflater inflater = LayoutInflater.from(context);
        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("video/mp4");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share Video"));

        });
        holder.save.setOnClickListener(v -> {
            btnClick.btn_click2();
            checkFolder();
            final String path = videoList.get(position).getPath();
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
            new MaterialAlertDialogBuilder(context, R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
                    .setTitle("Video Saved")
                    .setMessage("Video has been saved to " + destPath + status.getTitle() + " .You can check the Video on Saved Files")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {

                    })
                    .show();


        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
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


}
