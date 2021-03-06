package com.example.storyboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Sample;
import com.example.database.Table;

import org.w3c.dom.Text;

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
        String desc = dbHelper.getColumn(
                Table.COMPOSITION, Composition.DESCRIPTION.getName(),
                Composition.ID.getName(), String.valueOf(compositionID)
        )[0];

        String sample_movie[] = dbHelper.getColumn(
                Table.SAMPLE, Sample.FILE_ID.getName(),
                Sample.COMPOSITION_ID.getName(), String.valueOf(compositionID)
        );

        if(sample_movie.length  != 0) {
            ImageView image = (ImageView) rootView.findViewById(R.id.sample1);
            GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(image);
            Glide.with(getContext()).load(Integer.parseInt(sample_movie[0])).into(target);
            if(sample_movie.length > 1) {
                image = (ImageView) rootView.findViewById(R.id.sample2);
                target = new GlideDrawableImageViewTarget(image);
                Glide.with(getContext()).load(Integer.parseInt(sample_movie[1])).into(target);
            }
        }


        Button button = (Button) view.findViewById(R.id.enter);
        final TextView description = rootView.findViewById(R.id.description);
        description.setText(desc);

        button.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    if (fragmentManager != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // BackStackを設定
                        fragmentTransaction.addToBackStack(null);
                        DetailSettingFragment detailSettingFragment
                                = DetailSettingFragment.newInstance(dbHelper, storyBoardNumber, compositionID);

                        fragmentTransaction.replace(R.id.container, detailSettingFragment);
                        fragmentTransaction.commit();
                    }
                }
            };

}
