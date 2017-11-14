/*
 * Written by Andrew Katsanevas and Amit Athani
 * 3/3/2016
 */

#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include <QLabel>
#include <QMediaPlayer>

using namespace std;

/*
 * Constructor
 */
MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow) {
    ui->setupUi(this);

    // Change some details of the GUI
    ui->loseLabel->setVisible(false);
    ui->turnLabel->setVisible(false);
    ui->redButton->setStyleSheet("background-color:white");
    ui->blueButton->setStyleSheet("background-color:white");
    ui->centralWidget->setStyleSheet("background-color:lightgray");
    ui->startButton->setStyleSheet("background-color:darkgray");

    // Sounds
    playerRed = new QMediaPlayer();
    playerRed->setMedia(QUrl("qrc:/audio/Red blip.mp3"));

    playerBlue = new QMediaPlayer();
    playerBlue->setMedia(QUrl("qrc:/audio/Blue blip.mp3"));

    playerLose = new QMediaPlayer();
    playerLose->setMedia(QUrl("qrc:/audio/Lose.mp3"));
}

/*
 * Destructor
 */
MainWindow::~MainWindow() {
    delete ui;
}

/*
 * When start button is clicked, change its text and emit signal
 */
void MainWindow::on_startButton_clicked() {
    if(ui->startButton->text() == "Start")
        ui->startButton->setText("Restart");

    // Emit signal
    emit startGame();
}

/*
 * When the computer turns on red button, change color and play sound
 */
void MainWindow::compRedButtonOn() {
    ui->redButton->setStyleSheet("background-color:red");

    // Only play sound if in Sound mode
    if(ui->soundToggle->isChecked())
    {        
        if(playerRed->state() == QMediaPlayer::PlayingState)
            playerRed->setPosition(0);
        else if (playerRed->state() == QMediaPlayer::StoppedState)
            playerRed->play();
    }
}

/*
 * When computer turns off red button, change color back to white
 */
void MainWindow::compRedButtonOff() {
    ui->redButton->setStyleSheet("background-color:white");
}

/*
 * When the computer turns on blue button, change color and play sound
 */
void MainWindow::compBlueButtonOn() {
    ui->blueButton->setStyleSheet("background-color:blue");

    // Only play sound if in Sound mode
    if(ui->soundToggle->isChecked())
    {

        if(playerBlue->state() == QMediaPlayer::PlayingState)
                playerBlue->setPosition(0);
        else if (playerBlue->state() == QMediaPlayer::StoppedState)
                playerBlue->play();
    }

}

/*
 * When computer turns off blue button, change color back to white
 */
void MainWindow::compBlueButtonOff() {
    ui->blueButton->setStyleSheet("background-color:white");
}

/*
 * Enables both the red and blue buttons
 */
void MainWindow::enableButton() {
    ui->redButton->setEnabled(true);
    ui->blueButton->setEnabled(true);
}

/*
 * Disables both the red and blue buttons
 */
void MainWindow::disableButton() {
    ui->blueButton->setEnabled(false);
    ui->redButton->setEnabled(false);
}

/*
 * When the player presses the red button, make it red
 */
void MainWindow::on_redButton_pressed() {
    ui->redButton->setStyleSheet("background-color:red");
}

/*
 * When the player presses the blue button, make it blue
 */
void MainWindow::on_blueButton_pressed() {
    ui->blueButton->setStyleSheet("background-color:blue");
}

/*
 * When the player releases the red button, change color back
 * to white, play sound, and emit signal.
 */
void MainWindow::on_redButton_released() {
    ui->redButton->setStyleSheet("background-color:white");

    // Only play sound if in Sound mode
    if(ui->soundToggle->isChecked())
    {
        if(playerRed->state() == QMediaPlayer::PlayingState)
            playerRed->setPosition(0);
        else if (playerRed->state() == QMediaPlayer::StoppedState)
            playerRed->play();
    }

    // Emit signal
    emit buttonChosenInGUI(0);

}

/*
 * When the player releases the blue button, change color back
 * to white, play sound, and emit signal.
 */
