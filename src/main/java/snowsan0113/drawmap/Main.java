package snowsan0113.drawmap;

import com.google.gson.JsonObject;
import snowsan0113.drawmap.util.quake.TsunamiAPI;
import snowsan0113.drawmap.util.quake.TsunamiAreaType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.InflaterInputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("取得したい番号：");
        Scanner scan = new Scanner(System.in);
        int index = scan.nextInt();

        TsunamiAPI tsunami = new TsunamiAPI(2);
        TsunamiAPI.Areas area = new TsunamiAPI.Areas(2, index);
        Map<TsunamiAreaType, TsunamiAPI.Areas.AreaData> areaMap = area.getAreaMap();

        System.out.println(areaMap.toString() + "\n");
        for (TsunamiAreaType tsunamiAreaType : TsunamiAreaType.values()) {
            if (!areaMap.containsKey(tsunamiAreaType)) continue;

            TsunamiAPI.TsunamiType grade = areaMap.get(tsunamiAreaType).getGrade();
            if (grade != TsunamiAPI.TsunamiType.UNKNOWN) {
                System.out.println(tsunamiAreaType.getJPString() + ":" + grade.getString() + "\n");
            }
        }

    }

}
