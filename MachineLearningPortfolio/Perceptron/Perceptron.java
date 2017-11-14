package machinelearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Andrew Katsanevas
 * CS 6350
 * 9/26/2017
 */
public class Perceptron
{
	public static void main(String[] args) throws Exception
	{
		
		/*
		 *  PART 1
		 *  Cross Validation
		 */
		
		System.out.println("======================================================");
		System.out.println("CROSS VALIDATION");
		System.out.println("======================================================");
		
		ArrayList<Example> examples = readExamples("phishing.train");
		
		double[] parameters = {1, 0.1, 0.01};
		
		Result result = new Result();
		double accuracy = -1;;
		
		// SIMPLE
		double bestSimpleAccuracy = 0;
		double bestSimpleRate = 0;
		for(int r=0; r<3; r++)
		{
			double totalAccuracy = 0;
			double total = 0;
			for(int c=0; c<5; c++)
			{
				result = new Result();
				examples = readExamplesCV(c);
				for(int e=0; e<10; e++)
				{
					result = simplePerceptron(result.w, result.b, parameters[r], examples, result.t);
					accuracy = testCV(result.w, result.b, c);
				}
				totalAccuracy += accuracy;
				total++;
			}
			double averageAccuracy = totalAccuracy/total;
			
			if(averageAccuracy > bestSimpleAccuracy)
			{
				bestSimpleAccuracy = averageAccuracy;
				bestSimpleRate = parameters[r];
			}
			
			System.out.println("Simple (rate: " + parameters[r] + "): " + averageAccuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Best Simple accuracy: " + bestSimpleAccuracy + "% (rate: " + bestSimpleRate + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		
		// DYNAMIC
		double bestDynamicAccuracy = 0;
		double bestDynamicRate = 0;
		for(int r=0; r<3; r++)
		{
			double totalAccuracy = 0;
			double total = 0;
			for(int c=0; c<5; c++)
			{
				result = new Result();
				examples = readExamplesCV(c);
				for(int e=0; e<10; e++)
				{
					result = dynamicPerceptron(result.w, result.b, parameters[r], examples, result.t);
					accuracy = testCV(result.w, result.b, c);
				}
				totalAccuracy += accuracy;
				total++;
			}
			double averageAccuracy = totalAccuracy/total;
			
			if(averageAccuracy > bestDynamicAccuracy)
			{
				bestDynamicAccuracy = averageAccuracy;
				bestDynamicRate = parameters[r];
			}
			
			System.out.println("Dynamic (rate: " + parameters[r] + "): " + averageAccuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Best Dynamic accuracy: " + bestDynamicAccuracy + "% (rate: " + bestDynamicRate + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
				
		// MARGIN
		double bestMarginAccuracy = 0;
		double bestMarginRate = 0;
		double bestMarginMargin = 0;
		for(int r=0; r<3; r++)
		{
			for(int m=0; m<3; m++)
			{
				double totalAccuracy = 0;
				double total = 0;
				for(int c=0; c<5; c++)
				{
					result = new Result();
					examples = readExamplesCV(c);
					for(int e=0; e<10; e++)
					{
						result = marginPerceptron(result.w, result.b, parameters[r], examples, result.t, m);
						accuracy = testCV(result.w, result.b, c);
					}
					totalAccuracy += accuracy;
					total++;
				}
				double averageAccuracy = totalAccuracy/total;
				
				if(averageAccuracy > bestMarginAccuracy)
				{
					bestMarginAccuracy = averageAccuracy;
					bestMarginRate = parameters[r];
					bestMarginMargin = parameters[m];
				}
				
				System.out.println("Margin (rate: " + parameters[r] + ", margin: " + parameters[m] + "): " + averageAccuracy + "%");
			}
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Best Margin accuracy: " + bestMarginAccuracy + "% (rate: " + bestMarginRate + ", margin: " + bestMarginMargin + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		
		// AVERAGED
		double bestAveragedAccuracy = 0;
		double bestAveragedRate = 0;
		for(int r=0; r<3; r++)
		{
			double totalAccuracy = 0;
			double total = 0;
			for(int c=0; c<5; c++)
			{
				result = new Result();
				examples = readExamplesCV(c);
				for(int e=0; e<10; e++)
				{
					result = averagedPerceptron(result.w, result.w.clone(), result.b, result.ba, parameters[r], examples, result.t);
					accuracy = testCV(result.a, result.ba, c);
				}
				totalAccuracy += accuracy;
				total++;
			}
			double averageAccuracy = totalAccuracy/total;
			
			if(averageAccuracy > bestAveragedAccuracy)
			{
				bestAveragedAccuracy = averageAccuracy;
				bestAveragedRate = parameters[r];
			}
			
			System.out.println("Averaged (rate: " + parameters[r] + "): " + averageAccuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Best Averaged accuracy: " + bestAveragedAccuracy + "% (rate: " + bestAveragedRate + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
	
		// AGGRESSIVE
		double bestAggressiveAccuracy = 0;
		double bestAggressiveRate = 0;
		double bestAggressiveMargin = 0;
		for(int r=0; r<3; r++)
		{
			for(int m=0; m<3; m++)
			{
				double totalAccuracy = 0;
				double total = 0;
				for(int c=0; c<5; c++)
				{
					result = new Result();
					examples = readExamplesCV(c);
					for(int e=0; e<10; e++)
					{
						result = aggressivePerceptron(result.w, result.b, parameters[r], examples, result.t, m);
						accuracy = testCV(result.w, result.b, c);
					}
					totalAccuracy += accuracy;
					total++;
				}
				double averageAccuracy = totalAccuracy/total;
				
				if(averageAccuracy > bestAggressiveAccuracy)
				{
					bestAggressiveAccuracy = averageAccuracy;
					bestAggressiveRate = parameters[r];
					bestAggressiveMargin = parameters[m];
				}
				
				System.out.println("Aggressive (rate: " + parameters[r] + ", margin: " + parameters[m] + "): " + averageAccuracy + "%");
			}
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Best Aggressive accuracy: " + bestAggressiveAccuracy + "% (rate: " + bestAggressiveRate + ", margin: " + bestAggressiveMargin + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		
		
		
		/*
		 * PART 2
		 */
		
		System.out.println("======================================================");
		System.out.println("DEVELOPMENT");
		System.out.println("======================================================");
		
		ArrayList<Example> devExamples = readExamples("phishing.dev");
		examples = readExamples("phishing.train");
		
		double bestOverallAccuracy = 0;
		String bestOverallVariant = "none";
		
		
		// SIMPLE
		result = new Result();
		int bestSimpleEpoch = -1;
		double bestSimpleEpochAccuracy = 0;
		for(int e=0; e<20; e++)
		{
			result = simplePerceptron(result.w, result.b, bestSimpleRate, examples, result.t);
			accuracy = test(result.w, result.b, devExamples);
			
			int epoch = e+1;
			
			if(accuracy >= bestSimpleEpochAccuracy)
			{
				bestSimpleEpoch = epoch;
				bestSimpleEpochAccuracy = accuracy;
			}
			
			System.out.println("Simple Epoch " + epoch + ": " + accuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Updates performed: " + result.t);
		System.out.println("Best Simple Epoch: " + bestSimpleEpoch + "(" + bestSimpleEpochAccuracy + "% accurate)");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		if(bestSimpleEpochAccuracy > bestOverallAccuracy)
		{
			bestOverallAccuracy = bestSimpleEpochAccuracy;
			bestOverallVariant = "Simple";
		}
		
		
		// DYNAMIC
		result = new Result();
		int bestDynamicEpoch = -1;
		double bestDynamicEpochAccuracy = 0;
		for(int e=0; e<20; e++)
		{
			result = dynamicPerceptron(result.w, result.b, bestDynamicRate, examples, result.t);
			accuracy = test(result.w, result.b, devExamples);
			
			int epoch = e+1;
			
			if(accuracy >= bestDynamicEpochAccuracy)
			{
				bestDynamicEpoch = epoch;
				bestDynamicEpochAccuracy = accuracy;
			}
			
			System.out.println("Dynamic Epoch " + epoch + ": " + accuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Updates performed: " + result.t);
		System.out.println("Best Dynamic Epoch: " + bestDynamicEpoch + "(" + bestDynamicEpochAccuracy + "% accurate)");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		if(bestDynamicEpochAccuracy > bestOverallAccuracy)
		{
			bestOverallAccuracy = bestDynamicEpochAccuracy;
			bestOverallVariant = "Dynamic";
		}
		
		
		// MARGIN
		result = new Result();
		int bestMarginEpoch = -1;
		double bestMarginEpochAccuracy = 0;
		for(int e=0; e<20; e++)
		{
			result = marginPerceptron(result.w, result.b, bestMarginRate, examples, result.t, bestMarginMargin);
			accuracy = test(result.w, result.b, devExamples);
			
			int epoch = e+1;
			
			if(accuracy >= bestMarginEpochAccuracy)
			{
				bestMarginEpoch = epoch;
				bestMarginEpochAccuracy = accuracy;
			}
			
			System.out.println("Margin Epoch " + epoch + ": " + accuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Updates performed: " + result.t);
		System.out.println("Best Margin Epoch: " + bestMarginEpoch + "(" + bestMarginEpochAccuracy + "% accurate)");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		if(bestMarginEpochAccuracy > bestOverallAccuracy)
		{
			bestOverallAccuracy = bestMarginEpochAccuracy;
			bestOverallVariant = "Margin";
		}
		
		
		// AVERAGED
		result = new Result();
		int bestAveragedEpoch = -1;
		double bestAveragedEpochAccuracy = 0;
		for(int e=0; e<20; e++)
		{
			result = averagedPerceptron(result.w, result.w.clone(), result.b, result.ba, bestAveragedRate, examples, result.t);
			accuracy = test(result.a, result.ba, devExamples);
			
			int epoch = e+1;
			
			if(accuracy >= bestAveragedEpochAccuracy)
			{
				bestAveragedEpoch = epoch;
				bestAveragedEpochAccuracy = accuracy;
			}
			
			System.out.println("Averaged Epoch " + epoch + ": " + accuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Updates performed: " + result.t);
		System.out.println("Best Averaged Epoch: " + bestAveragedEpoch + "(" + bestAveragedEpochAccuracy + "% accurate)");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		if(bestAveragedEpochAccuracy > bestOverallAccuracy)
		{
			bestOverallAccuracy = bestAveragedEpochAccuracy;
			bestOverallVariant = "Averaged";
		}
		
		// AGGRESSIVE
		result = new Result();
		int bestAggressiveEpoch = -1;
		double bestAggressiveEpochAccuracy = 0;
		for(int e=0; e<20; e++)
		{
			result = aggressivePerceptron(result.w, result.b, bestAggressiveRate, examples, result.t, bestAggressiveMargin);
			accuracy = test(result.w, result.b, devExamples);
			
			int epoch = e+1;
			
			if(accuracy >= bestAggressiveEpochAccuracy)
			{
				bestAggressiveEpoch = epoch;
				bestAggressiveEpochAccuracy = accuracy;
			}
			
			System.out.println("Aggressive Epoch " + epoch + ": " + accuracy + "%");
		}
		System.out.println("------------------------------------------------------");
		System.out.println("Updates performed: " + result.t);
		System.out.println("Best Aggressive Epoch: " + bestAggressiveEpoch + "(" + bestAggressiveEpochAccuracy + "% accurate)");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		if(bestAggressiveEpochAccuracy > bestOverallAccuracy)
		{
			bestOverallAccuracy = bestAggressiveEpochAccuracy;
			bestOverallVariant = "Aggressive";
		}
		
		System.out.println("------------------------------------------------------");
		System.out.println("Best Overall: " + bestOverallVariant + "(" + bestOverallAccuracy + ")");
		System.out.println("------------------------------------------------------");
		System.out.println();
		
		
		/*
		 * PART 3
		 * Test
		 */
		
		System.out.println("======================================================");
		System.out.println("TEST");
		System.out.println("======================================================");

		ArrayList<Example> testExamples = readExamples("phishing.train");
		
		if(bestOverallVariant.equals("Simple"))
		{
			result = new Result();
			for(int e=0; e<bestSimpleEpoch; e++)
			{
				result = simplePerceptron(result.w, result.b, bestSimpleRate, examples, result.t);
			}
			accuracy = test(result.w, result.b, testExamples);
			System.out.println("Best test");
			System.out.println("Simple with rate: " + bestSimpleRate + " for " + bestSimpleEpoch + " epochs");
			System.out.println("Accuracy: " + accuracy + "%");
		}
		else if(bestOverallVariant.equals("Dynamic"))
		{
			result = new Result();
			for(int e=0; e<bestDynamicEpoch; e++)
			{
				result = dynamicPerceptron(result.w, result.b, bestDynamicRate, examples, result.t);
			}
			accuracy = test(result.w, result.b, testExamples);
			System.out.println("Best Test");
			System.out.println("Dynamic with rate: " + bestDynamicRate + " for " + bestDynamicEpoch + " epochs");
			System.out.println("Accuracy: " + accuracy + "%");
		}
		else if(bestOverallVariant.equals("Margin"))
		{
			result = new Result();
			for(int e=0; e<bestMarginEpoch; e++)
			{
				result = marginPerceptron(result.w, result.b, bestMarginRate, examples, result.t, bestMarginMargin);
			}
			accuracy = test(result.w, result.b, testExamples);
			System.out.println("Best Test");
			System.out.println("Margin with rate: " + bestMarginRate + " and margin: " + bestMarginMargin + " for " + bestMarginEpoch + "epochs");
			System.out.println("Accuracy: " + accuracy + "%");
		}
		else if(bestOverallVariant.equals("Averaged"))
		{
			result = new Result();
			for(int e=0; e<bestAveragedEpoch; e++)
			{
				result = averagedPerceptron(result.w, result.w.clone(), result.b, result.ba, bestAveragedRate, examples, result.t);
			}
			accuracy = test(result.a, result.ba, testExamples);
			System.out.println("Best Test");
			System.out.println("Averaged with rate: " + bestAveragedRate + " for " + bestAveragedEpoch + " epochs");
			System.out.println("Accuracy: " + accuracy + "%");
		}
		else if(bestOverallVariant.equals("Aggressive"))
		{
			result = new Result();
			for(int e=0; e<bestAggressiveEpoch; e++)
			{
				result = aggressivePerceptron(result.w, result.b, bestAggressiveRate, examples, result.t, bestAggressiveMargin);
			}
			accuracy = test(result.w, result.b, testExamples);
			System.out.println("Best Test");
			System.out.println("Aggressive with rate: " + bestAggressiveRate + " and margin: " + bestAggressiveMargin + " for " + bestAggressiveEpoch + "epochs");
			System.out.println("Accuracy: " + accuracy + "%");
		}
	}
	
	public static Result simplePerceptron(double[] w, double b, double r, ArrayList<Example> examples, int t) throws Exception
	{	
		Collections.shuffle(examples);
		
		int updateCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			if(prediction < 0)
			{		
				updateCount++;
				double[] ryx = matrixTimesConstant(ex.values,  ex.label * r);
				w = matrixAdd(w, ryx);
				b += r * ex.label;
				t++;
			}
		}
		
		return new Result(w, b, t, updateCount);
	}
	
	public static Result dynamicPerceptron(double[] w, double b, double r, ArrayList<Example> examples, int t) throws Exception
	{		
		Collections.shuffle(examples);
		int updateCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			if(prediction < 0)
			{		
				updateCount++;
				double rate = r/(double)(1+t);
				
				double[] ryx = matrixTimesConstant(ex.values,  ex.label * rate);
				w = matrixAdd(w, ryx);
				b += rate * ex.label;
				t++;
			}
		}
		
		return new Result(w, b, t, updateCount);
	}
	
	public static Result marginPerceptron(double[] w, double b, double r, ArrayList<Example> examples, int t, double margin) throws Exception
	{		
		Collections.shuffle(examples);
		int updateCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			if(prediction < margin)
			{			
				updateCount++;
				double rate = r/(double)(1+t);
				
				double[] ryx = matrixTimesConstant(ex.values,  ex.label * rate);
				w = matrixAdd(w, ryx);
				b += rate * ex.label;
				t++;
			}
		}
		
		return new Result(w, b, t, updateCount);
	}
	
	public static Result averagedPerceptron(double[] w, double[] a, double b, double ba, double r, ArrayList<Example> examples, int t) throws Exception
	{		
		Collections.shuffle(examples);
		int updateCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			if(prediction < 0)
			{				
				updateCount++;
				double[] ryx = matrixTimesConstant(ex.values,  ex.label * r);
				w = matrixAdd(w, ryx);
				b += r * ex.label;
				t++;
			}
			a = matrixAdd(a,w);
			ba += b;
		}
		
		return new Result(w, a, b, ba, t, updateCount);
	}
	
	public static Result aggressivePerceptron(double[] w, double b, double r, ArrayList<Example> examples, int t, double margin) throws Exception
	{
		Collections.shuffle(examples);
		int updateCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			if(prediction <= margin)
			{		
				updateCount++;
				double rate = (margin - (ex.label*dotProduct(w,ex.values))) / (dotProduct(ex.values,ex.values)+1.0);
				
				double[] ryx = matrixTimesConstant(ex.values,  ex.label * rate);
				w = matrixAdd(w, ryx);
				b += rate * ex.label;
				t++;
			}
		}
		
		return new Result(w, b, t, updateCount);
	}
	
	public static double test(double[] w, double b, String filename) throws IOException
	{
		ArrayList<Example> examples = readExamples(filename);
		
		double wrongCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			
			if(prediction < 0)
			{
				wrongCount++;
			}
		}
		
		double total = examples.size();
		
		return 100.0 * (1.0 - (wrongCount/total));
	}
	
	public static double test(double[] w, double b, ArrayList<Example> examples) throws IOException
	{		
		double wrongCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			
			if(prediction < 0)
			{
				wrongCount++;
			}
		}
		
		double total = examples.size();
		
		return 100.0 * (1.0 - (wrongCount/total));
	}
	
	public static double testCV(double[] w, double b, int test) throws IOException
	{
		ArrayList<Example> examples = readExamples("training0" + test + ".data");
		
		double wrongCount = 0;
		for(Example ex : examples)
		{
			double prediction = ex.label * (dotProduct(ex.values, w) + b);
			
			if(prediction < 0)
			{
				wrongCount++;
			}
		}
		
		double total = examples.size();
		
		return 100.0 * (1.0 - (wrongCount/total));
	}
	
	public static double[] matrixTimesConstant(double[] matrix, double c)
	{
		double[] result = Arrays.copyOf(matrix, matrix.length);
		
		for(int i=0; i<matrix.length; i++)
		{
			result[i] *= c;
		}
		
		return result;
	}
	
	public static double[] matrixAdd(double[] m1, double[] m2) throws Exception
	{
		if(m1.length != m2.length)
		{
			throw new Exception("Matrix sizes don't match");
		}
		
		double[] result = new double[m1.length];
		
		for(int i=0; i<m1.length; i++)
		{
			result[i] = m1[i] + m2[i];
		}
		
		return result;
	}
	
	public static double dotProduct(double[] x, double[] w)
	{
		double sum = 0;
		for(int i=0; i<70; i++)
		{
			sum += x[i] * w[i];
		}
		
		return sum;
	}
	
	public static ArrayList<Example> readExamples(String filename) throws IOException
	{
		// Read file
		ArrayList<Example> examples = new ArrayList<Example>();
		BufferedReader exampleReader = new BufferedReader(new FileReader(filename));
		String line;
		while((line=exampleReader.readLine()) != null)
		{
			Example nextExample = makeExample(line);
			examples.add(nextExample);
		}
		exampleReader.close();

		return examples;
	}
	
	public static ArrayList<Example> readExamplesCV(int test) throws IOException
	{
		// Read file
		ArrayList<Example> examples = new ArrayList<Example>();
		
		for(int i=0; i<5; i++)
		{
			if(i != test)
			{
				BufferedReader exampleReader = new BufferedReader(new FileReader("training0" + i + ".data"));
				String line;
				while((line=exampleReader.readLine()) != null)
				{
					Example nextExample = makeExample(line);
					examples.add(nextExample);
				}
				exampleReader.close();
			}
		}

		return examples;
	}
	
	public static Example makeExample(String line)
	{
		String[] split = line.split(" ");
		
		Example example = new Example();
		
		if(line.charAt(0) == '-')
		{
			example.label = -1.0;
		}
		else
		{
			example.label = 1.0;
		}
		
		String current;
		for(int i=1; i<split.length; i++)
		{
			current = split[i];
			String[] splitCurrent = current.split(":");
			String indexString = splitCurrent[0];
			int nextIndex = Integer.parseInt(indexString);
			String valueString = splitCurrent[1];
			double nextValue = Double.parseDouble(valueString);
			example.values[nextIndex] = nextValue;
		}
		
		return example;
	}
}

class Example
{
	double label;
	double[] values;
	
	public Example(int theLabel, double[] theValues)
	{
		label = theLabel;
		values = theValues;
	}
	
	public Example()
	{
		label = -2;
		values = new double[70];
		Arrays.fill(values, 0.0);
	}
}

class Result
{
	double[] w;
	double[] a;
	double b;
	double ba;
	int t;
	int updateCount;
	
	public Result(double[] theW, double theB, int theT, int theUpdateCount)
	{
		w = theW;
		b = theB;
		t= theT;
		updateCount = theUpdateCount;
		
		a = null;
		ba = -1;
	}
	
	public Result(double[] theW, double[] theA, double theB, double theBa, int theT, int theUpdateCount)
	{
		w = theW;
		a = theA;
		b = theB;
		ba = theBa;
		t= theT;
		updateCount = theUpdateCount;
	}
	
	public Result()
	{		
		w = new double[70];
		for(int i=0; i<w.length; i++)
		{
			w[i] = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
		}
		
		a = new double[70];
		for(int i=0; i<a.length; i++)
		{
			a[i] = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
		}

		b = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
		ba = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
		t = 0;
		updateCount = 0;
	}
}
