#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <SFML/Graphics.hpp>
#include <level.h>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    model = new Model();
    myWorld = new World(ui->centralWidget, QPoint(50, 50), QSize(900, 450));
    myWorld->hide();

    switchToLoginMode();
    connectEverything();

    //gameState = 0;
}

MainWindow::~MainWindow()
{
    delete ui;
}

// The form hides/shows correct panels for Game frame to appear
void MainWindow::switchToHomeMode()
{
    // Clear the fields
    ui->usernameField->setText(QString(""));
    ui->passwordField->setText(QString(""));

    ui->loginFrame->hide();
    ui->sequenceFrame->hide();
    ui->commandFrame->hide();
    ui->sequenceFrame->hide();
    ui->levelFrame->show();
    ui->levelFrame->raise();
    ui->logoutButton->show();
    ui->homeButton->show();
    ui->userStatsLabel->show();
}

// The form hides/shows correct panels for Login frame to appear
void MainWindow::switchToLoginMode()
{
    ui->levelFrame->hide();
    ui->logoutButton->hide();
    ui->homeButton->hide();
    ui->loginFailedLabel->hide();
    ui->commandFrame->hide();
    ui->userStatsLabel->hide();
    ui->sequenceFrame->hide();
    ui->loginFrame->show();
    ui->scoreFrame->hide();
    ui->firstNameField->hide();
    ui->firstNameLabel->hide();
    ui->lastNameField->hide();
    ui->lastNameLabel->hide();
    ui->instructorLabel->hide();
    ui->noBox->hide();
    ui->yesBox->hide();
    ui->cancelButton->hide();
    ui->instructorNameLabel->hide();
    ui->instructorNameField->hide();
    ui->createLoginButton->show();
    ui->createLoginLabel->show();
    ui->loginButton->show();
    ui->signUpFinalButton->hide();
}

void MainWindow::switchToPlayMode()
{
    ui->levelFrame->hide();
    ui->scoreFrame->lower();
    ui->sequenceFrame->show();
    ui->commandFrame->show();
    ui->runButton->setEnabled(true);
}

// Clear the current sequence
void MainWindow::clearSequence()
{
    size_t startingSize = sequenceButtons.size();
    for (size_t i = 0; i < startingSize; i++)
    {
        on_removeLastButton_clicked();
    }
}

// Switch panel for running each command
void MainWindow::runSwitch(int action)
{
    switch (action)
        {
            case 0:
                //myWorld->forwardFlag = true;
                myWorld->insertCommand(0);
                break;
            case 1:
               // myWorld->jumpFlag = true;
                myWorld->insertCommand(1);
                break;
            case 2:
                //myWorld->waitFlag = true;
                myWorld->insertCommand(2);
                myWorld->insertCommand(2);
                break;

            default:
                return;
        }
}



// ***** SLOTS *****

void MainWindow::connectEverything()
{
    connect(ui->loginButton, SIGNAL(clicked(bool)), this, SLOT(loginClicked()));
    connect(ui->createLoginButton, SIGNAL(clicked(bool)), this, SLOT(signUpClicked()));
    connect(ui->logoutButton, SIGNAL(clicked(bool)), this, SLOT(logoutClicked()));
    connect(ui->signUpFinalButton, SIGNAL(clicked(bool)), this, SLOT(signUpFinalClicked()));
    connect(ui->yesBox, SIGNAL(clicked(bool)), this, SLOT(on_yesBox_clicked()));
    connect(ui->noBox, SIGNAL(clicked(bool)), this, SLOT(on_noBox_clicked()));
    connect(ui->cancelButton, SIGNAL(clicked(bool)), this, SLOT(on_cancelButton_Clicked()));
    connect(ui->homeButton, SIGNAL(clicked(bool)), this, SLOT(homeClicked()));
    connect(ui->level1Button, SIGNAL(clicked(bool)), this, SLOT(on_level1Button_clicked()));
    connect(ui->removeButton, SIGNAL(clicked(bool)), this, SLOT(on_removeLastButton_clicked()));
    connect(ui->if1, SIGNAL(clicked(bool)), this, SLOT(on_if1_clicked()));
    connect(ui->if2, SIGNAL(clicked(bool)), this, SLOT(on_if2_clicked()));
    connect(ui->loop1, SIGNAL(clicked(bool)), this, SLOT(on_loop1_clicked()));
    connect(ui->loop2, SIGNAL(clicked(bool)), this, SLOT(on_loop2_clicked()));
    connect(ui->loop3, SIGNAL(clicked(bool)), this, SLOT(on_loop3_clicked()));
    connect(this, SIGNAL(setGameState(int)), myWorld, SLOT(gameStateChanged(int)));
    connect(myWorld, SIGNAL(getStats()), this, SLOT(showStats()));
    connect(myWorld, SIGNAL(restartLevel()), this, SLOT(reloadLevel()));
    connect(model,SIGNAL(loginFailedSignal()), this, SLOT(loginFailed()));
    connect(model,SIGNAL(loginSucceededSignal()), this, SLOT(loginSucceeded()));
    connect(model,SIGNAL(createUserFailedSignal()), this, SLOT(createUserFailed()));
    connect(model,SIGNAL(createUserSucceededSignal()), this, SLOT(createUserSucceeded()));
    //connect(model,SIGNAL(saveFailedSignal()), this, SLOT(saveFailed()));
    //connect(model,SIGNAL(saveSucceededSignal()), this, SLOT(saveSucceeded()));
    connect(model,SIGNAL(noSuchTeacherSignal()), this, SLOT(noSuchTeacher()));
    connect(model,SIGNAL(badConnectionSignal()), this, SLOT(connectionFailed()));
}

