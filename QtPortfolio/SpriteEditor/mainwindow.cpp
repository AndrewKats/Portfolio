// Team Qt3.14
// Hannah Palma (u0791894), Matt Brownell (u0919206), Amit Athani (u0914565),
// Bradley Dawn (u0948671), Andrew Katsanevas (u0901239)
// Sprite Editor Assignment
// Due April 5th, 2016

#include "mainwindow.h"
#include "spritemodel.h"
#include "ui_mainwindow.h"

using namespace std;

// Constructor
MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setStyleSheet("background-color: lightGray;");

    ui->toolLabel->setText("    P");
    ui->frameBox->addItem(tr("Frame 1"));
    tableWidget = new QTableWidget(this);
    toolMode = "pen";
    penSize=1;
    rows = 25;
    columns = 30;
    scale = 20;
    maxScale = 20;
    shapeWidth = 3;
    shapeHeight = 3;
    circleDiam = 4;
    numFrames = 1;
    currentIndex = 0;

    recents = new vector<QColor*>();
    frames = new string[100];

    setUpCanvas();
    setUpColorDialog();
    setUpBasicColors();;
    connectAllWidgets();
}

// Destructor
MainWindow::~MainWindow()
{
    delete ui;
}

// Set GUI up with our canvas
void MainWindow::setUpCanvas()
{
    // Establishing the canvas
    tableWidget->setSelectionMode(QAbstractItemView::SingleSelection);
    tableWidget->setRowCount(rows);
    tableWidget->setColumnCount(columns);
    tableWidget->setEditTriggers(QAbstractItemView::NoEditTriggers);
    tableWidget->setStyleSheet("background-color: lightGray;");

    verticalHeader = tableWidget->verticalHeader();
    verticalHeader->sectionResizeMode(QHeaderView::Fixed);
    verticalHeader->setDefaultSectionSize(scale);
    horizontalHeader = tableWidget->horizontalHeader();
    horizontalHeader->sectionResizeMode(QHeaderView::Fixed);
    horizontalHeader->setDefaultSectionSize(scale);
    tableWidget->horizontalHeader()->setVisible(false);
    tableWidget->verticalHeader()->setVisible(false);

    tableWidget->setGeometry(300,100,columns*scale+5,rows*scale+5);

    // Populate the table cells
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            tableWidget->setItem(i, j, new QTableWidgetItem);
            tableWidget->item(i, j)->setBackgroundColor(*(new QColor(0,0,0,0)));
        }
    }

    // It looks weird, but this HAS to be separate :/
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            tableWidget->item(i, j)->setBackgroundColor(*(new QColor(0,0,0,0)));
        }
    }
}


// Initialize Color Dialog Box and it's coordinating buttons
void MainWindow::setUpColorDialog()
{
    // Establish ColorDialog and Color
    colorDialog = new QColorDialog(this);
    color = new QColor(0, 0, 0, 255);
    QString changeHighlight = QString::fromStdString("selection-background-color: rgba(") + QString::number(color->red()) + QString::fromStdString(",") + QString::number(color->green()) + QString::fromStdString(",") + QString::number(color->blue()) + QString::fromStdString(",") + QString::number(color->alpha()) + QString::fromStdString(");");
    tableWidget->setStyleSheet(changeHighlight);
}

