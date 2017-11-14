using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;
using System.Collections.Generic;


namespace UnitTestProject1
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestToStringMethod()
        {
            Assert.AreEqual(new Formula("3+5*9").ToString(), ("3+5*9"));
            Assert.AreEqual(new Formula(" 3 + 5* 9/7+ 34").ToString(), ("3+5*9/7+34"));
            Assert.AreEqual(new Formula("a5+67-5*u7").ToString(), ("a5+67-5*u7"));
            Assert.AreEqual(new Formula("a5  +    67- 5*u7").ToString(), ("a5+67-5*u7"));
            Assert.AreEqual(new Formula("y5 +4*    D43/ DeSYer3").ToString(), ("y5+4*D43/DeSYer3"));
            Assert.AreEqual(new Formula("y5+4*D43/DeSYer3", s => s.ToLower(), s => true).ToString(), ("y5+4*d43/desyer3"));
            Assert.AreNotEqual(new Formula("y5+4*D43/DeSYer3").ToString(), ("y5+4*d43/desyer3"));
            Assert.AreNotEqual(new Formula("y5+4*D43/DeSYer3", s => s.ToLower(), s => true).ToString(), ("y5+4*D43/DeSYer3"));
        }

        [TestMethod]
        public void TestEqualsMethod()
        {
            Formula formula1 = null;
            Formula formula2 = null;
            Assert.IsFalse(new Formula("24").Equals(formula1));
            Assert.IsFalse(new Formula("7+8*9").Equals(formula2));
            Assert.IsTrue(new Formula("2.0 + x7").Equals(new Formula("2.000 + x7")));
            Assert.IsTrue(new Formula("x1+y2", s=>s.ToUpper(), s => true).Equals(new Formula("X1  +  Y2")));
            Assert.IsFalse(new Formula("x1+y2").Equals(new Formula("X1+Y2")));
            Assert.IsFalse(new Formula("x1+y2").Equals(new Formula("y2+x1")));
            Assert.IsFalse(new Formula("x1+y2").Equals("x1+y2"));
            Assert.IsFalse(new Formula("x1+y2").Equals(7));
            Assert.IsFalse(new Formula("x1+y2").Equals(null));
            Assert.IsFalse(new Formula("x1+y2", s => s.ToUpper(), v => true).Equals(new Formula("x1+y2")));
        }

        [TestMethod]
        public void TestGetVariablesMethod()
        {
            HashSet<String> aPends1 = new HashSet<string>(new Formula("_3_4dfg5+g5g4+4534*_3_4dfg5-sldf+3-5*234").GetVariables());
            Assert.IsTrue(aPends1.SetEquals(new HashSet<string> { "_3_4dfg5", "g5g4", "sldf" }));
            HashSet<String> aPends2 = new HashSet<string>(new Formula("_3_4DfG5+g5g4+4534*_3_4dfg5-sldf+3-5*234").GetVariables());
            Assert.IsTrue(aPends2.SetEquals(new HashSet<string> { "_3_4DfG5", "g5g4", "_3_4dfg5", "sldf" }));
            HashSet<String> aPends3 = new HashSet<string>(new Formula("_3_4DfG5+g5g4+4534*_3_4dfg5-sldf+3-5*234",s=>s.ToUpper(),s=>true).GetVariables());
            Assert.IsTrue(aPends3.SetEquals(new HashSet<string> {"G5G4", "_3_4DFG5", "SLDF" }));
            HashSet<String> aPends4 = new HashSet<string>(new Formula("x+y*z", s=>s.ToUpper(), s => true).GetVariables());
            Assert.IsTrue(aPends4.SetEquals(new HashSet<string> { "X", "Y", "Z" }));
            HashSet<String> aPends5 = new HashSet<string>(new Formula("x+X*z", s=>s.ToUpper(), s => true).GetVariables());
            Assert.IsTrue(aPends5.SetEquals(new HashSet<string> { "X", "Z" }));
            HashSet<String> aPends6 = new HashSet<string>(new Formula("x+X*z").GetVariables());
            Assert.IsTrue(aPends6.SetEquals(new HashSet<string> { "x", "X", "z" }));
        }

        [TestMethod]
        public void TestOtherEqualsMethod()
        {
            Formula formula1 = null;
            Formula formula2 = null;
            Assert.IsTrue(formula1 == formula2);
            Assert.IsFalse(formula1 == new Formula("hjjjjjjjjjjj"));
            Assert.IsFalse(new Formula("h47")==formula2);
            Assert.IsTrue(new Formula("2.0 + x7") == new Formula("2.000 + x7"));
            Assert.IsTrue(new Formula("x1+y2", s => s.ToUpper(), s => true)==new Formula("X1  +  Y2"));
            Assert.IsFalse(new Formula("x1+y2")==(new Formula("X1+Y2")));
            Assert.IsFalse(new Formula("x1+y2")==(new Formula("y2+x1")));
            Assert.IsFalse(new Formula("x1+y2", s => s.ToUpper(), v => true)==(new Formula("x1+y2")));
        }

        [TestMethod]
        public void TestNotEqualsMethod()
        {
            Formula formula1 = null;
            Formula formula2 = null;
            Assert.IsFalse(formula1 != formula2);
            Assert.IsTrue(formula1 != new Formula("hjjjjjjjjjjj"));
            Assert.IsTrue(new Formula("h47") != formula2);
            Assert.IsFalse(new Formula("2.0 + x7") != new Formula("2.000 + x7"));
            Assert.IsFalse(new Formula("x1+y2", s => s.ToUpper(), s => true) != new Formula("X1  +  Y2"));
            Assert.IsTrue(new Formula("x1+y2") != (new Formula("X1+Y2")));
            Assert.IsTrue(new Formula("x1+y2") != (new Formula("y2+x1")));
            Assert.IsTrue(new Formula("x1+y2", s => s.ToUpper(), v => true) != (new Formula("x1+y2")));
        }
        [TestMethod]
        public void TestGetHashCodeMethod()
        {
            
            Assert.AreEqual(new Formula("4+5-dgfd/3").GetHashCode(), new Formula("4+5-dgfd/3").GetHashCode());
            Assert.AreEqual(new Formula("2.0 + x7").GetHashCode(), new Formula("2.000 + x7").GetHashCode());
            Assert.AreEqual(new Formula("x1+y2", s => s.ToUpper(), s => true).GetHashCode(), new Formula("X1  +  Y2").GetHashCode());
        }


        //Private methods
        [TestMethod]
        public void TestPrivateToString()
        {
            Assert.AreEqual(new Formula("3.00000").ToString(), ("3"));
            Assert.AreEqual(new Formula("3.10500").ToString(), ("3.105"));
            Assert.AreNotEqual(new Formula("3.00000").ToString(), ("3.00000"));
            Assert.AreNotEqual(new Formula("3").ToString(), ("3.000"));
        }

        [TestMethod]
        public void TestPrivateGetHashCode()
        {
            Assert.AreEqual(new Formula("43534").GetHashCode(), "43534".GetHashCode());
        }
    }
}
