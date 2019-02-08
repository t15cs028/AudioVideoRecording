package com.example.storyboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.database.Sample;
import com.example.database.Table;

public class DetailCompositionFragment extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private int compositionID;
    private View rootView;

    public static DetailCompositionFragment newInstance(DBHelper dbHelper, int storyBoardNumber, int compositionID){
        DetailCompositionFragment fragment = new DetailCompositionFragment();
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
        System.out.println(String.valueOf(compositionID));
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(image);
        Glide.with(getContext()).load(Integer.parseInt(layout_id)).into(target);

        String sample_movie[] = dbHelper.getColumn(
                Table.SAMPLE, Sample.FILE_ID.getName(),
                Sample.COMPOSITION_ID.getName(), String.valueOf(compositionID)
        );

        image = (ImageView) rootView.findViewById(R.id.sample1);
        target = new GlideDrawableImageViewTarget(image);
        Glide.with(getContext()).load(Integer.parseInt(sample_movie[0])).into(target);
        /*
        image = (ImageView) rootView.findViewById(R.id.sample2);
        target = new GlideDrawableImageViewTarget(image);
        Glide.with(getContext()).load(Integer.parseInt(sample_movie[1])).into(target);
        */


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
