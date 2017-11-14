#-------------------------------------------------
#
# Project created by QtCreator 2016-04-14T16:41:24
#
#-------------------------------------------------

QT       += core gui
CONFIG += c++14
QT += network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = LearnToCodeClient
TEMPLATE = app

QMAKE_CXXFLAGS += -std=c++11

SOURCES += main.cpp\
        mainwindow.cpp \
    model.cpp \
    user.cpp \
    qsfmlcanvas.cpp \
    world.cpp \
    httprequest.cpp \
    worlditem.cpp \
    level.cpp

HEADERS  += mainwindow.h \
    model.h \
    user.h \
    qsfmlcanvas.h \
    world.h \
    httprequest.h \
    worlditem.h \
    level.h

FORMS    += mainwindow.ui

#include sfml & box2d
INCLUDEPATH += /usr/include/SFML
LIBS += -lsfml-system -lsfml-window -lsfml-graphics -lsfml-audio -lBox2D

#Box2D Windows
win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/ -lBox2
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/ -lBox2d

INCLUDEPATH += $$PWD/../lib/Box2D-master/Box2D
DEPENDPATH += $$PWD/../lib/Box2D-master/Box2D

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/libBox2.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/libBox2d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/Box2.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/Box2D-master/build-Box2D-Desktop_Qt_5_6_0_MinGW_32bit-Default/Box2D/Box2d.lib

#SFML Windows
INCLUDEPATH += $$PWD/../lib/SFML_GITHUB/include
DEPENDPATH += $$PWD/../lib/SFML_GITHUB/include

#Graphics
win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-graphics
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-graphics-d

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-graphics.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-graphics-d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-graphics.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-graphics-d.lib

#Window

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-window
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-window-d

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-window.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-window-d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-window.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-window-d.lib

#System

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-system
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-system-d

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-system.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-system-d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-system.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-system-d.lib

#Audio

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-audio
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-audio-d

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-audio.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-audio-d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-audio.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-audio-d.lib

#Network

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-network
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/ -lsfml-network-d

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-network.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/libsfml-network-d.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-network.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../lib/SFML_GITHUB/build-SFML_GITHUB/lib/sfml-network-d.lib

RESOURCES += \
    icons.qrc

DISTFILES +=
