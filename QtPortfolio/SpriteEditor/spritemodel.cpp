// Team Qtπ
// Hannah Palma (u0791894), Matt Brownell (u0919206), Amit Athani (u0914565),
// Bradley Dawn (u0948671), Andrew Katsanevas (u0901239)
// Sprite Editor Assignment
// Due April 5th, 2016
#include "spritemodel.h"
#include "mainwindow.h"
#include <qpainter.h>
#include <qfile.h>
#include <qtextstream.h>
#include <iostream>
#include <sstream>
#include <Magick++.h>

/*
 * This initializes the model and adds the GUI to the model
 * Primarily done by Amit Athani
 */
SpriteModel::SpriteModel(MainWindow *gui, int pixScale)
{
    this->pixScale = pixScale;
    this->gui = gui;
    this->ui = ui;
}

// Preview the sprite as GIF
void SpriteModel::previewSprite()
{
    // Save existing frame
    saveExistingSprite();

    // Set a dummy input for a temp file
    QString input = "temp";

    // Pixmap to draw the sprite on
    pixMap = new QPixmap(gui->tableWidget->columnCount()*20, gui->tableWidget->rowCount()*20);
    // fill in the pixmap with a white background
    pixMap->fill();

    // keeps track of the frames
    int pngNum = 0;

    // create a painter to paint on the pixmap
    QPainter *paint = new QPainter(pixMap);
    // Loop through all the frames
    for(int i = 0; i < gui->numFrames; i++)
    {

        QString frames (QString::fromStdString(gui->frames[i]));
        // Removes new lines and excess spaces
        frames = frames.simplified();
        // Split the file into individual numbers
        QStringList list = frames.split(' ');
        int rowCount = 0;
        int colCount = 0;

        for(int i = 0; i < list.length(); i++)
        {
           // check for every 4th element to read in RGBA values
           if((i+1)%4 == 0)
           {
               QColor color (list.at(i-3).toInt(), list.at(i-2).toInt(),
                                      list.at(i-1).toInt(), list.at(i).toInt());
               paint->setPen(color);
               // Paint the pixmap and scale it
               paint->fillRect(colCount * 20, rowCount * 20, pixScale + 10, pixScale + 10, color);
               colCount++;

               // check if we are at the end of a row
               if(((i+1)/4) % gui->tableWidget->columnCount() == 0)
               {
                   colCount = 0;
                   rowCount++;
               }
           }

           // Save file if we have completed reading through all the rows
           if(rowCount == gui->tableWidget->rowCount())
           {
               rowCount = 0;
               QString fileName (input + QString::number(pngNum) + ".png");
               QFile file(fileName);
               file.open(QIODevice::WriteOnly);
               pixMap->save(&file, "PNG");
               file.close();
               pixMap->fill();
               pngNum++;
           }
        }
    }

    QPixmap *map= NULL;

    // Go through all the images and scale them to fit the preview
    for(int i = 0; i < gui->numFrames; i++)
    {
        map  = new QPixmap();
        QString fileName (input + QString::number(i) + ".png");
        map->load(fileName);
        *map = map->scaled(150, 150, Qt::KeepAspectRatio);
        map->save(fileName);
    }

    // add all the images to a vector of imageMagick libraries
    vector<Magick::Image> frames;
        for(int i = 0; i < gui->numFrames; i++)
        {
            // adds it to the GIF
            QString str = input + QString::number(i) + ".png";
            Magick::Image img (str.toStdString());
            int val = 100/(gui->fps);
            img.animationDelay(val);

            frames.push_back(img);
            QString temp = input + ".gif";
            Magick::writeImages(frames.begin(), frames.end(), temp.toStdString());
        }

        // Delete stale PNGs
        for(int i = 0; i < gui->numFrames; i++)
        {
            QString fileName (input + QString::number(i) + ".png");
            QFile::remove(fileName);
        }
        // call method in GUI to display a preview.
        gui->displayGif();
}

/*
 *  Save the Sprite as a Text
 */
void SpriteModel::saveAsText() {
    // Save current frame
    saveExistingSprite();
    // Open up a dialog for file input
    QString input = gui->openSaveDialog();

    QFile file(input + " .ssp");
    input = input + ".ssp";
    if(!file.open(QIODevice::WriteOnly))
    {
        return;
    }
    // Open a stream to save stuff
    QTextStream stream(&file);
    stream << gui->tableWidget->rowCount()<< " ";
    stream << gui->tableWidget->columnCount() << endl;
    stream << gui->numFrames << endl;
    for(int i = 0; i < gui->numFrames; i++)
    {
        QString s = QString::fromStdString(gui->frames[i]);
        stream << s;
    }

    file.close();
}

