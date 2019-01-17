/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

/**
 *
 * @author admin
 */
public class PantallaJuego implements Screen {
    
   final SpaceShooter game;
   private Texture imagenDisparo;
   private Texture imagenDisparoEnemigo;
   private Texture imagenNave;
   private Texture imagenNaveEnemiga;
   private Sound sonidoDisparo;
   private Music musicaEspacial;
   private OrthographicCamera camara;
   private Rectangle nave;
   private Rectangle naveEnemiga;
   private Array<Rectangle> vectorDisparos;
   private Array<Rectangle> vectorDisparosEnemigos;
   private long tiempoUltimoDisparo;
   private long tiempoUltimoDisparoEnemigo;
   private int navesDestruidas;
   private int disparosRecibidos;
   private int disparosAcertados;
   private int velocidad=200;
   private double frecDisparo;
   
   public PantallaJuego(final SpaceShooter game) {
       this.frecDisparo=1000000000;
       this.disparosAcertados=0;
       this.disparosRecibidos=0;
       this.navesDestruidas=0;
       this.game=game;
       this.imagenDisparo = new Texture(Gdx.files.internal("disparo.png"));
       this.imagenDisparoEnemigo = new Texture(Gdx.files.internal("disparoEnemigo.png"));
       this.imagenNave = new Texture(Gdx.files.internal("nave.png"));
       this.imagenNaveEnemiga = new Texture (Gdx.files.internal("naveEnemiga.png"));
       this.sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("torpedo.mp3"));
       this.musicaEspacial = Gdx.audio.newMusic(Gdx.files.internal("musicaespacial.mp3"));

      // se aplica que la música se repita en bucle, comienza la reproducción de la música de fondo
      musicaEspacial.setLooping(true);
      musicaEspacial.play();

      // crea la cámara ortográfica y el lote de sprites
      camara = new OrthographicCamera();
      camara.setToOrtho(false, 800, 480);
      game.batch = new SpriteBatch();

      // crea un rectángulo (clase Rectangle) para representar lógicamente el cubo
      nave = new Rectangle();
      nave.x = 800 / 2 - 64 / 2; // centra el cubo horizontal
      nave.y = 20; // esquina inferior izquierda del cubo estará a 20 píxeles del límite inferior
      nave.width = 64;
      nave.height = 64;
      
      naveEnemiga = new Rectangle();
      naveEnemiga.x = 800 / 2 - 64 / 2; // centra el cubo horizontal
      naveEnemiga.y = 400; // esquina inferior izquierda del cubo estará a 20 píxeles del límite inferior
      naveEnemiga.width = 64;
      naveEnemiga.height = 64;

