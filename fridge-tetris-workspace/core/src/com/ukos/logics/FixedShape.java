/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Ukita
 */
public class FixedShape implements RotatableGrid{

    private final Array<BlockDrawable> blocks;

    public FixedShape(Array<BlockDrawable> blocks) {
    	this.blocks = blocks;
    }

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
    	return new FixedShape(newBlocks);
    }

    @Override
    public FixedShape rotateLeft() {
    	Array<BlockDrawable> newBlocks = new Array<BlockDrawable>();
    	for (BlockDrawable block : blocks) {
    		float x = -block.getPoint().Y();
    		float y = block.getPoint().X();
    		newBlocks.add(new BlockDrawable(new Point(x,y), block.getStyle()));
		}
    	return new FixedShape(newBlocks);
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
}

