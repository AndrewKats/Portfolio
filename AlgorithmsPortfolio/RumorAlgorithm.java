package rumors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class RumorAlgorithm 
{
	public static int maxDistance = 0;
	public static HashMap<Vertex, ArrayList<Vertex>> graph = new HashMap<Vertex, ArrayList<Vertex>>();
	
	public static void main(String[] args) throws IOException
    {
		ArrayList<String> results = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String studentAmountString = br.readLine();
		int studentAmount = Integer.parseInt(studentAmountString);
		for(int i = 0; i < studentAmount; i++)
		{
			String studentString = br.readLine();
			Vertex newVert = new Vertex(studentString);
			graph.put(newVert, new ArrayList<Vertex>());
		}
		
		String edgeAmountString = br.readLine();
		int edgeAmount = Integer.parseInt(edgeAmountString);
		for(int j = 0; j < edgeAmount; j++)
		{
			String edgeString = br.readLine();
            String[] edgeArray = edgeString.split(" ");
            String firstFriend = edgeArray[0];
            String secondFriend = edgeArray[1];
            
            for(Vertex firstVert : graph.keySet())
            {
            	if(firstVert.name.equals(firstFriend))
            	{
            		for(Vertex secondVert : graph.keySet())
            		{
            			if(secondVert.name.equals(secondFriend))
            			{
            				ArrayList<Vertex> firstOriginalList = new ArrayList<Vertex>(graph.get(firstVert));
            				firstOriginalList.add(secondVert);
            				graph.put(firstVert, firstOriginalList);
            				
            				ArrayList<Vertex> secondOriginalList = new ArrayList<Vertex>(graph.get(secondVert));
            				secondOriginalList.add(firstVert);
            				graph.put(secondVert, secondOriginalList);
            			}
            		}
            	}
            }
		}
		
		String rumorAmountString = br.readLine();
		int rumorAmount = Integer.parseInt(rumorAmountString);
		for(int z=0; z<rumorAmount; z++)
		{
			String sourceString = br.readLine();
			
			for(Vertex vert : graph.keySet())
            {
            	if(vert.name.equals(sourceString))
            	{
            		maxDistance = 0;
            		bfs(graph, vert);
            		results.add(resultString());
            	}
            }
            	
		}
		
		br.close();
		
		for(String s : results)
		{
			System.out.println(s);
		}
    }
	
	public static String resultString()
	{
		String output = "";
		ArrayList<ArrayList<Vertex>> organize = new ArrayList<ArrayList<Vertex>>(maxDistance);	
		for(int i = 0; i < maxDistance + 1; i++)
		{
			organize.add(new ArrayList<Vertex>());
		}
		ArrayList<Vertex> infinites = new ArrayList<Vertex>();
		for(Vertex v : graph.keySet())
		{
			if(v.distance == Integer.MAX_VALUE)
			{
				infinites.add(v);
			}
			else
			{
				organize.get(v.distance).add(v);
			}
		}
		for(ArrayList<Vertex> list : organize)
		{
			list.sort(null);
		}
		
		infinites.sort(null);
		
		for(ArrayList<Vertex> list : organize)
		{
			for(Vertex v : list)
			{
				output += v.name + " ";
			}
		}
		
		for(Vertex v : infinites)
		{
			output = output + v.name + " ";
		}
		
		return output.trim();
	}
	
	public static void bfs(HashMap<Vertex, ArrayList<Vertex>> graph, Vertex source)
	{	
		for(Vertex startVert : graph.keySet())
		{
			startVert.distance = Integer.MAX_VALUE;
			startVert.previous = null;
		}
		
		source.distance = 0;
		
		Queue<Vertex> Q = (Queue<Vertex>) new LinkedList<Vertex>();
		Q.add(source);
		
		while(!Q.isEmpty())
		{
			Vertex u = Q.remove();
			for(Vertex child : graph.get(u))
			{
				if(child.distance == Integer.MAX_VALUE)
				{
					Q.add(child);
					child.distance = u.distance + 1;
					
					if(child.distance > maxDistance)
					{
						maxDistance = child.distance;
					}
					
					child.previous = u;
				}
			}
		}
	}
	
	public static class Vertex implements Comparator<Vertex>, Comparable<Vertex>
    {
		public String name;
        public int distance;
        Vertex previous;
        public Vertex(String theName)
        {
            name = theName;
            distance = Integer.MAX_VALUE;
            previous = null;
        }
        
        @Override
		public int compare(Vertex o1, Vertex o2) 
		{
			return (o1.name).compareTo(o2.name);
		}

		@Override
		public int compareTo(Vertex arg0) 
		{
			return (this.name).compareTo(arg0.name);
		}
        
    }
}
