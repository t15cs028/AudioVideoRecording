package com.example.storyboard;


import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.OnKeyboardVisibilityListener;
import com.example.camera.R;
import com.example.database.DBHelper;
import com.example.database.Stories;
import com.example.database.Table;

public class NewStoryFragment extends Fragment {


    private DBHelper dbHelper;
    private View rootView;
    private ViewGroup container;

    public NewStoryFragment(){

    }

    public void newInstances(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newstory_main, container, false);
        this.container = container;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.enter);
        final EditText text = (EditText) view.findViewById(R.id.name);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String date = dbHelper.getNowDateTime();
                SpannableStringBuilder sb = (SpannableStringBuilder) text.getText();
                String [] str = new String [] {
                        "id",
                        date,
                        sb.toString(),
                        "new"
                };
                for(int i = 0; i < str.length; i++){
                    System.out.println(i + " : " + str[i]);
                }

                // 名前の入力なし
                if (str[Stories.NAME.getNumber()].length() == 0) {
                    showError("Please input name!");
                }
                // 既に名前が存在する
                else if(dbHelper.existStoryName(str[Stories.NAME.getNumber()])) {
                    showError("This name Story exist. Try again other name...");
                }

                else {

                    // データをセットする
                    // 登録時にエラーが起きた
                    if(!dbHelper.setRecord(Table.STORIES, str)){
                        showError("error : Please try again...");
                    }
                    // 登録が成功した
                    else {
                        // テーブルに新しく挿入したデータのプライマリーキーを取得
                        String [] ids = dbHelper.getColumn(Table.STORIES, Stories.ID.getName(),
                                Stories.DATE.getName(), "'" + date + "'");

                        int id = 0;
                        for(String s :ids){
                            id = Integer.parseInt(s);
                        }

                        FragmentManager fragmentManager = getFragmentManager();

                        if (fragmentManager != null) {
                            fragmentManager.popBackStack();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            // BackStackを設定
                            fragmentTransaction.addToBackStack(null);
                            CompositionFragment compositionFragment = new CompositionFragment();
                            compositionFragment.newInstances(dbHelper, id);
                            fragmentTransaction.replace(R.id.container, compositionFragment);
                            fragmentTransaction.commit();
                        }
                    }
                }

            }
        });

        /*
        setKeyboardVisibilityListener(new OnKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                Log.i("Keyboard state", visible ? "Keyboard is active" : "Keyboard is Inactive");
            }
        });
        */
    }

    /*
    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = (ViewGroup)container.findViewById(R.id.container);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private boolean alreadyOpen;
                    private final int defaultKeyboardHeightDP = 100;
                    private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
                    private final Rect rect = new Rect();
                    @Override
                    public void onGlobalLayout() {
                        int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                        parentView.getWindowVisibleDisplayFrame(rect);
                        int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                        boolean isShown = heightDiff >= estimatedKeyboardHeight;
                        if (isShown == alreadyOpen) {
                            Log.i("Keyboard state", "Ignoring global layout change...");
                            return;
                        }
                        alreadyOpen = isShown;
                        onKeyboardVisibilityListener.onVisibilityChanged(isShown);
                    }
                }
        );
     }
     */

    /*
     * エラー等のToastを表示する
     * str : 表示したいString
     */
    public void showError(String str){
        Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        toast.show();
    }

}

