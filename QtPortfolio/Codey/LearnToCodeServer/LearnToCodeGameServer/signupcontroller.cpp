#include "signupcontroller.h"
#include "dbconnection.h"

SignupController::SignupController(QObject* parent)
    :HttpRequestHandler(parent) {
    // empty
}

// Handles http requests having the "/signup" route
// The expected request parameters are "username" and "accountType"
// "accountType" should equal either "student" or "teacher"
// If the username is not yet taken, a new account will be created in the
// database.
void SignupController::service(HttpRequest &request, HttpResponse &response) {

    QString fullname = QString(request.getParameter("fullname"));
    QString username = QString(request.getParameter("username"));
    QString password = QString(request.getParameter("password"));
    QString isTeacher = QString(request.getParameter("accountType"));
    QString instructorName = QString(request.getParameter("instructor"));

    //QByteArray res = QByteArray(resStr.toStdString().c_str());
    response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");

    // Ask database if the user exists and if the password is correct (indirectly via getUserInfo())
    DBConnection db;

    bool usernameIsAvailable = db.createUser(fullname.toStdString(), username.toStdString(), password.toStdString(), isTeacher.toStdString());


    // If the credentials were invalid
    if(!usernameIsAvailable) {
        response.write("Signup failed.",true); // true = end of response
        return;
    }

    else {
        bool isTeacherBool = 1;
        if(isTeacher == "0") {
            isTeacherBool = 0;
            bool result = db.addStudentTeacherRelation(instructorName.toStdString(), username.toStdString());
            if(result == 0) {
                response.write("No Such Teacher.", true);
                return;
            }
        }
        response.write("signup\n");
        response.write(username.append("\n").toStdString().c_str());
        response.write(isTeacher.toStdString().c_str(),true);
    }
}


