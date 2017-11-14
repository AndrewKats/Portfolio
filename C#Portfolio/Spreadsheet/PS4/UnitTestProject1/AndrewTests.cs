// Implemented by Andrew Katsanevas, 10/1/2015, for CS3500.
// Updated by Andrew Katsanevas, 10/15/2015, for PS5.

using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;
using SS;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Threading;
using System.Xml;
using System.IO;

namespace SpreadsheetTester
{
    /// <summary>
    /// Tests the methods of the Spreadsheet library
    /// </summary>
    [TestClass]
    public class SpreadsheetTester
    {
        /// <summary>
        /// Tests the GetCellContents method on an empty cell
        /// </summary>
        [TestMethod]
        public void EmptyTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.AreEqual("", ss.GetCellContents("A2"));
        }

        /// <summary>
        /// Tests the GetNamesOfAllNonemptyCells method on an empty cell
        /// </summary>
        [TestMethod]
        public void EmptyTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>()).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests the SetContentsOfCell and GetNamesOfAllNonemptyCells methods on a single cell with a number
        /// </summary>
        [TestMethod]
        public void OneNumberTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "2")));
            Assert.AreEqual(2.0, ss.GetCellContents("A2"));
            Assert.AreEqual("", ss.GetCellContents("B3"));
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests the SetContentsOfCell and GetNamesOfAllNonemptyCells methods on a single cell with text
        /// </summary>
        [TestMethod]
        public void OneTextTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "Hello")));
            Assert.AreEqual("Hello", ss.GetCellContents("A2"));
            Assert.AreEqual("", ss.GetCellContents("B3"));
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests the SetContentsOfCell and GetNamesOfAllNonemptyCells methods on a single cell with a Formula
        /// </summary>
        [TestMethod]
        public void OneFormulaTest()
        {
            Spreadsheet ss = new Spreadsheet();
            HashSet<string> THESET = (HashSet<string>)ss.SetContentsOfCell("A2", "=1+1");
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "=1+1")));
            Assert.AreEqual(new Formula("1+1"), ss.GetCellContents("A2"));
            Assert.AreEqual("", ss.GetCellContents("B3"));
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests methods on cells of only numbers
        /// </summary>
        [TestMethod]
        public void NumberTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "2")));
            Assert.IsTrue((new HashSet<string>() { "B3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B3", "3")));
            Assert.IsTrue((new HashSet<string>() { "C4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C4", "4")));
            Assert.AreEqual(3.0, ss.GetCellContents("B3"));
            Assert.AreEqual("", ss.GetCellContents("T84"));
            Assert.IsTrue((new HashSet<string>() { "A2", "B3", "C4" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests methods on cells of only text
        /// </summary>
        [TestMethod]
        public void TextTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "two")));
            Assert.IsTrue((new HashSet<string>() { "B3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B3", "three")));
            Assert.IsTrue((new HashSet<string>() { "C4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C4", "four")));
            Assert.AreEqual("three", ss.GetCellContents("B3"));
            Assert.AreEqual("", ss.GetCellContents("T84"));
            Assert.IsTrue((new HashSet<string>() { "A2", "B3", "C4" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests methods on cells of only Formulas
        /// </summary>
        [TestMethod]
        public void FormulaTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A2", "=1+1")));
            Assert.IsTrue((new HashSet<string>() { "B3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B3", "=x1 + 3")));
            Assert.IsTrue((new HashSet<string>() { "C4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C4", "=5*5")));
            Assert.AreEqual(new Formula("x1 + 3"), ss.GetCellContents("B3"));
            Assert.AreEqual("", ss.GetCellContents("T84"));
            Assert.IsTrue((new HashSet<string>() { "A2", "B3", "C4" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests methods on cells of only Formulas that depend on each other
        /// </summary>
        [TestMethod]
        public void BigFormulaTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1+ 3")));
            Assert.IsTrue((new HashSet<string>() { "C3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 + A1")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "=C3 + B2")));
            Assert.IsTrue((new HashSet<string>() { "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 - A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "D4" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from Formula to text
        /// </summary>
        [TestMethod]
        public void Formula2TextTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.AreEqual(new Formula("1 + 1"), ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Hello")));
            Assert.AreEqual("Hello", ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from Formula to number
        /// </summary>
        [TestMethod]
        public void Formula2NumberTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.AreEqual(new Formula("1 + 1"), ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "5.8")));
            Assert.AreEqual(5.8, ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from text to Formula
        /// </summary>
        [TestMethod]
        public void Text2FormulaTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Hello")));
            Assert.AreEqual("Hello", ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.AreEqual(new Formula("1 + 1"), ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from text to number
        /// </summary>
        [TestMethod]
        public void Text2NumberTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Hello")));
            Assert.AreEqual("Hello", ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "5")));
            Assert.AreEqual(5.0, ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from number to Formula
        /// </summary>
        [TestMethod]
        public void Number2FormulaTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "1.5e10")));
            Assert.AreEqual(1.5e10, ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=x3 - 1.44e5")));
            Assert.AreEqual(new Formula("x3 - 1.44e5"), ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests changing a cell's content type from number to text
        /// </summary>
        [TestMethod]
        public void Number2TextTest()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "1.5e10")));
            Assert.AreEqual(1.5e10, ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Hello")));
            Assert.AreEqual("Hello", ss.GetCellContents("A1"));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests a variety of sequential operations of the spreadsheet.
        /// For thoroughly testing the methods of PS4.
        /// </summary>
        [TestMethod]
        public void TheBigOne()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N30" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N30", "30")));
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.IsTrue((new HashSet<string>() { "T1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("T1", "Hi")));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1+ 3")));
            Assert.IsTrue((new HashSet<string>() { "C3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 + A1")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "=C3 + B2")));
            Assert.IsTrue((new HashSet<string>() { "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 - A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Yo Dawg")));
            Assert.IsTrue((new HashSet<string>() { "B2", "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "5.0")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "")));
            Assert.IsTrue((new HashSet<string>() { "N30" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N30", "")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "T1" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
        }

        /// <summary>
        /// Tests the GetValue method of Spreadsheet for numbers
        /// </summary>
        [TestMethod]
        public void GetValueTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N30" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N30", "30")));
            Assert.AreEqual(30.0, ss.GetCellValue("N30"));
        }

        /// <summary>
        /// Tests the GetValue method of Spreadsheet for Formulas
        /// </summary>
        [TestMethod]
        public void GetValueTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "a5" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("a5", "=1+1")));
            Assert.AreEqual(2.0, ss.GetCellValue("a5"));
        }

        /// <summary>
        /// Tests the GetValue method of Spreadsheet for Formulas with variables
        /// </summary>
        [TestMethod]
        public void GetValueTest3()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "a5" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("a5", "=1")));
            Assert.IsTrue((new HashSet<string>() { "a6" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("a6", "1")));
            Assert.IsTrue((new HashSet<string>() { "b2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("b2", "=1+a5+a6")));
            Assert.AreEqual(1.0, ss.GetCellValue("a5"));
            Assert.AreEqual(1.0, ss.GetCellValue("a6"));
            Assert.AreEqual(3.0, ss.GetCellValue("b2"));
        }

        /// <summary>
        /// Tests the GetValue method of Spreadsheet for text
        /// </summary>
        [TestMethod]
        public void GetValueTest4()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "bbb555" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("bbb555", "hello")));
            Assert.AreEqual("hello", ss.GetCellValue("bbb555"));
        }

        /// <summary>
        /// Tests the GetValue method of Spreadsheet for an empty spreadsheet
        /// </summary>
        [TestMethod]
        public void GetValueTestEmtpy()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.AreEqual("", ss.GetCellValue("A7"));
        }

        /// <summary>
        /// Tests the Changed variable of Spreadsheet for an empty spreadsheet
        /// </summary>
        [TestMethod]
        public void ChangedTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.AreEqual(false, ss.Changed);
        }

        /// <summary>
        /// Tests the Changed variable of Spreadsheet for adding a number
        /// </summary>
        [TestMethod]
        public void ChangedTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "30")));
            Assert.AreEqual(true, ss.Changed);
        }

        /// <summary>
        /// Tests the Changed variable of Spreadsheet for adding a formula
        /// </summary>
        [TestMethod]
        public void ChangedTest3()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "=1+1")));
            Assert.AreEqual(true, ss.Changed);
        }

        /// <summary>
        /// Tests the Changed variable of Spreadsheet for adding text
        /// </summary>
        [TestMethod]
        public void ChangedTest4()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.AreEqual(true, ss.Changed);
        }

        /// <summary>
        /// Tests the Changed variable of Spreadsheet for saving
        /// </summary>
        [TestMethod]
        public void ChangedTest5()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            ss.Save("MySpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
        }


        /// <summary>
        /// Tests the three-argument constructor and its version
        /// </summary>
        [TestMethod]
        public void ThreeArgConstrTest()
        {
            Spreadsheet ss = new Spreadsheet(s => Regex.IsMatch(s, @"^[A-Z]+([0-5])+$"), s => s.ToUpper(), "1.1");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "30")));
            Assert.AreEqual("1.1", ss.Version);
        }

        /// <summary>
        /// Tests the three-argument constructor for adding cells
        /// </summary>
        [TestMethod]
        public void ThreeArgTest1()
        {
            Spreadsheet ss = new Spreadsheet(s => Regex.IsMatch(s, @"^[A-Z]+([0-5])+$"), s => s.ToUpper(), "1.1");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "30")));
            Assert.IsTrue((new HashSet<string>() { "A0" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("a0", "=5+N3")));
            Assert.AreEqual(30.0, ss.GetCellValue("N3"));
            Assert.AreEqual(35.0, ss.GetCellValue("A0"));
            Assert.AreEqual("", ss.GetCellValue("B4"));
        }

        /// <summary>
        /// Tests the Save and GetSavedVerions methods of Spreadsheet for an empty spreadsheet
        /// </summary>
        [TestMethod]
        public void SaveAndVersionTestEmpty()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.Save("EmptySpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("default", ss.GetSavedVersion("EmptySpreadsheet.xml"));
        }

        /// <summary>
        /// Tests the Save and GetSavedVerions methods of Spreadsheet for a spreadsheet of one element
        /// </summary>
        [TestMethod]
        public void SaveAndVersionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            ss.Save("OneSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("default", ss.GetSavedVersion("OneSpreadsheet.xml"));
        }

        /// <summary>
        /// Tests the Save and GetSavedVerions methods of Spreadsheet for a spreadsheet of a few elements
        /// </summary>
        [TestMethod]
        public void SaveAndVersionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.IsTrue((new HashSet<string>() { "A4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A4", "5.5")));
            Assert.IsTrue((new HashSet<string>() { "B4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B4", "= A4 + 17")));
            ss.Save("TheSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("default", ss.GetSavedVersion("TheSpreadsheet.xml"));
        }

        /// <summary>
        /// Tests the Save and GetSavedVerions methods of Spreadsheet with the three-argument constructor
        /// </summary>
        [TestMethod]
        public void SaveAndVersionTest3()
        {
            Spreadsheet ss = new Spreadsheet(s => true, s => s, "1.5");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.IsTrue((new HashSet<string>() { "A4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A4", "5.5")));
            Assert.IsTrue((new HashSet<string>() { "B4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B4", "= A4 + 17")));
            ss.Save("ThirdSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("1.5", ss.GetSavedVersion("ThirdSpreadsheet.xml"));
        }

        /// <summary>
        /// Tests the Save and GetSavedVerions methods of Spreadsheet with the four-argument constructor
        /// </summary>
        [TestMethod]
        public void SaveAndVersionTest4()
        {
            Spreadsheet ss = new Spreadsheet(s => 1 == 1, s => s, "1.5");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.IsTrue((new HashSet<string>() { "A4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A4", "5.5")));
            Assert.IsTrue((new HashSet<string>() { "B4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B4", "= A4 + 17")));
            ss.Save("FourthSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("1.5", ss.GetSavedVersion("FourthSpreadsheet.xml"));

            Assert.AreEqual("hello", ss.GetCellValue("N3"));
            Assert.AreEqual("hello", ss.GetCellContents("N3"));
            Assert.AreEqual(5.5, ss.GetCellValue("A4"));
            Assert.AreEqual(5.5, ss.GetCellContents("A4"));
            Assert.AreEqual(22.5, ss.GetCellValue("B4"));
            Assert.AreEqual(new Formula("A4 + 17"), ss.GetCellContents("B4"));

            Spreadsheet ss2 = new Spreadsheet("FourthSpreadsheet.xml", s => true, s => s, "1.5");
            Assert.AreEqual(false, ss2.Changed);
            Assert.AreEqual("1.5", ss2.GetSavedVersion("FourthSpreadsheet.xml"));

            Assert.AreEqual("hello", ss2.GetCellValue("N3"));
            Assert.AreEqual("hello", ss2.GetCellContents("N3"));
            Assert.AreEqual(5.5, ss2.GetCellValue("A4"));
            Assert.AreEqual(5.5, ss2.GetCellContents("A4"));
            Assert.AreEqual(22.5, ss2.GetCellValue("B4"));
            Assert.AreEqual(new Formula("A4 + 17"), ss2.GetCellContents("B4"));
        }

        /// <summary>
        /// Tests a variety of sequential operations of the spreadsheet.
        /// Includes new operations from PS5.
        /// </summary>
        [TestMethod]
        public void TheBiggerOne()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.AreEqual(false, ss.Changed);

            Assert.IsTrue((new HashSet<string>() { "n30" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("n30", "30")));
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.IsTrue((new HashSet<string>() { "T1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("T1", "Hi")));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1+ 3")));

            ss.Save("BigSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);

            Assert.AreEqual("default", ss.Version);
            Assert.IsTrue((new HashSet<string>() { "C3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 + A1")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "=C3 + B2")));
            Assert.IsTrue((new HashSet<string>() { "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 - A1")));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "Yo Dawg")));
            Assert.IsTrue((new HashSet<string>() { "B2", "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "5.0")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "")));
            Assert.IsTrue((new HashSet<string>() { "n30" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("n30", "")));

            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "T1" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
            Assert.AreEqual(true, ss.Changed);
            ss.Save("BigSpreadsheet.xml");

            Spreadsheet ss2 = new Spreadsheet("BigSpreadsheet.xml", s => true, s => s.ToUpper(), "default");
            Assert.AreEqual(false, ss2.Changed);
            Assert.AreEqual("default", ss2.Version);

            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "T1" }).SetEquals(ss.GetNamesOfAllNonemptyCells()));
            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "T1" }).SetEquals(ss2.GetNamesOfAllNonemptyCells()));
            Assert.IsTrue((new HashSet<string>() { "N30" }).SetEquals((HashSet<string>)ss2.SetContentsOfCell("n30", "=5*5")));
            Assert.IsTrue((new HashSet<string>() { "Y7" }).SetEquals((HashSet<string>)ss2.SetContentsOfCell("y7", "= n30 + 6.5")));


            Assert.AreEqual(new Formula("5*5"), ss2.GetCellContents("n30"));
            Assert.AreEqual(25.0, ss2.GetCellValue("N30"));

            Assert.IsTrue((new HashSet<string>() { "A1", "B2", "C3", "T1", "N30", "Y7" }).SetEquals(ss2.GetNamesOfAllNonemptyCells()));
            Assert.AreEqual(true, ss2.Changed);
            ss2.Save("Big2Spreadsheet.xml");
            Assert.AreEqual(false, ss2.Changed);
        }

        /// <summary>
        /// Tests the four-argument constructor for throwing an exception when the wrong version is given
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void SaveAndVersionExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet(s => true, s => s, "1.5");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.IsTrue((new HashSet<string>() { "A4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A4", "5.5")));
            Assert.IsTrue((new HashSet<string>() { "B4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B4", "= A4 + 17")));
            ss.Save("ExpSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("1.5", ss.GetSavedVersion("ExpSpreadsheet.xml"));
            Spreadsheet ss2 = new Spreadsheet("ExpSpreadsheet.xml", s => true, s => s, "1.6");
        }

        /// <summary>
        /// Tests the four-argument constructor for throwing an exception when a nonexistent file is given
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void SaveAndVersionExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet("NonexistentSpreadsheet.xml", s => true, s => s, "1.0");
        }

        /// <summary>
        /// Tests the four-argument constructor for throwing an exception when the new validator invalidates all the variables
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void SaveAndVersionExceptionTest3()
        {
            Spreadsheet ss = new Spreadsheet(s => true, s => s, "1.5");
            Assert.IsTrue((new HashSet<string>() { "N3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("N3", "hello")));
            Assert.IsTrue((new HashSet<string>() { "A4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A4", "5.5")));
            Assert.IsTrue((new HashSet<string>() { "B4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B4", "= A4 + 17")));
            ss.Save("ExpSpreadsheet.xml");
            Assert.AreEqual(false, ss.Changed);
            Assert.AreEqual("1.5", ss.GetSavedVersion("ExpSpreadsheet.xml"));
            Spreadsheet ss2 = new Spreadsheet("ExpSpreadsheet.xml", s => false, s => s, "1.5");
        }

        /// <summary>
        /// Tests the GetCellValue method of Spreadsheet for throwing an exception when an illegal variable name is given
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void GetCellValueExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.GetCellValue("_");
        }

        /// <summary>
        /// Tests the GetCellValue method of Spreadsheet for throwing an exception when a null variable name is given
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void GetCellValueExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.GetCellValue(null);
        }

        /// <summary>
        /// Tests the GetCellValue method of Spreadsheet for throwing an exception when an invalid variable name is given
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void GetCellValueExceptionTest3()
        {
            Spreadsheet ss = new Spreadsheet(s => Regex.IsMatch(s, @"^[A-Z]+([0-5])+$"), s => s.ToUpper(), "1.1");
            ss.GetCellValue("A6");
        }

        /// <summary>
        /// Tests GetCellContents for throwing an exception when passed null
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void GetExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.GetCellContents(null);
        }

        /// <summary>
        /// Tests GetCellContents for throwing an exception when passed an invalid name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void GetExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.GetCellContents("12");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed a null Formula
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ArgumentNullException))]
        public void FormulaExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell("A2", null);
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed a null name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void FormulaExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell(null, "=1+1");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed an invalid name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void FormulaExceptionTest3()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell("23_A", "=1+1");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed an invalid name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void FormulaExceptionTest4()
        {
            Spreadsheet ss = new Spreadsheet(s => Regex.IsMatch(s, @"^[A-Z]+([0-5])+$"), s => s.ToUpper(), "1.1");
            ss.SetContentsOfCell("GR8", "=1+1");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed a null string
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ArgumentNullException))]
        public void TextExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell("A2", (string)null);
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed a null name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void TextExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell(null, "Hello");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed an invalid name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void TextExceptionTest3()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell("23987______b", "Hello");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed a null name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void NumberExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell(null, "55.0");
        }

        /// <summary>
        /// Tests SetContentsOfCell for throwing an exception when passed an invalid name
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void NumberExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            ss.SetContentsOfCell("2_b", "57");
        }

        /// <summary>
        /// Tests that the CircularException is thrown
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void CircularExceptionTest1()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=1 + 1")));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1 + 3")));
            Assert.IsTrue((new HashSet<string>() { "C3" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 + A1")));
            Assert.IsTrue((new HashSet<string>() { "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("D4", "=C3 + B2")));
            Assert.IsTrue((new HashSet<string>() { "C3", "D4" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("C3", "=B2 - D4")));
        }

        /// <summary>
        /// Tests that the CircularException is thrown when one cell depends on itself
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void CircularExceptionTest2()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=A1")));
        }

        /// <summary>
        /// Tests that the CircularException is thrown when two cells depend on each other
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void CircularExceptionTest3()
        {
            Spreadsheet ss = new Spreadsheet();
            Assert.IsTrue((new HashSet<string>() { "A1" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("A1", "=B2")));
            Assert.IsTrue((new HashSet<string>() { "B2" }).SetEquals((HashSet<string>)ss.SetContentsOfCell("B2", "=A1")));
        }

        
    }
}