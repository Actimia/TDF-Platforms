package org.dyndns.tdfpro.platforms;

import java.awt.Point;

public class Vec2 {
    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 NORTH = new Vec2(0, -1);
    public static final Vec2 SOUTH = new Vec2(0, 1);
    public static final Vec2 WEST = new Vec2(-1, 0);
    public static final Vec2 EAST = new Vec2(1, 0);

    public final float x;
    public final float y;

    public Vec2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(final Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Adds the vectors.
     *
     * @param o
     * @return
     */
    public Vec2 add(final Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }

    /**
     * Subtracts a vector.
     *
     * @param o
     * @return
     */
    public Vec2 sub(final Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }

    /**
     * Create a new vector with these values.
     *
     * @param x
     * @param y
     * @return
     */
    public Vec2 set(final float x, final float y) {
        return new Vec2(x, y);
    }

    /**
     * Multiply the vector with a scalar (scale with factor).
     *
     * @param f
     * @return
     */
    public Vec2 mul(final float f) {
        return new Vec2(f * x, f * y);
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == getClass()) {
            Vec2 v = (Vec2) o;
            return x == v.x && y == v.y;
        }
        return false;
    }

    /**
     * Dot product with another vector.
     *
     * @param o
     * @return
     */
    public float dot(final Vec2 o) {
        return x * o.x + y * o.y;
    }

    /**
     * Distance between two vectors.
     *
     * @param o
     * @return
     */
    public float distance(final Vec2 o) {
        return sub(o).length();
    }

    /**
     * Distance squared between two vectors.
     *
     * @param o
     * @return
     */
    public float distanceSquared(final Vec2 o) {
        return sub(o).lengthSquared();
    }

    @Override
    public int hashCode() {
        return 31 * Float.floatToIntBits(x) + Float.floatToIntBits(y);
    }

    /**
     * Negates this vector.
     *
     * @return
     */
    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 negateLocal() {
        return negate();
    }

    /**
     * Length of a vector.
     *
     * @return
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Squared length.
     *
     * @return
     */
    public float lengthSquared() {
        return x * x + y * y;
    }

    /**
     * Same direction, but length 1.
     *
     * @return
     */
    public Vec2 normalise() {
        final float l = length();
        return new Vec2(x / l, y / l);
    }

    /**
     * Set to length l if longer, else do nothing.
     *
     * @param l
     * @return
     */
    public Vec2 clamp(float l) {
        if (l * l <= lengthSquared()) {
            return this;
        }
        return scale(l);
    }

    /**
     * Same direction, but length a.
     *
     * @param a
     * @return
     */
    public Vec2 scale(final float a) {
        final float l = length();
        if (l == 0) {
            return this;
        }
        return new Vec2(x * a / l, y * a / l);
    }

    public Vec2 copy() {
        return this;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
