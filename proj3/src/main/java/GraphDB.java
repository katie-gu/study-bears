import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */


    //private Node<Integer>[] adj;
    HashMap<Long, Node> h = new HashMap<>();


    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public HashMap<Long, Node> getMap() {
        return h;
    }
    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public void addNode(Node v) {
        h.put(v.getId(), v);
    }

    public void removeNode(Node v) {
        h.remove(v);
    }

    public void addEdge(Node v, Node w) {
        h.get(v.getId()).adj.add(w);
        h.get(w.getId()).adj.add(v);
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> temp = new ArrayList<>();
        temp.addAll(h.keySet());
       // Set hKeys = h.keySet();

        for (Long v: temp) {
            if (!(h.get(v).hasEdge())) {
           // if (h.get(v).adj == null)
                h.remove(v);
            }
        }

    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> vertexId = new ArrayList<>();
        vertexId.addAll(h.keySet());

        return vertexId;
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        ArrayList<Long> adjId = new ArrayList<>();
        for (Node n : h.get(v).adj) {
            adjId.add(n.getId());
        }
        return adjId;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        //long v = 1790732915L;
        //long w = 374609585L;

        double lonDist = (h.get(v).getLongitude() - h.get(w).getLongitude());
        double lonDistSq = lonDist * lonDist;
        double latDist = (h.get(v).getLatitude() - h.get(w).getLatitude());
        double latDistSq = latDist * latDist;

        return Math.sqrt(lonDistSq + latDistSq);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        Node temp = new Node("0", Double.toString(lat), Double.toString(lon));

        //COMMENT OUT LATER
       // h.put(temp.getId(), temp);

        double dist = 1000000;
        double currDist = 0;

        for (Long v : h.keySet()) {
            double lonSq = (h.get(v).getLongitude() - lon) * (h.get(v).getLongitude() - lon);
            double latSq = (h.get(v).getLatitude() - lat) * (h.get(v).getLatitude() - lat);
            currDist = (Math.sqrt(lonSq + latSq));
            if (currDist < dist) {
                dist = currDist;
                temp = h.get(v);
            }

        }

        return temp.getId();
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return h.get(v).getLongitude();
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return h.get(v).getLatitude();
    }
}
