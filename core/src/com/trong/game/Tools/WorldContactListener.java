package com.trong.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.trong.game.Sprites.Enemy;
import com.trong.game.Sprites.InteractiveTileObject;
import com.trong.game.Sprites.Items.Item;
import com.trong.game.Sprites.Mario;
import com.trong.game.SuperMario;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

//     check cai nao la cai dau cua con mario
//        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
//            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//
//            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onHeadHit((Mario) object.getUserData());
//            }
//        }

        switch (cDef){
            case SuperMario.MARIO_HEAD_BIT | SuperMario.BRICK_BIT:

            case SuperMario.MARIO_HEAD_BIT | SuperMario.COIN_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;

            // mario nhay len head cua goomba
            case SuperMario.ENEMY_HEAD_BIT | SuperMario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;

            // goomba va cham voi pipe ve doi chieu van toc
            case SuperMario.ENEMY_BIT | SuperMario.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

            // mario va cham voi goomba
            case SuperMario.MARIO_BIT | SuperMario.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit();
                else
                    ((Mario) fixB.getUserData()).hit();
                break;

            case SuperMario.ENEMY_BIT | SuperMario.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

            // mushroom va cham voi pipe ve doi chieu van toc
            case SuperMario.ITEM_BIT | SuperMario.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;

            case SuperMario.ITEM_BIT | SuperMario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
