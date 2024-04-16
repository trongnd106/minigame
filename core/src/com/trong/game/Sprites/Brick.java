package com.trong.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.trong.game.Scenes.Hud;
import com.trong.game.Screens.PlayScreen;
import com.trong.game.SuperMario;

import java.util.HashMap;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen,object);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()){
            setCategoryFilter(SuperMario.DESTROY_BIT);
            // khi va cham thi vien gach se bien mat
            getCell().setTile(null);
            Hud.addScore(200);
            SuperMario.manager.get("sounds/breakblock.wav", Sound.class).play();
        }
        SuperMario.manager.get("sounds/bump.wav", Sound.class).play();
    }
}
