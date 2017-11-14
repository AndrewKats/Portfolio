package bank;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BankQueueAlgorithm 
{
	public static void main(String[] args) 
	{
		Kattio io = new Kattio(System.in, System.out);
		
		int people = io.getInt();
		int minutes = io.getInt();
		
		ArrayList<ArrayList<Integer>> customers = new ArrayList<ArrayList<Integer>>(4);

		for(int i=0; i<minutes; i++)
		{
			customers.add(null);
		}
		
		for(int i=0; i<people; i++)
		{
			int personMoney = io.getInt();
			int personTime = io.getInt();
			if(customers.get(personTime) == null)
			{
				customers.set(personTime, new ArrayList<Integer>());
			}
			customers.get(personTime).add(personMoney);
		}
		
		for(int i=0; i<customers.size(); i++)
		{
			if(customers.get(i) != null)
			{
				customers.get(i).sort(null);
			}
		}
		
		
		
		int cash = maxCash(customers, minutes, people);
		
		io.print(cash);
		
		io.close();
	}

	public static int maxCash(ArrayList<ArrayList<Integer>> customers, int minutes, int people) 
	{
		int totalMoney = 0;
		
		
		for(int i=minutes; i>=0; i--)
		{
			int max = 0;
			int maxIndex = -1;
			int maxMinute = -1;
			for(int j=i; j<minutes; j++)
			{
				for(int ind = 0; ind < people; ind++)
				{	
					if(customers.get(j) != null)
					{
						if(ind < customers.get(j).size())
						{
							if(customers.get(j).get(ind) > max)
							{
								max = customers.get(j).get(ind);
								maxIndex = ind;
								maxMinute = j;
							}
						}
					}
				}
			}
			totalMoney += max;
			if(maxMinute != -1)
			{
				customers.get(maxMinute).remove(maxIndex);
			}
		}
		
		return totalMoney;
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
