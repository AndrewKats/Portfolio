//name:William Garnes
//uid:u0922152

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;

namespace FormulaEvaluator
{
    /// <summary>
    /// Evaluator Class is used to solve equations entered as a string
    /// </summary>
    public static class Evaluator
    {
        /// <summary>
        /// delegate used to look up variables entered into equation
        /// </summary>
        /// <param name="variable"> variable to be looked up which has some value</param>
        /// <returns>a value for the sent variable</returns>
        public delegate int Lookup(String variable);

        /// <summary>
        /// evaluates arithmetic expressions written using standard infix notation
        /// </summary>
        /// <param name="expression">string to be evaluated</param>
        /// <param name="variableEvaluator">function which should return value of variables sent with expression</param>
        /// <returns>an int of the solved equation</returns>
        public static int Evaluate(String expression, Lookup variableEvaluator)
        {
            //array of sent expression
            string[] equation = expression.CreateArray();

            //stacks that will hold values and operators
            var values = new Stack<double>();
            var operators = new Stack<string>();

            //equation solved
            SolveEquation(equation, variableEvaluator, values, operators);

            //makes sure that operator stack is empty and value stack has only one value inside of it
            if (values.Count != 1)
            {
                if (values.Count == 0)
                    throw new ArgumentException("no values entered");
                throw new ArgumentException((values.Count - 1) + " value(s) exist without operators");
            }
            if (operators.Count > 0)
                throw new ArgumentException((operators.Count) + " operator(s) performed without two values");

            return (int)values.Pop();
        }

        /// <summary>
        /// takes an array of strings and then solves and solves the equation in the array
        /// </summary>
        /// <param name="equation">array of strings which stores equation</param>
        /// <param name="variableEvaluator">function which should return value of variables sent with expression</param>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void SolveEquation(string[] equation, Lookup variableEvaluator, Stack<double> values, Stack<string> operators)
        {
            //used to store values that are sent from the equation
            double value;

            //loops through entire list and performs the proper instructions
            foreach (string t in equation)
            {
                if (t.Equals("*") || t.Equals("/") || t.Equals("("))
                    operators.Push(t);

                else if (t.Equals("+") || t.Equals("-"))
                {
                    PlusAndMinusOperation(values, operators);
                    operators.Push(t);
                }

                else if (t.Equals(")"))
                    RightParenthesisOperation(values, operators);

                else if (double.TryParse(t, out value))
                {
                    values.Push(value);
                    MultiplcationAndDivsionOperation(values, operators);
                }

                else
                {
                    if (!Regex.IsMatch(t, @"^[a-zA-Z]+[0-9]+$"))
                        throw new ArgumentException(t + " is an illegal variable name");
                    value = (double)variableEvaluator(t);
                    values.Push(value);
                    MultiplcationAndDivsionOperation(values, operators);
                }
            }
            PlusAndMinusOperation(values, operators);
        }

        /// <summary>
        /// used to perform addition and subtraction operations
        /// </summary>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void PlusAndMinusOperation(Stack<double> values, Stack<string> operators)
        {
            if (operators.Count > 0 && (operators.Peek().Equals("+") || operators.Peek().Equals("-")))
                Operate(operators.Pop(), values, operators);
        }

        /// <summary>
        /// used to perform multiplication and division operations
        /// </summary>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void MultiplcationAndDivsionOperation(Stack<double> values, Stack<string> operators)
        {
            if (operators.Count > 0 && (operators.Peek().Equals("*") || operators.Peek().Equals("/")))
                Operate(operators.Pop(), values, operators);
        }

        /// <summary>
        /// performs an opperation based on the opperator sent
        /// </summary>
        /// <param name="operate">operation with be performed</param>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void Operate(string operate, Stack<double> values, Stack<string> operators)
        {
            //used to store the value from the solved equation 
            double value;
            if (values.Count < 2)
                throw new ArgumentException(operate + " operator perfomed without two values");

            //the number which came second in the operation being performed
            double number2 = values.Pop();

            //the number which came first in the operation being performed
            double number1 = values.Pop();

            //checks the operator and then performs proper opperation
            //if all fail division is performed
            switch (operate)
            {
                case "+":
                    value = number1 + number2;
                    break;
                case "-":
                    value = number1 - number2;
                    break;
                case "*":
                    value = number1 * number2;
                    break;
                default:
                    if (number2 == 0)
                        throw new ArgumentException(number1 + "/" + number2 + " is division by 0");
                    value = number1 / number2;
                    break;
            }
            //placed value from opperation onto the values stack
            values.Push(value);
        }

        /// <summary>
        /// performs the math on the inside of Parenthesis and deletes left Parenthesis
        /// from the opperators stack
        /// </summary>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void RightParenthesisOperation(Stack<double> values, Stack<string> operators)
        {
            PlusAndMinusOperation(values, operators);
            if (operators.Count == 0 || !operators.Pop().Equals("("))
                throw new ArgumentException("Parenthesis missplacement");
            MultiplcationAndDivsionOperation(values, operators);
        }

        /// <summary>
        /// takes a string and returns an array of the string
        /// </summary>
        /// <param name="equation">string to be split and placed into array</param>
        /// <returns>array of sent string</returns>
        private static string[] CreateArray(this string equation)
        {
            //remove all whitespaces and then place string into an array
            //formula found at http://stackoverflow.com/questions/6219454/efficient-way-to-remove-all-whitespace-from-string
            equation = new string(equation.ToCharArray().Where(c => (!Char.IsWhiteSpace(c) && !c.Equals('\0'))).ToArray());
            string[] substrings = Regex.Split(equation, "(\\()|(\\))|(-)|(\\+)|(\\*)|(/)");
            //take all empty strings in array and delete them
            // formula found at http://stackoverflow.com/questions/496896/how-to-delete-an-element-from-an-array-in-c-sharp
            substrings = substrings.Where(val => val != string.Empty).ToArray();
            return substrings;
        }
    }
}