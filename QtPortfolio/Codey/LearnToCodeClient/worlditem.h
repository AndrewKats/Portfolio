#ifndef WORLDITEM_H
#define WORLDITEM_H
#include <Box2D/Box2D.h>
#include <string>
#include <SFML/Graphics.hpp>

class WorldItem
{
public:
    WorldItem();

    void init(b2World* world, const b2Vec2& position, const b2Vec2& dimentions, int type, std::string hello, sf::Sprite* sprite, b2Vec2 origin = b2Vec2(0,0));
    b2Body* getBody() const;
    b2Fixture* getFixture() const;
    void StartContact();
    void EndContact();
    std::string objectType;
    bool contacting;
    sf::Sprite* sprite;

private:
    b2Body* m_body = nullptr;
    b2Fixture* m_fixture = nullptr;
};

class MyContactListener : public b2ContactListener
{
public:
    virtual void BeginContact(b2Contact* contact);
    virtual void EndContact(b2Contact* contact);
};

#endif // WORLDITEM_H
