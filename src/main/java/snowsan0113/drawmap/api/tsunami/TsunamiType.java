package snowsan0113.drawmap.api.tsunami;

public enum TsunamiType {
    MAJORWARNING("大津波警報", "MajorWarning"),
    WARNING("津波警報", "Warning"),
    WATCH("津波注意報", "Watch"),
    UNKNOWN("不明", "Unknown");

    private final String jp_string;
    private final String api_name;

    TsunamiType(String jp_string, String api_name) {
        this.jp_string = jp_string;
        this.api_name = api_name;
    }

    public String getString() {
        return jp_string;
    }

    public String getApiName() {
        return api_name;
    }
}
