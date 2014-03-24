package com.game.throwstone;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	   private SurfaceHolder holder;
	   private Thread thread;

	   private Paint paintImg;
	   private Paint paintYel;
	   private Paint paintRed;
	   private Paint paintBkg;

	   private Bitmap bkgrnd;
	   private Bitmap throwImg1;
	   private Bitmap throwImg2;
	   private Bitmap stoneImg;
	   private Bitmap bump1Img;
	   private Bitmap bump2Img;
	   private Bitmap[] stoneRotateImg;
	     
	   private Stone tStone;
	   private ArrayList<Worm> wormContainer;
	   
	   private int throwStatus; //0:readyForNextThrow  1: adjustPower  2:throwed
	   private int timeThrow;
	   private int countStone;
	   private float powerThrow;
	   private double speedThrowX;
	   private double speedThrowY;
	   private double angelThrowX;
	   private double angelThrowY;
	   
	   private Date pushDownTime;
	   private static int gameStat;
	   
	   /////// --Parameters-- //////////////////////////////////////////////////
	   
	   private static final double WIGHT_ACCER = 130;
	   private static final double TIME_RATE = 0.06;
	   private static final float POWER_SPEED_RATE = (float)4.0;
	   private static final float WORM_MOVE_SPEED = (float)0.6;

	   private static final int THROW_IMG_Y = 240;
	   private static final int STONE_FROM_X = 120;
	   private static final int STONE_FROM_Y = 280;

	   private static final int BUCKET_FROM_Y = 330;
	   private static final int BUCKET_FROM_X = 210;
	   private static final int BUCKET_TO_X = 320;
	   
	   //////////////////////////////////////////////////////////////
	 
	   public MainView(Context context)
	   {
	      super(context);
	      gameStat = 1;
	 
	      holder = null;
	      thread = null;

	      getHolder().addCallback(this);
	      bkgrnd = BitmapFactory.decodeResource(getResources(), R.drawable.bkground1);
	      throwImg1 = BitmapFactory.decodeResource(getResources(), R.drawable.throw1);
	      throwImg2 = BitmapFactory.decodeResource(getResources(), R.drawable.throw2);
	      stoneImg = BitmapFactory.decodeResource(getResources(), R.drawable.stone);
	      bump1Img = BitmapFactory.decodeResource(getResources(), R.drawable.bump1);
	      bump2Img = BitmapFactory.decodeResource(getResources(), R.drawable.bump2);
	      stoneRotateImg = new Bitmap[6];
		  Matrix mtrx = new Matrix();
	      for (int i=0; i<stoneRotateImg.length; i++){
             mtrx.postRotate(60*(i+1), stoneImg.getWidth()/2, stoneImg.getHeight()/2);
        	 stoneRotateImg[i] = Bitmap.createBitmap(stoneImg, 0, 0, stoneImg.getWidth(), stoneImg.getHeight(), mtrx, true);
	      }
	      
	      paintImg = new Paint();
	      paintBkg = new Paint();
	      
	      paintRed = new Paint();
	      paintRed.setAntiAlias(true);
	      paintRed.setColor(Color.RED);
	      
	      paintYel = new Paint();
	      paintYel.setAntiAlias(true);
	      paintYel.setColor(Color.YELLOW);
	      paintYel.setStrokeWidth(10);
	      
	      this.countStone = 0;
	      this.throwStatus = 0;
	      this.powerThrow = -1;
     	  this.timeThrow = -1;
	   }
	 
	   public void surfaceDestroyed(SurfaceHolder holder)
	   {
	      thread = null;
	   }
	 
	   public void surfaceCreated(SurfaceHolder holder)
	   {
	      this.holder = holder;
	      thread = new Thread(this);
	   }
	 
	   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	   {
	      this.initializeObject(width, height);
	   }
	   
	   private void initializeObject(int width, int height){

		  tStone = new Stone(stoneImg, (float)STONE_FROM_X, (float)STONE_FROM_Y);
	      wormContainer = new ArrayList<Worm>();
	      for(int i=0; i<10; i++){
	    	 int bugId = ((int)(Math.random() * 100)) % 3;
	    	 
	    	 if (bugId == 0) bugId = R.drawable.bug1;
	    	 else if (bugId == 1) bugId = R.drawable.bug2; 	 
	    	 else if (bugId == 2) bugId = R.drawable.bug3;
	    	 else bugId = R.drawable.bug3;
	    	 
		     Bitmap bugImg = BitmapFactory.decodeResource(getResources(), bugId);
	         Worm bug = new Worm(bugImg, (float)(width*0.9+i*200+Math.random()*50), (float)(height*(0.4 + Math.random()*0.5)));
	         wormContainer.add(bug);
	      }

	      if(thread != null) thread.start();
	   }
	 
	   // Ãè»­
	   public void run()
	   {
		 int loopCnt = 0;
         
	      while (thread != null)
	      {
	    	 loopCnt = loopCnt % 6;
	         Canvas canvas = holder.lockCanvas();
	         
	         try{
	        	 //draw back ground
	        	 this.drawBackGround(canvas);
	        	 //draw the bugs
	        	 boolean gameOverYN = this.drawBugs(canvas);
		         //game over
	        	 if (gameOverYN){
	       	      	 this.drawScore(canvas);
	        	 }
	        	 if (this.throwStatus == 1){
			         //draw power line
	        		 this.drawPoweline(canvas);
	        	 }
	        	 if (this.throwStatus == 2){
		        	 this.timeThrow++;
	         		 //draw the gollira
	    	         canvas.drawBitmap(throwImg2, 0, THROW_IMG_Y, paintBkg);
	         		 //draw the stone
		        	 this.drawStone(canvas, loopCnt);
		         } else {
	         		 //draw the gollira
	    	         canvas.drawBitmap(throwImg1, 0, THROW_IMG_Y, paintBkg);
		         }
	        	 ///////////////////////
	        	 Thread.sleep(5);
	        	 loopCnt ++;
	        	 ///////////////////////
	         } catch (InterruptedException e) {
	             e.printStackTrace();
	         }
	         
	         holder.unlockCanvasAndPost(canvas);
	      }
	   }

	   private void drawBackGround(Canvas canvas){
	      canvas.drawBitmap(bkgrnd, 0, 0, paintBkg);
	      int idxDeadBug = 0;
		  for(Worm bug : wormContainer){
        	if(bug.getStatus() == 1){
        		
        	} else if(bug.getStatus() == -1){
        		idxDeadBug ++;
        		canvas.drawCircle(idxDeadBug * 30, 30, 10, paintRed);
        	}
		  }
	   }

	   private void drawScore(Canvas canvas){
	      Paint paintTxt = new Paint();
	      paintTxt.setAntiAlias(true);
	      paintTxt.setColor(Color.DKGRAY);
	      
	      Bitmap scoreBoard = BitmapFactory.decodeResource(getResources(), R.drawable.scoreboard);
	      Bitmap restartBtn = BitmapFactory.decodeResource(getResources(), R.drawable.restartbtn);
	      canvas.drawBitmap(scoreBoard, 200, 70, paintBkg);
	      canvas.drawBitmap(restartBtn, 320, 260, paintBkg);
	      
		  int countBugs = 0;
		  for(Worm bug : wormContainer){
        	if(bug.getStatus() == -1){
        		countBugs++;	
        	}
		  }
	      paintTxt.setTextSize(30);
    	  canvas.drawText("Count of stones used : " + String.valueOf(this.countStone), 290f, 120f, paintTxt);
    	  canvas.drawText("Count of bugs struck : " + String.valueOf(countBugs), 290f, 170f, paintTxt);
    	  int score = countBugs * 10 - (this.countStone - wormContainer.size());
	      paintTxt.setTextSize(50);
    	  canvas.drawText("Your Score : " + String.valueOf(score), 290f, 240f, paintTxt);
    	  
    	  //game stop
	      thread = null;
	      this.countStone = 0;
	   }
	   
	   private boolean drawBugs(Canvas canvas){

      	 boolean gameOverYN = true;
		  for(Worm bug : wormContainer){
        	if(bug.getStatus() == 0){
	        	//bug moves
	            bug.setX(bug.getX() - WORM_MOVE_SPEED);
	            if (bug.getX() > BUCKET_TO_X) {
	         		canvas.drawBitmap(bug.getBug_img(), 
	         				(float)(bug.getX()-bug.getBug_img().getWidth()*0.5), 
	         				(float)(bug.getY()-bug.getBug_img().getHeight()*0.5), paintImg);
	            }else{
	            	bug.setStatus(20);
	            }
         		gameOverYN = false;
        	} else if (bug.getStatus() < -1){
         		canvas.drawBitmap(this.bump1Img, 
         				(float)(bug.getX()-this.bump1Img.getWidth()*0.5), 
         				(float)(bug.getY()-this.bump1Img.getHeight()*0.5), paintImg);
         		bug.setStatus(bug.getStatus() + 1);
         		gameOverYN = false;
        	} else if (bug.getStatus() > 1){
         		canvas.drawBitmap(this.bump2Img, 
         				(float)(bug.getX()-this.bump2Img.getWidth()*0.5), 
         				(float)(bug.getY()-this.bump2Img.getHeight()*0.5), paintImg);
         		bug.setStatus(bug.getStatus() - 1);
         		gameOverYN = false;
        	}
		  }
		  return gameOverYN;
	   }
	   
	   private void drawStone(Canvas canvas, int loopCnt){

  		 double time = this.timeThrow * TIME_RATE;
  		 tStone.setX((float)(STONE_FROM_X + (speedThrowX * time)));
  		 tStone.setY((float)(STONE_FROM_Y + (speedThrowY * time) + (0.5 * time * time * WIGHT_ACCER)));
  		 double tgAngle = Math.abs(speedThrowX / speedThrowY);

      	 if (isAttacked() || (tgAngle > 10 && tStone.getX() > BUCKET_FROM_X)
      		|| tStone.getX() > (this.holder.getSurfaceFrame().width()+(tStone.getSton_img().getWidth()*0.5)) 
      		|| tStone.getY() > (this.holder.getSurfaceFrame().height()+(tStone.getSton_img().getHeight()*0.5))
      		|| (tStone.getY() > BUCKET_FROM_Y && tStone.getX() > BUCKET_FROM_X && tStone.getX() < BUCKET_TO_X) ){
      		 
      		 tStone.setX((float)STONE_FROM_X);
      		 tStone.setY((float)STONE_FROM_Y);
      		 this.powerThrow = -1;
      		 this.throwStatus = 0;
      		 this.timeThrow = -1;
      		 
      		 if (tgAngle <= 10) this.countStone++;
	        	 
      	 }else{
      		 //draw the stone
       		 canvas.drawBitmap(stoneRotateImg[loopCnt],
       				 (float)(tStone.getX()-tStone.getSton_img().getWidth()*0.5),
       				 (float)(tStone.getY()-tStone.getSton_img().getHeight()*0.5), paintImg);
      	 }
	   }

	   private void drawPoweline(Canvas canvas){
		   this.powerThrow = ((new Date()).getTime() - this.pushDownTime.getTime()) / 10;
		   if (this.powerThrow > 120f) this.powerThrow = 120f;
		   //canvas.drawLine(20, 450, 20+this.powerThrow, 450, this.paintYel);
		   canvas.drawLine((float)STONE_FROM_X, (float)STONE_FROM_Y, 
						   (float)(STONE_FROM_X + this.angelThrowX * this.powerThrow),
						   (float)(STONE_FROM_Y + this.angelThrowY * this.powerThrow), this.paintYel);
	   }
	   
	   private boolean isAttacked(){
		 
         for(Worm bug : this.wormContainer){
        	if (bug.getStatus() == 0){
	            float dist = ((tStone.getX()-bug.getX())*(tStone.getX()-bug.getX())) + ((tStone.getY()-bug.getY())*(tStone.getY()-bug.getY()));
	            
	            if (dist < 400f) {
	            	bug.setStatus(-20);
	            	return true;
	            }
        	}
         }
         return false;
	   }
	   
	   //////Touch Event///////
	   public boolean onTouchEvent(MotionEvent event)
	   {
	      float x = event.getX();
	      float y = event.getY();
 		  setAngleThrow(x, y);
	 
	      // »­Ãæ¤Î¥¿¥Ã¥Á•r
	      if (event.getAction() == MotionEvent.ACTION_DOWN){
	         if (thread != null && throwStatus == 0){
	        	 this.pushDownTime = new Date();
	        	 this.powerThrow = 0f;
	        	 this.throwStatus = 1;
	        	 this.timeThrow = -1;
	         }
	      } else if (event.getAction() == MotionEvent.ACTION_MOVE){
	    	  //this.pushDownTime = null;
	      } else if (event.getAction() == MotionEvent.ACTION_UP){
	    	 //game restart
	    	 if (isRestart(x, y)){
	   	        thread = new Thread(this);
	   	        this.initializeObject(this.holder.getSurfaceFrame().width(), this.holder.getSurfaceFrame().height());
	    	 } else if (thread != null && this.pushDownTime != null){
	    		 this.speedThrowX = this.angelThrowX * (this.powerThrow * POWER_SPEED_RATE);
	    		 this.speedThrowY = this.angelThrowY * (this.powerThrow * POWER_SPEED_RATE);
	    		 
	    		 this.angelThrowX = 0;
	    		 this.angelThrowY = 0;
	    		 this.pushDownTime = null;
	    		 this.timeThrow = 0;
	    		 this.throwStatus = 2;
	    	 }
	      }
	      return true;
	   }
	   
	   private boolean isRestart(float x, float y){
    	 if (thread == null && 
    		(x >= 380 && x <= 520 && y >= 270 && y <= 360)){
    		 return true;
    	 }
    	 return false;
	   }
	   
	   private void setAngleThrow(float x, float y){
  		 this.angelThrowX = (x - STONE_FROM_X) / Math.sqrt((y - STONE_FROM_Y) * (y - STONE_FROM_Y) + (x - STONE_FROM_X) * (x - STONE_FROM_X));
  		 this.angelThrowY = (y - STONE_FROM_Y) / Math.sqrt((y - STONE_FROM_Y) * (y - STONE_FROM_Y) + (x - STONE_FROM_X) * (x - STONE_FROM_X));
	   }

	public static int getGameStat() {
		return gameStat;
	}

	public static void setGameStat(int gameStat) {
		MainView.gameStat = gameStat;
	}

}
