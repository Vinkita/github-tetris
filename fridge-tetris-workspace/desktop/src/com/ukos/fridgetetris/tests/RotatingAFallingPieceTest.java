
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
import com.ukos.logics.Tetromino;

//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class RotatingAFallingPieceTest extends Assert {
    

    private Board board = new Board(10, 6);
    private final int A_FUCK_LOAD = 11;
    private final FixedShape PIEZA = new FixedShape(new Array<BlockDrawable>(
													new BlockDrawable[] {
															new BlockDrawable(new Point(0,2), "I"),
															new BlockDrawable(new Point(0,1), "I"),
															new BlockDrawable(new Point(0,0), "I"),
															new BlockDrawable(new Point(0,-1), "I"),
														}));
    
    
    public class Rotations{
        
        @Before
        public void dropPiece(){
            board.drop(Tetromino.T_SHAPE);
        }
        
        @Test
        public void rotates_right(){
            board.rotatePieceRight();
            assertEquals("[6:5],[5:6],[5:5],[5:4]", board.toString());
        }
        
        @Test
        public void rotates_left(){
            board.rotatePieceLeft();
            assertEquals("[4:5],[5:4],[5:5],[5:6]", board.toString());
        }
        
    }
    
    public class Rotations_against_the_wall{
        
        @Before
        public void dropPiece(){
            board.drop(PIEZA);
            board.tick();
            board.tick();
            assertEquals("[5:5],[5:4],[5:3],[5:2]",board.toString());
        }
        
        @Test
        public void does_right_kick(){
            for (int i = 0; i < A_FUCK_LOAD; i++)
                board.testMovePieceToRight();
            board.rotatePieceRight();
            assertEquals("[9:3],[8:3],[7:3],[6:3]", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_left_kick(){
            for (int i = 0; i < A_FUCK_LOAD; i++){
                board.testMovePieceToLeft();                
            }
            board.rotatePieceLeft();
            assertEquals("[0:3],[1:3],[2:3],[3:3]", board.toString());
            assertTrue(board.hasFalling());
        }
    }
    
    
    public class Rotations_against_a_piece{
        @Before
        public void drop_Piece(){
        	FixedShape shape = new FixedShape(new Array<BlockDrawable>(
					new BlockDrawable[] {
							new BlockDrawable(new Point(-5,-5), "X"),
							new BlockDrawable(new Point(4,-5), "X"),
							new BlockDrawable(new Point(-5,-4), "X"),
							new BlockDrawable(new Point(4,-4), "X"),
							new BlockDrawable(new Point(-5,-3), "X"),
							new BlockDrawable(new Point(4,-3), "X"),
							new BlockDrawable(new Point(-5,-2), "X"),
							new BlockDrawable(new Point(4,-2), "X"),
							new BlockDrawable(new Point(-5,-1), "X"),
							new BlockDrawable(new Point(4,-1), "X"),
							new BlockDrawable(new Point(-5,0), "X"),
							new BlockDrawable(new Point(4,0), "X")							
						}));
            board.drop(shape);
            board.tick();
            System.out.println(board.toString());
            board.drop(PIEZA);
            board.tick();
            board.tick();
            
        }
        
        @Test
        public void does_right_kick_against_piece(){
            for(int i = 0; i < A_FUCK_LOAD; i++){
                board.testMovePieceToRight();
            }
            board.rotatePieceLeft();
            assertEquals("[9:5],[0:5],[9:4],[0:4],[9:3],[0:3],[9:2],[0:2],[9:1],[0:1],[9:0],[0:0],"+
            "[5:3],[6:3],[7:3],[8:3]", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_left_kick_against_piece(){
            for(int i = 0; i < A_FUCK_LOAD; i++){
                board.movePieceToLeft();
            }
            board.rotatePieceLeft();
            assertEquals("[9:5],[0:5],[9:4],[0:4],[9:3],[0:3],[9:2],[0:2],[9:1],[0:1],[9:0],[0:0],"+
                    "[2:3],[3:3],[4:3],[5:3]", board.toString());
            assertTrue(board.hasFalling());
        }
        
        
    }
    
    public class Piece_in_a_narrow_space{
        @Before
        public void dropPiece(){
        	FixedShape shape = new FixedShape(new Array<BlockDrawable>(
					new BlockDrawable[] {
							new BlockDrawable(new Point(-2,-5), "X"),
							new BlockDrawable(new Point(1,-5), "X"),
							new BlockDrawable(new Point(-2,-4), "X"),
							new BlockDrawable(new Point(1,-4), "X"),
							new BlockDrawable(new Point(-2,-3), "X"),
							new BlockDrawable(new Point(1,-3), "X"),
							new BlockDrawable(new Point(-2,-2), "X"),
							new BlockDrawable(new Point(1,-2), "X"),
							new BlockDrawable(new Point(-2,-1), "X"),
							new BlockDrawable(new Point(1,-1), "X"),
							new BlockDrawable(new Point(-2,0), "X"),
							new BlockDrawable(new Point(1,0), "X")							
						}));
        	board.drop(shape);
        	board.tick();                    
            board.drop(PIEZA);
            board.tick();
            board.tick();
            assertEquals("[6:5],[3:5],[6:4],[3:4],[6:3],[3:3],[6:2],[3:2],[6:1],[3:1],[6:0],[3:0]," +
            "[5:5],[5:4],[5:3],[5:2]", board.toString());
        }
        
        @Test
        public void does_not_rotate_right(){          
            
            
            board.rotatePieceRight();
            assertEquals("[6:5],[3:5],[6:4],[3:4],[6:3],[3:3],[6:2],[3:2],[6:1],[3:1],[6:0],[3:0]," +
                    "[5:5],[5:4],[5:3],[5:2]", board.toString());
            assertTrue(board.hasFalling());
        }
        
        @Test
        public void does_not_rotate_left(){
            board.rotatePieceLeft();
            assertEquals("[6:5],[3:5],[6:4],[3:4],[6:3],[3:3],[6:2],[3:2],[6:1],[3:1],[6:0],[3:0]," +
                    "[5:5],[5:4],[5:3],[5:2]", board.toString());
            assertTrue(board.hasFalling());
        }
    }
    
    
}
