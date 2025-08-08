package snowsan0113.drawmap;

import com.google.gson.JsonObject;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import snowsan0113.drawmap.util.DrawUtil;
import snowsan0113.drawmap.util.quake.TsunamiAPI;
import snowsan0113.drawmap.util.quake.TsunamiAreaType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.zip.InflaterInputStream;

public class Main {

    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        System.out.println("取得したい番号：");
        Scanner scan = new Scanner(System.in);
        int index = scan.nextInt();

        TsunamiAPI tsunami = new TsunamiAPI(4);
        TsunamiAPI.Areas area = new TsunamiAPI.Areas(4, index);
        Map<TsunamiAreaType, TsunamiAPI.Areas.AreaData> areaMap = area.getAreaMap();

        System.out.println(areaMap.toString() + "\n");

        Map<Color, List<TsunamiAreaType>> map = new HashMap<>();
        map.put(new Color(200, 0, 100), new ArrayList<>());
        map.put(Color.RED, new ArrayList<>());
        map.put(Color.YELLOW, new ArrayList<>());

        for (TsunamiAreaType tsunamiAreaType : TsunamiAreaType.values()) {
            if (!areaMap.containsKey(tsunamiAreaType)) continue;

            TsunamiAPI.TsunamiType grade = areaMap.get(tsunamiAreaType).getGrade();
            if (grade != TsunamiAPI.TsunamiType.UNKNOWN) {
                System.out.println(tsunamiAreaType.getJPString() + ":" + grade.getString() + "\n");

                if (grade == TsunamiAPI.TsunamiType.MAJORWARNING) {
                    List<TsunamiAreaType> list = map.get(new Color(200, 0, 100));
                    list.add(tsunamiAreaType);
                }
                else if (grade == TsunamiAPI.TsunamiType.WARNING) {
                    List<TsunamiAreaType> list = map.get(Color.RED);
                    list.add(tsunamiAreaType);
                }
                else if (grade == TsunamiAPI.TsunamiType.WATCH) {
                    List<TsunamiAreaType> list = map.get(Color.YELLOW);
                    list.add(tsunamiAreaType);
                }
            }
        }

        BufferedImage image = DrawUtil.createDrawMap(map);
        ImageIO.write(image, "png", new File("tsunami.png"));

    }

}
