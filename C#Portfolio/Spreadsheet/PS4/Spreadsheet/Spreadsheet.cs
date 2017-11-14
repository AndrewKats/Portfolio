//Name:William Garnes
//uid: u0922152
//branch PS5
using System;
using System.Collections.Generic;
using System.Linq;
using SpreadsheetUtilities;
using System.Text.RegularExpressions;
using System.Xml;
using System.IO;

namespace SS
{
    /// <summary>
    /// performs the operations as described in the abstract class file
    /// </summary>
    public class Spreadsheet:AbstractSpreadsheet
    {
        /// <summary>
        /// throws an error if a name is invalid
        /// </summary>
        /// <param name="name">the name to be tested</param>
        private void CheckIfNameIsValid(ref string name)
        {
            if (name == null)
                throw new InvalidNameException();
            name = Normalize(name);
            if (name == null || !Regex.IsMatch(name, @"^[a-zA-Z]+[\d]+$")||!IsValid(name))
                throw new InvalidNameException();
        }
        /// <summary>
        /// dependency graph to hold all of the different dependencies
        /// </summary>
        private DependencyGraph dependencyGraph { get; set; }

        /// <summary>
        /// Holds the names of all of the non empty cells
        /// </summary>
        private Dictionary<string,Cell> nonEmptyCells { get; set; }

        /// <summary>
        /// holds the data of the most current cell being entered
        /// </summary>
        private object oldData { get; set; }

        /// <summary>
        /// performs the operation as described in the abstract file
        /// </summary>
        public override bool Changed { get; protected set; }

        /// <summary>
        /// returns the value in a cell if it is a number
        /// </summary>
        /// <param name="value">the cell which contains needed value</param>
        /// <returns>a double equivalent to the sent value</returns>
        private double Lookup(string value)
        {
            if(!nonEmptyCells.ContainsKey(value))
                throw new ArgumentException();
            if (!(nonEmptyCells[value].value is double))
                throw new ArgumentException();
            return (double)nonEmptyCells[value].value;
        }

        /// <summary>
        /// creates the data structures needed for the linked list which is a dictionary to hold the
        /// names of the cells and the cells.  A dependency graph object is used to store all 
        /// dependency 
        /// </summary>
        public Spreadsheet():
            this(s=>true,s=>s, "default")
        {
        }

        /// <summary>
        /// creates the data structures needed for the linked list which is a dictionary to hold the
        /// names of the cells and the cells.  A dependency graph object is used to store all 
        /// dependency  
        /// </summary>
        /// <param name="isValid"></param>
        /// <param name="normalize"></param>
        /// <param name="version"></param>
        public Spreadsheet(Func<string, bool> isValid, Func<string, string> normalize, string version):
            base(isValid,  normalize, version)
        {
            dependencyGraph = new DependencyGraph();
            nonEmptyCells = new Dictionary<string, Cell>();
            Changed = false;
        }

        /// <summary>
        /// creates the data structures needed for the linked list which is a dictionary to hold the
        /// names of the cells and the cells.  A dependency graph object is used to store all 
        /// dependency 
        /// </summary>
        /// <param name="filename"></param>
        /// <param name="isValid"></param>
        /// <param name="normalize"></param>
        /// <param name="version"></param>
        public Spreadsheet(string filename,Func<string, bool> isValid, Func<string, string> normalize, string version) :
            this(isValid, normalize, version)
        {
            Load(filename);
            Changed = false;
        }

        /// <summary>
        /// performs the operation as described in the abstract file.
        /// </summary>
        /// <returns></returns>
        public override IEnumerable<string> GetNamesOfAllNonemptyCells()
        {
            return nonEmptyCells.Keys.ToArray<string>();
        }

        /// <summary>
        /// performs the operation as described in the abstract file. If the name does not exist
        /// an empty string is returned 
        /// </summary>
        /// <param name="name"></param>
        /// <returns>the contents of a cell</returns>
        public override object GetCellContents(string name)
        {
            CheckIfNameIsValid(ref name);
            if(nonEmptyCells.ContainsKey(name))
                return nonEmptyCells[name].contents;
            return "";
        }

        /// <summary>
        /// performs the operation as described in the abstract file. If the name does not exist
        /// an empty string is returned 
        /// </summary>
        /// <param name="name"></param>
        /// <returns>the value of a cell</returns>
        public override object GetCellValue(string name)
        {
            CheckIfNameIsValid(ref name);
            if (nonEmptyCells.ContainsKey(name))
                return nonEmptyCells[name].value;
            return "";
        }

