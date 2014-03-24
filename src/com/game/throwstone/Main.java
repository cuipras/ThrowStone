package com.game.throwstone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Main extends Activity implements OnClickListener
{
  public void onCreate(Bundle savedInstanceState)
  {
     super.onCreate(savedInstanceState);
     //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     setContentView(R.layout.main);
     
     ImageButton btn = (ImageButton)findViewById(R.id.imageButton1);
     btn.setOnClickListener(this);

  }
  
  public void onClick(View v) {
	MainView gameView = new MainView(this);
	setContentView(gameView);
//	try{
//		while(MainView.getGameStat() == 1){
//			Thread.sleep(1000);
//		}
//	}catch(Exception e){}
//	
//	//return to the start page
//	setContentView(R.layout.main);
	
  }

}
