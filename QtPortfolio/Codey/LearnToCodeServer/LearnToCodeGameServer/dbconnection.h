#ifndef DBCONNECTION_H
#define DBCONNECTION_H

#include <mysql.h>
#include <iostream>
#include <QVector>

class DBConnection {
    MYSQL_RES *result;
    MYSQL_ROW row;
    MYSQL *connection, mysql;
public:
    DBConnection();
    QVector<QString> getUserInfo(std::string, std::string);
    bool createUser(std::string, std::string, std::string passwordStr, std::string isTeacher);
    bool addStudentTeacherRelation(std::string teacher, std::string student);
    bool removeStudentTeacherRelation(std::string teacher, std::string student);
    void setUserScore(QString userName, QString scores);
    QString getStatsForTeacher(QString);
    ~DBConnection();
};

#endif // DBCONNECTION_H
