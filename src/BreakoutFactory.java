import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

public class BreakoutFactory implements TextEntityFactory {

    @SpawnSymbol('9')
    public Entity newBar(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return Entities.builder()
                .from(data)
                .type(BreakoutType.BAR).viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("bar.png"))
                .with(physics, new CollidableComponent(true))
                .with(new BarComponent())
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
