#ifndef MODEL_H
#define MODEL_H

#include <QString>
#include <QObject>
#include <QVector>
#include <vector>
#include <map>
#include "user.h"
#include "httprequest.h"

using namespace std;

class Model : public QObject
{
    Q_OBJECT

public:
    Model();
    User* thisUser;

    void attemptSQLLogin(QString username, QString password);
    void createSQLLogin(QString fullname, QString username, QString password, bool isInstructor, QString instructorName);
    void saveProgress(int level, int score);


    bool offlineLogin();


public slots:
    void loginFailed();
    void loginSucceeded(QString username, QVector<int> levelScores, int totalScore, bool isInstructor);
    void createUserFailed();
    void createUserSucceeded(QString username, bool isInstructor);
    void saveFailed();
    void saveSucceeded();
    void badConnection();
    void noSuchTeacher();

signals:
    void loginFailedSignal();
    void loginSucceededSignal();
    void createUserFailedSignal();
    void createUserSucceededSignal();
    void saveFailedSignal();
    void saveSucceededSignal();
    void badConnectionSignal();
    void noSuchTeacherSignal();



private:
    std::vector<User*> users;
    QString serverUrl = "http://localhost:3030";
    HttpRequest* requestObj;
     void saveUserInSQL();

    void connectRequestObj();
};

#endif // MODEL_H
