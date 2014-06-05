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
 * @author Esko Luontola
 */
@RunWith(NestedJUnit.class)
public class FallingBlocksTest extends Assert {

    // Step 1: Starting small
    // - See the README for motivation
    // - Next step: RotatingPiecesOfBlocksTest

    private final Board board = new Board(3, 3);


    public class A_new_board {

        @Test
        public void is_empty() {
            assertTrue(board.getBlocksToDraw().size == 0);
        }

        @Test
        public void has_no_falling_blocks() {
            assertFalse(board.hasFalling());
        }
    }


    public class When_a_block_is_dropped {

        @Before
        public void dropBlock() {
            board.drop(new FixedShape(new Array<BlockDrawable>(
            							new BlockDrawable[] {
        										new BlockDrawable(new Point(0,0), "X")
            							})));
        }

        @Test
        public void the_block_is_falling() {
            assertTrue(board.hasFalling());
        }
    

        @Test
        public void it_starts_from_the_top_middle() {
            assertEquals(board.getFallingPiece().toString(),"1,2");
        }


        @Test
        public void it_moves_down_one_row_per_tick() {
            board.update(0);
            assertEquals(board.getFallingPiece().toString(), "1,1");
        }

        @Test
        public void at_most_one_block_may_be_falling_at_a_time() {
            	board.drop(new FixedShape(new Array<BlockDrawable>(
							new BlockDrawable[] {
									new BlockDrawable(new Point(0,0), "X")
						})));
            assertEquals(board.getFallingPiece().toString(),"1,2");
        }
    }
    

    public class When_a_block_reaches_the_bottom {

        @Before
        public void fallToLastRow() {
        	board.drop(new FixedShape(new Array<BlockDrawable>(
					new BlockDrawable[] {
							new BlockDrawable(new Point(0,0), "X")
					})));
        	board.update(0);
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
            board.update(0);
        }

        @Test
        public void it_is_still_falling_on_the_last_row() {
            assertEquals(board.getFallingPiece().toString(),"1,0");
            assertTrue("the player should still be able to move the block", board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_bottom() {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
            board.update(0);
            
            assertFalse("the block should stop moving", board.hasFalling());
            assertEquals(board.cellAt(new Point(1,0)),"X");
        }
    }

/*

    public class When_a_block_lands_on_another_block {

        @Before
        public void landOnAnother() {
            board.drop(new Block('X'));
            board.tick();
            board.tick();
            board.toString();
            board.tick();
            assertEquals("" +
                    "...\n" +
                    "...\n" +
                    ".X.\n", board.toString());
            assertFalse(board.hasFalling());

            board.drop(new Block('Y'));
            board.tick();
        }

        @Test
        public void it_is_still_falling_right_above_the_other_block() {
            assertEquals("" +
                    "...\n" +
                    ".Y.\n" +
                    ".X.\n", board.toString());
            assertTrue("the player should still be able to avoid landing on the other block", board.hasFalling());
        }

        @Test
        public void it_stops_when_it_hits_the_other_block() {
            board.tick();
            assertEquals("" +
                    "...\n" +
                    ".Y.\n" +
                    ".X.\n", board.toString());
            assertFalse("the block should stop moving when it lands on the other block", board.hasFalling());
        }
    }
*/
}