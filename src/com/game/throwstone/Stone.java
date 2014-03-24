package com.game.throwstone;

import android.graphics.Bitmap;


public class Stone{
	
   public Bitmap ston_img;
   public float x;
   public float y;
    
   public Stone(Bitmap img, float x, float y)
   {
      this.ston_img = img;
      this.x = x;
      this.y = y;
   }
	
	public Bitmap getSton_img() {
		return ston_img;
	}
	
	public void setSton_img(Bitmap ston_img) {
		this.ston_img = ston_img;
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

}
