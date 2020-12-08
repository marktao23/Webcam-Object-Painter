# Webcam-Object-Painter

The goal of this assignment is to produce a webcam-based painting program in which
the region on which the mouse is clicked acts as a sort of paintbrush. When clicked on
an object in the frame, it recolors that object as a consistent shade of that RGB value
of whatever pixel the mouse was clicked on.

RegionFinder basically "searches" the image, and finds the given region that should be colored
in by evaluating the values of the colors in the neighboring pixels. CamPaint has several functions,
one of which actually paints the image, as well as undoes the paint, and there's even a screenshot function.
