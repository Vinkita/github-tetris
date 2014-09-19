/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 * Un Array de BlockDrawables que constituyen una forma
 * @author Ukos
 */
public class FixedShape implements RotatableGrid, Cloneable{

    /**
     * Los bloques que constituyen la forma
     */
    private Array<BlockDrawable> blocks;
    /**
     * Código de textura
     */
    private final String textureKey;

    public FixedShape(Array<BlockDrawable> blocks, String textureKey) {
    	this.blocks = blocks;
    	this.textureKey = textureKey;
    }

    public FixedShape(Array<BlockDrawable> blocks) {
    	this.blocks = blocks;
    	textureKey = blocks.first().getStyle().substring(0, 0);
	}

    @Override
	public String cellAt(Point point) {
    	String style = EMPTY;
    	for (BlockDrawable block : blocks) {
    		if (block.getPoint().X() == point.X() && block.getPoint().Y() == point.Y())
    			style = block.getStyle();
    	}
    	return style;
    }

    @Override
    public FixedShape rotateRight() {
    	Array<BlockDrawable> newBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : blocks) {
    		float x = block.getPoint().Y();
    		float y = -block.getPoint().X();
    		newBlocks.add(new BlockDrawable(new Point(x,y), block.getStyle()));
		}
    	return new FixedShape(newBlocks, new String(textureKey));
    }

    @Override
    public FixedShape rotateLeft() {
    	Array<BlockDrawable> newBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : blocks) {
    		float x = -block.getPoint().Y();
    		float y = block.getPoint().X();
    		newBlocks.add(new BlockDrawable(new Point(x,y), block.getStyle()));
		}
    	return new FixedShape(newBlocks, new String(textureKey));
    }

	@Override
	public Array<Point> getPoints() {
		Array<Point> innerPoints = new Array<Point>();
		for (BlockDrawable block : blocks)
			innerPoints.add(block.getPoint());
		return innerPoints;
	}

	@Override
	public Array<BlockDrawable> allBlocks() {
		return blocks;
	}

	
	@Override
	public String toString(){
		String points = "";		
		for(Point point : getPoints()){
			points += "," + point.toString();
		}
		points = points.replaceFirst(",", "");
		return points;
	}

	/**
	 * @return el código de textura
	 */
	public String getTextureKey() {
		return textureKey;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		FixedShape aux = (FixedShape)super.clone();
		aux.blocks = new Array<BlockDrawable>();
		for(BlockDrawable block : blocks)
			aux.blocks.add((BlockDrawable) block.clone());
		return aux;
	}

}

