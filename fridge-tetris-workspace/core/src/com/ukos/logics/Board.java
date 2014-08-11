package com.ukos.logics;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;

public class Board implements Grid{
    
    private FallingPiece falling;
    private FallingPiece ghost;
    private Array<BlockDrawable> tablero;
    private ArrayList<IRowListener> listeners = new ArrayList<IRowListener>();
    private ArrayList<IStopBlockListener> blockListeners = new ArrayList<IStopBlockListener>();
    private float width;
    private float height;
    private long autoFallRate = 500000000;
	private long lastAutoFall;
	private long moveRate     = 100000000;
	private long lastMove;
	private ShuffleBag bolsita = new ShuffleBag();
	private int removedRows = 0;
	private boolean ghostActivated = false;
	private boolean gameOver = false;
	
	//Experimental
	private ArrayMap<Integer, String[]> deletedRowsInfo = new ArrayMap<Integer, String[]>();

    
    public Board(float width, float height) {
        tablero = new Array<BlockDrawable>();
        this.width = width;
        this.height = height;
        deletedRowsInfo.ordered = true;
        reset();
    }
    
    public void reset(){
    	tablero.clear();
    	falling = null;
    	gameOver = false;
    }
    
    public void drop(RotatableGrid piece) {
    	if (!hasFalling()) {
	        Point centro = new Point(width / 2, height-1);
	        falling = new FallingPiece(piece).moveTo(centro);
	        if(conflictsWithBoard(falling)){
	        	gameOver = true;
	        	falling = null;
	        } else{
		        if(isGhostActivated())
		        	generateGhost();
	        }
	        triggerBlockListeners();
    	}
        
    }
    
    private void generateGhost() {
		try {
			ghost = (FallingPiece)falling.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		while(moveIfNoConflict(ghost.moveDown(), ghost));		
		for (BlockDrawable block : ghost.allBlocks())
			block.setGhost(true);
	}

	public void tick() {
    	 if (hasFalling()){
             if (!moveIfNoConflict(falling.moveDown(), falling))
            	 stopFallingBlock();
    	 }
    }
    
    public boolean hasFalling(){
        return falling != null;
    }

    public boolean isGhostActivated() {
		return ghostActivated;
	}

	public void setGhostActivated(boolean ghostActivated) {
		this.ghostActivated = ghostActivated;
	}
	
	public boolean isGameOver(){
		return gameOver;
	}

	private void stopFallingBlock() {
        assert hasFalling();
        copyToBoard(falling);
        if (isAboveLimit(falling)){
        	gameOver = true;
        	triggerBlockListeners();
        } else {        	
	        falling = null;
	        
	        removedRows = checkLines();
	        triggerListeners(removedRows);        
        }
    }
	
	private boolean isAboveLimit(FallingPiece piece){
		for (Point p : piece.allOuterPoints()) {
            if (p.Y() >= height) 
                return true;
        }
		return false;
	}

	private void copyToBoard(FallingPiece piece) {
    	tablero.reverse();
    	for (BlockDrawable block : piece.allBlocks()){
    		BlockDrawable aux = new BlockDrawable(piece.toOuterPoint(block.getPoint()), block.getStyle()); 
			tablero.add(aux);
		}
    	tablero.reverse();
    }
    
    private void deleteRow(float row){
        if(row >= 0){
        	for (Iterator<BlockDrawable> blocks = tablero.iterator(); blocks.hasNext();){
        		BlockDrawable block = blocks.next();
        		if (block.getPoint().Y() == row){
        			deletedRowsInfo.get((int)row)[(int)block.getPoint().X()] = block.getStyle();
        			blocks.remove();
        		}
        		else if (block.getPoint().Y() > row)
        			block.setPunto(block.getPoint().moveDown());
        	}
        }
    }
        
    private int checkLines(){
    	int contRows = 0;
    	
    	for (float y = height; y >= 0 && contRows < 4; y--) {
    		if (isFullRow(y)) {
    			deletedRowsInfo.insert(deletedRowsInfo.size, Integer.valueOf((int)y), new String[10]);
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
    
    private void triggerBlockListeners() {
    	for(IStopBlockListener listener : blockListeners){
    		listener.onStoppedBlock();
    	}
    }
    
    public void addRowListener(IRowListener listener){
        listeners.add(listener);
    }
    
    public void addBlockListener(IStopBlockListener listener){
    	blockListeners.add(listener);
    }
    
    public void movePieceToLeft() {
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moveIfNoConflict(falling.moveLeft(), falling);
        		if(isGhostActivated())
        			generateGhost();
            }
        }
    }
    
    /**

     * <b> Only for testing purposes! </b><br><br>

     *  Moves the falling piece to the left, if it can. 

     */

    public void testMovePieceToLeft() {
        if (hasFalling()){
        	moveIfNoConflict(falling.moveLeft(), falling);
        	if(isGhostActivated())
    			generateGhost();
        }
    }
    
    public void movePieceToRight(){
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moveIfNoConflict(falling.moveRight(), falling);
        		if(isGhostActivated())
        			generateGhost();
            }
        }
    }
    