        /// <summary>
        /// performs the operation as described in the abstract file.
        /// </summary>
        /// <param name="name">cell's name</param>
        /// <param name="content">cell's content</param>
        /// <returns>all of the dependents of a cell</returns>
        public override ISet<string> SetContentsOfCell(string name, string content)
        {
            if (content == null)
                throw new ArgumentNullException("content cannot be null");

            CheckIfNameIsValid(ref name);
            ISet<string> data;

            //if the content entered is a double use "SetCellContents" for doubles
            double value;
            if (double.TryParse(content, out value))
                data = SetCellContents(name, value);
            //if the content entered is a formula use "SetCellContents" for formulas
            else if (content.Trim().Length > 0 && content.Trim()[0].Equals('='))
                data = SetCellContents(name, new Formula(content.Replace("=", ""),Normalize,IsValid));
            //if the content entered is a string use "SetCellContents" for strings
            else
                data = SetCellContents(name, content);

            //if data is null that means that a circular exception was thrown
            //this if will catch null data, then restore the old data and then rethrow a circular exception

            if (data == null)
            {
                if(oldData.GetType().Equals(typeof(double)))
                    data = SetCellContents(name, (double)oldData);
                else if (oldData.GetType().Equals(typeof(string)))
                    data = SetCellContents(name, (string)oldData);
                else 
                    data = SetCellContents(name, (Formula)oldData);
                throw new CircularException();
            }
            Changed = true;
            return data;
                
        }

        /// <summary>
        /// performs the operations as described in the abstract class file. If it is called and a 
        /// cell already exists, then the content of that cell with be changed along with the value
        /// </summary>
        /// <param name="name">cell's name</param>
        /// <param name="number">number to be stored in cell</param>
        /// <returns>all of the dependents of a cell</returns>
        protected override ISet<string> SetCellContents(string name, double number)
        {
            addCell(name, number,number);
            return CellsToRecalculate(name);
        }

        /// <summary>
        /// performs the operations as described in the abstract class file. If it is called and a 
        /// cell already exists, then the content of that cell with be changed along with the value
        /// </summary>
        /// <param name="name">name of the cell</param>
        /// <param name="formula">formula to be stored in the cell</param>
        /// <returns>all of the dependents of a cell</returns>
        protected override ISet<string> SetCellContents(string name, Formula formula)
        {
            addCell(name, formula, formula.Evaluate(Lookup));
            dependencyGraph.ReplaceDependents(name, formula.GetVariables());
            return CellsToRecalculate(name);
        }

        /// <summary>
        /// performs the operations as described in the abstract class file. If it is called and a 
        /// cell already exists, then the content of that cell with be changed along with the value.
        /// If an empty string is entered then the cell will not be created
        /// </summary>
        /// <param name="name">name of the cell</param>
        /// <param name="text">text to be stored in the cell</param>
        /// <returns>all of the dependents of a cell</returns>
        protected override ISet<string> SetCellContents(string name, string text)
        {
            if(text.Trim().Equals(""))
            {
                dependencyGraph.ReplaceDependents(name, new string[] { });
                nonEmptyCells.Remove(name);                   
            }
            else
            {
                addCell(name, text,text);
            }

            return CellsToRecalculate(name);
        }

        /// <summary>
        /// Adds a cell to the shreadsheet.  If the cell already exists then the data
        /// is changed and its dependees are removed. The value of the cell is then set 
        /// to the value sent.
        /// </summary>
        /// <param name="name">name of the cell</param>
        /// <param name="content">the cells content</param>
        /// <param name="value">the cells value</param>
        private void addCell(string name, object content, object value)
        {
            try
            {
                nonEmptyCells.Add(name, new Cell(content));
                oldData = "";
            }

            catch (ArgumentException)
            {
                oldData = nonEmptyCells[name].contents;
                nonEmptyCells[name].contents = content;
                dependencyGraph.ReplaceDependents(name, new string[] { });
            }
            nonEmptyCells[name].value = value;

        }

        /// <summary>
        /// finds all of the dependents of a cell recalculates them and then sends all
        /// of the recalculated cells
        /// </summary>
        /// <param name="name">name of the cell</param>
        /// <returns>all cells dependent on the cell</returns>
        private ISet<string> CellsToRecalculate(string name)
        {
            List<string> cellsToRecalculate;
            try { cellsToRecalculate = GetCellsToRecalculate(name).ToList(); }
            catch(CircularException) { return null;}
            
            cellsToRecalculate.RemoveAt(0);
            FillInDependents(cellsToRecalculate);
            cellsToRecalculate.Insert(0, name);
            return new HashSet<string>(cellsToRecalculate);
        }



