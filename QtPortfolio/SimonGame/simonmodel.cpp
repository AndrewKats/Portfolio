/*
 * Written by Andrew Katsanevas and Amit Athani
 * 3/3/2016
 */

#include "simonmodel.h"
#include <iostream>
#include "mainwindow.h"
#include <QtGlobal>
#include <QTimer>
#include <QTime>

using namespace std;

/*
 * Constructor
 */
SimonModel::SimonModel(MainWindow *gui, QObject *parent) : QObject(parent) {
    this->gui = gui;
    // Initialize variables
    playerMove=0;
    cpuMove = 0;
    timeScale = 1000;
    cpuTurn = true;
    score=0;
    highScore=0;
    level=1;
}

/*
 * Slot that starts the game
 */
void SimonModel::gameStart() {
    // Set the score display
    score=0;
    gui->setScore(QString::number(score));
    // Set the level display
    level=1;
    gui->setLevel(QString::number(level));
    // Clear the pattern
    pattern.clear();
    // Clear the progress display
    progress = 0;
    this->gui->displayScore(progress);
    // Hide the lose message
    this->gui->hideLoseText();
    // Reset player and cpu move
    playerMove=0;
    cpuMove = 0;
    // Make it the computer's turn
    cpuTurn = true;
    addRandomColor(pattern);
    beginCpuTurn();
}

void SimonModel::beginCpuSlot()
{
    beginCpuTurn();
}

/*
 * The start of the computer's turn
 */
void SimonModel::beginCpuTurn() {
    progress = 0;
    gui->displayScore(progress);
    addRandomColor(pattern);
    gui->disableStart();
    gui->disableButton();
    gui->labelCompTurn();
    playCpu();
}

/*
 * Execute the computer's turn
 */
void SimonModel::playCpu() {

    // Keep player move at 0 until cpu turn is over
    if(cpuMove < pattern.size()) {
        playerMove = 0;
    }
    // If cpu turn is over, enable buttons
    else {
        playerMove = 0;
        cpuTurn = false;
        cpuMove = 0;
        gui->enableButton();
        gui->enableStart();
        gui->labelPlayerTurn();
        return;
    }
    // Light up the correct button
    if(pattern.at(cpuMove) == 0) {
        //cout << cpuMove << " RED" << endl;
        this->gui->compRedButtonOn();
        QTimer::singleShot(timeScale/(pattern.size()/2), this, SLOT(cpuRedPress()));
     }
    else {
        //cout << cpuMove << " BLUE" << endl;
        this->gui->compBlueButtonOn();
        QTimer::singleShot(timeScale/(pattern.size()/2), this, SLOT(cpuBluePress()));
    }
    // Increment computer's move
    cpuMove++;
}

/*
 * The computer presses the red button
 */
void SimonModel::cpuRedPress() {
    this->gui->compRedButtonOff();
    QTimer::singleShot(timeScale/(pattern.size()/2), this, SLOT(waitForNextOn()));
}

/*
 * The computer presses the blue button
 */
void SimonModel::cpuBluePress() {
    this->gui->compBlueButtonOff();
    QTimer::singleShot(timeScale/(pattern.size()/2), this, SLOT(waitForNextOn()));
}

/*
 * A slot for running a computer turn
 */
void SimonModel::waitForNextOn() {
    playCpu();
}

/*
 * Insert a new color into the pattern
 */
void SimonModel::addRandomColor(vector<int>& vec) {
    QTime time = time.currentTime();
    qsrand(time.msec() + (cpuMove + qrand()) * 13 * qrand() - 17);
    int i = qrand()%2;
    vec.push_back(i);
}

/*
 * Checks the player's choice of button
 * and determines if continue or lose
 */
void SimonModel::buttonChosen(int data)
{
    // If wrong choice, lose
    if(data != pattern.at(playerMove)) {
        youLose();
        return;
    }
    // If last turn correct, reset and start computer's turn
    if(playerMove == pattern.size() - 1) {
        score++;
        gui->setScore(QString::number(score));
        // Set high score if achieved
        if(score>highScore)
        {
            highScore=score;
            gui->setHighScore(QString::number(highScore));
        }
        cpuMove=0;
        cpuTurn=true;
        playerMove=0;

        level++;
        gui->setLevel(QString::number(level));

        gui->disableButton();
        gui->disableStart();
        progress=100;
        gui->displayScore(progress);
        QTimer::singleShot(400, this, SLOT(beginCpuSlot()));
        return;
    }
    // If correct and not last turn, update variables and displays
    score++;
    gui->setScore(QString::number(score));
    // Set high score if achieved
    if(score>highScore)
    {
        highScore=score;
        gui->setHighScore(QString::number(highScore));
    }
    playerMove++;
    progress = (int)((double)((double)playerMove /(double)pattern.size()) * 100);
    gui->displayScore(progress);
}

/*
 * Put the game in lose state
 */
void SimonModel::youLose()
{
    // Show lose message
    this->gui->setLoseText();
    // Disable red and blue buttons
    this->gui->disableButton();
    // Clear the patter
    pattern.clear();
    // Set variables for next game
    cpuTurn=true;
    cpuMove=0;
    playerMove=0;
}
