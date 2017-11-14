/*
 * Written by Andrew Katsanevas and Amit Athani
 * 3/3/2016
 */

#include "mainwindow.h"
#include <QApplication>
#include <simonmodel.h>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();

    // Create instance of model to use in connections
    SimonModel model(&w);

    // Signal slot connections
    QObject::connect(&w, &MainWindow::startGame, &model, &SimonModel::gameStart);
    QObject::connect(&w, &MainWindow::buttonChosenInGUI, &model, &SimonModel::buttonChosen);

    return a.exec();
}
