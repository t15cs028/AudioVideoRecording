package com.example.storyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camera.R;

import java.util.List;

public class BlockListAdapter_bak extends ArrayAdapter<Block> {

    private int resource;
    private List<Block> blocks;
    private LayoutInflater inflater;
    private BlockListAdapterListener listener;

    /*
     * コンストラクタ
     */

    public BlockListAdapter_bak(Context context, int resource, List<Block> blocks){
        super(context, resource, blocks);

        this.resource = resource;
        this.blocks = blocks;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getResource(){
        return resource;
    }

    public List<Block> getBlocks(){
        return blocks;
    }

    public LayoutInflater getInflater(){
        return inflater;
    }

    public interface BlockListAdapterListener {
        void myFunc(Object position);
    }

    public void setListener(BlockListAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = inflater.inflate(resource, parent, false);
        }

        // リストビューに表示する要素を取得
        Block block = blocks.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(block.getThumbnail());

        // タイトルを設定
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(block.getName());

        // 録画ボタンを設定
        ImageButton record = (ImageButton) view.findViewById(R.id.recordButton);
        record.setTag(position);
        record.setImageBitmap(block.getRecord());
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.myFunc(v.getTag());
            }
        });

        return view;
    }


}
