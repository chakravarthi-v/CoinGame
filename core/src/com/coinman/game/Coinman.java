package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Coinman extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,coin,bomb,dizzy;
	Texture[] man;
	int manState=0,pause=0,manY,coinCount=0,bombCount=0,score=0,gameState=0;
	float velocity=0,gravity=0.8f;
	ArrayList<Rectangle> coinRectangle=new ArrayList<>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<>();
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Integer> bombXs=new ArrayList<>();
	ArrayList<Integer> bombYs=new ArrayList<>();
	Random random;
	Rectangle manRec;
	BitmapFont pic;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man =new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		dizzy=new Texture("dizzy-1.png");
		random=new Random();
		pic=new BitmapFont();
		pic.setColor(Color.WHITE);
		pic.getData().setScale(10);
	}
	public void makeCoin(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makBomb(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState==1){
			//game is live
			     //make Bomb
			if(bombCount<250){
				bombCount++;
			}
			else{
				bombCount=0;
				makBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-25);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}
			//make coins
			if(coinCount<100){
				coinCount++;
			}
			else{
				coinCount=0;
				makeCoin();
			}
			coinRectangle.clear();
			for(int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-20);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
			if(Gdx.input.isTouched()){
				velocity=-13;
			}
			if(pause<8){
				pause++;
			}
			else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity+=gravity;
			manY-=velocity;
			if(manY<=0){
				manY=0;
			}
		}
		else if(gameState==0){
			//waiting to start
			if(Gdx.input.isTouched()){
				gameState=1;
			}
		}
		else if(gameState==2){
			//game over
			if(Gdx.input.isTouched()){
				if(Gdx.input.isTouched()) {
					gameState = 1;
					manY = Gdx.graphics.getHeight() / 2;
					score = 0;
					velocity = 0;
					coinYs.clear();
					coinXs.clear();
					coinRectangle.clear();
					coinCount = 0;
					bombYs.clear();
					bombXs.clear();
					bombRectangle.clear();
					bombCount = 0;
				}
			}
		}
		if(gameState==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY);
		}
		else{
			batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY);
		}
		manRec=new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
		for(int i=0;i<coinRectangle.size();i++){
			if(Intersector.overlaps(manRec,coinRectangle.get(i))){
				score++;
				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for(int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRec,bombRectangle.get(i))){
				gameState=2;
			}
		}
		pic.draw(batch,String.valueOf(score),100,200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
