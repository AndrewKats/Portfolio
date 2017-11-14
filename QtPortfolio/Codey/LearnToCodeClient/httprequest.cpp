#include "httprequest.h"
#include <QUrlQuery>
#include <QtNetwork/QNetworkAccessManager>
#include <QDebug>


HttpRequest::HttpRequest(){manager = new QNetworkAccessManager(this);}


// Makes a request to the game server. The first argument is the url of the server, including the
// route. E.g. "localhost:3030/login" The second argument is an array of url query name-value pairs.
// E.g. ["username","coderBabe89","password","TopSecret"] equals "?username=coderBabe89&password=TopSecret"
void HttpRequest::makeServerRequest(QString url, QVector<QString> params){

    QUrl serviceUrl = QUrl(url);
    QUrlQuery urlQueries;
    QString paramName;
    QString paramValue;
    for(int i = 0; i < params.size(); i++){
        if(i%2==0)
            paramName = params.at(i);
        else{
            paramValue = params.at(i);
             urlQueries.addQueryItem(paramName,paramValue);
        }
    }

    connect(this->manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(serviceRequestFinished(QNetworkReply*)));
    // append the querystring to the url address
    serviceUrl.setQuery(urlQueries.query());
    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader,
        "text/html");
    manager->get(request);

}

// Called when the server responds with data
void HttpRequest::serviceRequestFinished(QNetworkReply *reply){
    if(!reply->error() == QNetworkReply::NoError){
        emit badConnection();
        reply->deleteLater();
        return;
    }

    QString response = QString(reply->readAll());

     // Take action according to response type (login, signup, save, etc.)
    if(response == "Invalid login."){
        qDebug() << "Login Failed!";
        emit loginFailed();

    }

    else if(response == "Signup failed."){
        emit createUserFailed();

    }

    else if(response == "No Such Teacher.") {
        emit noSuchTeacher();
    }

    else if(response == "Save failed."){
        emit saveFailed();

    }

    else if(response.indexOf("login") == 0){

    qDebug() << QString(response);
    QStringList userInfo = response.split("\n");
    QString username = userInfo.at(1);
    QStringList levelScoresList = userInfo.at(2).split(",");
    QVector<int> levelScores;
    for(QString str: levelScoresList){
        levelScores.append(str.toInt());
    }
    int totalScore = userInfo.at(3).toInt();
    bool isInstructor;
    int isInstructorInt = userInfo.at(4).toInt();
    if(isInstructorInt == 0)
        isInstructor = false;
    else if(isInstructor == 1)
        isInstructor = true;
    else{
        isInstructor = false;
        qDebug() << "Something went wrong with the login request! The isInstructor field was not correct in the server response";
    }

    emit loginSucceeded(username,levelScores,totalScore,isInstructor);

    }

    // Covers server responses to requests trying to create a new user, where the user
    // was successfully created
    else if(response.indexOf("signup") == 0){
        qDebug() << QString(response);
        QStringList userInfo = response.split("\n");
        QString username = userInfo.at(1);
        QString accountType = userInfo.at(2);
        bool isInstructor;
        if(accountType.compare("1") == 0)
            isInstructor = true;
        else
            isInstructor = false;

        emit createUserSucceeded(username,isInstructor);

    }

    reply->deleteLater();
}
