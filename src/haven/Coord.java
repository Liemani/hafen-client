/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import static java.lang.Math.PI;
import java.util.Iterator;

// lmi custom import
import static lmi.Constant.*;

public class Coord implements Comparable<Coord>, java.io.Serializable {
    public int x, y;
    public static Coord z = new Coord(0, 0);
    public static Coord[] uecw = {of(0, -1), of(1, 0), of(0, 1), of(-1, 0)};
    public static Coord[] uccw = {of(0, 0), of(1, 0), of(1, 1), of(0, 1)};
    public static Coord[] upcw = {of( 0, -1), of( 1, -1), of( 1,  0), of( 1,  1),
				  of( 0,  1), of(-1,  1), of(-1,  0), of(-1, -1)};
    public static Coord[] usqc = {of(-1, -1), of( 0, -1), of( 1, -1),
				  of(-1,  0), of( 0,  0), of( 1,  0),
				  of(-1,  1), of( 0,  1), of( 1,  1)};

    public Coord(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Coord(Coord c) {
	this(c.x, c.y);
    }

    public Coord(Coord3f c) {
	this((int)c.x, (int)c.y);
    }

    public Coord() {
	this(0, 0);
    }

    public Coord(java.awt.Dimension d) {
	this(d.width, d.height);
    }

    public static Coord of(int x, int y) {return(new Coord(x, y));}
    public static Coord of(int x) {return(of(x, x));}
    public static Coord of(Coord c) {return(of(c.x, c.y));}

    public static Coord sc(double a, double r) {
	return(of((int)Math.round(Math.cos(a) * r), -(int)Math.round(Math.sin(a) * r)));
    }

    public boolean equals(Object o) {
	if(!(o instanceof Coord))
	    return(false);
	Coord c = (Coord)o;
	return((c.x == x) && (c.y == y));
    }

    public boolean equals(int X, int Y) {
	return((x == X) && (y == Y));
    }

    public int compareTo(Coord c) {
	if(c.y != y)
	    return(c.y - y);
	if(c.x != x)
	    return(c.x - x);
	return(0);
    }

    public int hashCode() {
	return(((y & 0xffff) * 31) + (x & 0xffff));
    }

    public Coord add(int ax, int ay) {
	return(of(x + ax, y + ay));
    }

    public Coord add(Coord b) {
	return(add(b.x, b.y));
    }

    public Coord sub(int ax, int ay) {
	return(of(x - ax, y - ay));
    }

    public Coord sub(Coord b) {
	return(sub(b.x, b.y));
    }

    public Coord mul(int f) {
	return(of(x * f, y * f));
    }

    public Coord mul(int fx, int fy) {
	return(of(x * fx, y * fy));
    }

    public Coord mul(double f) {
	return(of((int)Math.round(x * f), (int)Math.round(y * f)));
    }

    public Coord mul(double fx, double fy) {
	return(of((int)Math.round(x * fx), (int)Math.round(y * fy)));
    }

    public Coord inv() {
	return(of(-x, -y));
    }

    public Coord mul(Coord f) {
	return(of(x * f.x, y * f.y));
    }

    public Coord2d mul(Coord2d f) {
	return(Coord2d.of(x * f.x, y * f.y));
    }

    public Coord div(Coord d) {
	return(of(Utils.floordiv(x, d.x), Utils.floordiv(y, d.y)));
    }

    public Coord div(int d) {
	return(div(of(d)));
    }

    public Coord div(double d) {
        return(of((int)Math.round(x / d), (int)Math.round(y / d)));
    }

    public Coord mod(Coord d) {
	return(of(Utils.floormod(x, d.x), Utils.floormod(y, d.y)));
    }

    public boolean isect(Coord c, Coord s) {
	return((x >= c.x) && (y >= c.y) && (x < c.x + s.x) && (y < c.y + s.y));
    }

    public String toString() {
	return("(" + x + ", " + y + ")");
    }

    public double angle(Coord o) {
	Coord c = o.add(this.inv());
	if(c.x == 0) {
	    if(c.y < 0)
		return(-PI / 2);
	    else
		return(PI / 2);
	} else {
	    if(c.x < 0) {
		if(c.y < 0)
		    return(-PI + Math.atan((double)c.y / (double)c.x));
		else
		    return(PI + Math.atan((double)c.y / (double)c.x));
	    } else {
		return(Math.atan((double)c.y / (double)c.x));
	    }
	}
    }

    public double abs() {
	double x = this.x, y = this.y;
	return(Math.sqrt((x * x) + (y * y)));
    }

    public Coord norm(double n) {
	return(mul(n / abs()));
    }

    public double dist(Coord o) {
	long dx = o.x - x;
	long dy = o.y - y;
	return(Math.sqrt((dx * dx) + (dy * dy)));
    }

    public Coord clip(Coord ul, Coord sz) {
	Coord ret = this;
	if(ret.x < ul.x)
	    ret = of(ul.x, ret.y);
	if(ret.y < ul.y)
	    ret = of(ret.x, ul.y);
	if(ret.x > ul.x + sz.x)
	    ret = of(ul.x + sz.x, ret.y);
	if(ret.y > ul.y + sz.y)
	    ret = of(ret.x, ul.y + sz.y);
	return(ret);
    }

    public Coord clip(Area area) {
	int x = this.x, y = this.y;
	if(x < area.ul.x) x = area.ul.x;
	if(y < area.ul.y) y = area.ul.y;
	if(x > area.br.x) x = area.br.x;
	if(y > area.br.y) y = area.br.y;
	return(((x == this.x) && (y == this.y)) ? this : of(x, y));
    }

    public Coord clipi(Area area) {
	int x = this.x, y = this.y;
	if(x < area.ul.x) x = area.ul.x;
	if(y < area.ul.y) y = area.ul.y;
	if(x >= area.br.x) x = area.br.x - 1;
	if(y >= area.br.y) y = area.br.y - 1;
	return(((x == this.x) && (y == this.y)) ? this : of(x, y));
    }

    public Iterable<Coord> offsets(Coord... list) {
	return(new Iterable<Coord>() {
		public Iterator<Coord> iterator() {
		    return(new Iterator<Coord>() {
			    int i = 0;

			    public boolean hasNext() {
				return(i < list.length);
			    }

			    public Coord next() {
				return(add(list[i++]));
			    }
			});
		}
	    });
    }

    public Coord wy(int y) {
        return(of(x, y));
    }

    public Coord addy(int dy) {
        return(of(x, y + dy));
    }

    public Coord min(int x, int y) {
	return(of(Math.min(this.x, x), Math.min(this.y, y)));
    }

    // lmi custom
    // Static Value
    public static final Coord zero = new Coord(0, 0);

    // Factory
    public static Coord of(haven.Coord2d coord2d) {
        return new Coord(
                (int)Math.floor(coord2d.x * COORD_PER_COORD2D),
                (int)Math.floor(coord2d.y * COORD_PER_COORD2D));
    }

    // Convert To Haven Coordinate
    public haven.Coord2d toCoord2d() {
        return new haven.Coord2d(
                this.x * COORD2D_PER_COORD,
                this.y * COORD2D_PER_COORD);
    }

    // Equal To
    public boolean equals(Coord coord) { return this.x == coord.x && this.y == coord.y; }

    // Operation
    //  Common Vector
    public Coord subtract(Coord coord) {
        Coord newCoord = new Coord();

        newCoord.x = this.x - coord.x;
        newCoord.y = this.y - coord.y;

        return newCoord;
    }

    public Coord multiply(Coord coord) {
        Coord newCoord = new Coord();

        newCoord.x = this.x * coord.x;
        newCoord.y = this.y * coord.y;

        return newCoord;
    }

    public Coord divide(Coord coord) {
        Coord newCoord = new Coord();

        newCoord.x = this.x / coord.x;
        newCoord.y = this.y / coord.y;

        return newCoord;
    }

    //  Common Scalar
    public Coord add(int value) {
        Coord newCoord = new Coord();

        newCoord.x = this.x + value;
        newCoord.y = this.y + value;

        return newCoord;
    }

    public Coord subtract(int value) {
        Coord newCoord = new Coord();

        newCoord.x = this.x - value;
        newCoord.y = this.y - value;

        return newCoord;
    }

    public Coord multiply(int value) {
        Coord newCoord = new Coord();

        newCoord.x = this.x * value;
        newCoord.y = this.y * value;

        return newCoord;
    }

    public Coord divide(int value) {
        Coord newCoord = new Coord();

        newCoord.x = this.x / value;
        newCoord.y = this.y / value;

        return newCoord;
    }

    public Coord floorDivide(int value) {
        Coord newCoord = new Coord();

        newCoord.x = Math.floorDiv(this.x, value);
        newCoord.y = Math.floorDiv(this.y, value);

        return newCoord;
    }

    //  Transform Vector
    public Coord assignAdd(Coord coord) {
        this.x += coord.x;
        this.y += coord.y;
        return this;
    }

    public Coord assignSubtract(Coord coord) {
        this.x -= coord.x;
        this.y -= coord.y;
        return this;
    }

    public Coord assignMultiply(Coord coord) {
        this.x *= coord.x;
        this.y *= coord.y;
        return this;
    }

    public Coord assignDivide(Coord coord) {
        this.x /= coord.x;
        this.y /= coord.y;
        return this;
    }

    //  Transform Scalar
    public Coord assignAdd(int value) {
        this.x += value;
        this.y += value;
        return this;
    }

    public Coord assignSubtract(int value) {
        this.x -= value;
        this.y -= value;
        return this;
    }

    public Coord assignMultiply(int value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Coord assignDivide(int value) {
        this.x /= value;
        this.y /= value;
        return this;
    }

    public Coord assignFloorDivide(int value) {
        this.x = Math.floorDiv(this.x, value);
        this.y = Math.floorDiv(this.y, value);
        return this;
    }

    // Convenient
    public Coord center() {
        return Coord.of(this)
            .assignFloorDivide(TILE_IN_COORD)
            .assignMultiply(TILE_IN_COORD)
            .assignAdd(TILE_IN_COORD / 2);
    }

    public Coord north() { return this.add(0, -TILE_IN_COORD); }
    public Coord east() { return this.add(TILE_IN_COORD, 0); }
    public Coord west() { return this.add(-TILE_IN_COORD, 0); }
    public Coord south() { return this.add(0, TILE_IN_COORD); }

    public Coord offset(int x, int y) { return this.add(x, y); }

    // warning: ignore overflow
    public double distance(Coord coord) {
        final int dx = this.x - coord.x;
        final int dy = this.y - coord.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
