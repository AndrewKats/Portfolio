#include "deletecontroller.h"
#include "dbconnection.h"
#include <QDebug>
DeleteController::DeleteController(QObject* parent)
    :HttpRequestHandler(parent) {
}


//
void DeleteController::service(HttpRequest &request, HttpResponse &response) {

    QString teacher = QString(request.getParameter("teacher"));
    QString student = QString(request.getParameter("student"));


    //QByteArray res = QByteArray(resStr.toStdString().c_str());
    response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");

    // Ask database if the user exists and if the password is correct (indirectly via getUserInfo())
    DBConnection db;
    db.removeStudentTeacherRelation(teacher.toStdString(),student.toStdString());


}
