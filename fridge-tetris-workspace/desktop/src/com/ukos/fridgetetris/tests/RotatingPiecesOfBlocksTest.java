// Copyright (c) 2008-2012  Esko Luontola <www.orfjackal.net>
// You may use and modify this source code freely for personal non-commercial use.
// This source code may NOT be used as course material without prior written agreement.

package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.FixedShape;

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


    public class A_piece_of_3x3_blocks {

        @Before
        public void createPiece() {
            piece = new FixedShape("" +
                    ".X.\n" +
                    ".X.\n" +
                    "...\n");
        }

        @Test
        public void consists_of_many_blocks() {
            assertEquals("" +
                    ".X.\n" +
                    ".X.\n" +
                    "...\n", piece.toString());
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
            assertEquals("" +
                    "...\n" +
                    ".XX\n" +
                    "...\n", piece.toString());
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
            assertEquals("" +
                    "...\n" +
                    "XX.\n" +
                    "...\n", piece.toString());
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
            piece = new FixedShape("" +
                    "..XXX\n" +
                    "..XX.\n" +
                    "..X..\n" +
                    ".....\n" +
                    ".....\n");
        }

        @Test
        public void consists_of_many_blocks() {
            assertEquals("" +
                    "..XXX\n" +
                    "..XX.\n" +
                    "..X..\n" +
                    ".....\n" +
                    ".....\n", piece.toString());
        }

        @Test
        public void can_be_rotated_right() {
            piece = piece.rotateRight();
            assertEquals("" +
                    ".....\n" +
                    ".....\n" +
                    "..XXX\n" +
                    "...XX\n" +
                    "....X\n", piece.toString());
        }

        @Test
        public void can_be_rotated_left() {
            piece = piece.rotateLeft();
            assertEquals("" +
                    "X....\n" +
                    "XX...\n" +
                    "XXX..\n" +
                    ".....\n" +
                    ".....\n", piece.toString());
        }
    }

}