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
import com.dalphs.control.BallComponent;
import com.dalphs.control.BatComponent;
import com.dalphs.control.BrickComponent;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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

    private int lives;
    private int currentLevel;
    private Text levelText;
    private Text livesText;

    //Method to get access to the methods from the BatComponent class
    private BatComponent getBatControl(){
        return getGameWorld().getSingleton(BreakoutType.BAT).get().getComponent(BatComponent.class);
    }

    //Method to get access to the methods from the BallComponent class
    private BallComponent getBallControl(){
        return getGameWorld().getSingleton(BreakoutType.BALL).get().getComponent(BallComponent.class);
    }

    //THe basic settings foor the game, size and title
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Breakout");
        gameSettings.setWidth(900);
        gameSettings.setHeight(780);
        gameSettings.setVersion("0.1");
    }

    //Method that starts the game and initialises number of lives and the current level
    @Override
    protected void initGame() {
        lives = 3;
        currentLevel = 1;
        initLevel();
        initBackground();
    }

    //Method that starts the first level
    public void initLevel(){
        nextLevel();
    }

    //Method which controls the bar using user input.
    @Override
    protected void initInput() {
        //If user presses A the left() method from the BatComponent class is called on the bat in the game
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getBatControl().left();
            }
        }, KeyCode.A );

        //If user presses D the right() method from the BatComponent class is called on the bat in the game
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getBatControl().right();
            }
        }, KeyCode.D );
    }

    @Override
    protected void initPhysics() {
        //This method determines the gravity i.e what way the elements moves if there er no other forces apllied
        getPhysicsWorld().setGravity(0, 0);

        //This method handles coollisions between all entities of type BALL and BRICK
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreakoutType.BALL, BreakoutType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                //Calls the onHit method on the Brick that gets hit by the ball
                brick.getComponent(BrickComponent.class).onHit();
                //If there are no entites that contains the "BRICK" when calling the toString method
                //basically when all bricks are derstroyed, the game will continue at the next level
                if(!getGameWorld().getEntities().toString().contains("BRICK") ){
                    currentLevel++;
                    lives++;
                    livesText.setText("Lives: " + lives);
                    levelText.setText("Level: " + currentLevel);
                    nextLevel();
                }
            }
        });

        //This collisionhandler handle collisions between all BALL entities and FIRE entites
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreakoutType.BALL, BreakoutType.FIRE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
               loseLife(ball);
            }
        });
    }

    @Override
    protected void initUI() {
        //The text with level in the top left corner
        levelText = getUIFactory().newText("Level: " + currentLevel, Color.GREY, 28);
        levelText.setY(30); levelText.setX(20);

        //The text with lives in the top right corner
        livesText = getUIFactory().newText("Lives: " + lives, Color.GREY, 28);
        livesText.setY(30); livesText.setX(760);

        //adding both texts to the gameScene
        getGameScene().addUINodes(levelText, livesText);

        gameText("Level 1");

    }

    private void initBackground(){
        //the background for the game which goes from red to yellow
        Rectangle bg0 = new Rectangle(getWidth(), getHeight(),
                new LinearGradient(getWidth() / 2, 0, getWidth() / 2,
                        getHeight(), false, CycleMethod.NO_CYCLE,
                        new Stop(0.2, Color.YELLOW), new Stop(0.8, Color.DARKRED)));

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(), Color.color(0, 0, 0, 0.2));
        bg1.setBlendMode(BlendMode.DARKEN);

        //Connecting the two backgrounds
        EntityView bg = new EntityView();
        bg.addNode(bg0);
        bg.addNode(bg1);

        //Adding the background to the game
        Entities.builder()
                .viewFromNode(bg)
                .renderLayer(RenderLayer.BACKGROUND)
                .with(new IrremovableComponent())
                .buildAndAttach(getGameWorld());

        //Creating screenbounds so the ball doesnt go out of the screen
        Entity screenBounds = Entities.makeScreenBounds(40);
        screenBounds.addComponent(new IrremovableComponent());

        //Adding the bounds to the game
        getGameWorld().addEntity(screenBounds);
    }

    public void loseLife(Entity ball){
        //Repositioning ball when a life is lost
        getGameWorld().removeEntity(ball);
        ball.setPosition(350,350);
        getGameWorld().addEntity(ball);

        lives--;
        livesText.setText("Lives: " + lives);

        //if there is no more lives the game ends and if there are "Try again" is displayed
        if(lives == 0)
            gameLost(ball);
        else
            gameText("Try again");
    }

    //method is run when there is no more lives
    public void gameLost(Entity ball){
        Text text = getUIFactory().newText("YOU LOSE!", Color.BLACK, 100);
        text.setX(200);
        text.setY(400);

        //removes ball and adds a "YOU LOSE" text to the gameScene
        getGameScene().addUINode(text);
        getGameWorld().removeEntity(ball);

        //Button which restarts the game if pressed
        Button retryButton = new Button("Try again");
        retryHandler retryHandler = new retryHandler();
        retryButton.setOnAction(retryHandler);
        retryButton.setLayoutY(450);
        retryButton.setLayoutX(350);
        retryButton.setStyle("-fx-font-size: 30;");
        retryButton.setPrefSize(200,75);


        getGameScene().addUINode(retryButton);

    }

    //class which hclears gamesScene and restarts the game
    class retryHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e){
            getGameScene().clear();
            initGame();
            initUI();
        }

    }

    public void nextLevel(){
        //Reads the level form the a .txt file and adds it to the gameWorld
        TextLevelParser parser = new TextLevelParser(new BreakoutFactory());
        Level level = parser.parse("levels/level" + currentLevel + ".txt");
        getGameWorld().setLevel(level);

        gameText("Level " + currentLevel);
    }

    //Method for adding text which will float across the screen
    public void gameText(String s){
        Text text = getUIFactory().newText(s, Color.BLACK, 48);
        getGameScene().addUINode(text);

        //Determines the pattern the text is floating
        QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

        //creates the "animation" for the floating text
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
