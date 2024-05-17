/* WUGraph.java with Linked Lists and Hash Tables */
// Vincent Z. Hsiao, Yosef Samara

package graph;
import dict.*;
import list.*;
import set.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */



public class WUGraph {

  // vertex class

  // Edge private class to support the "weight" functionality and pointers to half-node positions in the adjacency set to keep the removal operation O(D)
  // VertexPair edgeAsVertexPair(): converts the edge's two vertex objects into a VertexPair object
  private class Edge{
    // vertex1 and vertex2: the "Vertexes" in the edge connection. Passed in with the constructor
    // DListNode v1ptr, v2ptr: The pointers which point to the adjacency list node positions, of each connection, for each vertex's linked list.
    // int weight: the weight of the connection, passed in via the Adjacency Graph

    Object vertex1;
    Object vertex2;
    DListNode v1ptr;
    DListNode v2ptr;
    int weight;
    Edge(Object v1, Object v2, int weight){
      vertex1 = v1;
      vertex2 = v2;

      this.weight = weight;
    }

    int getWeight(){
      return weight;
    }
    void setv1ptr(DListNode v1ptr){
      this.v1ptr=v1ptr;
    }
    void setv2ptr(DListNode v2ptr){
      this.v2ptr=v2ptr;
    }
    void setWeight(int weight){
      this.weight=weight;
    }

    // return the edge as a vertex pair
    VertexPair edgeAsVertexPair(){
      return new VertexPair(vertex1, vertex2);
    }

  }

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */

  // VertexAdjList: The adjacency list with the vertex object as a key and a doubly-linked Linked List as its value, storing the vertexes in which it shares edges with.
  // edgesList: Hash table with VertexPair(vertex1, vertex2) as its key and a Edge object as its value
  // vertexList: A doubly linked list containing all of the vertices of the grpah
  HashTableChained vertexAdjList;
  HashTableChained edgesList;

  DList vertexList;
  int vertexCount;
  int edgesCount;


