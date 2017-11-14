package cs4150;

/*
 * Andrew Katsanevas
 * CS4150
 * Ceiling Function
 * 9/8/2016
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Ceiling 
{
	public static void main(String[] args) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String firstLineString = br.readLine();
        String[] firstLineArray = firstLineString.split(" ");
        int treeAmount = Integer.parseInt(firstLineArray[0]);
        int treeSize = Integer.parseInt(firstLineArray[1]);
        
        BST[] trees = new BST[treeAmount];
        
        for(int i=0; i<treeAmount; i++)
        {
        	String treeString = br.readLine();
        	String[] treeArray = treeString.split(" ");
        	int[] treeIntArray = new int[treeSize];
        	for(int j=0; j<treeSize; j++)
        	{
        		treeIntArray[j] = Integer.parseInt(treeArray[j]);
        	}
        	
        	trees[i] = new BST(treeIntArray);
        }
        
        br.close();
        
        System.out.println(amountShapes(trees));
	}
	
	public static int amountShapes(BST[] trees)
	{
		ArrayList<BST> unique = new ArrayList<BST>();
		
		for(BST tree : trees)
		{
			if(unique.size() == 0)
			{
				unique.add(tree);
			}
			else
			{
				boolean addIt = true;
				for(BST uTree : unique)
				{
					if(sameShape(tree.root, uTree.root))
					{
						addIt = false;
					}
				}
				if(addIt)
				{
					unique.add(tree);
				}
			}	
		}
		
		
		return unique.size();
	}
	
	public static boolean sameShape(Node first, Node second)
	{
		if(first==null && second==null)
		{
			//System.out.println("Here1");
			return true;	
		}
		else if(!(first!=null && second!=null))
		{
			//System.out.println("Here2");
			return false;	
		}
		else
		{
			//System.out.println("Here3");
			//System.out.println("First: " + first.value);
			//System.out.println("Second: " + second.value);
			return (sameShape(first.left, second.left) && sameShape(first.right, second.right));
		}
		
		
		
	}
}

class BST
{
	public Node root;
	
	public BST(int[] values)
	{
		root = new Node(values[0]);
		for(int i=1; i<values.length; i++)
		{
			insertBST(values[i], root);
		}
	}
	
	
	public Node insertBST(int value, Node root)
	{
		if(root==null)
		{
			root = new Node(value);
			return root;
		}
		
		if(value < root.value)
		{
			root.left = insertBST(value, root.left);
		}
		if(value > root.value)
		{
			root.right = insertBST(value, root.right);
		}
		return root;
		
	}
}

class Node
{
	int value;
	Node left;
	Node right;
	
	public Node(int value)
	{
		left = null;
		right = null;
		this.value = value;
	}
}