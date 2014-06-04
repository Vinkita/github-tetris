/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ukita
 */
public class ShuffleBag{
    private ArrayList<RotatablePiece> UnShuffledBag = new ArrayList<RotatablePiece>();
    private ArrayList<RotatablePiece> ShuffledBag = new ArrayList<RotatablePiece>();
    
    private final int MAXI = 12;    
    private int lastI = 0;
    
    
    public ShuffleBag(){
        fillUnshuffled();
        try {
            refillShuffled();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ShuffleBag.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fillUnshuffled(){
        UnShuffledBag = new ArrayList<RotatablePiece>();
        UnShuffledBag.add(0, Tetromino.I_SHAPE);
        UnShuffledBag.add(1, Tetromino.O_SHAPE);
        UnShuffledBag.add(2, Tetromino.T_SHAPE);
        UnShuffledBag.add(3, Tetromino.S_SHAPE);
        UnShuffledBag.add(4, Tetromino.Z_SHAPE);
        UnShuffledBag.add(5, Tetromino.J_SHAPE);
        UnShuffledBag.add(6, Tetromino.L_SHAPE);
    }
    
    private void refillShuffled() throws CloneNotSupportedException {
        @SuppressWarnings("unchecked")
		ArrayList<RotatablePiece> auxMap = (ArrayList<RotatablePiece>)UnShuffledBag.clone();
        
        Random rand = new Random();
        while(!auxMap.isEmpty()){
            int number = rand.nextInt(auxMap.size());
            int indexOfIShape = auxMap.indexOf(Tetromino.I_SHAPE);
            
            if (number == indexOfIShape)
            {
                lastI = 0;
            }
            else if (lastI > MAXI){
                number = indexOfIShape;
                lastI = 0;
            }
            
            ShuffledBag.add(auxMap.remove(number));
        }
        
    }
    
    public RotatablePiece pullOut(){
    
        
        
        RotatablePiece pulledTetro = ShuffledBag.remove(0);
        if(ShuffledBag.isEmpty()){
            try {
                refillShuffled();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ShuffleBag.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pulledTetro;
    }
    
    public RotatablePiece preview(){
        return ShuffledBag.get(0);
    }
}