void MainWindow::on_blueButton_released() {
    ui->blueButton->setStyleSheet("background-color:white");

    // Only play sound if in Sound mode
    if(ui->soundToggle->isChecked())
    {
        if(playerBlue->state() == QMediaPlayer::PlayingState)
                playerBlue->setPosition(0);
        else if (playerBlue->state() == QMediaPlayer::StoppedState)
                playerBlue->play();
    }

    // Emit signal
    emit buttonChosenInGUI(1);
}

/*
 * When the game is lost, display message, change start text back,
 * and play lose sound.
 */
void MainWindow::setLoseText() {
    ui->turnLabel->setVisible(false);
    ui->loseLabel->setVisible(true);
    ui->startButton->setText("Start");

    // Only play sound if in Sound mode
    if(ui->soundToggle->isChecked())
    {    
        if(playerLose->state() == QMediaPlayer::PlayingState)
                playerLose->setPosition(0);
        else if (playerLose->state() == QMediaPlayer::StoppedState)
                playerLose->play();
    }

}

/*
 * When the game is not lost, hide lose message
 * and Start button to Restart button
 */
void MainWindow::hideLoseText() {
    ui->loseLabel->setVisible(false);
    ui->startButton->setText("Restart");
}

/*
 * Displays the given level progress
 */
void MainWindow::displayScore(int val) {
    ui->progressBar->setValue(val);
}

/*
 * Disables the start button
 */
void MainWindow::disableStart() {
    ui->startButton->setEnabled(false);
}

/*
 * Enables the start button
 */
void MainWindow::enableStart() {
    ui->startButton->setEnabled(true);

}

/*
 * Displays the given level on the GUI
 */
void MainWindow::setLevel(QString level)
{
    ui->levelValueText->setText(level);
}

/*
 * Displays the given score on the GUI
 */
void MainWindow::setScore(QString score)
{
    ui->scoreValue->setText(score);
}

/*
 * Displays the given high score on the GUI
 */
void MainWindow::setHighScore(QString score)
{
    ui->highScoreValue->setText(score);
}

/*
 * Display that it is the computer's turn
 */
void MainWindow::labelCompTurn()
{
    ui->turnLabel->setVisible(true);
    ui->turnLabel->setText("COMPUTER'S TURN");
}

/*
 * Display that it is the player's turn
 */
void MainWindow::labelPlayerTurn()
{
    ui->turnLabel->setVisible(true);
    ui->turnLabel->setText("YOUR TURN");
}

/*
 * Toggles dark mode
 */
void MainWindow::on_darkBox_clicked()
{
    // Dark
    if(ui->darkBox->isChecked())
    {
        ui->centralWidget->setStyleSheet("background-color:black");
        ui->scoreLabel->setStyleSheet("color:white");
        ui->highScoreLabel->setStyleSheet("color:white");
        ui->scoreValue->setStyleSheet("color:white");
        ui->highScoreValue->setStyleSheet("color:white");
        ui->levelLabel->setStyleSheet("color:white");
        ui->levelValueText->setStyleSheet("color:white");
        ui->loseLabel->setStyleSheet("color:white");
        ui->progressLabel->setStyleSheet("color:white");
        ui->darkBox->setStyleSheet("color:white");
        ui->soundToggle->setStyleSheet("color:white");
        ui->progressBar->setStyleSheet("color:white");
        ui->creditsLabel->setStyleSheet("color:white");
        ui->turnLabel->setStyleSheet("color:white");
    }
    // Regular
    else
    {
        ui->centralWidget->setStyleSheet("background-color:lightgray");
        ui->scoreLabel->setStyleSheet("color:black");
        ui->highScoreLabel->setStyleSheet("color:black");
        ui->scoreValue->setStyleSheet("color:black");
        ui->highScoreValue->setStyleSheet("color:black");
        ui->levelLabel->setStyleSheet("color:black");
        ui->levelValueText->setStyleSheet("color:black");
        ui->loseLabel->setStyleSheet("color:black");
        ui->progressLabel->setStyleSheet("color:black");
        ui->darkBox->setStyleSheet("color:black");
        ui->soundToggle->setStyleSheet("color:black");
        ui->progressBar->setStyleSheet("color:black");
        ui->creditsLabel->setStyleSheet("color:black");
        ui->turnLabel->setStyleSheet("color:black");
    }
}
