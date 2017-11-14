#include "world.h"
#include <iostream>
#include <string>
#include <QDir>
#include <stdlib.h>
#include <Box2D/Box2D.h>
#include <worlditem.h>
#include <SFML/Graphics.hpp>
#include <level.h>
#include <QTime>


using namespace std;

World::World(QWidget* Parent, const QPoint& Position, const QSize& Size) : QSFMLCanvas(Parent, Position, Size)
{
    // Assign world values
    worldX = Position.x();
    worldY = Position.y();
    worldW = Size.width();
    worldH = Size.height();
    commandFinished = true;
    command = 0;
    waitTime = 0;
    level = nullptr;

    gameState = 0;
    lvl1.loadFromFile("../textures/Level1.png");
    lvl2.loadFromFile("../textures/Level2.png");
    lvl3.loadFromFile("../textures/Level3.png");
    lvl4.loadFromFile("../textures/Level4.png");
    lvl5.loadFromFile("../textures/Level5.png");
    lvl6.loadFromFile("../textures/Level6.png");
    lvl7.loadFromFile("../textures/Level7.png");
    lvl8.loadFromFile("../textures/Level8.png");
    lvl9.loadFromFile("../textures/Level9.png");
    codeyIdle.loadFromFile("../textures/Codey.png");
    codeyJump.loadFromFile("../textures/Codey_JumpUp2.png");
    codeyForward.loadFromFile("../textures/Codey_JumpForward2.png");


    //William's Code
    m_world = std::make_unique<b2World>(b2Vec2(0.0f,100.81f));
    m_world->SetContactListener(&myContactListenerInstance);
    platformJump = false;
    moveDistance = 50;
    setFlagsFalse();

    //transfered from level
    boxType["platform"] = 0;
    boxType["ground"] = 0;
    boxType["player"] = 1;
    boxType["flag"] = 0;
    boxType["spikes"] = 2;
    boxType["door"] = 2;


    sizes["platform"] = b2Vec2(50.0f,50.0f);
    sizes["ground"] = b2Vec2(50.0f,50.0f);
    sizes["spikes"] = b2Vec2(50.0f, 50.0f);
    sizes["player"] = b2Vec2(20.0f,45.0f);
    sizes["flag"] = b2Vec2(1.0f,150.0f);
    sizes["door"] = b2Vec2(100.0f,1.0f);

    sprites["platform"] = &grassSprite2;
    sprites["ground"] = &grassSprite;
    sprites["spikes"] = &spikeSprite;
    sprites["player"] = &characterSprite;
    sprites["flag"] = &flagSprite;
    sprites["door"] = &trapDoor;

    lightColor[true] = &greenSprite;
    lightColor[false] = &redSprite;

    grassTexture.loadFromFile("../textures/GrassBlock.png");
    grassTexture1.loadFromFile("../textures/GrassBlock1.png");
    grassTexture2.loadFromFile("../textures/GrassBlock2.png");
    grassTexture3.loadFromFile("../textures/GrassBlock3.png");
    grassSprite.setTexture(grassTexture);
    grassSprite1.setTexture(grassTexture1);
    grassSprite2.setTexture(grassTexture2);
    grassSprite3.setTexture(grassTexture3);
    grassSprite.setOrigin(25,25);
    grassSprite1.setOrigin(25,25);
    grassSprite2.setOrigin(25,25);
    grassSprite3.setOrigin(25,25);
    spikeTexture.loadFromFile("../textures/SpikeBlock.png");
    spikeSprite.setTexture(spikeTexture);
    spikeSprite.setOrigin(25,25);
    characterSprite.setTexture(codeyIdle);
    characterSprite.setOrigin(25,25);
    flagTexture.loadFromFile("../textures/Code Flag.png");
    flagSprite.setTexture(flagTexture);
    flagSprite.setOrigin(25,75);
    flagTexture1.loadFromFile("../textures/Code Flag1.png");
    flagSprite1.setTexture(flagTexture1);
    flagSprite1.setOrigin(25,75);
    doorTexture.loadFromFile("../textures/TrapDoor.png");
    trapDoor.setTexture(doorTexture);
    redTexture.loadFromFile("../textures/RedLight.png");
    redSprite.setTexture(redTexture);
    greenTexture.loadFromFile("../textures/GreenLight.png");
    greenSprite.setTexture(greenTexture);
    greenSprite.setPosition(850,0);
    redSprite.setPosition(850,0);
    //trapDoor.setOrigin(,1);
}

void World::createWorld(Level* level){
    if(this->level != nullptr)
        cleanWorld();
    this->level = level;
    for (b2Body* b = m_world->GetBodyList(); b; b = b->GetNext())
    {
        m_world->DestroyBody(b);

    }
    level->init(m_world.get());
}

void World::gameStateChanged(int phase) {
    this->gameState = phase;
}

void World::wait()
{
    waitTime++;
    if(waitTime >= 60){
        waitTime = 0;
        waitFlag = false;
        commandFinished = true;
    }
}

void World::ChangeSprite(string item)
{
    if(item == "ground" )
        ChangedGrassSprite();
    else if(item == "platform")
        ChangedPlatformSprite();
    else if(item == "flag")
        ChangedFlagSprite();
}

void World::ChangedGrassSprite()
{
    sf::Sprite* sprite;
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    randomNumber = qrand() % 2;
    if(randomNumber == 0)
        sprite = &grassSprite2;
    else if(randomNumber == 1)
        sprite = &grassSprite3;

    sprites["ground"] = sprite;

}

