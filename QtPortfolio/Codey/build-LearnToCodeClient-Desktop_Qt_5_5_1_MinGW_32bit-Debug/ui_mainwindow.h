/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.5.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QFrame>
#include <QtWidgets/QGridLayout>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSpinBox>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralWidget;
    QFrame *loginFrame;
    QLabel *loginFailedLabel;
    QPushButton *createLoginButton;
    QLabel *createLoginLabel;
    QWidget *horizontalLayoutWidget_4;
    QHBoxLayout *actualLoginLayout;
    QGridLayout *loginLayout;
    QLabel *usernameLabel;
    QLineEdit *passwordField;
    QLineEdit *usernameField;
    QPushButton *loginButton;
    QLabel *label_4;
    QLabel *label_3;
    QLabel *passwordLabel;
    QWidget *layoutWidget;
    QGridLayout *createLayout;
    QLineEdit *lastNameField;
    QLabel *instructorLabel;
    QLabel *instructorNameLabel;
    QLabel *lastNameLabel;
    QLineEdit *instructorNameField;
    QHBoxLayout *yesNoLayout;
    QCheckBox *yesBox;
    QCheckBox *noBox;
    QLineEdit *firstNameField;
    QLabel *firstNameLabel;
    QPushButton *signUpFinalButton;
    QPushButton *cancelButton;
    QFrame *commandFrame;
    QWidget *horizontalLayoutWidget;
    QHBoxLayout *commandLayout;
    QPushButton *forwardButton;
    QPushButton *jumpButton;
    QPushButton *waitButton;
    QPushButton *ifButton;
    QPushButton *loopButton;
    QPushButton *removeButton;
    QLabel *commandsLabel;
    QLabel *label;
    QLabel *label_2;
    QPushButton *if1;
    QPushButton *if2;
    QPushButton *loop1;
    QPushButton *loop2;
    QPushButton *loop3;
    QSpinBox *loopSpinBox;
    QPushButton *restartButton;
    QPushButton *if12;
    QPushButton *if22;
    QPushButton *logoutButton;
    QFrame *levelFrame;
    QWidget *gridLayoutWidget_2;
    QGridLayout *levelLayout;
    QVBoxLayout *level3Layout;
    QLabel *level3Label;
    QPushButton *level3Button;
    QVBoxLayout *level7Layout;
    QLabel *level7Label;
    QPushButton *level7Button;
    QVBoxLayout *level2Layout;
    QLabel *level2Label;
    QPushButton *level2Button;
    QVBoxLayout *level4Layout;
    QLabel *level4Label;
    QPushButton *level4Button;
    QVBoxLayout *level1Layout;
    QLabel *level1Label;
    QPushButton *level1Button;
    QVBoxLayout *level5Layout;
    QLabel *level5Label;
    QPushButton *level5Button;
    QVBoxLayout *level6Layout;
    QLabel *level6Label;
    QPushButton *level6Button;
    QVBoxLayout *level9Layout;
    QLabel *level9Label;
    QPushButton *level9Button;
    QVBoxLayout *level8Layout;
    QLabel *level8Label;
    QPushButton *level8Button;
    QLabel *userStatsLabel;
    QFrame *sequenceFrame;
    QWidget *verticalLayoutWidget;
    QVBoxLayout *sequenceLayout;
    QPushButton *runButton;
    QLabel *sequenceLabel;
    QFrame *scoreFrame;
    QFrame *scoreGrid;
    QWidget *gridLayoutWidget_3;
    QGridLayout *scoreLayout;
    QLabel *scoreValue;
    QLabel *highschoolValue;
    QPushButton *continueButton;
    QLabel *highscoreLabel;
    QLabel *scoreLabel;
    QPushButton *homeButton;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QStringLiteral("MainWindow"));
        MainWindow->resize(1117, 628);
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        loginFrame = new QFrame(centralWidget);
        loginFrame->setObjectName(QStringLiteral("loginFrame"));
        loginFrame->setGeometry(QRect(380, 200, 421, 241));
        loginFrame->setStyleSheet(QStringLiteral(""));
        loginFrame->setFrameShape(QFrame::StyledPanel);
        loginFrame->setFrameShadow(QFrame::Raised);
        loginFailedLabel = new QLabel(loginFrame);
        loginFailedLabel->setObjectName(QStringLiteral("loginFailedLabel"));
        loginFailedLabel->setGeometry(QRect(50, 90, 201, 20));
        loginFailedLabel->setStyleSheet(QStringLiteral("color: rgb(243, 0, 0); qproperty-alignment: AlignCenter;"));
        createLoginButton = new QPushButton(loginFrame);
        createLoginButton->setObjectName(QStringLiteral("createLoginButton"));
        createLoginButton->setGeometry(QRect(110, 140, 75, 23));
        createLoginLabel = new QLabel(loginFrame);
        createLoginLabel->setObjectName(QStringLiteral("createLoginLabel"));
        createLoginLabel->setGeometry(QRect(100, 120, 101, 16));
        horizontalLayoutWidget_4 = new QWidget(loginFrame);
        horizontalLayoutWidget_4->setObjectName(QStringLiteral("horizontalLayoutWidget_4"));
        horizontalLayoutWidget_4->setGeometry(QRect(60, 10, 181, 113));
        actualLoginLayout = new QHBoxLayout(horizontalLayoutWidget_4);
        actualLoginLayout->setSpacing(6);
        actualLoginLayout->setContentsMargins(11, 11, 11, 11);
        actualLoginLayout->setObjectName(QStringLiteral("actualLoginLayout"));
        actualLoginLayout->setContentsMargins(0, 0, 0, 0);
        loginLayout = new QGridLayout();
        loginLayout->setSpacing(6);
        loginLayout->setObjectName(QStringLiteral("loginLayout"));
        usernameLabel = new QLabel(horizontalLayoutWidget_4);
        usernameLabel->setObjectName(QStringLiteral("usernameLabel"));

        loginLayout->addWidget(usernameLabel, 0, 0, 1, 1);

        passwordField = new QLineEdit(horizontalLayoutWidget_4);
        passwordField->setObjectName(QStringLiteral("passwordField"));
        passwordField->setEchoMode(QLineEdit::Password);

        loginLayout->addWidget(passwordField, 1, 1, 1, 1);

        usernameField = new QLineEdit(horizontalLayoutWidget_4);
        usernameField->setObjectName(QStringLiteral("usernameField"));

        loginLayout->addWidget(usernameField, 0, 1, 1, 1);

        loginButton = new QPushButton(horizontalLayoutWidget_4);
        loginButton->setObjectName(QStringLiteral("loginButton"));
        loginButton->setEnabled(true);

        loginLayout->addWidget(loginButton, 2, 1, 1, 1);

        label_4 = new QLabel(horizontalLayoutWidget_4);
        label_4->setObjectName(QStringLiteral("label_4"));

        loginLayout->addWidget(label_4, 3, 0, 1, 1);

        label_3 = new QLabel(horizontalLayoutWidget_4);
        label_3->setObjectName(QStringLiteral("label_3"));

        loginLayout->addWidget(label_3, 3, 1, 1, 1);

        passwordLabel = new QLabel(horizontalLayoutWidget_4);
        passwordLabel->setObjectName(QStringLiteral("passwordLabel"));

        loginLayout->addWidget(passwordLabel, 1, 0, 1, 1);


        actualLoginLayout->addLayout(loginLayout);

        layoutWidget = new QWidget(loginFrame);
        layoutWidget->setObjectName(QStringLiteral("layoutWidget"));
        layoutWidget->setGeometry(QRect(250, 10, 172, 111));
        createLayout = new QGridLayout(layoutWidget);
        createLayout->setSpacing(6);
        createLayout->setContentsMargins(11, 11, 11, 11);
        createLayout->setObjectName(QStringLiteral("createLayout"));
        createLayout->setVerticalSpacing(6);
        createLayout->setContentsMargins(0, 0, 0, 0);
        lastNameField = new QLineEdit(layoutWidget);
        lastNameField->setObjectName(QStringLiteral("lastNameField"));
        lastNameField->setEchoMode(QLineEdit::Normal);

        createLayout->addWidget(lastNameField, 1, 1, 1, 1);

        instructorLabel = new QLabel(layoutWidget);
        instructorLabel->setObjectName(QStringLiteral("instructorLabel"));

        createLayout->addWidget(instructorLabel, 2, 0, 1, 1);

        instructorNameLabel = new QLabel(layoutWidget);
        instructorNameLabel->setObjectName(QStringLiteral("instructorNameLabel"));

        createLayout->addWidget(instructorNameLabel, 3, 0, 1, 1);

        lastNameLabel = new QLabel(layoutWidget);
        lastNameLabel->setObjectName(QStringLiteral("lastNameLabel"));

        createLayout->addWidget(lastNameLabel, 1, 0, 1, 1);

        instructorNameField = new QLineEdit(layoutWidget);
        instructorNameField->setObjectName(QStringLiteral("instructorNameField"));
        instructorNameField->setEchoMode(QLineEdit::Normal);

        createLayout->addWidget(instructorNameField, 3, 1, 1, 1);

        yesNoLayout = new QHBoxLayout();
        yesNoLayout->setSpacing(6);
        yesNoLayout->setObjectName(QStringLiteral("yesNoLayout"));
        yesBox = new QCheckBox(layoutWidget);
        yesBox->setObjectName(QStringLiteral("yesBox"));

        yesNoLayout->addWidget(yesBox);

        noBox = new QCheckBox(layoutWidget);
        noBox->setObjectName(QStringLiteral("noBox"));

        yesNoLayout->addWidget(noBox);


        createLayout->addLayout(yesNoLayout, 2, 1, 1, 1);

        firstNameField = new QLineEdit(layoutWidget);
        firstNameField->setObjectName(QStringLiteral("firstNameField"));

        createLayout->addWidget(firstNameField, 0, 1, 1, 1);

        firstNameLabel = new QLabel(layoutWidget);
        firstNameLabel->setObjectName(QStringLiteral("firstNameLabel"));

        createLayout->addWidget(firstNameLabel, 0, 0, 1, 1);

        signUpFinalButton = new QPushButton(loginFrame);
        signUpFinalButton->setObjectName(QStringLiteral("signUpFinalButton"));
        signUpFinalButton->setGeometry(QRect(260, 130, 61, 23));
        cancelButton = new QPushButton(loginFrame);
        cancelButton->setObjectName(QStringLiteral("cancelButton"));
        cancelButton->setGeometry(QRect(330, 130, 61, 23));
        commandFrame = new QFrame(centralWidget);
        commandFrame->setObjectName(QStringLiteral("commandFrame"));
        commandFrame->setEnabled(true);
        commandFrame->setGeometry(QRect(50, 510, 931, 61));
        commandFrame->setStyleSheet(QStringLiteral(""));
        commandFrame->setFrameShape(QFrame::StyledPanel);
        commandFrame->setFrameShadow(QFrame::Raised);
        horizontalLayoutWidget = new QWidget(commandFrame);
        horizontalLayoutWidget->setObjectName(QStringLiteral("horizontalLayoutWidget"));
        horizontalLayoutWidget->setGeometry(QRect(0, 10, 621, 51));
        commandLayout = new QHBoxLayout(horizontalLayoutWidget);
        commandLayout->setSpacing(6);
        commandLayout->setContentsMargins(11, 11, 11, 11);
        commandLayout->setObjectName(QStringLiteral("commandLayout"));
        commandLayout->setContentsMargins(0, 0, 0, 0);
        forwardButton = new QPushButton(horizontalLayoutWidget);
        forwardButton->setObjectName(QStringLiteral("forwardButton"));
        QIcon icon;
        icon.addFile(QStringLiteral(":/icons/icons/forwardIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        forwardButton->setIcon(icon);
        forwardButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(forwardButton);

        jumpButton = new QPushButton(horizontalLayoutWidget);
        jumpButton->setObjectName(QStringLiteral("jumpButton"));
        QIcon icon1;
        icon1.addFile(QStringLiteral(":/icons/icons/jumpIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        jumpButton->setIcon(icon1);
        jumpButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(jumpButton);

        waitButton = new QPushButton(horizontalLayoutWidget);
        waitButton->setObjectName(QStringLiteral("waitButton"));
        QIcon icon2;
        icon2.addFile(QStringLiteral(":/icons/icons/waitIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        waitButton->setIcon(icon2);
        waitButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(waitButton);

        ifButton = new QPushButton(horizontalLayoutWidget);
        ifButton->setObjectName(QStringLiteral("ifButton"));
        QIcon icon3;
        icon3.addFile(QStringLiteral(":/icons/icons/ifIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        ifButton->setIcon(icon3);
        ifButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(ifButton);

        loopButton = new QPushButton(horizontalLayoutWidget);
        loopButton->setObjectName(QStringLiteral("loopButton"));
        QIcon icon4;
        icon4.addFile(QStringLiteral(":/icons/icons/loopIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        loopButton->setIcon(icon4);
        loopButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(loopButton);

        removeButton = new QPushButton(horizontalLayoutWidget);
        removeButton->setObjectName(QStringLiteral("removeButton"));
        QIcon icon5;
        icon5.addFile(QStringLiteral(":/icons/icons/removeLastIcon.ico"), QSize(), QIcon::Normal, QIcon::Off);
        removeButton->setIcon(icon5);
        removeButton->setIconSize(QSize(25, 25));

        commandLayout->addWidget(removeButton);

        commandsLabel = new QLabel(commandFrame);
        commandsLabel->setObjectName(QStringLiteral("commandsLabel"));
        commandsLabel->setGeometry(QRect(0, 0, 61, 16));
        label = new QLabel(commandFrame);
        label->setObjectName(QStringLiteral("label"));
        label->setGeometry(QRect(560, 0, 41, 16));
        label_2 = new QLabel(commandFrame);
        label_2->setObjectName(QStringLiteral("label_2"));
        label_2->setGeometry(QRect(700, 0, 51, 16));
        if1 = new QPushButton(commandFrame);
        if1->setObjectName(QStringLiteral("if1"));
        if1->setGeometry(QRect(560, 20, 41, 31));
        if2 = new QPushButton(commandFrame);
        if2->setObjectName(QStringLiteral("if2"));
        if2->setGeometry(QRect(640, 20, 41, 31));
        loop1 = new QPushButton(commandFrame);
        loop1->setObjectName(QStringLiteral("loop1"));
        loop1->setGeometry(QRect(700, 20, 41, 31));
        loop2 = new QPushButton(commandFrame);
        loop2->setObjectName(QStringLiteral("loop2"));
        loop2->setGeometry(QRect(740, 20, 41, 31));
        loop3 = new QPushButton(commandFrame);
        loop3->setObjectName(QStringLiteral("loop3"));
        loop3->setGeometry(QRect(780, 20, 41, 31));
        loopSpinBox = new QSpinBox(commandFrame);
        loopSpinBox->setObjectName(QStringLiteral("loopSpinBox"));
        loopSpinBox->setGeometry(QRect(760, 0, 31, 22));
        loopSpinBox->setMinimum(1);
        loopSpinBox->setMaximum(4);
        restartButton = new QPushButton(commandFrame);
        restartButton->setObjectName(QStringLiteral("restartButton"));
        restartButton->setGeometry(QRect(850, 10, 81, 41));
        if12 = new QPushButton(commandFrame);
        if12->setObjectName(QStringLiteral("if12"));
        if12->setGeometry(QRect(600, 20, 41, 31));
        if22 = new QPushButton(commandFrame);
        if22->setObjectName(QStringLiteral("if22"));
        if22->setGeometry(QRect(680, 20, 41, 31));
        logoutButton = new QPushButton(centralWidget);
        logoutButton->setObjectName(QStringLiteral("logoutButton"));
        logoutButton->setGeometry(QRect(50, 10, 75, 31));
        levelFrame = new QFrame(centralWidget);
        levelFrame->setObjectName(QStringLiteral("levelFrame"));
        levelFrame->setGeometry(QRect(60, 60, 880, 430));
        levelFrame->setStyleSheet(QStringLiteral("background: rgb(226, 226, 226)"));
        levelFrame->setFrameShape(QFrame::Box);
        levelFrame->setFrameShadow(QFrame::Plain);
        gridLayoutWidget_2 = new QWidget(levelFrame);
        gridLayoutWidget_2->setObjectName(QStringLiteral("gridLayoutWidget_2"));
        gridLayoutWidget_2->setGeometry(QRect(9, 9, 861, 411));
        levelLayout = new QGridLayout(gridLayoutWidget_2);
        levelLayout->setSpacing(6);
        levelLayout->setContentsMargins(11, 11, 11, 11);
        levelLayout->setObjectName(QStringLiteral("levelLayout"));
        levelLayout->setContentsMargins(0, 0, 0, 0);
        level3Layout = new QVBoxLayout();
        level3Layout->setSpacing(6);
        level3Layout->setObjectName(QStringLiteral("level3Layout"));
        level3Label = new QLabel(gridLayoutWidget_2);
        level3Label->setObjectName(QStringLiteral("level3Label"));
        level3Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level3Layout->addWidget(level3Label);

        level3Button = new QPushButton(gridLayoutWidget_2);
        level3Button->setObjectName(QStringLiteral("level3Button"));
        level3Button->setMinimumSize(QSize(0, 100));

        level3Layout->addWidget(level3Button);


        levelLayout->addLayout(level3Layout, 0, 2, 1, 1);

        level7Layout = new QVBoxLayout();
        level7Layout->setSpacing(6);
        level7Layout->setObjectName(QStringLiteral("level7Layout"));
        level7Label = new QLabel(gridLayoutWidget_2);
        level7Label->setObjectName(QStringLiteral("level7Label"));
        level7Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level7Layout->addWidget(level7Label);

        level7Button = new QPushButton(gridLayoutWidget_2);
        level7Button->setObjectName(QStringLiteral("level7Button"));
        level7Button->setMinimumSize(QSize(0, 100));

        level7Layout->addWidget(level7Button);


        levelLayout->addLayout(level7Layout, 2, 0, 1, 1);

        level2Layout = new QVBoxLayout();
        level2Layout->setSpacing(6);
        level2Layout->setObjectName(QStringLiteral("level2Layout"));
        level2Label = new QLabel(gridLayoutWidget_2);
        level2Label->setObjectName(QStringLiteral("level2Label"));
        level2Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level2Layout->addWidget(level2Label);

        level2Button = new QPushButton(gridLayoutWidget_2);
        level2Button->setObjectName(QStringLiteral("level2Button"));
        level2Button->setMinimumSize(QSize(0, 100));

        level2Layout->addWidget(level2Button);


        levelLayout->addLayout(level2Layout, 0, 1, 1, 1);

        level4Layout = new QVBoxLayout();
        level4Layout->setSpacing(6);
        level4Layout->setObjectName(QStringLiteral("level4Layout"));
        level4Label = new QLabel(gridLayoutWidget_2);
        level4Label->setObjectName(QStringLiteral("level4Label"));
        level4Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level4Layout->addWidget(level4Label);

        level4Button = new QPushButton(gridLayoutWidget_2);
        level4Button->setObjectName(QStringLiteral("level4Button"));
        level4Button->setMinimumSize(QSize(0, 100));

        level4Layout->addWidget(level4Button);


        levelLayout->addLayout(level4Layout, 1, 0, 1, 1);

        level1Layout = new QVBoxLayout();
        level1Layout->setSpacing(6);
        level1Layout->setObjectName(QStringLiteral("level1Layout"));
        level1Label = new QLabel(gridLayoutWidget_2);
        level1Label->setObjectName(QStringLiteral("level1Label"));
        level1Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level1Layout->addWidget(level1Label);

        level1Button = new QPushButton(gridLayoutWidget_2);
        level1Button->setObjectName(QStringLiteral("level1Button"));
        level1Button->setMinimumSize(QSize(0, 100));

        level1Layout->addWidget(level1Button);


        levelLayout->addLayout(level1Layout, 0, 0, 1, 1);

        level5Layout = new QVBoxLayout();
        level5Layout->setSpacing(6);
        level5Layout->setObjectName(QStringLiteral("level5Layout"));
        level5Label = new QLabel(gridLayoutWidget_2);
        level5Label->setObjectName(QStringLiteral("level5Label"));
        level5Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level5Layout->addWidget(level5Label);

        level5Button = new QPushButton(gridLayoutWidget_2);
        level5Button->setObjectName(QStringLiteral("level5Button"));
        level5Button->setMinimumSize(QSize(0, 100));

        level5Layout->addWidget(level5Button);


        levelLayout->addLayout(level5Layout, 1, 1, 1, 1);

        level6Layout = new QVBoxLayout();
        level6Layout->setSpacing(6);
        level6Layout->setObjectName(QStringLiteral("level6Layout"));
        level6Label = new QLabel(gridLayoutWidget_2);
        level6Label->setObjectName(QStringLiteral("level6Label"));
        level6Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level6Layout->addWidget(level6Label);

        level6Button = new QPushButton(gridLayoutWidget_2);
        level6Button->setObjectName(QStringLiteral("level6Button"));
        level6Button->setMinimumSize(QSize(0, 100));

        level6Layout->addWidget(level6Button);


        levelLayout->addLayout(level6Layout, 1, 2, 1, 1);

        level9Layout = new QVBoxLayout();
        level9Layout->setSpacing(6);
        level9Layout->setObjectName(QStringLiteral("level9Layout"));
        level9Label = new QLabel(gridLayoutWidget_2);
        level9Label->setObjectName(QStringLiteral("level9Label"));
        level9Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level9Layout->addWidget(level9Label);

        level9Button = new QPushButton(gridLayoutWidget_2);
        level9Button->setObjectName(QStringLiteral("level9Button"));
        level9Button->setMinimumSize(QSize(0, 100));

        level9Layout->addWidget(level9Button);


        levelLayout->addLayout(level9Layout, 2, 2, 1, 1);

        level8Layout = new QVBoxLayout();
        level8Layout->setSpacing(6);
        level8Layout->setObjectName(QStringLiteral("level8Layout"));
        level8Label = new QLabel(gridLayoutWidget_2);
        level8Label->setObjectName(QStringLiteral("level8Label"));
        level8Label->setStyleSheet(QStringLiteral("qproperty-alignment: AlignCenter;"));

        level8Layout->addWidget(level8Label);

        level8Button = new QPushButton(gridLayoutWidget_2);
        level8Button->setObjectName(QStringLiteral("level8Button"));
        level8Button->setMinimumSize(QSize(0, 100));

        level8Layout->addWidget(level8Button);


        levelLayout->addLayout(level8Layout, 2, 1, 1, 1);

        userStatsLabel = new QLabel(centralWidget);
        userStatsLabel->setObjectName(QStringLiteral("userStatsLabel"));
        userStatsLabel->setGeometry(QRect(220, 12, 851, 21));
        QFont font;
        font.setPointSize(14);
        font.setBold(true);
        font.setWeight(75);
        userStatsLabel->setFont(font);
        sequenceFrame = new QFrame(centralWidget);
        sequenceFrame->setObjectName(QStringLiteral("sequenceFrame"));
        sequenceFrame->setGeometry(QRect(989, 49, 101, 521));
        sequenceFrame->setFrameShape(QFrame::StyledPanel);
        sequenceFrame->setFrameShadow(QFrame::Raised);
        verticalLayoutWidget = new QWidget(sequenceFrame);
        verticalLayoutWidget->setObjectName(QStringLiteral("verticalLayoutWidget"));
        verticalLayoutWidget->setGeometry(QRect(0, 20, 101, 431));
        sequenceLayout = new QVBoxLayout(verticalLayoutWidget);
        sequenceLayout->setSpacing(6);
        sequenceLayout->setContentsMargins(11, 11, 11, 11);
        sequenceLayout->setObjectName(QStringLiteral("sequenceLayout"));
        sequenceLayout->setContentsMargins(0, 0, 0, 0);
        runButton = new QPushButton(sequenceFrame);
        runButton->setObjectName(QStringLiteral("runButton"));
        runButton->setGeometry(QRect(10, 470, 75, 41));
        sequenceLabel = new QLabel(sequenceFrame);
        sequenceLabel->setObjectName(QStringLiteral("sequenceLabel"));
        sequenceLabel->setGeometry(QRect(0, 0, 61, 16));
        scoreFrame = new QFrame(centralWidget);
        scoreFrame->setObjectName(QStringLiteral("scoreFrame"));
        scoreFrame->setGeometry(QRect(50, 50, 901, 451));
        scoreFrame->setStyleSheet(QStringLiteral("background: rgb(226, 226, 226)"));
        scoreFrame->setFrameShape(QFrame::StyledPanel);
        scoreFrame->setFrameShadow(QFrame::Raised);
        scoreGrid = new QFrame(scoreFrame);
        scoreGrid->setObjectName(QStringLiteral("scoreGrid"));
        scoreGrid->setGeometry(QRect(280, 120, 311, 231));
        scoreGrid->setFrameShape(QFrame::StyledPanel);
        scoreGrid->setFrameShadow(QFrame::Raised);
        gridLayoutWidget_3 = new QWidget(scoreGrid);
        gridLayoutWidget_3->setObjectName(QStringLiteral("gridLayoutWidget_3"));
        gridLayoutWidget_3->setGeometry(QRect(20, 10, 271, 191));
        scoreLayout = new QGridLayout(gridLayoutWidget_3);
        scoreLayout->setSpacing(6);
        scoreLayout->setContentsMargins(11, 11, 11, 11);
        scoreLayout->setObjectName(QStringLiteral("scoreLayout"));
        scoreLayout->setContentsMargins(0, 0, 0, 0);
        scoreValue = new QLabel(gridLayoutWidget_3);
        scoreValue->setObjectName(QStringLiteral("scoreValue"));
        scoreValue->setFont(font);
        scoreValue->setFrameShape(QFrame::NoFrame);

        scoreLayout->addWidget(scoreValue, 1, 1, 1, 1);

        highschoolValue = new QLabel(gridLayoutWidget_3);
        highschoolValue->setObjectName(QStringLiteral("highschoolValue"));
        highschoolValue->setFont(font);
        highschoolValue->setFrameShape(QFrame::NoFrame);
        highschoolValue->setLineWidth(1);

        scoreLayout->addWidget(highschoolValue, 2, 1, 1, 1);

        continueButton = new QPushButton(gridLayoutWidget_3);
        continueButton->setObjectName(QStringLiteral("continueButton"));

        scoreLayout->addWidget(continueButton, 3, 1, 1, 1);

        highscoreLabel = new QLabel(gridLayoutWidget_3);
        highscoreLabel->setObjectName(QStringLiteral("highscoreLabel"));
        highscoreLabel->setFont(font);
        highscoreLabel->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);

        scoreLayout->addWidget(highscoreLabel, 2, 0, 1, 1);

        scoreLabel = new QLabel(gridLayoutWidget_3);
        scoreLabel->setObjectName(QStringLiteral("scoreLabel"));
        scoreLabel->setFont(font);
        scoreLabel->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);

        scoreLayout->addWidget(scoreLabel, 1, 0, 1, 1);

        homeButton = new QPushButton(centralWidget);
        homeButton->setObjectName(QStringLiteral("homeButton"));
        homeButton->setGeometry(QRect(130, 10, 75, 31));
        MainWindow->setCentralWidget(centralWidget);
        commandFrame->raise();
        logoutButton->raise();
        userStatsLabel->raise();
        sequenceFrame->raise();
        homeButton->raise();
        scoreFrame->raise();
        loginFrame->raise();
        levelFrame->raise();
        menuBar = new QMenuBar(MainWindow);
        menuBar->setObjectName(QStringLiteral("menuBar"));
        menuBar->setGeometry(QRect(0, 0, 1117, 26));
        MainWindow->setMenuBar(menuBar);
        mainToolBar = new QToolBar(MainWindow);
        mainToolBar->setObjectName(QStringLiteral("mainToolBar"));
        MainWindow->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(MainWindow);
        statusBar->setObjectName(QStringLiteral("statusBar"));
        MainWindow->setStatusBar(statusBar);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "MainWindow", 0));
        loginFailedLabel->setText(QApplication::translate("MainWindow", "Login failed! Please try again.", 0));
        createLoginButton->setText(QApplication::translate("MainWindow", "Sign up!", 0));
        createLoginLabel->setText(QApplication::translate("MainWindow", "Are you a new user?", 0));
        usernameLabel->setText(QApplication::translate("MainWindow", "Username:", 0));
        loginButton->setText(QApplication::translate("MainWindow", "Login", 0));
        label_4->setText(QString());
        label_3->setText(QString());
        passwordLabel->setText(QApplication::translate("MainWindow", "Password:", 0));
        instructorLabel->setText(QApplication::translate("MainWindow", "Instructor?", 0));
        instructorNameLabel->setText(QApplication::translate("MainWindow", "Instructor:", 0));
        lastNameLabel->setText(QApplication::translate("MainWindow", "Last name:", 0));
        yesBox->setText(QApplication::translate("MainWindow", "Yes", 0));
        noBox->setText(QApplication::translate("MainWindow", "No", 0));
        firstNameLabel->setText(QApplication::translate("MainWindow", "First name:", 0));
        signUpFinalButton->setText(QApplication::translate("MainWindow", "Sign Up", 0));
        cancelButton->setText(QApplication::translate("MainWindow", "Cancel", 0));
        forwardButton->setText(QApplication::translate("MainWindow", "Forward();", 0));
        jumpButton->setText(QApplication::translate("MainWindow", "Jump();", 0));
        waitButton->setText(QApplication::translate("MainWindow", "Wait();", 0));
        ifButton->setText(QApplication::translate("MainWindow", "If()", 0));
        loopButton->setText(QApplication::translate("MainWindow", "Loop()", 0));
        removeButton->setText(QApplication::translate("MainWindow", "Remove Last", 0));
        commandsLabel->setText(QApplication::translate("MainWindow", "Commands:", 0));
        label->setText(QApplication::translate("MainWindow", "(if claw)", 0));
        label_2->setText(QApplication::translate("MainWindow", "(loop box)", 0));
        if1->setText(QString());
        if2->setText(QString());
        loop1->setText(QString());
        loop2->setText(QString());
        loop3->setText(QString());
        restartButton->setText(QApplication::translate("MainWindow", "Restart Level", 0));
        if12->setText(QString());
        if22->setText(QString());
        logoutButton->setText(QApplication::translate("MainWindow", "Logout?", 0));
        level3Label->setText(QApplication::translate("MainWindow", "Level 3:", 0));
        level3Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level7Label->setText(QApplication::translate("MainWindow", "Level 7:", 0));
        level7Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level2Label->setText(QApplication::translate("MainWindow", "Level 2:", 0));
        level2Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level4Label->setText(QApplication::translate("MainWindow", "Level 4:", 0));
        level4Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level1Label->setText(QApplication::translate("MainWindow", "Level 1:", 0));
        level1Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level5Label->setText(QApplication::translate("MainWindow", "Level 5:", 0));
        level5Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level6Label->setText(QApplication::translate("MainWindow", "Level 6:", 0));
        level6Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level9Label->setText(QApplication::translate("MainWindow", "Level 9:", 0));
        level9Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        level8Label->setText(QApplication::translate("MainWindow", "Level 8:", 0));
        level8Button->setText(QApplication::translate("MainWindow", "PushButton", 0));
        userStatsLabel->setText(QApplication::translate("MainWindow", "Welcome", 0));
        runButton->setText(QApplication::translate("MainWindow", "Run it!!", 0));
        sequenceLabel->setText(QApplication::translate("MainWindow", "Sequence:", 0));
        scoreValue->setText(QString());
        highschoolValue->setText(QString());
        continueButton->setText(QApplication::translate("MainWindow", "Continue", 0));
        highscoreLabel->setText(QApplication::translate("MainWindow", "Highscore:", 0));
        scoreLabel->setText(QApplication::translate("MainWindow", "Score:", 0));
        homeButton->setText(QApplication::translate("MainWindow", "Home", 0));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
