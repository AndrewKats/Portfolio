#-------------------------------------------------
#
# Project created by QtCreator 2016-03-01T23:04:50
#
#-------------------------------------------------

QT       += core gui \
            multimedia

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = SimonGame
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    simonmodel.cpp

HEADERS  += mainwindow.h \
    simonmodel.h

FORMS    += mainwindow.ui

RESOURCES += \
    audio.qrc
