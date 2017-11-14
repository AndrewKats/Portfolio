#ifndef SPRITEMODEL_H
#define SPRITEMODEL_H

#include <QObject>
#include "mainwindow.h"

class SpriteModel : public QObject
{
    Q_OBJECT
    QPixmap *pixMap;
    MainWindow *gui;
    int pixScale;
    Ui::MainWindow *ui;
public:
    explicit SpriteModel(MainWindow *gui = NULL, int pixScale = 20);

signals:

public slots:
     void saveAsGif();
     void saveAsText();
     void loadSavedSprite();
     void previewSprite();

private:
     void saveExistingSprite();
};

#endif // SPRITEMODEL_H

