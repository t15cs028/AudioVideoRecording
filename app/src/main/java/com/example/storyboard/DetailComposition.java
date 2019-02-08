package com.example.storyboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Story;
import com.example.database.Table;

import static com.example.database.DBHelper.URL;

public class DetailComposition extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private int compositionID;
    private View rootView;

    public static DetailComposition newInstance(DBHelper dbHelper, int storyBoardNumber, int compositionID){
        DetailComposition fragment = new DetailComposition();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        fragment.compositionID = compositionID;
        return fragment;
    }
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detailcomposition_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image = (ImageView) rootView.findViewById(R.id.composition);
        String layout_id = dbHelper.getColumn(
                Table.COMPOSITION, Composition.THUMB_ID.getName(),
                Composition.ID.getName(), String.valueOf(compositionID)
        )[0];
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(image);
        Glide.with(getContext()).load(Integer.parseInt(layout_id)).into(target);


        Button button = (Button) view.findViewById(R.id.enter);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText desc = (EditText) view.findViewById(R.id.description);


        button.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            };

}
