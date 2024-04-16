package com.trong.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.trong.game.Screens.PlayScreen;
import com.trong.game.SuperMario;

public class Mario extends Sprite {
    public World world;  /// world that mario live in
    public Body b2body;
    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING};
    public State currentState;
    public State previousState;
    private Animation marioRun;
    private TextureRegion marioJump;

    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;
    private boolean runningRight;
    private float stateTimer;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToReDefineMario;
    private PlayScreen screen;

    public Mario(PlayScreen screen){    // Mario generated in world
        // initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
//        marioStand = new TextureRegion(getTexture(),0,0,16,16);

        currentState = State.STANDING;
        stateTimer = 0; runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioRun = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

//        this.world = world;
        defineMario();
        setBounds(0,0,16/SuperMario.PPM, 16/SuperMario.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        if(marioIsBig)
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 - 6/SuperMario.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToReDefineMario){
            redifineMario();
        }
    }

    public void defineBigMario(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10/SuperMario.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);

        // dinh nghia nhung thu ma mario co the va cham
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.MARIO_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14/SuperMario.PPM));
        b2body.createFixture(fdef).setUserData(this);

        // tao 1 cam bien tren dau cua Mario -> nhay len va cham
        EdgeShape head = new EdgeShape();   // 1 line
        head.set(new Vector2(-2/SuperMario.PPM, 6/SuperMario.PPM), new Vector2(2/SuperMario.PPM, 6/SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/SuperMario.PPM, 32/SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);

        // dinh nghia nhung thu ma mario co the va cham
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.MARIO_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = shape;
//        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);

        // tao 1 cam bien tren dau cua Mario -> nhay len va cham
        EdgeShape head = new EdgeShape();   // 1 line
        head.set(new Vector2(-2/SuperMario.PPM, 6/SuperMario.PPM), new Vector2(2/SuperMario.PPM, 6/SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void redifineMario(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);   // destroy bigmario

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);

        // dinh nghia nhung thu ma mario co the va cham
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.MARIO_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = shape;
//        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);

        // tao 1 cam bien tren dau cua Mario -> nhay len va cham
        EdgeShape head = new EdgeShape();   // 1 line
        head.set(new Vector2(-2/SuperMario.PPM, 6/SuperMario.PPM), new Vector2(2/SuperMario.PPM, 6/SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToReDefineMario = false;
    }


    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) :
                        (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }
        // mario quay mặt sang trái nhưng region không quay về bên trái
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }
    public State getState(){
        if(runGrowAnimation)
            return State.GROWING;
        // nhảy lên và rơi xuống - ani: giơ tay lên, nhưng khi rơi khỏi vách thì trạng thái đứng bình thường
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else return State.STANDING;
    }
    public void grow(){
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight()*2);
        SuperMario.manager.get("sounds/powerup.wav", Sound.class).play();
    }
    public boolean isBig(){
        return marioIsBig;
    }
    public void hit(){
        if(marioIsBig){
            marioIsBig = false;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight()/2);
            SuperMario.manager.get("sounds/powerdown.wav", Sound.class).play();
        }
        else {
            SuperMario.manager.get("sounds/mariodie.wav", Sound.class).play();
        }
    }
}
