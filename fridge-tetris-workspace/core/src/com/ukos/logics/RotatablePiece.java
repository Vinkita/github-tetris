
package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 * Contiene un Array de FixedShape utilizado para simular rotaciones
 * @author Ukita
 */
public class RotatablePiece implements RotatableGrid{

    /**
     * Contiene instancias de FixedShape que representan las rotaciones de this
     */
    private Array<FixedShape> rotations = new Array<FixedShape>();
    /**
     * El indice de la rotacion
     */
    private final int current;
    
    /**
     * Crea una RotatablePiece a partir de un Array de BlockDrawable
     * 
     * @param maxRots  el numero máximo de rotaciones que puede realizar la pieza
     * @param curRotation  el indice de la rotacion actual
     * @param shapeBlocks los BlockDrawable que conformaran la primer pieza 
     * @param textureKey el codigo de textura 
     */
    public RotatablePiece(int maxRots, int curRotation, Array<BlockDrawable> shapeBlocks, String textureKey) {
        this(maxRots, curRotation);
        FixedShape piece = firstRotation(new FixedShape(shapeBlocks, textureKey), curRotation);
        this.rotations = fillRotations(piece, maxRots, textureKey);
    }
    
    /**
     * Crea una RotatablePiece a partir de un Array de FixedShape, con la rotacion actual especificada
     * @param curRotation  el indice de la rotacion actual
     * @param rotations  el arreglo de FixedShape
     */
    private RotatablePiece(int curRotation, Array<FixedShape> rotations) {
        this(rotations.size, curRotation);
        this.rotations = rotations;
    }
    
    private RotatablePiece(int maxRots, int curRotation) {
        if(curRotation >= maxRots)
            curRotation = 0;
        else if (curRotation < 0)
            curRotation = maxRots - 1;
        this.current = curRotation;
    }
    
    private FixedShape firstRotation(FixedShape piece, int curRotation){
        for(int i = 0; i < curRotation; i++){
            piece = piece.rotateRight();
        }
        return piece;
    }
    
    /**
     * Crea un Array y lo llena con rotaciones de una FixedShape
     * @param piece  la FixedShape inicial
     * @param maxRots  el numero de rotciones que contendra el arreglo
     * @param textureKey el codigo de textura inicial, a partir del cual se asignaran los codigos de cada bloque individual
     * @return un Array que representa las rotaciones de la RotatablePiece
     */
    private Array<FixedShape> fillRotations(FixedShape piece, int maxRots, String textureKey){
        Array<FixedShape> arr = new Array<FixedShape>(maxRots);
        arr.add(piece);
        for(int i = 1; i < maxRots; i++){
        	String[] textureNames = Tetromino.colors.get(textureKey+i);
        	FixedShape fs = arr.get(i-1).rotateRight();
        	Array<BlockDrawable> blocks = fs.allBlocks();
        	for(int j = 0, z = blocks.size; j < z; j++){
        		BlockDrawable block = blocks.get(j);
        		block.setStyle(textureNames[j]);
        	}
        	arr.add(fs);
        }
        return arr;
    }
    
    @Override
    public RotatablePiece rotateRight() {
        return new RotatablePiece(current + 1, rotations);
    }

    @Override
    public RotatablePiece rotateLeft() {
        return new RotatablePiece(current - 1, rotations);
    }

    @Override
    public String cellAt(Point punto) {
        return getCurrentShape().cellAt(punto);
    }
    
    @Override
    public String toString(){
        return getCurrentShape().toString();
    }
    
    @Override
    public boolean equals(Object o){
    	if(o != null)
    		return this.toString().equals(o.toString());
    	return false;
    }
    
    private FixedShape getCurrentShape(){
        return rotations.get(current);
    }
    
    public Array<Point> getPoints(){
    	Array<Point> auxPoints = new Array<Point>();
    	for (BlockDrawable block : allBlocks()) {
    		auxPoints.add(block.getPoint());
    	}
    	return auxPoints;
    }

	@Override
	public Array<BlockDrawable> allBlocks() {
		return getCurrentShape().allBlocks();
	}
	
	/**
	 * @return  todos los bloques de todas las rotaciones de la pieza.
	 */
	public Array<BlockDrawable> allShapesBlocks(){
		Array<BlockDrawable> arraux = new Array<BlockDrawable>();
		for(FixedShape shape : rotations)
			arraux.addAll(shape.allBlocks());
		return arraux;
	}

	/**
	 * @return  el código de textura de la pieza actual
	 */
	public String getTextureKey() {
		return getCurrentShape().getTextureKey();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		RotatablePiece aux = (RotatablePiece) super.clone();
		aux.rotations = new Array<FixedShape>();
		for (FixedShape shape : rotations)
			aux.rotations.add((FixedShape) shape.clone());
		return aux;
	}
}
