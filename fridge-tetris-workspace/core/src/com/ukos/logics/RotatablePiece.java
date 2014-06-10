
package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Ukita
 */
public class RotatablePiece implements RotatableGrid{

    private Array<FixedShape> rotations = new Array<FixedShape>();
    private final int current;
    
    public RotatablePiece(int maxRots, int curRotation, Array<BlockDrawable> shapeBlocks, String textureKey) {
        this(maxRots, curRotation);
        FixedShape piece = firstRotation(new FixedShape(shapeBlocks), curRotation);
        this.rotations = fillRotations(piece, maxRots, textureKey);
    }
    
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
        return this.toString().equals(o.toString());
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
}
