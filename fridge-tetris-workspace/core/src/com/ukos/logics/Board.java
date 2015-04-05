package com.ukos.logics;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Clase que contiene la logica del juego.
 * <br>Esto incluye:
 * <li>Dimensiones del tablero.
 * <li>Restricciones al movimiento de las piezas.
 * <li>Controles de movimiento.
 * 
 * @author Ukos
 *
 */
public class Board implements Grid{
    
    /**
     * La pieza cayendo
     */
    private FallingPiece falling;
    /**
     * La pieza fantasma, muestra donde se posará la pieza cayendo
     */
    private FallingPiece ghost;
    /**
     * Contiene todas los bloques de las piezas que ya se han fijado al tablero
     */
    private Array<BlockDrawable> tablero;
    private ArrayList<IRowListener> listeners = new ArrayList<IRowListener>();
    private ArrayList<IStopBlockListener> blockListeners = new ArrayList<IStopBlockListener>();
    /**
     * El ancho del tablero
     */
    private float width;
    /**
     * El alto del tablero
     */
    private float height;
    /**
     * La tasa de caida de la pieza
     */
    private long autoFallRate;// = 500000000;
	private long lastAutoFall;
	/**
	 * La tasa de movimiento manual de la pieza
	 */
	private long moveRate;//     = 100000000;
	private long lastMove;
	private ShuffleBag bolsita = new ShuffleBag();
	private int removedRows = 0;
	private int totalRows = 0;
	private boolean ghostActivated = false;
	private boolean gameOver = false;
	private int level = 0;
	
	/**
	 * Cuando se remueve una (o varias) fila, la informacion de estilo de sus bloques es guardada aquí.
	 * Luego, el BoardRenderer utiliza esta informacion para crear el efecto de explosión de las filas. 
	 */
	private ArrayMap<Integer, TextureRegion[]> deletedRowsInfo = new ArrayMap<Integer, TextureRegion[]>();

    
    /** Crea un nuevo Board, con el ancho y la altura especificados
     * @param width  el ancho del tablero
     * @param height  la altura del tablero
     */
    public Board(float width, float height) {
        tablero = new Array<BlockDrawable>();
        this.width = width;
        this.height = height;
        deletedRowsInfo.ordered = true;
        reset();
    }
    
    /**
     * Carga el estado inicial del juego.
     */
    public void reset(){
    	tablero.clear();
    	falling = null;
    	gameOver = false;
    	level = 0;
    	totalRows = 0;
    	LevelHelper.setLevelSpeed(level, this);
    }
    
    /**
     * Crea una nueva FallingPiece a partir de la RotatableGrid y la situa al tope del tablero.
     * @param piece  sera contenida por la nueva FallingPiece
     */
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
    
    /**
     * Genera la pieza fantasma y la coloca en posicion.
     * Setea la propiedad "ghost" de todos sus bloques a true
     */
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

	/**
	 * Si hay una pieza cayendo, la mueve una unidad hacia abajo o la detiene segun corresponda.
	 * Este metodo es utilizado en los test unitarios.
	 */
	public void tick() {
    	 if (hasFalling()){
             if (!moveIfNoConflict(falling.moveDown(), falling))
            	 stopFallingBlock();
    	 }
    }
    
    /**
     * @return  si el tablero contiene una pieza cayendo o no
     */
    public boolean hasFalling(){
        return falling != null;
    }

    /**
     * @return  si la pieza fantasma está activada o no
     */
    public boolean isGhostActivated() {
		return ghostActivated;
	}

	/**
	 * Cambia el estado de la pieza fantasma (activada/desactivada)
	 * @param ghostActivated  el nuevo estado de la pieza;
	 */
	public void setGhostActivated(boolean ghostActivated) {
		this.ghostActivated = ghostActivated;
	}
	
	/**
	 * @return  true si el juego ha terminado, false si no
	 */
	public boolean isGameOver(){
		return gameOver;
	}

