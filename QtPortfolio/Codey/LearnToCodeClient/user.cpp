#include "user.h"

// Constructor for creating a new user.
User::User(QString _username, bool _isInstructor)
{
    username = _username;
    isInstructor = _isInstructor;
    totalPoints = 0;
    pointsPerLevel = QVector<int>();

}


// Constructor used when logging in an existing user from the database
User::User(QString _username, QVector<int> _pointsPerLevel, int _totalPoints, bool _isInstructor ){
    username = _username;
    pointsPerLevel = _pointsPerLevel;
    totalPoints = _totalPoints;
    isInstructor = _isInstructor;
}