/*
 * Loads a sprite saved in a .ssp file
 */
void SpriteModel::loadSavedSprite()
{
    // Open a dialog to choose a file to load
    QString input = gui->openLoadDialog();
    QFile file(input);

    if(!file.open(QIODevice::ReadOnly))
    {
        return;
    }

    // Read the number of rows and columns
    QTextStream instream(&file);
    QString line = instream.readLine();
    QStringList list = line.split(' ');

    gui->changeSpinBox(list.at(0).toInt(), list.at(1).toInt());

    line = instream.readLine();

    // Read the number of frames
    list = line.split(' ');

    gui->numFrames = list.at(0).toInt();

    int frameCount = 0;
    int count = 0;

    // Loop through the file line by line and add pixels to the sprite
    instream.seek(0);
    instream.readLine();
    while(!instream.atEnd())
    {
        line = instream.readLine();
        gui->frames[frameCount] += line.toStdString() + "\n";

        if(count == gui->tableWidget->rowCount()) {
            frameCount++;
            if(frameCount != 1)
                gui->setFrameBox(frameCount);
            count = 0;
        }

        count++;
    }

    instream.seek(0);
    instream.readLine();
    instream.readLine();
    int rowCount = 0;
    int colCount = 0;
    int i;
    while(!instream.atEnd())
    {
        QString line = instream.readLine();
        list = line.split(' ');

        for(i = 0; i < list.length(); i++)
        {
            if((i+1)%4 == 0) {
             gui->tableWidget->item(rowCount, ((i+1)/4)-1)->setBackgroundColor
                     (*(new QColor(list.at(i-3).toInt(), list.at(i-2).toInt(),
                                   list.at(i-1).toInt(), list.at(i).toInt())));
             colCount++;
            }
        }
        rowCount++;
       if(gui->tableWidget->rowCount() == rowCount)
       {
            break;
       }
    }

    file.close();
}

// Saves the Sprite as a GIF functions similar to preview
void SpriteModel::saveAsGif()
{

    saveExistingSprite();

    QString input = gui->openSaveGifDialog();

    pixMap = new QPixmap(gui->tableWidget->columnCount()*20, gui->tableWidget->rowCount()*20);
    pixMap->fill();

    int pngNum = 0;

    QPainter *paint = new QPainter(pixMap);
    for(int i = 0; i < gui->numFrames; i++)
    {
        QString frames (QString::fromStdString(gui->frames[i]));
        frames = frames.simplified();
        QStringList list = frames.split(' ');
        int rowCount = 0;
        int colCount = 0;
        for(int i = 0; i < list.length(); i++)
        {

           if((i+1)%4 == 0)
           {
               QColor color (list.at(i-3).toInt(), list.at(i-2).toInt(),
                                      list.at(i-1).toInt(), list.at(i).toInt());
               paint->setPen(color);
               paint->fillRect(colCount * 20, rowCount * 20, pixScale + 10, pixScale + 10, color);
               colCount++;

               if(((i+1)/4) % gui->tableWidget->columnCount() == 0)
               {
                   colCount = 0;
                   rowCount++;
               }
           }

           if(rowCount == gui->tableWidget->rowCount())
           {
               rowCount = 0;
               QString fileName (input + QString::number(pngNum) + ".png");
               QFile file(fileName);
               file.open(QIODevice::WriteOnly);
               pixMap->save(&file, "PNG");
               file.close();
               pixMap->fill();
               pngNum++;
           }
        }
    }

    vector<Magick::Image> frames;
        for(int i = 0; i < gui->numFrames; i++)
        {
            QString str = input + QString::number(i) + ".png";
            Magick::Image img (str.toStdString());
            int val = 100/(gui->fps);
            img.animationDelay(val);
            frames.push_back(img);
            QString temp = input + ".gif";
            Magick::writeImages(frames.begin(), frames.end(), temp.toStdString());
        }

        for(int i = 0; i < gui->numFrames; i++)
        {
            QString fileName (input + QString::number(i) + ".png");
            QFile::remove(fileName);
        }

}

// Saves the current frame before doing stuff
void SpriteModel::saveExistingSprite()
{
    string frameSave = "";
    ostringstream oss;
    for (int i = 0; i < gui->tableWidget->rowCount(); i++)
    {
        for (int j = 0; j < gui->tableWidget->columnCount(); j++)
        {
            QColor current = gui->tableWidget->item(i, j)->backgroundColor();
            string blank = " ";
            oss << frameSave << current.red() << blank << current.green() << blank << current.blue() << blank << current.alpha() << blank;
        }
        oss << "\n";
    }

    gui->frames[gui->getCurrentFrameIndex()] = oss.str();
}

