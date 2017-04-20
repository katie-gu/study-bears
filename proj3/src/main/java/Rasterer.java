//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

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
    NodeComparator nc = new NodeComparator();
    ArrayList<QuadTree.Node> arr = new ArrayList<>();


    TreeSet<Double> x = new TreeSet<>();
    //  TreeSet<Double> y = new TreeSet<>();
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



    public class QuadTree {

        private static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
                ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;

        private Node root = new Node("", 0, ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT);

        //private String prevImgName = "";
        public QuadTree() {
            buildQuadTree(root);

        }

        //recursion?
        private Node buildQuadTree(Node root) {

            if (root.depth == 8) {
                return null;
            }

            root.topLeft = buildQuadTree(new Node((root.imgName + 1), root.depth + 1,
                    root.topLeftXPos, root.topLeftYPos,
                    (root.topLeftXPos + root.bottomRightXPos) / 2,
                    (root.topLeftYPos + root.bottomRightYPos) / 2));

            root.topRight = buildQuadTree(new Node((root.imgName + 2),
                    root.depth + 1,
                    (root.topLeftXPos + root.bottomRightXPos) / 2,
                    root.topLeftYPos, root.bottomRightXPos,
                    (root.topLeftYPos + root.bottomRightYPos) / 2));

            root.bottomLeft = buildQuadTree(new Node((root.imgName + 3), root.depth + 1,
                    root.topLeftXPos,
                    (root.topLeftYPos + root.bottomRightYPos) / 2,
                    (root.topLeftXPos + root.bottomRightXPos) / 2,
                    root.bottomRightYPos));

            root.bottomRight = buildQuadTree(new Node((root.imgName + 4), root.depth + 1,
                    (root.topLeftXPos + root.bottomRightXPos) / 2,
                    (root.topLeftYPos + root.bottomRightYPos) / 2,
                    root.bottomRightXPos,
                    root.bottomRightYPos));

            return root;

        }

        public class Node {
            Double topLeftXPos, topLeftYPos, bottomRightXPos, bottomRightYPos;
            Node topLeft,  topRight,  bottomLeft,  bottomRight;
            String imgName;
            int depth;
            //store subtrees as instance variables?
            public Node(String imgName, int depth, double topLeftXPos,
                        double topLeftYPos, double bottomRightXPos, double bottomRightYPos) {
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


            public String toString() {
                return new StringBuilder().append("img/").append(imgName)
                        .append(".png").toString();
            }


            public boolean intersectsTile(double query_ulX, double query_ulY,
                                          double query_lrX, double query_lrY) {
                return ((this.topLeftXPos > query_lrX)
                        || (this.bottomRightXPos < query_ulX)
                        || (this.topLeftYPos < query_lrY) || (this.bottomRightYPos > query_ulY));


            }

            public String getImgName() {
                return "img/" + this.imgName + ".png";
            }
        }

    }

    public class NodeComparator implements Comparator<QuadTree.Node> {
        @Override
        public int compare(QuadTree.Node o1, QuadTree.Node o2) {
            if ((o1.topLeftXPos.equals(o2.topLeftXPos))
                    && (o1.topLeftYPos > o2.topLeftYPos)) {
                return -1;
            } else if ((o1.topLeftYPos.equals(o2.topLeftYPos))
                    && (o1.topLeftXPos < o2.topLeftXPos)) {
                return -1;
            } else if ((o1.topLeftXPos < o2.topLeftXPos)
                    && (o1.topLeftYPos > o2.topLeftYPos)) {
                return -1;
            } else if ((o1.topLeftXPos > o2.topLeftXPos)
                    && (o1.topLeftYPos > o2.topLeftYPos)) {
                return -1;
            } else if (o1.topLeftXPos.equals(o2.topLeftXPos)
                    && (o1.topLeftYPos.equals(o2.topLeftYPos))) {
                return 0;
            }
            return 1;        }


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
            if (!(n.intersectsTile(params.get("ullon"), params.get("ullat"),
                    params.get("lrlon"), params.get("lrlat")))) {
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
                //  p.add(n);
                //nodeList.add(n);
                x.add(n.topLeftXPos);
                //y.add(n.topLeftYPos);
                //System.out.println("Added: " + n.imgName);
            }
        }
        //}
        return arr;


    }

    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println("Parameters: " + params);
        Map<String, Object> results = new HashMap<>();


        double lonDDP = (params.get("lrlon") - params.get("ullon")) / (params.get("w"));

        ArrayList<QuadTree.Node> pq = pruneTree(params, q.root);
        if (pq == null) {
            query_success = false;
        }
        Collections.sort(pq, nc);

        int col = x.size();
        int row = pq.size() / col;

        //ArrayList<Double> xPos = new ArrayList<Double>(x);
        //System.out.println("xPosList : " + xPos);

        //ArrayList<Double> yPos = new ArrayList<Double>(y);
        //Collections.reverse(yPos);


        String[][] img = new String[row][col];
        //  QuadTree.Node[][] imgNodes = new QuadTree.Node[row][col];

        int rowVal = 0;
        int colVal = 0;
        for (QuadTree.Node n : pq) {
            img[rowVal][colVal] = n.getImgName();
            //  imgNodes[rowVal][colVal] = n;

            if (colVal >= img[0].length - 1) {
                colVal = 0;
                rowVal += 1;
            } else {
                colVal += 1;
            }

            // pq.remove(n);
        }



        //System.out.println("Rastered images: " + Arrays.deepToString(img));

        results.put("render_grid", img);
        results.put("raster_ul_lon", pq.get(0).topLeftXPos);
        results.put("raster_ul_lat", pq.get(0).topLeftYPos);
        results.put("raster_lr_lon", pq.get(pq.size() - 1).bottomRightXPos);
        results.put("raster_lr_lat", pq.get(pq.size() - 1).bottomRightYPos);
        results.put("depth", pq.get(0).depth);
        results.put("query_success", query_success);
        results.put("raster_height", img[0].length * 256);
        results.put("raster_width", img.length * 256);

        pq.clear();
        //System.out.println("Results: " + results);

        x.clear();
        // y.clear();
        //a.clear();

        return results;

    }





}
