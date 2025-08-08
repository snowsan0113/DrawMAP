package snowsan0113.drawmap.util;

import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.style.*;
import org.geotools.api.style.Stroke;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.StyleBuilder;
import snowsan0113.drawmap.util.quake.TsunamiAreaType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DrawUtil {

    private static int width = 1280, height = 1080;

    public static BufferedImage createDrawMap(Map<Color, List<TsunamiAreaType>> color_map) throws IOException {
        BufferedImage map = getMap();
        Graphics2D g2d = map.createGraphics();
        g2d.setPaint(new Color(255, 255, 255, 1));
        g2d.fillRect(0, 0, width, height);
        g2d.drawImage(getTsunamiArea(color_map), 0, 0, null);
        g2d.dispose();

        return map;
    }

    private static BufferedImage getMap() throws IOException {
        SimpleFeatureSource featureSource = getFeature(new File("F:\\Users\\user\\Downloads\\20190125_AreaForecastLocalEEW_GIS\\緊急地震速報／府県予報区.shp"));
        MapContent map = new MapContent();
        SimpleFeatureCollection features = featureSource.getFeatures();
        map.addLayer(new FeatureLayer(features, createStyle(Color.LIGHT_GRAY, 2f, 0.8f)));

        return createImage(map);
    }

    private static BufferedImage getTsunamiArea(Map<Color, List<TsunamiAreaType>> color_map) throws IOException {
        SimpleFeatureSource featureSource = getFeature(new File("F:\\Users\\user\\Downloads\\20240520_AreaTsunami_GIS\\津波予報区.shp"));

        MapContent map = new MapContent();
        SimpleFeatureCollection allFeatures = featureSource.getFeatures();
        map.addLayer(new FeatureLayer(allFeatures, createStyle(Color.LIGHT_GRAY, 4f, 0)));

        for (Map.Entry<Color, List<TsunamiAreaType>> entry : color_map.entrySet()) {
            Color color = entry.getKey();
            List<TsunamiAreaType> area_list = entry.getValue();
            List<String> area_string_list = area_list.stream().map(TsunamiAreaType::getJPString).toList();

            for (String area : area_string_list) {
                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                Filter filter = ff.equals(ff.property("name"), ff.literal(area));
                SimpleFeatureCollection features = featureSource.getFeatures(filter);
                map.addLayer(new FeatureLayer(features, createStyle(color, 4f, 0)));
            }

        }

        return createImage(map);
    }

    //下以降はutil
    private static BufferedImage createImage(MapContent map) {
        ReferencedEnvelope envelope = map.layers().getFirst().getBounds();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(new Color(255, 255, 255, 1));
        g2d.fillRect(0, 0, width, height);

        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);

        renderer.paint(g2d, new Rectangle(width, height), envelope);

        g2d.dispose();
        map.dispose();

        return image;
    }

    private static SimpleFeatureSource getFeature(File file) throws IOException {
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(file);
        store.setCharset(StandardCharsets.UTF_8);
        return store.getFeatureSource();
    }

    private static Style createStyle(Color color, float strokeWidth, float f) {
        StyleBuilder sb = new StyleBuilder();
        Stroke stroke = sb.createStroke(color, strokeWidth);
        Fill fill = sb.createFill(color, f);
        PolygonSymbolizer sym = sb.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = sb.createRule(sym);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle(null, rule);
        Style style = sb.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

}
