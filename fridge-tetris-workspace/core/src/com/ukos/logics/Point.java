package com.ukos.logics;

import com.badlogic.gdx.math.Vector2;

/**
 * Representa la posicion de un elemento.
 * <br> Es similar a la clase {@link Vector2}, con la diferencia de que {@code Point} que inmutable.
 * @author Ukita
 */
public class Point{
    private Vector2 point = new Vector2();

    /**
     * Crea un nuevo Point con las coordenadas especificadas
     * @param x  la coordenada x 
     * @param y  la coordenada y
     */
    public Point(float x, float y) {
        point.x = x;
        point.y = y;
    }
    
    /**
     * Crea un nuevo Point a partir de un Vector2
     * @param point  Vector2 que contiene las coordenadas
     */
    public Point(Vector2 point) {
        this.point = point;
    }
    
    /**
     * @return  el componente x del vector
     */
    public float X() {
        return point.x;
    }

    /**
     * @return  el componente y del vector
     */
    public float Y() {
        return point.y;
    }
    
    /**
     * Crea un <code>Punto</code> con la misma posicion que el actual y lo mueve una unidad hacia abajo
     * @return  un Punto debajo del actual
     */
    public Point moveDown() {
        return new Point(point.x, point.y-1);
    }

    /**
     * Crea un <code>Punto</code> con la misma posicion que el actual y lo mueve una unidad hacia la izquierda
     * @return  un Punto a la izquierda del actual
     */
    public Point moveLeft() {
        return new Point(point.x-1, point.y);
    }

    /**
     * Crea un <code>Punto</code> con la misma posicion que el actual y lo mueve una unidad hacia la derecha
     * @return  un Punto a la derecha del actual
     */
    public Point moveRight() {
        return new Point(point.x+1, point.y);
    }
	
	/**
	 * @return  una copia de este Punto
	 */
	public Point cpy() {
		return new Point(point.cpy());
	}

	/**
	 * Crea una copia del Punto actual y le añade el Punto especificado
	 * @param p  el Punto a añadir
	 * @return  un nuevo Punto
	 */
	public Point add(Point p) {
		Vector2 aux = point.cpy();
		return new Point(aux.add(p.point));
	}

	/**
	 * Crea una copia del Punto actual y le añade las coordenadas especificadas
	 * @param x  la coordenada x a añadir
	 * @param y  la coordenada y a añadir
	 * @return  un nuevo Punto
	 */
	public Point add(float x, float y) {
		Vector2 aux = point.cpy();
		return new Point(aux.add(x, y));
	}

	@Override
	public String toString() {
		return "[" + (int)point.x + ":" + (int)point.y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Point)
			if(((Point) obj).X() == this.X() && ((Point) obj).Y() == this.Y())
				return true;
		return false;
	}
	
}