// Draw basic color selection buttons
void MainWindow::setUpBasicColors()
{
    // Display Button
    displayButton = new QPushButton(this);
    displayButton->setGeometry(QRect(QPoint(125, 480), QSize(60,30)));
    displayButton->setStyleSheet("background-color: black;");
    displayButton->setToolTipDuration(4000);
    displayButton->setToolTip(QString("Current color selected."));
    // Red Button
    redButton = new QPushButton(this);
    redButton->setGeometry(QRect(QPoint(100, 520), QSize(20,20)));
    redButton->setStyleSheet("background-color: red;");
    // Blue Button
    blueButton = new QPushButton(this);
    blueButton->setGeometry(QRect(QPoint(122, 520), QSize(20,20)));
    blueButton->setStyleSheet("background-color: blue;");
    // Yellow Button
    yellowButton = new QPushButton(this);
    yellowButton->setGeometry(QRect(QPoint(144, 520), QSize(20,20)));
    yellowButton->setStyleSheet("background-color: yellow;");
    // Green Button
    greenButton = new QPushButton(this);
    greenButton->setGeometry(QRect(QPoint(166, 520), QSize(20,20)));
    greenButton->setStyleSheet("background-color: green;");
    // Orange Button
    orangeButton = new QPushButton(this);
    orangeButton->setGeometry(QRect(QPoint(188, 520), QSize(20,20)));
    orangeButton->setStyleSheet("background-color: chocolate;");
    // Purple Button
    purpleButton = new QPushButton(this);
    purpleButton->setGeometry(QRect(QPoint(100, 545), QSize(20,20)));
    purpleButton->setStyleSheet("background-color: purple;");
    // Pink Button
    pinkButton = new QPushButton(this);
    pinkButton->setGeometry(QRect(QPoint(122, 545), QSize(20,20)));
    pinkButton->setStyleSheet("background-color: hotpink;");
    // Black Button
    blackButton = new QPushButton(this);
    blackButton->setGeometry(QRect(QPoint(144, 545), QSize(20,20)));
    blackButton->setStyleSheet("background-color: black;");
    // Gray Button
    grayButton = new QPushButton(this);
    grayButton->setGeometry(QRect(QPoint(166, 545), QSize(20,20)));
    grayButton->setStyleSheet("background-color: gray;");
    // White Button
    whiteButton = new QPushButton(this);
    whiteButton->setGeometry(QRect(QPoint(188, 545), QSize(20,20)));
    whiteButton->setStyleSheet("background-color: white;");


    // Megaman light blue
    megaManLBButton = new QPushButton(this);
    megaManLBButton->setGeometry(QRect(QPoint(100, 520), QSize(20,20)));
    megaManLBButton->setStyleSheet("background-color: #15c2ed;");
    megaManLBButton->setVisible(false);
    // Megaman dark blue
    megaManDBButton = new QPushButton(this);
    megaManDBButton->setGeometry(QRect(QPoint(122, 520), QSize(20,20)));
    megaManDBButton->setStyleSheet("background-color: #2282d9;");
    megaManDBButton->setVisible(false);
    // Megaman skin
    megaManSkinButton = new QPushButton(this);
    megaManSkinButton->setGeometry(QRect(QPoint(144, 520), QSize(20,20)));
    megaManSkinButton->setStyleSheet("background-color: #fee998;");
    megaManSkinButton->setVisible(false);
    // Megaman black
    megaManBlackButton = new QPushButton(this);
    megaManBlackButton->setGeometry(QRect(QPoint(166, 520), QSize(20,20)));
    megaManBlackButton->setStyleSheet("background-color: black;");
    megaManBlackButton->setVisible(false);
    // Megaman white
    megaManWhiteButton = new QPushButton(this);
    megaManWhiteButton->setGeometry(QRect(QPoint(188, 520), QSize(20,20)));
    megaManWhiteButton->setStyleSheet("background-color: white;");
    megaManWhiteButton->setVisible(false);

    // GameBoy Darkest Green
    GBDarkestButton = new QPushButton(this);
    GBDarkestButton->setGeometry(QRect(QPoint(100, 520), QSize(20,20)));
    GBDarkestButton->setStyleSheet("background-color: #0f380f;");
    GBDarkestButton->setVisible(false);
    // GameBoy Dark Green
    GBDarkButton = new QPushButton(this);
    GBDarkButton->setGeometry(QRect(QPoint(122, 520), QSize(20,20)));
    GBDarkButton->setStyleSheet("background-color: #306230;");
    GBDarkButton->setVisible(false);
    // GameBoy Light Green
    GBLightButton = new QPushButton(this);
    GBLightButton->setGeometry(QRect(QPoint(144, 520), QSize(20,20)));
    GBLightButton->setStyleSheet("background-color: #8bac0f;");
    GBLightButton->setVisible(false);
    // GameBoy Lightest Green
    GBLightestButton = new QPushButton(this);
    GBLightestButton->setGeometry(QRect(QPoint(166, 520), QSize(20,20)));
    GBLightestButton->setStyleSheet("background-color: #9bbc0f;");
    GBLightestButton->setVisible(false);
    // GameBoy Black
    GBBlackButton = new QPushButton(this);
    GBBlackButton->setGeometry(QRect(QPoint(188, 520), QSize(20,20)));
    GBBlackButton->setStyleSheet("background-color: black;");
    GBBlackButton->setVisible(false);

    // Recent1
    Recent1 = new QPushButton(this);
    Recent1->setGeometry(QRect(QPoint(100, 570), QSize(20,20)));
    Recent1->setStyleSheet("background-color: transparent;");
    // Recent2
    Recent2 = new QPushButton(this);
    Recent2->setGeometry(QRect(QPoint(122, 570), QSize(20,20)));
    Recent2->setStyleSheet("background-color: transparent;");
    // Recent3
    Recent3 = new QPushButton(this);
    Recent3->setGeometry(QRect(QPoint(144, 570), QSize(20,20)));
    Recent3->setStyleSheet("background-color: transparent;");
    // Recent4
    Recent4 = new QPushButton(this);
    Recent4->setGeometry(QRect(QPoint(166, 570), QSize(20,20)));
    Recent4->setStyleSheet("background-color: transparent;");
    // Recent5
    Recent5 = new QPushButton(this);
    Recent5->setGeometry(QRect(QPoint(188, 570), QSize(20,20)));
    Recent5->setStyleSheet("background-color: transparent;");

    ui->paletteBox->addItem(tr("Standard"));
    ui->paletteBox->addItem(tr("GameBoy"));
    ui->paletteBox->addItem(tr("MegaMan"));
}

