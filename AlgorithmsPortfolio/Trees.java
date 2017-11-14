/*
 * Andrew Katsanevas
 * CS 4150
 * 9/1/2016
 */

package trees;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Trees
{
	public static void main(String[] args) throws IOException
	{
		// Read in input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String seedlingString = br.readLine();
		String treesString = br.readLine();
		br.close();
		
		
		String[] treesArray = treesString.split(" ");
		int seedlings = Integer.parseInt(seedlingString);
		
		ArrayList<Integer> trees = new ArrayList<Integer>(seedlings);
		for(String s : treesArray)
		{
			trees.add(Integer.parseInt(s));
		}
		
		// Use the algorithm
		int day = treePartyDay(trees);
		
		// Output the solution
		System.out.print(day);
	}
	
	public static int treePartyDay(ArrayList<Integer> trees)
	{
		// Sort the tree day values in descending order
		Collections.sort(trees);
		Collections.reverse(trees);
		
		// Add each tree day value's index to it
		int index = 0;	
		while(index<trees.size())
		{
			trees.set(index, trees.get(index)+index);
			index++;
		}
		
		// Find the max of these modified tree day values
		int max = trees.get(0);
		for(int i : trees)
		{
			if(i > max)
			{
				max = i;
			}
		}
		
		// The max tree day value plus two is the day of the party
		return max+2;			
	}
}
