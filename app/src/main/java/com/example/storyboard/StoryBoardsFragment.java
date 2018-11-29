package com.example.storyboard;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.os.Bundle;import android.support.annotation.NonNull;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentTransaction;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.AdapterView;import android.widget.ListView;import com.example.camera.R;import com.example.database.DBHelper;import com.example.database.Stories;import com.example.database.Table;import com.example.dialog.BlockDialogFragment;import java.util.ArrayList;public class StoryBoardsFragment extends Fragment {    // private Button [] button;    private DBHelper dbHelper;    private View rootView;    /*    private ViewGroup container;    */    public static StoryBoardsFragment newInstance(DBHelper dbHelper){        StoryBoardsFragment fragment = new StoryBoardsFragment();        fragment.dbHelper = dbHelper;        return fragment;    }    public StoryBoardsFragment() {    }    @Override    public void onCreate(Bundle saveInstanceState){        super.onCreate(saveInstanceState);        setRetainInstance(true);    }    @Nullable    @Override    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,                             @Nullable Bundle savedInstanceState) {        rootView = inflater.inflate(R.layout.storyboards_main, container, false);        return rootView;    }    @Override    public void onActivityCreated(Bundle savedInstanceState) {        super.onActivityCreated(savedInstanceState);        String name[] = dbHelper.getColumn(Table.STORIES, Stories.NAME.getName());        if (name != null){            ArrayList<StoryList> listItem = new ArrayList<>();            ListView list = (ListView) rootView.findViewById(R.id.sample_listview);            Bitmap bmp = BitmapFactory.decodeResource(getResources(),                    R.drawable.ic_launcher_round);            for(int i = 0; i < name.length; i++) {                StoryList block = new StoryList(i, name[i], bmp);                listItem.add(block);            }            StoryListAdapter adapter =                    new StoryListAdapter(getActivity(), R.layout.liststories_main, listItem);            list.setAdapter(adapter);            list.setOnItemClickListener(onItemClickListener);            list.setOnItemLongClickListener(onItemLongClickListener);        }    }    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {        @Override        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {            /*            // タップしたアイテムの取得            ListView listView = (ListView)parent;            Block item = (Block)listView.getItemAtPosition(position);  // SampleListItemにキャスト            */            // Log.d(TAG, "onListItemClick position => " + position + " : id => " + id);            // タッチされた絵コンテ作品のIDを取得            String storiesID[] = dbHelper.getColumn(Table.STORIES, Stories.ID.getName());            FragmentManager fragmentManager = getFragmentManager();            if (fragmentManager != null) {                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();                // BackStackを設定                fragmentTransaction.addToBackStack(null);                int storyID = Integer.parseInt(storiesID[position]);                StoryBoardFragment storyBoardFragment = StoryBoardFragment.newInstance(dbHelper, storyID);                fragmentTransaction.replace(R.id.container, storyBoardFragment);                fragmentTransaction.commit();            }        }    };    // リストをロングクリックしたときの動作    private AdapterView.OnItemLongClickListener onItemLongClickListener            = new AdapterView.OnItemLongClickListener(){        @Override        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){            final BlockDialogFragment dialog = BlockDialogFragment.newInstance(dbHelper);            dialog.setTargetFragment(null, 100);            /*            Bundle bundle = new Bundle();            bundle.putInt("aaa", 0);            dialog.setArguments(bundle);            */            dialog.show(getChildFragmentManager(), "my_dialog");            return true;        }    };}