void MainWindow::connectAllWidgets()
{
    connect(ui->penSlider, SIGNAL(valueChanged(int)), this, SLOT(penSizeChange(int)));
    connect(ui->fpsSlider, SIGNAL(sliderReleased()), this, SLOT(fpsChange()));

    connect(ui->frameBox, SIGNAL(currentIndexChanged(int)), this, SLOT(frameChanged(int)));
    connect(ui->newFrameButton, SIGNAL(clicked(bool)), this, SLOT(newFrameClicked()));
    connect(ui->nextFrameButton, SIGNAL(clicked(bool)), this, SLOT(nextFrameClicked()));
    connect(ui->previousFrameButton, SIGNAL(clicked(bool)), this, SLOT(previousFrameClicked()));
    connect(ui->copyFrameButton, SIGNAL(clicked(bool)), this, SLOT(copyFrameClicked()));
    connect(ui->pasteFrameButton, SIGNAL(clicked(bool)), this, SLOT(pasteFrameClicked()));

    connect(ui->penButton, SIGNAL(clicked()), this, SLOT(penClicked()));
    connect(ui->eraserButton, SIGNAL(clicked()), this, SLOT(eraserClicked()));
    connect(ui->vmPenButton, SIGNAL(clicked()), this, SLOT(vmClicked()));
    connect(ui->hmPenButton, SIGNAL(clicked()), this, SLOT(hmClicked()));
    connect(ui->bucketButton, SIGNAL(clicked()), this, SLOT(bucketClicked()));
    connect(ui->rectangleButton, SIGNAL(clicked()), this, SLOT(rectClicked()));
    connect(ui->lmButton, SIGNAL(clicked(bool)), this, SLOT(lmClicked()));
    connect(ui->rmButton, SIGNAL(clicked(bool)), this, SLOT(rmClicked()));
    connect(ui->rectangleButton, SIGNAL(clicked()), this, SLOT(rectClicked()));

    connect(ui->rectHeight, SIGNAL(valueChanged(int)), this, SLOT(heightChanged(int)));
    connect(ui->rectWidth, SIGNAL(valueChanged(int)), this, SLOT(widthChanged(int)));
    connect(ui->diameter, SIGNAL(valueChanged(int)), this, SLOT(diameterChanged(int)));
    connect(ui->circleButton, SIGNAL(clicked()), this, SLOT(circleClicked()));

    connect(ui->vertFlipButton, SIGNAL(clicked()), this, SLOT(vertFlipClicked()));
    connect(ui->horizFlipButton, SIGNAL(clicked()), this, SLOT(horizFlipClicked()));
    connect(ui->rotateButton, SIGNAL(clicked()), this, SLOT(rotateClicked()));

    connect(tableWidget, SIGNAL(cellClicked(int,int)), this, SLOT(cellPaint(int, int)));
    connect(tableWidget, SIGNAL(cellEntered(int,int)), this, SLOT(cellPaint(int, int)));
    connect(tableWidget, SIGNAL(cellDoubleClicked(int,int)), this, SLOT(shapePaint(int,int)));

    connect(colorDialog, SIGNAL(colorSelected(QColor)), this, SLOT(changeColor()));
    connect(ui->moreColorsButton, SIGNAL(clicked()), this, SLOT(moreColorsClicked()));
    connect(ui->paletteBox, SIGNAL(currentIndexChanged(int)), this, SLOT(paletteChanged(int)));

    connect(redButton, SIGNAL(clicked()), this, SLOT(redClicked()));
    connect(blueButton, SIGNAL(clicked()), this, SLOT(blueClicked()));
    connect(yellowButton, SIGNAL(clicked()), this, SLOT(yellowClicked()));
    connect(greenButton, SIGNAL(clicked()), this, SLOT(greenClicked()));
    connect(orangeButton, SIGNAL(clicked()), this, SLOT(orangeClicked()));
    connect(purpleButton, SIGNAL(clicked()), this, SLOT(purpleClicked()));

    connect(pinkButton, SIGNAL(clicked()), this, SLOT(pinkClicked()));
    connect(blueButton, SIGNAL(clicked()), this, SLOT(blueClicked()));
    connect(blackButton, SIGNAL(clicked()), this, SLOT(blackClicked()));
    connect(grayButton, SIGNAL(clicked()), this, SLOT(grayClicked()));
    connect(whiteButton, SIGNAL(clicked()), this, SLOT(whiteClicked()));

    connect(megaManLBButton, SIGNAL(clicked()), this, SLOT(lbClicked()));
    connect(megaManDBButton, SIGNAL(clicked()), this, SLOT(dbClicked()));
    connect(megaManSkinButton, SIGNAL(clicked()), this, SLOT(skinClicked()));
    connect(megaManBlackButton, SIGNAL(clicked()), this, SLOT(blackClicked()));
    connect(megaManWhiteButton, SIGNAL(clicked()), this, SLOT(whiteClicked()));

    connect(GBDarkestButton, SIGNAL(clicked()), this, SLOT(GBDarkestClicked()));
    connect(GBDarkButton, SIGNAL(clicked()), this, SLOT(GBDarkClicked()));
    connect(GBLightButton, SIGNAL(clicked()), this, SLOT(GBLightClicked()));
    connect(GBLightestButton, SIGNAL(clicked()), this, SLOT(GBLightestClicked()));
    connect(GBBlackButton, SIGNAL(clicked()), this, SLOT(blackClicked()));

    connect(Recent1, SIGNAL(clicked()), this, SLOT(recent1Clicked()));
    connect(Recent2, SIGNAL(clicked()), this, SLOT(recent2Clicked()));
    connect(Recent3, SIGNAL(clicked()), this, SLOT(recent3Clicked()));
    connect(Recent4, SIGNAL(clicked()), this, SLOT(recent4Clicked()));
    connect(Recent5, SIGNAL(clicked()), this, SLOT(recent5Clicked()));

    SpriteModel *model = new SpriteModel(this, 10);
    connect(ui->actionSave_File, &QAction::triggered, model, &SpriteModel::saveAsText);
    connect(ui->actionLoad_File, &QAction::triggered, model, &SpriteModel::loadSavedSprite);
    connect(ui->actionExport_as_GIF, &QAction::triggered, model, &SpriteModel::saveAsGif);

    connect(ui->actionNew_File, &QAction::triggered, this, &MainWindow::newFileClicked);
    ui->actionNew_File->trigger();

    connect(ui->displayPreviewButton, &QPushButton::clicked, model, &SpriteModel::previewSprite);
}

// Method called by the model to display the current preview gif
void MainWindow::displayGif()
{
    int height = 150;
    int width = 150;
    int xAnchor = 1025 - (width/2);
    int yAnchor = 470;
    ui->previewLabel->setGeometry(QRect(QPoint(xAnchor,yAnchor), QSize(width,height)));

    QMovie *preview = new QMovie("temp.gif");
    ui->previewLabel->setMovie(preview);
    preview->start();
}


// Method to draw with Pen tool
void MainWindow::tryDraw(int x, int y)
{
    if(x<rows && x>=0 && y<columns && y>=0)
    {
        tableWidget->item(x,y)->setBackgroundColor(*color);
    }
}

// Method to draw with Vertical Mirror tool
void MainWindow::tryDrawVM(int x, int y)
{
    if(x<rows && x>=0 && y<columns && y>=0)
    {
        tableWidget->item(x,y)->setBackgroundColor(*color);
    }
    if(x<rows && (columns-(y+1))<columns && (columns-(y+1))>=0)
    {
        tableWidget->item(x,columns-(y+1))->setBackgroundColor(*color);
    }
}

// Method to draw with Horizontal Mirror tool
void MainWindow::tryDrawHM(int x, int y)
{
    if(x<rows && x>=0 && y<columns && y>=0)
    {
        tableWidget->item(x,y)->setBackgroundColor(*color);
    }
    if(y<columns && (rows-(x+1))<rows && (rows-(x+1))>=0)
    {
        tableWidget->item(rows-(x+1),y)->setBackgroundColor(*color);
    }
}

// Method to draw with Erase tool
void MainWindow::tryErase(int x, int y)
{
    if(x<rows && x>=0 && y<columns && y>=0)
    {
        tableWidget->item(x,y)->setBackgroundColor(*(new QColor(0, 0, 0, 0)));
    }
}

