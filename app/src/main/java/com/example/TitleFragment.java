package com.example;import android.os.Bundle;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentTransaction;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import com.example.camera.R;import com.example.database.DBHelper;import com.example.storyboard.NewStoryFragment;import com.example.storyboard.ProjectsFragment;public class TitleFragment extends Fragment {    private DBHelper dbHelper;    public static TitleFragment newInstance(DBHelper dbHelper){        TitleFragment fragment = new TitleFragment();        fragment.dbHelper = dbHelper;        return fragment;    }    public TitleFragment(){    }    @Override    public void onCreate(Bundle saveInstanceState){        super.onCreate(saveInstanceState);        setRetainInstance(true);    }    @Override    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,                             final Bundle savedInstanceState) {        final View rootView = inflater.inflate(R.layout.menu_main, container, false);        return rootView;    }    @Override    public void onViewCreated(View view, Bundle savedInstanceState) {        super.onViewCreated(view, savedInstanceState);        Button newProject = (Button) view.findViewById(R.id.new_project);        Button projects = (Button) view.findViewById(R.id.project);        newProject.setOnClickListener(onClickListener);        projects.setOnClickListener(onClickListener);    }    private final View.OnClickListener onClickListener = new View.OnClickListener() {        @Override        public void onClick(View view) {            FragmentManager fragmentManager = getFragmentManager();            if (fragmentManager != null) {                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();                // BackStackを設定                fragmentTransaction.addToBackStack(null);                // ボタンクリック処理                switch (view.getId()){                    case R.id.new_project:                        NewStoryFragment newStoryFragment = NewStoryFragment.newInstance(dbHelper);                        fragmentTransaction.replace(R.id.container, newStoryFragment);                        break;                    case R.id.project:                        ProjectsFragment projectsFragment = ProjectsFragment.newInstance(dbHelper);                        fragmentTransaction.replace(R.id.container, projectsFragment);                        break;                }                fragmentTransaction.commit();            }        }    };}