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
import com.ukos.logics.ScoreCounter;

/**
 *
 * @author Ukita
 */
@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class CountingScoreTest extends Assert{

//   TODO  removing full rows 
//   TODO counting removed rows,
//     (For counting the removed rows, you could launch an event (call a listener's method) when a row is removed )
//   TODO counting score
//   TODO choosing the next piece by random (using a shuffle bag).
    
    private final int A_BUNCH = 12;
    
    private Board board = new Board(8, 10);
    private ScoreCounter counter;
    
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
        
    
  
    public class Counting_Scores{
        
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
            counter = new ScoreCounter();
            board.addRowListener(counter);
            assertEquals("No Rows should be counted yet", 0, counter.getRemovedRows());
            assertEquals("Score should be zero", 0, counter.getTotalScore());
        }
        
        @Test
        public void points_for_single_row(){
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
            assertEquals(1, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_1_ROW, counter.getTotalScore());
        }
        
        @Test
        public void points_for_TWO_rows(){
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
            
            assertEquals(2, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_THREE_rows(){
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
            
            assertEquals(3, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_3_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_FOUR_rows(){
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
            
            assertEquals(4, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_4_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_ZERO_rows(){
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
            assertEquals(0, counter.getRemovedRows());
            assertEquals(0, counter.getTotalScore());
        }
        
        @Test
        public void can_accumulate_points(){
            int pieza1 = 2;
            int pieza2 = 1;
            int pieza3 = 2;
            board.drop(PIEZA_2);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertEquals(pieza1, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS, counter.getTotalScore());
            
            board.drop(PIEZA_1);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertEquals(pieza1 + pieza2, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS + ScoreCounter.POINTS_1_ROW, counter.getTotalScore());
            
            board.drop(PIEZA_2);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertEquals(pieza1 + pieza2 + pieza3, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS + ScoreCounter.POINTS_1_ROW + ScoreCounter.POINTS_2_ROWS,
                    counter.getTotalScore());
        }
        
        @Test
        public void points_for_IRREGULAR_rows(){
            board = new Board("" + 
                    "..........\n" +
                    "..........\n" +
                    "..........\n" +
                    "XXXXX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XX.XX.XXXX\n" +
                    "XXXXX.XXXX\n" +
                    "XXX.X.XXXX\n");
            board.addRowListener(counter);
            
            board.drop(PIEZA_4);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals("board should have 2", 2, board.getRemovedRows());
            assertEquals("listener should have 2", 2, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS, counter.getTotalScore());
        }
    }
    
    
    
}
