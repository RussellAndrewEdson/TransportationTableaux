TransportationTableaux
======================

Solves a given Transportation Problem in Operations Research step-by-step!  
Designed for teaching/demonstration purposes.

![The GUI for the program.](https://raw.githubusercontent.com/RussellAndrewEdson/TransportationTableaux/master/transportationtableaux_v3screenshot.png "Solving a transportation problem with the GUI")

The program has finally been released!

At this point, the program is completely functional.
A tutorial on how to use the program to solve a Transportation problem can be found in the User Guide.

For ease of use, an executable .jar file has been assembled that can be run in any Java Runtime Environment.
(All the Scala core libraries are bundled in as well -- convenient, though it does make the file quite hefty at ~8MB.)

Those that would like to play around with the source code are encouraged to use the Scala Build Tool (http://www.scala-sbt.org/), which easily retrieves all of the necessary Scala libraries and allows for the interfaces and tests to be run straight from the command line.

19/01 Update
------------

With the addition of the problem-balancing check, the program is finished in terms of functionality; it does everything that I wanted it to do. Of course there's some testing to be done to make sure I haven't broken anything with the update, but other than that, all that's left is housekeeping:
 - There's an aesthetic bug where the tableau resizes itself weirdly when the 'Step'/'Solve' button is first clicked and the Basic solution text appears. This doesn't affect functionality, but it looks a bit ugly where the textboxes/labels at the bottom of the left and right columns appear drawn over the boundary. Hopefully this can be remedied without having to -fix- the sizes of the tableau components.
 - Sometimes when resizing you need to click twice to get the window to resize itself properly. This obviously needs to be changed if possible.
