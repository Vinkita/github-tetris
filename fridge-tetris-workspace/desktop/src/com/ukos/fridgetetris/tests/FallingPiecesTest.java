package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.ukos.logics.Board;
import com.ukos.logics.Tetromino;


//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class FallingPiecesTest extends Assert {

    // Step 4: Safe steps
    // - Remove the @Ignore annotation from this class
    // - See the README for how to proceed with this refactoring in safe steps
    // - Next step: MovingAFallingPieceTest


    private final Board board = new Board(8, 6);


    public class When_a_piece_is_dropped {

        @Before
        public void dropPiece() {
            board.drop(Tetromino.T_SHAPE);
        }

        @Test
        public void it_starts_from_top_middle() {
//        	System.out.println(Tetromino.T_SHAPE.toString());
        	assertEquals("[4:6],[3:5],[4:5],[5:5]", board.toString());
//        	[0:1],[-1:0],[0:0],[1:0]
//            assertEquals("" +
//                    "...TTT..\n" +
//                    "........\n" +
//                    "........\n" +
//                    "........\n" +
//                    "........\n" +
//                    "........\n", board.toString());
        }
    }



    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    public class When_a_piece_reaches_the_bottom {

        @Before
        public void fallToLastRow() {
            board.drop(Tetromino.T_SHAPE);
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[4:1],[3:0],[4:0],[5:0]", board.toString());
        }
        
        @Test
        public void it_is_still_falling_on_the_last_row() {
        	assertTrue("Tendria que haber una pieza", board.hasFalling());
        	assertEquals("[4:1],[3:0],[4:0],[5:0]", board.toString());
            assertTrue(board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_bottom() {
            board.tick();
            assertEquals("[5:0],[4:0],[3:0],[4:1]", board.toString());
            assertFalse(board.hasFalling());
        }
    }



    public class When_a_piece_lands_on_another_piece {

        @Before
        public void landOnAnother() {
            board.drop(Tetromino.T_SHAPE);
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[5:0],[4:0],[3:0],[4:1]", board.toString());
            assertFalse(board.hasFalling());

            board.drop(Tetromino.T_SHAPE);
            board.tick();
            board.tick();
            board.tick();
            assertTrue(board.hasFalling());
        }
//        @Ignore("contains no test")
        @Test
        public void it_is_still_falling_right_above_the_other_piece() {
        	assertEquals("" +
        			"[5:0],[4:0],[3:0],[4:1]," +
        			"[4:3],[3:2],[4:2],[5:2]" 
        			, board.toString());
            assertTrue("Should still be a falling piece", board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_other_piece() {
            board.tick();
            assertEquals("" +
            		"[5:2],[4:2],[3:2],[4:3]," + 
        			"[5:0],[4:0],[3:0],[4:1]"
        			, board.toString());
            assertFalse(board.hasFalling());
        }
    }

}
