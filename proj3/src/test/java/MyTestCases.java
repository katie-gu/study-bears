import org.junit.Test;
import java.util.HashMap;
/**
 * Created by jhinukbarman on 4/14/17.
 */
public class MyTestCases {

    @Test
    public void testRasterer() {
        //Rasterer.QuadTree r = new Rasterer("img/root.png").QuadTree();
        Rasterer r = new Rasterer("img/root.png");
        //lrlon=-122.24053369025242, ullon=-122.24163047377972, w=892.0, h=875.0, ullat=37.87655856892288, lrlat=37.87548268822065
        HashMap<String, Double> m = new HashMap<>();
        m.put("lrlon", -122.24053369025242);
        m.put("ullon", -122.24163047377972);
        m.put("w", 892.0);
        m.put("h", 875.0);
        m.put("ullat", 37.87655856892288);
        m.put("lrlat", 37.87548268822065);

        r.getMapRaster(m);
        //assert();
    }


}
