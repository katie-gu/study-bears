import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    String imgRoot;
    QuadTree q = new QuadTree();

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        this.imgRoot = "/Users/jhinukbarman/cs61b/aej/proj3/img";
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>Has dimensions of at least w by h, where w and h are the user viewport width
     *         and height.</li>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */


    private class QuadTree{
        private Node root;

        private class Node {
            double topLeftXPos,topLeftYPos, bottomRightXPos, bottomRightYPos;
            Node topLeft,  topRight,  bottomLeft,  bottomRight;
            String imgName;
            //int depth;
            //store subtrees as instance variables?
            public Node(double topLeftXPos, double topLeftYPos, double bottomRightXPos, double bottomRightYPos, String imgName) {
                this.topLeftXPos = topLeftXPos;
                this.topLeftYPos = topLeftYPos;
                this.bottomRightXPos = bottomRightXPos;
                this.bottomRightYPos = bottomRightYPos;
                this.imgName = imgName;
                //topLeft = new Node(topLeftPos, );
            }

        }

        public boolean intersectsTile(double query_ul, double query_lr) {

        }

        public boolean lonDPPsmallerThanOrIsLeaf(int queriesLonDPP) {

        }
    }


    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser.");




        /*
        String[][] img = {{"img/2143411.png", "img/2143412.png", "img/2143421.png"}, {"img/2143413.png", "img/2143414.png", "img/2143423.png"}, {"img/2143431.png", "img/2143432.png", "img/2143441.png"}};

        results.put("render_grid", img);
        results.put("raster_ul_lon", -122.24212646484375);
        results.put("raster_height", 768);
        results.put("depth", 7);
        results.put("raster_lr_lon", -122.24006652832031);
        results.put("raster_lr_lat", 37.87538940251607);
        results.put("raster_ul_lat", 37.87701580361881);
        results.put("query_success", true);
        results.put("raster_width", 768);
        System.out.println(results);
        */

        return results;

    }

    private int getlonDDP(Map<String, Double> params) {
        double lonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        return lonDPP;
    }

    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser.");
        return results;
    }

}
