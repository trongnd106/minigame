package com.trong.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.trong.game.Sprites.Enemy;
import com.trong.game.Sprites.Goomba;
import com.trong.game.Sprites.Items.Item;
import com.trong.game.Sprites.Items.ItemDef;
import com.trong.game.Sprites.Items.Mushroom;
import com.trong.game.SuperMario;
import com.trong.game.Scenes.Hud;
import com.trong.game.Sprites.Mario;
import com.trong.game.Tools.B2WorldCreator;
import com.trong.game.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {
    private SuperMario game;
    // basic playscreen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TextureAtlas atlas;
    private com.trong.game.Scenes.Hud hud;
    // tiled map variables
    private TmxMapLoader maploader; // load map into game
    private TiledMap map;           // reference to the map itself
    private OrthogonalTiledMapRenderer renderer;    // render map to the screen
    // box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;   //graphical representations of fixtures & body
    private B2WorldCreator creator;
    // sprites
    private Mario player;
    private Music music;
    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    public PlayScreen(SuperMario game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMario.V_WIDTH/SuperMario.PPM, SuperMario.V_HEIGHT/SuperMario.PPM, gameCam);
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/SuperMario.PPM);
        //note: getWorld not getScreen
        gameCam.position.set(SuperMario.V_WIDTH/2/SuperMario.PPM, SuperMario.V_HEIGHT/2/SuperMario.PPM, 0);
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        world = new World(new Vector2(0,-10), true);   // gravity & isSleep-object
        b2dr = new Box2DDebugRenderer();
        //create mario in our game world
        player = new Mario(this);   // fix this =)

        creator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());
        music = SuperMario.manager.get("music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        // co the gay ra loi hien thi goomba, WARNING this!!!
//        goomba = new Goomba(this, 5.64f, .16f);
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();
    }
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
//  if our user is holding down mouse move our camera through the game world
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }
    public void update(float dt){
        // handle user input first
        handleInput(dt);
        handleSpawningItems();
        // box2d use meters, kilograms and seconds for its unit measurements
        world.step(1/60f, 6, 2);
        player.update(dt);
//        goomba.update(dt);
        for(Enemy enemy : creator.getGoombas()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224/SuperMario.PPM){
                enemy.b2body.setActive(true);
            }
        }
        for(Item item : items){
            item.update(dt);
        }
        hud.update(dt);
        gameCam.position.x = player.b2body.getPosition().x;
        // update our gamecam with correct coordinations after changes
        gameCam.update();
        // tell our renderer to draw only what your camera can see in our game world
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        // seperate our update logic from render
        update(delta);

        // clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        renderer.render();

        // renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
//        goomba.draw(game.batch);
        for(Enemy enemy : creator.getGoombas()){
            enemy.draw(game.batch);
        }
        for(Item item : items){
            item.draw(game.batch);
        }
        game.batch.end();

        // set our batch to now draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        renderer.dispose();
        hud.dispose();
        b2dr.dispose();
    }
}
