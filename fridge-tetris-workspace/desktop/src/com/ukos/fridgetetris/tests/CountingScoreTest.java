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
import com.ukos.logics.ScoreCounter;

/**
 *
 * @author Ukos
 */
//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class CountingScoreTest extends Assert{

    private final int A_BUNCH = 12;
    
    private Board board = new Board(10, 8);
    private ScoreCounter counter;
    
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
	
	public class Counting_Scores{
        
        @Before
        public void setUp() {
            board.drop(TABL1);
            board.tick();
            
            counter = new ScoreCounter(board.getLevel());
            board.addRowListener(counter);
            assertEquals("No Rows should be counted yet", 0, counter.getRemovedRows());
            assertEquals("Score should be zero", 0, counter.getTotalScore());
        }
        
        @Test
        public void points_for_single_row(){
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
            assertEquals(1, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_1_ROW, counter.getTotalScore());
        }
        
        @Test
        public void points_for_TWO_rows(){
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
            
            assertEquals(2, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_2_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_THREE_rows(){
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
            
            assertEquals(3, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_3_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_FOUR_rows(){
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
            
            assertEquals(4, counter.getRemovedRows());
            assertEquals(ScoreCounter.POINTS_4_ROWS, counter.getTotalScore());
        }
        
        @Test
        public void points_for_ZERO_rows(){
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
            board = new Board(10,8);
            board.drop(TABL2);
            board.tick();
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
