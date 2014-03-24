package com.game.throwstone;

import android.graphics.Bitmap;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class CopyOfStone {
	
   public float radius;
   public ImageView stoneIV;
   public float x;
   public float y;
    
   public CopyOfStone(Bitmap stoneBmp, float radius, float x, float y)
   {

	  //this.stoneIV = new ImageView(this);
	  RotateAnimation  anim = new RotateAnimation(0, 360, stoneIV.getWidth()/2, stoneIV.getHeight()/2);
	  anim.setDuration(1000); 
	  stoneIV.startAnimation(anim); 
      this.radius = radius;
      this.x = x;
      this.y = y;
   }

}
