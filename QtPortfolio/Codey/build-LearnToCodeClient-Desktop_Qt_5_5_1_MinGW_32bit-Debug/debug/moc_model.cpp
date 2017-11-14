/****************************************************************************
** Meta object code from reading C++ file 'model.h'
**
** Created by: The Qt Meta Object Compiler version 67 (Qt 5.5.1)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../../LearnToCodeClient/model.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#include <QtCore/QVector>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'model.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 67
#error "This file was generated using the moc from 5.5.1. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
struct qt_meta_stringdata_Model_t {
    QByteArrayData data[23];
    char stringdata0[347];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_Model_t, stringdata0) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_Model_t qt_meta_stringdata_Model = {
    {
QT_MOC_LITERAL(0, 0, 5), // "Model"
QT_MOC_LITERAL(1, 6, 17), // "loginFailedSignal"
QT_MOC_LITERAL(2, 24, 0), // ""
QT_MOC_LITERAL(3, 25, 20), // "loginSucceededSignal"
QT_MOC_LITERAL(4, 46, 22), // "createUserFailedSignal"
QT_MOC_LITERAL(5, 69, 25), // "createUserSucceededSignal"
QT_MOC_LITERAL(6, 95, 16), // "saveFailedSignal"
QT_MOC_LITERAL(7, 112, 19), // "saveSucceededSignal"
QT_MOC_LITERAL(8, 132, 19), // "badConnectionSignal"
QT_MOC_LITERAL(9, 152, 19), // "noSuchTeacherSignal"
QT_MOC_LITERAL(10, 172, 11), // "loginFailed"
QT_MOC_LITERAL(11, 184, 14), // "loginSucceeded"
QT_MOC_LITERAL(12, 199, 8), // "username"
QT_MOC_LITERAL(13, 208, 12), // "QVector<int>"
QT_MOC_LITERAL(14, 221, 11), // "levelScores"
QT_MOC_LITERAL(15, 233, 10), // "totalScore"
QT_MOC_LITERAL(16, 244, 12), // "isInstructor"
QT_MOC_LITERAL(17, 257, 16), // "createUserFailed"
QT_MOC_LITERAL(18, 274, 19), // "createUserSucceeded"
QT_MOC_LITERAL(19, 294, 10), // "saveFailed"
QT_MOC_LITERAL(20, 305, 13), // "saveSucceeded"
QT_MOC_LITERAL(21, 319, 13), // "badConnection"
QT_MOC_LITERAL(22, 333, 13) // "noSuchTeacher"

    },
    "Model\0loginFailedSignal\0\0loginSucceededSignal\0"
    "createUserFailedSignal\0createUserSucceededSignal\0"
    "saveFailedSignal\0saveSucceededSignal\0"
    "badConnectionSignal\0noSuchTeacherSignal\0"
    "loginFailed\0loginSucceeded\0username\0"
    "QVector<int>\0levelScores\0totalScore\0"
    "isInstructor\0createUserFailed\0"
    "createUserSucceeded\0saveFailed\0"
    "saveSucceeded\0badConnection\0noSuchTeacher"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_Model[] = {

 // content:
       7,       // revision
       0,       // classname
       0,    0, // classinfo
      16,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       8,       // signalCount

 // signals: name, argc, parameters, tag, flags
       1,    0,   94,    2, 0x06 /* Public */,
       3,    0,   95,    2, 0x06 /* Public */,
       4,    0,   96,    2, 0x06 /* Public */,
       5,    0,   97,    2, 0x06 /* Public */,
       6,    0,   98,    2, 0x06 /* Public */,
       7,    0,   99,    2, 0x06 /* Public */,
       8,    0,  100,    2, 0x06 /* Public */,
       9,    0,  101,    2, 0x06 /* Public */,

 // slots: name, argc, parameters, tag, flags
      10,    0,  102,    2, 0x0a /* Public */,
      11,    4,  103,    2, 0x0a /* Public */,
      17,    0,  112,    2, 0x0a /* Public */,
      18,    2,  113,    2, 0x0a /* Public */,
      19,    0,  118,    2, 0x0a /* Public */,
      20,    0,  119,    2, 0x0a /* Public */,
      21,    0,  120,    2, 0x0a /* Public */,
      22,    0,  121,    2, 0x0a /* Public */,

 // signals: parameters
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,

 // slots: parameters
    QMetaType::Void,
    QMetaType::Void, QMetaType::QString, 0x80000000 | 13, QMetaType::Int, QMetaType::Bool,   12,   14,   15,   16,
    QMetaType::Void,
    QMetaType::Void, QMetaType::QString, QMetaType::Bool,   12,   16,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,

       0        // eod
};

