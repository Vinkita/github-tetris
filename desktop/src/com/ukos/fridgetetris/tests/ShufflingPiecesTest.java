
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
