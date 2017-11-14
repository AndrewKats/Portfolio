#include "dbconnection.h"
#include <iostream>
#include <qvector.h>
#include <mysql.h>
#include <QDebug>

DBConnection::DBConnection() {
    mysql_init(&mysql);
    this->result = NULL;
    connection = mysql_real_connect(&mysql,"155.97.196.17","admin","cs3505@dave","sys",3306,0,0);
    if (connection == NULL) {
        throw -1;
    }
}

DBConnection::~DBConnection() {
    if(result != NULL)
        mysql_free_result(result);
    mysql_close(connection);
    std::cout << "Done Connecting" << std::endl;
}

QVector<QString> DBConnection::getUserInfo(std::string userName, std::string pWord) {

    QVector<QString> userInfo;
    std::string query = "SELECT * FROM Users where login = \'" + userName + "\' and userPassword = \'" + pWord + "\';";
    if (mysql_query(connection,  query.c_str())) {
        userInfo.append("0");
    }
    else{
        userInfo.append("1");
    }
    result = mysql_store_result(connection);

    if (result == NULL || result->row_count == 0) {
        userInfo.append("0");
    }
    else{
        userInfo.append("1");
    }
    int totalScore = 0;
    int num_fields = mysql_num_fields(result);
    QString isTeacher = "0";
    QString levelScore;
    while ((row = mysql_fetch_row(result))) {
        for(int i = 0; i < num_fields; i++) {
            //  std::cout << row[i] << std::endl;
            QString str = row[i];
            if(row[i] && i == 3) {
                isTeacher = str;
            }
            if(row[i] && i > 3) {
                totalScore += str.toInt();
                levelScore.append(str).append(",");
            }
        }
    }
    QString temp = QString::number(totalScore);
    userInfo.append(levelScore);
    userInfo.append(temp);
    userInfo.append(isTeacher);
    return userInfo;
}

bool DBConnection::addStudentTeacherRelation(std::string teacher, std::string student) {
    std::string query = "Insert into taughtby (teacherLogin, studentLogin) values ('" + teacher + "' , '" + student + "');";
    std::string findTeacher = "Select * from users where login = '" + teacher + "' and isTeacher = 1;";
    std::string removeUser = "DELETE FROM users WHERE login = '" + student + "';";

    if (mysql_query(connection,  findTeacher.c_str())) {
        return 0;
    }
    result = mysql_store_result(connection);

    if (result == NULL || result->row_count == 0) {
        mysql_query(connection, removeUser.c_str());
        return 0;
    }
    std::cout << query << std::endl;

    if (mysql_query(connection, query.c_str())) {
        mysql_query(connection, removeUser.c_str());
        return 0;
    }
    return 1;
    //DELETE FROM `sys`.`taughtby` WHERE `teacherLogin`='dave' and`studentLogin`='ABC';

}

bool DBConnection::removeStudentTeacherRelation(std::string teacher, std::string student) {

    std::string query = "DELETE FROM `sys`.`taughtby` WHERE teacher = '" + teacher + "' and studentLogin = '" + student + "';";

    if (mysql_query(connection, query.c_str())) {
        return 0;
    }
    std::cout << query << std::endl;


    return 1;

}


bool DBConnection::createUser(std::string fullName, std::string username , std::string passwordStr, std::string isTeacher) {
    std::string query = "Insert into users (login, userPassword, userName, isTeacher) values ('" + username + "','" + passwordStr + "','" + fullName + "','" + isTeacher + "');";
        //std::cout << query << std::endl;

    if (mysql_query(connection, query.c_str())) {
        //std::cout << "Error!!!" << std::endl;
        return 0;
    }
    return 1;
}

void DBConnection::setUserScore(QString userName, QString scores) {
    //UPDATE `sys`.`users` SET `lvl1Score`=score WHERE `login`=loginString;
    QStringList scoresList = scores.split(",", QString::SkipEmptyParts);

    std::string query = "Update users SET lvl1Score = " + scoresList.at(0).toStdString() + ", lvl2Score =" + scoresList.at(1).toStdString()
            + ", lvl3Score =" + scoresList.at(2).toStdString() + ", lvl4Score =" + scoresList.at(3).toStdString()
            + ", lvl5Score =" + scoresList.at(4).toStdString() + ", lvl6Score =" + scoresList.at(5).toStdString()
            + ", lvl7Score =" + scoresList.at(6).toStdString() + ", lvl8Score =" + scoresList.at(7).toStdString()
            + ", lvl9Score =" + scoresList.at(8).toStdString() + " where login = '" + userName.toStdString() + "';";
    std::cout << query << std::endl;

    if (mysql_query(connection,  query.c_str())) {
        std::cout << "ERROR" << std::endl;
        throw -1;
    }
}

QString DBConnection::getStatsForTeacher(QString teacher) {
    std::string getAllStudents = "SELECT login, userName, lvl1Score, lvl2Score, lvl3Score, lvl4Score, lvl5Score, lvl6Score, lvl7Score, lvl8Score, lvl9Score, lvl1Score+lvl2Score+lvl3Score+lvl4Score+lvl5Score+lvl6Score+lvl7Score+lvl8Score+lvl9Score AS totalScore FROM users U, taughtby T WHERE U.login=T.studentLogin and teacherLogin = '" + teacher.toStdString() + "';";
    if (mysql_query(connection, getAllStudents.c_str())) {
        return "";
    }

    result = mysql_store_result(connection);

    if (result == NULL || result->row_count == 0) {
        QString str =  "";
        return str;
    }
    int num_fields = mysql_num_fields(result);

    int studentNumber = 0;
    QString output = "\"id\",\"fullName\",currentlevel,score\n";
    QString login = "";
    QString userName = "";
    QString totalScore = "";
    while ((row = mysql_fetch_row(result))) {
        login = row[0];
        userName = row[1];
        output.append("\"" + login + "\",\"" + userName + "\",");
        for(int i = 2; i < num_fields; i++) {
            QString temp = row[i];
            if(i < 10 && temp == "0") {
                output.append(QString::number(i-1) + ",");
                break;
            }
            if(i == 10) {
                output.append(QString::number(9) + ",");
            }
        }
        totalScore = row[11];
        output.append( totalScore + "\n");
    }
    return output;
}

