#ifndef LEVEL_H
#define LEVEL_H
#include <string>
#include <QMap>
#include <string>
#include <vector>
#include <time.h>
#include <Box2D/Box2D.h>
#include <worlditem.h>


class World;

class WorldItemTuple{
public:
    WorldItemTuple(int x, int y, std::string item);
    int x;
    int y;
    std::string item;
};


class Level
{
public:
    Level(World* world);
    ~Level();
    void init(b2World* world);
    void Draw();

    //World items
    std::vector<WorldItem*> grounds;
    std::vector<WorldItem*> spikes;
    std::vector<WorldItem*> doors;
    std::vector<b2Vec2> heights;

    bool dead;
    bool isGreen;
    WorldItem* character;
    WorldItem* flagItem;

    bool grounded;
    int currentX;
    int currentY;
    int lightSwitch;
    QMap<int,int> platforms;
    bool killRobot;

private:

    void FillWorld(b2World* world);
    int ReturnDoorX(int x);
    int ReturnDoorY(int y);


protected:
    std::vector<WorldItemTuple*> items;
    World* world;
    virtual void FillItemsVector() = 0;
    int ReturnXCoordinate(int x);
    int ReturnYCoordinate(int y);
};

class Level1: public Level
{
public:
    Level1(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level2: public Level
{
public:
    Level2(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level3: public Level
{
public:
    Level3(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level4: public Level
{
public:
    Level4(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level5: public Level
{
public:
    Level5(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level6: public Level
{
public:
    Level6(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level7: public Level
{
public:
    Level7(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level8: public Level
{
public:
    Level8(World* world);

private:

protected:
    virtual void FillItemsVector();
};

class Level9: public Level
{
public:
    Level9(World* world);

private:

protected:
    virtual void FillItemsVector();
};









#endif // LEVEL_H
