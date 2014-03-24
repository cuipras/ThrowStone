package com.game.throwstone;

import android.graphics.Bitmap;

public class Worm {

   private Bitmap bug_img;
   private float x;
   private float y;
   private int status;//0:free 1:safe -1:dead
    
   public Worm(Bitmap img, float x, float y)
   {
      this.bug_img = img;
      this.x = x;
      this.y = y;
      this.status = 0;
   }
	
	public Bitmap getBug_img() {
		return bug_img;
	}
	
	public void setBug_img(Bitmap bug_img) {
		this.bug_img = bug_img;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
   

}
