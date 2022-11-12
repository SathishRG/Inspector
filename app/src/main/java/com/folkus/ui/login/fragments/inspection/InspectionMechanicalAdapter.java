package com.folkus.ui.login.fragments.inspection;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.folkus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class InspectionMechanicalAdapter extends RecyclerView.Adapter<InspectionMechanicalAdapter.RecyclerViewHolder> {

    private Context mContext;
    InspectionMechanicalFragment mechanicalFragment;
    private ArrayList<Uri> picturesList;

    public InspectionMechanicalAdapter(Context context, ArrayList<Uri> stringArrayList, InspectionMechanicalFragment addImageActivityResultLauncher) {
        this.mContext = context;
        this.picturesList = stringArrayList;
        this.mechanicalFragment = addImageActivityResultLauncher;
    }

    @NonNull
    @Override
    public InspectionMechanicalAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inspection_add_image_video_item_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionMechanicalAdapter.RecyclerViewHolder holder, int position) {
        Uri listOfPictures = picturesList.get(position);
        int itemCount = getItemCount() - 1;

        if (position == itemCount) {
            Glide.with(mContext).load(R.drawable.plus_icon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
        }
        boolean isImage = isImageFile(listOfPictures);
        boolean isVideo = isVideoFile(listOfPictures);

        if (isImage) {
            Glide.with(mContext).load(listOfPictures)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        } else if (isVideo) {
            try {
                Uri selectedVideoUri = picturesList.get(position);
                String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                Cursor cursor = mContext.getContentResolver().query(selectedVideoUri, projection, null, null, null);
                cursor.moveToFirst();
                String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                // Setting the thumbnail of the video in to the image view
                BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                holder.videoView.setBackground(bitmapD);
            } catch (IllegalArgumentException e) {
                Log.e("onBindViewHolder: ", e.getMessage());
                e.printStackTrace();
            }
            holder.videoView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == itemCount) {
                    mechanicalFragment.addMultipleImageAndVideos();
                }
            }
        });

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == itemCount) {
                    mechanicalFragment.addMultipleImageAndVideos();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }

    public void updateList(ArrayList<Uri> picturePathsList) {
        this.picturesList = picturePathsList;
        Collections.reverse(this.picturesList);
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public VideoView videoView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.add_btn);
            videoView = itemView.findViewById(R.id.video_view);
        }
    }


    public boolean isImageFile(Uri uri) {
        if (uri != null) {
            ContentResolver cR = mContext.getContentResolver();
            String mimeType = cR.getType(uri);
            return mimeType != null && mimeType.startsWith("image");
        }
        return false;
    }

    public boolean isVideoFile(Uri uri) {
        if (uri != null) {
            ContentResolver cR = mContext.getContentResolver();
            String mimeType = cR.getType(uri);
            return mimeType != null && mimeType.startsWith("video");
        }
        return false;
    }
}
