#include "level.h"
#include "worlditem.h"
#include "iostream"
#include "world.h"
#include <QTime>
#define DEGTORAD 0.0174532925199432957f
#define RADTODEG 57.295779513082320876f

WorldItemTuple::WorldItemTuple(int x, int y, std::string item)
    :x(x), y(y), item(item){}

Level::Level(World* world): world(world), isGreen(true), dead(false), grounded(true),killRobot(false)
{
}

Level::~Level()
{
    for(auto& item : grounds)
        delete item;
    for(auto& item: items)
        delete item;
    delete character;
    delete flagItem;
}

void Level::FillItemsVector(){}

void Level::init(b2World *world)
{
    FillItemsVector();
    FillWorld(world);
}

void Level::Draw()
{
    bool noGrounding = false;
    if(character->getBody()->GetPosition().y > 378){
        noGrounding = true;
        killRobot = true;
        flagItem->getBody()->SetType(b2_kinematicBody);

    }

    //draw the character
    character->sprite->setPosition(character->getBody()->GetPosition().x,character->getBody()->GetPosition().y);
    character->sprite->setRotation(character->getBody()->GetAngle()*RADTODEG);
    world->draw(*character->sprite);

    //draw the ground
    for(auto& item : grounds){
        item->sprite->setPosition(item->getBody()->GetPosition().x,item->getBody()->GetPosition().y);
        world->draw(*item->sprite);
        if(item->contacting && !noGrounding){
            grounded = true;
            character->getBody()->SetTransform(character->getBody()->GetPosition(),0);
            currentY = item->getBody()->GetPosition().y-50;
            if(killRobot){
                character->getBody()->SetType(b2_kinematicBody);
                character->getBody()->SetLinearVelocity(b2Vec2(0,100000));
                currentX = character->getBody()->GetPosition().x;
            }


        }
    }
    int spike = 0;
    for(auto& item : spikes){
        item->sprite->setPosition(item->getBody()->GetPosition().x,item->getBody()->GetPosition().y);
        world->draw(*item->sprite);
        b2Vec2 vel = item->getBody()->GetLinearVelocity();
        if(isGreen && item->getBody()->GetPosition().y > heights[spike].y){
            vel.y = -100000000;
        }
        else if (!isGreen && item->getBody()->GetPosition().y < heights[spike].x){
            vel.y = 100000000;
            item->getBody()->SetLinearVelocity(vel);
        }
        else{
            vel.y = 0;
        }
        item->getBody()->SetLinearVelocity(vel);
        if(item->contacting){
            dead = true;
        }
        spike++;

    }
    for(auto& item : doors){
        item->sprite->setPosition(item->getBody()->GetPosition().x,item->getBody()->GetPosition().y);
        item->sprite->setRotation(item->getBody()->GetAngle()*RADTODEG);
        world->draw(*item->sprite);

        if(item->contacting && !noGrounding){
            grounded = true;
            currentY = item->getBody()->GetPosition().y-27;
            isGreen = true;
        }

        if(item->getBody()->GetAngle() * RADTODEG >= 90 && !isGreen)
            item->getBody()->SetAngularVelocity(-2);
        else if(item->getBody()->GetAngle() * RADTODEG <= 0 && !isGreen)
            item->getBody()->SetAngularVelocity(2);
        else if(isGreen && !killRobot){
            item->getBody()->SetAngularVelocity(0);
            item->getBody()->SetTransform(item->getBody()->GetPosition(),0);
        }
        //std::cout<<isGreen<<std::endl;

    }

    //draw the flag
    flagItem->sprite->setPosition(flagItem->getBody()->GetPosition().x,flagItem->getBody()->GetPosition().y);
    world->draw(*flagItem->sprite);
    //run game physics
    world->m_world->Step(1.0/60.0f,6,2);

}

int Level::ReturnXCoordinate(int x)
{
    return x * 50 + 25;
}

int Level::ReturnYCoordinate(int y)
{
    return 425 - y * 50;
}




void Level::FillWorld(b2World* world)
{
    int x;
    int y;
    for(auto& item : items){
        WorldItem* worldItem = new WorldItem;
        this->world->ChangeSprite(item->item);
        if(item->item == "door"){
            x = ReturnDoorX(item->x);
            y = ReturnDoorY(item->y);
            worldItem->init(world,b2Vec2(x,y),
                this->world->sizes[item->item],this->world->boxType[item->item],item->item,this->world->sprites[item->item],b2Vec2(50,0));
            //worldItem->getBody()->SetAngularVelocity(.05);
        }
        else{
            x = ReturnXCoordinate(item->x);
            y = ReturnYCoordinate(item->y);
            worldItem->init(world,b2Vec2(x,y),
                this->world->sizes[item->item],this->world->boxType[item->item],item->item,this->world->sprites[item->item]);
        }
        if(item->item == "player")
            character = worldItem;
        else if (item->item == "flag")
            flagItem = worldItem;
        else if (item->item == "spikes"){
            heights.push_back(b2Vec2(ReturnYCoordinate(item->y), ReturnYCoordinate(item->y+2)));
            spikes.push_back(worldItem);
            b2Vec2 vel = b2Vec2(0,-100000000);
            worldItem->getBody()->SetLinearVelocity(vel);

        }
        else if(item->item == "door"){
         doors.push_back(worldItem);
        }
        else{
            this->world->ChangedGrassSprite();
            if(item->item == "platform")
                platforms[ReturnXCoordinate(item->x)] = ReturnYCoordinate(item->y);
            grounds.push_back(worldItem);
        }


    }
}