// Recursive method to fill all surrounding neighbors of same color with new selected color
void MainWindow::recursivelyFill(int x, int y, QColor startingColor)
{
    // First paint this square
    tableWidget->item(x, y)->setBackgroundColor(*color);

    // If neighbor 1 is in the canvas AND the same color, recurse
    if((x+1)<= rows-1 && (y) <= columns-1 &&
        tableWidget->item(x+1, y)->backgroundColor() == startingColor)
    {
        recursivelyFill(x+1, y, startingColor);
    }
    // Same check for neighbor 2
    if((x-1) >= 0 && (y) >= 0 &&
        tableWidget->item(x-1, y)->backgroundColor() == startingColor)
    {
        recursivelyFill(x-1, y, startingColor);
    }
    // Same check for neighbor 3
    if((x) <= rows-1 && (y+1) <= columns-1 &&
        tableWidget->item(x, y+1)->backgroundColor() == startingColor)
    {
        recursivelyFill(x, y+1, startingColor);
    }
    // Same check for neighbor 4
    if((x) >= 0 && (y-1) >= 0 &&
        tableWidget->item(x, y-1)->backgroundColor() == startingColor)
    {
        recursivelyFill(x, y-1, startingColor);
    }
}

// Vertically flip the frame
void MainWindow::vFlip()
{
    for(int i=0; i<columns; i++)
    {
        for(int j=0; j<rows/2; j++)
        {

            QColor *temp1 = new QColor(tableWidget->item(i,j)->backgroundColor());
            QColor *temp2 = new QColor(tableWidget->item(i,columns-(j+1))->backgroundColor());

            tableWidget->item(i,j)->setBackgroundColor(*temp2);
            tableWidget->item(i,columns-(j+1))->setBackgroundColor(*temp1);
        }
    }
}

// Horizontally flip the frame
void MainWindow::hFlip()
{
    //tableWidget->setStyleSheet("selection-background-color: rgba(0,0,0,0);");
    for(int j=0; j<columns; j++)
    {
        for(int i=0; i<rows/2; i++)
        {

            QColor *temp1 = new QColor(tableWidget->item(i,j)->backgroundColor());
            QColor *temp2 = new QColor(tableWidget->item(rows-(i+1),j)->backgroundColor());

            tableWidget->item(i,j)->setBackgroundColor(*temp2);
            tableWidget->item(rows-(i+1),j)->setBackgroundColor(*temp1);
        }
    }
}

// Rotate the frame 90 degrees
void MainWindow::rotateFrame()
{
    QColor *rColors[columns][rows];

    for(int i=0; i<columns; i++)
    {
        for(int j=0; j<rows; j++)
        {
            rColors[i][j] = new QColor(tableWidget->item(i,j)->backgroundColor());
        }
    }

    for(int i=0; i<columns; i++)
    {
        for(int j=0; j<rows; j++)
        {
            if(rows%2==1)
            {
                int col = ((rows/2)-j) + rows/2;
                int row = (i-(rows/2)) + rows/2;
                //if(col>columns-1 || row>rows-1)
                QColor *newColor = new QColor((rColors[col][row])->red(),(rColors[col][row])->green(),(rColors[col][row])->blue(),(rColors[col][row])->alpha());
                tableWidget->item(i,j)->setBackgroundColor(*newColor);
            }
        }
    }
}

// Set Current color
void MainWindow::setColor(QColor *newColor)
{
    color = newColor;
    displayButton->setStyleSheet("background-color: rgba(" + QString::number(color->red()) + QString::fromStdString(",") + QString::number(color->green()) + QString::fromStdString(",") + QString::number(color->blue()) + QString::fromStdString(",") + QString::number(color->alpha()) + QString::fromStdString(");"));
}

// Update Recent Color Palette
void MainWindow::updateRecents(QColor *newColor)
{
    recents->push_back(newColor);
    if(recents->size()>5)
    {
        recents->erase(recents->begin(), recents->begin()+1);
    }

    if(recents->size()>0)
    {
        Recent1->setStyleSheet("background-color: rgba(" + QString::number((recents->at(0))->red()) + QString::fromStdString(",") + QString::number((recents->at(0))->green()) + QString::fromStdString(",") + QString::number((recents->at(0))->blue()) + QString::fromStdString(",") + QString::number((recents->at(0))->alpha()) + QString::fromStdString(");"));

        if(recents->size()>1)
        {
            Recent2->setStyleSheet("background-color: rgba(" + QString::number((recents->at(1))->red()) + QString::fromStdString(",") + QString::number((recents->at(1))->green()) + QString::fromStdString(",") + QString::number((recents->at(1))->blue()) + QString::fromStdString(",") + QString::number((recents->at(1))->alpha()) + QString::fromStdString(");"));

            if(recents->size()>2)
            {
                Recent3->setStyleSheet("background-color: rgba(" + QString::number((recents->at(2))->red()) + QString::fromStdString(",") + QString::number((recents->at(2))->green()) + QString::fromStdString(",") + QString::number((recents->at(2))->blue()) + QString::fromStdString(",") + QString::number((recents->at(2))->alpha()) + QString::fromStdString(");"));

                if(recents->size()>3)
                {
                    Recent4->setStyleSheet("background-color: rgba(" + QString::number((recents->at(3))->red()) + QString::fromStdString(",") + QString::number((recents->at(3))->green()) + QString::fromStdString(",") + QString::number((recents->at(3))->blue()) + QString::fromStdString(",") + QString::number((recents->at(3))->alpha()) + QString::fromStdString(");"));

                    if(recents->size()>4)
                    {
                        Recent5->setStyleSheet("background-color: rgba(" + QString::number((recents->at(4))->red()) + QString::fromStdString(",") + QString::number((recents->at(4))->green()) + QString::fromStdString(",") + QString::number((recents->at(4))->blue()) + QString::fromStdString(",") + QString::number((recents->at(4))->alpha()) + QString::fromStdString(");"));
                    }
                }
            }
        }
    }
}

// Don't mess with stuff while we're configuring a new file
void MainWindow::disableFrameButtons()
{
    //ui->frameBox->setEnabled(false);
    ui->newFrameButton->setEnabled(false);
    ui->nextFrameButton->setEnabled(false);
    ui->previousFrameButton->setEnabled(false);
    ui->copyFrameButton->setEnabled(false);
    ui->pasteFrameButton->setEnabled(false);
    ui->displayPreviewButton->setEnabled(false);
    ui->penButton->setEnabled(false);
    ui->eraserButton->setEnabled(false);
    ui->bucketButton->setEnabled(false);
    ui->rotateButton->setEnabled(false);
    ui->hmPenButton->setEnabled(false);
    ui->vmPenButton->setEnabled(false);
    ui->rmButton->setEnabled(false);
    ui->lmButton->setEnabled(false);
    ui->rectangleButton->setEnabled(false);
    ui->rectHeight->setEnabled(false);
    ui->rectWidth->setEnabled(false);
    ui->circleButton->setEnabled(false);
    ui->diameter->setEnabled(false);
    ui->horizFlipButton->setEnabled(false);
    ui->vertFlipButton->setEnabled(false);
    ui->penSlider->setEnabled(false);
    ui->fpsSlider->setEnabled(false);
}

