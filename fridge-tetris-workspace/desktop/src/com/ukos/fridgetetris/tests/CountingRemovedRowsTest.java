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

import com.badlogic.gdx.utils.Array;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.FixedShape;
import com.ukos.logics.Point;

/**
 *
 * @author Ukos
 */
//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class CountingRemovedRowsTest extends Assert{

    private final int A_BUNCH = 12;
    
    private Board board = new Board(10, 8);
    
    private FixedShape TABL1 = new FixedShape(new Array<BlockDrawable>(
									new BlockDrawable[] {
											new BlockDrawable(new Point(-5,-7), "X"),
											new BlockDrawable(new Point(-4,-7), "X"),
											new BlockDrawable(new Point(-3,-7), "X"),
											new BlockDrawable(new Point(-2,-7), "X"),
											new BlockDrawable(new Point(-1,-7), "X"),
											new BlockDrawable(new Point(1,-7), "X"),
											new BlockDrawable(new Point(2,-7), "X"),
											new BlockDrawable(new Point(3,-7), "X"),
											new BlockDrawable(new Point(4,-7), "X"),
											
											new BlockDrawable(new Point(-5,-6), "X"),
											new BlockDrawable(new Point(-4,-6), "X"),
											new BlockDrawable(new Point(-3,-6), "X"),
											new BlockDrawable(new Point(-2,-6), "X"),
											new BlockDrawable(new Point(-1,-6), "X"),
											new BlockDrawable(new Point(1,-6), "X"),
											new BlockDrawable(new Point(2,-6), "X"),
											new BlockDrawable(new Point(3,-6), "X"),
											new BlockDrawable(new Point(4,-6), "X"),
											
											new BlockDrawable(new Point(-5,-5), "X"),
											new BlockDrawable(new Point(-4,-5), "X"),
											new BlockDrawable(new Point(-3,-5), "X"),
											new BlockDrawable(new Point(-2,-5), "X"),
											new BlockDrawable(new Point(-1,-5), "X"),
											new BlockDrawable(new Point(1,-5), "X"),
											new BlockDrawable(new Point(2,-5), "X"),
											new BlockDrawable(new Point(3,-5), "X"),
											new BlockDrawable(new Point(4,-5), "X"),
											
											new BlockDrawable(new Point(-5,-4), "X"),
											new BlockDrawable(new Point(-4,-4), "X"),
											new BlockDrawable(new Point(-3,-4), "X"),
											new BlockDrawable(new Point(-2,-4), "X"),
											new BlockDrawable(new Point(-1,-4), "X"),
											new BlockDrawable(new Point(1,-4), "X"),
											new BlockDrawable(new Point(2,-4), "X"),
											new BlockDrawable(new Point(3,-4), "X"),
											new BlockDrawable(new Point(4,-4), "X"),
											
											new BlockDrawable(new Point(-5,-3), "X"),
											new BlockDrawable(new Point(-4,-3), "X"),
											new BlockDrawable(new Point(-3,-3), "X"),
											new BlockDrawable(new Point(-2,-3), "X"),
											new BlockDrawable(new Point(-1,-3), "X"),
											new BlockDrawable(new Point(1,-3), "X"),
											new BlockDrawable(new Point(2,-3), "X"),
											new BlockDrawable(new Point(3,-3), "X"),
											new BlockDrawable(new Point(4,-3), "X"),
										}));

    private FixedShape TABL2 = new FixedShape(new Array<BlockDrawable>(
						    		new BlockDrawable[] {
						    				new BlockDrawable(new Point(-5,-7), "X"),
						    				new BlockDrawable(new Point(-4,-7), "X"),
						    				new BlockDrawable(new Point(-3,-7), "X"),
						    				new BlockDrawable(new Point(-1,-7), "X"),
						    				new BlockDrawable(new Point(1,-7), "X"),
						    				new BlockDrawable(new Point(2,-7), "X"),
						    				new BlockDrawable(new Point(3,-7), "X"),
						    				new BlockDrawable(new Point(4,-7), "X"),
						    				
						    				new BlockDrawable(new Point(-5,-6), "X"),
						    				new BlockDrawable(new Point(-4,-6), "X"),
						    				new BlockDrawable(new Point(-3,-6), "X"),
						    				new BlockDrawable(new Point(-2,-6), "X"),
						    				new BlockDrawable(new Point(-1,-6), "X"),
						    				new BlockDrawable(new Point(1,-6), "X"),
						    				new BlockDrawable(new Point(2,-6), "X"),
						    				new BlockDrawable(new Point(3,-6), "X"),
						    				new BlockDrawable(new Point(4,-6), "X"),
						    				
						    				new BlockDrawable(new Point(-5,-5), "X"),
						    				new BlockDrawable(new Point(-4,-5), "X"),
						    				new BlockDrawable(new Point(-2,-5), "X"),
						    				new BlockDrawable(new Point(-1,-5), "X"),
						    				new BlockDrawable(new Point(1,-5), "X"),
						    				new BlockDrawable(new Point(2,-5), "X"),
						    				new BlockDrawable(new Point(3,-5), "X"),
						    				new BlockDrawable(new Point(4,-5), "X"),
						    				
						    				new BlockDrawable(new Point(-5,-4), "X"),
						    				new BlockDrawable(new Point(-4,-4), "X"),
						    				new BlockDrawable(new Point(-3,-4), "X"),
						    				new BlockDrawable(new Point(-2,-4), "X"),
						    				new BlockDrawable(new Point(-1,-4), "X"),
						    				new BlockDrawable(new Point(1,-4), "X"),
						    				new BlockDrawable(new Point(2,-4), "X"),
						    				new BlockDrawable(new Point(3,-4), "X"),
						    				new BlockDrawable(new Point(4,-4), "X"),
						    				
						    				new BlockDrawable(new Point(-5,-3), "X"),
						    				new BlockDrawable(new Point(-4,-3), "X"),
						    				new BlockDrawable(new Point(-3,-3), "X"),
						    				new BlockDrawable(new Point(-2,-3), "X"),
						    				new BlockDrawable(new Point(-1,-3), "X"),
						    				new BlockDrawable(new Point(1,-3), "X"),
						    				new BlockDrawable(new Point(2,-3), "X"),
						    				new BlockDrawable(new Point(3,-3), "X"),
						    				new BlockDrawable(new Point(4,-3), "X"),
    		}));
    
    private final FixedShape PIEZA_1 = new FixedShape(new Array<BlockDrawable>(
										new BlockDrawable[] {
												new BlockDrawable(new Point(0,0), "I")
											}));
    
    private final FixedShape PIEZA_2 = new FixedShape(new Array<BlockDrawable>(
										new BlockDrawable[] {
												new BlockDrawable(new Point(0,0), "I"),
												new BlockDrawable(new Point(0,-1), "I")
											}));
    
    private final FixedShape PIEZA_3 = new FixedShape(new Array<BlockDrawable>(
										new BlockDrawable[] {
												new BlockDrawable(new Point(0,0), "I"),
												new BlockDrawable(new Point(0,-1), "I"),
												new BlockDrawable(new Point(0,-2), "I")
											}));
    
    private final FixedShape PIEZA_4 = new FixedShape(new Array<BlockDrawable>(
										new BlockDrawable[] {
												new BlockDrawable(new Point(0,0), "I"),
												new BlockDrawable(new Point(0,-1), "I"),
												new BlockDrawable(new Point(0,-2), "I"),
												new BlockDrawable(new Point(0,-3), "I")
											}));
    
    private final FixedShape PIEZA_H = new FixedShape(new Array<BlockDrawable>(
    		new BlockDrawable[] {
    				new BlockDrawable(new Point(-2,0), "I"),
    				new BlockDrawable(new Point(-1,0), "I"),
    				new BlockDrawable(new Point(0,0), "I"),
    				new BlockDrawable(new Point(1,0), "I"),
    				new BlockDrawable(new Point(2,0), "I")
    		}));
   
    public class Counting_removed_rows{
        
        @Before
        public void setUp() {
            board.drop(TABL1);
            board.tick();
            Assert.assertEquals("No Rows should be removed yet", 0, board.getRemovedRows());
        }
        
        @Test
        public void can_count_single_row(){
            board.drop(PIEZA_1);
            assertEquals("" +
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]," +
            		"[5:7]"
            		, board.toString()); 
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
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]," +
            		"[5:7],[5:6]"
            		, board.toString()); 
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
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]," +
            		"[5:7],[5:6],[5:5]"
            		, board.toString()); 
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
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]," +
            		"[5:7],[5:6],[5:5],[5:4]"
            		, board.toString()); 
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals(4, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_ZERO_rows(){
            board.drop(PIEZA_H);
            
            assertEquals("" +
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]," +
            		"[3:7],[4:7],[5:7],[6:7],[7:7]"
            		, board.toString()); 
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            
            assertEquals("" +
            		"[7:5],[6:5],[5:5],[4:5],[3:5]," +
            		"[9:4],[8:4],[7:4],[6:4],[4:4],[3:4],[2:4],[1:4],[0:4]," +
            		"[9:3],[8:3],[7:3],[6:3],[4:3],[3:3],[2:3],[1:3],[0:3]," +
            		"[9:2],[8:2],[7:2],[6:2],[4:2],[3:2],[2:2],[1:2],[0:2]," +
            		"[9:1],[8:1],[7:1],[6:1],[4:1],[3:1],[2:1],[1:1],[0:1]," +
            		"[9:0],[8:0],[7:0],[6:0],[4:0],[3:0],[2:0],[1:0],[0:0]" 
            		, board.toString()); 
            assertEquals(0, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
        
        @Test
        public void can_count_IRREGULAR_rows(){
            board = new Board(10,8);
            board.drop(TABL2);
            board.tick();
            
            board.drop(PIEZA_4);
            for(int i = 0; i < A_BUNCH; i++){
                board.tick();
            }
            assertEquals(2, board.getRemovedRows());
            assertFalse(board.hasFalling());
        }
    }
    
    
    
}
