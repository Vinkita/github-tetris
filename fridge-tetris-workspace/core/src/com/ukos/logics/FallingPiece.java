/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**  
 * Utilizada para simular el movimiento de una instancia de {@link RotatableGrid}.
 * <br>El estado de {@code FallingPiece} cambia para reflejar movimento, 
 * mientras que el de la instancia de {@code RotatableGrid} permanece igual.
 * @author Ukos
 */
public class FallingPiece implements Cloneable{
    
    /**
     * La posicion de la pieza dentro del tablero
     */
    private  Point coord;
    /**
     * La pieza interior, que representa la forma y contiene la logica de rotacion
     */
    private RotatableGrid innerPiece;
    
    /**
     * Crea una nueva FallingPiece, con posicion (0,0), que contiene a la RotatableGrid especificada
     * @param innerPiece  la pieza interior
     */
    public FallingPiece(RotatableGrid innerPiece){
        this(new Point(0,0), innerPiece);
    }
    
    /**
     * Crea una nueva FallingPiece, con la posicion indicada, y que contiene a la RotatableGrid especificada
     * @param punto  la posicion inicial  
     * @param innerPiece  la pieza interior
     */
    private FallingPiece(Point punto, RotatableGrid innerPiece) {
        this.coord = punto;
        this.innerPiece = innerPiece;
    }
       
    /**
     * @return el componente x de la posicion
     */
    public float getX(){
        return coord.X();
    }
    
    /**
     * @return el componente y de la posicion
     */
    public float getY(){
        return coord.Y();
    }
    
    public Point getCoord(){
    	return coord;
    }
    
    public void setCoord(Point coord){
    	this.coord = coord;
    }
    
    /**
     * Crea una nueva copia de esta FallingPiece, pero con nuevas coordenadas
     * @param centro
     * @return
     */
    public FallingPiece moveTo(Point centro) {
        return new FallingPiece(centro, innerPiece);
    }

    /**
     * Crea una copia de esta FallingPiece, y la mueve una unidad hacia abajo 
     * @return  una FallingPiece debajo de la actual
     */
    public FallingPiece moveDown() {
        return new FallingPiece(coord.moveDown(), innerPiece);
    }
    
    /**
     * Crea una copia de esta FallingPiece, y la mueve una unidad hacia la izquierda 
     * @return  una FallingPiece debajo de la actual
     */
    public FallingPiece moveLeft(){
        return new FallingPiece(coord.moveLeft(), innerPiece);
    }
    
    /**
     * Crea una copia de esta FallingPiece, y la mueve una unidad hacia la derecha 
     * @return  una FallingPiece debajo de la actual
     */
    public FallingPiece moveRight(){
        return new FallingPiece(coord.moveRight(), innerPiece);
    }
    
    /**
     * Crea una copia de esta FallingPiece, y la rota hacia la derecha 
     * @return  una FallingPiece rotada hacia la derecha
     */
    public FallingPiece rotateRight(){
        return new FallingPiece(coord, innerPiece.rotateRight());
    }
    
    /**
     * Crea una copia de esta FallingPiece, y la rota hacia la izquierda 
     * @return  una FallingPiece rotada hacia la derecha
     */
    public FallingPiece rotateLeft(){
        return new FallingPiece(coord, innerPiece.rotateLeft());
    }
    
    /**
     * Devuelve el estilo de la celda que se encuentra en el Punto especificado
     * @param punto  las coordenadas de la celda
     * @return  el estilo de la celda
     */
    public String cellAt(Point punto) {
        Point inner = toInnerPoint(punto);
        return innerPiece.cellAt(inner);
    }

    /**
     * Toma todos los Point de la pieza interna, convierte sus coordenadas a coordenadas exteriores, 
     * y crea un Array conteniendo nuevos Point con las coordenadas exteriores.
     * @return
     */
    public Array<Point> allOuterPoints() {
    	Array<Point> innerPoints = innerPiece.getPoints();
        Array<Point> outerPoints = new Array<Point>();
        for (Point inner : innerPoints) {
            outerPoints.add(toOuterPoint(inner));
        }
        return outerPoints;
    }
    
    /**
     * @return  todos los BlockDrawable que componen la rotacion actual de la pieza interior.
     */
    public Array<BlockDrawable> allBlocks(){
    	Array<BlockDrawable> allBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : innerPiece.allBlocks())
    		allBlocks.add(block);
    	return allBlocks;
    }

    /**
     * @return un Array que contiene copias de los bloques de la pieza interior, 
     * con sus coordenadas convertidas a coordenadas exteriores.  
     */
    public Array<BlockDrawable> allOuterBlocks(){
    	Array<BlockDrawable> allBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : innerPiece.allBlocks())
    		allBlocks.add(new BlockDrawable(toOuterPoint(block.getPoint()), block.getStyle(), block.isGhost(), block.getTextureRegion()));
    	return allBlocks;
    }
    
    /**
     * Convierte las coordenadas exteriores de un Point a coordenadas interiores de la pieza, 
     * segun la posicion de la misma 
     * @param punto
     * @return
     */
    private Point toInnerPoint(Point punto){
    	return punto.add(-coord.X(), -coord.Y());
//        return new Point(punto.getRow() - coord.getRow(), punto.getCol() - coord.getCol());
    }
    
    /**
     * Convierte las coordenadas de un Point a coordenadas exteriores a la pieza, 
     * segun la posicion de la misma 
     * @param punto
     * @return
     */
    public Point toOuterPoint(Point punto){
    	return punto.add(coord);
//        return new Point(punto.getRow() + coord.getRow(), punto.getCol() + coord.getCol());
    }

    /**
     * Determina si la pieza esta ocupando el espacio indicado 
     * @param punto  
     * @return
     */
    public boolean isAt(Point punto) {
        Point inner = toInnerPoint(punto);
        for (Point block : innerPiece.getPoints())
        	if (block.X() == inner.X() && block.Y() == inner.Y())
        		return true;
        return false;
    }
    
    public boolean isAtX(Point punto) {
    	Point inner = toInnerPoint(punto);
    	for (Point block : innerPiece.getPoints())
    		if (block.X() == inner.X())
    			return true;
    	return false;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new FallingPiece(new Point(coord.X(), coord.Y()), (RotatableGrid)innerPiece.clone());
    }

    public String toString() {
    	String points = "";		
		for(Point point : allOuterPoints()){
			points += "," + point.toString();
		}
		points = points.replaceFirst(",", "");
		return points;
    }

	/**
	 * @return  la pieza interna
	 */
	public RotatableGrid getInnerPiece() {
		return innerPiece;
	}

	/**
	 * @param innerPiece  la nueva pieza interna
	 */
	public void setInnerPiece(RotatableGrid innerPiece) {
		this.innerPiece = innerPiece;
	}
    
    
}
