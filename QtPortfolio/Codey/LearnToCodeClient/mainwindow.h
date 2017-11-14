#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QPushButton>
#include <QTimer>
#include "model.h"
#include "user.h"
#include "world.h"


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    Model *model;
    World* myWorld;

    // SEQUENCE:
    // 0 : forward
    // 1 : jump
    // 2 : wait
    // 3 : if
    // 4 : for (loop)
    // 9 : remove last command
    QVector<int> sequence;
    QVector<QPushButton*> sequenceButtons;

    int if1Action = -1;
    int if12Action = -1;
    int if2Action = -1;
    int if22Action = -1;
    int loop1Action = -1;
    int loop2Action = -1;
    int loop3Action = -1;

    int index = 0;
    int currLevel = 0;


    void connectEverything();
    void switchToHomeMode();
    void switchToLoginMode();
    void switchToPlayMode();
    void clearSequence();
    void runSwitch(int action);


signals:
    void setGameState(int);
    void forward();

public slots:
    // buttons
    void loginClicked();
    void signUpClicked();
    void signUpFinalClicked();
    void on_cancelButton_Clicked();
    void logoutClicked();
    void homeClicked();

    // model interaction
    void loginFailed();
    void loginSucceeded();
    void createUserFailed();
    void noSuchTeacher();
    void createUserSucceeded();

    //void saveFailed();
    //void saveSucceeded();
    void connectionFailed();
    void showStats();
    void reloadLevel();

private slots:
    // Gameplay commands
    void on_forwardButton_clicked();
    void on_jumpButton_clicked();
    void on_waitButton_clicked();
    void on_continueButton_clicked();
    void on_ifButton_clicked();
    void on_loopButton_clicked();
    void on_removeLastButton_clicked();
    void on_runButton_clicked();
    void on_restartButton_clicked();
    void on_yesBox_clicked();
    void on_noBox_clicked();

    // Command structures
    void on_if1_clicked();
    void on_if2_clicked();
    void on_loop1_clicked();
    void on_loop2_clicked();
    void on_loop3_clicked();
    void nextSequence();

    // Home level selection
    void on_level1Button_clicked();
    void on_level2Button_clicked();
    void on_level3Button_clicked();
    void on_level4Button_clicked();
    void on_level5Button_clicked();
    void on_level6Button_clicked();
    void on_level7Button_clicked();
    void on_level8Button_clicked();
    void on_level9Button_clicked();

    void on_if12_clicked();
    void on_if22_clicked();
};

#endif // MAINWINDOW_H
