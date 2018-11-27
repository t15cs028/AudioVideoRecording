package com.example.storyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camera.R;

import java.util.List;

import static com.example.database.DBHelper.TAG;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

    // thumbnail or composition image
    private List<Integer> itemImages;
    private List<Bitmap> itemThumbnails;

    // block name
    private List<String> itemNames;
    // record button
    private List<Integer> itemCameras;

    private OnRecyclerListener listener;

    private ViewHolder holder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageView;
        TextView textView;
        ImageButton imageButton;

        ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.thumbnail);
            textView = (TextView) v.findViewById(R.id.title);
            imageButton = (ImageButton) v.findViewById(R.id.recordButton);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public BlockListAdapter(
            List<Integer> itemImages,
            List<Bitmap> itemThumbnails,
            List<String> itemNames,
            List<Integer> itemCamera,
            OnRecyclerListener listener) {
        this.itemImages = itemImages;
        this.itemThumbnails = itemThumbnails;
        this.itemNames = itemNames;
        this.itemCameras = itemCamera;
        this.listener = listener;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listblocks_main, parent, false);
        holder = new ViewHolder(view);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        System.out.println(itemImages.get(position));
        holder.imageView.setImageResource(itemImages.get(position));
        if(itemThumbnails.get(position) != null){
            holder.imageView.setImageBitmap(itemThumbnails.get(position));
        }
        holder.textView.setText(itemNames.get(position));
        holder.imageButton.setImageResource(itemCameras.get(position));

        // クリックリスナを搭載
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClicked(v, position);
                // final int position = v.getAdapterPosition(); // positionを取得
                // 何かの処理をします
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraClicked(v, position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public ViewHolder getHolder(){
        return holder;
    }

}