// Go ahead and mess with stuff
void MainWindow::enableFrameButtons()
{
    ui->newFrameButton->setEnabled(true);
    ui->nextFrameButton->setEnabled(true);
    ui->previousFrameButton->setEnabled(true);
    ui->copyFrameButton->setEnabled(true);
    ui->pasteFrameButton->setEnabled(true);
    ui->displayPreviewButton->setEnabled(true);
    ui->penButton->setEnabled(true);
    ui->eraserButton->setEnabled(true);
    ui->bucketButton->setEnabled(true);
    ui->rotateButton->setEnabled(true);
    ui->hmPenButton->setEnabled(true);
    ui->vmPenButton->setEnabled(true);
    ui->rmButton->setEnabled(true);
    ui->lmButton->setEnabled(true);
    ui->rectangleButton->setEnabled(true);
    ui->rectHeight->setEnabled(true);
    ui->rectWidth->setEnabled(true);
    ui->circleButton->setEnabled(true);
    ui->diameter->setEnabled(true);
    ui->horizFlipButton->setEnabled(true);
    ui->vertFlipButton->setEnabled(true);
    ui->penSlider->setEnabled(true);
    ui->fpsSlider->setEnabled(true);
}



// ---- HELPERS FOR THE MODEL ----

void MainWindow::changeSpinBox(int row, int col)
{
    ui->actionNew_File->trigger();
    ui->rowsSpinbox->setValue(row);
    ui->columnsSpinbox->setValue(col);
    ui->confirmation->click();
}


QString MainWindow::openSaveDialog()
{
    QString fileName = QFileDialog::getSaveFileName(this,
        tr("Select"), "./", tr("Sprie Files (*.ssp)"));
    return fileName;
}

QString MainWindow::openLoadDialog()
{
    QString fileName = QFileDialog::getOpenFileName(this,
        tr("Select"), "./", tr("Sprite Files (*.ssp)"));
    return fileName;
}

QString MainWindow::openSaveGifDialog()
{
    QString fileName = QFileDialog::getSaveFileName(this,
        tr("Select"), "./", tr("GIFs (*.gif)"));
    return fileName;
}


int MainWindow::getCurrentFrameIndex()
{
    return ui->frameBox->currentIndex();
}

void MainWindow::setFrameBox(int frameCount)
{
    ui->frameBox->addItem(QString("Frame ").append(QString::number(frameCount)));
}



// ---- SLOTS ----

// When editor has been opened, or new file has been created
void MainWindow::newFileClicked()
{
    disableFrameButtons();
    tableWidget->hide();
    ui->rowsLabel->show();
    ui->columnsLabel->show();
    ui->rowsSpinbox->show();
    ui->rowsSpinbox->setStyleSheet("background-color: white;");
    ui->columnsSpinbox->show();
    ui->columnsSpinbox->setStyleSheet("background-color: white;");
    ui->confirmation->show();

    numFrames = 1;
    while(ui->frameBox->count() > 1) {
        cout << ui->frameBox->count();
        ui->frameBox->removeItem(ui->frameBox->count() - 1);
    }
    connect(ui->confirmation, &QPushButton::clicked,this, &MainWindow::confirmationClicked);
}

// When new file has been formatted
void MainWindow::confirmationClicked()
{
    toolMode = "pen";
    penSize=1;
    fps = 1;
    rows = ui->rowsSpinbox->value();
    columns = ui->columnsSpinbox->value();
    int power;
    if (rows > columns)
        power = rows;
    else
        power = columns;
    int inc = 25;
    for (int i = 20; i > 4; i=i-2)
    {
        maxScale = i;
        scale = i;
        if (power <= inc)
            break;
        inc= inc +6;
    }

    toolMode = "pen";
    penSize=1;
    fps = 1;
    shapeWidth = 3;
    shapeHeight = 3;
    circleDiam = 4;
    numFrames = 1;
    currentIndex = 0;
    copyFrame = "";

    frames = new string[100];

    setUpCanvas();
    tableWidget->show();
    ui->rowsLabel->hide();
    ui->columnsLabel->hide();
    ui->rowsSpinbox->hide();
    ui->columnsSpinbox->hide();
    ui->confirmation->hide();
    ui->welcomeLabel->hide();
    ui->welcomeLabel2->hide();
    enableFrameButtons();
}

