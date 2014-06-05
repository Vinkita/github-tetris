/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.Board;
import com.ukos.logics.FixedShape;

/**
 *
 * @author Ukita
 */
@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class CountingRemovedRowsTest extends Assert{

//   TODO  removing full rows 
//   TODO counting removed rows,
//     (For counting the removed rows, you could launch an event (call a listener's method) when a row is removed )
//   TODO counting score
//   TODO choosing the next piece by random (using a shuffle bag).
    
    private final int A_BUNCH = 12;
    
    private Board board = new Board(8, 10);
    
    private final FixedShape PIEZA_1 = new FixedShape("" +
                                        "I");
    
    private final FixedShape PIEZA_2 = new FixedShape("" +
                                    ".I.\n" +
                                    ".I.\n" +
                                    "...\n");
    
    private final FixedShape PIEZA_3 = new FixedShape("" +
                                    ".I.\n" +
                                    ".I.\n" +
                                    ".I.\n");
    
    private final FixedShape PIEZA_4 = new FixedShape("" +
                                    "..I..\n" +
                                    "..I..\n" +
                                    "..I..\n" +
                                    "..I..\n" +
                                    ".....\n");
        
    
  
    public class Counting_removed_rows{
        
        @Before
        public void setUp() {
            board = new Board("" + 
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n");
            Assert.assertEquals("No Rows should be removed yet", 0, board.getRemovedRows());
        }
        
        @Test
        public void can_count_single_row(){
            board.drop(PIEZA_1);
            assertEquals("" + 
                    ".....I....\n" +
                    "..........\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertFalse(board.hasFalling());
            assertEquals(1, board.getRemovedRows());
        }
        
        @Test
        public void can_count_TWO_rows(){
            board.drop(PIEZA_2);
            assertEquals("" + 
                    ".....I....\n" +
                    ".....I....\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals(2, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_THREE_rows(){
            board.drop(PIEZA_3);
            assertEquals("" + 
                    ".....I....\n" +
                    ".....I....\n" +
                    ".....I....\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals(3, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_FOUR_rows(){
            board.drop(PIEZA_4);
            assertEquals("" + 
                    ".....I....\n" +
                    ".....I....\n" +
                    ".....I....\n" +
                    "XXXXXIXXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals(4, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_ZERO_rows(){
            board.drop(new FixedShape("" +
                    "IIIII\n" +
                    ".....\n" +
                    ".....\n" +
                    ".....\n" +
                    ".....\n"));
            
            assertEquals("" + 
                    "...IIIII..\n" +
                    "..........\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals("" + 
                    "..........\n" +
                    "..........\n" +
                    "...IIIII..\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n", board.toString());
            assertEquals(0, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_IRREGULAR_rows(){
            board = new Board("" + 
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XX.XX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXX.X.XXXX\n");
            
            board.drop(PIEZA_4);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertEquals(2, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
    }
    
    
    
}
