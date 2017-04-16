import java.lang.reflect.Array;
import java.util.*;

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
    ArrayList<QuadTree.Node> arr = new ArrayList<QuadTree.Node>();
    TreeSet<Double> x = new TreeSet<>();
    TreeSet<Double> y = new TreeSet<>();
    boolean query_success = true;
    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        this.imgRoot = imgRoot + "root.png";
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


    public class QuadTree{

        private static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
                ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;

        private Node root = new Node("", 0, ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT);

        private String prevImgName = "";
        public QuadTree() {
            buildQuadTree(root);

        }

        //recursion?
        private Node buildQuadTree(Node root) {
            String prevImgName = "";

            //root.imgName = "";

            if (root.depth == 8) {
                //return new Node("", 0, 0,0,0,0); //break recursion
                return null;
            }

            root.topLeft = buildQuadTree(new Node((root.imgName + 1), root.depth + 1,
                    root.topLeftXPos, root.topLeftYPos, (root.topLeftXPos + root.bottomRightXPos)/2,
                    (root.topLeftYPos + root.bottomRightYPos)/2));

            root.topRight = buildQuadTree(new Node((root.imgName + 2),
                    root.depth + 1, (root.topLeftXPos + root.bottomRightXPos)/2,
                    root.topLeftYPos, root.bottomRightXPos,
                    (root.topLeftYPos + root.bottomRightYPos)/2));

            root.bottomLeft = buildQuadTree(new Node((root.imgName + 3), root.depth + 1,
                    root.topLeftXPos, (root.topLeftYPos + root.bottomRightYPos)/2, (root.topLeftXPos + root.bottomRightXPos)/2,
                    root.bottomRightYPos));

            root.bottomRight = buildQuadTree(new Node((root.imgName + 4), root.depth + 1,
                    (root.topLeftXPos + root.bottomRightXPos)/2, (root.topLeftYPos + root.bottomRightYPos)/2, root.bottomRightXPos,
                    root.bottomRightYPos));

            return root;

        }

        public String toString() {
            return root.bottomRight.bottomRight.bottomRight.bottomLeft.bottomRight.bottomRight.bottomRight.imgName;
        }

        public class Node {
            double topLeftXPos,topLeftYPos, bottomRightXPos, bottomRightYPos;
            Node topLeft,  topRight,  bottomLeft,  bottomRight;
            String imgName;
            int depth;
            //store subtrees as instance variables?
            public Node(String imgName, int depth, double topLeftXPos, double topLeftYPos, double bottomRightXPos, double bottomRightYPos) {
                this.imgName = imgName;
                this.depth = depth;
                this.topLeftXPos = topLeftXPos;
                this.topLeftYPos = topLeftYPos;
                this.bottomRightXPos = bottomRightXPos;
                this.bottomRightYPos = bottomRightYPos;
            }

            public boolean lonDPPsmallerThanOrIsLeaf(double queriesLonDPP) {
                double currLonDPP = (this.bottomRightXPos - this.topLeftXPos) / 256;
                return (currLonDPP < queriesLonDPP) || (depth >= 7);
            }

            public boolean intersectsTile(double query_ulX, double query_ulY, double query_lrX, double query_lrY) {
               // System.out.println("query_ulX: " + query_ulX + " query_ulY: " + query_ulY
                //    + " query_lrX: " + query_lrX + " query_lrY: " + query_lrY);
              //  System.out.println("topLeftXPos: " + this.topLeftXPos + " topLeftYPos: " + this.topLeftYPos
               //     + " bottomRightXPos: " + this.bottomRightXPos + " bottomRightYPos " + this.bottomRightYPos);
                double midpointXPos = (this.topLeftXPos + this.bottomRightXPos) / 2;
                double midpointYPos = (this.topLeftYPos + this.bottomRightYPos) / 2;

                if ((this.topLeftXPos > query_lrX) && (this.bottomRightXPos < query_ulX) &&
                        (this.topLeftYPos < query_lrY) && (this.bottomRightYPos > query_ulY)) {
                        return false;
                } else {
                    if ((this.topLeftXPos >= query_ulX) && (this.topLeftXPos <= query_lrX) &&
                            (this.topLeftYPos <= query_ulY) && (this.topLeftYPos >= query_lrY)) {
                        return true;
                    } else if ((this.bottomRightXPos >= query_ulX) && (this.bottomRightXPos <= query_lrX)
                            && (this.bottomRightYPos <= query_ulY) && (this.bottomRightYPos >= query_lrY)) {
                        return true;
                    } else if ((query_ulX <= this.bottomRightXPos) && (query_ulX >= this.topLeftXPos)
                            && (query_ulY >= this.bottomRightYPos) && (query_ulY <= this.topLeftYPos)) {
                        return true;
                    } else if ((query_lrX <= this.bottomRightXPos) && (query_lrX >= this.topLeftXPos)
                            && (query_lrY >= this.bottomRightYPos) && (query_lrY <= this.topLeftYPos)) {
                        return true;
                    } else if ((midpointXPos >= query_ulX) && (midpointXPos <= query_lrX)
                            && (midpointYPos <= query_ulY) && (midpointYPos >= query_ulY)) {
                        return true;
                    } else if ((this.topLeftXPos >= query_ulX) && (this.topLeftXPos <= query_lrX)
                            && (this.bottomRightYPos <= query_ulY) && (this.bottomRightYPos >= query_lrY)) {
                        return true;
                    } else if ((this.bottomRightXPos >= query_ulX) && (this.bottomRightXPos <= query_lrX)
                            && (this.topLeftYPos <= query_ulY) && (this.topLeftYPos >= query_lrY)) {
                        return true;
                    }
                    else {
                        return false;
                    }

                }


            }
        }

    }


    public ArrayList<QuadTree.Node> pruneTree(Map<String, Double> params, QuadTree.Node n) {
        //QuadTree temp = q;
        //ArrayList<QuadTree.Node> arr = new ArrayList<QuadTree.Node>(); //does this work?
        double lonDDP = (params.get("lrlon") - params.get("ullon")) / (params.get("w"));

        if (n.imgName.equals("")) {
            pruneTree(params, n.topLeft);
            pruneTree(params, n.topRight);
            pruneTree(params, n.bottomLeft);
            pruneTree(params, n.bottomRight);
        } else {
            if (!(n.intersectsTile(params.get("ullon"), params.get("ullat"), params.get("lrlon"), params.get("lrlat")))) {
               // System.out.println("Inside this now");
                return null;
                // return new QuadTree.Node("", 0, 0 ,0 ,0, 0);
            } else if (!(n.lonDPPsmallerThanOrIsLeaf(lonDDP))) {
              //  System.out.println("Inside hereeee");
                pruneTree(params, n.topLeft);
                pruneTree(params, n.topRight);
                pruneTree(params, n.bottomLeft);
                pruneTree(params, n.bottomRight);
            } else {
                arr.add(n);
                x.add(n.topLeftXPos);
                y.add(n.topLeftYPos);
               // System.out.println("Added: " + n.imgName);
            }
        }
        //}
        return arr;


    }

    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();




       // System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
         //                  + "your browser.");
        //QuadTree temp = q;

        double lonDDP = (params.get("lrlon") - params.get("ullon")) / (params.get("w"));
        ArrayList<QuadTree.Node> a = pruneTree(params, q.root);
       // System.out.println(q.toString());

        ArrayList<Double> xPos = new ArrayList<Double>(x);
        //System.out.println("xPosList : " + xPos);

        ArrayList<Double> yPos = new ArrayList<Double>(y);
        Collections.reverse(yPos);
        //System.out.println("yPosList : " + yPos);
        if (xPos.size() == 0) {
            query_success = false;
        }

        String[][] img = new String[xPos.size()][yPos.size()];
        QuadTree.Node[][] imgNodes = new QuadTree.Node[xPos.size()][yPos.size()];


       // System.out.println("ArrayList size: " + a);
        for (QuadTree.Node n : a) {
            //System.out.println(n.imgName);
            int col = xPos.indexOf(n.topLeftXPos);
            int row = yPos.indexOf(n.topLeftYPos);
            img[row][col] = "img/" + n.imgName + ".png";
            imgNodes[row][col] = n;

        }



        //System.out.println(Arrays.deepToString(img));

        results.put("render_grid", img);
        results.put("raster_ul_lon", imgNodes[0][0].topLeftXPos);
        results.put("raster_ul_lat", imgNodes[0][0].topLeftYPos);
        results.put("raster_lr_lon", imgNodes[imgNodes.length-1][imgNodes[0].length-1].bottomRightXPos);
        results.put("raster_lr_lat", imgNodes[imgNodes.length-1][imgNodes[0].length-1].bottomRightYPos);
        results.put("depth", imgNodes[0][0].depth);
        results.put("query_success", query_success);
        results.put("raster_height", img[0].length * 256);
        results.put("raster_width", img.length * 256);

        //System.out.println(results);

        /*
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                System.out.println(img[i][j]);
            }
        }
        */
       // System.out.println("X TreeSet: ");
        //System.out.println(x);


        //System.out.println("Y TreeSet: ");
        //System.out.println(y);







        // q.root = new Node("root.png", );
        //stop quadtree at certain depth. and then check if intersects the query



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





}
