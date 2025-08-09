package snowsan0113.drawmap.api;

public enum PrefectureType {
    HOKKAIDO("北海道"),
    AOMORI("青森県"),
    IWATE("岩手県"),
    MIYAGI("宮城県"),
    AKITA("秋田県"),
    YAMAGATA("山形県"),
    FUKUSHIMA("福島県"),
    KAGOSHIMA("鹿児島県");

    private final String jp_name;

    PrefectureType(String jp_name) {
        this.jp_name = jp_name;
    }
}
