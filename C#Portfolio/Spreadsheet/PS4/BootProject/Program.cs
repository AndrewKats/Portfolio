using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SS;
using System.Xml;

namespace BootProject
{
    class Program
    {
        static void Main(string[] args)
        {

            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("a5", "34");
            spreadsheet.SetContentsOfCell("a7", "=a5");
            spreadsheet.SetContentsOfCell("a3", "hello");
            spreadsheet.SetContentsOfCell("a8", "=7+8-9");
            spreadsheet.Save("testfile");

            Spreadsheet spreadsheet2 = new Spreadsheet("testfile", s => true, s => s, "default");
            spreadsheet2.Save("hello");

            Console.WriteLine(spreadsheet.GetSavedVersion("testfile"));
            Console.WriteLine(spreadsheet.GetSavedVersion("hello"));

            foreach(string name in spreadsheet.GetNamesOfAllNonemptyCells())
            {
                Console.WriteLine("name: "+name+" content: "+spreadsheet.GetCellContents(name) +" value: "+spreadsheet.GetCellValue(name));
            }
            Console.WriteLine();
            foreach (string name in spreadsheet2.GetNamesOfAllNonemptyCells())
            {
                Console.WriteLine("name: " + name+" content: " + spreadsheet2.GetCellContents(name) + " value: " + spreadsheet2.GetCellValue(name));
            }

        }
    }
}
