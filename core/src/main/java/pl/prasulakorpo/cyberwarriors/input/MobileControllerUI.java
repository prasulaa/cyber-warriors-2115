package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static pl.prasulakorpo.cyberwarriors.GameProperties.HEIGHT;
import static pl.prasulakorpo.cyberwarriors.GameProperties.WIDTH;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.*;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.BUTTON_B_CLICKED;

public class MobileControllerUI {

    public static Stage createStage(InputListener inputListener) {
        boolean debug = false;
        Stage stage = new Stage(new FitViewport(WIDTH, HEIGHT));
        stage.addListener(inputListener);
        Table table = new Table();
        table.setSize(WIDTH, HEIGHT*1/3);
//        table.setDebug(true);


        Table tableLeft = createSmallButtonPanel(
            BUTTON_LEFT, BUTTON_LEFT_CLICKED, inputListener(inputListener, Input.Keys.A),
            BUTTON_RIGHT, BUTTON_RIGHT_CLICKED, inputListener(inputListener, Input.Keys.D),
            BUTTON_DOWN, BUTTON_DOWN_CLICKED, inputListener(inputListener, Input.Keys.S),
            debug);

        Table tableRight = createSmallButtonPanel(
            BUTTON_A, BUTTON_A_CLICKED, inputListener(inputListener, 0),
            BUTTON_C, BUTTON_C_CLICKED, inputListener(inputListener, 0),
            BUTTON_B, BUTTON_B_CLICKED, inputListener(inputListener, Input.Keys.SPACE),
            debug);

        table.add(tableLeft).size(WIDTH/4, HEIGHT*1/3).left();
        table.add(new Table()).expand();
        table.add(tableRight).size(WIDTH/4, HEIGHT*1/3).right();

        stage.addActor(table);
        return stage;
    }

    private static Table createSmallButtonPanel(String pathBut1, String pathBut1Click, EventListener eventListener1,
                                                String pathBut2, String pathBut2Click, EventListener eventListener2,
                                                String pathBut3, String pathBut3Click, EventListener eventListener3,
                                                boolean debug) {
        Table table = new Table();
        table.setDebug(debug);

        table.add(createButton(pathBut1, pathBut1Click, eventListener1)).expand();
        table.add(createButton(pathBut2, pathBut2Click, eventListener2)).expand();
        table.row();
        table.add(createButton(pathBut3, pathBut3Click, eventListener3)).colspan(2).expand();

        return table;
    }

    private static Actor createButton(String pathUp, String pathDown, EventListener eventListener) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(pathUp))));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(pathDown))));
        ImageButton imageButton = new ImageButton(style);
        imageButton.addListener(eventListener);
        return imageButton;
    }

    private static InputListener inputListener(InputListener inputListener, int key) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return inputListener.keyDown(event, key);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                inputListener.keyUp(event, key);
            }
        };
    }

}
