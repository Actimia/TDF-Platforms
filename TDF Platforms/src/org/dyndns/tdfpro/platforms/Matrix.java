package org.dyndns.tdfpro.platforms;

public class Matrix<T> {
    private Object[][] mat;

    public Matrix(int x, int y) {
        mat = new Object[x][y];
    }

    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        if (x >= 0 && x < mat.length && y >= 0 && y < mat[0].length) {
            return (T) mat[x][y];
        }
        return null;
    }

    public T set(int x, int y, T o) {
        mat[x][y] = o;
        return o;
    }
}
