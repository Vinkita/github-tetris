// Copyright (c) 2008-2012  Esko Luontola <www.orfjackal.net>
// You may use and modify this source code freely for personal non-commercial use.
// This source code may NOT be used as course material without prior written agreement.

package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

<<<<<<< HEAD
import com.ukos.logics.FixedShape;
=======
import com.badlogic.gdx.utils.Array;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.FixedShape;
import com.ukos.logics.Point;
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652

/**
 * @author Esko Luontola
 */
//@Ignore("contains no test") // TODO: uncomment me when you start doing these tests
@RunWith(NestedJUnit.class)
public class RotatingPiecesOfBlocksTest extends Assert {

    // Step 2: Stepping stone for rotation algorithms
    // - Remove the @Ignore annotation from this class
    // - See README for motivation
    // - Next step: RotatingTetrominoesTest


    private FixedShape piece;
<<<<<<< HEAD
=======
    private Array<BlockDrawable> blockArray;
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652


    public class A_piece_of_3x3_blocks {

<<<<<<< HEAD
        @Before
        public void createPiece() {
            piece = new FixedShape("" +
                    ".X.\n" +
                    ".X.\n" +
                    "...\n");
=======
        @Before        
        public void createPiece() {		
        	blockArray = new Array<BlockDrawable>(
		    				new BlockDrawable[] {
		    					new BlockDrawable(new Point(0,1), "X"),
		    					new BlockDrawable(new Point(0,0), "X")
		    					});
            piece = new FixedShape(blockArray);            
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void consists_of_many_blocks() {
<<<<<<< HEAD
            assertEquals("" +
                    ".X.\n" +
                    ".X.\n" +
                    "...\n", piece.toString());
=======
            assertEquals("[0:1], " +
            		"[0:0]", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
<<<<<<< HEAD
            assertEquals("" +
                    "...\n" +
                    ".XX\n" +
                    "...\n", piece.toString());
=======
//            assertEquals("" +
//                    "...\n" +
//                    ".XX\n" +
//                    "...\n", piece.toString());
            assertEquals("[1:0], " +
            		"[0:0]", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
<<<<<<< HEAD
            assertEquals("" +
                    "...\n" +
                    "XX.\n" +
                    "...\n", piece.toString());
=======
            assertEquals("[-1:0], " +
            		"[0:0]", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }
    }

   /* public class A_piece_of_4x4_blocks {

        @Before
        public void createPiece() {
            piece = new Piece("" +
                    ".XX.\n" +
                    ".XX.\n" +
                    "....\n" +
                    "....\n");
        }

        @Test
        public void consists_of_many_blocks() {
            assertEquals("" +
                    ".XX.\n" +
                    ".XX.\n" +
                    "....\n" +
                    "....\n", piece.toString());
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
            assertEquals("" +
                    "....\n" +
                    "..XX\n" +
                    "..XX\n" +
                    "....\n", piece.toString());
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
            assertEquals("" +
                    "....\n" +
                    "XX..\n" +
                    "XX..\n" +
                    "....\n", piece.toString());
        }
    } */



    public class A_piece_of_5x5_blocks {

        @Before
        public void createPiece() {
<<<<<<< HEAD
            piece = new FixedShape("" +
                    "..XXX\n" +
                    "..XX.\n" +
                    "..X..\n" +
                    ".....\n" +
                    ".....\n");
=======
        	blockArray = new Array<BlockDrawable>(
    				new BlockDrawable[] {
						new BlockDrawable(new Point(2,2), "X"),
						new BlockDrawable(new Point(1,2), "X"),
						new BlockDrawable(new Point(0,2), "X"),
						new BlockDrawable(new Point(1,1), "X"),
    					new BlockDrawable(new Point(0,1), "X"),
    					new BlockDrawable(new Point(0,0), "X")
    					});
//        	piece = new FixedShape("" +
//        			"..XXX\n" +
//        			"..XX.\n" +
//        			"..X..\n" +
//        			".....\n" +
//        			".....\n");
            piece = new FixedShape(blockArray);
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void consists_of_many_blocks() {
<<<<<<< HEAD
            assertEquals("" +
                    "..XXX\n" +
                    "..XX.\n" +
                    "..X..\n" +
                    ".....\n" +
                    ".....\n", piece.toString());
=======
            assertEquals("[2:2], " +
			"[1:2], " +
			"[0:2], " +
			"[1:1], " +
			"[0:1], " +
			"[0:0]", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
<<<<<<< HEAD
            assertEquals("" +
                    ".....\n" +
                    ".....\n" +
                    "..XXX\n" +
                    "...XX\n" +
                    "....X\n", piece.toString());
=======
            assertEquals("[2:-2], " +
        			"[2:-1], " +
        			"[2:0], " +
        			"[1:-1], " +
        			"[1:0], " +
        			"[0:0]", piece.toString());
//            assertEquals("" +
//                    ".....\n" +
//                    ".....\n" +
//                    "..XXX\n" +
//                    "...XX\n" +
//                    "....X\n", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
<<<<<<< HEAD
            assertEquals("" +
                    "X....\n" +
                    "XX...\n" +
                    "XXX..\n" +
                    ".....\n" +
                    ".....\n", piece.toString());
=======
            assertEquals("[-2:2], " +
        			"[-2:1], " +
        			"[-2:0], " +
        			"[-1:1], " +
        			"[-1:0], " +
        			"[0:0]", piece.toString());
//            assertEquals("" +
//                    "X....\n" +
//                    "XX...\n" +
//                    "XXX..\n" +
//                    ".....\n" +
//                    ".....\n", piece.toString());
>>>>>>> 31c1f77041f713b8a94c8312f6bc039e19091652
        }
    }

}
