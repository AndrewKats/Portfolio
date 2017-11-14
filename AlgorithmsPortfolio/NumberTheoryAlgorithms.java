package numbertheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NumberTheoryAlgorithms 
{
	
	public static void main(String[] args) throws IOException
    {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line="";
		while((line = br.readLine()) != null)
		{
	
			String[] lineArray = line.split(" ");
			
			String nextOp = lineArray[0];
			if(nextOp.equals("gcd"))
			{
				long first = Long.parseLong(lineArray[1]);
				long second = Long.parseLong(lineArray[2]);
				System.out.println(gcd(first, second));
			}
			else if(nextOp.equals("exp"))
			{
				long first = Long.parseLong(lineArray[1]);
				long second = Long.parseLong(lineArray[2]);
				long third = Long.parseLong(lineArray[3]);
				System.out.println(modexp(first, second, third));
			}
			else if(nextOp.equals("inverse"))
			{
				long first = Long.parseLong(lineArray[1]);
				long second = Long.parseLong(lineArray[2]);
				System.out.println(inverse(first, second));
			}
			else if(nextOp.equals("isprime"))
			{
				long first = Long.parseLong(lineArray[1]);
				System.out.println(isPrime(first));
			}
			else if(nextOp.equals("key"))
			{
				long first = Long.parseLong(lineArray[1]);
				long second = Long.parseLong(lineArray[2]);
				System.out.println(key(first, second));
			}
			
		}
		
		br.close();	
    }
	
	public static long mod(long a, long b)
	{
		return (((a%b) + b) % b);
	}
	
	public static long gcd(long a, long b)
	{
		if(b==0)
		{
			return a; 
		}
		else
		{
			return gcd(b, mod(a,b));
		}
	}
	
	public static long modexp(long x, long y, long N)
	{
		if(y==0)
		{
			return 1;
		}
		else
		{
			long z = modexp(x, y/2, N);
			if(y%2 == 0)
			{
				return mod(z*z,N);
			}
			else
			{
				long mz = mod(z*z, N);
				long mx = mod(x, N);
				return mod(mz*mx,N);
			}
		}
	}
	
	public static long[] ee(long a, long b)
	{
		if(b==0)
		{
			long[] result = new long[3];
			result[0] = 1;
			result[1] = 0;
			result[2] = a;
			return result;
		}
		else
		{
			long[] recur = ee(b, mod(a,b));
			
			long[] result = new long[3];
			result[0] = recur[1];
			result[1] = recur[0]-(a/b)*recur[1];
			result[2] = recur[2];
			return result;
		}
	}
	
	public static String inverse(long a, long N)
	{
		long[] eeArray = ee(a,N);
		if(eeArray[2]==1)
		{
			return "" + mod(eeArray[0],N);
		}
		else
		{
			return "none";
		}
	}
	
	
	public static String isPrime(long N)
	{
		long a1 = 2;
		long a2 = 3;
		long a3 = 5;
		
		if(modexp(a1, N-1, N)==1 && modexp(a2, N-1, N)==1 && modexp(a3, N-1, N)==1)
		{
			return "yes";
		}
		else
		{
			return "no";
		}
	}
	
	public static long modulus(long p, long q)
	{
		return p*q;
	}
	
	public static long publicExponent(long p, long q)
	{
		long phi = (p-1)*(q-1);
		for(long i=2; i<phi; i++)
		{
			if(gcd(i,phi)==1)
			{
				return i;
			}
		}
		return -1;
	}
	
	public static long privateExponent(long p, long q)
	{
		long e = publicExponent(p, q);
		long phi = (p-1)*(q-1);
		
		long d = Long.parseLong(inverse(e,phi));
		
		return d;
	}
	
	public static String key(long p, long q)
	{
		return modulus(p,q) + " " + publicExponent(p,q) + " " + privateExponent(p,q);
	}
	

}