package artgallery;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ArtGalleryAlgorithm 
{
	static int[][] values;
	
	static HashMap<String, Integer> memoMap = new HashMap<String, Integer>();
	
	public static void main(String[] args) 
	{
		Kattio io = new Kattio(System.in, System.out);
		
		int n = io.getInt();
		int k = io.getInt();
		
		values = new int[n][2];
		
		for(int i=0; i<n; i++)
		{
			values[i][0] = io.getInt();
			values[i][1] = io.getInt();
		}
		
		
		int result = maxValue(0, -1, k);
		io.println(result);
		
		io.close();
	}
	
	public static int maxValue(int r, int uncloseableRoom, int k)
	{
		String key = "" + r + "," + uncloseableRoom + "," + k;
		if(memoMap.get(key) != null)
		{
			return memoMap.get(key);
		}
		
		if(r == values.length)
		{
			return 0;
		}
		
		int max = 0;
		
		if(k < values.length - r)
		{
			if(uncloseableRoom == 0)
			{
				max = maxOfThree(max, values[r][0] + maxValue(r+1, 0, k-1), values[r][0] + values[r][1] + maxValue(r+1, -1, k));
			}
			else if(uncloseableRoom == 1)
			{
				max = maxOfThree(max, values[r][1] + maxValue(r+1, 1, k-1), values[r][0] + values[r][1] + maxValue(r+1, -1, k));
			}
			else if(uncloseableRoom == -1)
			{
				max = maxOfFour(max, values[r][0] + maxValue(r+1, 0, k-1), values[r][1] + maxValue(r+1, 1, k-1), values[r][0] + values[r][1] + maxValue(r+1, -1, k));
			}
		}
		else if(k == values.length - r)
		{
			if(uncloseableRoom == 0)
			{
				max = Integer.max(max, values[r][0] + maxValue(r+1, 0, k-1));
			}
			else if(uncloseableRoom == 1)
			{
				max = Integer.max(max, values[r][1] + maxValue(r+1, 1, k-1));
			}
			else if(uncloseableRoom == -1)
			{
				max = maxOfThree(max, values[r][0] + maxValue(r+1, 0, k-1), values[r][1] + maxValue(r+1, 1, k-1));
			}
		}
		
		memoMap.put(key, max);
		
		return max;
	}
	
	public static int maxOfThree(int a, int b, int c)
	{
		int max = Integer.MIN_VALUE;
		max = Integer.max(max, a);
		max = Integer.max(max, b);
		max = Integer.max(max, c);
		return max;
	}
	
	public static int maxOfFour(int a, int b, int c, int d)
	{
		int max = Integer.MIN_VALUE;
		max = Integer.max(max, a);
		max = Integer.max(max, b);
		max = Integer.max(max, c);
		max = Integer.max(max, d);
		return max;
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