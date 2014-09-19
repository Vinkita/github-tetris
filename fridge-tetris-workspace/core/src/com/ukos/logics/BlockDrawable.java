package com.ukos.logics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class BlockDrawable implements Cloneable{
	
	private Point point;
	//El objetivo de style es contener el string que indica que textura se debe cargar
	private String style;
	private boolean ghost;
	private float width;
	private float height;
	
	public BlockDrawable(Point point, String style) {
		init(point, style);
	}

	public BlockDrawable(Point point, String style, boolean ghost) {
		init(point, style, ghost);
	}
	
	private void init(Point point, String style){
		init(point, style, false);
	}

	private void init(Point point, String style, boolean ghost){
		this.point = point;
		this.style = style;
		width = height = 1;
		this.ghost = ghost;
	}
	
	public boolean isGhost(){
		return ghost;
	}

	public void setGhost(boolean ghost){
		this.ghost = ghost;
	}
	
	public Point getPoint() {
		return point;
	}

	public void setPunto(Point point) {
		this.point = point;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public void draw(Batch batch, TextureRegion texture, Vector2 offset){
		if(ghost)
			batch.setColor(.3f, .3f, .3f, .3f);
		batch.draw(texture, offset.x + point.X(), offset.y + point.Y(), width, height);		
		if(ghost)
			batch.setColor(1, 1, 1, 1);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		BlockDrawable aux = (BlockDrawable) super.clone();
		aux.setPunto(new Point(point.X(), point.Y()));
		return aux;
	}

	public void setSize(float w, float h) {
		 width = w;
		 height = h;		
	}
	
	

}
