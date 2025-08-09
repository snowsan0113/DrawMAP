package snowsan0113.drawmap.api.quake;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuakeAPI {

    private String api_url = "https://api.p2pquake.net/v2/jma/quake";
    private int limit;
    private int index;

    public QuakeAPI(int limit, int index) {
        this.limit = limit;
        this.index = index;
        this.api_url = this.api_url + "?limit=" + limit;
    }

    public JsonArray getRawJson() throws IOException {
        URL url = new URL(api_url);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder content = new StringBuilder();

        String inputLine;
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();
        return  (new Gson()).fromJson(content.toString(), JsonArray.class);
    }

    public EarthquakeData getEarthquake() throws IOException {
        JsonArray raw = getRawJson();
        JsonObject quake_obj = raw.get(index).getAsJsonObject();
        JsonObject earthquake_obj = quake_obj.getAsJsonObject("earthquake");
        JsonObject hypocenter_obj = earthquake_obj.getAsJsonObject("hypocenter");

        //震源データ
        String name = hypocenter_obj.get("name").getAsString();
        int depth = hypocenter_obj.get("depth").getAsInt();
        double magnitude = hypocenter_obj.get("magnitude").getAsDouble();
        double latitude = hypocenter_obj.get("latitude").getAsDouble();
        double longitude = hypocenter_obj.get("longitude").getAsDouble();

        return new EarthquakeData(name, depth, magnitude, latitude, longitude);
    }

    public List<PointData> getPointList() throws IOException {
        List<PointData> list = new ArrayList<>();
        JsonArray raw = getRawJson();
        JsonObject quake_obj = raw.get(index).getAsJsonObject();
        JsonArray points_array = quake_obj.getAsJsonArray("points");
        for (JsonElement point : points_array) {
            JsonObject point_obj = point.getAsJsonObject();
            String addr = point_obj.get("addr").getAsString();
            boolean isArea = point_obj.get("isArea").getAsBoolean();
            String pref = point_obj.get("pref").getAsString();
            int scale_raw = point_obj.get("scale").getAsInt();
            QuakeType scale = Arrays.stream(QuakeType.values())
                    .filter(type -> type.getRawData() == scale_raw)
                    .findFirst()
                    .orElse(null);
            list.add(new PointData(addr, isArea, pref, scale));
        }
        return list;
    }


    /**
     * @param name      震源名
     * @param depth     深さ
     * @param magnitude マグニチュード
     * @param latitude  緯度
     * @param longitude 経度
     */
    public record EarthquakeData(String name, int depth, double magnitude, double latitude, double longitude) {
        @Override
        public String toString() {
            return "{震源名：" + name + ",緯度:" + latitude + ",経度" + longitude + ",深さ:" + depth + ",マグニチュード" + magnitude + "}";
        }
    }

    /**
     * @param addr 新原名
     * @param isArea 区域内かどうか
     * @param pref 都道府県
     * @param scale //震度
     */
    public record PointData(String addr, boolean isArea, String pref, QuakeType scale) {

        /**
         * @return {震度観測点:" + addr + ",区域名か:" + isArea + ",都道府県:" + pref + ",震度:" + scale + "} の形式で返す
         */
        @Override
        public String toString() {
            return "{震度観測点:" + addr + ",区域名か:" + isArea + ",都道府県:" + pref + ",震度:" + scale + "}";
        }
    }
}
