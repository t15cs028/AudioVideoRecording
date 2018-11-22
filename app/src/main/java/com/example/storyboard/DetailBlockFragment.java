package com.example.storyboard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Story;
import com.example.database.Table;

import static com.example.database.DBHelper.URL;

public class DetailBlockFragment extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private int compositionID;
    private View rootView;

    public DetailBlockFragment(){

    }

    public void newInstances(DBHelper dbHelper, int storyBoardNumber, int compositionID){
        this.dbHelper = dbHelper;
        this.storyBoardNumber = storyBoardNumber;
        this.compositionID = compositionID;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detailblock_main, container, false);
        return rootView;
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    */

    // /*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image = (ImageView) rootView.findViewById(R.id.composition);
        String layout_id [] = dbHelper.getColumn(
                Table.COMPOSITION, Composition.THUMB_ID.getName(),
                Composition.ID.getName(), String.valueOf(compositionID)
        );
        image.setImageResource(Integer.parseInt(layout_id[0]));


        Button button = (Button) view.findViewById(R.id.enter);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText desc = (EditText) view.findViewById(R.id.description);


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                SpannableStringBuilder [] sb =
                        new SpannableStringBuilder[]{
                                (SpannableStringBuilder) name.getText(),
                                (SpannableStringBuilder) desc.getText()
                        };


                int order = dbHelper.getNumOfRecord(Table.STORY,
                        Story.STORIES_ID.getName(), String.valueOf(storyBoardNumber));

                String [] str =
                        new String[]{
                                "id", String.valueOf(order),
                                sb[0].toString(), String.valueOf(compositionID),
                                sb[1].toString(), URL, String.valueOf(storyBoardNumber)
                        };

                // 名前の入力なし
                if (sb[0].toString().length() == 0 || sb[1].toString().length() == 0) {
                    showError("Please input name!");
                }

                /*
                // 既に名前が存在する
                else if(dbHelper.existStoryName(str)) {
                    showError("This name Story exist. Try again other name...");
                }
                */

                else {
                    // データをセットする
                    // 登録時にエラーが起きた
                    if(!dbHelper.setRecord(Table.STORY, str)){
                        showError("error : Please try again...");
                    }
                    // 登録が成功した
                    else {
                        FragmentManager fragmentManager = getFragmentManager();

                        if (fragmentManager != null) {
                            // 二つ前の画面（絵コンテ）に戻る
                            fragmentManager.popBackStack();

                        }
                    }
                }

            }
        });
    }
    // */

    /*
     * エラー等のToastを表示する
     * str : 表示したいString
     */
    public void showError(String str){
        Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
