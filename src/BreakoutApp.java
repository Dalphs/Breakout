import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.settings.GameSettings;

public class BreakoutApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Breakout");
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setVersion("0.1");
        gameSettings.setIntroEnabled(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
