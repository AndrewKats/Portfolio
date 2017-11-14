// Team Qt3.14
// Hannah Palma (u0791894), Matt Brownell (u0919206), Amit Athani (u0914565),
// Bradley Dawn (u0948671), Andrew Katsanevas (u0901239)
// Sprite Editor Assignment
// Due April 5th, 2016

#include "mainwindow.h"
#include <QApplication>
#include <QColorDialog>
#include <QFrame>
#include <QHBoxLayout>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;

    w.show();

    return a.exec();
}
