#include "model.h"
#include <QVector>
#include <QDebug>

Model::Model()
{
    requestObj = new HttpRequest();
    connectRequestObj();
}

// Saves a user's score for a certain level. If the score
// is lower than the user's previous high score for that level, this
// method does nothing. Else, it also updates total score
// and updates the user's stats in the database
void Model::saveProgress(int level, int score){
    int prevScore = thisUser->pointsPerLevel.at(level-1);
    if(score > prevScore)
        thisUser->pointsPerLevel[level-1] = score;
    int totalScore = 0;
    // update total score
    for(int score : thisUser->pointsPerLevel){
        totalScore += score;
    }

    // save user's info in the db
    saveUserInSQL();

}

// Checks with the db to verify the supplied login credentials
void Model::attemptSQLLogin(QString username, QString password)
{
    QVector<QString> parameters;
    parameters.append("username");
    parameters.append(username);
    parameters.append("password");
    parameters.append(password);
    requestObj->makeServerRequest(serverUrl.append("/login"), parameters);
    serverUrl.replace("/login",""); //serverUrl is once again www.blah.com/ without a path



    // The act of making a server request is asynchronous, so we can't return anything from this method.
    // When the server responds, the requestObj will emit a signal that a model slot will receive

}

// This method will ACTUALLY QUERY the server.
void Model::createSQLLogin(QString fullname, QString username, QString password, bool isInstructor, QString instructorName)
{
    QVector<QString> parameters;

    parameters.append("fullname");
    parameters.append(fullname);
    parameters.append("username");
    parameters.append(username);
    parameters.append("password");
    parameters.append(password);
    parameters.append("accountType");
    if(isInstructor)
        parameters.append("1");
    else
        parameters.append("0");
    parameters.append("instructor");
    parameters.append(instructorName);


    requestObj->makeServerRequest(serverUrl.append("/signup"), parameters);
    serverUrl.replace("/signup",""); //serverUrl is once again www.blah.com/ without a path

    // The act of making a server request is asynchronous, so we can't return anything from this method.
    // When the server responds, the requestObj will emit a signal that a model slot will receive
}

// This method will ACTUALLY QUERY the mySQL Database. Don't run if you have it config'd.
void Model::saveUserInSQL()
{

    QVector<QString> parameters;

    parameters.append("username");
    parameters.append(thisUser->username);
    parameters.append("scores");
    QString scores = "";
    int score;
    foreach(score, thisUser->pointsPerLevel){
        scores.append(QString::number(score)+ ",");
    }
    scores = scores.remove(scores.length()-1, 1);
    parameters.append(scores);
    requestObj->makeServerRequest(serverUrl.append("/save"), parameters);
    serverUrl.replace("/save","");

    // "thisUser" s stats have been changed. Under its username in the DB, update it's information to match.
    // return true if successful -- ? Or we can just make it void. Up to you guys.


}



// Attempt to create a login. If successful, return true. If login
// already exists, return false.
bool Model::offlineLogin()
{
    return true;
}

// Slot called if server tells us the user login failed
void Model::loginFailed(){
    qDebug() << "In the Model slot \"login Failed\"";
    emit Model::loginFailedSignal();
}

// Slot called if server tells us the user login succeeded
void Model::loginSucceeded(QString username, QVector<int> levelScores, int totalScore, bool isInstructor){
    thisUser = new User(username,levelScores,totalScore,isInstructor);

    emit loginSucceededSignal();
}

void Model::createUserFailed(){
    emit createUserFailedSignal();
}

void Model::noSuchTeacher() {
    emit noSuchTeacherSignal();
}

void Model::createUserSucceeded(QString username, bool isInstructor){

    thisUser = new User(username,isInstructor);
    emit createUserSucceededSignal();
}

void Model::saveFailed(){
    emit saveFailedSignal();
}

void Model::saveSucceeded(){
    emit saveSucceededSignal();
}

void Model::badConnection(){
    emit badConnectionSignal();
}

void Model::connectRequestObj(){
    connect(requestObj,SIGNAL(loginFailed()), this, SLOT(loginFailed()));
    connect(requestObj,SIGNAL(loginSucceeded(QString,QVector<int>,int,bool)), this, SLOT(loginSucceeded(QString,QVector<int>,int,bool)));
    connect(requestObj,SIGNAL(createUserFailed()), this, SLOT(createUserFailed()));
    connect(requestObj,SIGNAL(createUserSucceeded(QString,bool)), this, SLOT(createUserSucceeded(QString,bool)));
    connect(requestObj,SIGNAL(saveFailed()), this, SLOT(saveFailed()));
    connect(requestObj,SIGNAL(saveSucceeded()), this, SLOT(saveSucceeded()));
    connect(requestObj,SIGNAL(badConnection()), this, SLOT(badConnection()));
    connect(requestObj,SIGNAL(noSuchTeacher()), this, SLOT(noSuchTeacher()));


}

