package com.example.storyboard;



import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.camera.CameraFragment;
import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Story;
import com.example.database.Table;
import com.example.encoder.MediaPlayFragment;
import com.example.encoder.VideoExport;

;import java.util.ArrayList;
import java.util.List;

import static com.example.database.DBHelper.TAG;
import static com.example.database.DBHelper.URL;

public class StoryBoardFragment extends Fragment implements OnRecyclerListener {

    class DataSet{
        String id;
        String order;
        String name;
        String layout;
        String description;
        String url;
    }

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private View rootView;

    private RecyclerView.Adapter adapter;
    // url or composition image
    private List<Integer> itemImages;
    private List<Bitmap> itemThumbnails;
    // block name
    private List<String> itemNames;
    // record button
    private List<Integer> itemCameras;


    public static StoryBoardFragment newInstance(DBHelper dbHelper, int storyBoardNumber){
        StoryBoardFragment fragment = new StoryBoardFragment();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        return fragment;
    }

    public StoryBoardFragment() {

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.storyboard_main, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);




        DataSet [] data = setData();

        if(data != null) {
            itemImages = new ArrayList<>();
            itemThumbnails = new ArrayList<>();
            itemNames = new ArrayList<>();
            itemCameras = new ArrayList<>();


            for (int i = 0; i < data.length; i++) {

                itemImages.add(Integer.parseInt(data[i].layout));
                itemNames.add(data[i].url);
                itemCameras.add(android.R.drawable.ic_menu_camera);
                if (!URL.equals(data[i].url)) {
                    ThumbnailUtils tu = new ThumbnailUtils();
                    Bitmap bmp
                            = tu.createVideoThumbnail(data[i].url,
                            MediaStore.Video.Thumbnails.MINI_KIND);
                    itemThumbnails.add(bmp);
                } else {
                    itemThumbnails.add(null);
                }
            }


            // specify an adapter (see also next example)
            adapter = new BlockListAdapter(itemImages, itemThumbnails, itemNames, itemCameras, this);
            recyclerView.setAdapter(adapter);


            // ItemTouchHelper
            ItemTouchHelper itemDecor = new ItemTouchHelper(
                    new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(RecyclerView recyclerView,
                                              RecyclerView.ViewHolder viewHolder,
                                              RecyclerView.ViewHolder target) {

                            final int fromPos = viewHolder.getAdapterPosition();
                            final int toPos = target.getAdapterPosition();

                            DataSet[] data = setData();

                            // 移動したときに順番TURNを変更
                            dbHelper.setField(Table.STORY, data[fromPos].id,
                                    Story.TURN.getName(), data[toPos].order);

                            dbHelper.setField(Table.STORY, data[toPos].id,
                                    Story.TURN.getName(), data[fromPos].order);

                            adapter.notifyItemMoved(fromPos, toPos);
                            return true;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                             int direction) {
                            final int fromPos = viewHolder.getAdapterPosition();
                            itemImages.remove(fromPos);
                            itemNames.remove(fromPos);
                            itemCameras.remove(fromPos);

                            String id = setData()[fromPos].id;
                            // dbHelper.deleteRecord(Table.STORY, id[fromPos]);
                            dbHelper.deleteStoryRecord(id,
                                    String.valueOf(storyBoardNumber));

                            adapter.notifyItemRemoved(fromPos);
                        }
                    });
            itemDecor.attachToRecyclerView(recyclerView);

