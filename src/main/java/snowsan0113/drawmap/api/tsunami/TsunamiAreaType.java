package snowsan0113.drawmap.api.tsunami;

public enum TsunamiAreaType {

    OKHOTSK("オホーツク海沿岸"),
    HOKKAIDO_PACIFIC_OCEAN_EAST("北海道太平洋沿岸東部"),
    HOKKAIDO_PACIFIC_OCEAN_CENTER("北海道太平洋沿岸中部"),
    HOKKAIDO_PACIFIC_OCEAN_WEST("北海道太平洋沿岸西部"),
    AOMORI_PACIFIC_OCEAN("青森県太平洋沿岸"),
    IWATE("岩手県"),
    MIYAGI("宮城県"),
    FUKUSHIMA("福島県"),
    IBARAKI("茨城県"),
    CHIBA_KUJUKURI_SOTOBO("千葉県九十九里・外房"),
    IZU("伊豆諸島"),
    YAKUSHIMA_TANEGASHIMA("種子島・屋久島地方");

    private final String jp_string;

    TsunamiAreaType(String jp_string) {
        this.jp_string = jp_string;
    }

    public String getJPString() {
        return jp_string;
    }

    public static TsunamiAreaType convertType(String name) {
        for (TsunamiAreaType tsunamiAreaType : TsunamiAreaType.values()) {
            if (tsunamiAreaType.getJPString().equalsIgnoreCase(name)) {
                return tsunamiAreaType;
            }
        }
        return null;
    }
}
