Programming Assignment 6: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
We use dynamic programming to find a horizontal/vertical seam. We keep two
arrays, one for the weights and the other for the path. First, we initialize the
last row of the weights array to the energy of each pixel. Then, we recursively
go up by one row and chose the minimum weight among the three pixels in the row
below for each pixel. At the same time, we kept track of the pixel we choose in
the path array. When we get to the first row, we choose the smallest weight and
follow the path array to get the horizontal/vertical seam.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
An image would be suitable if it has a good contrast between important
rows/columns and the other rows/columns. If the energy in some rows/columns are
clearly higher than others, that means even if you remove other rows/columns,
that does not change the content of the image. Therefore, it would be difficult
to remove rows/columns from an image that does not have much of a contrast but
has important details. For example, a portrait would be difficult to work with
since it is easier for people to notice the details that may not have high
energy.

/* *****************************************************************************
 *  NOTE: This question is optional and worth 1 point of extra credit!!!
 *
 *  The removeHorizontalSeam() method you implemented probably takes O(WH) time,
 *  but could it be efficient? (W is the width of the current picture,
 *  and H the height).

 *  For this particular question, suppose you only want to implement the
 *  SeamCarver() constructor, as well as the picture() and
 *  removeHorizontalSeam() methods, so ignore the other methods you
 *  implemented in the assignment. Can you implement them in such a way that
 *  SeamCarver() and Picture() take O(WH) time, but removeHorizontalSeam()
 *  takes time that is asymptotically less than O(WH) (so O(W + H) or
 *  O(WlogH + HlogW) would both be allowed)?
 *
 *  Describe your algorithm and any data structures you use and state the
 *  runtime of removeHorizontalSeam(). You don't have to write any code.
 **************************************************************************** */
In the constructor, instead of the current implementation,
we can use nodes to represent the pixel instead of an array. The node will have
the nodes to the pixel directly above you, the adjacent two pixels, and the
pixel directly below you and color. Also, keep track of the height and width of
the current picture. In addition, store the first nodes of each row as instance
variables. This will only take O(WH) as there are only WH pixels.
When removing horizontal seam, you have to update the new energy and nodes.
Update the adjacent nodes and energy of the pixels takes O(H) as there are 2H
pixels to update.  Then, update the height and width in constant time. Also,
update the first nodes of each row if needed, which can take O(H) time.
When calling picture(), you can construct an empty picture object with the
width and height in the instance variables. Then, follow nodes to the adjacent
pixels to extract the colors.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
We enjoyed doing this.