    /**
    * <b> Only for testing purposes! </b><br><br>
    * Moves the falling piece to the right, if it can.
    */
        public void testMovePieceToRight() {
            if (hasFalling()){
             moveIfNoConflict(falling.moveRight(), falling);
             if(isGhostActivated())
     			generateGhost();
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
        		
        	}if (!moveIfNoConflict(falling.moveDown(), falling))
                stopFallingBlock();
        }
    }
    
    public boolean moveIfNoConflict(FallingPiece test, FallingPiece realPiece){
        if (!conflictsWithBoard(test)){
//        	realPiece = test;
            realPiece.setCoord(test.getCoord());
            realPiece.setInnerPiece(test.getInnerPiece());
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
            //TODO cambiar falling por parametro en kick
            kicked = moveIfNoConflict(test, falling);
        }
        return kicked;
    }
    
    private boolean doLeftKick(FallingPiece test, int iterations){
        boolean kicked = false;
        for(int i = 0; i < iterations && kicked == false; i++){
            test = test.moveLeft();
            //TODO cambiar falling por parametro en kick
            kicked = moveIfNoConflict(test, falling);
        }
        return kicked;
    }
    
    public void rotatePieceRight(){
        if (hasFalling()){
            FallingPiece test =falling.rotateRight();
            if (!moveIfNoConflict(test, falling))
                Kick(test);
            if(isGhostActivated())
    			generateGhost();
        }
    }
    
    public void rotatePieceLeft(){
        if (hasFalling()){
            FallingPiece test = falling.rotateLeft();
            if (!moveIfNoConflict(test, falling))
                Kick(test);
            if(isGhostActivated())
    			generateGhost();
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

    public Array<BlockDrawable> getAllBlocksToDraw() {
    	Array<BlockDrawable> blocksToDraw = new Array<BlockDrawable>();
		blocksToDraw.addAll(tablero);
		if(isGhostActivated()){
			blocksToDraw.addAll(getGhostBlocksToDraw());
		}
		if (hasFalling())
			blocksToDraw.addAll(falling.allOuterBlocks());
		
		return blocksToDraw;
    }
    
	public Array<BlockDrawable> getBoardBlocksToDraw() {
		Array<BlockDrawable> blocksToDraw = new Array<BlockDrawable>();
		blocksToDraw.addAll(tablero);
//		if (hasFalling())
//			blocksToDraw.addAll(falling.allOuterBlocks());
//		if(isGhostActivated()){
//			blocksToDraw.addAll(getGhostBlocksToDraw());
//		}
		
		return blocksToDraw;
	}
	
	public Array<BlockDrawable> getFallingBlocksToDraw() {
		if (hasFalling())
			return falling.allOuterBlocks();
		return new Array<BlockDrawable>();
	}
	
	
	public Array<BlockDrawable> getGhostBlocksToDraw() {
		Array<BlockDrawable> blocksToDraw = new Array<BlockDrawable>();		
		if(hasFalling()){
//			for(BlockDrawable block : ghost.allOuterBlocks())
//				if (!collidesWithPiece(block.getPoint(), falling))
//					blocksToDraw.add(block);
			return ghost.allOuterBlocks();
		}		
		return blocksToDraw;
	}
	
	private boolean collidesWithPiece(Point punto, FallingPiece piece){
		for	(Point aux : piece.allOuterPoints())
			if (punto.equals(aux))
				return true;		
		return false;
	}

	public void update(float delta) {
		if(!gameOver){
			if (!hasFalling())
				drop(bolsita.pullOut());
			if (timeForAutoFall()) {
				lastMove = 0;
				movePieceDown();
			}		
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
		for(BlockDrawable block : getBoardBlocksToDraw()){			
			points += "," + block.getPoint().toString();
		}
		if(isGhostActivated()){
			for(BlockDrawable block : getGhostBlocksToDraw()){			
				points += "," + block.getPoint().toString();
			}
		}
		points = points.replaceFirst(",", "");
		return points;
	}

	/**
	    * <b> Only for testing purposes! </b><br><br>
	    * Returns the number of rows removed after a piece drops
	    */
	public int getRemovedRows() {
		return removedRows;
	}
	
	public ArrayMap<Integer, String[]> getDeletedRows() {
		return deletedRowsInfo;
	}
	
	public Array<RotatablePiece> getPreviewPieces(int cant){
		return bolsita.preview(cant);
	}


//	public String getGhostString(){
//		String points = "";		
//		if(ghost != null){
//			for(BlockDrawable block : ghost.allOuterBlocks()){			
//				points += "," + block.getPoint().toString();
//			}
//			points = points.replaceFirst(",", "");
//		}
//		return points;
//	}
}
