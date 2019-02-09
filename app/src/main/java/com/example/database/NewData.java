package com.example.database;import com.example.camera.R;import java.util.ArrayList;import java.util.List;public class NewData {    public NewData(){    }    public List<String[]> getCompositionData(){        List<String[]> datas = new ArrayList<>();        String str [] = new String []{                "id", "三分割法",                "交点や三分割をしている直線上に被写体を配置することで、"                        +"バランスよく撮影することができます。どんな被写体にも使いやすいです。",                String.valueOf(R.mipmap.lattice),                String.valueOf(R.mipmap.waist_left),                String.valueOf(R.mipmap.lattice_small),                Tag.OTHER.getName()        };        datas.add(str);        str = new String [] {                "id", "シンメトリー構図",                "主題を中心において上下や左右が対称的に映る構図です。",                String.valueOf(R.mipmap.symmetry),                "symmetry",                String.valueOf(R.mipmap.symmetry),                Tag.BUILDING.getName()        };        datas.add(str);        str = new String [] {                "id", "日の丸構図",                "画面中央に被写体を配置することで、主題をストレートに見せることができます。",                String.valueOf(R.mipmap.japan_flag),                "symmetry",                String.valueOf(R.mipmap.japan_flag),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "奥行きを生かした構図",                "道や廊下といった奥行きのある景色の消失点として手前から奥の景色を撮影することで、"                        + "求心的な映像になり，消失点に視線が誘導される映像になります。",                String.valueOf(R.mipmap.diagonal),                "symmetry",                String.valueOf(R.mipmap.diagonal),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "求心的な構図",                "高い建物の頂上を収束点として見上げるように撮影することで"                        + "いくつかの放射状の線が感じられ、求心的な映像になります。",                String.valueOf(R.mipmap.centripetal),                "symmetry",                String.valueOf(R.mipmap.centripetal),                Tag.BUILDING.getName()        };        datas.add(str);        str = new String [] {                "id", "三角構図",                "家屋の屋根やピラミッドのような、上向き三角形の被写体やラインを取り入れることで、"                        + "安定感が得られます。",                String.valueOf(R.mipmap.triangle),                "symmetry",                String.valueOf(R.mipmap.triangle_smll),                Tag.BUILDING.getName()        };        datas.add(str);        str = new String [] {                "id", "フルショット",                "人物全体を撮影します。上部を少し多めに空けた方が安定感がでます。"                        + "目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.full_right),                String.valueOf(R.mipmap.full_left),                String.valueOf(R.mipmap.full_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "ニーショット",                "人物の膝から上を撮影します。二人の人物が並んで歩くショットにおススメです。"                        + "人の目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.knee_right),                String.valueOf(R.mipmap.knee_left),                String.valueOf(R.mipmap.knee_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "ウエストショット",                "人物の腰から上を撮影します。上半身に大きな動きのある人物の撮影におススメです。"                        + "人の目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.waist_right),                String.valueOf(R.mipmap.waist_left),                String.valueOf(R.mipmap.waist_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "バストショット",                "人物の胸から上を撮影します。"                        + "人の目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.bust_right),                String.valueOf(R.mipmap.bust_left),                String.valueOf(R.mipmap.bust_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "アップショット",                "人物の肩から上を撮影します。人の話を撮る際におススメです。"                        + "人の目線方向や進行方向に空間を持たせることで、"                        + "水平方向に動きを持たせます。",                String.valueOf(R.mipmap.bust_right),                String.valueOf(R.mipmap.bust_left),                String.valueOf(R.mipmap.bust_right),                Tag.PORTRAIT.getName()        };        datas.add(str);        str = new String [] {                "id", "逆三角形の構図",                "逆三角形の被写体やラインを取り入れることで、不安定な感じを演出します。",                String.valueOf(R.mipmap.reverse_triangle),                "symmetry",                String.valueOf(R.mipmap.reverse_triangle),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "トンネル構図",                "手前に門やトンネル、木、橋などの視野を遮るものを配置し、"                        + "奥の被写体が明るいことで、"                        + "視線が奥に誘導される効果があります。",                String.valueOf(R.mipmap.tunnel),                "symmetry",                String.valueOf(R.mipmap.tunnel),                Tag.BUILDING.getName()        };        datas.add(str);        str = new String [] {                "id", "S字構図",                "川や道、道路などのS字ラインを画面に組み込むことで、柔らかな感じをもたらすと共に"                        + "変化をもたらします。",                String.valueOf(R.mipmap.s_curve),                "symmetry",                String.valueOf(R.mipmap.s_curve),                Tag.SCENERY.getName()        };        datas.add(str);        str = new String [] {                "id", "対比の構図",                "見せたいもの2つを均等に配置する構図です。",                String.valueOf(R.mipmap.comparison),                "symmetry",                String.valueOf(R.mipmap.comparison),                Tag.OTHER.getName()        };        datas.add(str);        str = new String [] {                "id", "強調構図",                "左右どちらかを目立たせるように被写体を配置する構図です。",                String.valueOf(R.mipmap.emphasis_right),                String.valueOf(R.mipmap.emphasis_left),                String.valueOf(R.mipmap.emphasis_right),                Tag.OTHER.getName()        };        datas.add(str);        return datas;    }    public List<String[]> getSampleData(){        List<String[]> datas = new ArrayList<>();        String str []            = new String []{"id", String.valueOf(R.mipmap.sample_lattice1), String.valueOf(1)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_lattice2), String.valueOf(1)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_symmetry1), String.valueOf(2)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_symmetry2), String.valueOf(2)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_japanflag1), String.valueOf(3)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_japanflag2), String.valueOf(3)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_diagonal1), String.valueOf(4)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_diagonal2), String.valueOf(4)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_centripetal1), String.valueOf(5)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_centripetal2), String.valueOf(5)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_triangle1), String.valueOf(6)};        datas.add(str);        str = new String []{"id", String.valueOf(R.mipmap.sample_full1), String.valueOf(7)};        datas.add(str);        return datas;    }}