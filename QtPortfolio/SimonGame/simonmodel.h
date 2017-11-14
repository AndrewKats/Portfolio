/*
 * Written by Andrew Katsanevas and Amit Athani
 * 3/3/2016
 */

#include <vector>
#include <mainwindow.h>
#ifndef SIMONMODEL_H
#define SIMONMODEL_H

#include <QObject>

class SimonModel : public QObject
{
    Q_OBJECT

public:
    explicit SimonModel(MainWindow* gui, QObject *parent = 0);

private:
    // Private members
    std::vector<int> pattern;
    MainWindow *gui;
    void addRandomColor(std::vector<int>&);
    int progress;
    unsigned int playerMove;
    unsigned int cpuMove;
    bool cpuTurn;
    int timeScale;
    int score;
    int highScore;
    int level;

    // Private methods
    void playCpu();
    void beginCpuTurn();
    void youLose();

public slots:
    void gameStart();
    void cpuBluePress();
    void cpuRedPress();
    void waitForNextOn();
    void buttonChosen(int);
    void beginCpuSlot();
};

#endif // SIMONMODEL_H
