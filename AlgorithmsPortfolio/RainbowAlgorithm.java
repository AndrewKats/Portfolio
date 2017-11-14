package rainbow;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RainbowAlgorithm 
{
	static int[] distance;
	
	static HashMap<Integer, Integer> memoMap = new HashMap<Integer, Integer>();
	
	public static void main(String[] args) 
	{
		Kattio io = new Kattio(System.in, System.out);
		
		int hotels = io.getInt();
		
		distance = new int[hotels+1];
		
		for(int i=0; i<hotels+1; i++)
		{
			distance[i] = io.getInt();
		}
		
			
		int result = followTheYellowBrickRoad();
		io.print(result);
		
		io.close();
	}
	
	
	public static int followTheYellowBrickRoad()
	{
		return penalty(0);
	}
	
	public static int penalty(int i)
	{
		if(memoMap.get(i) != null)
		{
			return memoMap.get(i);
		}
		
		if(i==distance.length-1)
		{	
			return 0;
		}
		
		int min = Integer.MAX_VALUE;
		
		for(int j=1; j+i<distance.length; j++)
		{
			min = Integer.min(min, (int) Math.pow(400-(distance[i+j] - distance[i]), 2) + penalty(i+j));
		}
		
		memoMap.put(i, min);
		
		return min;
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
