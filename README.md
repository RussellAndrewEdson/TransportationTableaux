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

19/11 Update
------------

Finally got around to finishing up that last test example, and changed the repository structure a bit:
 - the User Guide has been moved to a separate folder, and the TeX files/images for the PDF have been uploaded too.
 - The binary file for V0.3 has been removed from the repository -- it's still available in the release, but this way cloning/downloading the repository doesn't need to deal with copying the massive .jar file across anymore.

I'm pretty happy with this project at this point. The one last thing to be added in terms of functionality is for the tableau to check whether it's balanced and have a popup message to the user if it isn't (which will give the user the option to automatically balance it by adding fictitious supplies/demands, or -- the more likely scenario -- allow the user to go back and change something if the imbalance was unintentional.) This shouldn't take too long once I get around to it.

And obviously there's some fixing to do:
 - There's an aesthetic bug where the tableau resizes itself weirdly when the 'Step'/'Solve' button is first clicked and the Basic solution text appears. This doesn't affect functionality, but it looks a bit ugly where the textboxes/labels at the bottom of the left and right columns appear drawn over the boundary. Hopefully this can be remedied without having to -fix- the sizes of the tableau components.
 - Sometimes when resizing you need to click twice to get the window to resize itself properly. This obviously needs to be changed if possible.
 - There is some code clean-up I want to do: the CycleView class has some unnecessary duplication where the branching for the drawing is concerned, and the UiView/VjView and SupplyView/DemandView components should really be refactored into single classes that are parameterised by the text label (or even a single class for all 4 that is parameterised by both the label as well as the contained textboxes/labels, though this might be tricky to get the getter/setter methods chosen properly.) 
