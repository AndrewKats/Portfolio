#include "statscontroller.h"
#include "dbconnection.h"

StatsController::StatsController(QObject* parent)
    :HttpRequestHandler(parent) {
    // empty
}

// Handles http requests having the "/signup" route
// The expected request parameters are "username" and "accountType"
// "accountType" should equal either "student" or "teacher"
// If the username is not yet taken, a new account will be created in the
// database.
void StatsController::service(HttpRequest &request, HttpResponse &response) {
    QString teacher = QString(request.getParameter("showStat"));
    DBConnection db;
    QString result = db.getStatsForTeacher(teacher);
    result = result.remove(result.length()-1,1);
    QString filename="../LearnToCodeGameServer/webReport/data.csv";
    QFile file( filename );
    if ( file.open(QIODevice::ReadWrite) ) {
        QTextStream stream( &file );
        stream << result << endl;
    }
}
