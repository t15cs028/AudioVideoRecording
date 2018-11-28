package com.example.storyboard;



import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.dialog.YesNoDialogFragment;
import com.example.encoder.MediaPlayFragment;
import com.example.encoder.VideoExport;

;import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.database.DBHelper.TAG;
import static com.example.database.DBHelper.URL;
import static java.util.Collections.sort;
import static java.util.Collections.swap;

public class StoryBoardFragment extends Fragment implements OnRecyclerListener {


    private final static int FromPos = 0;
    private final static int ToPos = 1;

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

    private List<int[]> movePosition;

    private boolean movement;

    public static StoryBoardFragment newInstance(DBHelper dbHelper, int storyBoardNumber){
        StoryBoardFragment fragment = new StoryBoardFragment();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        fragment.movement = false;
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




        List<DataSet> data = setData();

        if(data != null) {
            itemImages = new ArrayList<>();
            itemThumbnails = new ArrayList<>();
            itemNames = new ArrayList<>();
            itemCameras = new ArrayList<>();

            movePosition = new ArrayList<>();

            for (DataSet d : data) {

                itemImages.add(Integer.parseInt(d.layout));
                itemNames.add(d.order);
                itemCameras.add(android.R.drawable.ic_menu_camera);
                if (!URL.equals(d.url)) {
                    ThumbnailUtils tu = new ThumbnailUtils();
                    Bitmap bmp
                            = tu.createVideoThumbnail(d.url,
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
                            Log.d(TAG, "from position => " + fromPos
                            + ", to position => " + toPos);
                            List<DataSet> data = setData();

                            int positions[] = new int [] {fromPos, toPos};
                            movePosition.add(positions);
                            adapter.notifyItemMoved(fromPos, toPos);
                            return true;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                             int direction) {
                            final int fromPos = viewHolder.getAdapterPosition();

                            Log.d(TAG, "delete position => " + fromPos);
                            itemImages.remove(fromPos);
                            itemNames.remove(fromPos);
                            itemCameras.remove(fromPos);

                            int positions[] = new int [] {fromPos, -1};
                            movePosition.add(positions);
                            adapter.notifyItemRemoved(fromPos);
                            showToast("success to delete");
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
        List<DataSet> datas = setData();
        String id = datas.get(position).id;
        String path = datas.get(position).url;

        updateDataBase(datas);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // BackStackを設定
            fragmentTransaction.addToBackStack(null);

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
        List<DataSet> datas = setData();
        String id = datas.get(position).id;
        String layout
                = dbHelper.getColumn(
                        Table.COMPOSITION, Composition.ID.getName(),
                        Composition.THUMB_ID.getName(), setData().get(position).layout
                )[0];

        updateDataBase(datas);
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
            List<DataSet> datas = setData();
            updateDataBase(datas);
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
            List<DataSet> datas = setData();
            updateDataBase(datas);
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

    private void updateDataBase(List<DataSet> datas){
        for(int[] i : movePosition){
            if(i[ToPos] == -1){
                dbHelper.deleteRecord(Table.STORY, datas.get(i[FromPos]).id);
                datas.remove(i[FromPos]);
            }
            else {
                swap(datas, i[FromPos], i[ToPos]);
            }
        }
        for(int i = 0; i < datas.size(); i++){
            dbHelper.setField(Table.STORY, datas.get(i).id,
                    Story.TURN.getName(), String.valueOf(i));
        }
        movePosition.clear();
    }

    private List<DataSet> setData(){
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
            List<DataSet> datas = new ArrayList<>();

            for (int i = 0; i < records; i++) {
                DataSet data = new DataSet();
                data.id = tmp.get(Story.ID.getNumber())[i];
                data.order = tmp.get(Story.TURN.getNumber())[i];
                data.name = tmp.get(Story.NAME.getNumber())[i];
                data.layout = dbHelper.getColumn(
                        Table.COMPOSITION, Composition.THUMB_ID.getName(),
                        Composition.ID.getName(), tmp.get(Story.COMPOSITION_ID.getNumber())[i]
                        )[0];
                data.description = tmp.get(Story.DESCRIPTION.getNumber())[i];
                data.url = tmp.get(Story.FILE_URL.getNumber())[i];
                datas.add(data);
            }
            List<DataSet> reArrangeID = reArrangeID(datas);
            return reArrangeID;
        }
        return null;
    }

    private List<DataSet> reArrangeID(List<DataSet> data) {
        int length = data.size();
        List<DataSet> rearrage = data;

        for(int i = 0; i < length; i++){
            int min = i;
            for(int j = i; j < length; j++){
                if(Integer.parseInt(rearrage.get(min).order)
                        > Integer.parseInt(rearrage.get(j).order)) {
                    min = j;
                }
            }
            swap(rearrage, i, min);
        }
        return rearrage;
    }


    /*
     * エラー等のToastを表示する
     * str : 表示したいString
     */
    public void showToast(String str){
        Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
