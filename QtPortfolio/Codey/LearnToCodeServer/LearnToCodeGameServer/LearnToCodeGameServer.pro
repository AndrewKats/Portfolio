#-------------------------------------------------
#
# Project created by QtCreator 2016-04-18T11:56:20
#
#-------------------------------------------------

QT += core
QT -= gui
QT += network

TARGET = LearnToCodeGameServer
CONFIG   += console
CONFIG   -= app_bundle

TEMPLATE = app


SOURCES += main.cpp \
    requestmapper.cpp \
    logincontroller.cpp \
    signupcontroller.cpp \
    dbconnection.cpp \
    savecontroller.cpp \
    statscontroller.cpp \
    deletecontroller.cpp


HEADERS += \
    requestmapper.h \
    logincontroller.h \
    signupcontroller.h \
    dbconnection.h \
    savecontroller.h \
    statscontroller.h \
    deletecontroller.h


include(../QtWebApp/QtWebApp/httpserver/httpserver.pri)



OTHER_FILES += etc/webapp1.ini


# Amit
#win32: LIBS += -L$$PWD/../../../../../../Downloads/mysql-5.7.12-win32/lib/ -llibmysql

#INCLUDEPATH += $$PWD/../../../../../../Downloads/mysql-5.7.12-win32/include

#DEPENDPATH += $$PWD/../../../../../../Downloads/mysql-5.7.12-win32/include


# Hannah
#win32: LIBS += -L$$PWD/../../../../mySQL/lib/ -llibmysql

#INCLUDEPATH += $$PWD/../../../../mySQL/include
#DEPENDPATH += $$PWD/../../../../mySQL/include

#win32: LIBS += -L$$PWD/../../../../../Downloads/mysql-5.7.12-win32/lib/ -llibmysql

#INCLUDEPATH += $$PWD/../../../../../Downloads/mysql-5.7.12-win32/include
#DEPENDPATH += $$PWD/../../../../../Downloads/mysql-5.7.12-win32/include

#win32: LIBS += -L$$PWD/'../../../../../../../Program Files (x86)/MySQL/MySQL Server 5.7/lib/' -llibmysql

#INCLUDEPATH += $$PWD/'../../../../../../../Program Files (x86)/MySQL/MySQL Server 5.7/include'
#DEPENDPATH += $$PWD/'../../../../../../../Program Files (x86)/MySQL/MySQL Server 5.7/include'

#Bradley Desktop
#win32: LIBS += -L$$PWD/../../../../../MYSQL/mysql-5.7.12-win32/lib/ -llibmysql

#INCLUDEPATH += $$PWD/../../../../../MYSQL/mysql-5.7.12-win32/include
#DEPENDPATH += $$PWD/../../../../../MYSQL/mysql-5.7.12-win32/include

#Bradley's Laptop
#win32: LIBS += -L$$PWD/../../../../../libs/mysql-5.7.12-win32/mysql-5.7.12-win32/lib/ -llibmysql

#INCLUDEPATH += $$PWD/../../../../../libs/mysql-5.7.12-win32/mysql-5.7.12-win32/include
#DEPENDPATH += $$PWD/../../../../../libs/mysql-5.7.12-win32/mysql-5.7.12-win32/include
win32: LIBS += -L$$PWD/../../lib/mysql-5.7.12-win32/lib/ -llibmysql

INCLUDEPATH += $$PWD/../../lib/mysql-5.7.12-win32/include
DEPENDPATH += $$PWD/../../lib/mysql-5.7.12-win32/include
