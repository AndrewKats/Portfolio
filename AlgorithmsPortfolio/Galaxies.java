package galaxies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Galaxies
{

    public static void main(String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String firstLineString = br.readLine();
        String[] firstLineArray = firstLineString.split(" ");
        long distance = Long.parseLong(firstLineArray[0]);
        int starAmount = Integer.parseInt(firstLineArray[1]);
        
        Star[] stars = new Star[starAmount];
        
        for(int i=0; i<starAmount; i++)
        {
            String starString = br.readLine();
            String[] starArray = starString.split(" ");
            Star newStar = new Star(Long.parseLong(starArray[0]), Long.parseLong(starArray[1]));
            stars[i] = newStar;
        }
        
        br.close();

        Star majStar = newFindMajority(stars, distance*distance);
        if(majStar==null)
        {
        	System.out.println("NO");
        }
        else
        {
        	System.out.println(countTheStars(majStar, stars, distance*distance));
        }
        
        /*
        ArrayList<Long> assignedGalaxies = assignGalaxies(stars, distance*distance);                
        long majority = findMajority(assignedGalaxies);
        if(majority==0)
        {
            System.out.println("NO");
        }
        else
        {
        	long majCount = 0;
        	for(long l : assignedGalaxies)
        	{
        		if(l == majority)
        		{
        			majCount++;
        		}
        	}
            System.out.println(majCount);
        }
        */
    }
    
    public static Star newFindMajority(Star[] stars, long d)
    {
        if(stars.length == 0)
        {
            return null;
        }
        else if(stars.length == 1)
        {
            return stars[0];
        }
        else
        {
            Star[] aPrime = newaPrime(stars, d);
            Star x = newFindMajority(aPrime, d);
            if(x==null)
            {
                if(stars.length%2 != 0)
                {
                    long count = newCandCount(stars, stars[stars.length-1], d);
                    if(count > (stars.length/2))
                    {
                        return stars[stars.length-1];
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                long count = newCandCount(stars, x, d);
                if(count > (stars.length/2))
                {
                    return x;
                }
                else
                {
                    return null;
                }
            }
        }
    }
    
    public static long countTheStars(Star majStar, Star[] stars, long d)
    {
    	long starCount = 0;
    	for(Star s : stars)
    	{
    		if(measureDistance(s, majStar) < d)
    		{
    			starCount++;
    		}
    	}
    	
    	return starCount;
    }
    
    public static Star[] newaPrime(Star[] stars, long d)
    {
        ArrayList<Star> aPrime = new ArrayList<Star>();
        for(int i=0; i<stars.length-1; i+=2)
        {
            if(measureDistance(stars[i], stars[i+1]) < d)
            {
                aPrime.add(stars[i]);
            }
        }
          
        return aPrime.toArray(new Star[0]);
    }
    
    public static long newCandCount(Star[] stars, Star cand, long d)
    {
        long count = 0;
        for(Star s : stars)
        {
            if(measureDistance(s, cand)<d)
            {
                count++;
            }
        }
        return count;
    }
    
    public static long findMajority(ArrayList<Long> galaxies)
    {
        if(galaxies.size() == 0)
        {
            return 0;
        }
        else if(galaxies.size() == 1)
        {
            return galaxies.get(0);
        }
        else
        {
            ArrayList<Long> aPrime = aPrime(galaxies);
            long x = findMajority(aPrime);
            if(x==0)
            {
                if(galaxies.size()%2 != 0)
                {
                    long count = candCount(galaxies, galaxies.get(galaxies.size()-1));
                    if(count > (galaxies.size()/2))
                    {
                        return galaxies.get(galaxies.size()-1);
                    }
                    else
                    {
                        return 0;
                    }
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                long count = candCount(galaxies, x);
                if(count > (galaxies.size()/2))
                {
                    return x;
                }
                else
                {
                    return 0;
                }
            }
        }
    }
    
    public static long candCount(ArrayList<Long> numbers, long cand)
    {
        long count = 0;
        for(long l : numbers)
        {
            if(l == cand)
            {
                count++;
            }
        }
        return count;
    }
    
    public static ArrayList<Long> aPrime(ArrayList<Long> numbers)
    {
        ArrayList<Long> aPrime = new ArrayList<Long>();
        for(int i=0; i<numbers.size()-1; i+=2)
        {
            if(numbers.get(i) == numbers.get(i+1))
            {
                aPrime.add(numbers.get(i));
            }
        }
        return aPrime;
    }
    
    public static ArrayList<Long> assignGalaxies(Star[] stars, long d)
    {
        ArrayList<Star> uniques = new ArrayList<Star>();
        ArrayList<Long> galaxies = new ArrayList<Long>(stars.length);
        
        long newGalaxyId = 1;
        for(Star s : stars)
        {
            if(uniques.isEmpty())
            {
                s.galaxy=newGalaxyId;
                uniques.add(s);
                galaxies.add(newGalaxyId);
            }
            else
            {
                int i=0;
                boolean found = false;
                while(i<uniques.size() && !found)
                {
                    if(measureDistance(s, uniques.get(i)) <= d)
                    {
                        galaxies.add(uniques.get(i).galaxy);
                        found=true;
                    }   
                    i++;
                }
                
                if(!found)
                {
                    newGalaxyId++;
                    s.galaxy=newGalaxyId;
                    galaxies.add(newGalaxyId);
                    uniques.add(s);
                }
            }
        }
        return galaxies;
    }
    
    public static long measureDistance(Star s1, Star s2)
    {
    	/*
    	long xterm = s2.xpos-s1.xpos;
    	if(xterm<0)
    	{
    		xterm = xterm * -1;
    	}
    	long yterm = s2.ypos-s1.ypos;
    	if(yterm<0)
    	{
    		yterm = xterm * -1;
    	}
    	
    	if(xterm>yterm)
    	{
    		return xterm;
    	}
    	else
    	{
    		return yterm;
    	}
    	*/
    	
    	
        long xterm = (s2.xpos - s1.xpos) * (s2.xpos - s1.xpos);
        long yterm = (s2.ypos - s1.ypos) * (s2.ypos - s1.ypos);
        return xterm + yterm;
        
    }
    
    public static class Star
    {
        public long xpos;
        public long ypos;
        public long galaxy;
        public Star(long x, long y)
        {
            xpos=x;
            ypos=y;
            galaxy=0;
        }
    }
}
