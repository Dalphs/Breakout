package com.dalphs.control;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

import java.util.Random;

public class BallComponent extends Component {

    private PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {
        limitVelocity();
    }

    //This methods makes sure that the ball does not fall below a specified speed, so the ball doesnt stop
    private void limitVelocity(){

        if(Math.abs(physics.getLinearVelocity().getX()) < 5 * 60){
            physics.setLinearVelocity(Math.signum(physics.getLinearVelocity().getX()) * 5 * 60,
                    physics.getLinearVelocity().getY());
        }

        if(Math.abs(physics.getLinearVelocity().getY()) < 5 * 60){
            physics.setLinearVelocity(physics.getLinearVelocity().getX(),
                    Math.signum(physics.getLinearVelocity().getY()) * 5 * 60);
        }
    }

    public void release(){
        //The ball is released in a random upward direction
        int x = (int) (Math.random() * 11) - 5;
        int y = (int) (Math.random() * 10) + 1;
        physics.setBodyLinearVelocity(new Vec2(x, y));
    }

    //Method is not used
    public void replace(){
        entity.setPosition(400,500);
    }
}
