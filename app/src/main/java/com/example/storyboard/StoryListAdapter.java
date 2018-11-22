package com.example.storyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camera.R;

import java.util.List;

public class StoryListAdapter extends ArrayAdapter<StoryList> {

    private int resource;
    private List<StoryList> stories;
    private LayoutInflater inflater;

    /*
     * コンストラクタ
     */

    public StoryListAdapter(Context context, int resource, List<StoryList> stories){
        super(context, resource,  stories);

        this.resource = resource;
        this.stories = stories;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getResource(){
        return resource;
    }

    public List<StoryList> getBlocks(){
        return stories;
    }

    public LayoutInflater getInflater(){
        return inflater;
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
        StoryList story = stories.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(story.getThumbnail());

        // タイトルを設定
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(story.getName());

        return view;
    }


}
