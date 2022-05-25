package com.Game.Util.Math;

import java.util.Random;

public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(float xy) {
        this.x = xy;
        this.y = xy;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public String toString() {
        return x + " " + y;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;

        return new Vector2(x, y);
    }

    public Vector2 add(float dx, float dy) {
        this.x = x + dx;
        this.y = y + dy;

        return new Vector2(x, y);
    }


    public Vector2 subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;

        return new Vector2(x, y);
    }

    public Vector2 addClone(Vector2 other) {
        float xx = x + other.x;
        float yy = y + other.y;

        return new Vector2(xx, yy);
    }

    public Vector2 addClone(float dx, float dy) {
        float xx = x + dx;
        float yy = y + dy;

        return new Vector2(xx, yy);
    }

    public Vector2 addClone(double dx, double dy) {
        float xx = x + (float) dx;
        float yy = y + (float) dy;

        return new Vector2(xx, yy);
    }

    public Vector2 subtractClone(Vector2 other) {
        float xx = x - other.x;
        float yy = y - other.y;

        return new Vector2(xx, yy);
    }

    public Vector2 subtractClone(float dx, float dy) {
        float xx = x - dx;
        float yy = y - dy;

        return new Vector2(xx, yy);
    }

    public int compareTo(Vector2 other) {
        boolean c1 = x > other.x;
        boolean c2 = y > other.y;
        boolean c3 = x == other.x;
        boolean c4 = y == other.y;

        return ((c1 ? 1 : -1) + (c2 ? 1 : -1) + (c3 ? 1 : 0) + (c4 ? 1 : 0)) / 2;

        /*
          Values:
          Mix => 0 points
          Greater Than => 1 points
          Less Than => -1 points
        */
    }

    public boolean equalTo(Vector2 compare) {
        return x == compare.x && y == compare.y;
    }

    public Vector2 scale(float scaleFactor) {
        this.x *= scaleFactor;
        this.y *= scaleFactor;

        return new Vector2(x, y);
    }

    public Vector2 scaleClone(Vector2 scaleFactor) {
        float xx = x * scaleFactor.x;
        float yy = y * scaleFactor.y;

        return new Vector2(xx, yy);
    }

    public Vector2 scaleClone(float x, float y) {
        float xx = this.x * x;
        float yy = this.y * y;

        return new Vector2(xx, yy);
    }

    public Vector2 scaleClone(float scaleFactor) {
        float xx = x * scaleFactor;
        float yy = y * scaleFactor;

        return new Vector2(xx, yy);
    }

    public Vector2 addClone(float offset) {
        float xx = x + offset;
        float yy = y + offset;

        return new Vector2(xx, yy);
    }

    public void offset(float offset) {
        x += offset;
        y += offset;
    }

    public static Vector2 randomVector(float xRange, float yRange) {
        Random random = new Random();

        Vector2 offset = new Vector2(random.nextFloat() * xRange, random.nextFloat() * yRange);

        return offset;
    }

    public static Vector2 magnitudeDirection(Vector2 v1, Vector2 v2) {
        Vector2 difference = v2.subtractClone(v1);
        difference.normalize();
        return difference;
    }

    public static Vector2 reverseMag(Vector2 v1, Vector2 v2) {
        float dx = v2.x - v1.x;
        float dy = v2.y - v1.y;

        Vector2 result = new Vector2(dx, dy);

        float slope = 0;

        boolean xy = (Math.abs(dx) < Math.abs(dy));

        slope = xy ? 1 / dx : 1 / dy;

        result.scale(slope);

        float xx = result.x;
        float yy = result.y;

        result.x = Math.abs(xx) * Math.signum(dx);
        result.y = Math.abs(yy) * Math.signum(dy);

        return result;
    }

    /**
     * Changes the parent vector to the intersection of a line
     * from (0,0) to (x,y) and a circle radius 1 centered at
     * the origin, so that the line has a length of 1. This
     * makes sure that when something moves, no matter what
     * direction they are going, they will be moving at a
     * static speed.
     */
    public Vector2 normalize() {
        double distance = Math.sqrt(x * x + y * y);

        if (distance == 0)
            return this;

        x /= distance;
        y /= distance;

        return this;
    }

    public float getScale() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static Vector2 dynamicNormalization(Vector2 origin, Vector2 vector) {
        Vector2 v = vector.subtractClone(origin);
        v.normalize();
        v.addClone(origin);

        return v;
    }

    public static Vector2 normalize(Vector2 vector) {
        Vector2 v = vector.clone();
        v.normalize();

        return v;
    }

    public static float distance(Vector2 v1, Vector2 v2) {
        double x = (v1.x - v2.x);
        double y = (v1.y - v2.y);

        x = Math.pow(x, 2);
        y = Math.pow(y, 2);

        double dist = x + y;

        dist = Math.sqrt(dist);

        return (float) dist;
    }

    public boolean greaterThan(Vector2 v1) {
        return compareTo(v1) == 1;
    }

    public static Vector2 identity(float scale) {
        return new Vector2(scale, scale);
    }

    public boolean isEqual(Vector2 other) {
        return x == other.x && y == other.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public String ints() {
        return x + ", " + y;
    }

    public static Vector2 fromString(String string) {
        String[] parts = string.split(" ");

        return new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
    }

    /**
     * Creates a movement vector for a potential moving object / projectile / enemy that will hit a target moving in a certain direction.
     * If the target continues to move in the same direction, using this direction ensures that the object will be hit the player, assuming it is applied
     * like a normal movement Vector2.
     *
     * IF the provided speed prevents the object from mathematically reaching the object, the method will instead return the magnitude direction
     * between the self and the target.
     *
     * TODO: Come up with a better alternative if the method is unable to come up with a definite answer. (The determinant is less than zero)
     *
     * @param selfPos - Position of the object that is attempting to reach the target
     * @param targetPos - The current position of the target
     * @param targetVel - The current velocity of the target (if this remains constant, the target will be hit)
     * @param speed - The speed the object moves at
     * @return The velocity needed to target the player's current trajectory.
     */
    public static Vector2 movementPredictVector(Vector2 selfPos, Vector2 targetPos, Vector2 targetVel, float speed) {
        try {
        /*
            The 'difference quotients' of the function.
            These don't serve a concrete mathematical purpose, but they shorten the code a little bit.
            I also do recognize that difference quotients already exist in math, I just do not have anything better to call them,
            as they are a quotient of differences.
         */
            float Dx = (targetPos.x - selfPos.x) / (targetPos.y - selfPos.y);
            float Dy = (targetPos.y - selfPos.y) / (targetPos.x - selfPos.x);

        /*
            References to the needed parameters squared, just makes the code a bit more readable.
         */
            Vector2 vp2 = new Vector2(targetVel.x * targetVel.x, targetVel.y * targetVel.y);
            Vector2 D2 = new Vector2(Dx * Dx, Dy * Dy);
            float s2 = speed * speed;


        /*
            The actual equation this function revolves around. If you wanna derive this yourself, just do some system of equations with the
            following equations.

            selfPos.x + selfVel.x * t = targetPos.x + targetVel.x * t
            selfPos.y + selfVel.y * t = targetPos.y + targetVel.y * t
            selfVel.x^2 + selfVel.y^2 = speed^2

            Solve for t in one equation and then input that into the other.
            Solve for one of the selfVel values in terms of the other and then plug it into the final equation.
         */
            Vector2 selfVel = Vector2.zero();

            // The 'determinants' of the function, if either are less than zero, that means that the object cannot mathematically reach the player.
            float determX = -D2.y * vp2.x + 2 * Dy * targetVel.x * targetVel.y + D2.y * s2 + s2 - vp2.y;
            float determY = -D2.x * vp2.y + 2 * Dx * targetVel.y * targetVel.x + D2.x * s2 + s2 - vp2.x;

            selfVel.x = (D2.y * targetVel.x - Dy * targetVel.y + (float) Math.sqrt(determX))
                    / (1 + D2.y);
            selfVel.y = (D2.x * targetVel.y - Dx * targetVel.x + (float) Math.sqrt(determY))
                    / (1 + D2.x);


            // Something something sqrt(x) = (+/-)sqrt(x)
            if (targetPos.y < selfPos.y)
                selfVel.y *= -1;

            if (targetPos.x < selfPos.x)
                selfVel.x *= -1;

            return selfVel.normalize();
        } catch (Exception e) {
            // I am not sure if an imaginary number is an ArithmeticException or not, and in case there are other possible edge-cases
            // it is just best for now to print out the stack trace for *unexpected* errors.
            if (!(e instanceof ArithmeticException))
                e.printStackTrace();

            // See above, a second case in case there is a divide by zero error with Dx or Dy, or
            // if the determinant is less than zero and returns a complex number for selfVel.x or selfVel.y
            return Vector2.magnitudeDirection(selfPos, targetPos);
        }
    }
}