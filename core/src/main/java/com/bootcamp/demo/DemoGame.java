package com.bootcamp.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.bootcamp.demo.events.GameStartedEvent;
import com.bootcamp.demo.managers.API;
import com.bootcamp.demo.events.core.EventModule;
import com.bootcamp.demo.util.services.ImageFactory;

public class DemoGame extends Game {
    private ImageFactory imageFactory;

    @Override
    public void create () {
        imageFactory = new ImageFactory();
        Gdx.input.setInputProcessor(new InputMultiplexer());

        setScreen(new GameScreen());
        API.get(EventModule.class).fireEvent(GameStartedEvent.class);
    }

    @Override
    public void dispose () {
        super.dispose();
        imageFactory.dispose();
        API.Instance().dispose();
        Gdx.app.exit();
    }
}