// Slot for when the login button is clicked.
void MainWindow::loginClicked()
{
    ui->loginFailedLabel->hide();

    if (ui->usernameField->text() == "" || ui->passwordField->text() == "")
        return;

    // AMIT AND JACKSON -- FILL THIS IN
    model->attemptSQLLogin(ui->usernameField->text(), ui->passwordField->text());

}

// Slot for when a new user needs to be created.
void MainWindow::signUpClicked()
{
    ui->loginFailedLabel->hide();
    ui->createLoginButton->hide();
    ui->createLoginLabel->hide();
    ui->loginButton->hide();
    ui->signUpFinalButton->show();
    ui->firstNameField->show();
    ui->firstNameLabel->show();
    ui->lastNameField->show();
    ui->lastNameLabel->show();
    ui->instructorLabel->show();
    ui->noBox->show();
    ui->yesBox->show();
    ui->cancelButton->showFullScreen();
    ui->instructorNameLabel->show();
    ui->instructorNameField->show();
    ui->instructorNameField->setEnabled(false);
}

void MainWindow::signUpFinalClicked()
{
    // Ignore clicks with nothing entered for UN/PW
    if (ui->usernameField->text() == "" || ui->passwordField->text() == "" || (!ui->yesBox->isChecked() && !ui->noBox->isChecked()))
        return;
    bool isTeacher = ui->yesBox->isChecked();
    QString fullName = "";
    fullName.append(ui->firstNameField->text());
    fullName.append(" " + ui->lastNameField->text());
    // AMIT AND JACKSON -- FILL THIS IN
    model->createSQLLogin(fullName, ui->usernameField->text(), ui->passwordField->text(), isTeacher, ui->instructorNameField->text());

    // In case of emergency -- Offline Login path will work like a charm <3
    //if (model->offlineLogin())
        //switchToGameMode();
}

void MainWindow::on_cancelButton_Clicked()
{
    switchToLoginMode();
}

// Slot for when the log out button is clicked
void MainWindow::logoutClicked()
{
    // TODO: Save info in Model

    switchToLoginMode();

    myWorld->RenderWindow::setVisible(false);
    myWorld->hide();

}

void MainWindow::on_yesBox_clicked()
{
    ui->noBox->setChecked(false);
    ui->instructorNameField->setEnabled(false);
    ui->instructorNameField->setText("");
}

void MainWindow::on_noBox_clicked()
{
    ui->yesBox->setChecked(false);
    ui->instructorNameField->setEnabled(true);
}

void MainWindow::homeClicked()
{
    switchToHomeMode();
    clearSequence();
}

void MainWindow::on_continueButton_clicked()
{
    ui->sequenceFrame->hide();
    ui->scoreFrame->lower();
    ui->levelFrame->show();
    ui->levelFrame->raise();
    clearSequence();
}

void MainWindow::on_restartButton_clicked()
{
    myWorld->levelLost();
}