// Slot for receiving clicks on the canvas grid
void MainWindow::cellPaint(int x, int y)
{
    QString changeHighlight = QString::fromStdString("selection-background-color: rgba(") + QString::number(color->red()) +
                                                    QString::fromStdString(",") + QString::number(color->green()) +
                                                    QString::fromStdString(",") + QString::number(color->blue()) +
                                                    QString::fromStdString(",") + QString::number(color->alpha()) +
                                                    QString::fromStdString(");");
    tableWidget->setStyleSheet(changeHighlight);

    if(toolMode=="pen")
    {
        if(penSize>=1)
        {
            tryDraw(x,y);
        }
        if(penSize>=2)
        {
            tryDraw(x+1,y);
            tryDraw(x,y+1);
            tryDraw(x+1,y+1);
        }
        if(penSize>=3)
        {
            tryDraw(x+2,y);
            tryDraw(x,y+2);
            tryDraw(x+1,y+2);
            tryDraw(x+2,y+1);
            tryDraw(x+2,y+2);
        }
        if(penSize==4)
        {
            tryDraw(x+3,y);
            tryDraw(x,y+3);
            tryDraw(x+1,y+3);
            tryDraw(x+3,y+1);
            tryDraw(x+2,y+3);
            tryDraw(x+3,y+2);
            tryDraw(x+3,y+3);
        }
    }

    else if(toolMode=="eraser")
    {
        tableWidget->setStyleSheet("selection-background-color: rgba(0,0,0,0);");
        tableWidget->item(x,y)->setBackgroundColor(*(new QColor(0, 0, 0, 0)));

        if(penSize>=1)
        {
            tryErase(x,y);
        }
        if(penSize>=2)
        {
            tryErase(x+1,y);
            tryErase(x,y+1);
            tryErase(x+1,y+1);
        }
        if(penSize>=3)
        {
            tryErase(x+2,y);
            tryErase(x,y+2);
            tryErase(x+1,y+2);
            tryErase(x+2,y+1);
            tryErase(x+2,y+2);
        }
        if(penSize==4)
        {
            tryErase(x+3,y);
            tryErase(x,y+3);
            tryErase(x+1,y+3);
            tryErase(x+3,y+1);
            tryErase(x+2,y+3);
            tryErase(x+3,y+2);
            tryErase(x+3,y+3);
        }
    }

    else if(toolMode=="verticalmirrorpen")
    {
        if(penSize>=1)
        {
            tryDrawVM(x,y);
        }
        if(penSize>=2)
        {
            tryDrawVM(x+1,y);
            tryDrawVM(x,y+1);
            tryDrawVM(x+1,y+1);
        }
        if(penSize>=3)
        {
            tryDrawVM(x+2,y);
            tryDrawVM(x,y+2);
            tryDrawVM(x+1,y+2);
            tryDrawVM(x+2,y+1);
            tryDrawVM(x+2,y+2);
        }
        if(penSize==4)
        {
            tryDrawVM(x+3,y);
            tryDrawVM(x,y+3);
            tryDrawVM(x+1,y+3);
            tryDrawVM(x+3,y+1);
            tryDrawVM(x+2,y+3);
            tryDrawVM(x+3,y+2);
            tryDrawVM(x+3,y+3);
        }
    }

    else if(toolMode=="horizontalmirrorpen")
    {
        if(penSize>=1)
        {
            tryDrawHM(x,y);
        }
        if(penSize>=2)
        {
            tryDrawHM(x+1,y);
            tryDrawHM(x,y+1);
            tryDrawHM(x+1,y+1);
        }
        if(penSize>=3)
        {
            tryDrawHM(x+2,y);
            tryDrawHM(x,y+2);
            tryDrawHM(x+1,y+2);
            tryDrawHM(x+2,y+1);
            tryDrawHM(x+2,y+2);
        }
        if(penSize==4)
        {
            tryDrawHM(x+3,y);
            tryDrawHM(x,y+3);
            tryDrawHM(x+1,y+3);
            tryDrawHM(x+3,y+1);
            tryDrawHM(x+2,y+3);
            tryDrawHM(x+3,y+2);
            tryDrawHM(x+3,y+3);
        }
    }

    else if(toolMode=="bucket")
    {
        QColor startingColor = tableWidget->item(x, y)->backgroundColor();

        if (startingColor == *color)
            return;

        recursivelyFill(x, y, startingColor);
    }

    ui->magicButton->setFocus();
}

// Method for painting a particular shape
void MainWindow::shapePaint(int x, int y)
{
    if(toolMode=="rect")
    {
        for (int i = 1; i < shapeWidth-1; i++)
        {
                tryDraw(x,y+i);
                tryDraw(x+shapeHeight-1,y+i);
        }

        for (int j = 0; j < shapeHeight; j++)
        {
                tryDraw(x+j,y);
                tryDraw(x+j,y+shapeWidth-1);
        }

    }
    else if (toolMode == "circle")
    {
        QString changeHighlight = QString::fromStdString("selection-background-color: rgba(") + QString::number(color->red()) + QString::fromStdString(",") + QString::number(color->green()) + QString::fromStdString(",") + QString::number(color->blue()) + QString::fromStdString(",") + QString::number(color->alpha()) + QString::fromStdString(");");
        tableWidget->setStyleSheet(changeHighlight);
        for (int i = 0; i < circleDiam/2; i++)
        {
                tryDraw(x-i,y+i);
                tryDraw(x-i, (y+(circleDiam-1))-i);
                tryDraw(x+1+i,y+i);
                tryDraw(x+1+i,(y+(circleDiam-1))-i);
        }
    }
}

// When the height of the rectangle tool is changed
void MainWindow::heightChanged(int y)
{
    if (y < 1)
    {
        y = 1;
        ui->rectHeight->setValue(1);
    }
    else if (y > rows)
    {
        y = rows;
        ui->rectHeight->setValue(rows);
    }
    else
        shapeHeight = y;
}

// When the width of the rectangle tool is changed
void MainWindow::widthChanged(int x)
{
    if (x < 1)
    {
        x = 1;
        ui->rectWidth->setValue(1);
    }
    else if (x > columns)
    {
        x = columns;
        ui->rectWidth->setValue(columns);
    }
    else
        shapeWidth = x;
}

// When the diameter of the circle tool is changed
void MainWindow::diameterChanged(int d)
{
    circleDiam = d;
}



// ---- TOOL SLOTS ----

void MainWindow::penClicked()
{
    toolMode = "pen";
    ui->toolLabel->setText("   P");
}

void MainWindow::eraserClicked()
{
    toolMode = "eraser";
    ui->toolLabel->setText("   E");
}

void MainWindow::vmClicked()
{
    toolMode = "verticalmirrorpen";
    ui->toolLabel->setText("   VM");
}

void MainWindow::hmClicked()
{
    toolMode = "horizontalmirrorpen";
    ui->toolLabel->setText("   HM");
}

// Zoom out
void MainWindow::lmClicked()
{
    if (scale == 5)
        return;
    scale = scale - 5;
    horizontalHeader->setDefaultSectionSize(scale);
    verticalHeader->setDefaultSectionSize(scale);
    tableWidget->setGeometry(300,100,columns*scale+5,rows*scale+5);
}

// Zoom in
void MainWindow::rmClicked()
{
    if (scale == maxScale)
        return;
    scale = scale + 5;
    horizontalHeader->setDefaultSectionSize(scale);
    verticalHeader->setDefaultSectionSize(scale);
    tableWidget->setGeometry(300,100,columns*scale+5,rows*scale+5);
}

