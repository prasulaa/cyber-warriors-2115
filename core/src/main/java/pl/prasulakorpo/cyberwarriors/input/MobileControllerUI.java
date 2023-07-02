package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.HEIGHT;
import static pl.prasulakorpo.cyberwarriors.model.GameProperties.WIDTH;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.*;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.BUTTON_B_CLICKED;

public class MobileControllerUI {

    public static Stage createStage(InputHandler inputHandler) {
        boolean debug = false;
        Stage stage = new Stage(new FitViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setSize(WIDTH, HEIGHT*1/3);
        table.setDebug(true);


        Table tableLeft = createSmallButtonPanel(BUTTON_LEFT, BUTTON_LEFT_CLICKED, BUTTON_RIGHT, BUTTON_RIGHT_CLICKED, BUTTON_DOWN, BUTTON_DOWN_CLICKED, debug);
        Table tableRight = createSmallButtonPanel(BUTTON_A, BUTTON_A_CLICKED, BUTTON_C, BUTTON_C_CLICKED, BUTTON_B, BUTTON_B_CLICKED, debug);

        table.add(tableLeft).size(WIDTH/4, HEIGHT*1/3).left();
        table.add(new Table()).expand();
        table.add(tableRight).size(WIDTH/4, HEIGHT*1/3).right();

        stage.addActor(table);
        return stage;
    }

    private static Table createSmallButtonPanel(String pathBut1, String pathBut1Click,
                                                String pathBut2, String pathBut2Click,
                                                String pathBut3, String pathBut3Click,
                                                boolean debug) {
        Table table = new Table();
        table.setDebug(debug);

        table.add(createButton(pathBut1, pathBut1Click, null)).expand();
        table.add(createButton(pathBut2, pathBut2Click, null)).expand();
        table.row();
        table.add(createButton(pathBut3, pathBut3Click, null)).colspan(2).expand();

        return table;
    }

//    private static Table createSmallButtonPanel(String pathBut1, String pathBut1Click,
//                                                String pathBut2, String pathBut2Click,
//                                                String pathBut3, String pathBut3Click,
//                                                boolean debug) {
//        Table table = new Table();
//        table.setDebug(debug);
//
//        Table tableUp = new Table();
//        tableUp.setDebug(debug);
//        tableUp.add(createButton(pathBut1, pathBut1Click, null)).expand();
//        tableUp.add(createButton(pathBut2, pathBut2Click, null)).expand();
//
//        table.add(tableUp).width(WIDTH/4).expand();
//        table.row();
//        table.add(createButton(pathBut3, pathBut3Click, null)).expand();
//
//        return table;
//    }

    private static Actor createButton(String pathUp, String pathDown, EventListener eventListener) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(pathUp))));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(pathDown))));
        ImageButton imageButton = new ImageButton(style);
//        imageButton.addListener(eventListener);
        return imageButton;
    }

}