void MainWindow::showStats()
{
    int score = 500 - 25*sequence.size();
    for (int i = 0; i < sequence.size(); i++)
    {
        if (sequence.at(i) == 4)
            score = score - 2*(ui->loopSpinBox->value());
    }

    ui->scoreValue->setText(QString::number(score));
    model->saveProgress(currLevel, score);

    ui->levelFrame->hide();
    ui->levelFrame->lower();
    ui->scoreFrame->raise();
    ui->scoreFrame->show();
    ui->commandFrame->hide();
    ui->sequenceFrame->hide();
}

void MainWindow::reloadLevel()
{
    ui->runButton->setEnabled(true);

    if (myWorld->getGameState() == 1)
    {
        MainWindow::on_level1Button_clicked();
    }
    else if (myWorld->getGameState() == 2)
    {
        MainWindow::on_level2Button_clicked();
    }
    else if (myWorld->getGameState() == 3)
    {
        MainWindow::on_level3Button_clicked();
    }
    else if (myWorld->getGameState() == 4)
    {
        MainWindow::on_level4Button_clicked();
    }
    else if (myWorld->getGameState() == 5)
    {
        MainWindow::on_level5Button_clicked();
    }
    else if (myWorld->getGameState() == 6)
    {
        MainWindow::on_level6Button_clicked();
    }
    else if (myWorld->getGameState() == 7)
    {
        MainWindow::on_level7Button_clicked();
    }
    else if (myWorld->getGameState() == 8)
    {
        MainWindow::on_level8Button_clicked();
    }
    else if (myWorld->getGameState() == 9)
    {
        MainWindow::on_level9Button_clicked();
    }
}

void MainWindow::on_forwardButton_clicked()
{
    // Add a new action to the sequence
    QPushButton *forwardSeq = new QPushButton(ui->sequenceFrame);
    forwardSeq->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
    forwardSeq->setIconSize(QSize(40, 40));
    ui->sequenceLayout->addWidget(forwardSeq);
    sequenceButtons.push_back(forwardSeq);
    sequence.push_back(0);
}

void MainWindow::on_jumpButton_clicked()
{
    // Add a new action to the sequence
    QPushButton *jumpSeq = new QPushButton(ui->sequenceFrame);
    jumpSeq->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
    jumpSeq->setIconSize(QSize(40, 40));
    ui->sequenceLayout->addWidget(jumpSeq);
    sequenceButtons.push_back(jumpSeq);
    sequence.push_back(1);
}

void MainWindow::on_waitButton_clicked()
{
    // Add a new action to the sequence
    QPushButton *waitSeq = new QPushButton(ui->sequenceFrame);
    waitSeq->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
    waitSeq->setIconSize(QSize(40, 40));
    ui->sequenceLayout->addWidget(waitSeq);
    sequenceButtons.push_back(waitSeq);
    sequence.push_back(2);
}

void MainWindow::on_ifButton_clicked()
{
    // Just adds whatever is inside of the if claw to the sequence

    if (if1Action == -1 || if2Action == -1)
        return;

    QPushButton *ifSeq = new QPushButton(ui->sequenceFrame);
    ifSeq->setIcon(QIcon(":/icons/icons/ifIcon.ico"));
    ifSeq->setIconSize(QSize(40, 40));
    ui->sequenceLayout->addWidget(ifSeq);
    sequenceButtons.push_back(ifSeq);
    sequence.push_back(3);
}

void MainWindow::on_loopButton_clicked()
{
    // Just adds whatever is inside of the loopbox to the sequence

    if (loop1Action == -1 || loop2Action == -1 || loop3Action == -1)
        return;

    QPushButton *loopSeq = new QPushButton(ui->sequenceFrame);
    loopSeq->setIcon(QIcon(":/icons/icons/loopIcon.ico"));
    loopSeq->setIconSize(QSize(40, 40));
    ui->sequenceLayout->addWidget(loopSeq);
    sequenceButtons.push_back(loopSeq);
    sequence.push_back(4);
}

// Response to clicking the Remove Last button from the view
void MainWindow::on_removeLastButton_clicked()
{
    if (sequence.size() != 0 && sequenceButtons.size() != 0)
    {
        sequence.removeLast();
        ui->sequenceLayout->removeWidget(sequenceButtons.last());
        sequenceButtons.last()->hide();
        sequenceButtons.removeLast();
    }
}

