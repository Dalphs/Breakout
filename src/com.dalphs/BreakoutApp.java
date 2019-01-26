package com.dalphs;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.util.Optional;
import com.dalphs.control.BallComponent;
import com.dalphs.control.BatComponent;
import com.dalphs.control.BrickComponent;
import javafx.animation.PathTransition;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BreakoutApp extends GameApplication {

    private int lives = 3;
    private int currentLevel = 1;

    private BatComponent getBatControl(){
        return getGameWorld().getSingleton(BreakoutType.BAT).get().getComponent(BatComponent.class);
    }

    private BallComponent getBallControl(){
        return getGameWorld().getSingleton(BreakoutType.BALL).get().getComponent(BallComponent.class);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Breakout");
        gameSettings.setWidth(900);
        gameSettings.setHeight(780);
        gameSettings.setVersion("0.1");
        gameSettings.setIntroEnabled(false);
    }

    @Override
    protected void initGame() {
        initLevel();
        initBackground();
    }

    public void initLevel(){
        nextLevel();

    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getBatControl().left();
            }
        }, KeyCode.A );

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getBatControl().right();
            }
        }, KeyCode.D );
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreakoutType.BALL, BreakoutType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                brick.getComponent(BrickComponent.class).onHit();

                if(!getGameWorld().getEntities().toString().contains("BRICK") ){
                    currentLevel++;
                    nextLevel();
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreakoutType.BALL, BreakoutType.FIRE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
               loseLife(ball);
            }
        });
    }

    @Override
    protected void initUI() {
        Text text = getUIFactory().newText("Level 1", Color.BLACK, 48);
        getGameScene().addUINode(text);
        System.out.println(BallComponent.class);

        QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
            getBallControl().release();
        });
        transition.play();

    }

    private void initBackground(){
        Rectangle bg0 = new Rectangle(getWidth(), getHeight(),
                new LinearGradient(getWidth() / 2, 0, getWidth() / 2,
                        getHeight(), false, CycleMethod.NO_CYCLE,
                        new Stop(0.2, Color.YELLOW), new Stop(0.8, Color.DARKRED)));

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(), Color.color(0, 0, 0, 0.2));
        bg1.setBlendMode(BlendMode.DARKEN);

        EntityView bg = new EntityView();
        bg.addNode(bg0);
        bg.addNode(bg1);

        Entities.builder()
                .viewFromNode(bg)
                .renderLayer(RenderLayer.BACKGROUND)
                .with(new IrremovableComponent())
                .buildAndAttach(getGameWorld());

        Entity screenBounds = Entities.makeScreenBounds(40);
        screenBounds.addComponent(new IrremovableComponent());

        getGameWorld().addEntity(screenBounds);
    }

    public void loseLife(Entity ball){
        getGameWorld().removeEntity(ball);
        ball.setPosition(350,350);
        getGameWorld().addEntity(ball);
        lives--;
        System.out.println(lives);
        if(lives == 0)
            gameLost(ball);
        else
            gameText("Try again");
    }

    public void gameLost(Entity ball){
        Text text = getUIFactory().newText("YOU LOSE!", Color.BLACK, 100);
        text.setX(200);
        text.setY(400);

        getGameScene().addUINode(text);
        getGameWorld().removeEntity(ball);

    }

    public void nextLevel(){
        TextLevelParser parser = new TextLevelParser(new BreakoutFactory());
        Level level = parser.parse("levels/level" + currentLevel + ".txt");
        getGameWorld().setLevel(level);

        gameText("Level " + currentLevel);
    }

    public void gameText(String s){
        Text text = getUIFactory().newText(s, Color.BLACK, 48);
        getGameScene().addUINode(text);

        QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
            getBallControl().release();
        });
        transition.play();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