void MainWindow::bucketClicked()
{
    toolMode = "bucket";
    ui->toolLabel->setText("   B");
}

void MainWindow::rectClicked()
{
    toolMode = "rect";
    ui->toolLabel->setText("   R");
}

void MainWindow::circleClicked()
{
    toolMode = "circle";
    ui->toolLabel->setText("   C");
}

void MainWindow::vertFlipClicked()
{
    vFlip();
}

void MainWindow::horizFlipClicked()
{
    hFlip();
}

void MainWindow::rotateClicked()
{
    rotateFrame();
}

// Slider to change pen size
void MainWindow::penSizeChange(int size)
{
    ui->penSizeLabel->setText(QString::number(size));
    penSize = size;
}

// Slider to change gif fps
void MainWindow::fpsChange()
{
    fps = ui->fpsSlider->value();
    ui->fpsLabel->setText(QString::number(fps));

    ui->displayPreviewButton->clicked();
}

void MainWindow::newFrameClicked()
{
    currentIndex++;
    numFrames++;
    ui->frameBox->addItem(QString("Frame ").append(QString::number(numFrames)));

    // Saving the last frame
    string frame = "";
    ostringstream oss;
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            QColor current = tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frame << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }

    // Creating new frame
    frames[ui->frameBox->currentIndex()] = oss.str();

    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            tableWidget->item(i,j)->setBackgroundColor(*(new QColor(0, 0, 0, 0)));
        }
    }

    ui->frameBox->setCurrentIndex(numFrames-1);
}

// Update canvas to show next table in vector
void MainWindow::nextFrameClicked()
{
    if (ui->frameBox->currentIndex() == numFrames-1)
        return;

    // Saving the previous frame
    currentIndex++;
    string frameSave = "";
    ostringstream oss;
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            QColor current = tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frameSave << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }

    frames[ui->frameBox->currentIndex()] = oss.str();

    int row = 0;
    string frame = frames[ui->frameBox->currentIndex()+1];
    istringstream stream(frame);
    string line;

    // Get next frame and display it
    while(getline(stream, line))
    {
        istringstream lineStream(line);
        for (int i = 0; i < columns; i++)
        {
            int red;
            lineStream >> red;
            lineStream.clear();
            int green;
            lineStream >> green;
            lineStream.clear();
            int blue;
            lineStream >> blue;
            lineStream.clear();
            int alpha;
            lineStream >> alpha;
            lineStream.clear();
            tableWidget->item(row,i)->setBackgroundColor(*(new QColor(red, green, blue, alpha)));
        }
        row++;
    }

    ui->frameBox->setCurrentIndex(ui->frameBox->currentIndex()+1);
}

// Update canvas to show previous table in vector
void MainWindow::previousFrameClicked()
{
    if (ui->frameBox->currentIndex() == 0)
        return;

    // Saving the current frame
    currentIndex--;
    string frameSave = "";
    ostringstream oss;
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            QColor current = tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frameSave << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }

    frames[ui->frameBox->currentIndex()] = oss.str();

    int row = 0;
    string frame = frames[ui->frameBox->currentIndex()-1];
    istringstream stream(frame);
    string line;

    // Getting the previous frame and displaying it
    while(getline(stream, line))
    {
        istringstream lineStream(line);
        for (int i = 0; i < columns; i++)
        {
            int red;
            lineStream >> red;
            lineStream.clear();
            int green;
            lineStream >> green;
            lineStream.clear();
            int blue;
            lineStream >> blue;
            lineStream.clear();
            int alpha;
            lineStream >> alpha;
            lineStream.clear();
            tableWidget->item(row,i)->setBackgroundColor(*(new QColor(red, green, blue, alpha)));
        }
        row++;
    }

    ui->frameBox->setCurrentIndex(ui->frameBox->currentIndex()-1);
}

// Slot for dropdown frame box index changes
void MainWindow::frameChanged(int newIndex)
{
    int row = 0;
    string frame = frames[newIndex];
    istringstream stream(frame);
    string line;

    // Saving the current frame
    string frameSave = "";
    ostringstream oss;
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            QColor current = tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frameSave << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }

    // Getting the new frame and displaying it
    frames[currentIndex] = oss.str();
    currentIndex = newIndex;

    while(getline(stream, line))
    {
        istringstream lineStream(line);
        for (int i = 0; i < columns; i++)
        {
            int red;
            lineStream >> red;
            lineStream.clear();
            int green;
            lineStream >> green;
            lineStream.clear();
            int blue;
            lineStream >> blue;
            lineStream.clear();
            int alpha;
            lineStream >> alpha;
            lineStream.clear();
            tableWidget->item(row,i)->setBackgroundColor(*(new QColor(red, green, blue, alpha)));
        }
        row++;
    }
}

