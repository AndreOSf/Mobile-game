package com.example.basketballmobile;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FrameLayout gameFrame;
    private int frameHeight, frameWidth;
    private LinearLayout mainMenu;

    private ImageView cesta, bola;
    //private Drawable cestaDir, cestaEsq;

    private int cestaTam;
    private int er;

    private float cestaX, cestaY;
    private float bolaX, bolaY;

    private TextView pontuacao;
    private int pontuacaoInt, time;

    private Timer timer;
    private Handler handler = new Handler();

    private  boolean start_flag = false;
    private boolean action_flag = false;
    private boolean bola_flag = false;

    private Drawable imageCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        mainMenu = findViewById(R.id.mainMenu);
        cesta = findViewById(R.id.cesta);
        bola = findViewById(R.id.bola);
        pontuacao = findViewById(R.id.pontuacao);

        imageCesta = ResourcesCompat.getDrawable(getResources(), R.drawable.cesta, null);
        //imageCesta = AppCompatResources.getDrawable(null, R.drawable.cesta);
    }

    public void changePos(){

        time += 1;

        //bola
        bolaY += 25;

        float bolaCenterX = bolaX + bola.getWidth()/2;
        float bolaCenterY = bolaY + bola.getHeight()/2;

        if(hitCheck(bolaCenterX, bolaCenterY)){
            bolaY = frameHeight + 100;
            pontuacaoInt += 1;
        }
        if(!hitCheck(bolaCenterX, bolaCenterY) && bolaY > cestaY){
            er += 1;
        }

        if(bolaY > frameHeight){
            bolaY = -100;
            bolaX = (float) Math.floor(Math.random() * (frameWidth - bola.getWidth()));
        }
        bola.setX(bolaX);
        bola.setY(bolaY);

        //
        if(action_flag){
            cestaX += 17;
            cesta.setImageDrawable(imageCesta);
        }
        else {
            cestaX -= 17;
            cesta.setImageDrawable(imageCesta);
        }

        if(cestaX < 0){
            cestaX = 0;
            cesta.setImageDrawable(imageCesta);
        }
        if((frameWidth - cestaTam)-50 < cestaX){
            cestaX = (frameWidth - cestaTam)-50;
            cesta.setImageDrawable(imageCesta);
        }

        if( er == 50){
            gameOver();
        }
        if(time == 1000){
            gameOver();
        }

        cesta.setX(cestaX);
        pontuacao.setText("Pontuação: "+ pontuacaoInt);
    }

    public boolean hitCheck(float x, float y){
        if(cestaX <= x && x <= cestaX + cestaTam && cestaY <= y && y <= frameHeight){
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(start_flag){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                action_flag = true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                action_flag = false;
            }
        }
        return true;
    }

    public void gameOver(){
        timer.cancel();
        timer = null;
        start_flag = false;
        er = 0;

        mainMenu.setVisibility(View.VISIBLE);
        cesta.setVisibility(View.INVISIBLE);
        bola.setVisibility(View.INVISIBLE);
    }

    public void startGame(View view){

        start_flag = true;
        mainMenu.setVisibility(View.INVISIBLE);

            if(frameHeight == 0){
                frameHeight = gameFrame.getHeight();
                frameWidth = gameFrame.getWidth();
                cestaTam = cesta.getHeight();

                cestaX = cesta.getX();
                cestaY = cesta.getY();
            }

        cesta.setX(0.0f);
        bola.setY(3000.0f);

        bolaY = bola.getY();

        cesta.setVisibility(View.VISIBLE);
        bola.setVisibility(View.VISIBLE);

        time = 0;
        pontuacaoInt = 0;
        pontuacao.setText("Pontuação: 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(start_flag){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

}
