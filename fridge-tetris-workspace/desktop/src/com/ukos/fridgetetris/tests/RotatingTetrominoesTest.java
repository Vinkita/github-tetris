package com.ukos.fridgetetris.tests;

import net.orfjackal.nestedjunit.NestedJUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukos.logics.RotatablePiece;
import com.ukos.logics.Tetromino;

/**
* @author Ukos
*/
//@Ignore("contains no test")
@RunWith(NestedJUnit.class)
public class RotatingTetrominoesTest extends Assert {

    // Step 3: The actual rotation algorithms
    // - Remove the @Ignore annotation from this class
    // - See README for how "Tetromino" is different from "Piece"
    // - Next step: FallingPiecesTest


    private RotatablePiece shape;


    public class All_shape_instances {

        @Before
        public void createAnyShape() {
            shape = Tetromino.T_SHAPE;
        }

        @Test
        public void are_immutable() {
            String original = shape.toString();
            shape.rotateRight();
            assertEquals(original, shape.toString());
            shape.rotateLeft();
            assertEquals(original, shape.toString());
        }
    }



    public class The_T_shape {

        @Before
        public void createTShape() {
            shape = Tetromino.T_SHAPE;
        }

        @Test
        public void is_shaped_like_T() {
            assertEquals("[0:1],[-1:0],[0:0],[1:0]", shape.toString());
        }

        @Test
        public void can_be_rotated_right_3_times() {
            shape = shape.rotateRight();
            assertEquals("[1:0],[0:1],[0:0],[0:-1]", shape.toString());
            shape = shape.rotateRight();
            assertEquals("[0:-1],[1:0],[0:0],[-1:0]", shape.toString());
            shape = shape.rotateRight();
            assertEquals("[-1:0],[0:-1],[0:0],[0:1]", shape.toString());
        }

        @Test
        public void can_be_rotated_left_3_times() {
            shape = shape.rotateLeft();
            assertEquals("[-1:0],[0:-1],[0:0],[0:1]", shape.toString());
            shape = shape.rotateLeft();
            assertEquals("[0:-1],[1:0],[0:0],[-1:0]", shape.toString());
            shape = shape.rotateLeft();
            assertEquals("[1:0],[0:1],[0:0],[0:-1]", shape.toString());
        }

        @Test
        public void rotating_it_4_times_will_go_back_to_the_original_shape() {
            String originalShape = shape.toString();
            shape = shape.rotateRight().rotateRight().rotateRight().rotateRight();
            assertEquals(originalShape, shape.toString());
            shape = shape.rotateLeft().rotateLeft().rotateLeft().rotateLeft();
            assertEquals(originalShape, shape.toString());
        }
    }
    
    public class The_I_shape {

        @Before
        public void createIShape() {
            shape = Tetromino.I_SHAPE;
        }

        @Test
        public void is_shaped_like_I() {
            assertEquals("[-2:0],[-1:0],[0:0],[1:0]", shape.toString());
        }

        @Test
        public void can_be_rotated_right_once() {
            shape = shape.rotateRight();
            assertEquals("[0:2],[0:1],[0:0],[0:-1]", shape.toString());
        }

        @Test
        public void can_be_rotated_left_once() {
            shape = shape.rotateLeft();
            assertEquals("[0:-2],[0:-1],[0:0],[0:1]", shape.toString());
        }

        @Test
        public void rotating_it_four_times_will_get_back_to_the_original_shape() {
            String originalShape = shape.toString();
            shape = shape.rotateRight().rotateRight().rotateRight().rotateRight();
            assertEquals(originalShape, shape.toString());
            shape = shape.rotateLeft().rotateLeft().rotateLeft().rotateLeft();
            assertEquals(originalShape, shape.toString());
        }
    }



    public class The_O_shape {

        @Before
        public void createOShape() {
            shape = Tetromino.O_SHAPE;
        }

        @Test
        public void is_shaped_like_O() {
            assertEquals("[0:1],[1:1],[0:0],[1:0]", shape.toString());
        }

        @Test
        public void can_not_be_rotated_right() {
            shape = shape.rotateRight();
            assertEquals("[0:1],[1:1],[0:0],[1:0]", shape.toString());
        }

        @Test
        public void can_not_be_rotated_left() {
            shape = shape.rotateLeft();
            assertEquals("[0:1],[1:1],[0:0],[1:0]", shape.toString());
        }
    }
}