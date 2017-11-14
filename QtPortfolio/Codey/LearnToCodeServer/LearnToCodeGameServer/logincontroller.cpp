#include "logincontroller.h"
#include "dbconnection.h"
#include <QDebug>

LoginController::LoginController(QObject* parent)
    :HttpRequestHandler(parent) {
}

// Handles http requests having the "/login" route.
// Performs user authentication against the database.
// The expected request parameters are "username" and "password"
// The response is either "Authentication Failed." or the level that the
// user is on (always 1 for teachers). E.g., "3"
void LoginController::service(HttpRequest &request, HttpResponse &response) {

    QString username = QString(request.getParameter("username"));
    QString password = QString(request.getParameter("password"));


    //QByteArray res = QByteArray(resStr.toStdString().c_str());
    response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");

    // Ask database if the user exists and if the password is correct (indirectly via getUserInfo())
    DBConnection db;
    QVector<QString> playerInfo = db.getUserInfo(username.toStdString(), password.toStdString());

    // If the Connection is not good
    if(playerInfo.at(0).compare(QString("0")) == 0) {
        response.write("Could not connect to db.",true);
        return;
    }
    // If the credentials were invalid
    else if(playerInfo.at(1).compare(QString("0")) == 0) {
        response.write("Invalid login.",true); // true = end of response
        return;
    }

    else {
        // TODO: Remove last comma
        QByteArray levelScores = playerInfo.at(2).toStdString().c_str();
        QByteArray totalScore = playerInfo.at(3).toStdString().c_str();
        QByteArray accountType = playerInfo.at(4).toStdString().c_str();
        QByteArray usernameBytes = username.toStdString().c_str();

        response.write("login\n");
        response.write(usernameBytes.append("\n"));
        response.write(levelScores.append("\n"));
        response.write(totalScore.append("\n"));
        response.write(accountType,true);
    }
}
