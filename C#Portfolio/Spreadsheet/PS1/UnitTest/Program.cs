using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FormulaEvaluator;

namespace UnitTest
{
    class Program
    {

        static double VariableValue(string variable) 
        {
            var dict = new Dictionary<string, int>(){
	            {"cat3", 2},{"dog45", 1},{"llama123", 0},{"iguana6543", 5}};

            return dict[variable];
        }

        static void Main(string[] args)
        {
            Console.WriteLine(Evaluator.Evaluate("3\01+1", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("9 8",VariableValue));
            Console.WriteLine(Evaluator.Evaluate("(3\t45+8)*llama123-8*(9-7)", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("8+9*(23-34)/iguana6543", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("5+8/4*12", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("5+8/(4*12)", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("cat3/dog45*iguana6543+llama123", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("(2+7)*(8-4)", VariableValue));
            Console.WriteLine(Evaluator.Evaluate("2*(3+5+6+2+4)",VariableValue));
        }
    }
}