// Copy the current index table widget to a member variable and
// Wait for it to be pasted later (once user has moved to new location)
void MainWindow::copyFrameClicked()
{
    string frame = "";
    ostringstream oss;
    for (int i = 0; i<rows; i++)
    {
        for (int j = 0; j<columns; j++)
        {
            QColor current = tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frame << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }
    copyFrame = oss.str();
}

// Replaces the current frame with the copied frame
void MainWindow::pasteFrameClicked()
{
    int row = 0;
    string frame = copyFrame;
    istringstream stream(frame);
    string line;

    while(getline(stream, line))
    {
        istringstream lineStream(line);
        for (int i = 0; i < columns; i++)
        {
            int red;
            lineStream >> red;
            lineStream.clear();
            int green;
            lineStream >> green;
            lineStream.clear();
            int blue;
            lineStream >> blue;
            lineStream.clear();
            int alpha;
            lineStream >> alpha;
            lineStream.clear();
            tableWidget->item(row,i)->setBackgroundColor(*(new QColor(red, green, blue, alpha)));
        }
        row++;
    }
}



// ---- COLOR PALETTE SLOTS ----

void MainWindow::redClicked()
{
    setColor(new QColor(255, 0, 0, 255));
}

void MainWindow::blueClicked()
{
    setColor(new QColor(0, 0, 255, 255));
}

void MainWindow::yellowClicked()
{
    setColor(new QColor(255, 255, 0, 255));
}

void MainWindow::greenClicked()
{
    setColor(new QColor(0, 128, 0, 255));
}

void MainWindow::orangeClicked()
{
    setColor(new QColor(210, 105, 30, 255));
}

void MainWindow::purpleClicked()
{
    setColor(new QColor(128, 0, 128, 255));
}

void MainWindow::pinkClicked()
{
    setColor(new QColor(255, 105, 180, 255));
}

void MainWindow::blackClicked()
{
    setColor(new QColor(0, 0, 0, 255));
}

void MainWindow::grayClicked()
{
    setColor(new QColor(128, 128, 128, 255));
}

void MainWindow::whiteClicked()
{
    setColor(new QColor(255, 255, 255, 255));
}

void MainWindow::lbClicked()
{
    setColor(new QColor(21, 194, 237, 255));
}

void MainWindow::dbClicked()
{
    setColor(new QColor(34, 130, 217, 255));
}

void MainWindow::skinClicked()
{
    setColor(new QColor(254, 233, 152, 255));
}

void MainWindow::GBDarkestClicked()
{
    setColor(new QColor(15, 56, 15, 255));
}

void MainWindow::GBDarkClicked()
{
    setColor(new QColor(48, 98, 48, 255));
}

void MainWindow::GBLightClicked()
{
    setColor(new QColor(139, 172, 15, 255));
}

void MainWindow::GBLightestClicked()
{
    setColor(new QColor(155, 188, 15, 255));
}

void MainWindow::recent1Clicked()
{
    if(recents->size() > 0)
    {
        setColor(recents->at(0));
    }
}

void MainWindow::recent2Clicked()
{
    if(recents->size() > 1)
    {
        setColor(recents->at(1));
    }
}

void MainWindow::recent3Clicked()
{
    if(recents->size() > 2)
    {
        setColor(recents->at(2));
    }
}

void MainWindow::recent4Clicked()
{
    if(recents->size() > 3)
    {
        setColor(recents->at(3));
    }
}

void MainWindow::recent5Clicked()
{
    if(recents->size() > 4)
    {
        setColor(recents->at(4));
    }
}

void MainWindow::changeColor()
{
    setColor(new QColor(colorDialog->selectedColor()));

    updateRecents(new QColor(colorDialog->selectedColor()));

    colorDialog->hide();

    ui->magicButton->setFocus();
}

void MainWindow::paletteChanged(int newPalette)
{
    if(newPalette==0)
    {
        redButton->setVisible(true);
        blueButton->setVisible(true);
        yellowButton->setVisible(true);
        greenButton->setVisible(true);
        orangeButton->setVisible(true);
        purpleButton->setVisible(true);
        pinkButton->setVisible(true);
        blackButton->setVisible(true);
        grayButton->setVisible(true);
        whiteButton->setVisible(true);

        megaManLBButton->setVisible(false);
        megaManDBButton->setVisible(false);
        megaManSkinButton->setVisible(false);
        megaManBlackButton->setVisible(false);
        megaManWhiteButton->setVisible(false);

        GBDarkestButton->setVisible(false);
        GBDarkButton->setVisible(false);
        GBLightButton->setVisible(false);
        GBLightestButton->setVisible(false);
        GBBlackButton->setVisible(false);

    }
    if(newPalette==1)
    {
        redButton->setVisible(false);
        blueButton->setVisible(false);
        yellowButton->setVisible(false);
        greenButton->setVisible(false);
        orangeButton->setVisible(false);
        purpleButton->setVisible(false);
        pinkButton->setVisible(false);
        blackButton->setVisible(false);
        grayButton->setVisible(false);
        whiteButton->setVisible(false);

        megaManLBButton->setVisible(false);
        megaManDBButton->setVisible(false);
        megaManSkinButton->setVisible(false);
        megaManBlackButton->setVisible(false);
        megaManWhiteButton->setVisible(false);

        GBDarkestButton->setVisible(true);
        GBDarkButton->setVisible(true);
        GBLightButton->setVisible(true);
        GBLightestButton->setVisible(true);
        GBBlackButton->setVisible(true);
    }
    if(newPalette==2)
    {
        redButton->setVisible(false);
        blueButton->setVisible(false);
        yellowButton->setVisible(false);
        greenButton->setVisible(false);
        orangeButton->setVisible(false);
        purpleButton->setVisible(false);
        pinkButton->setVisible(false);
        blackButton->setVisible(false);
        grayButton->setVisible(false);
        whiteButton->setVisible(false);

        megaManLBButton->setVisible(true);
        megaManDBButton->setVisible(true);
        megaManSkinButton->setVisible(true);
        megaManBlackButton->setVisible(true);
        megaManWhiteButton->setVisible(true);

        GBDarkestButton->setVisible(false);
        GBDarkButton->setVisible(false);
        GBLightButton->setVisible(false);
        GBLightestButton->setVisible(false);
        GBBlackButton->setVisible(false);
    }
}

// Slot for opening the color pallette from moreColors
void MainWindow::moreColorsClicked()
{
     // Setup
    //this->setCentralWidget((colorDialog));
    colorDialog->setWindowFlags((Qt::Widget));
    colorDialog->setOptions(QColorDialog::DontUseNativeDialog);// | QColorDialog::NoButtons);

    colorDialog->show();
}

// For hotkeys
void MainWindow::keyPressEvent(QKeyEvent *event)
{
    if(event->key() == Qt::Key_P)
    {
        toolMode = "pen";
        ui->toolLabel->setText("   P");
    }

    if(event->key() == Qt::Key_E)
    {
        toolMode = "eraser";
        ui->toolLabel->setText("   E");
    }


    if(event->key() == Qt::Key_V)
    {
        toolMode = "verticalmirrorpen";
        ui->toolLabel->setText("   VM");
    }

    if(event->key() == Qt::Key_H)
    {
        toolMode = "horizontalmirrorpen";
        ui->toolLabel->setText("   HM");
    }

    if(event->key() == Qt::Key_C)
    {
        toolMode = "circle";
        ui->toolLabel->setText("   C");
    }

    if(event->key() == Qt::Key_B)
    {
        toolMode = "bucket";
        ui->toolLabel->setText("   B");
    }

    if(event->key() == Qt::Key_R)
    {
        toolMode = "rect";
        ui->toolLabel->setText("   R");
    }

}
