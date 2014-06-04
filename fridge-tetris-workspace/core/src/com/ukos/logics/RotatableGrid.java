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
public interface RotatableGrid extends Grid{
    
    RotatableGrid rotateRight();
    
    RotatableGrid rotateLeft();
    
    Array<Point> getPoints();
    
    Array<BlockDrawable> allBlocks();
}
