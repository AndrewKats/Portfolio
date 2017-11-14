#-------------------------------------------------
#
# Project created by QtCreator 2016-03-29T16:16:41
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = SpriteEditor
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    spritemodel.cpp

HEADERS  += mainwindow.h \
    spritemodel.h

FORMS    += mainwindow.ui

RESOURCES += \
    res.qrc


unix:!macx: LIBS += -L$$PWD/../../../../../../usr/lib64/ -lMagick++ -lMagickCore

INCLUDEPATH += $$PWD/../../../../../../usr/include/ImageMagick
DEPENDPATH += $$PWD/../../../../../../usr/include/ImageMagick
