package snowsan0113.drawmap.api.tsunami;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TsunamiAPI {

    private String api_url = "https://api.p2pquake.net/v2/jma/tsunami";
    private final int code = 551;
    private int limit;

    public TsunamiAPI(int limit) {
        this.limit = limit;
        this.api_url = this.api_url + "?code=" + code + "&limit=" + limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        this.api_url = this.api_url + "?code=" + code + "&limit=" + limit;
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

    public static class Areas {

        private final TsunamiAPI root_api;
        private int get_index;

        public Areas(int limit, int get_index) {
            this.root_api = new TsunamiAPI(limit);
            this.get_index = get_index;
        }

        public Map<TsunamiAreaType, AreaData> getAreaMap() throws IOException {
            Map<TsunamiAreaType, AreaData> map = new HashMap<>();
            JsonArray raw_json = getAreaArray();
            for (int n = 0; n < raw_json.size(); n++) {
                JsonObject area_data = raw_json.get(n).getAsJsonObject();
                String name = area_data.get("name").getAsString();
                String grade_string = area_data.get("grade").getAsString();
                map.put(TsunamiAreaType.convertType(name), new AreaData(grade_string));
            }
            return map;
        }

        public JsonArray getAreaArray() throws IOException {
            JsonObject raw_json = getRawJson();
            return raw_json.getAsJsonArray("areas");
        }

        public JsonObject getRawJson() throws IOException {
            JsonArray raw_json = root_api.getRawJson();
            return raw_json.get(get_index).getAsJsonObject();
        }

        public int getIndex() {
            return get_index;
        }

        public void setIndex(int get_index) {
            this.get_index = get_index;
        }

        public static class AreaData {

            private TsunamiType tsunamiType;

            public AreaData(String tsunamiType) {
                Arrays.stream(TsunamiType.values())
                        .filter(type -> type.getApiName().equalsIgnoreCase(tsunamiType))
                        .forEach(type -> this.tsunamiType = type);
            }

            public TsunamiType getGrade() {
                return tsunamiType;
            }

            public String toString() {
                return "{grade=" + tsunamiType + "}";
            }
        }
    }

}
