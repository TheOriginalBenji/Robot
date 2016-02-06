
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javafx.geometry.Point2D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author GP60
 */
public class GraphToTree {

    protected Map<String, String> parent = new HashMap<String, String>(),
            origName = new HashMap<String, String>();
    protected Stack stack = new Stack();
    String vertexString;
    int count = 0;
    WeightedGraph graphNew = new WeightedGraph();

    public GraphToTree(WeightedGraph<Integer> graph) {
        parent.put("1", " ");
        origName.put("1", Integer.toString(graph.getVertex(0)));
        stack.push(graph.getVertex(0));
        graphNew.addVertex(graph.getVertex(0));
        for (int edges: graph.getNeighbors(0)){
            graphNew.addVertex(edges);
            graphNew.addEdge(edges,graph.getVertex(0),edges.getWeight(edges,graph.getVertex(0)));
        }
        

        while (true) {
            if (stack.empty()) {
                break;
            }
            vertexString = (String) stack.pop();
            String countString = Integer.toString(count);

            parent.put(countString, parent.get(count - 1) + Integer.toString(graph.getVertex(count - 1)));

            origName.put(countString, Integer.toString(graph.getVertex(count)));

            graphNew.addVertex(graph.getVertex(count));

        }
    }
}
