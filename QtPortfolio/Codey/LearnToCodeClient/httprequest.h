#ifndef HTTPREQUEST_H
#define HTTPREQUEST_H

#include <QtNetwork/QNetworkReply>
#include <QtNetwork/QNetworkAccessManager>


// Makes http requests to a web server and notifies other classes of the
// server's response via signals
class HttpRequest : public QObject{
    Q_OBJECT

public:
    HttpRequest();
    void makeServerRequest(QString,QVector<QString>);

public slots:
    void serviceRequestFinished(QNetworkReply*);

signals:
    void loginFailed();
    void loginSucceeded(QString username, QVector<int> levelScores, int totalScore, bool isInstructor);
    void createUserFailed();
    void createUserSucceeded(QString username,bool isInstructor);
    void saveFailed();
    void saveSucceeded();
    void badConnection();
    void noSuchTeacher();

private:
    QNetworkAccessManager *manager;

};



#endif // HTTPREQUEST_H

