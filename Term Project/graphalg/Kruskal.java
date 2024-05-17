/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;


import java.util.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 *4e9rowAr82SzIAXX+uJRbQ==
 */


public class Kruskal {

  private static class Edge {
    private Object vertex1;
    private Object vertex2;
    private int weight;

    public Edge(Object vertex1, Object vertex2, int weight) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.weight = weight;
    }

    public int getWeight() {
      return weight;
    }
    public void setWeight(int weight) {
      this.weight = weight;
    }
    public Object getVertex1() {
      return vertex1;
    }
    public void setVertex1(Object vertex1) {
      this.vertex1 = vertex1;
    }

    public Object getVertex2() {
      return vertex2;
    }
    public void setVertex2(Object vertex2) {
      this.vertex2 = vertex2;
    }

  }

  private static void merge(Edge[] numbers, int i, int j, int k) {
    int mergedSize = k - i + 1;                // Size of merged partition
    Edge[] mergedNumbers = new Edge[mergedSize]; // Dynamically allocates temporary
    // array for merged numbers
    int mergePos = 0;                          // Position to insert merged number
    int leftPos = i;                           // Initialize left partition position
    int rightPos = j + 1;                      // Initialize right partition position

    // Add smallest element from left or right partition to merged numbers
    while (leftPos <= j && rightPos <= k) {
      if (numbers[leftPos].getWeight() <= numbers[rightPos].getWeight()) {
        mergedNumbers[mergePos] = numbers[leftPos];
        leftPos++;
      }
      else {
        mergedNumbers[mergePos] = numbers[rightPos];
        rightPos++;
      }
      mergePos++;
    }

    // If left partition is not empty, add remaining elements to merged numbers
    while (leftPos <= j) {
      mergedNumbers[mergePos] = numbers[leftPos];
      leftPos++;
      mergePos++;
    }

    // If right partition is not empty, add remaining elements to merged numbers
    while (rightPos <= k) {
      mergedNumbers[mergePos] = numbers[rightPos];
      rightPos++;
      mergePos++;
    }

    // Copy merged numbers back to numbers
    for (mergePos = 0; mergePos < mergedSize; mergePos++) {
      numbers[i + mergePos] = mergedNumbers[mergePos];
    }
  }

  private static void mergeSort(Edge[] numbers, int i, int k) {
    int j = 0;

    if (i < k) {
      j = (i + k) / 2;  // Find the midpoint in the partition

      // Recursively sort left and right partitions
      mergeSort(numbers, i, j);
      mergeSort(numbers, j + 1, k);

      // Merge left and right partition in sorted order
      merge(numbers, i, j, k);
    }
  }

  private static HashSet<Edge> depthFirstSearch(WUGraph graph, Object startVertex) {
    Stack<Object> vertexStack = new Stack<Object>();
    HashSet<Object> visitedSet = new HashSet<Object>();
    HashSet<Edge> edgeList = new HashSet<Edge>();

    vertexStack.push(startVertex);

    while (!vertexStack.isEmpty()) {
      Object currentVertex = vertexStack.pop();
      if (!visitedSet.contains(currentVertex)) {
        visitedSet.add(currentVertex);
        for(int i = 0; i < graph.getNeighbors(currentVertex).neighborList.length; i++) {
          Object vertex = graph.getNeighbors(currentVertex).neighborList[i];
          Edge edge = new Edge(currentVertex, vertex, graph.getNeighbors(currentVertex).weightList[i]);
          edgeList.add(edge);
          vertexStack.push(vertex);
        }
      }
    }
    return edgeList;
  }

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *4e9rowAr82SzIAXX+uJRbQ==
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {
    HashMap<Object, Integer> vertexMap = new HashMap<>();
    Object[] vertexList = g.getVertices(); //Object Array
    DisjointSets disjointSets = new DisjointSets(vertexList.length);



    //PART 1
    //Create a new graph T having the same vertices as G, but no edges (yet).
    WUGraph t = new WUGraph();
    for(Object vertex : g.getVertices()) {
      t.addVertex(vertex);
    }

    //4e9rowAr82SzIAXX+uJRbQ==
    //PART 2
    //Make a list of all edges in G
    HashSet<Edge> set = new HashSet<>();
    for (Object vertex : g.getVertices()) {
      if (!vertexMap.containsKey(vertex)) {
        set.addAll(depthFirstSearch(g, vertex));
      }
    }
    Edge[] edgeList = set.toArray(new Edge[set.size()]);

    //PART 3
    //Sort edges by weight in O(|E| log |E|) time
    //private static void mergeSort(Edge[] numbers, int i, int k) {
    mergeSort(edgeList, 0, edgeList.length - 1);


    //PART 4.1
    //Create MST using disjoint sets and unions

    Integer vertexNumber = 0;
    //creates a hash table of each vertex that maps to a unique integer
    for(Object vertex : vertexList) {
      vertexMap.put(vertex, vertexNumber);
      vertexNumber++;
    }

    //PART 4.2
    for(Edge edge : edgeList) {
      Object vertex1 = edge.getVertex1();
      Object vertex2 = edge.getVertex2();

      int root1 = disjointSets.find(vertexMap.get(vertex1));
      int root2 = disjointSets.find(vertexMap.get(vertex2));

      if(root1 != root2) {
        t.addEdge(vertex1, vertex2, edge.getWeight());
        disjointSets.union(root1, root2);
      }
    }
    return t;

  }

}

