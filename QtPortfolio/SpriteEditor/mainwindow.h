// Team Qt3.14
// Hannah Palma (u0791894), Matt Brownell (u0919206), Amit Athani (u0914565),
// Bradley Dawn (u0948671), Andrew Katsanevas (u0901239)
// Sprite Editor Assignment
// Due April 5th, 2016

#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTableWidget>
#include <QPushButton>
#include <QColorDialog>
#include <QLabel>
#include <QHBoxLayout>
#include <iostream>
#include <QHeaderView>
#include <QSpinBox>
#include <QComboBox>
#include <QFileDialog>
#include <iostream>
#include <sstream>
#include <string>
#include <QKeyEvent>
#include <QMovie>

using namespace std;

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    QTableWidget *tableWidget;
    string *frames;
    int numFrames;
    void changeSpinBox(int row, int col);
    QString openSaveDialog();
    QString openLoadDialog();
    QString openSaveGifDialog();
    int getCurrentFrameIndex();
    void setFrameBox(int frameCount);
    int fps;
    void displayGif();



private:
    Ui::MainWindow *ui;
    QColor *color;
    QColorDialog *colorDialog;
    QString toolMode;
    QHeaderView *verticalHeader;
    QHeaderView *horizontalHeader;
    QPushButton *displayButton;
    QPushButton *redButton;
    QPushButton *blueButton;
    QPushButton *yellowButton;
    QPushButton *greenButton;
    QPushButton *orangeButton;
    QPushButton *purpleButton;
    QPushButton *pinkButton;
    QPushButton *blackButton;
    QPushButton *grayButton;
    QPushButton *whiteButton;

    QPushButton *megaManLBButton;
    QPushButton *megaManDBButton;
    QPushButton *megaManSkinButton;
    QPushButton *megaManBlackButton;
    QPushButton *megaManWhiteButton;

    QPushButton *GBDarkestButton;
    QPushButton *GBDarkButton;
    QPushButton *GBLightButton;
    QPushButton *GBLightestButton;
    QPushButton *GBBlackButton;

    std::vector<QColor*> *recents;
    QPushButton *Recent1;
    QPushButton *Recent2;
    QPushButton *Recent3;
    QPushButton *Recent4;
    QPushButton *Recent5;

    QComboBox *paletteBox;
    int shapeWidth;
    int shapeHeight;
    int circleDiam;
    int rows;
    int columns;
    int scale;
    int maxScale;
    int currentIndex;

    string copyFrame;
    int penSize;

    void setUpCanvas();
    void setUpColorDialog();
    void setUpBasicColors();
    void setUpSizeSlider();
    void setUpToolButtons();
    void setUpFramePanel();
    void setUpPreviewPanel();
    void connectAllWidgets();

    void tryDraw(int x, int y);
    void tryErase(int x, int y);
    void tryDrawHM(int x, int y);
    void tryDrawVM(int x, int y);
    void setColor(QColor *newColor);
    void recursivelyFill(int x, int y, QColor startingColor);
    void keyPressEvent(QKeyEvent *event);
    void vFlip();
    void hFlip();
    void rotateFrame();
    void updateRecents(QColor *newColor);

    void disableFrameButtons();
    void enableFrameButtons();


private slots:
    void cellPaint(int, int);
    void moreColorsClicked();
    void changeColor();
    void shapePaint(int, int);
    void penClicked();
    void eraserClicked();
    void vmClicked();
    void hmClicked();
    void vertFlipClicked();
    void horizFlipClicked();
    void rotateClicked();

    void redClicked();
    void blueClicked();
    void yellowClicked();
    void greenClicked();
    void orangeClicked();
    void purpleClicked();
    void pinkClicked();
    void blackClicked();
    void grayClicked();
    void whiteClicked();
    void lbClicked();
    void dbClicked();
    void skinClicked();
    void GBDarkestClicked();
    void GBDarkClicked();
    void GBLightClicked();
    void GBLightestClicked();
    void recent1Clicked();
    void recent2Clicked();
    void recent3Clicked();
    void recent4Clicked();
    void recent5Clicked();
    void lmClicked();
    void rmClicked();
    void paletteChanged(int);
    void newFrameClicked();
    void nextFrameClicked();
    void previousFrameClicked();
    void copyFrameClicked();
    void pasteFrameClicked();
    void frameChanged(int);
    void penSizeChange(int);
    void bucketClicked();
    void circleClicked();
    void rectClicked();
    void diameterChanged(int);
    void heightChanged(int);
    void widthChanged(int);
    void newFileClicked();
    void confirmationClicked();
    void fpsChange();

};

#endif // MAINWINDOW_H
