package autosink;

/*
 * Andrew Katsanevas
 * 9/23/2016
 * CS4150
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class SinkAlgorithm
{
	public static int clock = 1;
	public static void main(String[] args) throws IOException
    {		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inputString = br.readLine();
        
		ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
        
		while(!inputString.equals("0 0 0 0"))
		{
		
			HashMap<Vertex, ArrayList<Edge>> graph = new HashMap<Vertex, ArrayList<Edge>>();
			ArrayList<String> stringsToPrint = new ArrayList<String>();
		
	        
			String[] inputArray = inputString.split(" ");
	        int amountNodes = Integer.parseInt(inputArray[0]);
	        int amountEdges = Integer.parseInt(inputArray[1]);
	        int amountQueries = Integer.parseInt(inputArray[2]);
	        int startIndex = Integer.parseInt(inputArray[3]);
	        
	        for(int i=0; i<amountNodes; i++)
	        {
	            Vertex newVert = new Vertex(i);
	            graph.put(newVert, new ArrayList<Edge>());
	        }
	        
	        for(int i=0; i<amountEdges ; i++)
	        {
	        	String edgeString = br.readLine();
	        	String[] edgeArray = edgeString.split(" ");
	        	int firstIndex = Integer.parseInt(edgeArray[0]);
	        	int secondIndex = Integer.parseInt(edgeArray[1]);
	        	int weight = Integer.parseInt(edgeArray[2]);
	        	
	        	for(Vertex firstVert : graph.keySet())
	            {
	            	if(firstVert.name == firstIndex)
	            	{
	            		for(Vertex secondVert : graph.keySet())
	            		{
	            			if(secondVert.name == secondIndex)
	            			{
	            				ArrayList<Edge> originalList = new ArrayList<Edge>(graph.get(firstVert));
	            				originalList.add(new Edge(secondVert, weight));
	            				graph.put(firstVert, originalList);
	            			}
	            		}
	            	}
	            }
	        }
	        
	        
	        for(int z=0; z<amountQueries; z++)
	        {	            
	            int secondPlace = Integer.parseInt(br.readLine());
	            
	            for(Vertex vert : graph.keySet())
	            {
	            	if(vert.name == startIndex)
	            	{
	            		dijkstra(graph, vert);
	            		
	            		for(Vertex dest : graph.keySet())
	            		{
	            			if(dest.name == secondPlace)
	            			{
	            				if(dest.name == vert.name)
	            				{
	            					stringsToPrint.add("0");
	            				}
	            				else if(dest.distance == Integer.MAX_VALUE)
	            				{
	            					stringsToPrint.add("Impossible");
	            				}
	            				else
	            				{
	            					stringsToPrint.add("" + dest.distance);
	            				}
	            			}
	            		}
	            	}
	            }
	        }
	        	           
        
	        resultList.add(stringsToPrint);
	        
	        inputString = br.readLine();
		}
		
		br.close();
        
		
		for(ArrayList<String> r : resultList)
		{
			for(String s : r)
			{
				System.out.println(s);
			}
			System.out.println("");
		}
    }
	
	
	public static void dijkstra(HashMap<Vertex, ArrayList<Edge>> graph, Vertex source)
	{	
		
		for(Vertex startVert : graph.keySet())
		{
			startVert.distance = Integer.MAX_VALUE;
			//startVert.previous = null;
		}
		
		
		source.distance=0;
		
		PriorityQueue<Vertex> PQ = new PriorityQueue<Vertex>();
		insertOrChange(PQ, source, 0);
		
		while(!PQ.isEmpty())
		{
			Vertex u = PQ.remove();
			
			for(Edge e : graph.get(u))
			{
				if(e.toVertex.distance > u.distance + e.weight)
				{
					e.toVertex.distance = u.distance + e.weight;
					//child.previous = u;
					insertOrChange(PQ, e.toVertex, e.toVertex.distance);
				}
			}
		}
	}
	
	
	public static void insertOrChange(PriorityQueue<Vertex> PQ, Vertex v, int newDistance)
	{
		if(PQ.contains(v))
		{
			PQ.remove();
			v.distance = newDistance;
			PQ.add(v);
		}
		else
		{
			v.distance = newDistance;
			PQ.add(v);
		}
	}
	

	
	public static class Vertex implements Comparator<Vertex>, Comparable<Vertex>
    {
		public int name;
        public int distance;
        public Vertex(int theName)
        {
            name = theName;
            distance = Integer.MAX_VALUE;
        }
        
		@Override
		public int compare(Vertex o1, Vertex o2) 
		{
			if(o1.distance < o2.distance)
			{
				return 1;
			}
			if(o1.distance > o2.distance)
			{
				return -1;
			}
			
			return 0;
		}

		@Override
		public int compareTo(Vertex arg0) 
		{
			if(this.distance < arg0.distance)
			{
				return 1;
			}
			if(this.distance > arg0.distance)
			{
				return -1;
			}
			
			return 0;
		}
    }
	
	public static class Edge
	{
		public Vertex toVertex;
		public int weight;
		
		public Edge(Vertex theToVertex, int theWeight)
		{
			toVertex = theToVertex;
			weight = theWeight;
		}
	}
}
