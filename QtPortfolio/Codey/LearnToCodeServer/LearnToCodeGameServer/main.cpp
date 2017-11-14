#include <QCoreApplication>
#include <QSettings>
#include "httplistener.h"
#include "helloworldcontroller.h"
#include "requestmapper.h"


int main(int argc, char *argv[])
{
    QCoreApplication app(argc, argv);


    // Static file controller
    QSettings* fileSettings=new QSettings("../LearnToCodeGameServer/etc/webapp1.ini",QSettings::IniFormat,&app);
    fileSettings->beginGroup("files");
    RequestMapper::staticFileController=new StaticFileController(fileSettings,&app);

    // The various server settings are defined in etc/webapp1.ini
    QSettings* listenerSettings=
            new QSettings("../LearnToCodeGameServer/etc/webapp1.ini", QSettings::IniFormat,&app);
    qDebug("config file loaded");

    listenerSettings->beginGroup("listener");
    // Start the HTTP server
    new HttpListener(listenerSettings, new RequestMapper(&app), &app);

    return app.exec();
}
