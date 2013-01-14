package org.dyndns.tdfpro.platforms;

import java.awt.Point;

public class Vec2 {
    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 NORTH = new Vec2(0, -1);
    public static final Vec2 SOUTH = new Vec2(0, 1);
    public static final Vec2 WEST = new Vec2(-1, 0);
    public static final Vec2 EAST = new Vec2(1, 0);

    public float x;
    public float y;

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

    public Vec2 addl(final Vec2 o) {
        x += o.x;
        y += o.y;
        return this;
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

    public Vec2 subl(final Vec2 o) {
        x -= o.x;
        y -= o.y;
        return this;
    }

    /**
     * Create a new vector with these values.
     * 
     * @param x
     * @param y
     * @return
     */
    public Vec2 set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
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

    public Vec2 mull(final float f) {
        x *= f;
        y *= f;
        return this;
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
    public Vec2 neg() {
        return new Vec2(-x, -y);
    }

    public Vec2 negl() {
        x = -x;
        y = -y;
        return this;
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
    public Vec2 norm() {
        final float l = length();
        return new Vec2(x / l, y / l);
    }

    public Vec2 norml() {
        final float l = length();
        x /= l;
        y /= l;
        return this;
    }

    /**
     * Set to length l if longer, else do nothing.
     * 
     * @param l
     * @return
     */
    public Vec2 clamp(final float l) {
        if (l * l <= lengthSquared()) {
            return this;
        }
        return scale(l);
    }

    public Vec2 clampl(final float l) {
        if (l * l <= lengthSquared()) {
            return this;
        }
        return scalel(l);
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

    public Vec2 scalel(final float a) {
        final float l = length();
        if (l != 0) {
            x *= a / l;
            y *= a / l;
        }
        return this;

    }

    public Vec2 copy() {
        return new Vec2(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public float angle(Vec2 o) {
        return (float) Math.toDegrees(Math.atan2(o.y - y, o.x - x));
    }

    public static Vec2 fromAngleAndLength(float angle, float len) {
        float rads = (float) Math.toRadians(angle);
        Vec2 res = new Vec2((float) Math.cos(rads), (float) Math.sin(rads));
        return res.mull(len);
    }
}