void World::ChangedPlatformSprite()
{
    sf::Sprite* sprite;
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    randomNumber = qrand() % 2;
    if(randomNumber == 0)
        sprite = &grassSprite;
    else if(randomNumber == 1)
        sprite = &grassSprite1;
     sprites["platform"] = sprite;
}

void World::ChangedFlagSprite()
{
    sf::Sprite* sprite;
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    randomNumber = qrand() % 2;
    if(randomNumber == 0)
        sprite = &flagSprite;
    else if(randomNumber == 1)
        sprite = &flagSprite1;
    sprites["flag"] = sprite;
}

void World::cleanWorld()
{
    for (b2Body* b = m_world->GetBodyList(); b; b = b->GetNext())
    {
        m_world->DestroyBody(b);

    }
    command = 0;
    commandFinished = true;
    commands.clear();
    moveDistance  = 50;
    delete level;
    level = nullptr;

}

void World::setLevel()
{
    if(gameState == 1)
        lvl.setTexture(lvl1);
    else if (gameState == 2)
        lvl.setTexture(lvl2);
    else if (gameState == 3)
        lvl.setTexture(lvl3);
    else if (gameState == 4)
        lvl.setTexture(lvl4);
    else if (gameState == 5)
        lvl.setTexture(lvl5);
    else if (gameState == 6)
        lvl.setTexture(lvl6);
    else if (gameState == 7)
        lvl.setTexture(lvl7);
    else if (gameState == 8)
        lvl.setTexture(lvl8);
    else if (gameState == 9)
        lvl.setTexture(lvl9);

}

int World::getGameState()
{
    return gameState;
}

bool World::getLight(){
    return level->isGreen;
}

void World::forward(){
    //William's Code
    if (level->character->getBody()->GetPosition().x >= level->currentX + moveDistance)
    {
        level->character->getBody()->SetLinearVelocity(b2Vec2(0, 600));
        //level->character->getBody()->ApplyLinearImpulse( b2Vec2(0,0), level->character->getBody()->GetWorldCenter(),true );
        level->currentX+=moveDistance;
        level->character->getBody()->SetTransform(b2Vec2(level->currentX,level->currentY),0.0f);
        forwardFlag = false;
        commandFinished = true;
        moveDistance = 50;
        level->grounded = false;
    }
    else
    {
        b2Vec2 vel = level->character->getBody()->GetLinearVelocity();
        vel.x = 300;
        vel.y = 0;
        level->character->getBody()->SetLinearVelocity(vel);
        level->grounded = false;
    }
}

void World::insertCommand(int x){
    commands.push_back(x);
}

void World::jump() {
    if(!platformJump) {
           moveDistance = 100;

    }
    else
        moveDistance = 50;


    if (level->character->getBody()->GetPosition().y <= level->currentY - 100)
    {
        b2Vec2 vel = level->character->getBody()->GetLinearVelocity();
        vel.y = 0;
        level->character->getBody()->SetLinearVelocity(vel);
        jumpFlag = false;
        platformJump = false;
        forwardFlag = true;
        level->currentY -= 100;
    }

    else{
        b2Vec2 vel = level->character->getBody()->GetLinearVelocity();
        vel.y = -1400;
        level->character->getBody()->SetLinearVelocity(vel);
        level->grounded = false;
    }
}


void World::OnInit()
{
    myClock.restart();
}

void World::levelWon()
{
    setFlagsFalse();
    gameStateChanged(0);
    RenderWindow::setVisible(false);
    cleanWorld();
    emit getStats();
}

void World::levelLost()
{
    setFlagsFalse();
    RenderWindow::setVisible(false);
    cleanWorld();
    emit restartLevel();
}

void World::setFlagsFalse(){
    forwardFlag = false;
    jumpFlag = false;
}

void World::OnUpdate()
{
    if(jumpFlag){
            if(level->platforms.contains(level->currentX+50) && level->platforms[level->currentX+50] - level->currentY + 50 <= 5
                    && level->platforms[level->currentX+50] - level->currentY + 50 >= -5){

            platformJump = true;
            }
        }

    // Clear screen
    if (gameState != 0)
    {
        //cout<<command<<endl;
        if (commands.size() > 0 && commandFinished && level->grounded && commands.size() > command && !level->killRobot){
            commandFinished = false;
            if(commands[command] == 0)
                forwardFlag = true;
            else if (commands[command] == 1)
                jumpFlag = true;
            else if (commands[command] == 2)
                waitFlag = true;
            command ++;
            if(command % 2 == 1 && !level->killRobot) {

                level->isGreen = !level->isGreen;
            }

        }
        if(forwardFlag){
            characterSprite.setTexture(codeyForward);
            forward();
        }

        else if(jumpFlag){
            characterSprite.setTexture(codeyJump);
            jump();
        }
        else if(waitFlag){
            wait();
        }

        else
            characterSprite.setTexture(codeyIdle);

        RenderWindow::setVisible(true);
        RenderWindow::setFramerateLimit(60);
        RenderWindow::clear(sf::Color::Green);
        RenderWindow::setPosition(sf::Vector2i(worldX,worldY));
        RenderWindow::setSize(sf::Vector2u(worldW,worldH));

        draw(lvl);
        if(gameState == 7 ||gameState == 8 ||gameState == 9){
            light = lightColor[level->isGreen];
            draw(*light);
        }

        //William Code
        level->Draw();
        if(level->flagItem->contacting)
            levelWon();
        //William Code End
        else if (level->dead)
            levelLost();
        else if(level->character->getBody()->GetPosition().y > 450)
            levelLost();

    }
    else
    {
        RenderWindow::setVisible(false);
    }
}
