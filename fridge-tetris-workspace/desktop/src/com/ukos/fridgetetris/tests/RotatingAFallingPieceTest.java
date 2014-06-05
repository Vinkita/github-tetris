// Copyright (c) 2008-2012  Esko Luontola <www.orfjackal.net>
// You may use and modify this source code freely for personal non-commercial use.
// This source code may NOT be used as course material without prior written agreement.

package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.Board;
import com.ukos.logics.FixedShape;
import com.ukos.logics.Tetromino;

@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class RotatingAFallingPieceTest extends Assert {

    // Step 6: Training wheels off
    // - Remove the @Ignore annotation from this class
    // - You're now responsible for covering all corner cases
    // - Next step: see the README for details

    // TODO: a falling piece can be rotated clockwise
    // TODO: a falling piece can be rotated counter-clockwise
    // TODO: it can not be rotated when there is no room to rotate (left wall, right wall, other pieces...)

    // TODO: when piece is up against a wall (or piece) and it is rotated (no room to rotate), move it away from the wall ("wallkick")
    // See: http://bsixcentdouze.free.fr/tc/tgm-en/tgm.html
    // http://bsixcentdouze.free.fr/tc/tgm-en/img/wallkick1.png
    // http://bsixcentdouze.free.fr/tc/tgm-en/img/wallkick2.png
    // http://bsixcentdouze.free.fr/tc/tgm-en/img/wallkick3.png
    

    private Board board = new Board(6, 10);
    private final int A_FUCK_LOAD = 11;
    private final FixedShape PIEZA = new FixedShape("" +
            "..I..\n" +
            "..I..\n" +
            "..I..\n" +
            "..I..\n" +
            ".....\n");
    
    public class Rotations{
        
        @Before
        public void dropPiece(){
            board.drop(Tetromino.T_SHAPE);
        }
        
        @Test
        public void rotates_right(){
            board.rotatePieceRight();
            assertEquals("" +
                    ".....T....\n" +
                    ".....TT...\n" +                    
                    ".....T....\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void rotates_left(){
            board.rotatePieceLeft();
            assertEquals("" +
                    ".....T....\n" +
                    "....TT....\n" +                    
                    ".....T....\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
    }
    
    public class Rotations_against_the_wall{
        
        @Before
        public void dropPiece(){
            board.drop(PIEZA);
            assertEquals("" +
                    ".....I....\n" +
                    ".....I....\n" +                    
                    ".....I....\n" +
                    ".....I....\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void does_right_kick(){
            for (int i = 0; i < A_FUCK_LOAD; i++)
                board.movePieceToRight();
            board.rotatePieceRight();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "......IIII\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_left_kick(){
            for (int i = 0; i < A_FUCK_LOAD; i++){
                board.movePieceToLeft();                
            }
            board.rotatePieceLeft();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "IIII......\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
            assertTrue(board.hasFalling());
        }
    }
    
    
    public class Rotations_against_a_piece{
        @Before
        public void drop_Piece(){
            board = new Board("" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n");
            board.drop(PIEZA);
        }
        
        @Test
        public void does_right_kick_against_piece(){
            for(int i = 0; i < A_FUCK_LOAD; i++){
                board.movePieceToRight();
            }
            board.rotatePieceLeft();
            assertEquals("" +
                    "X........X\n" +
                    "X........X\n" +
                    "X....IIIIX\n" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_left_kick_against_piece(){
            for(int i = 0; i < A_FUCK_LOAD; i++){
                board.movePieceToLeft();
            }
            board.rotatePieceLeft();
            assertEquals("" +
                    "X........X\n" +
                    "X........X\n" +
                    "XIIII....X\n" +
                    "X........X\n" +
                    "X........X\n" +
                    "X........X\n", board.toString());
            assertTrue(board.hasFalling());
        }
        
        
    }
    
    public class Piece_in_a_narrow_space{
        @Before
        public void dropPiece(){
            board = new Board("" +
                    "...X..X...\n" +
                    "...X..X...\n" +
                    "...X..X...\n" +
                    "...X..X...\n" +
                    "...X..X...\n" +
                    "...X..X...\n");
                    
            board.drop(PIEZA);
            assertEquals("" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X..X...\n" +
                    "...X..X...\n", board.toString());
        }
        
        @Test
        public void does_not_rotate_right(){          
            
            
            board.rotatePieceRight();
            assertEquals("" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X..X...\n" +
                    "...X..X...\n", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_not_rotate_left(){
            board.rotatePieceLeft();
            assertEquals("" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X.IX...\n" +
                    "...X..X...\n" +
                    "...X..X...\n", board.toString());
            assertTrue(board.hasFalling());
        }
    }
    
    
}
