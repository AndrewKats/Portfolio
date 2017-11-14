import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Andrew Katsanevas
public class DecisionTree 
{
	public static int featureCount = 25;
	
	public static void main(String[] args) throws IOException
	{	
		Node tree = train("updated_train.txt", featureCount);
		double accuracy = test(tree, "updated_test.txt", featureCount);
		System.out.println(accuracy + "% accurate on test with no depth restriction");
		System.out.println();
		
		System.out.println("CVS:");
		System.out.println("====================================================");
		ArrayList<Double> depthAverages = new ArrayList<Double>();
		for(int i = 0; i <= featureCount; i++)
		{
			int newDepth = i;
			double total = 0;
			
			tree = tripleTrain("updated_training00.txt", "updated_training01.txt", "updated_training02.txt", newDepth);
			double accuracy1 = test(tree, "updated_training03.txt", newDepth);
			total += accuracy1;
			//System.out.println(accuracy1 + "% accurate on train: 012 test: 3 with depth " + newDepth);
			
			tree = tripleTrain("updated_training00.txt", "updated_training01.txt", "updated_training03.txt", newDepth);
			double accuracy2 = test(tree, "updated_training02.txt", newDepth);
			total += accuracy2;
			//System.out.println(accuracy2 + "% accurate on train: 013 test: 2 with depth " + newDepth);
			
			tree = tripleTrain("updated_training00.txt", "updated_training02.txt", "updated_training03.txt", newDepth);
			double accuracy3 = test(tree, "updated_training01.txt", newDepth);
			total += accuracy3;
			//System.out.println(accuracy3 + "% accurate on train: 023 test: 1 with depth " + newDepth);
			
			tree = tripleTrain("updated_training01.txt", "updated_training02.txt", "updated_training03.txt", newDepth);
			double accuracy4 = test(tree, "updated_training00.txt", newDepth);
			total += accuracy4;
			//System.out.println(accuracy4 + "% accurate on train: 123 test: 0 with depth " + newDepth);
			
			double average = total/4;
			depthAverages.add(average);
		}
		
		double maxAccuracy = 0;
		int bestDepth = -1;
		for(int i = 0; i <= featureCount; i++)
		{
			if(depthAverages.get(i) > maxAccuracy)
			{
				maxAccuracy = depthAverages.get(i);
				bestDepth = i;
			}
			System.out.println("Average accuracy of depth " + i + " is " + depthAverages.get(i) + "%");
		}
		System.out.println();
		System.out.println("Best Depth: " + bestDepth + " with " + maxAccuracy + "% accuracy");
		
		tree = train("updated_train.txt", 4);
		double finalAccuracy = test(tree, "updated_test.txt", 4);
		System.out.println("Using this best depth of " + bestDepth + " to train the tree, I am " + finalAccuracy + "% accurate on updated_test.txt");
	}
	
	public static double test(Node tree, String filename, int depth) throws IOException
	{
		// Run experiment
		BufferedReader testReader = new BufferedReader(new FileReader(filename));
		String testLine;
		int testTotal = 0;
		int accurateTotal = 0;
		while((testLine=testReader.readLine()) != null)
		{
			Row nextRow = makeRow(testLine);
			
			testTotal++;
			accurateTotal += testIt(nextRow, tree);
		}
		testReader.close();
		
		double accuracy = ((double)accurateTotal)/((double)testTotal);
		double percent = accuracy*100;
		//BigDecimal bd = new BigDecimal(percent);
		//return bd.round(new MathContext(6));	
		
		return percent;
	}
	
	public static Node train(String filename, int depth) throws IOException
	{
		// Read file
		ArrayList<Row> table = new ArrayList<Row>();
		BufferedReader trainReader = new BufferedReader(new FileReader(filename));
		String line;
		while((line=trainReader.readLine()) != null)
		{
			Row nextRow = makeRow(line);
			table.add(nextRow);
		}
		trainReader.close();

		ArrayList<Boolean> noneUsed = new ArrayList<Boolean>(featureCount);
		for(int i = 0; i < featureCount; i++)
		{
			noneUsed.add(false);
		}
		
		// Create tree
		return ID3(table, noneUsed, depth);
	}
	
