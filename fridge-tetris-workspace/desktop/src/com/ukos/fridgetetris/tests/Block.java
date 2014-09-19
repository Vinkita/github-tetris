package com.ukos.fridgetetris.tests;

import com.badlogic.gdx.utils.Array;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Point;
import com.ukos.logics.RotatableGrid;

/**
 * Crea una RotatableGrid de 1x1 bloque 
 * @author Ukos
 */
public class Block implements RotatableGrid{
    
    /**
     * El codigo de estilo del bloque
     */
    private final String style;
    
    public Block(String style) {
        this.style =style;
    }   
    
    public String style(){
        return style;
    }
    
    @Override
    public String cellAt(Point punto) {
        return style;
    }

    @Override
    public RotatableGrid rotateRight() {
        return this;
    }

    @Override
    public RotatableGrid rotateLeft() {
        return this;
    }

	@Override
	public Array<Point> getPoints() {
		return new Array<Point>(new Point[] {new Point(0, 0)});
	}

	@Override
	public Array<BlockDrawable> allBlocks() {
		return new Array<BlockDrawable>(new BlockDrawable[] { new BlockDrawable(new Point(0,0), style)});
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    
}
