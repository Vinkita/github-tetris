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
import com.ukos.logics.Tetromino;

@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class MovingAFallingPieceTest extends Assert {

    // Step 5: It's your turn now
    // - Remove the @Ignore annotation from this class
    // - The test names have been provided, you just need to fill in the test body
    // - Next step: RotatingAFallingPieceTest

    // TODO: a falling piece can be moved left
    // TODO: a falling piece can be moved right
    // TODO: a falling piece can be moved down
    // TODO: it will not move left over the board
    // TODO: it will not move right over the board
    // TODO: it will not move down over the board (will stop falling)
    // TODO: it can not be moved left if another piece is in the way
    // TODO: it can not be moved right if another piece is in the way
    // TODO: it can not be moved down if another piece is in the way (will stop falling)
    // TODO: it can not be moved if there is no piece falling

    // P.S. Take into consideration, that part of the piece's area may be empty cells.
    // Only non-empty cells should take part in the collision checks.
    
    private final Board board = new Board(6, 10);
    
    public class Pieces_can_be_moved {

        @Before
        public void dropPiece() {
            board.drop(Tetromino.T_SHAPE);
        }

        @Test
        public void it_starts_from_top_middle() {
            assertEquals("" +
                    ".....T....\n" +
                    "....TTT...\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        
        @Test
        public void it_can_be_moved_left() {
            board.movePieceToLeft();
            assertEquals("" +
                    "....T.....\n" +
                    "...TTT....\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void it_can_be_moved_right() {
            board.movePieceToRight();
            assertEquals("" +
                    "......T...\n" +
                    ".....TTT..\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void it_can_be_moved_down(){
            board.movePieceDown();
            assertEquals("" +
                    "..........\n" +
                    ".....T....\n" +
                    "....TTT...\n" +                    
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
    }
    
    public class Pieces_can_not_be_moved_over_the_board {
        
        @Before
        public void dropPiece() {
            board.drop(Tetromino.T_SHAPE);
        }
        
        @Test
        public void it_can_not_be_moved_left_over_the_board(){
            board.movePieceToLeft();
            board.movePieceToLeft();
            board.movePieceToLeft();
            board.movePieceToLeft();
            assertEquals("" +
                    ".T........\n" +
                    "TTT.......\n" +                    
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
            
            board.movePieceToLeft();
            assertEquals("" +
                    ".T........\n" +
                    "TTT.......\n" +                    
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void it_can_not_be_moved_right_over_the_board(){
            board.movePieceToRight();
            board.movePieceToRight();
            board.movePieceToRight();            
            assertEquals("" +
                    "........T.\n" +
                    ".......TTT\n" +                    
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
            
            board.movePieceToRight();
            assertEquals("" +
                    "........T.\n" +
                    ".......TTT\n" +                    
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n", board.toString());
        }
        
        @Test
        public void it_can_not_be_moved_down_over_the_board(){
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    ".....T....\n" +
                    "....TTT...\n" , board.toString());                    
            
            board.movePieceDown();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    ".....T....\n" +
                    "....TTT...\n" , board.toString());                    
            assertFalse(board.hasFalling());
        }
    }
    
    public class can_not_move_with_piece_on_the_way {
        
        @Before
        public void dropPiece() {
            board.drop(Tetromino.O_SHAPE);
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceToLeft();
            board.movePieceToLeft();
            board.movePieceToLeft();
            board.tick();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..OO......\n" +
                    "..OO......\n" , board.toString());
            assertFalse(board.hasFalling());
            
            board.drop(Tetromino.O_SHAPE);
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceDown();
            board.movePieceToRight();
            board.movePieceToRight();
            board.tick();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..OO...OO.\n" +
                    "..OO...OO.\n" , board.toString());
            assertFalse(board.hasFalling());
            
            board.drop(Tetromino.T_SHAPE);
            board.movePieceDown();
            board.movePieceDown();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    ".....T....\n" +
                    "....TTT...\n" +
                    "..OO...OO.\n" +
                    "..OO...OO.\n" , board.toString());
            
        } 
        
        @Test
        public void it_can_not_be_moved_left_over_other_piece(){
            board.movePieceDown();
            board.movePieceToLeft();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    ".....T....\n" +
                    "..OOTTTOO.\n" +
                    "..OO...OO.\n" , board.toString());
            assertTrue(board.hasFalling());            
        }
        
        @Test
        public void it_can_not_be_moved_right_over_other_piece(){
            board.movePieceDown();
            board.movePieceToRight();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    ".....T....\n" +
                    "..OOTTTOO.\n" +
                    "..OO...OO.\n" , board.toString());
            assertTrue(board.hasFalling());            
        }
        
        @Test
        public void it_can_not_be_moved_down_over_other_piece(){
            board.movePieceToRight();
            board.movePieceDown();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "......T...\n" +
                    ".....TTT..\n" +
                    "..OO...OO.\n" +
                    "..OO...OO.\n" , board.toString());
            assertFalse(board.hasFalling());            
        }
        
        @Test
        public void it_can_not_be_moved_if_has_no_piece_falling(){
            board.tick();
            board.tick();
            board.tick();
            
            board.movePieceDown();
            board.movePieceToLeft();
            board.movePieceToRight();
            assertEquals("" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "..OO.T.OO.\n" +
                    "..OOTTTOO.\n" , board.toString());
            assertFalse(board.hasFalling());            
        }
    }
}
