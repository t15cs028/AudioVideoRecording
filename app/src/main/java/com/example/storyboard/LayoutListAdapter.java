package com.example.storyboard;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camera.R;

import java.util.List;

public class LayoutListAdapter extends ArrayAdapter<LayoutBlock> {

    private int resource;
    private List<LayoutBlock> layouts;
    private LayoutInflater inflater;
    private BlockListAdapterListener listener;

    /*
     * コンストラクタ
     */

    public LayoutListAdapter(Context context, int resource, List<LayoutBlock> layouts){
        super(context, resource, layouts);
        this.resource = resource;
        this.layouts = layouts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getResource(){
        return resource;
    }

    public List<LayoutBlock> getLayouts(){
        return layouts;
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
            view = inflater.inflate(resource, null);
        }



        // リストビューに表示する要素を取得
        LayoutBlock layout = layouts.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(layout.getThumbnail());

        // タイトルを設定
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(layout.getName());

        // 説明を設定
        TextView description = (TextView)view.findViewById(R.id.description);
        description.setText(layout.getDescription());


        return view;
    }


}
