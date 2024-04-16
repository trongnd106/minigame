package com.trong.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.trong.game.Scenes.Hud;
import com.trong.game.Screens.PlayScreen;
import com.trong.game.Sprites.Items.ItemDef;
import com.trong.game.Sprites.Items.Mushroom;
import com.trong.game.SuperMario;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public Coin(PlayScreen screen, MapObject object){
        super(screen,object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN)
            SuperMario.manager.get("sounds/bump.wav", Sound.class).play();
        else {
            if(object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16/SuperMario.PPM), Mushroom.class));
                SuperMario.manager.get("sounds/powerup_spawn.wav", Sound.class).play();
            }
            else {
                SuperMario.manager.get("sounds/coin.wav", Sound.class).play();
                getCell().setTile(tileSet.getTile(BLANK_COIN));
                Hud.addScore(100);
            }
        }
    }

}
