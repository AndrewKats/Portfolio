package salesman;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.StringTokenizer;

public class SalesmanAlgorithm 
{
	static Kattio io = new Kattio(System.in, System.out);
	static int size = io.getInt();
	static int[][] graph = new int[size][size];
	
	static int minWeight = Integer.MAX_VALUE;
	
	static int bestSoFar = Integer.MAX_VALUE;
	int weightSoFar = Integer.MAX_VALUE;
	
	static HashSet<Integer> visited = new HashSet<Integer>();
	
	static int[] minRow = new int[size];
	
	static int minSum = 0;
	
	public static void main(String[] args) 
	{	
		
		for(int i=0; i<size; i++)
		{
			int rowMin = Integer.MAX_VALUE;
			for(int j=0; j<size; j++)
			{
				int addThis = io.getInt();
				minWeight = Integer.min(minWeight, addThis);
				rowMin = Integer.min(rowMin, addThis);
				graph[i][j] = addThis;
			}
			minRow[i] = rowMin;
			minSum += rowMin;
		}
		
		
		salesmanAlgorithm(0, 0, minSum);
		
		io.println(bestSoFar);
		
		io.close();
	}
	
	
	public static void salesmanAlgorithm(int start, int soFar, int minPoss)
	{
		
		int visitedSize = visited.size();
		if(!visited.contains(start))
		{
			visitedSize++;
		}
		
		
		if(visitedSize == size)
		{
			int tryThis = soFar + graph[start][0];
			if(tryThis < bestSoFar)
			{
				bestSoFar = tryThis;
			}
			return;		
		}
		else
		{	
			int nextMinPoss = minPoss - minRow[start];
			int possibleMin = soFar + nextMinPoss + minWeight;
			if(possibleMin >= bestSoFar)
			{
				return;
			}
			
			visited.add(start);
			
			for(int i=0; i<size; i++)
			{
				if(!visited.contains(i))
				{
					int nextWeight = graph[start][i];
					salesmanAlgorithm(i, soFar+nextWeight, nextMinPoss);
				}
			}
			
			visited.remove(start);
			return;	
		}
	}
}

class Kattio extends PrintWriter 
{
	public Kattio(InputStream i) {
		super(new BufferedOutputStream(System.out));
		r = new BufferedReader(new InputStreamReader(i));
	}

	public Kattio(InputStream i, OutputStream o) {
		super(new BufferedOutputStream(o));
		r = new BufferedReader(new InputStreamReader(i));
	}

	public boolean hasMoreTokens() {
		return peekToken() != null;
	}

	public int getInt() {
		return Integer.parseInt(nextToken());
	}

	public double getDouble() {
		return Double.parseDouble(nextToken());
	}

	public long getLong() {
		return Long.parseLong(nextToken());
	}

	public String getWord() {
		return nextToken();
	}

	private BufferedReader r;
	private String line;
	private StringTokenizer st;
	private String token;

	private String peekToken() {
		if (token == null)
			try {
				while (st == null || !st.hasMoreTokens()) {
					line = r.readLine();
					if (line == null)
						return null;
					st = new StringTokenizer(line);
				}
				token = st.nextToken();
			} catch (IOException e) {
			}
		return token;
	}

	private String nextToken() {
		String ans = peekToken();
		token = null;
		return ans;
	}
}