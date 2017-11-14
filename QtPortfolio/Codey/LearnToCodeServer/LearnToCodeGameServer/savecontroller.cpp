#include "savecontroller.h"
#include "dbconnection.h"
#include "httprequesthandler.h"

#include <QDebug>

SaveController::SaveController(QObject* parent)
    :HttpRequestHandler(parent) {
}

// Handles http requests having the "/login" route.
// Performs user authentication against the database.
// The expected request parameters are "username" and "password"
// The response is either "Authentication Failed." or the level that the
// user is on (always 1 for teachers). E.g., "3"
void SaveController::service(HttpRequest &request, HttpResponse &response) {

    QString username = QString(request.getParameter("username"));
    QString scores = QString(request.getParameter("scores"));


    //QByteArray res = QByteArray(resStr.toStdString().c_str());
    response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");

    // Ask database if the user exists and if the password is correct (indirectly via getUserInfo())
    DBConnection db;
    db.setUserScore(username, scores);
}
