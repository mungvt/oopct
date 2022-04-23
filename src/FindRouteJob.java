import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class FindRouteJob
{
    String fileName;
    Dictionary edges = new Hashtable();
    List<List<String>> connections = new ArrayList<>();
    List<List<String>> allRoutes = new ArrayList<>();
    public FindRouteJob(){
        fileName = "vd012.net.xml";
        this.readData();
    }
    private void readData()
    {
        try
        {
            File file = new File(this.fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList edgeList = doc.getElementsByTagName("edge");
            int num_edge = edgeList.getLength();
            for (int itr = 0; itr < num_edge; itr++)
            {
                Node edge = edgeList.item(itr);
                if (edge.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element edgeElement = (Element) edge;
                    String edge_id = edgeElement.getAttribute("id");
                    NodeList laneList = edgeElement.getElementsByTagName("lane");
                    List<String> lane_list = new ArrayList<>();
                    int num_lane = laneList.getLength();
                    for (int i = 0; i < num_lane; i++) {
                        Node lane = laneList.item(i);
                        if (lane.getNodeType() == Node.ELEMENT_NODE) {
                            Element laneElement = (Element) lane;
                            String lane_id = laneElement.getAttribute("id");
                            String lane_index = laneElement.getAttribute("index");
                            String lane_length = laneElement.getAttribute("length");
                            lane_list.add(lane_id);
                            lane_list.add(lane_index);
                            lane_list.add(lane_length);
                        }
                    }
                    this.edges.put(edge_id, lane_list);
                }
            }

            NodeList connectionList = doc.getElementsByTagName("connection");
            int num_connection = connectionList.getLength();
            for (int itr = 0; itr < num_connection; itr++) {
                Node connection = connectionList.item(itr);
                List<String> connectionInfo = new ArrayList<>();
                if (connection.getNodeType() == Node.ELEMENT_NODE) {
                    Element connectionElement = (Element) connection;
                    String from = connectionElement.getAttribute("from");
                    String to = connectionElement.getAttribute("to");
                    String fromLane = connectionElement.getAttribute("fromLane");
                    String toLane = connectionElement.getAttribute("toLane");
                    connectionInfo.add(from);
                    connectionInfo.add(to);
                    connectionInfo.add(fromLane);
                    connectionInfo.add(toLane);
                    }
                this.connections.add(connectionInfo);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<String> findEdgeOfLane(String laneId){
        List<String> routes = new ArrayList<>();
        for (Enumeration edge = this.edges.keys(); edge.hasMoreElements();){
            String edge_id = String.valueOf(edge.nextElement());
            List<String> lanes = (List<String>) this.edges.get(edge_id);
            for(int i = 0; i < lanes.size(); i++){
                String lane_id = String.valueOf(lanes.get(i));
                if(lane_id.equals(laneId)){
                    routes.add(edge_id);
                    String lane_index = lanes.get(i + 1);
                    String lane_length = lanes.get(i + 2);
                    routes.add(lane_index);
                    routes.add(lane_length);
                }
            }
        }
        return routes;
    }
    public String getOppositeLane(String landId){
        char Dash = '-';
        if (Character.compare(landId.charAt(0), Dash) == 0){
            return landId.substring(1);
        }
        else {
            return String.valueOf(Dash).concat(landId);
        }
    }
    public List<String> findConnections(String edgeId, String laneIndex){
        List<String> connections = new ArrayList<>();
        for (List<String> connection : this.connections) {
            if (connection.get(0).equals(edgeId) && connection.get(2).equals(laneIndex)) {
                connections.add(connection.get(1)+ "_" + connection.get(3));
            }
        }
        return connections;
    }

    public boolean checkFoundLane(String laneId){
        for(List<String> route : this.allRoutes){
            if(route.get(0).equals(laneId)){
                return true;
            }
        }
        return false;
    }
    public void findAllRoutes(String laneId){

        List<String> edge = this.findEdgeOfLane(laneId);
        if(!edge.isEmpty()) {
            List<String> connections = new ArrayList<>();
            List<String> route = new ArrayList<>();
            route.add(laneId);
            route.add(edge.get(2));
            route.add(this.getOppositeLane(laneId));
            route.addAll(this.findConnections(edge.get(0), edge.get(1)));
            this.allRoutes.add(route);
            for (int i = route.size() - 1; i > 1; i--) {
                String lane = route.get(i);
                if (!this.checkFoundLane(lane)) {
                    this.findAllRoutes(route.get(i));
                }
            }
        }
    }

    public void printFoundEdge(String laneId){
        List<String> edge = this.findEdgeOfLane(laneId);
        if(edge.isEmpty()) {
            System.out.println("The lane not found!");
        }
        else {
            System.out.println("This is info of your lane: ");
            System.out.println("    - Lane Id: " + laneId);
            System.out.println("    - Edge Id: " + edge.get(0));
            System.out.println("    - Index: " + edge.get(1));
            System.out.println("    - Length: " + edge.get(2));
        }
    }

    public void writeFoundRoutes(String laneId){
        this.findAllRoutes(laneId);
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            for(List<String> route : this.allRoutes){
                String stringRoute = String.join(" ", route);
                myWriter.write(stringRoute);
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote all found routes to the file as 'output.txt'.");
        } catch (IOException e) {
            System.out.println("An error occurred when write file.");
            e.printStackTrace();
        }
    }

    public void run(){
        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type your lane: ");
            String laneId = scanner.nextLine();
            this.printFoundEdge(laneId);
            this.writeFoundRoutes(laneId);
            System.out.println("Type 0 to exit 1 to continue: ");
            String option = scanner.nextLine();
            if(option.equals("0")){
                break;
            }
        }
    }
    public static void main(String[] args) {
        FindRouteJob firstJob = new FindRouteJob();
        firstJob.run();
    }

}