      // crea el vector de gotas y crea la primera gota
      vectorDisparos = new Array<Rectangle>();
      vectorDisparosEnemigos = new Array<Rectangle>();
      
   }
   
   private void crearDisparo() {
      Rectangle disparo = new Rectangle();
      disparo.x = nave.x +32;
      disparo.y = nave.y + 32;
      disparo.width = 64;
      disparo.height = 64;
      vectorDisparos.add(disparo);
      tiempoUltimoDisparo = TimeUtils.nanoTime();
   }
   
   private void crearDisparoEnemigo() {
   
      Rectangle disparoEnemigo = new Rectangle();
      disparoEnemigo.x = naveEnemiga.x +32;
      disparoEnemigo.y = naveEnemiga.y + 32;
      disparoEnemigo.width = 64;
      disparoEnemigo.height = 64;
      vectorDisparosEnemigos.add(disparoEnemigo);
      tiempoUltimoDisparoEnemigo = TimeUtils.nanoTime();
   
   }

   @Override
   public void render(float Delta) {
      // limpia la pantalla con un color azul oscuro. Los argumentos RGB de la función glClearcColor están en el rango entre 0 y 1
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // ordenada a la cámara actualizar sus matrices
      camara.update();

      // indica al lote de sprites que se represente en las coordenadas específicas de la cámara
      game.batch.setProjectionMatrix(camara.combined);

      // comienza un nuevo proceso y dibuja el nave y las gotas
      game.batch.begin();
      game.batch.draw(imagenNave, nave.x, nave.y);
      game.batch.draw(imagenNaveEnemiga, naveEnemiga.x, naveEnemiga.y);
      
      for(Rectangle disparo: vectorDisparos) {
         game.batch.draw(imagenDisparo, disparo.x, disparo.y);
      }
      
      for(Rectangle disparo: vectorDisparosEnemigos) {
         game.batch.draw(imagenDisparoEnemigo, disparo.x, disparo.y);
      }
      
      game.font.setColor(Color.BLACK);
      game.font.draw(game.batch,"Naves destruidas: "+Integer.toString(navesDestruidas), 50, 130);
      game.font.draw(game.batch,"Disparos recibidos: "+Integer.toString(disparosRecibidos), 50, 100);
      game.batch.end();

      // lectura de entrada
      if(Gdx.input.isTouched()) {
         Vector3 posicionTocada = new Vector3();
         posicionTocada.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camara.unproject(posicionTocada);
         nave.x = posicionTocada.x - 64 / 2;
      }
      if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) nave.x -= 600 * Gdx.graphics.getDeltaTime();
      if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) nave.x += 600 * Gdx.graphics.getDeltaTime();
      
      
      
      if (TimeUtils.nanoTime()- tiempoUltimoDisparo > 1000000000) {
            if(Gdx.input.isKeyPressed(Input.Keys.UP)) { crearDisparo(); sonidoDisparo.play();}
            
      }
      
      if (TimeUtils.nanoTime()- tiempoUltimoDisparoEnemigo > frecDisparo) {
          crearDisparoEnemigo();
      }
      
      
      naveEnemiga.x += velocidad * Gdx.graphics.getDeltaTime();
      
      if (naveEnemiga.x > 800 - 95 || naveEnemiga.x < 0) {
          velocidad=-velocidad;
      }
      
      
      
      // nos aseguramos de que el nave permanezca entre los límites de la pantalla
      if(nave.x < 0) nave.x = 0;
      if(nave.x > 800 - 96) nave.x = 800 - 96;
      
      if (naveEnemiga.x < 0) naveEnemiga.x = 0;
      if(naveEnemiga.x > 800 - 96) naveEnemiga.x = 800 - 96;
      

      Iterator<Rectangle> iter2 = vectorDisparosEnemigos.iterator();
      while(iter2.hasNext()) {
          Rectangle disparoEnemigo = iter2.next();
          disparoEnemigo.y -= 200 * Gdx.graphics.getDeltaTime();
          if (disparoEnemigo.y + 64 < 0) {
              iter2.remove();
          }
          
          if (disparoEnemigo.overlaps(nave)) {
              iter2.remove();
              disparosRecibidos++;
              
          }
          
          
      }
      // recorre las gotas y borra aquellas que hayan llegado al suelo (límite inferior de la pantalla) o toquen el nave, en ese caso se reproduce sonido.
      Iterator<Rectangle> iter = vectorDisparos.iterator();
      while(iter.hasNext()) {
         Rectangle disparo = iter.next();
         disparo.y += 200 * Gdx.graphics.getDeltaTime();
         if(disparo.y + 64 > 800) {
             iter.remove();
         }
         if(disparo.overlaps(naveEnemiga)) {
            
            iter.remove();
            frecDisparo=frecDisparo/1.5;
            if (velocidad>1) {
                velocidad=velocidad+50;
            } else {
                velocidad=velocidad-50;
            }
            
            disparosAcertados++;
            
            
            if (disparosAcertados==3) {
                navesDestruidas++;
                naveEnemiga.x = 800 / 2 - 64 / 2;
                disparosAcertados=0;
            }
         }
      }
      
      if (navesDestruidas>=3) {
          game.batch.begin();
          game.font.setColor(Color.WHITE);
          game.batch.end();
          
          game.setScreen(new PantallaFinal(game,navesDestruidas,disparosRecibidos));
      }
      
      if (disparosRecibidos>=3) {
          game.batch.begin();
          game.font.setColor(Color.WHITE);
          game.batch.end();
          
          game.setScreen(new PantallaFinal(game,navesDestruidas,disparosRecibidos));
      }
   }

   @Override
   public void dispose() {
      // liberamos todos los recursos
      imagenDisparo.dispose();
      imagenNave.dispose();
      sonidoDisparo.dispose();
      musicaEspacial.dispose();
      game.batch.dispose();
   }

    @Override
    public void show() {
        musicaEspacial.play();
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        musicaEspacial.dispose();
    }

    @Override
    public void resume() {
        musicaEspacial.dispose();
    }

    @Override
    public void hide() {
        musicaEspacial.dispose();
    }
}
