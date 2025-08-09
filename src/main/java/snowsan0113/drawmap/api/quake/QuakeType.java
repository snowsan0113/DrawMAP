package snowsan0113.drawmap.api.quake;

public enum QuakeType {
    @Deprecated ZERO("震度0", 0, "0"), //P2PAPIの仕様にないため
    ONE("震度1", 10, "1"),
    TWO("震度2", 20, "2"),
    THREE("震度3", 30, "3"),
    FOUR("震度4", 40, "4"),
    FIVE_LOWER("震度5弱", 45, "5-"),
    FIVE_UPPER("震度5強", 50, "5+"),
    SIX_LOWER("震度6弱",55, "6-"),
    SIX_UPPER("震度6強", 60, "6+"),
    SEVEN("震度7", 70, "7");

    private final String jp_name;
    private final String name;
    private final int raw_int;

    QuakeType(String jp_name, int raw_int, String name) {
        this.jp_name = jp_name;
        this.raw_int = raw_int;
        this.name = name;
    }

    public String getJPName() {
        return jp_name;
    }

    public int getRawData() {
        return raw_int;
    }

    public String getName() {
        return name;
    }
}
