Name: William Garnes
Date: October 1, 2015

The PS2 and PS3 libraries being used are the same ones turned in for the homework assignments 

This project will create the spreadsheet class.  An abstract method has been provided and the spreadsheet
class will impliment the abstract methods.  A cell class has been created to store the contents and the values 
of the cell.  When new cell contents are set a new cell is created and the values will immidietly be added inside 
of the set method.  If a cell has any cells dependent on it, the set methods will also set the values of the
dependents cells as well.  If the contents of a cell are changed all of the cells that cell depends on will be
changed, the contents will change, the value will be changed, and the cells that depend on that cell will also
be updated.  If a cell that has data in it, recieves an empty string then the cell will be deleted from the 
nonEmptyCells dictionary.  The nonEmptyCells dictionary has hold all of the cells that have data in them. The
key is the cells name and the value is the particular cell.  A dependency graph object is created from PS2 to hold
all of the spreadsheet dependencies.
 
