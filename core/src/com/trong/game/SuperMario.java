package com.trong.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.trong.game.Screens.PlayScreen;

public class SuperMario extends Game {
	public static final float V_WIDTH = 400;
	public static final float V_HEIGHT = 200;
	public static final float PPM = 100;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROY_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public SpriteBatch batch;

	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("music/mario_music.ogg", Music.class);
		manager.load("sounds/coin.wav", Sound.class);
		manager.load("sounds/bump.wav", Sound.class);
		manager.load("sounds/breakblock.wav", Sound.class);
		manager.load("sounds/powerup_spawn.wav", Sound.class);
		manager.load("sounds/powerup.wav", Sound.class);
		manager.load("sounds/powerdown.wav", Sound.class);
		manager.load("sounds/stomp.wav", Sound.class);
		manager.load("sounds/mariodie.wav", Sound.class);
		manager.finishLoading();  //dua tat ca vao hang doi

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
		manager.update();

	}

	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}





