package com.ukos.logics;


public class BlockDrawable {
	
	private Point point;
	//El objetivo de style es contener el string que indica que textura se debe cargar
	private String style;
	
	public BlockDrawable(Point point, String style) {
		this.point = point;
		this.style = style;
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
	
	

}