	public static Node tripleTrain(String filename1, String filename2, String filename3, int depth) throws IOException
	{
		// Read file1
		ArrayList<Row> table = new ArrayList<Row>();
		BufferedReader trainReader1 = new BufferedReader(new FileReader(filename1));
		String line1;
		while((line1=trainReader1.readLine()) != null)
		{
			Row nextRow = makeRow(line1);
			table.add(nextRow);
		}
		trainReader1.close();

		// Read file2
		BufferedReader trainReader2 = new BufferedReader(new FileReader(filename2));
		String line2;
		while((line2=trainReader2.readLine()) != null)
		{
			Row nextRow = makeRow(line2);
			table.add(nextRow);
		}
		trainReader2.close();
		
		// Read file2
		BufferedReader trainReader3 = new BufferedReader(new FileReader(filename3));
		String line3;
		while((line3=trainReader3.readLine()) != null)
		{
			Row nextRow = makeRow(line3);
			table.add(nextRow);
		}
		trainReader3.close();
		
		ArrayList<Boolean> noneUsed = new ArrayList<Boolean>(6);
		for(int i = 0; i < featureCount; i++)
		{
			noneUsed.add(false);
		}
		
		// Create tree
		return ID3(table, noneUsed, depth);
	}
	
	public static int testIt(Row row, Node tree)
	{
		Node current = tree;
		while(true)
		{
			if(current.attribute == -1)
			{
				if(current.plus == row.label)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
			
			if(row.attributes.get(current.attribute))
			{
				current = current.trueNode;
			}
			else
			{
				current = current.falseNode;
			}
		}
	}
	
	public static Node ID3(ArrayList<Row> table, ArrayList<Boolean> used, int depth)
	{
		// BASE CASE (all examples have the same label)
		int plusCount = 0;
		for(Row row : table)
		{
			if(row.label)
			{
				plusCount++;
			}
		}
		if(plusCount == 0)
		{
			// Minus leaf
			return new Node(false, -1);
		}
		if(plusCount == table.size())
		{
			// Plus leaf
			return new Node(true, -1);
		}
		
		// Choose the attribute
		int chosenAttribute = chooseAttribute(table, used);
		// System.out.println("Attribute chosen: " + chosenAttribute);
		
		boolean done = true;
		for(boolean b : used)
		{
			if(!b)
			{
				done = false;
			}
		}
		
		if(done || chosenAttribute == -1 || depth == 0)
		{
			// Find common value of label
			boolean common = false;
			if(plusCount >= (table.size() - plusCount))
			{
				common = true;
			}
			return new Node(common, -1);
		}
				
		// Create the new root
		Node newRoot = new Node(false, chosenAttribute);
			
		// Modify the used
		ArrayList<Boolean> newUsed = new ArrayList<Boolean>(used);
		newUsed.set(chosenAttribute, true);
		
		
		// false case
		ArrayList<Row> falseSubset = new ArrayList<Row>(table);
		for(int i = table.size()-1; i>=0; i--)
		{
			if(falseSubset.get(i).attributes.get(chosenAttribute))
			{
				falseSubset.remove(i);
			}
		}
		
		if(falseSubset.isEmpty())
		{
			// Find common value of label
			boolean common = false;
			if(plusCount >= (table.size() - plusCount))
			{
				common = true;
			}
			newRoot.falseNode = new Node(common, -1);
		}
		else
		{
			newRoot.falseNode = ID3(falseSubset, newUsed, depth-1);
		}
		
		// true case
		ArrayList<Row> trueSubset = new ArrayList<Row>(table);
		for(int i = table.size()-1; i>=0; i--)
		{
			if(!trueSubset.get(i).attributes.get(chosenAttribute))
			{
				trueSubset.remove(i);
			}
		}
		
		if(trueSubset.isEmpty())
		{
			// Find common value of label
			boolean common = false;
			if(plusCount >= (table.size() - plusCount))
			{
				common = true;
			}
			newRoot.trueNode = new Node(common, -1);
		}
		else
		{
			newRoot.trueNode = ID3(trueSubset, newUsed, depth-1);
		}
				
		
		return newRoot;
	}
	
	public static int chooseAttribute(ArrayList<Row> table, ArrayList<Boolean> used)
	{
		ArrayList<Double> gains = new ArrayList<Double>(featureCount);
		for(int i = 0; i < featureCount; i++)
		{
			gains.add(Double.NEGATIVE_INFINITY);
		}
		
		for(int i=0; i<featureCount; i++)
		{
			if(!used.get(i))
			{
				double sCount = 0;
				
				double yesCount = 0;
				double yesPlusCount = 0;
				
				double noCount = 0;
				double noPlusCount = 0;
				
				double rowCount = table.size();
				
				for(Row row : table)
				{
					if(row.attributes.get(i))
					{
						yesCount++;
						if(row.label)
						{
							yesPlusCount++;
							sCount++;
						}
					}
					else
					{
						noCount++;
						if(row.label)
						{
							noPlusCount++;
							sCount++;
						}
					}
				}
				
				double hS = entropy(sCount/rowCount);
				double hY = entropy(yesPlusCount/yesCount);
				double hN = entropy(noPlusCount/noCount);
				
				double newGain = hS - ((yesCount/rowCount) * hY) - ((noCount/rowCount) * hN);
				
				gains.set(i, newGain);
			}
		}
		
		double maxGain = Double.NEGATIVE_INFINITY;
		int maxIndex = -1;
		for(int i = 0; i < featureCount; i++)
		{
			if(gains.get(i) > maxGain)
			{
				maxGain = gains.get(i);
				maxIndex = i;
			}
		}
		
		return maxIndex;	
	}
	
	public static double entropy(double p)
	{
		if(p == 0)
		{
			return 0;
		}
		return (-p * log2(p)) - ((1-p) * log2(1-p));
	}
	
	public static double log2(double n)
	{
		return Math.log(n) / Math.log(2);
	}
	
	public static Row makeRow(String line)
	{
		Row row = new Row();
		
		String label = line.substring(0,1);
		String name = line.substring(2);

		if(label.equals("+"))
		{
			row.label = true;
		}
		else
		{
			row.label = false;
		}
		
		String[] split = name.split(" ");
		
		row.attributes.add(firstNameLongerThanLast(split));
		row.attributes.add(hasMiddleName(split));
		row.attributes.add(firstLastStartWithSameLetter(split));
		row.attributes.add(firstBeforeLastAlphabetically(split));
		row.attributes.add(secondLetterVowel(split));
		row.attributes.add(lastNameEvenLetters(split));
		row.attributes.add(firstLetterVowel(split));
		row.attributes.add(moreThanTwoVowelsInFirstName(split));
		row.attributes.add(firstNameLongerThanFour(split));
		row.attributes.add(lastNameLongerThanFour(split));
		row.attributes.add(moreThanTwoVowelsInLastName(split));
		row.attributes.add(firstNameEvenLetters(split));
		row.attributes.add(firstNameContainsVowel(split));
		row.attributes.add(lastNameContainsVowel(split));
		row.attributes.add(firstNameContainsA(split));
		row.attributes.add(lastNameContainsA(split));
		row.attributes.add(firstNameContainsE(split));
		row.attributes.add(lastNameContainsE(split));
		row.attributes.add(firstBeforeLastAlphabeticallyWithoutFirst(split));
		row.attributes.add(thirdLetterVowel(split));
		row.attributes.add(nameContainsPeriod(split));
		row.attributes.add(secondLetterLastVowel(split));
		row.attributes.add(firstLetterLastVowel(split));
		row.attributes.add(firstNameLastLetterVowel(split));
		row.attributes.add(lastNameLastLetterVowel(split));


		

		return row;
	}
	
	public static boolean firstNameLongerThanLast(String[] split)
	{
		String first = split[0];
		String last = split[split.length-1];
		return first.length() > last.length();
	}
	
	public static boolean hasMiddleName(String[] split)
	{
		return split.length > 2;
	}
	
	public static boolean firstLastStartWithSameLetter(String[] split)
	{
		String first = split[0];
		String last = split[split.length-1];
		String firstFirst = first.substring(0, 1);
		String firstLast = last.substring(0, 1);
		return firstFirst.equals(firstLast);
	}
	
	public static boolean firstBeforeLastAlphabetically(String[] split)
	{
		String firstLower = split[0].toLowerCase();
		String lastLower = split[split.length-1].toLowerCase();
		int compare = firstLower.compareTo(lastLower);
		return compare < 0;
	}
	
	public static boolean secondLetterVowel(String[] split)
	{
		String first = split[0];
		if(first.length() < 2)
		{
			return false;
		}
		String secondLetter = first.substring(1, 2);
		return "AEIOUaeiou".indexOf(secondLetter) != -1;
	}
	
	public static boolean lastNameEvenLetters(String[] split)
	{
		String last = split[split.length-1];
		return (last.length() % 2) == 0;
	}
	
	public static boolean firstLetterVowel(String[] split)
	{
		String first = split[0];
		String firstLetter = first.substring(0, 1);
		return "AEIOUaeiou".indexOf(firstLetter) != -1;
	}
	
	public static boolean moreThanTwoVowelsInFirstName(String[] split)
	{
		String first = split[0];
		int vowelCount = 0;
		for(int i=0; i<first.length(); i++)
		{
			String letter = first.substring(i,i+1);
			if("AEIOUaeiou".indexOf(letter) != -1)
			{
				vowelCount++;
			}
		}
		return vowelCount>2;
	}
	
	public static boolean firstNameLongerThanFour(String[] split)
	{
		String first = split[0];
		return first.length()>3;
	}
	
	public static boolean lastNameLongerThanFour(String[] split)
	{
		String last = split[split.length - 1];
		return last.length()>3;
	}
	
	public static boolean moreThanTwoNames(String[] split)
	{
		return split.length>2;
	}
	
	public static boolean moreThanTwoVowelsInLastName(String[] split)
	{
		String last = split[split.length-1];
		int vowelCount = 0;
		for(int i=0; i<last.length(); i++)
		{
			String letter = last.substring(i,i+1);
			if("AEIOUaeiou".indexOf(letter) != -1)
			{
				vowelCount++;
			}
		}
		return vowelCount>2;
	}
	
	public static boolean firstNameEvenLetters(String[] split)
	{
		String first = split[0];
		return (first.length() % 2) == 0;
	}
	
	public static boolean firstNameContainsVowel(String[] split)
	{
		String first = split[0];
		for(int i=0; i<first.length(); i++)
		{
			String letter = first.substring(i,i+1);
			if("AEIOUaeiou".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean lastNameContainsVowel(String[] split)
	{
		String last = split[split.length-1];
		for(int i=0; i<last.length(); i++)
		{
			String letter = last.substring(i,i+1);
			if("AEIOUaeiou".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean firstNameContainsA(String[] split)
	{
		String first = split[0];
		for(int i=0; i<first.length(); i++)
		{
			String letter = first.substring(i,i+1);
			if("Aa".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean lastNameContainsA(String[] split)
	{
		String last = split[split.length-1];
		for(int i=0; i<last.length(); i++)
		{
			String letter = last.substring(i,i+1);
			if("Aa".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean firstNameContainsE(String[] split)
	{
		String first = split[0];
		for(int i=0; i<first.length(); i++)
		{
			String letter = first.substring(i,i+1);
			if("Ee".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean lastNameContainsE(String[] split)
	{
		String last = split[split.length-1];
		for(int i=0; i<last.length(); i++)
		{
			String letter = last.substring(i,i+1);
			if("Ee".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean firstNameContainsI(String[] split)
	{
		String first = split[0];
		for(int i=0; i<first.length(); i++)
		{
			String letter = first.substring(i,i+1);
			if("Ii".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean lastNameContainsI(String[] split)
	{
		String last = split[split.length-1];
		for(int i=0; i<last.length(); i++)
		{
			String letter = last.substring(i,i+1);
			if("Ii".indexOf(letter) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean firstBeforeLastAlphabeticallyWithoutFirst(String[] split)
	{
		String firstLower = split[0].toLowerCase().substring(1);
		String lastLower = split[split.length-1].toLowerCase().substring(1);
		int compare = firstLower.compareTo(lastLower);
		return compare < 0;
	}
	
	public static boolean thirdLetterVowel(String[] split)
	{
		String first = split[0];
		if(first.length() < 3)
		{
			return false;
		}
		String thirdLetter = first.substring(2, 3);
		return "AEIOUaeiou".indexOf(thirdLetter) != -1;
	}
	
	public static boolean nameContainsPeriod(String[] split)
	{
		for(String s : split)
		{
			if(s.indexOf(".") != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean junior(String[] split)
	{
		for(String s : split)
		{
			if(s.equals("Jr."))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean secondLetterLastVowel(String[] split)
	{
		String last = split[split.length - 1];
		if(last.length() < 2)
		{
			return false;
		}
		String secondLetter = last.substring(1, 2);
		return "AEIOUaeiou".indexOf(secondLetter) != -1;
	}
	
	public static boolean firstLetterLastVowel(String[] split)
	{
		String last = split[split.length-1];
		String firstLetter = last.substring(0, 1);
		return "AEIOUaeiou".indexOf(firstLetter) != -1;
	}
	
	public static boolean firstNameLastLetterVowel(String[] split)
	{
		String first = split[0];
		String lastLetter = first.substring(first.length()-1, first.length());
		return "AEIOUaeiou".indexOf(lastLetter) != -1;
	}
	
	public static boolean lastNameLastLetterVowel(String[] split)
	{
		String last = split[split.length-1];
		String lastLetter = last.substring(last.length()-1, last.length());
		return "AEIOUaeiou".indexOf(lastLetter) != -1;
	}
}

class Row
{
	boolean label;
	
	ArrayList<Boolean> attributes;
	
	public Row(boolean theLabel, ArrayList<Boolean> theAttributes)
	{
		label = theLabel;
		attributes = theAttributes;
	}
	
	public Row()
	{
		label = false;
		attributes = new ArrayList<Boolean>();
	}
	
	// 0 First name longer than last
	// 1 Has middle name
	// 2 First and last name start with the same letter
	// 3 First name comes alphabetically before last
	// 4 Second letter of first name is a vowel
	// 5 Number of letter in last name even
	// 6 First letter is a vowel
	// 7 More than 2 vowels in first name
	// 8 First name longer than 4
	// ...
}

class Node
{
	boolean plus;
	int attribute;
	
	Node falseNode, trueNode;
	
	public Node(boolean isPlus, int theAttribute)
	{
		plus = isPlus;
		attribute = theAttribute;
		
		falseNode = null;
		trueNode = null;
	}
}