            Button newBlock = (Button) rootView.findViewById(R.id.newItem);
            Button writeDown = (Button) rootView.findViewById(R.id.writeDown);
            newBlock.setOnClickListener(onClickListenerNew);
            writeDown.setOnClickListener(onClickListenerWrite);


        }

    }

    @Override
    public void onRecyclerClicked(View v, int position){
        Log.d(TAG, "onListItemClick position => " + position);
        String id = setData()[position].id;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // BackStackを設定
            fragmentTransaction.addToBackStack(null);

            String path = setData()[position].url;
            if(path.equals("new")){
                BlockFragment fragment
                        = BlockFragment.newInstance(dbHelper, storyBoardNumber, Integer.parseInt(id));
                fragmentTransaction.replace(R.id.container, fragment);
            }
            else {
                MediaPlayFragment fragment
                        = MediaPlayFragment.newInstance(dbHelper, Integer.parseInt(id), Table.STORY);
                fragmentTransaction.replace(R.id.container, fragment);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCameraClicked(View v, int position){
        String id = setData()[position].id;
        String layout
                = dbHelper.getColumn(
                        Table.COMPOSITION, Composition.ID.getName(),
                        Composition.THUMB_ID.getName(), setData()[position].layout
                )[0];
        System.out.println(Integer.parseInt(layout));
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // BackStackを設定
            fragmentTransaction.addToBackStack(null);

            int blockID = Integer.parseInt(id);
            int layoutID = Integer.parseInt(layout);
            CameraFragment cameraFragment = CameraFragment.newInstance(dbHelper, blockID, layoutID);

            fragmentTransaction.replace(R.id.container, cameraFragment);
            fragmentTransaction.commit();
        }
    }




    private View.OnClickListener onClickListenerNew
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                CompositionFragment compositionFragment
                        = CompositionFragment.newInstance(dbHelper, storyBoardNumber);

                fragmentTransaction.replace(R.id.container, compositionFragment);
                fragmentTransaction.commit();
            }
        }
    };

    private View.OnClickListener onClickListenerWrite
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast("now writing down...");
            VideoExport videoExport = new VideoExport(getActivity(), dbHelper, storyBoardNumber);

            if(videoExport.loadInBackground()){
                showToast("success to write down a movie!");

                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    MediaPlayFragment mediaPlayFragment
                            = MediaPlayFragment.newInstance(dbHelper, storyBoardNumber, Table.STORIES);

                    fragmentTransaction.replace(R.id.container, mediaPlayFragment);
                    fragmentTransaction.commit();
                }

            }
            else {
                showToast("error: couldn't write down a movie...");
            }

        }
    };

    private DataSet [] setData(){
        // StroiesID は今回は不要のため，-1
        int columns = Story.values().length-1;
        int records = 0;

        List<String[]> tmp = new ArrayList<String[]>();

        Story[] values = Story.values();
        for(int i = 0; i < columns; i++){
            Story story = values[i];
            String [] str
                    = dbHelper.getColumn(
                    Table.STORY, story.getName(),
                    Story.STORIES_ID.getName(), String.valueOf(storyBoardNumber));
            if(str != null) {
                records = str.length;
                tmp.add(str);
            }
        }

        if(records != -1) {
            DataSet[] data = new DataSet[records];

            for (int i = 0; i < records; i++) {
                data[i] = new DataSet();
                data[i].id = tmp.get(Story.ID.getNumber())[i];
                data[i].order = tmp.get(Story.TURN.getNumber())[i];
                data[i].name = tmp.get(Story.NAME.getNumber())[i];
                data[i].layout = dbHelper.getColumn(
                        Table.COMPOSITION, Composition.THUMB_ID.getName(),
                        Composition.ID.getName(), tmp.get(Story.COMPOSITION_ID.getNumber())[i]
                        )[0];
                data[i].description = tmp.get(Story.DESCRIPTION.getNumber())[i];
                data[i].url = tmp.get(Story.FILE_URL.getNumber())[i];
            }

            DataSet[] reArrangeID = reArrangeID(data);
            return reArrangeID;
        }
        return null;
    }

    private DataSet[] reArrangeID(DataSet [] data) {
        int length = data.length;
        DataSet [] rearrage = data;

        for(int i = 0; i < length; i++){
            int min = i;
            for(int j = i; j < length; j++){
                if(Integer.parseInt(rearrage[min].order) > Integer.parseInt(rearrage[j].order)) {
                    min = j;
                }
            }
            DataSet tmp = rearrage[min];
            rearrage[min] = rearrage[i];
            rearrage[i] = tmp;
        }
        return rearrage;
    }


    /*
    // リストの普通のクリックをしたときの動作
    private AdapterView.OnItemClickListener onItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            // タップしたアイテムの取得
            ListView listView = (ListView)parent;
            Block item = (Block)listView.getItemAtPosition(position);  // SampleListItemにキャスト


            // Log.d(TAG, "onListItemClick position => " + position + " : id => " + id);

            // タッチされた絵コンテ作品のIDを取得
            String storiesID[] = dbHelper.getColumn(Table.STORIES, Stories.ID.getName());


            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                if(position == 0){
                    fragmentTransaction.replace(R.id.container,
                            new CompositionFragment(dbHelper, storyBoardNumber));
                }
                else {
                    //
                    fragmentTransaction.replace(R.id.container,
                            new StoryBoardFragment(dbHelper, Integer.parseInt(storiesID[position-1])));
                    //
                }
                fragmentTransaction.commit();
            }
        }
    };


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 100) {
            if (resultCode == DialogInterface.BUTTON_POSITIVE) {
                // positive_button 押下時の処理
            } else if (resultCode == DialogInterface.BUTTON_NEGATIVE) {
                // negative_button 押下時の処理
            }
        }
    }


    // リストをロングクリックしたときの動作
    private AdapterView.OnItemLongClickListener onItemLongClickListener
            = new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            final BlockDialogFragment dialog = new BlockDialogFragment();
            dialog.setTargetFragment(null, 100);
            Bundle bundle = new Bundle();
            bundle.putInt("aaa", 0);
            dialog.setArguments(bundle);
            dialog.show(getChildFragmentManager(), "my_dialog");
            return true;
        }
    };




    // 録画ボタンをクリックしたとき
    @Override
    public void myFunc(Object position) {

        // タッチされた絵コンテ作品のIDを取得
        String storyID[] = dbHelper.getColumn(Table.STORY, Stories.ID.getName());

            FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                int id = Integer.parseInt(position.toString());
                fragmentTransaction.replace(R.id.container,
                            new CameraFragment(dbHelper, Integer.parseInt(storyID[id-1])));
                fragmentTransaction.commit();
            }
    }
    */
    /*
     * エラー等のToastを表示する
     * str : 表示したいString
     */
    public void showToast(String str){
        Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
