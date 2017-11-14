using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SS;
using SpreadsheetUtilities;
using System.Collections.Generic;

namespace UnitTestProject1
{

    [TestClass]
    public class SpreadSheetTests
    {
        

        [TestMethod]
        public void GetNamesOfAllNonemptyCellsTest1()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            string[] cells = (string[])spreadsheet.GetNamesOfAllNonemptyCells();
            Assert.IsTrue(cells.Length == 0);
        }

        [TestMethod]
        public void GetNamesOfAllNonemptyCellsTest2()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a5", "34");
            spreadsheet.SetContentsOfCell("a3", "hello");
            spreadsheet.SetContentsOfCell("a8", "=7+8-9");
            HashSet<string> cells = new HashSet<string>(spreadsheet.GetNamesOfAllNonemptyCells());
            Assert.IsTrue(cells.Count == 3);
            Assert.IsTrue(cells.Contains("a5"));
            Assert.IsTrue(cells.Contains("a3"));
            Assert.IsTrue(cells.Contains("a8"));
        }

        [TestMethod]
        public void GetNamesOfAllNonemptyCellsTest3()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("dsf5", "       ");
            spreadsheet.SetContentsOfCell("sa4", "45");
            spreadsheet.SetContentsOfCell("sa3", "hello");
            spreadsheet.SetContentsOfCell("sa6", "=7+2");
            spreadsheet.SetContentsOfCell("sa4", "");
            spreadsheet.SetContentsOfCell("sa3", "       ");
            spreadsheet.SetContentsOfCell("sa6", " ");
            string[] cells = (string[])spreadsheet.GetNamesOfAllNonemptyCells();
            Assert.IsTrue(cells.Length == 0);
        }

        [TestMethod]
        public void GetCellContentsTest1()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a5", "34");
            spreadsheet.SetContentsOfCell("a7", "=a5");
            spreadsheet.SetContentsOfCell("a3", "hello");
            spreadsheet.SetContentsOfCell("a8", "=7+8-9");
            Assert.IsTrue(spreadsheet.GetCellContents("a5").Equals(34.0));
            Assert.IsTrue(spreadsheet.GetCellContents("a7").ToString().Equals("a5"));
            Assert.IsTrue(spreadsheet.GetCellContents("a3").Equals("hello"));
            Assert.IsTrue(spreadsheet.GetCellContents("a8").ToString().Equals("7+8-9"));
            Assert.IsTrue(spreadsheet.GetCellContents("dsf4").Equals(""));
        }

        [TestMethod()]
        public void GetCellContentsTest2()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a1", "34");
            spreadsheet.SetContentsOfCell("a2", "=45");
            spreadsheet.SetContentsOfCell("a3", "hello");
            spreadsheet.SetContentsOfCell("a4", "=77");

            spreadsheet.SetContentsOfCell("a1", "=45");
            spreadsheet.SetContentsOfCell("a2", "hello");
            spreadsheet.SetContentsOfCell("a3", "34");
            spreadsheet.SetContentsOfCell("a4", "       ");

            Assert.IsTrue(spreadsheet.GetCellContents("a1").ToString().Equals("45"));
            Assert.IsTrue(spreadsheet.GetCellContents("a2").Equals("hello"));
            Assert.IsTrue(spreadsheet.GetCellContents("a3").Equals(34.0));
            Assert.IsTrue(spreadsheet.GetCellContents("a4").Equals(""));

            Assert.IsTrue(new HashSet<string>(spreadsheet.GetNamesOfAllNonemptyCells()).Count.Equals(3));
        }

        [TestMethod()]
        [ExpectedException(typeof(InvalidNameException))]
        public void InvalidNameTest1()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("466fg", "45");
        }

        [TestMethod()]
        [ExpectedException(typeof(InvalidNameException))]
        public void InvalidNameTest2()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell(null, "45");
        }

        [TestMethod()]
        [ExpectedException(typeof(ArgumentNullException))]
        public void InvalidTextParameter()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("d4", null);
        }

        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void CircularTest1()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a1", "=a1");
        }

        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void CircularTest2()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a1", "=a2");
            spreadsheet.SetContentsOfCell("a2", "=a3");
            spreadsheet.SetContentsOfCell("a3", "=a4");
            spreadsheet.SetContentsOfCell("a4", "=a1");
        }



        [TestMethod]
        public void NoCrashing()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a1", "sdfsd");
            spreadsheet.SetContentsOfCell("a2", "=a1");
            spreadsheet.SetContentsOfCell("a3", "=a4");
            Assert.IsTrue(true);
        }

        [TestMethod]
        public void ValuesTest1()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a1", "asd");
            spreadsheet.SetContentsOfCell("a2", "34");
            spreadsheet.SetContentsOfCell("a3", "=5+6");
            spreadsheet.SetContentsOfCell("a4", "=a2");
            spreadsheet.SetContentsOfCell("a5", "=a1");
            spreadsheet.SetContentsOfCell("a6", "=a7");

            Assert.AreEqual("asd", spreadsheet.GetCellValue("a1"));
            Assert.AreEqual(34.0, spreadsheet.GetCellValue("a2"));
            Assert.AreEqual(11.0, spreadsheet.GetCellValue("a3"));
            Assert.AreEqual(34.0, spreadsheet.GetCellValue("a4"));
            Assert.AreEqual(typeof(FormulaError), spreadsheet.GetCellValue("a5").GetType());
            Assert.AreEqual(typeof(FormulaError), spreadsheet.GetCellValue("a5").GetType());


        }


    }
}