int Level::ReturnDoorX(int x)
{
    return ReturnXCoordinate(x) - 25;
}

int Level::ReturnDoorY(int y)
{
    return 450 - y * 50 + 1;
}

Level1::Level1(World* world) : Level(world)
{
}

void Level1::FillItemsVector()
{
    for(int x = 0; x < 18; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(7,2,"flag"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level2::Level2(World *world) : Level(world)
{
}

void Level2::FillItemsVector()
{
 for (int x = 0; x < 6; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 for (int x = 7; x < 11; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 for (int x = 11; x < 15; x++)
 {
     items.push_back(new WorldItemTuple(x,2,"platform"));
 }
 items.push_back(new WorldItemTuple(2,1,"player"));
 items.push_back(new WorldItemTuple(14,4,"flag"));
 currentX = ReturnXCoordinate(2);
 currentY = ReturnYCoordinate(1);
}

Level3::Level3(World *world) : Level(world)
{
}

void Level3::FillItemsVector()
{
 for (int x = 0; x < 3; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 for (int x = 4; x < 8; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 for (int x = 9; x < 11; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 for (int x = 12; x < 16; x++)
 {
     items.push_back(new WorldItemTuple(x,0,"ground"));
 }
 items.push_back(new WorldItemTuple(7,1,"platform"));
 items.push_back(new WorldItemTuple(7,2,"platform"));
 items.push_back(new WorldItemTuple(15,2,"platform"));
 items.push_back(new WorldItemTuple(2,1,"player"));
 items.push_back(new WorldItemTuple(15,4,"flag"));
 currentX = ReturnXCoordinate(2);
 currentY = ReturnYCoordinate(1);
}
Level4::Level4(World *world) : Level(world)
{
}

void Level4::FillItemsVector()
{
    for(int x = 0; x < 18; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(15,2,"flag"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level5::Level5(World *world) : Level(world)
{
}

void Level5::FillItemsVector()
{
    for(int x = 0; x < 3; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 3; x < 16; x+=2){
        items.push_back( new WorldItemTuple(x,2,"platform"));
    }
    for(int x = 4; x < 15; x+=2){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(15,4,"flag"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level6::Level6(World *world) : Level(world)
{
}

void Level6::FillItemsVector()
{
    for(int x = 0; x < 6; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 7; x < 10; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 10; x < 17; x+=2){
        items.push_back( new WorldItemTuple(x,2,"platform"));
    }
    items.push_back( new WorldItemTuple(6,2,"platform"));
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(16,4,"flag"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level7::Level7(World *world) : Level(world)
{
}

void Level7::FillItemsVector()
{
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    lightSwitch = qrand() % 2;
    if (lightSwitch == 0)
        isGreen = false;
    else
        isGreen = true;
    for(int x = 0; x < 4; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 6; x < 8; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 10; x < 18; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(11,2,"flag"));
    items.push_back(new WorldItemTuple(4,1,"door"));
    items.push_back(new WorldItemTuple(8,1,"door"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level8::Level8(World *world) : Level(world)
{
}

void Level8::FillItemsVector()
{
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    lightSwitch = qrand() % 2;
    if (lightSwitch == 0)
        isGreen = false;
    else
        isGreen = true;
    for(int x = 0; x < 18; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    items.push_back(new WorldItemTuple(2,1,"player"));
    items.push_back(new WorldItemTuple(13,2,"flag"));
    items.push_back(new WorldItemTuple(5,1,"spikes"));
    items.push_back(new WorldItemTuple(9,1,"spikes"));
    currentX = ReturnXCoordinate(2);
    currentY = ReturnYCoordinate(1);
}

Level9::Level9(World *world) : Level(world)
{
}

void Level9::FillItemsVector()
{
    QTime time = time.currentTime();
    qsrand(time.msec()+ qrand()*13*qrand()-17);
    lightSwitch = qrand() % 2;
    if (lightSwitch == 0)
        isGreen = false;
    else
        isGreen = true;
    for(int x = 0; x < 3; x++){
        items.push_back( new WorldItemTuple(x,0,"ground"));
    }
    for(int x = 12; x < 15; x++){
        items.push_back( new WorldItemTuple(x,2,"platform"));
    }

    items.push_back(new WorldItemTuple(3,1,"door"));
    items.push_back(new WorldItemTuple(13,3,"spikes"));
    items.push_back(new WorldItemTuple(1,1,"player"));
    items.push_back(new WorldItemTuple(16,2,"flag"));
    items.push_back( new WorldItemTuple(5,0,"ground"));
    items.push_back( new WorldItemTuple(7,0,"ground"));
    items.push_back( new WorldItemTuple(8,0,"ground"));
    items.push_back( new WorldItemTuple(11,0,"ground"));
    items.push_back( new WorldItemTuple(16,0,"ground"));
    items.push_back( new WorldItemTuple(6,2,"platform"));
    items.push_back( new WorldItemTuple(9,2,"platform"));
    currentX = ReturnXCoordinate(1);
    currentY = ReturnYCoordinate(1);
}