void Model::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Model *_t = static_cast<Model *>(_o);
        Q_UNUSED(_t)
        switch (_id) {
        case 0: _t->loginFailedSignal(); break;
        case 1: _t->loginSucceededSignal(); break;
        case 2: _t->createUserFailedSignal(); break;
        case 3: _t->createUserSucceededSignal(); break;
        case 4: _t->saveFailedSignal(); break;
        case 5: _t->saveSucceededSignal(); break;
        case 6: _t->badConnectionSignal(); break;
        case 7: _t->noSuchTeacherSignal(); break;
        case 8: _t->loginFailed(); break;
        case 9: _t->loginSucceeded((*reinterpret_cast< QString(*)>(_a[1])),(*reinterpret_cast< QVector<int>(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< bool(*)>(_a[4]))); break;
        case 10: _t->createUserFailed(); break;
        case 11: _t->createUserSucceeded((*reinterpret_cast< QString(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 12: _t->saveFailed(); break;
        case 13: _t->saveSucceeded(); break;
        case 14: _t->badConnection(); break;
        case 15: _t->noSuchTeacher(); break;
        default: ;
        }
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        switch (_id) {
        default: *reinterpret_cast<int*>(_a[0]) = -1; break;
        case 9:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 1:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QVector<int> >(); break;
            }
            break;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        void **func = reinterpret_cast<void **>(_a[1]);
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::loginFailedSignal)) {
                *result = 0;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::loginSucceededSignal)) {
                *result = 1;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::createUserFailedSignal)) {
                *result = 2;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::createUserSucceededSignal)) {
                *result = 3;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::saveFailedSignal)) {
                *result = 4;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::saveSucceededSignal)) {
                *result = 5;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::badConnectionSignal)) {
                *result = 6;
            }
        }
        {
            typedef void (Model::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Model::noSuchTeacherSignal)) {
                *result = 7;
            }
        }
    }
}

const QMetaObject Model::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_Model.data,
      qt_meta_data_Model,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *Model::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *Model::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_Model.stringdata0))
        return static_cast<void*>(const_cast< Model*>(this));
    return QObject::qt_metacast(_clname);
}

int Model::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 16)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 16;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 16)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 16;
    }
    return _id;
}

// SIGNAL 0
void Model::loginFailedSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 0, Q_NULLPTR);
}

// SIGNAL 1
void Model::loginSucceededSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 1, Q_NULLPTR);
}

// SIGNAL 2
void Model::createUserFailedSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 2, Q_NULLPTR);
}

// SIGNAL 3
void Model::createUserSucceededSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 3, Q_NULLPTR);
}

// SIGNAL 4
void Model::saveFailedSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 4, Q_NULLPTR);
}

// SIGNAL 5
void Model::saveSucceededSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 5, Q_NULLPTR);
}

// SIGNAL 6
void Model::badConnectionSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 6, Q_NULLPTR);
}

// SIGNAL 7
void Model::noSuchTeacherSignal()
{
    QMetaObject::activate(this, &staticMetaObject, 7, Q_NULLPTR);
}
QT_END_MOC_NAMESPACE
