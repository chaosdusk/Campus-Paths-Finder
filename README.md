# Campus-Paths-Finder

This is a class project from CSE 331: Software Design and Implementation

Campus Paths Finder is an app that allows users to select two buildings on the UW campus and displays the shortest path between the buildings. The app itself is written in Java.

![demo1](https://github.com/chaosdusk/Campus-Paths-Finder/blob/master/demo%20files/demo%201.PNG)

1. The starting location
2. The ending location
3. Displays the path on the map
4. Deletes the displayed path, if any

Here we will select the Bagley Building's NE entrance (BAG (NE)) and the Fishery Sciences Building (FSH).

![demo2](https://github.com/chaosdusk/Campus-Paths-Finder/blob/master/demo%20files/demo%202.PNG)

Now as you can see the displayed path is quite small, so let's zoom in a bit.

![demo3](https://github.com/chaosdusk/Campus-Paths-Finder/blob/master/demo%20files/demo%203.PNG)

Here we can see the displayed shortest path on the UW campus map. The blue circles are the buildings and the red line is the shortest path from BAG to FSH.

# Implementation

The Model of the app is the campus representation, backed by a directed, acyclic graph. Each node in the graph has a Location, some nodes are repsentations of buildings but most are not. Each edge is a straight path that directs one Location to another, and has a label representing the Euclidean distance of the path. A seperate Map keeps track of the locations that are actually buildings. 
When two buildings are selected, the shortest path is calculated using Dijkstra's Algorithm and a List of intermediate locations are returned, where each entry in the List is a tuple of (starting Coordinate, ending Coordinate, Euclidean distance). This List is then displayed as a path by the View.

The View/Controller of the app handles displaying the path to the user as well as accepting user commands to make changes to the View. This app is specifically tailored to the display dimensions of Nexus 5 and may not properly display the path on other devices. 
The Controller accepts user inputs on starting building, ending building, show path, and reset path. When the user has two buildings selected and clicks show path, the shortest path algorithm in the Model is called and the View displays the returned List of tuples on the map, in the form of a path.

# Tests

This app includes a full unit testing suite, including specification tests and implementation tests.

# How to use?

As with any Android source code, this app can be run by viewing the source code in Android Studio and then run on either an emulator or a physical device.
