package com.trong.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.trong.game.Screens.PlayScreen;
import com.trong.game.Sprites.Brick;
import com.trong.game.Sprites.Coin;
import com.trong.game.Sprites.Goomba;
import com.trong.game.SuperMario;

public class B2WorldCreator {
    public Array<Goomba> goombas;
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ SuperMario.PPM, (rect.getY() + rect.getHeight()/2)/SuperMario.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/SuperMario.PPM, rect.getHeight()/2/SuperMario.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/SuperMario.PPM, (rect.getY() + rect.getHeight()/2)/SuperMario.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/SuperMario.PPM, rect.getHeight()/2/SuperMario.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = SuperMario.OBJECT_BIT;
            body.createFixture(fdef);
        }

        //create brick bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, object);
        }

        //create coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Coin(screen, object);
        }

        // create all goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX()/SuperMario.PPM, rect.getY()/SuperMario.PPM));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
}
