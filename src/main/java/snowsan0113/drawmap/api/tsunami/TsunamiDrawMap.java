package snowsan0113.drawmap.api.tsunami;

import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import snowsan0113.drawmap.util.DrawUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TsunamiDrawMap {

    public static BufferedImage createDrawMap(Map<Color, List<TsunamiAreaType>> color_map) throws IOException {
        BufferedImage map = DrawUtil.getMap();
        Graphics2D g2d = map.createGraphics();
        g2d.setPaint(new Color(255, 255, 255, 1));
        g2d.fillRect(0, 0, 1280, 1080);
        g2d.drawImage(getTsunamiArea(color_map), 0, 0, null);
        g2d.dispose();

        return map;
    }

    private static BufferedImage getTsunamiArea(Map<Color, java.util.List<TsunamiAreaType>> color_map) throws IOException {
        SimpleFeatureSource featureSource = DrawUtil.getFeature(new File("F:\\Users\\user\\Downloads\\20240520_AreaTsunami_GIS\\津波予報区.shp"));

        MapContent map = new MapContent();
        SimpleFeatureCollection allFeatures = featureSource.getFeatures();
        map.addLayer(new FeatureLayer(allFeatures, DrawUtil.createStyle(Color.LIGHT_GRAY, 4f, 0)));

        for (Map.Entry<Color, java.util.List<TsunamiAreaType>> entry : color_map.entrySet()) {
            Color color = entry.getKey();
            java.util.List<TsunamiAreaType> area_list = entry.getValue();
            List<String> area_string_list = area_list.stream().map(TsunamiAreaType::getJPString).toList();

            for (String area : area_string_list) {
                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                Filter filter = ff.equals(ff.property("name"), ff.literal(area));
                SimpleFeatureCollection features = featureSource.getFeatures(filter);
                map.addLayer(new FeatureLayer(features, DrawUtil.createStyle(color, 4f, 0)));
            }

        }

        return DrawUtil.createImage(map, null, 1280, 1080);
    }
}
