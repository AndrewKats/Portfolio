#include "worlditem.h"
#include <Box2D/Box2D.h>

WorldItem::WorldItem()
{

}

void WorldItem::init(b2World *world, const b2Vec2 &position, const b2Vec2 &dimentions, int type, std::string hello,sf::Sprite* sprite, const b2Vec2 origin)
{
    contacting = false;
    objectType = hello;
    this->sprite = sprite;
    //make the body
    b2BodyDef bodyDef;
    if(type == 0)
        bodyDef.type = b2_staticBody;
    else if (type == 1)
        bodyDef.type = b2_dynamicBody;
    else
        bodyDef.type = b2_kinematicBody;
    bodyDef.position.Set(position.x, position.y);
    m_body = world->CreateBody(&bodyDef);

    b2PolygonShape boxShape;
    boxShape.SetAsBox(dimentions.x / 2.0f, dimentions.y / 2.0f,origin,0.0f);

    b2FixtureDef fixtureDef;
    fixtureDef.shape = &boxShape;
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.3f;
    m_fixture = m_body->CreateFixture(&fixtureDef);

    m_body->SetUserData(this);
}

b2Body *WorldItem::getBody() const
{
    return m_body;
}

b2Fixture *WorldItem::getFixture() const
{
    return m_fixture;
}

void WorldItem::StartContact()
{
    contacting = true;
}

void WorldItem::EndContact()
{
    contacting = false;
}

void MyContactListener::BeginContact(b2Contact *contact)
{
    void* bodyUserData = contact->GetFixtureA()->GetBody()->GetUserData();
    if(bodyUserData)
        static_cast<WorldItem*>(bodyUserData)->StartContact();

    bodyUserData = contact->GetFixtureB()->GetBody()->GetUserData();
    if(bodyUserData)
        static_cast<WorldItem*>(bodyUserData)->StartContact();
}

void MyContactListener::EndContact(b2Contact *contact)
{
    void* bodyUserData = contact->GetFixtureA()->GetBody()->GetUserData();
    if ( bodyUserData )
        static_cast<WorldItem*>( bodyUserData )->EndContact();

    bodyUserData = contact->GetFixtureB()->GetBody()->GetUserData();
    if ( bodyUserData )
        static_cast<WorldItem*>( bodyUserData )->EndContact();
}
