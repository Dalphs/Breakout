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
import com.dalphs.control.BallComponent;
import com.dalphs.control.BatComponent;
import com.dalphs.control.BrickComponent;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BreakoutFactory implements TextEntityFactory {

    @SpawnSymbol('9')
    public Entity newBat(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return Entities.builder()
                .from(data)
                .type(BreakoutType.BAT)
                .at(FXGL.getSettings().getWidth() / 2 - 50, FXGL.getSettings().getHeight() - 30)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("bar.png"))
                .with(physics, new CollidableComponent(true))
                .with(new BatComponent())
                .build();
    }

    @SpawnSymbol('2')
    public Entity newBrick(SpawnData data){
        return Entities.builder()
                .from(data)
                .type(BreakoutType.BRICK).viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick1.png"))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .with(new BrickComponent())
                .build();
    }

    @SpawnSymbol('1')
    public Entity newBall(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(5);
        emitter.setEmissionRate(0.5);
        emitter.setBlendMode(BlendMode.SRC_OVER);

        return Entities.builder()
                .from(data)
                .type(BreakoutType.BALL)
                .bbox(new HitBox("Main", BoundingShape.circle(10)))
                .viewFromNode(new Circle(10, Color.DARKGREY))
                .with(physics, new CollidableComponent(true))
                .with(new BallComponent())
                .build();
    }

    @Override
    public char emptyChar() {
        return 0;
    }

    @Override
    public int blockWidth() {
        return 40;
    }

    @Override
    public int blockHeight() {
        return 40;
    }
}
