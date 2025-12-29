import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    // store the picture
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("call constructor with null");

        // create a copy and store the copy into the instance variable
        Picture copy = new Picture(picture.width(), picture.height());
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                copy.setARGB(i, j, picture.getARGB(i, j));
            }
        }
        this.picture = copy;
    }

    // current picture
    public Picture picture() {
        Picture copy = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                copy.setARGB(i, j, picture.getARGB(i, j));
            }
        }
        return copy;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // let c1 and c2 be the 32-bit representation of the color
    // calculate the color difference between a and b
    private int delta(int c1, int c2) {
        int r = (int) Math.pow(((c1 >> 16) & 0xFF) - ((c2 >> 16) & 0xFF), 2);
        int g = (int) Math.pow(((c1 >> 8) & 0xFF) - ((c2 >> 8) & 0xFF), 2);
        int b = (int) Math.pow((c1 & 0xFF) - (c2 & 0xFF), 2);
        return r + g + b;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width())
            throw new IllegalArgumentException(
                    "Invalid x-coordinate in energy()");
        if (y < 0 || y >= height())
            throw new IllegalArgumentException(
                    "Invalid y-coordinate in energy()");

        // the 32-bit color representations of left, right, top, bottom vertex
        int left, right, top, bottom;
        if (x > 0) left = picture.getARGB(x - 1, y);
        else left = picture.getARGB(width() - 1, y);
        if (x < width() - 1) right = picture.getARGB(x + 1, y);
        else right = picture.getARGB(0, y);
        if (y > 0) top = picture.getARGB(x, y - 1);
        else top = picture.getARGB(x, height() - 1);
        if (y < height() - 1) bottom = picture.getARGB(x, y + 1);
        else bottom = picture.getARGB(x, 0);

        int deltaX = delta(left, right);
        int deltaY = delta(top, bottom);

        return Math.sqrt(deltaX + deltaY);
    }

    // find the minimum element from the array of length three
    private int findMinimumIndex(double[] array) {
        int index = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                index = i;
                min = array[i];
            }
        }
        return index - 1;
    }

    // find minimum weight path in x times y rectangle
    // the size of the energy array should be x times y
    private int[] minimumWeightPath(int x, int y, double[][] energy) {
        double[] weight = new double[x * y];
        int[] path = new int[x * y - y];
        // initialize the last row
        for (int i = 0; i < y; i++) {
            weight[x * y - y + i] = energy[x - 1][i];
        }
        // recursively go up the rows and choose the minimum of the three
        for (int i = x - 2; i >= 0; i--) {
            for (int j = 0; j < y; j++) {
                int currentIndex = i * y + j;

                // three possible options for the current pixel
                double[] children = new double[3];
                if (j == 0) children[0] = Double.POSITIVE_INFINITY;
                else children[0] = weight[currentIndex + y - 1];
                children[1] = weight[currentIndex + y];
                if (j == y - 1) children[2] = Double.POSITIVE_INFINITY;
                else children[2] = weight[currentIndex + y + 1];

                int minIndex = currentIndex + y + findMinimumIndex(children);
                weight[currentIndex] = weight[minIndex] + energy[i][j];
                path[currentIndex] = minIndex;
            }
        }

        // choose the smallest weight in the first row
        int champ = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < y; i++) {
            if (weight[i] < min) {
                min = weight[i];
                champ = i;
            }
        }

        int[] minimumPath = new int[x];
        int next = champ;
        for (int i = 0; i < x; i++) {
            minimumPath[i] = next % y;
            if (i != x - 1)
                next = path[next];
        }

        return minimumPath;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int height = height();
        int width = width();
        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
            }
        }

        return minimumWeightPath(width, height, energy);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = height();
        int width = width();
        double[][] energy = new double[height][width];
        // make sure to transpose the energy array
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[i][j] = energy(j, i);
            }
        }

        return minimumWeightPath(height, width, energy);
    }

    // check if the seam[] is valid or not
    private void seamValidityCheck(int[] seam, boolean vertical) {
        int length, range;
        if (vertical) {
            length = height();
            range = width();
        }
        else {
            length = width();
            range = height();
        }

        if (seam.length != length)
            throw new IllegalArgumentException("invalid length of seam[]");

        int before = -1;
        for (int i = 0; i < length; i++) {
            int current = seam[i];
            if (current < 0 || current >= range)
                throw new IllegalArgumentException(
                        "seam[] element out of bounds");

            if (i > 0) {
                if (Math.abs(current - before) > 1)
                    throw new IllegalArgumentException(
                            "seam[] elements not continuous");
            }

            before = current;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException(
                    "call removeHorizontalSeam() with null");
        if (height() == 1)
            throw new IllegalArgumentException(
                    "unable to remove horizontal seam "
                            + "since the height of picture is one");

        seamValidityCheck(seam, false);

        int height = height();
        int width = width();

        Picture generatedPicture = new Picture(width, height - 1);
        for (int i = 0; i < width; i++) {
            int remove = seam[i];
            int count = 0;
            for (int j = 0; j < height; j++) {
                if (j != remove) {
                    generatedPicture.setARGB(i, count, picture.getARGB(i, j));
                    count++;
                }
            }
        }

        picture = generatedPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException(
                    "call removeVerticalSeam() with null");
        if (width() == 1)
            throw new IllegalArgumentException(
                    "unable to remove vertical seam "
                            + "since the width of picture is one");

        seamValidityCheck(seam, true);

        int height = height();
        int width = width();

        Picture generatedPicture = new Picture(width - 1, height);
        for (int i = 0; i < height; i++) {
            int remove = seam[i];
            int count = 0;
            for (int j = 0; j < width; j++) {
                if (j != remove) {
                    generatedPicture.setARGB(count, i, picture.getARGB(j, i));
                    count++;
                }
            }
        }

        picture = generatedPicture;
    }

    //  unit testing
    public static void main(String[] args) {
        Picture testPicture = new Picture("city.jpg");
        StdOut.println(testPicture.width() + " " + testPicture.height());
        SeamCarver test = new SeamCarver(testPicture);
        test.removeHorizontalSeam(test.findHorizontalSeam());
        test.removeVerticalSeam(test.findVerticalSeam());
        test.removeHorizontalSeam(test.findHorizontalSeam());
        StdOut.println(test.width() + " " + test.height());
    }

}
