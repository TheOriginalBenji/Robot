import java.util.*;

public class UnWeightedGraph<V> extends AbstractGraph<V> {
  /** Construct an empty graph */
  public UnWeightedGraph() {
  }
    
  /** Construct a graph from vertices and edges stored in arrays */
  public UnWeightedGraph(V[] vertices, int[][] edges) {
    super(vertices, edges);
  }

  /** Construct a graph from vertices and edges stored in List */
  public UnWeightedGraph(List<V> vertices, List<Edge> edges) {
    super(vertices, edges);
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  public UnWeightedGraph(List<Edge> edges, int numberOfVertices) {
    super(edges, numberOfVertices);
  }
  
  /** Construct a graph from integer vertices 0, 1, and edge array */
  public UnWeightedGraph(int[][] edges, int numberOfVertices) {
    super(edges, numberOfVertices);
  }
}