/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 *
 * @author admin
 */
public class PantallaFinal implements Screen {
    SpaceShooter game;
    int navesDestruidas;
    int disparosRecibidos;

    OrthographicCamera camera;

    public PantallaFinal (SpaceShooter game, int navesDestruidas, int disparosRecibidos) {
	
        this.game = game;
        this.navesDestruidas = navesDestruidas;
        this.disparosRecibidos = disparosRecibidos;
	camera = new OrthographicCamera();
	camera.setToOrtho(false, 800, 480);

    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (navesDestruidas==3 && disparosRecibidos < 3) {
            game.batch.begin();
            game.font.draw(game.batch, "Has ganado!!! ", 100, 200);
            game.font.draw(game.batch, "Has destruido "+navesDestruidas+" naves!!", 100, 150);
            game.font.draw(game.batch, "Toca en cualquier lugar para empezar de nuevo!", 100, 100);
            game.batch.end();
        } else {
        
            game.batch.begin();
            game.font.draw(game.batch, "Has perdido!!! ", 100, 200);
            game.font.draw(game.batch, "Has destruido "+navesDestruidas+" naves!!", 100, 150);
            game.font.draw(game.batch, "Toca en cualquier lugar para empezar de nuevo!", 100, 100);
            game.batch.end();
        
        }
        
        if (Gdx.input.isTouched()) {
                game.setScreen(new PantallaJuego(game));
                dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        
    }
}
