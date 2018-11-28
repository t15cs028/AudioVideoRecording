package com.example.encoder;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;

import com.coremedia.iso.boxes.Container;
import com.example.database.DBHelper;
import com.example.database.Stories;
import com.example.database.Story;
import com.example.database.Table;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.encoder.MediaMuxerWrapper.DIR_NAME;

public class VideoExport extends AsyncTaskLoader<Boolean> {
    private DBHelper dbHelper;
    private int storyID;
    private int mType;

    public VideoExport(Context context, DBHelper dbHelper, int storyID){
        super(context);
        this.dbHelper = dbHelper;
        this.storyID = storyID;
        this.mType = 0;
    }

    @Override
    public Boolean loadInBackground() {
        switch (mType) {
            case 0:
                return append();
            case 1:
                return crop();
            case 2:
                return subTitle();
        }

        return false;
    }

    private boolean append() {
        String outputFilePath;
        try {
            // 複数の動画を読み込み
            List<Movie> movies = new ArrayList<>();
            String [] orders = dbHelper.getColumn(
                    Table.STORY, Story.TURN.getName(),
                    Story.STORIES_ID.getName(), String.valueOf(storyID)
            );
            String [] file_url = dbHelper.getColumn(
                    Table.STORY, Story.FILE_URL.getName(),
                    Story.STORIES_ID.getName(), String.valueOf(storyID)
            );


            for(int i = 0; i < orders.length; i++) {
                for(int j = 0; j < orders.length; j++) {
                    if (Integer.parseInt(orders[j]) == i) {
                        if (!file_url[j].equals("new")) {
                            movies.add(MovieCreator.build(file_url[j]));
                        }
                        break;
                    }
                }
            }
            /*
            String f1 = Environment.getExternalStorageDirectory() + "/Rec/181119_175925.mp4";
            String f2 = Environment.getExternalStorageDirectory() + "/Rec/181119_175951.mp4";
            Movie[] inMovies = new Movie[]{
                    MovieCreator.build(f1),
                    MovieCreator.build(f2)};
                    */

            // 1つのファイルに結合
            List<Track> videoTracks = new LinkedList<Track>();
            List<Track> audioTracks = new LinkedList<Track>();
            for (Movie m : movies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                }
            }
            Movie result = new Movie();
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }

            // 出力
            Container out = new DefaultMp4Builder().build(result);

            String storyName = dbHelper.getColumn(
                    Table.STORIES, Stories.NAME.getName(),
                    Stories.ID.getName(), String.valueOf(storyID)
            )[0];
            outputFilePath
                    = Environment.getExternalStorageDirectory() + "/"
                    + DIR_NAME + "/" + storyName + "/" + storyName + "_movie.mp4";
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            return false;
        }
        dbHelper.setField(Table.STORIES, String.valueOf(storyID), Stories.FILE_URL.getName(), outputFilePath);
        return true;
    }

    private boolean crop() {
        try {
            // オリジナル動画を読み込み
            String filePath = Environment.getExternalStorageDirectory() + "/Rec/181119_175925.mp4";
            Movie originalMovie = MovieCreator.build(filePath);

            // 分割
            Track track = originalMovie.getTracks().get(0);
            Movie movie = new Movie();
            movie.addTrack(new AppendTrack(new CroppedTrack(track, 200, 400)));

            // 出力
            Container out = new DefaultMp4Builder().build(movie);
            String outputFilePath = Environment.getExternalStorageDirectory() + "/Rec/output_crop.mp4";
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean subTitle() {
        try {
            // オリジナル動画を読み込み
            String filePath = Environment.getExternalStorageDirectory() + "/Rec/181119_175925.mp4";
            Movie countVideo = MovieCreator.build(filePath);

            // SubTitleを追加
            TextTrackImpl subTitleEng = new TextTrackImpl();
            subTitleEng.getTrackMetaData().setLanguage("eng");

            subTitleEng.getSubs().add(new TextTrackImpl.Line(0, 1000, "Five"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(1000, 2000, "Four"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(2000, 3000, "Three"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(3000, 4000, "Two"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(4000, 5000, "one"));
            countVideo.addTrack(subTitleEng);

            // 出力
            Container container = new DefaultMp4Builder().build(countVideo);
            String outputFilePath = Environment.getExternalStorageDirectory() + "/Rec/output_subtitle.mp4";
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            FileChannel channel = fos.getChannel();
            container.writeContainer(channel);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