// Response to clicking the run button
void MainWindow::on_runButton_clicked()
{
    ui->runButton->setEnabled(false);

    if (sequence.size() != 0 && sequenceButtons.size() != 0)
    {
        for (int i = 0; i < sequence.size(); i++)
        {
            nextSequence();
        }
    }
}

// Run the switch for the next command in the sequence
void MainWindow::nextSequence()
{
    if (sequence.at(index) == 3) // IF
    {
        if (!myWorld->getLight())
        {
            runSwitch(if1Action);
            runSwitch(if12Action);
        }
        else
        {
            runSwitch(if2Action);
            runSwitch(if22Action);
        }

    }
    else if (sequence.at(index) == 4) // LOOP
    {
        for (int i = 0; i < ui->loopSpinBox->value(); i++)
        {
            runSwitch(loop1Action);
            runSwitch(loop2Action);
            runSwitch(loop3Action);
        }
    }
    else // SINGLE COMMAND
    {
        runSwitch(sequence.at(index));
    }

    // Reset index if we're at the end of the sequence
    if (index >= sequence.size() - 1)
    {
        index = 0;
    }
    else
    {
        index++;

        // Reset the flags in myWorld
        myWorld->forwardFlag = false;
        myWorld->jumpFlag = false;
        myWorld->waitFlag = false;
    }
}

// First button in the if claw
void MainWindow::on_if1_clicked()
{
    // set the action that it represents
    if (if1Action >= 2)
        if1Action = 0;
    else
        if1Action++;

    // roll back the icons
    if (if1Action == 0)
    {
        ui->if1->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->if1->setIconSize(QSize(25, 25));
    }
    else if (if1Action == 1)
    {
        ui->if1->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->if1->setIconSize(QSize(25, 25));
    }
    else if (if1Action == 2)
    {
        ui->if1->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->if1->setIconSize(QSize(25, 25));
    }
}

// First button in the if claw
void MainWindow::on_if12_clicked()
{
    // set the action that it represents
    if (if12Action >= 2)
        if12Action = 0;
    else
        if12Action++;

    // roll back the icons
    if (if12Action == 0)
    {
        ui->if12->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->if12->setIconSize(QSize(25, 25));
    }
    else if (if12Action == 1)
    {
        ui->if12->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->if12->setIconSize(QSize(25, 25));
    }
    else if (if12Action == 2)
    {
        ui->if12->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->if12->setIconSize(QSize(25, 25));
    }
}

// Second button in the if claw
void MainWindow::on_if2_clicked()
{
    // set the action that it represents
    if (if2Action >= 2)
        if2Action = 0;
    else
        if2Action++;

    // roll back the icons
    if (if2Action == 0)
    {
        ui->if2->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->if2->setIconSize(QSize(25, 25));
    }
    else if (if2Action == 1)
    {
        ui->if2->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->if2->setIconSize(QSize(25, 25));
    }
    else if (if2Action == 2)
    {
        ui->if2->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->if2->setIconSize(QSize(25, 25));
    }
}

// Second button in the if claw
void MainWindow::on_if22_clicked()
{
    // set the action that it represents
    if (if22Action >= 2)
        if22Action = 0;
    else
        if22Action++;

    // roll back the icons
    if (if22Action == 0)
    {
        ui->if22->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->if22->setIconSize(QSize(25, 25));
    }
    else if (if22Action == 1)
    {
        ui->if22->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->if22->setIconSize(QSize(25, 25));
    }
    else if (if22Action == 2)
    {
        ui->if22->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->if22->setIconSize(QSize(25, 25));
    }
}

// First button in the loop claw
void MainWindow::on_loop1_clicked()
{
    // roll the action that it represents
    if (loop1Action >= 2)
        loop1Action = 0;
    else
        loop1Action++;

    // roll back the icons
    if (loop1Action == 0)
    {
        ui->loop1->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->loop1->setIconSize(QSize(25, 25));
    }
    else if (loop1Action == 1)
    {
        ui->loop1->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->loop1->setIconSize(QSize(25, 25));
    }
    else if (loop1Action == 2)
    {
        ui->loop1->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->loop1->setIconSize(QSize(25, 25));
    }
}

