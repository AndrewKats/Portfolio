//Name: Wlliam Garnes
//uid: u0922152
// Skeleton written by Joe Zachary for CS 3500, September 2013
// Read the entire skeleton carefully and completely before you
// do anything else!

// Version 1.1 (9/22/13 11:45 a.m.)

// Change log:
//  (Version 1.1) Repaired mistake in GetTokens
//  (Version 1.1) Changed specification of second constructor to
//                clarify description of how validation works

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace SpreadsheetUtilities
{
    /// <summary>
    /// Represents formulas written in standard infix notation using standard precedence
    /// rules.  The allowed symbols are non-negative numbers written using double-precision 
    /// floating-point syntax; variables that consist of a letter or underscore followed by 
    /// zero or more letters, underscores, or digits; parentheses; and the four operator 
    /// symbols +, -, *, and /.  
    /// 
    /// Spaces are significant only insofar that they delimit tokens.  For example, "xy" is
    /// a single variable, "x y" consists of two variables "x" and y; "x23" is a single variable; 
    /// and "x 23" consists of a variable "x" and a number "23".
    /// 
    /// Associated with every formula are two delegates:  a normalizer and a validator.  The
    /// normalizer is used to convert variables into a canonical form, and the validator is used
    /// to add extra restrictions on the validity of a variable (beyond the standard requirement 
    /// that it consist of a letter or underscore followed by zero or more letters, underscores,
    /// or digits.)  Their use is described in detail in the constructor and method comments.
    /// </summary>
    public class Formula
    {
        /// <summary>
        /// This variable is used to hold the equation once it has been parsed
        /// </summary>
        private string[] equation {get;set;}

        /// <summary>
        /// used to evaluate whether or not a token is a variable or not
        /// </summary>
        /// code for regex found at http://stackoverflow.com/questions/14659287/regex-for-english-characters-hyphen-and-underscore
        private Func<string, bool> IsVariable = token => Regex.IsMatch(token, @"^[a-zA-Z_]+[A-Za-z\d_]*$");

        /// <summary>
        /// delegate that is used to throw errors
        /// </summary>
        /// <param name="token"></param>
        delegate void ErrorCheck(string token);

        /// <summary>
        /// holds all of the variables in the equation array
        /// </summary>
        private string[] variables { get; set; }


        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically invalid,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer is the identity function, and the associated validator
        /// maps every string to true.  
        /// </summary>
        public Formula(String formula) :
            this(formula, s => s, s => true)
        {
        }

        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically incorrect,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer and validator are the second and third parameters,
        /// respectively.  
        /// 
        /// If the formula contains a variable v such that normalize(v) is not a legal variable, 
        /// throws a FormulaFormatException with an explanatory message. 
        /// 
        /// If the formula contains a variable v such that isValid(normalize(v)) is false,
        /// throws a FormulaFormatException with an explanatory message.
        /// 
        /// Suppose that N is a method that converts all the letters in a string to upper case, and
        /// that V is a method that returns true only if a string consists of one letter followed
        /// by one digit.  Then:
        /// 
        /// new Formula("x2+y3", N, V) should succeed
        /// new Formula("x+y3", N, V) should throw an exception, since V(N("x")) is false
        /// new Formula("2x+y3", N, V) should throw an exception, since "2x+y3" is syntactically incorrect.
        /// </summary>
        public Formula(String formula, Func<string, string> normalize, Func<string, bool> isValid)
        {
            if (formula == null||normalize == null||isValid == null)
                throw new ArgumentNullException("none of the inputed parameters can be null");

            //stores the sent IEnumerable into an array called equation
            equation = GetTokens(formula).ToArray<string>();

            CheckIfFormulaIsLegal(normalize, isValid);
        }

        /// <summary>
        /// Evaluates this Formula, using the lookup delegate to determine the values of
        /// variables.  When a variable symbol v needs to be determined, it should be looked up
        /// via lookup(normalize(v)). (Here, normalize is the normalizer that was passed to 
        /// the constructor.)
        /// 
        /// For example, if L("x") is 2, L("X") is 4, and N is a method that converts all the letters 
        /// in a string to upper case:
        /// 
        /// new Formula("x+7", N, s => true).Evaluate(L) is 11
        /// new Formula("x+7").Evaluate(L) is 9
        /// 
        /// Given a variable symbol as its parameter, lookup returns the variable's value 
        /// (if it has one) or throws an ArgumentException (otherwise).
        /// 
        /// If no undefined variables or divisions by zero are encountered when evaluating 
        /// this Formula, the value is returned.  Otherwise, a FormulaError is returned.  
        /// The Reason property of the FormulaError should have a meaningful explanation.
        ///
        /// This method should never throw an exception.
        /// </summary>
        public object Evaluate(Func<string, double> lookup)
        {
            try{return Evaluator.Evaluate(equation, lookup); }
            catch (ArgumentException e)
            {
                if (!e.Message.Contains(" is division by 0"))                
                    return new FormulaError(e.ParamName);
            }
			catch
			{
				return new FormulaError("one or more variable names were not found");
			}
            return new FormulaError("ArgumentException thrown");            
        }

        /// <summary>
        /// Enumerates the normalized versions of all of the variables that occur in this 
        /// formula.  No normalization may appear more than once in the enumeration, even 
        /// if it appears more than once in this Formula.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x+y*z", N, s => true).GetVariables() should enumerate "X", "Y", and "Z"
        /// new Formula("x+X*z", N, s => true).GetVariables() should enumerate "X" and "Z".
        /// new Formula("x+X*z").GetVariables() should enumerate "x", "X", and "z".
        /// </summary>
        public IEnumerable<String> GetVariables()
        {
            return new HashSet<string>(variables);
        }

        /// <summary>
        /// Returns a string containing no spaces which, if passed to the Formula
        /// constructor, will produce a Formula f such that this.Equals(f).  All of the
        /// variables in the string should be normalized.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x + y", N, s => true).ToString() should return "X+Y"
        /// new Formula("x + Y").ToString() should return "x+Y"
        /// </summary>
        public override string ToString()
        {
            return string.Join(string.Empty,equation);
        }

        /// <summary>
        /// If obj is null or obj is not a Formula, returns false.  Otherwise, reports
        /// whether or not this Formula and obj are equal.
        /// 
        /// Two Formulae are considered equal if they consist of the same tokens in the
        /// same order.  To determine token equality, all tokens are compared as strings 
        /// except for numeric tokens, which are compared as doubles, and variable tokens,
        /// whose normalized forms are compared as strings.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        ///  
        /// new Formula("x1+y2", N, s => true).Equals(new Formula("X1  +  Y2")) is true
        /// new Formula("x1+y2").Equals(new Formula("X1+Y2")) is false
        /// new Formula("x1+y2").Equals(new Formula("y2+x1")) is false
        /// new Formula("2.0 + x7").Equals(new Formula("2.000 + x7")) is true
        /// </summary>
        public override bool Equals(object obj)
        {
            if ((object)this == null && (object)obj == null)
                return true;
            if ((object)this == null||(obj == null) || (obj.GetType() != typeof(Formula)))
                return false;
            return ToString().Equals(obj.ToString());
        }

        /// <summary>
        /// Reports whether f1 == f2, using the notion of equality from the Equals method.
        /// Note that if both f1 and f2 are null, this method should return true.  If one is
        /// null and one is not, this method should return false.
        /// </summary>
        public static bool operator ==(Formula f1, Formula f2)
        {
            if ((object)f1 == null && (object)f2 == null)
                return true;
            else if ((object)f1 == null||(object)f2 == null)
                return false;
            return f1.Equals(f2);
        }

        /// <summary>
        /// Reports whether f1 != f2, using the notion of equality from the Equals method.
        /// Note that if both f1 and f2 are null, this method should return false.  If one is
        /// null and one is not, this method should return true.
        /// </summary>
        public static bool operator !=(Formula f1, Formula f2)
        {
            return !(f1==f2);
        }

        /// <summary>
        /// Returns a hash code for this Formula.  If f1.Equals(f2), then it must be the
        /// case that f1.GetHashCode() == f2.GetHashCode().  Ideally, the probability that two 
        /// randomly-generated unequal Formulae have the same hash code should be extremely small.
        /// </summary>
        public override int GetHashCode()
        {
            return ToString().GetHashCode();
        }

        /// <summary>
        /// Given an expression, enumerates the tokens that compose it.  Tokens are left paren;
        /// right paren; one of the four operator symbols; a string consisting of a letter or underscore
        /// followed by zero or more letters, digits, or underscores; a double literal; and anything that doesn't
        /// match one of those patterns.  There are no empty tokens, and no token contains white space.
        /// </summary>
        private static IEnumerable<string> GetTokens(String formula)
        {
            // Patterns for individual tokens
            String lpPattern = @"\(";
            String rpPattern = @"\)";
            String opPattern = @"[\+\-*/]";
            String varPattern = @"[a-zA-Z_](?: [a-zA-Z_]|\d)*";
            String doublePattern = @"(?: \d+\.\d* | \d*\.\d+ | \d+ ) (?: [eE][\+-]?\d+)?";
            String spacePattern = @"\s+";

            // Overall pattern
            String pattern = String.Format("({0}) | ({1}) | ({2}) | ({3}) | ({4}) | ({5})",
                                            lpPattern, rpPattern, opPattern, varPattern, doublePattern, spacePattern);

            // Enumerate matching tokens that don't consist solely of white space.
            foreach (String s in Regex.Split(formula, pattern, RegexOptions.IgnorePatternWhitespace))
            {
                if (!Regex.IsMatch(s, @"^\s*$", RegexOptions.Singleline))
                {
                    yield return s;
                }
            }

        }


        /// <summary>
        /// checks to see if everything typed into the given formula is a legal expression
        /// </summary>
        /// <param name="normalize"></param>
        /// <param name="isValid"></param>
        private void CheckIfFormulaIsLegal(Func<string, string> normalize, Func<string, bool> isValid)
        {
            OneTokenMinimum();
            LegalTokens(normalize, isValid);
            ProperOrder();            
        }

        /// <summary>
        /// checks to make sure that formula sent is not empty
        /// </summary>
        private void OneTokenMinimum()
        {
            if (equation.Length == 0)
                throw new FormulaFormatException("formuala contains no tokens");
        }

        /// <summary>
        /// checks to see if all of the tokens placed in the equation are legal
        /// </summary>
        /// <param name="normalize"></param>
        /// <param name="isValid"></param>
        private void LegalTokens(Func<string, string> normalize, Func<string, bool> isValid)
        {
            //double that will store token if it is a double
            double result;

            //will hold the tokens as the loop goes through
            string token;

            //list to hold all of the variable tokens
            List<string> variables = new List<string>();

            //Hashest that holds all of the operators and parenthesis to compare token to
            HashSet<string> operators = new HashSet<string> { "(", ")", "+", "-", "*", "/" };
            for(int x = 0; x<equation.Length;x++)
            {
                //sets token to current x in the list
                token = equation[x];
                if (!operators.Contains(token)) 
                {
                    if (double.TryParse(token, out result))
                        equation[x] = result.ToString();
                    else
                    {
                        //normalizes token if it is a variable and stores it inside of the 
                        //variables list and in puts normalized token back into the equation array
                        token = normalize(token);
                        variables.Add(token);
                        equation[x] = token;
                        if (!IsVariable(token) || !isValid(token))
                            throw new FormulaFormatException(token + " is an illegal token");
                    }

                }
            }
            this.variables = variables.ToArray();
        }

        /// <summary>
        /// Check to see if all of the tokens in the equation are in a proper order
        /// </summary>
        private void ProperOrder()
        {
            //variables to count the amount of right and left Parenthesis
            int[] ParenthesisCount = new int[]{0,0};

            //delegate to check for particular errors
            ErrorCheck currentFunction = StartingTokenRule;
            foreach (string token in equation)
            {
                currentFunction(token);
                IncreaseParenthesisCount(token, ParenthesisCount);
                RightParenthesisRule(ParenthesisCount);
                currentFunction = SwitchFunction(token);
            }
            BalanceParenthesisRule(ParenthesisCount);
            EndingTokenRule(equation[equation.Length-1]);
        }

        /// <summary>
        /// changes the error checking function based on the current token
        /// </summary>
        /// <param name="token"></param>
        /// <returns></returns>
        private ErrorCheck SwitchFunction(string token)
        {
            if (new HashSet<string>{ "(", "+", "-", "*", "/" }.Contains(token))
                return ParenthesisFollowingRule;
            else
                return ExtraFollowingRule;
        }

        /// <summary>
        /// increases parenthesis count if token is a parenthesis
        /// </summary>
        /// <param name="token"></param>
        /// <param name="ParenthesisCount"></param>
        private void IncreaseParenthesisCount(string token, int[] ParenthesisCount)
        {
            switch (token)
            {
                case ("("):
                    ParenthesisCount[0]++;
                    break;
                case (")"):
                    ParenthesisCount[1]++;
                    break;
            }
        }

        /// <summary>
        /// The first token of an expression must be a number, a variable, or an opening parenthesis.
        /// </summary>
        /// <param name="token"></param>
        private void StartingTokenRule(string token)
        {
            double result;
            if (!IsVariable(token) && !double.TryParse(token, out result) && !token.Equals("("))
                throw new FormulaFormatException("the first token must be a number, variable, or an opening parenthesis");
        }

        /// <summary>
        /// The total number of opening parentheses must equal the total number of closing parentheses.
        /// </summary>
        /// <param name="ParenthesisCount"></param>
        private void RightParenthesisRule(int[] ParenthesisCount)
        {
            if (ParenthesisCount[0] < ParenthesisCount[1])
                throw new FormulaFormatException("opening and closing parenthesis do not match up");
        }

        /// <summary>
        /// The total number of opening parentheses must equal the total number of closing parentheses.
        /// </summary>
        /// <param name="ParenthesisCount"></param>
        private void BalanceParenthesisRule(int[] ParenthesisCount)
        {
            if (ParenthesisCount[0]!=ParenthesisCount[1])
                throw new FormulaFormatException("there is not an equal number of left and right parenthesis");
        }

        /// <summary>
        /// The last token of an expression must be a number, a variable, or a closing parenthesis.
        /// </summary>
        /// <param name="token"></param>
        private void EndingTokenRule(string token)
        {
            //variable to hold token if it is a double
            double result;

            if (!IsVariable(token) && !double.TryParse(token, out result) && !token.Equals(")"))
                throw new FormulaFormatException("the last token must be a number, variable, or a closing parenthesis");
        }


        /// <summary>
        /// Any token that immediately follows an opening parenthesis or an operator must 
        /// be either a number, a variable, or an opening parenthesis.
        /// </summary>
        /// <param name="token"></param>
        private void ParenthesisFollowingRule(string token)
        {
            //variable to hold token if it is a double
            double result;

            if (!IsVariable(token) && !double.TryParse(token, out result) && !token.Equals("("))
                throw new FormulaFormatException(token+",a number, variable, or opening parenthesis must follow an opening parenthesis or an operator");
        }

        /// <summary>
        /// Any token that immediately follows a number, a variable, or a 
        /// closing parenthesis must be either an operator or a closing parenthesis.
        /// </summary>
        /// <param name="token"></param>
        private void ExtraFollowingRule(string token)
        {
            if (!new HashSet<string>{ ")", "+", "-", "*", "/" }.Contains(token))
                throw new FormulaFormatException(token+",an operator or closing parenthesis must follow a number, a variable or closing parenthesis");
        }

    }

    /// <summary>
    /// Used to report syntactic errors in the argument to the Formula constructor.
    /// </summary>
    public class FormulaFormatException : Exception
    {
        /// <summary>
        /// Constructs a FormulaFormatException containing the explanatory message.
        /// </summary>
        public FormulaFormatException(String message)
            : base(message)
        {
        }
    }

    /// <summary>
    /// Used as a possible return value of the Formula.Evaluate method.
    /// </summary>
    public struct FormulaError
    {
        /// <summary>
        /// Constructs a FormulaError containing the explanatory reason.
        /// </summary>
        /// <param name="reason"></param>
        public FormulaError(String reason)
            : this()
        {
            Reason = reason;
        }

        /// <summary>
        ///  The reason why this FormulaError was created.
        /// </summary>
        public string Reason { get; private set; }
    }

    /// <summary>
    /// Evaluator Class is used to solve equations entered as a string
    /// </summary>
    public static class Evaluator
    {
        /// <summary>
        /// evaluates arithmetic expressions written using standard infix notation
        /// </summary>
        /// <param name="equation">string to be evaluated</param>
        /// <param name="variableEvaluator">function which should return value of variables sent with expression</param>
        /// <returns>an int of the solved equation</returns>
        public static double Evaluate(string[] equation, Func<string,double> variableEvaluator)
        {
            //stacks that will hold values and operators
            var values = new Stack<double>();
            var operators = new Stack<string>();

            //equation solved
            SolveEquation(equation, variableEvaluator, values, operators);

            return values.Pop();
        }

        /// <summary>
        /// takes an array of strings and then solves and solves the equation in the array
        /// </summary>
        /// <param name="equation">array of strings which stores equation</param>
        /// <param name="variableEvaluator">function which should return value of variables sent with expression</param>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void SolveEquation(string[] equation, Func<string, double> variableEvaluator, Stack<double> values, Stack<string> operators)
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
                    value = variableEvaluator(t);
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
            if (operators.HasOnTop(new string[] { "+", "-" }))
                Operate(operators.Pop(), values, operators);
        }

        /// <summary>
        /// used to perform multiplication and division operations
        /// </summary>
        /// <param name="values">stack of doubles witch will hold all values from the equation</param>
        /// <param name="operators">stack of strings witch will hold all operators from the equation</param>
        private static void MultiplcationAndDivsionOperation(Stack<double> values, Stack<string> operators)
        {
            if (operators.HasOnTop(new string[]{"*","/"}))
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
            operators.Pop();
            MultiplcationAndDivsionOperation(values, operators);
        }

        /// <summary>
        /// Checks to see if a stack has a certain group of an object at the top
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="stack">the stack that will be checked</param>
        /// <param name="tokens">the IEnumerable of tokens to check for</param>
        /// <returns></returns>
        private static bool HasOnTop<T>(this Stack<T> stack, IEnumerable<T> tokens)
        {
            if(stack.Count > 0)
            foreach (T token in tokens)
                if ((stack.Peek().Equals(token)))
                    return true;            
            return false;
        }
    }
}
