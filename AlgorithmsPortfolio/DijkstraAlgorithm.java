package dijkstra;

/*
 * Andrew Katsanevas
 * 10/7/2016
 * CS4150
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.io.BufferedOutputStream;

public class DijkstraAlgorithm
{
	public static void main(String[] args) throws IOException
	{

		Kattio io = new Kattio(System.in, System.out);

		int[] inputArray = new int[4];
		inputArray[0] = io.getInt();
		inputArray[1] = io.getInt();
		inputArray[2] = io.getInt();
		inputArray[3] = io.getInt();

		while (!(inputArray[0] == 0 && inputArray[1] == 0 && inputArray[2] == 0 && inputArray[3] == 0))
		{

			int amountNodes = inputArray[0];
			int amountEdges = inputArray[1];
			int amountQueries = inputArray[2];
			int startIndex = inputArray[3];

			int[] dist = new int[amountNodes];
			Arrays.fill(dist, Integer.MAX_VALUE);

			ArrayList<ArrayList<Edge>> edgeList = new ArrayList<ArrayList<Edge>>(amountNodes);
			for (int i = 0; i < amountNodes; i++)
			{
				edgeList.add(null);
			}
			// Arrays.fill(edges, null);

			for (int i = 0; i < amountEdges; i++)
			{

				int firstIndex = io.getInt();
				int secondIndex = io.getInt();
				int weight = io.getInt();

				if (edgeList.get(firstIndex) == null)
				{
					edgeList.set(firstIndex, new ArrayList<Edge>());
					edgeList.get(firstIndex).add(new Edge(secondIndex, weight));
				} else
				{
					edgeList.get(firstIndex).add(new Edge(secondIndex, weight));
				}
			}

			for (int z = 0; z < amountQueries; z++)
			{
				int secondIndex = io.getInt();

				dijkstra(edgeList, dist, startIndex);

				if (startIndex == secondIndex)
				{
					io.println("0");
				} else if (dist[secondIndex] == Integer.MAX_VALUE)
				{
					io.println("Impossible");
				} else
				{
					io.println("" + dist[secondIndex]);
				}

			}

			inputArray[0] = io.getInt();
			inputArray[1] = io.getInt();
			inputArray[2] = io.getInt();
			inputArray[3] = io.getInt();

			io.println();
		}

		io.close();

	}

	public static void dijkstra(ArrayList<ArrayList<Edge>> edgy, int[] d, int s)
	{
		PriorityQueue<Integer> PQ = new PriorityQueue<Integer>(new Comparator<Integer>(){public int compare(Integer o1, Integer o2){return ((Integer) (d[o1])).compareTo(d[o2]);}});

		d[s] = 0;
		PQ.add(s);

		while (!PQ.isEmpty())
		{
			int u = PQ.remove();

			if (edgy.get(u) != null)
			{
				for (Edge lookEdge : edgy.get(u))
				{
					if (d[lookEdge.toVertex] > d[u] + lookEdge.weight)
					{
						d[lookEdge.toVertex] = d[u] + lookEdge.weight;
						PQ.add(lookEdge.toVertex);
					}
				}

			}
		}
	}

	public static class Edge
	{
		public int toVertex;
		public int weight;

		public Edge(int theToVertex, int theWeight)
		{
			toVertex = theToVertex;
			weight = theWeight;
		}
	}
}

class Kattio extends PrintWriter
{
	public Kattio(InputStream i)
	{
		super(new BufferedOutputStream(System.out));
		r = new BufferedReader(new InputStreamReader(i));
	}

	public Kattio(InputStream i, OutputStream o)
	{
		super(new BufferedOutputStream(o));
		r = new BufferedReader(new InputStreamReader(i));
	}

	public boolean hasMoreTokens()
	{
		return peekToken() != null;
	}

	public int getInt()
	{
		return Integer.parseInt(nextToken());
	}

	public double getDouble()
	{
		return Double.parseDouble(nextToken());
	}

	public long getLong()
	{
		return Long.parseLong(nextToken());
	}

	public String getWord()
	{
		return nextToken();
	}

	private BufferedReader r;
	private String line;
	private StringTokenizer st;
	private String token;

	private String peekToken()
	{
		if (token == null)
			try
			{
				while (st == null || !st.hasMoreTokens())
				{
					line = r.readLine();
					if (line == null)
						return null;
					st = new StringTokenizer(line);
				}
				token = st.nextToken();
			} catch (IOException e)
			{
			}
		return token;
	}

	private String nextToken()
	{
		String ans = peekToken();
		token = null;
		return ans;
	}
}
