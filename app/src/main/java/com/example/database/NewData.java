package com.example.database;import com.example.camera.R;import java.util.ArrayList;import java.util.List;public class NewData {    public NewData(){    }    public List<String[]> getCompositionData(){        List<String[]> datas = new ArrayList<>();        String str [] = new String []{                "id", "三分割法",                "交点や三分割をしている直線上に被写体を配置すろことで，"                        +"バランスよく撮影することができます。",                String.valueOf(R.mipmap.lattice),                String.valueOf(R.mipmap.lattice_small),                Tag.OTHER.getName()        };        datas.add(str);        str = new String [] {                "id", "フルショット",                "人物全体を撮影します。上部を少し多めに空けた方が安定感がでます。"                        + "目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.full_right),                String.valueOf(R.mipmap.full_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "ニーショット",                "人物の膝から上を撮影します。二人の人物が並んで歩くショットにおススメです。"                        + "目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.knee_right),                String.valueOf(R.mipmap.knee_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "ウエストショット",                "人物の膝からを撮影します。上半身に大きな動きのある人物の撮影におススメです。"                        + "目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.waist_right),                String.valueOf(R.mipmap.waist_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "バストショット",                "人物の胸から上を撮影します。人が移動する方向や人の目線方向（左）に"                        + "空間を持たせることで、水平方向に動きを持たせます。",                String.valueOf(R.mipmap.bust_right),                String.valueOf(R.mipmap.bust_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "三角構図",                "ピラミッドのような上向き三角形の被写体やラインを取り入れることで，"                        + "安定感が得られます",                String.valueOf(R.mipmap.triangle),                String.valueOf(R.mipmap.triangle_smll),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "逆三角形の構図",                "逆三角形の被写体やラインを取り入れることで，不安定な感じを演出します",                String.valueOf(R.mipmap.reverse_triangle),                String.valueOf(R.mipmap.reverse_triangle),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "日の丸構図",                "画面中央に被写体を配置することで，主題をストレートに見せることができます",                String.valueOf(R.mipmap.japan_flag),                String.valueOf(R.mipmap.japan_flag),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "シンメトリー構図",                "主題を中心において上下や左右が対称的に映る構図",                String.valueOf(R.mipmap.symmetry),                String.valueOf(R.mipmap.symmetry),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "トンネル構図",                "手前に視野を遮るものを配置し奥の被写体が明るいことで，"                        + "視線が置くに誘導される効果があります",                String.valueOf(R.mipmap.tunnel),                String.valueOf(R.mipmap.tunnel),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "求心的な構図",                "高い建物の頂上を中心としていくつかの放射状の線を感じることができます",                String.valueOf(R.mipmap.centripetal),                String.valueOf(R.mipmap.centripetal),                Tag.BUILDING.getName()        };        datas.add(str);        str = new String [] {                "id", "S字構図",                "川や道，道路などのS字ラインを画面に組み込むことで，柔らかな感じをもたらすと共に"                        + "変化をもたらします",                String.valueOf(R.mipmap.s_curve),                String.valueOf(R.mipmap.s_curve),                Tag.SCENERY.getName()        };        datas.add(str);        /*        str = new String [] {                "id", "ズームアウト",                "寄りから引きにズーミングすることで新しい情報を増えていく愉しみがあります\n"                + "ズームとパンを複合し位置調整を行いながらズームを行うと良い",                String.valueOf(R.mipmap.zoom_out),                String.valueOf(R.mipmap.zoom_out_roop),                Tag.SCENERY.getName()        };        datas.add(str);        */        return datas;    }}