        /// <summary>
        /// performs the operations as described in the abstract class file
        /// </summary>
        /// <param name="name">name of the cell</param>
        /// <returns>all of the cells dependent on the cell</returns>
        protected override IEnumerable<string> GetDirectDependents(string name)
        {
            CheckIfNameIsValid(ref name);
            return dependencyGraph.GetDependees(name);
        }

        /// <summary>
        /// takes all of the dependents of a cell and solves their equations
        /// </summary>
        /// <param dependents="dependents">cell name</param>
        private void FillInDependents(IEnumerable<string> dependents)
        {
            foreach(string name in dependents)
            {
                Cell cell = nonEmptyCells[name];
                Formula formula = (Formula)cell.contents;
                cell.value = formula.Evaluate(Lookup);
            }
        }

        /// <summary>
        /// saves the file
        /// </summary>
        /// <param name="filename">name of the file</param>
        public override void Save(string filename)
        {
            XmlWriter writer;

            try{writer = XmlWriter.Create(filename);}

            catch{throw new SpreadsheetReadWriteException("invalid file name");}

            try
            {
                using (writer)
                {
                    string formulaStarter;
                    writer.WriteStartDocument();
                    writer.WriteStartElement("spreadsheet");
                    writer.WriteAttributeString("version", Version);
                    foreach (KeyValuePair<string, Cell> cell in nonEmptyCells)
                    {
                        formulaStarter = string.Empty;
                        writer.WriteStartElement("cell");


                        if (cell.Value.contents.GetType() == typeof(Formula))
                            formulaStarter = "=";


                        writer.WriteElementString("name", cell.Key.ToString());
                        writer.WriteElementString("contents", formulaStarter + cell.Value.contents.ToString());
                        writer.WriteEndElement();

                    }
                    writer.WriteEndElement();
                    writer.WriteEndDocument();
                }
            }
            catch (Exception e) { throw new SpreadsheetReadWriteException(e.Message); }

            Changed = false;
        }

        /// <summary>
        /// gets the files and then returns the file version
        /// </summary>
        /// <param name="filename">name of the file</param>
        /// <returns>file version</returns>
        public override string GetSavedVersion(string filename)
        {
            XmlReader reader;

            try { reader = XmlReader.Create(filename); }

            catch { throw new SpreadsheetReadWriteException("invalid file name"); }

            try
            {
                using (reader)
                {
                    while (reader.Read())
                    {
                        if (reader.IsStartElement())
                        {

                            switch (reader.Name)
                            {

                                case "spreadsheet":
                                    return reader["version"];
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                throw new SpreadsheetReadWriteException(e.Message);
            }

            return "";
        }

        /// <summary>
        /// a load helper function for the constructor with four parameters
        /// loads an XML file and then fills in the cells
        /// </summary>
        /// <param name="filename">name of the file</param>
        private void Load(string filename)
        {
            XmlReader reader;

            try { reader = XmlReader.Create(filename); }

            catch { throw new SpreadsheetReadWriteException("invalid file name"); }

            string name;
            string content;
            try
            {
                using (reader)
                {
                    while (reader.Read())
                    {
                        if (reader.IsStartElement())
                        {

                            switch (reader.Name)
                            {

                                case "cell":
                                    reader.Read();
                                    reader.Read();
                                    name = reader.Value;
                                    reader.Read();
                                    reader.Read();
                                    reader.Read();
                                    content = reader.Value;
                                    SetContentsOfCell(name, content);
                                    break;

                                // Check if versions match
                                case "spreadsheet":
                                    if (reader["version"] != Version)
                                    {
                                        throw new SpreadsheetReadWriteException("Given version does not match saved version. Version of saved file must be the same as version parameter of constructor.");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                throw new SpreadsheetReadWriteException(e.Message);
            }

        }

    }

    /// <summary>
    /// a cell that hold content and bases the values off of the content
    /// </summary>
    public class Cell
    {
        /// <summary>
        /// holds the contents of the cell
        /// </summary>
        public object contents { get; set; }
        /// <summary>
        /// holds the value in the cell based on the content
        /// </summary>
        public object value { get; set; }

        /// <summary>
        /// creates a cell and sets its content
        /// </summary>
        /// <param name="contents">data that the cell will hold</param>
        public Cell(object contents)
        {
            this.contents = contents;
        }
    }
}
