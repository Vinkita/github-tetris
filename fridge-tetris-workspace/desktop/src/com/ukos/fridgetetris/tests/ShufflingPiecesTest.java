
package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.RotatableGrid;
import com.ukos.logics.RotatablePiece;
import com.ukos.logics.ShuffleBag;
import com.ukos.logics.Tetromino;

/**
 *
 * @author Ukita
 */
@RunWith(NestedJUnit.class)
public class ShufflingPiecesTest extends Assert{
    
    ShuffleBag bolsita;
    
    public class When_shuffling_pieces{
        
        @Before
        public void setup(){
            bolsita  = new ShuffleBag();
        }
        
        @Test
        public void shuffle_ok(){
            RotatableGrid tetro = bolsita.pullOut();
            assertTrue(tetro != null);
        }
        
        @Test
        public void pullsout_IShape_after_12_other_piece_drops(){
            RotatablePiece tetro;
            int cont=0;
            for (int i=0; i<1000; i++){
                tetro = bolsita.pullOut();
                cont++;
                if (tetro.equals(Tetromino.I_SHAPE))
                    cont=0;
                assertTrue(cont<13);
            }
        }
        
        @Test
        public void no_more_than_4_snake_pieces_in_a_row(){
        	int snakes = 0;
        	RotatablePiece tetro;
        	for(int j = 1; j < 8; j++){
        		for (int i = 0; i<100000; i++){
        			bolsita.preview(j);
        			tetro = bolsita.pullOut();
        			if (tetro.equals(Tetromino.S_SHAPE) || tetro.equals(Tetromino.Z_SHAPE))
        				snakes++;
        			else
        				snakes = 0;
        			assertFalse(snakes > 4);
        		}        		
        	}
        }
        
        @Test
        public void no_more_than_2_same_type_pieces_in_a_row(){
        	int same = 0;
        	RotatablePiece previous = null;
        	RotatablePiece tetro = null;
        	for(int j = 1; j < 8; j++){
        		for (int i = 0; i<100000; i++){
        			bolsita.preview(j);
        			previous = tetro;
        			tetro = bolsita.pullOut();
        			if (tetro.equals(previous))
        				same++;
        			else
        				same = 0;
        			assertFalse(same > 2);
        		}        		
        	}
        }
        
        @Test
        public void can_preview_next_piece(){
            assertTrue(bolsita.preview() != null);
        }
        
        @Test
        public void preview_matches_next_pulledout_piece(){
            for (int i = 0; i<1000; i++){
                RotatablePiece tetroPreview = bolsita.preview();
                RotatablePiece tetroNext = bolsita.pullOut();
                assertTrue(tetroPreview.equals(tetroNext));
            }
        }
        
        
        
    }
}
