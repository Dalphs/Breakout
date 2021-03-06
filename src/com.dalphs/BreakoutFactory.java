package com.dalphs;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.Texture;
import com.dalphs.control.BallComponent;
import com.dalphs.control.BatComponent;
import com.dalphs.control.BrickComponent;
import com.dalphs.control.FireComponent;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BreakoutFactory implements TextEntityFactory {

    //A 9 in a .txt file will translate to a bat
    @SpawnSymbol('9')
    public Entity newBat(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        //Building the bat using the BatComponent class and the physics defined above
        return Entities.builder()
                .from(data)
                .type(BreakoutType.BAT)
                .at(FXGL.getSettings().getWidth() / 2 - 50, FXGL.getSettings().getHeight() - 100)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("bar.png"))
                .with(physics, new CollidableComponent(true))
                .with(new BatComponent())
                .build();
    }

    //A 2 in a .txt will translate to a brick
    @SpawnSymbol('2')
    public Entity newBrick(SpawnData data){
        //Building a new entity og using the BRICK component
        return Entities.builder()
                .from(data)
                .type(BreakoutType.BRICK).viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick1.png", 50, 24))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .with(new BrickComponent())
                .build();
    }

    //A 1 in a .txt will translate to a ball
    @SpawnSymbol('1')
    //building a entity from the BallComponent class
    public Entity newBall(SpawnData data){
        //Defining the physics for the ball
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(5);
        emitter.setEmissionRate(0.5);
        emitter.setBlendMode(BlendMode.SRC_OVER);

        //Building a entity from the BallComponent class
        return Entities.builder()
                .from(data)
                .type(BreakoutType.BALL)
                .at(390, 450)
                .bbox(new HitBox("Main", BoundingShape.circle(10)))
                .viewFromNode(new Circle(10, Color.DARKGREY))
                .with(physics, new CollidableComponent(true))
                .with(new BallComponent())
                .build();
    }

    //A 3 in a .txt spawns a fireelement using the FireComponent class
    @SpawnSymbol('3')
    public Entity newFIRE(SpawnData data){
        //Creating a entity
        return Entities.builder()
                .from(data)
                .type(BreakoutType.FIRE).viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("fire.png", 100, 80))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .with(new FireComponent())
                .build();
    }

    @Override
    public char emptyChar() {
        return 0;
    }

    //The width of a character in the .txt file
    @Override
    public int blockWidth() {
        return 50;
    }

    //The height of af chahracther in the .txt file
    @Override
    public int blockHeight() {
        return 40;
    }
}
