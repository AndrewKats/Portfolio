/****************************************************************************
** Meta object code from reading C++ file 'httprequest.h'
**
** Created by: The Qt Meta Object Compiler version 67 (Qt 5.5.1)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../../LearnToCodeClient/httprequest.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#include <QtCore/QVector>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'httprequest.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 67
#error "This file was generated using the moc from 5.5.1. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
struct qt_meta_stringdata_HttpRequest_t {
    QByteArrayData data[17];
    char stringdata0[226];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_HttpRequest_t, stringdata0) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_HttpRequest_t qt_meta_stringdata_HttpRequest = {
    {
QT_MOC_LITERAL(0, 0, 11), // "HttpRequest"
QT_MOC_LITERAL(1, 12, 11), // "loginFailed"
QT_MOC_LITERAL(2, 24, 0), // ""
QT_MOC_LITERAL(3, 25, 14), // "loginSucceeded"
QT_MOC_LITERAL(4, 40, 8), // "username"
QT_MOC_LITERAL(5, 49, 12), // "QVector<int>"
QT_MOC_LITERAL(6, 62, 11), // "levelScores"
QT_MOC_LITERAL(7, 74, 10), // "totalScore"
QT_MOC_LITERAL(8, 85, 12), // "isInstructor"
QT_MOC_LITERAL(9, 98, 16), // "createUserFailed"
QT_MOC_LITERAL(10, 115, 19), // "createUserSucceeded"
QT_MOC_LITERAL(11, 135, 10), // "saveFailed"
QT_MOC_LITERAL(12, 146, 13), // "saveSucceeded"
QT_MOC_LITERAL(13, 160, 13), // "badConnection"
QT_MOC_LITERAL(14, 174, 13), // "noSuchTeacher"
QT_MOC_LITERAL(15, 188, 22), // "serviceRequestFinished"
QT_MOC_LITERAL(16, 211, 14) // "QNetworkReply*"

    },
    "HttpRequest\0loginFailed\0\0loginSucceeded\0"
    "username\0QVector<int>\0levelScores\0"
    "totalScore\0isInstructor\0createUserFailed\0"
    "createUserSucceeded\0saveFailed\0"
    "saveSucceeded\0badConnection\0noSuchTeacher\0"
    "serviceRequestFinished\0QNetworkReply*"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_HttpRequest[] = {

 // content:
       7,       // revision
       0,       // classname
       0,    0, // classinfo
       9,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       8,       // signalCount

 // signals: name, argc, parameters, tag, flags
       1,    0,   59,    2, 0x06 /* Public */,
       3,    4,   60,    2, 0x06 /* Public */,
       9,    0,   69,    2, 0x06 /* Public */,
      10,    2,   70,    2, 0x06 /* Public */,
      11,    0,   75,    2, 0x06 /* Public */,
      12,    0,   76,    2, 0x06 /* Public */,
      13,    0,   77,    2, 0x06 /* Public */,
      14,    0,   78,    2, 0x06 /* Public */,

 // slots: name, argc, parameters, tag, flags
      15,    1,   79,    2, 0x0a /* Public */,

 // signals: parameters
    QMetaType::Void,
    QMetaType::Void, QMetaType::QString, 0x80000000 | 5, QMetaType::Int, QMetaType::Bool,    4,    6,    7,    8,
    QMetaType::Void,
    QMetaType::Void, QMetaType::QString, QMetaType::Bool,    4,    8,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,

 // slots: parameters
    QMetaType::Void, 0x80000000 | 16,    2,

       0        // eod
};

void HttpRequest::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        HttpRequest *_t = static_cast<HttpRequest *>(_o);
        Q_UNUSED(_t)
        switch (_id) {
        case 0: _t->loginFailed(); break;
        case 1: _t->loginSucceeded((*reinterpret_cast< QString(*)>(_a[1])),(*reinterpret_cast< QVector<int>(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< bool(*)>(_a[4]))); break;
        case 2: _t->createUserFailed(); break;
        case 3: _t->createUserSucceeded((*reinterpret_cast< QString(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 4: _t->saveFailed(); break;
        case 5: _t->saveSucceeded(); break;
        case 6: _t->badConnection(); break;
        case 7: _t->noSuchTeacher(); break;
        case 8: _t->serviceRequestFinished((*reinterpret_cast< QNetworkReply*(*)>(_a[1]))); break;
        default: ;
        }
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        switch (_id) {
        default: *reinterpret_cast<int*>(_a[0]) = -1; break;
        case 1:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 1:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QVector<int> >(); break;
            }
            break;
        case 8:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 0:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QNetworkReply* >(); break;
            }
            break;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        void **func = reinterpret_cast<void **>(_a[1]);
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::loginFailed)) {
                *result = 0;
            }
        }
        {
            typedef void (HttpRequest::*_t)(QString , QVector<int> , int , bool );
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::loginSucceeded)) {
                *result = 1;
            }
        }
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::createUserFailed)) {
                *result = 2;
            }
        }
        {
            typedef void (HttpRequest::*_t)(QString , bool );
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::createUserSucceeded)) {
                *result = 3;
            }
        }
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::saveFailed)) {
                *result = 4;
            }
        }
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::saveSucceeded)) {
                *result = 5;
            }
        }
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::badConnection)) {
                *result = 6;
            }
        }
        {
            typedef void (HttpRequest::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&HttpRequest::noSuchTeacher)) {
                *result = 7;
            }
        }
    }
}

const QMetaObject HttpRequest::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_HttpRequest.data,
      qt_meta_data_HttpRequest,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *HttpRequest::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *HttpRequest::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_HttpRequest.stringdata0))
        return static_cast<void*>(const_cast< HttpRequest*>(this));
    return QObject::qt_metacast(_clname);
}

int HttpRequest::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 9)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 9;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 9)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 9;
    }
    return _id;
}

// SIGNAL 0
void HttpRequest::loginFailed()
{
    QMetaObject::activate(this, &staticMetaObject, 0, Q_NULLPTR);
}

// SIGNAL 1
void HttpRequest::loginSucceeded(QString _t1, QVector<int> _t2, int _t3, bool _t4)
{
    void *_a[] = { Q_NULLPTR, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)), const_cast<void*>(reinterpret_cast<const void*>(&_t4)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void HttpRequest::createUserFailed()
{
    QMetaObject::activate(this, &staticMetaObject, 2, Q_NULLPTR);
}

// SIGNAL 3
void HttpRequest::createUserSucceeded(QString _t1, bool _t2)
{
    void *_a[] = { Q_NULLPTR, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void HttpRequest::saveFailed()
{
    QMetaObject::activate(this, &staticMetaObject, 4, Q_NULLPTR);
}

// SIGNAL 5
void HttpRequest::saveSucceeded()
{
    QMetaObject::activate(this, &staticMetaObject, 5, Q_NULLPTR);
}

// SIGNAL 6
void HttpRequest::badConnection()
{
    QMetaObject::activate(this, &staticMetaObject, 6, Q_NULLPTR);
}

// SIGNAL 7
void HttpRequest::noSuchTeacher()
{
    QMetaObject::activate(this, &staticMetaObject, 7, Q_NULLPTR);
}
QT_END_MOC_NAMESPACE