	/**
	 * Detiene la pieza cayendo y fija sus bloques al tablero.
	 * <br>En este momento se chequea si el juego debe terminar. También se comprueban 
	 * las filas removidas y si ya es momento de cambiar de nivel, 
	 * notificando la los listeners correspondientes.
	 */
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
	        totalRows += removedRows;
	        checkLevel();
        }
    }
	
	/**
	 * Setea el nivel correspondiente según la 
	 * cantidad de filas removidas hasta el momento.
	 */
	private void checkLevel(){
		int aux = (int) Math.floor(totalRows / 10);
		if(aux > level){
			level = aux;
			LevelHelper.setLevelSpeed(level, this);
		}
	}
	
	/**
	 * Comprueba si la pieza cayendo se encuentra por encima del alto del tablero
	 * @param piece  la pieza cayendo
	 * @return  true, si algun bloque de la pieza se encuentra por encima del limite
	 */
	private boolean isAboveLimit(FallingPiece piece){
		for (Point p : piece.allOuterPoints()) {
            if (p.Y() >= height) 
                return true;
        }
		return false;
	}

	/**
	 * Fija una FallingPiece al tablero, agregando todos sus bloques al tablero.
	 * @param piece  la pieza a fijar.
	 */
	private void copyToBoard(FallingPiece piece) {
    	tablero.reverse();
    	for (BlockDrawable block : piece.allBlocks()){
    		BlockDrawable aux = new BlockDrawable(piece.toOuterPoint(block.getPoint()), block.getStyle(), false, block.getTextureRegion()); 
			tablero.add(aux);
		}
    	tablero.reverse();
    }
    
    /**
     * Borra una fila del tablero y mueve hacia abajo todas las filas por encima de ésta. 
     * <br>Además inserta la informacion de estilo de los bloques removidos en <code>deletedRowsInfo</code>
     * @param row  la fila a remover
     */
    private void deleteRow(float row){
        if(row >= 0){
        	deletedRowsInfo.insert(deletedRowsInfo.size, Integer.valueOf((int)row), new TextureRegion[10]);
        	for (Iterator<BlockDrawable> blocks = tablero.iterator(); blocks.hasNext();){
        		BlockDrawable block = blocks.next();
        		if (block.getPoint().Y() == row){
        			deletedRowsInfo.get((int)row)[(int)block.getPoint().X()] = block.getTextureRegion();
        			blocks.remove();
        		}
        		else if (block.getPoint().Y() > row)
        			block.setPunto(block.getPoint().moveDown());
        	}
        }
    }
        
    /**
     * Busca y remueve filas completas.
     * @return la cantidad de filas removidas
     */
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
    
    /**
     * Chequea si una fila está completa.
     * @param row  el numero de fila.
     * @return  true si la fila esta completa, false si no.
     */
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
    
    /**
     * Notifica a todos sus IRowListener cuando se remueven filas
     * @param removedRows  el numero de filas removidas.
     */
    private void triggerListeners(int removedRows) {
        for(IRowListener listener : listeners){
            listener.onRowsRemoved(removedRows, level);
        }
    }
    
    /**
     * Notifica a todos sus IStopBlockListener cuando se detiene la pieza cayendo.
     */
    private void triggerBlockListeners() {
    	for(IStopBlockListener listener : blockListeners){
    		listener.onStoppedBlock();
    	}
    }
    
    /**
     * Añade un nuevo IRowListener
     * @param listener
     */
    public void addRowListener(IRowListener listener){
        listeners.add(listener);
    }
    
    /**
     * Añade un nuevo IStopBlockListener
     * @param listener
     */
    public void addBlockListener(IStopBlockListener listener){
    	blockListeners.add(listener);
    }
    
    /**
     * Mueve hacia la izquierda a la pieza cayendo, si es que se cumplen todas las condiciones:
     * <p>
     * <li>La pieza debe existir.
     * <li>No debe moverse demasiado rápido.
     * <li>No debe conflictuar con el tablero o con otras piezas.
     */
    public boolean movePieceToLeft() {
    	boolean moved = false;
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moved = moveIfNoConflict(falling.moveLeft(), falling);
        		if(isGhostActivated())
        			generateGhost();
            }
        }
        return moved;
    }
    
    /**
     * <b> Sólo para testeo! </b><br><br>
     *  Mueve la pieza cayendo hacia la izquierda, si es que puede ser movida.
     */

    public void testMovePieceToLeft() {
        if (hasFalling()){
        	moveIfNoConflict(falling.moveLeft(), falling);
        	if(isGhostActivated())
    			generateGhost();
        }
    }
    
    /**
     * Mueve hacia la derecha a la pieza cayendo, si es que se cumplen todas las condiciones:
     * <p>
     * <li>La pieza debe existir.
     * <li>No debe moverse demasiado rápido.
     * <li>No debe conflictuar con el tablero o con otras piezas.
     */
    public boolean movePieceToRight(){
    	boolean moved = false;
        if (hasFalling()){
        	if(!moveTooFast()){
        		lastMove = TimeUtils.nanoTime();
        		moved = moveIfNoConflict(falling.moveRight(), falling);
        		if(isGhostActivated())
        			generateGhost();
            }
        }
        return moved;
    }
    
    /**
     * <b> Sólo para testeo! </b><br><br>
     *  Mueve la pieza cayendo hacia la derecha, si es que puede ser movida.
     */
        public void testMovePieceToRight() {
            if (hasFalling()){
             moveIfNoConflict(falling.moveRight(), falling);
             if(isGhostActivated())
     			generateGhost();
            }
        }
    
    /**
     * Mueve hacia la derecha a la pieza cayendo, si es que se cumplen todas las condiciones:
     * <p>
     * <li>La pieza debe existir.
     * <li>No debe moverse demasiado rápido.
     * <li>No debe conflictuar con el tablero o con otras piezas.
     * <li>Si la pieza presenta conflicto, es fijada al tablero.
     */
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
    
    /**
     * Si la pieza test no conflictua con el tablero, la pieza realPiece es movida y/o rotada 
     * a la posicion de la pieza test.
     * @param test
     * @param realPiece
     * @return  true, si realPiece pudo ser movida/rotada.
     */
    public boolean moveIfNoConflict(FallingPiece test, FallingPiece realPiece){
        if (!conflictsWithBoard(test)){
//        	realPiece = test;
            realPiece.setCoord(test.getCoord());
            realPiece.setInnerPiece(test.getInnerPiece());
            return true;
        }
        return false;
    }
    
    /**
     * Intenta "patear" la pieza test en ambas direcciones (sucesivamente) para luego mover realPiece a la posicion de test.
     * @param test
     * @param realPiece  la pieza real, que tomará el lugar de la pieza test si la "patada" fue exitosa.     
     */
    private void Kick(FallingPiece test, FallingPiece realPiece){
        int wide = 2;
        
        if(!doRightKick(test, realPiece, wide)){
            doLeftKick(test, realPiece, wide);
        }
    }
    
    /**
     * Intenta "patear" la pieza test hacia la derecha y luego mover realPiece a la posicion de test.
     * @param test
     * @param realPiece  la pieza real, que tomará el lugar de la pieza test si la "patada" fue exitosa. 
     * @param iterations  el numero maximo de movimientos hacia la derecha que podrán ser intentados. 
     * @return  true, si se pudo patear la pieza.
     */
    private boolean doRightKick(FallingPiece test, FallingPiece realPiece, int iterations){
        boolean kicked = false;
        for (int i = 0; i < iterations && kicked == false; i++){
            test = test.moveRight();
            kicked = moveIfNoConflict(test, falling);
        }
        return kicked;
    }
    
    /**
     * Intenta "patear" la pieza test hacia la izquierda y luego mover realPiece a la posicion de test.
     * @param test
     * @param realPiece  la pieza real, que tomará el lugar de la pieza test si la "patada" fue exitosa. 
     * @param iterations  el numero maximo de movimientos hacia la derecha que podrán ser intentados. 
     * @return  true, si se pudo patear la pieza.
     */
    private boolean doLeftKick(FallingPiece test, FallingPiece realPiece, int iterations){
        boolean kicked = false;
        for(int i = 0; i < iterations && kicked == false; i++){
            test = test.moveLeft();
            kicked = moveIfNoConflict(test, falling);
        }
        return kicked;
    }
    
    /**
     * Intenta rotar la pieza cayendo hacia la derecha.
     * <br>En caso de que no pueda ser rotada, intenta "patearla" horizontalmente
     */
    public void rotatePieceRight(){
        if (hasFalling()){
            FallingPiece test =falling.rotateRight();
            if (!moveIfNoConflict(test, falling))
                Kick(test, falling);
            if(isGhostActivated())
    			generateGhost();
        }
    }
    
    /**
     * Intenta rotar la pieza cayendo hacia la izquierda.
     * <br>En caso de que no pueda ser rotada, intenta "patearla" horizontalmente
     */
    public void rotatePieceLeft(){
        if (hasFalling()){
            FallingPiece test = falling.rotateLeft();
            if (!moveIfNoConflict(test, falling))
                Kick(test, falling);
            if(isGhostActivated())
    			generateGhost();
        }
    }
    
    /**
     * Comprueba si una pieza conflictúa con los límites del tablero, o con otras piezas.  
     * @param piece
     * @return true, si la pieza conflictua con el tablero.
     */
    private boolean conflictsWithBoard(FallingPiece piece) {
        return outsideBoard(piece) || hitsAnotherBlock(piece);
    }
    
    /**
     * Comprueba si una pieza conflictúa con los límites del tablero.
     * @param piece
     * @return true, si la pieza conflictúa con los limites del tablero.
     */
    private boolean outsideBoard(FallingPiece piece) {
        for (Point p : piece.allOuterPoints()) {
            if (outsideBoard(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cmprueba si un Punto se encuentra fuera de los límites del tablero.
     * @param p
     * @return true, si p se encuentra fuera del tablero.
     */
    private boolean outsideBoard(Point p) {
        return p.Y() < 0
                || p.X() < 0
                || p.X() >= width;
    }
    
    /**
     * Comprueba si una pieza conflictúa con las piezas ya fijadas al tablero.
     * @param piece
     * @return  true, si la pieza conflictúa con otras piezas.
     */
    private boolean hitsAnotherBlock(FallingPiece piece) {
         for (Point p : piece.allOuterPoints()) {
             if (hitsAnotherBlock(p))
                return true;
         }
         return false;
    }
    
    /**
     * Comprueba si un Punto conflictúa con las piezas ya fijadas al tablero.
     * @param piecePoint
     * @return  true, si piecePoint conflictúa con alguna piezas.
     */
    private boolean hitsAnotherBlock(Point piecePoint){
    	for (BlockDrawable block : tablero)
    		if (block.getPoint().X() == piecePoint.X() && block.getPoint().Y() == piecePoint.Y())
    			return true;
    	return false;
    }
 
    /**
     * @return  la altura del tablero.
     */
    public float getHeight() {
        return height;
    }
    
    /**
     * @return  el ancho del tablero.
     */
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

    /**
     * Crea y devuelve un Array que contiene todos los BlockDrawable que deben ser dibujados.
     * <br>Esto incluye:
     * <p>
     * <li>Los bloques fijados al tablero.
     * <li>Los bloques de la pieza fantasma, si es que esta activada.
     * <li>Los bloques de la pieza cayendo, si es que existe.
     */
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
    
    /**
     * Crea y devuelve un Array que contiene todos los BlockDrawable ya fijados al tablero.
     */
	public Array<BlockDrawable> getBoardBlocksToDraw() {
		Array<BlockDrawable> blocksToDraw = new Array<BlockDrawable>();
		blocksToDraw.addAll(tablero);		
		return blocksToDraw;
	}
	
	/**
     * Crea y devuelve un Array que contiene todos los BlockDrawable de la pieza cayendo.
     */
	public Array<BlockDrawable> getFallingBlocksToDraw() {
		if (hasFalling())
			return falling.allOuterBlocks();
		return new Array<BlockDrawable>();
	}
	
	/**
     * Crea y devuelve un Array que contiene todos los BlockDrawable de la pieza fantasma.
     */
	public Array<BlockDrawable> getGhostBlocksToDraw() {		
		if(hasFalling()){
			return ghost.allOuterBlocks();
		}		
		return new Array<BlockDrawable>();
	}

	/**
	 * Ejecucion del comportamiento lógico del tablero.
	 * @param delta
	 */
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

	/**
	 * @return true, si ya ha pasado el tiempo requerido para que la 
	 * pieza cayendo sea movida hacia abajo.
	 */
	private boolean timeForAutoFall() {
		return TimeUtils.nanoTime() - lastAutoFall > autoFallRate;
	}
	
	/**
	 * Calcula si el movimento manual de la pieza es demasiado rápido.
	 * @param tooFast  la cantidad mínima de nanosegundos que deben 
	 * haber transcurrido desde el último movimiento.
	 * @return 
	 */
	private boolean moveTooFast(long tooFast) {
		return TimeUtils.nanoTime() - lastMove < tooFast;
	}
	
	/**
	 * Calcula si el movimento manual de la pieza es demasiado rápido.
	 * @return 
	 */
	private boolean moveTooFast() {
		return moveTooFast(moveRate);
	}
	
	/**
	 * @return la pieza cayendo.
	 */
	public FallingPiece getFallingPiece() {
		return falling;
	}
	
	public String toString(){
		String points = "";		
		for(BlockDrawable block : getAllBlocksToDraw()){			
			points += "," + block.getPoint().toString();
		}
		points = points.replaceFirst(",", "");
		return points;
	}

	/**
	 * <b> Sólo para testeo! </b><br><br>
	 * @return  el número de filas que fueron removidas cuando la pieza cayendo es fijada al tablero. 
	 */
	public int getRemovedRows() {
		return removedRows;
	}

	/**
	 * @return  el Array que contiene la informacion de las filas removidas.
	 */
	public ArrayMap<Integer, TextureRegion[]> getDeletedRows() {
		return deletedRowsInfo;
	}
	
	/**
	 * Devuelve, en orden y sin quitarlas de la ShuffleBag, las siguientes piezas que caeran.
	 * @param cant  La cantidad requerida de piezas.
	 * @return Un arreglo que contiene las proximas piezas.
	 */
	public Array<RotatablePiece> getPreviewPieces(int cant){
		return bolsita.preview(cant);
	}

	/**
	 * @return  el nivel actual del juego.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return  la tasa de caida de la pieza.
	 */
	protected long getAutoFallRate() {
		return autoFallRate;
	}

	/**
	 * Setea la tasa de caida de la pieza.
	 * @param autoFallRate  la nueva tasa de caida.
	 */
	protected void setAutoFallRate(long autoFallRate) {
		this.autoFallRate = autoFallRate;
	}

	/**
	 * @return  la tasa de movimiento de la pieza.
	 */
	protected long getMoveRate() {
		return moveRate;
	}

	/**
	 * Setea la tasa de movimiento de la pieza.
	 * @param moveRate  la nueva tasa de movimiento.
	 */
	protected void setMoveRate(long moveRate) {
		this.moveRate = moveRate;
	}

	public void hardDrop() {
		while(moveIfNoConflict(falling.moveDown(), falling));		
	}

	
}
