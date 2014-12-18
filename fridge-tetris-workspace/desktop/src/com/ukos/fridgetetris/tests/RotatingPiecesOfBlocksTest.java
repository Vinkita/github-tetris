// Copyright (c) 2008-2012  Esko Luontola <www.orfjackal.net>
// You may use and modify this source code freely for personal non-commercial use.
// This source code may NOT be used as course material without prior written agreement.

package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.utils.Array;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.FixedShape;
import com.ukos.logics.Point;

/**
 * @author Esko Luontola
 */
//@Ignore("contains no test") 
@RunWith(NestedJUnit.class)
public class RotatingPiecesOfBlocksTest extends Assert {

    // Step 2: Stepping stone for rotation algorithms
    // - Remove the @Ignore annotation from this class
    // - See README for motivation
    // - Next step: RotatingTetrominoesTest


    private FixedShape piece;
    private Array<BlockDrawable> blockArray;


    public class A_piece_of_3x3_blocks {

        @Before        
        public void createPiece() {		
        	blockArray = new Array<BlockDrawable>(
		    				new BlockDrawable[] {
		    					new BlockDrawable(new Point(0,1), "X"),
		    					new BlockDrawable(new Point(0,0), "X")
		    					});
            piece = new FixedShape(blockArray);            
        }

        @Test
        public void consists_of_many_blocks() {
            assertEquals("[0:1]," +
            		"[0:0]", piece.toString());
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
            assertEquals("[1:0]," +
            		"[0:0]", piece.toString());
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
            assertEquals("[-1:0]," +
            		"[0:0]", piece.toString());
        }
    }
    
    public class A_piece_of_5x5_blocks {

        @Before
        public void createPiece() {
        	blockArray = new Array<BlockDrawable>(
    				new BlockDrawable[] {
						new BlockDrawable(new Point(2,2), "X"),
						new BlockDrawable(new Point(1,2), "X"),
						new BlockDrawable(new Point(0,2), "X"),
						new BlockDrawable(new Point(1,1), "X"),
    					new BlockDrawable(new Point(0,1), "X"),
    					new BlockDrawable(new Point(0,0), "X")
    					});
            piece = new FixedShape(blockArray);
        }

        @Test
        public void consists_of_many_blocks() {
            assertEquals("[2:2]," +
			"[1:2]," +
			"[0:2]," +
			"[1:1]," +
			"[0:1]," +
			"[0:0]", piece.toString());
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
            assertEquals("[2:-2]," +
        			"[2:-1]," +
        			"[2:0]," +
        			"[1:-1]," +
        			"[1:0]," +
        			"[0:0]", piece.toString());
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
            assertEquals("[-2:2]," +
        			"[-2:1]," +
        			"[-2:0]," +
        			"[-1:1]," +
        			"[-1:0]," +
        			"[0:0]", piece.toString());
        }
    }

}
