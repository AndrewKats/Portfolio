/*
 * Written by Andrew Katsanevas and Amit Athani
 * 3/3/2016
 */

#include <QMediaPlayer>

#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    // Public methods
    explicit MainWindow(QWidget *parent = 0);
    void compRedButtonOn();
    void compRedButtonOff();
    void compBlueButtonOn();
    void compBlueButtonOff();
    void enableButton();
    void disableButton();
    void setLoseText();
    void hideLoseText();
    void displayScore(int);
    void enableStart();
    void disableStart();
    void setScore(QString);
    void setHighScore(QString score);
    void setLevel(QString);
    void labelPlayerTurn();
    void labelCompTurn();
    ~MainWindow();

    // Public members
    Ui::MainWindow *ui;
    QMediaPlayer *playerRed;
    QMediaPlayer *playerBlue;
    QMediaPlayer *playerLose;


signals:
    void startGame();
    void buttonChosenInGUI(int);


private slots:
    void on_startButton_clicked();
    void on_redButton_pressed();
    void on_blueButton_pressed();
    void on_redButton_released();
    void on_blueButton_released();
    void on_darkBox_clicked();
};

#endif // MAINWINDOW_H
