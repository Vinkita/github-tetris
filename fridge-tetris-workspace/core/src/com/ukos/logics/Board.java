package com.ukos.logics;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Board implements Grid{
    
    private FallingPiece falling;
    private Array<BlockDrawable> tablero;
    private ArrayList<IRowListener> listeners = new ArrayList<IRowListener>();
    private float width;
    private float height;
    private long autoFallRate = 500000000;
	private long lastAutoFall;
	private long moveRate     = 100000000;
	private long lastMove;
	private ShuffleBag bolsita = new ShuffleBag();

    
    public Board(float width, float height) {
        tablero = new Array<BlockDrawable>();
        this.width = width;
        this.height = height;
    }
    
    public void drop(RotatableGrid piece) {
    	if (!hasFalling()) {
	        Point centro = new Point(width / 2, height-1);
	        falling = new FallingPiece(piece).moveTo(centro);
    	}
        
    }
    
    public void tick() {
    	 if (hasFalling()){
             if (!moveIfNoConflict(falling.moveDown()))
            	 stopFallingBlock();
    	 }
    }
    
    public boolean hasFalling(){
        return falling != null;
    }

    private void stopFallingBlock() {
        assert hasFalling();
        copyToBoard(falling);
        falling = null;
        
        triggerListeners(checkLines());        
    }
     
    private void copyToBoard(FallingPiece piece) {
    	tablero.reverse();
    	for (BlockDrawable block : piece.allBlocks()){
    		block.setPunto(piece.toOuterPoint(block.getPoint()));
			tablero.add(block);
		}
    	tablero.reverse();
    }
    
    private void deleteRow(float row){
        if(row > 0){
        	for (Iterator<BlockDrawable> blocks = tablero.iterator(); blocks.hasNext();){
        		BlockDrawable block = blocks.next();
        		if (block.getPoint().Y() == row)
        			blocks.remove();
        		else if (block.getPoint().Y() > row)
        			block.setPunto(block.getPoint().moveDown());
        	}
        }
    }
        
    private int checkLines(){
    	int contRows = 0;
    	
    	for (float y = height; y >= 0 && contRows < 4; y--) {
    		if (isFullRow(y)) {
    			contRows++;
    			deleteRow(y);
    		}
    	}
    	return contRows;
    }
    
    private boolean isFullRow(float row){
    	int contCol=0;
    	for (BlockDrawable block : tablero) {
    		if (block.getPoint().Y() == row)
    			contCol++;
    		if (contCol >= width)
    			return true;
    	}
    	return false;
    }
    
    private void triggerListeners(int removedRows) {
        for(IRowListener listener : listeners){
            listener.onRowsRemoved(removedRows);
        }
    }
    
    public void addRowListener(IRowListener listener){
        listeners.add(listener);
    }
    
    public void movePieceToLeft() {
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moveIfNoConflict(falling.moveLeft());
            }
        }
    }
    
    public void movePieceToRight(){
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moveIfNoConflict(falling.moveRight());
            }
        }
    }
    
    public void movePieceDown(){
        if (hasFalling()){
        	if (moveTooFast()) {
    			return;
    		}
        	lastMove = TimeUtils.nanoTime();
        	if(timeForAutoFall()){
        		lastAutoFall = TimeUtils.nanoTime();
        		
        	}if (!moveIfNoConflict(falling.moveDown()))
                stopFallingBlock();
        }
    }
    
    public boolean moveIfNoConflict(FallingPiece test){
        if (!conflictsWithBoard(test)){
            falling = test;
            return true;
        }
        return false;
    }
    
    private void Kick(FallingPiece piece){
        int wide = 2;
        
        if(!doRightKick(piece, wide)){
            doLeftKick(piece, wide);
        }
    }
    
    private boolean doRightKick(FallingPiece test, int iterations){
        boolean kicked = false;
        for (int i = 0; i < iterations && kicked == false; i++){
            test = test.moveRight();
            kicked = moveIfNoConflict(test);
        }
        return kicked;
    }
    
    private boolean doLeftKick(FallingPiece test, int iterations){
        boolean kicked = false;
        for(int i = 0; i < iterations && kicked == false; i++){
            test = test.moveLeft();
            kicked = moveIfNoConflict(test);
        }
        return kicked;
    }
    
    public void rotatePieceRight(){
        if (hasFalling()){
            FallingPiece test =falling.rotateRight();
            if (!moveIfNoConflict(test))
                Kick(test);
        }
    }
    
    public void rotatePieceLeft(){
        if (hasFalling()){
            FallingPiece test = falling.rotateLeft();
            if (!moveIfNoConflict(test))
                Kick(test);
        }
    }
    
    private boolean conflictsWithBoard(FallingPiece piece) {
        return outsideBoard(piece) || hitsAnotherBlock(piece);
    }
    
    private boolean outsideBoard(FallingPiece piece) {
        for (Point p : piece.allOuterPoints()) {
            if (outsideBoard(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean outsideBoard(Point p) {
        return p.Y() < 0
                || p.X() < 0
                || p.X() >= width;
    }
    
    private boolean hitsAnotherBlock(FallingPiece piece) {
         for (Point p : piece.allOuterPoints()) {
             if (hitsAnotherBlock(p))
                return true;
         }
         return false;
    }
    
    private boolean hitsAnotherBlock(Point piecePoint){
    	for (BlockDrawable block : tablero)
    		if (block.getPoint().X() == piecePoint.X() && block.getPoint().Y() == piecePoint.Y())
    			return true;
    	return false;
    }
 
    public float getHeight() {
        return height;
    }
    
     public float getWidth() {
        return width;
    }
        
    @Override
    public String cellAt(Point punto) {
        if (falling != null && falling.isAt(punto)) {
            return falling.cellAt(punto);
        } else {
            for (BlockDrawable block : tablero)
            	if (block.getPoint().X() == punto.X() && block.getPoint().Y() == punto.Y())
            		return block.getStyle();
        }
        return EMPTY;
    }

	public Array<BlockDrawable> getBlocksToDraw() {
		Array<BlockDrawable> blocksToDraw = new Array<BlockDrawable>();
		blocksToDraw.addAll(tablero);
		if (hasFalling())
			blocksToDraw.addAll(falling.allOuterBlocks());
		return blocksToDraw;
	}

	public void update(float delta) {
		if (!hasFalling())
			drop(bolsita.pullOut());
		if (timeForAutoFall()) {
			lastMove = 0;
			movePieceDown();
		}		
	}

	private boolean timeForAutoFall() {
		return TimeUtils.nanoTime() - lastAutoFall > autoFallRate;
	}
	
	private boolean moveTooFast(long tooFast) {
		return TimeUtils.nanoTime() - lastMove < tooFast;
	}
	
	private boolean moveTooFast() {
		return moveTooFast(moveRate);
	}
	
	public FallingPiece getFallingPiece() {
		return falling;
	}
	
	public String toString(){
		String points = "";		
		for(BlockDrawable block : getBlocksToDraw()){			
			points += "," + block.getPoint().toString();
		}
		points = points.replaceFirst(",", "");
		return points;
	}
}
