package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.utils.Array;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.FixedShape;
import com.ukos.logics.Point;

/**
* @author Ukos
*/
@RunWith(NestedJUnit.class)
public class FallingBlocksTest extends Assert {

    // Step 1: Starting small
    // - See the README for motivation
    // - Next step: RotatingPiecesOfBlocksTest

    private final Board board = new Board(3, 3);
    private FixedShape testFalling = new FixedShape(new Array<BlockDrawable>(
new BlockDrawable[] {
new BlockDrawable(new Point(0,0), "X")
}));


    public class A_new_board {

        @Test
        public void is_empty() {
            assertEquals(board.getBoardBlocksToDraw().toString(), "[]");
        }

        @Test
        public void has_no_falling_blocks() {
            assertFalse(board.hasFalling());
        }
    }


    public class When_a_block_is_dropped {

        @Before
        public void dropBlock() {
            board.drop(testFalling);
        }

        @Test
        public void the_block_is_falling() {
            assertTrue(board.hasFalling());
        }
    

        @Test
        public void it_starts_from_the_top_middle() {
            assertEquals("[1:2]",board.toString());
        }


        @Test
        public void it_moves_down_one_row_per_tick() {
            board.tick();
            assertEquals("[1:1]",board.toString());
        }

        @Test
        public void at_most_one_block_may_be_falling_at_a_time() {
         board.drop(testFalling);
            assertEquals("[1:2]",board.toString());
        }
    }

    public class When_a_block_reaches_the_bottom {

        @Before
        public void fallToLastRow() {
            board.drop(testFalling);
            board.tick();
            board.tick();
        }

        @Test
        public void it_is_still_falling_on_the_last_row() {
         assertEquals("[1:0]",board.toString());
            assertTrue("the player should still be able to move the block", board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_bottom() {
            board.tick();
            
            assertEquals("[1:0]",board.toString());
            assertFalse("The block should stop moving", board.hasFalling());
        }
    }



    public class When_a_block_lands_on_another_block {

        @Before
        public void landOnAnother() {
            board.drop(testFalling);
            board.tick();
            board.tick();
            board.tick();
            assertEquals("[1:0]",board.toString());
            assertFalse("The block should stop moving", board.hasFalling());

            board.drop(testFalling);
            board.tick();
        }

        @Test
        public void it_is_still_falling_right_above_the_other_block() {
         assertEquals("[1:0],[1:1]",board.toString());
            assertTrue("The player should still be able to avoid landing on the other block", board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_other_block() {
            board.tick();
     assertEquals("[1:1],[1:0]",board.toString());
            assertFalse("The block should stop moving when it lands on the other block", board.hasFalling());
        }
    }

}