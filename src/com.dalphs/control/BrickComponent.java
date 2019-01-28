package com.dalphs.control;

import com.almasb.fxgl.entity.component.Component;

public class BrickComponent extends Component {

    private int lives = 1;

    //Method used when a Brick is hit
    public void onHit(){
        lives--;
        if(lives == 0)
            entity.removeFromWorld();
    }
}
