import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.settings.GameSettings;

public class BreakoutApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Breakout");
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setVersion("0.1");
        gameSettings.setIntroEnabled(false);
    }

    @Override
    protected void initGame() {
        initLevel();
    }

    public void initLevel(){
        TextLevelParser parser = new TextLevelParser(new BreakoutFactory());
        Level level = parser.parse("levels/bricksballbar.txt");
        getGameWorld().setLevel(level);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
