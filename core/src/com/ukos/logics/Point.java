/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukos.logics;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Ukita
 */
public class Point{
    private Vector2 point = new Vector2();

    public Point(float x, float y) {
        point.x = x;
        point.y = y;
    }
    
    public Point(Vector2 point) {
        this.point = point;
    }
    
    public float X() {
        return point.x;
    }

    public float Y() {
        return point.y;
    }
    
    public Point moveDown() {
        return new Point(point.x, point.y-1);
    }

    public Point moveLeft() {
        return new Point(point.x-1, point.y);
    }

    public Point moveRight() {
        return new Point(point.x+1, point.y);
    }
	
	public Point cpy() {
		return new Point(point.cpy());
	}

	public Point add(Point v) {
		Vector2 aux = point.cpy();
		return new Point(aux.add(v.point));
	}

	public Point add(float x, float y) {
		Vector2 aux = point.cpy();
		return new Point(aux.add(x, y));
	}
}
