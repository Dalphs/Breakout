package com.dalphs;

import com.almasb.fxgl.entity.component.Component;

public class BrickComponent extends Component {

    private int lives = 1;

    public void onHit(){
        lives--;
        if(lives == 0)
            entity.removeFromWorld();
    }
}
