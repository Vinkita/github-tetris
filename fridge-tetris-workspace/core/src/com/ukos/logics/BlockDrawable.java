package com.ukos.logics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class BlockDrawable implements Cloneable{
	
	/**
	 * Punto que representa la ubicacion de este BlockDrawable
	 */
	private Point point;
	//El objetivo de style es contener el string que indica que textura se debe cargar
	/**
	 * Éste String indica qué textura se debera cargar 
	 * TODO hate
	 */
	private String style;
	/**
	 * Si esta variable es true, el bloque será dibujado con semitranparencia
	 */
	private boolean ghost;
	/**
	 * El ancho del bloque
	 */
	private float width;
	/**
	 * La altura del bloque
	 */
	private float height;
	
	/**
	 * Crea un nuevo BlockDrawable a con la posicion, el estilo, y el estado "ghost" especificados
	 * @param point  la posicion
	 * @param style  el codigo de estilo
	 * @param ghost  si el bloque debe ser dibujado como "fantasma" o no
	 */
	public BlockDrawable(Point point, String style, boolean ghost) {
		init(point, style, ghost);
	}
	
	/**
	 * Crea un nuevo BlockDrawable a con la posicion y el estilo especificados. El valor de ghost se setea false por defecto
	 * @param point  la posicion
	 * @param style  el codigo de estilo
	 */
	public BlockDrawable(Point point, String style) {
//		init(point, style);
		this(point, style, false);
	}
	
//	private void init(Point point, String style){
//		init(point, style, false);
//	}

	/**
	 * Es llamada por el costructor para inicializar el BLockDrawable
	 */
	private void init(Point point, String style, boolean ghost){
		this.point = point;
		this.style = style;
		width = height = 1;
		this.ghost = ghost;
	}
	
	/**
	 * @return  Si el bloque es "fantasma" o no
	 */
	public boolean isGhost(){
		return ghost;
	}

	/**
	 * @param ghost el nuevo valor de la variable ghost
	 */
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
	
	/**
	 * Dibuja un bloque
	 * @param batch  el objeto Batch 
	 * @param texture  la textura correspondiente al bloque
	 * @param offset  la posicion relativa dentro de la pantalla
	 */
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

	/**
	 * Cambia el tamaño del bloque
	 * @param w  el nuevo ancho
	 * @param h  la nueva altura
	 */
	public void setSize(float w, float h) {
		 width = w;
		 height = h;		
	}
	
	

}
