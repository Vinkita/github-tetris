package com.ukos.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BmFontAccessor implements TweenAccessor<BitmapFont> {
	
	public static final int SCALE = 0, COLOR = 1, ALPHA = 2; 

	@Override
	public int getValues(BitmapFont target, int tweenType, float[] returnValues) {
		 switch(tweenType){
		 case SCALE:
			 returnValues[0] = target.getScaleX();
			 returnValues[1] = target.getScaleY();
			 return 2;
		 case COLOR:
			 returnValues[0] = target.getColor().r;
			 returnValues[1] = target.getColor().g;
			 returnValues[2] = target.getColor().b;
			 returnValues[2] = target.getColor().a;
			 return 4;
		 case ALPHA:
			 returnValues[0] = target.getColor().a;
			 return 1;
		 default:
			 assert false;
			 return -1;
		 }
	}

	@Override
	public void setValues(BitmapFont target, int tweenType, float[] newValues) {
		 switch(tweenType){
		 case SCALE:
			 target.setScale(newValues[0]);
			 break;
		 case COLOR:
			 target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
			 break;
		 case ALPHA:
			 target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
		 default:
			 assert false;
		 }
		
	}

}
