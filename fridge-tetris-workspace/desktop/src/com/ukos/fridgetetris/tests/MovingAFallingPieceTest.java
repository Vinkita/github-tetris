package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.Board;
import com.ukos.logics.Tetromino;

/**
 * @author Ukos
 */
//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class MovingAFallingPieceTest extends Assert {

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
    
    private final Board board = new Board(8, 6);
    
    public class Pieces_can_be_moved {

        @Before
        public void dropPiece() {
            board.drop(Tetromino.T_SHAPE);
        }

        @Test
        public void it_starts_from_top_middle() {
            assertEquals("[4:6],[3:5],[4:5],[5:5]", board.toString());
        }
        
        
        @Test
        public void it_can_be_moved_left() {
            board.movePieceToLeft();
            assertEquals("[3:6],[2:5],[3:5],[4:5]", board.toString());
        }
        
        @Test
        public void it_can_be_moved_right() {
            board.movePieceToRight();
            assertEquals("[5:6],[4:5],[5:5],[6:5]", board.toString());
        }
        
        @Test
        public void it_can_be_moved_down(){
            board.movePieceDown();
            assertEquals("[4:5],[3:4],[4:4],[5:4]", board.toString());
        }
    }
    
    public class Pieces_can_not_be_moved_over_the_board {
        
        @Before
        public void dropPiece() {
            board.drop(Tetromino.T_SHAPE);
        }
        
        @Test
        public void it_can_not_be_moved_left_over_the_board(){
            board.testMovePieceToLeft();
            board.testMovePieceToLeft();
            board.testMovePieceToLeft();
            assertEquals("[1:6],[0:5],[1:5],[2:5]", board.toString());
            
            board.testMovePieceToLeft();
            assertEquals("[1:6],[0:5],[1:5],[2:5]", board.toString());
        }
        
        @Test
        public void it_can_not_be_moved_right_over_the_board(){
            board.testMovePieceToRight();
            board.testMovePieceToRight();
            board.testMovePieceToRight();            
            assertEquals("[6:6],[5:5],[6:5],[7:5]", board.toString());
            
            board.testMovePieceToRight();
            assertEquals("[6:6],[5:5],[6:5],[7:5]", board.toString());
        }
        
        @Test
        public void it_can_not_be_moved_down_over_the_board(){
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[4:1],[3:0],[4:0],[5:0]", board.toString());
            
            board.tick();
            assertEquals("[5:0],[4:0],[3:0],[4:1]", board.toString());
            assertFalse(board.hasFalling());
        }
    }
    
    public class can_not_move_with_piece_on_the_way {
        
        @Before
        public void dropPiece() {
            board.drop(Tetromino.O_SHAPE);
            assertEquals("[4:6],[5:6],[4:5],[5:5]", board.toString());
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.testMovePieceToLeft();
            board.testMovePieceToLeft();
            board.testMovePieceToLeft();
            board.tick();
            assertEquals("[2:0],[1:0],[2:1],[1:1]", board.toString());
            assertFalse(board.hasFalling());
            
            board.drop(Tetromino.O_SHAPE);
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.testMovePieceToRight();
            board.testMovePieceToRight();
            board.tick();
            assertEquals("[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1]", board.toString());
            assertFalse(board.hasFalling());
            
            board.drop(Tetromino.T_SHAPE);
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1],[4:3],[3:2],[4:2],[5:2]", board.toString());
            
        } 
        
        @Test
        public void it_can_not_be_moved_left_over_other_piece(){
            board.tick();
            board.testMovePieceToLeft();
            assertEquals("[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1],[4:2],[3:1],[4:1],[5:1]", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void it_can_not_be_moved_right_over_other_piece(){
            board.tick();
            board.testMovePieceToRight();
            assertEquals("[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1],[4:2],[3:1],[4:1],[5:1]", board.toString());
            assertTrue(board.hasFalling());            
        }
        
        @Test
        public void it_can_not_be_moved_down_over_other_piece(){
            board.testMovePieceToRight();
            board.tick();
//            assertEquals("" +
//                    "........\n" +
//                    "........\n" +
//                    "....T...\n" +
//                    "...TTT..\n" +
//                    ".OO...OO\n" +
//                    ".OO...OO\n" , board.toString());
            assertEquals("[6:2],[5:2],[4:2],[5:3],[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1]", board.toString());
            assertFalse(board.hasFalling());            
        }
        
        @Test
        public void it_can_not_be_moved_if_has_no_piece_falling(){
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[5:0],[4:0],[3:0],[4:1],[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1]", board.toString());
            
            board.tick();
            board.testMovePieceToLeft();
            board.testMovePieceToRight();
            assertEquals("[5:0],[4:0],[3:0],[4:1],[7:0],[6:0],[7:1],[6:1],[2:0],[1:0],[2:1],[1:1]", board.toString());
            assertFalse(board.hasFalling());            
        }
    }
}
