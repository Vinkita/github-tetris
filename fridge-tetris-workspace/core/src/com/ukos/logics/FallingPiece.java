/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author MarianoMDQ
 */
public class FallingPiece{
    
    private final Point coord;
    private final RotatableGrid innerPiece;
    
    public FallingPiece(RotatableGrid innerPiece){
        this(new Point(0,0), innerPiece);
    }
    
    private FallingPiece(Point punto, RotatableGrid innerPiece) {
        this.coord = punto;
        this.innerPiece = innerPiece;
    }
       
    public float getX(){
        return coord.X();
    }
    
    public float getY(){
        return coord.Y();
    }
    
//    public int lastOccupiedRow(){
//        int length = rows() - 1;
//        int last = getY() + length;
//        for(int r = length; r >=0; r--){
//            if(emptyRow(r)){
//                last--;
//            }
//            else
//                return last;
//        }
//        return last;
//    }
    
//    private boolean emptyRow(int row){
//        for(int c = 0; c < cols(); c++){
//            if(innerPiece.cellAt(new VectorPoint(row, c)) != EMPTY)
//                return false;
//        }
//        return true;
//    }
    
    public FallingPiece moveTo(Point centro) {
        return new FallingPiece(centro, innerPiece);
    }
    
    public FallingPiece moveDown() {
        return new FallingPiece(coord.moveDown(), innerPiece);
    }
    
    public FallingPiece moveLeft(){
        return new FallingPiece(coord.moveLeft(), innerPiece);
    }
    
    public FallingPiece moveRight(){
        return new FallingPiece(coord.moveRight(), innerPiece);
    }
    
    public FallingPiece rotateRight(){
        return new FallingPiece(coord, innerPiece.rotateRight());
    }
    
    public FallingPiece rotateLeft(){
        return new FallingPiece(coord, innerPiece.rotateLeft());
    }
    
    public String cellAt(Point punto) {
        Point inner = toInnerPoint(punto);
        return innerPiece.cellAt(inner);
    }
    
    public Array<Point> allOuterPoints() {
    	Array<Point> innerPoints = innerPiece.getPoints();
        Array<Point> outerPoints = new Array<Point>();
        for (Point inner : innerPoints) {
            outerPoints.add(toOuterPoint(inner));
        }
        return outerPoints;
    }
    
    public Array<BlockDrawable> allBlocks(){
    	Array<BlockDrawable> allBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : innerPiece.allBlocks())
    		allBlocks.add(block);
    	return allBlocks;
    }

    public Array<BlockDrawable> allOuterBlocks(){
    	Array<BlockDrawable> allBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : innerPiece.allBlocks())
    		allBlocks.add(new BlockDrawable(toOuterPoint(block.getPoint()), block.getStyle()));
    	return allBlocks;
    }
    
    private Point toInnerPoint(Point punto){
    	return punto.add(-coord.X(), -coord.Y());
//        return new Point(punto.getRow() - coord.getRow(), punto.getCol() - coord.getCol());
    }
    
    public Point toOuterPoint(Point punto){
    	return punto.add(coord);
//        return new Point(punto.getRow() + coord.getRow(), punto.getCol() + coord.getCol());
    }

    public boolean isAt(Point punto) {
        Point inner = toInnerPoint(punto);
        for (Point block : innerPiece.getPoints())
        	if (block.X() == inner.X() && block.Y() == inner.Y())
        		return true;
        return false;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new FallingPiece(new Point(coord.X(), coord.Y()), innerPiece);
    }

    
    
}