// Second button in the loop claw
void MainWindow::on_loop2_clicked()
{
    // roll the action that it represents
    if (loop2Action >= 2)
        loop2Action = 0;
    else
        loop2Action++;

    // roll back the icons
    if (loop2Action == 0)
    {
        ui->loop2->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->loop2->setIconSize(QSize(25, 25));
    }
    else if (loop2Action == 1)
    {
        ui->loop2->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->loop2->setIconSize(QSize(25, 25));
    }
    else if (loop2Action == 2)
    {
        ui->loop2->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->loop2->setIconSize(QSize(25, 25));
    }
}

// Third button in the loop claw
void MainWindow::on_loop3_clicked()
{
    // roll the action that it represents
    if (loop3Action >= 2)
        loop3Action = 0;
    else
        loop3Action++;

    // roll back the icons
    if (loop3Action == 0)
    {
        ui->loop3->setIcon(QIcon(":/icons/icons/forwardIcon.ico"));
        ui->loop3->setIconSize(QSize(25, 25));
    }
    else if (loop3Action == 1)
    {
        ui->loop3->setIcon(QIcon(":/icons/icons/jumpIcon.ico"));
        ui->loop3->setIconSize(QSize(25, 25));
    }
    else if (loop3Action == 2)
    {
        ui->loop3->setIcon(QIcon(":/icons/icons/waitIcon.ico"));
        ui->loop3->setIconSize(QSize(25, 25));
    }
}

void MainWindow::on_level1Button_clicked()
{
    switchToPlayMode();
    currLevel = 1;

    Level1* level = new Level1(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(1);
    myWorld->setLevel();
}
void MainWindow::on_level2Button_clicked()
{
    switchToPlayMode();
    currLevel = 2;

    Level2* level = new Level2(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(2);
    myWorld->setLevel();
}

void MainWindow::on_level3Button_clicked()
{
    switchToPlayMode();
    currLevel = 3;

    Level3* level = new Level3(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(3);
    myWorld->setLevel();
}

void MainWindow::on_level4Button_clicked()
{
    switchToPlayMode();
    currLevel = 4;

    Level4* level = new Level4(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(4);
    myWorld->setLevel();
}

void MainWindow::on_level5Button_clicked()
{
    switchToPlayMode();
    currLevel = 5;

    Level5* level = new Level5(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(5);
    myWorld->setLevel();
}

void MainWindow::on_level6Button_clicked()
{
    switchToPlayMode();
    currLevel = 6;

    Level6* level = new Level6(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(6);
    myWorld->setLevel();
}

void MainWindow::on_level7Button_clicked()
{
    switchToPlayMode();
    currLevel = 7;

    Level7* level = new Level7(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(7);
    myWorld->setLevel();
}

void MainWindow::on_level8Button_clicked()
{
    switchToPlayMode();
    currLevel = 8;

    Level8* level = new Level8(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(8);
    myWorld->setLevel();
}

void MainWindow::on_level9Button_clicked()
{
    switchToPlayMode();
    currLevel = 9;

    Level9* level = new Level9(myWorld);
    myWorld->createWorld(level);
    myWorld->show();

    myWorld->RenderWindow::setVisible(true);
    emit setGameState(9);
    myWorld->setLevel();
}



// ***** SERVER SLOTS *****

// Slot called when server's response comes back for validating user login
void MainWindow::loginSucceeded(){
    switchToHomeMode();
    ui->userStatsLabel->setText(QString("Welcome " + model->thisUser->username));
}

// Slot called when server's response comes back for validating user login
void MainWindow::loginFailed(){
    ui->loginFailedLabel->setText(QString("Login failed! Please try again."));
    ui->loginFailedLabel->show();
}

void MainWindow::connectionFailed(){
    ui->loginFailedLabel->setText(QString("Oops! Unable to connect.\nFeel free to login as a Guest. "));
    ui->loginFailedLabel->show();
}

void MainWindow::createUserSucceeded(){
    switchToHomeMode();
    ui->userStatsLabel->setText(QString("Welcome " + model->thisUser->username));
}

void MainWindow::createUserFailed(){
    ui->loginFailedLabel->setText(QString("This username already exists. Please try again."));
    ui->loginFailedLabel->show();
}
void MainWindow::noSuchTeacher() {
    ui->loginFailedLabel->setText(QString("That teacher does not exist."));
    ui->loginFailedLabel->show();
}


