#ifndef WORLD_H
#define WORLD_H
#include "qsfmlcanvas.h"
#include <iostream>
#include <vector>
#include <QObject>

//William's Inlcudes
#include <QMap>
#include <worlditem.h>
#include <Box2D/Box2D.h>
#include <SFML/Graphics.hpp>
#include <level.h>
#include <string>

class World : public QSFMLCanvas {

    friend class Level;
    friend class Level1;
    friend class Level2;
    friend class Level3;
    friend class Level4;
    friend class Level5;
    friend class Level6;
    friend class Level7;
    friend class Level8;
    friend class Level9;

    Q_OBJECT
public :
    World(QWidget* Parent, const QPoint& Position, const QSize& Size);
    void createWorld(Level* level);
    virtual void OnInit();
    virtual void OnUpdate();
    bool forwardFlag;
    bool jumpFlag;
    bool waitFlag;
    void forward();
    void jump();
    void setLevel();
    void levelWon();
    void levelLost();
    void cleanWorld();
    bool getLight();
    void setFlagsFalse();
    int getGameState();
    void insertCommand(int);

public slots :
    void gameStateChanged(int phase);

signals:
    void getStats();
    void restartLevel();

private :
    int worldX;
    int worldY;
    int worldW;
    int worldH;
    int gameState;
    bool commandFinished;
    int command;
    int waitTime;
    sf::Sprite* light;
    int randomNumber;

    sf::Clock myClock;
    sf::Sprite lvl;
    sf::Texture lvl1;
    sf::Texture lvl2;
    sf::Texture lvl3;
    sf::Texture lvl4;
    sf::Texture lvl5;
    sf::Texture lvl6;
    sf::Texture lvl7;
    sf::Texture lvl8;
    sf::Texture lvl9;
    sf::Texture codeyIdle;
    sf::Texture codeyForward;
    sf::Texture codeyJump;
    sf::RectangleShape myRect;

    //William's Code
    std::unique_ptr<b2World> m_world;
    MyContactListener myContactListenerInstance;
    bool platformJump;
    int moveDistance;
    Level* level;
    std::vector<int> commands;
    void wait();
    void ChangeSprite(std::string item);
    void ChangedGrassSprite();
    void ChangedPlatformSprite();
    void ChangedFlagSprite();

    //Transfered From Level
    QMap<std::string, int> boxType;
    QMap<std::string, b2Vec2> sizes;
    QMap<std::string, sf::Sprite*> sprites;
    QMap<bool,sf::Sprite*> lightColor;
    //sprites
    sf::Sprite grassSprite;
    sf::Sprite grassSprite1;
    sf::Sprite grassSprite2;
    sf::Sprite grassSprite3;
    sf::Sprite spikeSprite;
    sf::Sprite characterSprite;
    sf::Sprite flagSprite;
    sf::Sprite flagSprite1;
    sf::Sprite trapDoor;
    sf::Sprite redSprite;
    sf::Sprite greenSprite;
    //textures
    sf::Texture grassTexture;
    sf::Texture grassTexture1;
    sf::Texture grassTexture2;
    sf::Texture grassTexture3;
    //sf::Texture characterTexture;
    sf::Texture flagTexture;
    sf::Texture flagTexture1;
    sf::Texture spikeTexture;
    sf::Texture doorTexture;
    sf::Texture redTexture;
    sf::Texture greenTexture;
    //clean the world
    //void cleanWorld();


};
#endif // WORLD_H

