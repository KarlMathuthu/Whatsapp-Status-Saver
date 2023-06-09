package com.acmefyblue.statussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.acmefyblue.statussaver.R;
import com.acmefyblue.statussaver.model.Status;
import com.acmefyblue.statussaver.ui.ImageView;
import com.acmefyblue.statussaver.ui.VideoView;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final List<Status> imagesList;
    private Context context;

    public SavedAdapter(List<Status> imagesList) {
        this.imagesList = imagesList;
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

        holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24));
        holder.share.setVisibility(View.VISIBLE);
        holder.save.setVisibility(View.VISIBLE);

        final Status status = imagesList.get(position);

        if (status.isVideo())
            Glide.with(context).asBitmap().load(status.getFile()).into(holder.imageView);

        else
            Picasso.get().load(status.getFile()).into(holder.imageView);

        holder.save.setOnClickListener(view -> new MaterialAlertDialogBuilder(context,R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
                .setMessage("Are you sure you want to delete this item")
                .setPositiveButton("Delete", (dialogInterface, i) -> {

                    if (status.getFile().delete()) {
                        imagesList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .show());
        holder.imageView.setOnClickListener(v -> {
            final String path = imagesList.get(position).getPath();
            Intent intent;
            if (!imagesList.get(position).isVideo()) {
                intent = new Intent(context, ImageView.class);


            } else {
                intent = new Intent(context, VideoView.class);

            }
            intent.putExtra("file", path);
            context.startActivity(intent);

        });


        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            if (status.isVideo())
                shareIntent.setType("image/mp4");
            else
                shareIntent.setType("image/jpg");

            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