  // default constructor with zero parameters: initializes an empty graph with no edges/vertices
  public WUGraph(){

    // get everything initialized!


    vertexAdjList = new HashTableChained();
    edgesList = new HashTableChained();

    vertexList = new DList();

    vertexCount=0;
    edgesCount=0;

  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount(){
    return vertexCount;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount(){
    return edgesCount;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */


  //
  public Object[] getVertices(){
    // temporary pointer to traverse the linked list
    DListNode temp = vertexList.front();
    Object[] retVal = new Object[vertexCount];
    for(int i = 0; i < vertexCount; i++){
      retVal[i] = temp.item;
      temp = vertexList.next(temp);
    }
    return retVal;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex){
    // if the vertex does not have a entry in the Vertex Adjacency list, add a entry with key=vertex object and value = empty doubly linked list
    // increment vertexCont by one, and add to vertex linked list.
    if(vertexAdjList.find(vertex) == null) {
      vertexList.insertBack(vertex);
      vertexAdjList.insert(vertex, new DList());
      vertexCount++;
    }


  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex){
    // if the vertex exists, get the vertex's neighbors from the adjacency list
    // traverse through this Doubly linked list and get the Edge object of each connection from the edge hashtable
    // via a new VertexPair with the current vertex and the vertex of the pointer
    // remove both half-edge pointers from their respective linked lists, and decrease edgeCount by one
    // once done, remove the vertex from the adjacency list and decrease the vertexCount
    // TO DO: Remove the vertex from the Vertex linked list
    if(vertexAdjList.find(vertex) != null){ // if it finds the vertex
      DList vertexConnections = ((DList) vertexAdjList.find(vertex).value()); // set vertexConnections as the adjacency list of the said vertex
      int adjListLength = vertexConnections.length();
      DListNode copy = vertexConnections.front(); // create a copy of the front of vertexConnections to traverse
      for (int i = 0; i < adjListLength; i++){
        VertexPair curEdgeVertexPair = new VertexPair(vertex, copy.item); // set curEdgeVertexPair as a new VertexPair(vertex passed in to removeVertex, vertex which copy is on)
        if(edgesList.find(curEdgeVertexPair) != null){
          this.removeEdge(vertex, copy.item); // remove the edge of the vertex

          /*Edge curEdge = ((Edge) edgesList.find(curEdgeVertexPair).value());
          ((DList) vertexAdjList.find(curEdge.vertex1).value()).remove(curEdge.v1ptr);
          ((DList) vertexAdjList.find(curEdge.vertex2).value()).remove(curEdge.v2ptr);
          edgesList.remove(curEdgeVertexPair);
          edgesCount--; */
        }
        copy = vertexConnections.next(copy);
      }
      vertexAdjList.remove(vertex);

      // remove the vertex from the vertex linked list
      DListNode copy2 = vertexList.front();
      for (int i = 0; i < vertexCount; i++){
        if(copy2.item.equals(vertex)){
          vertexList.remove(copy2);
          break;
        }
        copy2=vertexList.next(copy2);
      }
      vertexCount--;
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex){
    // true if the vertex is in the vertex adjacency list's keys, false otherwise
    if(vertexAdjList.find(vertex) != null){
      return true;
    }
    return false;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex){
    // count the number of vertices in the vertex's adjacency list linked list
    if(vertexAdjList.find(vertex) != null){
      return ((DList) vertexAdjList.find(vertex).value()).length();
    }
    return 0;
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
    // Get the vertex's linked list from the adjacency list hashtable
    // Traverse through this list and store its different partners and their edge weights (from the edgesList hash talbe) in lists
    // return it as a Neighbors object
    DList vertexNeighbors = ((DList) vertexAdjList.find(vertex).value());
    Object[] neighborVertices = new Object[vertexNeighbors.length()];

    int[] weights = new int[vertexNeighbors.length()];

    DListNode copy = vertexNeighbors.front();


    for (int i = 0; i < vertexNeighbors.length(); i++){
      Object curVertice = copy.item;
      int curWeight = ((Edge) edgesList.find(new VertexPair(vertex, curVertice)).value()).getWeight();
      weights[i] = curWeight;
      neighborVertices[i] = curVertice;
      copy=vertexNeighbors.next(copy);
    }
    Neighbors retVal = new Neighbors();
    retVal.neighborList = neighborVertices;
    retVal.weightList = weights;
    if(neighborVertices.length == 0){
      return null;
    }
    return retVal;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){
    // if a existing edge does not exist in the vertex adjacency list, add a new Edge object
    // initialize this object with vertex1 and vertex2 = u and v, vertex pointer 1 pointing to the adjacency list node of v in u, and vertex pointer 2 pointing to the adjacency list node ov u in v
    // add the edge into the edge hashtable, key = the vertex pair, and increment the edgescount
    if(vertexAdjList.find(u) != null && vertexAdjList.find(v) != null){
      if(edgesList.find(new VertexPair(u,v)) == null) {
        Edge tempEdge = new Edge(u, v, weight);
        // if it is a self edge
        if(u.equals(v)){
          ((DList) vertexAdjList.find(u).value()).insertBack(v);
          tempEdge.setv1ptr(((DList) vertexAdjList.find(u).value()).back());
          tempEdge.setv2ptr(null);

        } else {
          ((DList) vertexAdjList.find(u).value()).insertBack(v);
          tempEdge.setv1ptr(((DList) vertexAdjList.find(u).value()).back());
          ((DList) vertexAdjList.find(v).value()).insertBack(u);
          tempEdge.setv2ptr(((DList) vertexAdjList.find(v).value()).back());
        }
        edgesList.insert(new VertexPair(u, v), tempEdge);
        edgesCount++;
      } else {
        // if the edge already exists, but is added again with a different weight,
        // update the existing edge with the new weight
        ((Edge) edgesList.find(new VertexPair(u,v)).value()).setWeight(weight);
      }
    }

  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    // if the vertexpair(u,v) is a key in the edges list, remove the edge
    // remove its references in the vertex adjacency list by removing the nodes referenced by the two pointers

    if (edgesList.find(new VertexPair(u, v)) != null && vertexAdjList.find(u) != null && vertexAdjList.find(v) != null) { // if the VertexPair(u,v) is found in the edgeList, vertex u and v is found in the adjacency list
      ((DList) vertexAdjList.find(u).value()).remove(((Edge) edgesList.find(new VertexPair(u, v)).value()).v1ptr); // -> remove the vertex v instance in u's adjacency list (v1ptr is a quick o(1) reference to this node)

      if(((Edge) edgesList.find(new VertexPair(u, v)).value()).v2ptr != null) {
        // a self-edge would fail this condition, as the v1ptr is the only node, and v2ptr is null
        ((DList) vertexAdjList.find(v).value()).remove(((Edge) edgesList.find(new VertexPair(u, v)).value()).v2ptr);

      }


      edgesList.remove(new VertexPair(u, v)); // edgeList removes the vertexPair and its Edge object value from the hash table
      // flawed line
      edgesCount--; // decrements the edge count of the graph by one
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */

  public boolean isEdge(Object u, Object v){
    if(edgesList.find(new VertexPair(u,v)) != null){
      return true;
    } return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v){
    // if the edge exists, return its "weight"
    if(edgesList.find(new VertexPair(u,v)) != null){
      return ((Edge) edgesList.find(new VertexPair(u,v)).value()).getWeight();
    }
    return 0;
  }

}
