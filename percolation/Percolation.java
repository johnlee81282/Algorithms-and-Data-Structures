import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // the percolation grid where true is open and false is closed
    private boolean[][] grid;
    // length of the column or the row
    private int size;
    // root that contains all the top row elements
    private int top;
    // root that contains all the bottom row elements
    private int bottom;
    // number of open sites
    private int count;
    // pre-made weighted quick union from Princeton
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Error: out of bounds");
        }
        grid = new boolean[n][n];
        size = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        top = size * size;
        bottom = size * size + 1;
        count = 0;
    }

    // transforms the index of the matrix into a one dimension index
    private int onedimension(int row, int col) {
        return row * size + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > size - 1 || col < 0 || col > size - 1) {
            throw new IllegalArgumentException("Error: Out of bound");
        }

        if (!grid[row][col]) {
            grid[row][col] = true;
            count += 1;
        }

        if (row == 0) {
            uf.union(onedimension(row, col), top);
        }
        if (row == size - 1) {
            uf.union(onedimension(row, col), bottom);
        }

        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(onedimension(row - 1, col), onedimension(row, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(onedimension(row, col - 1), onedimension(row, col));
        }
        if (row < size - 1 && isOpen(row + 1, col)) {
            uf.union(onedimension(row + 1, col), onedimension(row, col));
        }
        if (col < size - 1 && isOpen(row, col + 1)) {
            uf.union(onedimension(row, col + 1), onedimension(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > size - 1 || col < 0 || col > size - 1) {
            throw new IllegalArgumentException("Error: Out of bound");
        }
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > size - 1 || col < 0 || col > size - 1) {
            throw new IllegalArgumentException("Error: Out of bound");
        }
        return uf.find(onedimension(row, col)) == uf.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }


    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation test = new Percolation(5);
        test.open(0, 1);
        test.open(1, 1);
        test.open(1, 2);
        test.open(2, 2);
        test.open(2, 3);
        test.open(3, 3);
        test.open(4, 3);
        test.open(1, 4);

        System.out.println(test.isOpen(2, 4));
        System.out.println(test.isFull(1, 4));

        System.out.println(test.numberOfOpenSites());

        System.out.println(test.percolates());
    }

}
