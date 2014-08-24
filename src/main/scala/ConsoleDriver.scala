/*
 * Copyright (c) 2013-2014 Russell Andrew Edson
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// Code for the Console driver. (WIP)

import java.util.Scanner

object ConsoleDriver extends App {
  /** We define the width of each square of the printed grid.
    * This number is arbitrary -- larger widths can increase readability
    * if the allocations/costs are particularly large numbers.
    */
  val squareWidth = 10

  /** Prints the current transportation tableau to the screen in a nicely
    * formatted grid. The ui and vj values are specified separately above
    * the tableau, and the basic solution and current cost are printed 
    * below the tableau for easy inspection.
    *
    * @param tableau The transportation tableau to be printed to the screen.
    */
  def printTableau(tableau: TransportationTableau) {
    /* We print a dotted line to visually separate the current tableau
     * from any other tableaux that have been printed previously.
     * (Note: the '5' is chosen arbitrarily. This number doesn't really
     * affect anything -- it is purely for aesthetics.)
     */
    println("-" * (5 * squareWidth))

    /* Print the ui and vj dual variable values. */
    println("Ui: " + tableau.ui.mkString(", "))
    println("Vj: " + tableau.vj.mkString(", "))

    /* Get the tableau information needed to print the allocation grid. */
    val linkFlowCosts = tableau.linkFlowCosts
    val allocations = tableau.allocations

    /* We determine the horizontal size for the square grid so that
     * everything prints nicely.
     */
    val gridWidth = allocations(0).length * squareWidth + 1

    /* Print the link-flow cost and allocations matrix. */
    for (i <- (0 until allocations.length)) {
      println("\t" + ("-" * gridWidth))
      print("\t|")
      for (j <- (0 until linkFlowCosts(i).length)) {
        print(("%1$" + (squareWidth-1).toString + "s|")
            .format(linkFlowCosts(i)(j).toString))
      }
      println()
      print("\t|")
      for (j <- (0 until allocations(i).length)) {
        print(("%1$-" + (squareWidth-1).toString + "s|")
            .format(allocations(i)(j).toString))
      }
      println()
    }
    println("\t" + ("-" * gridWidth))

    /* We print the current basic solution at the bottom of the tableau. */
    println("Basic Solution: " + tableau.basicSolution.mkString(", "))

    /* We print the current cost at the bottom of the tableau. */
    println("Cost = " + tableau.cost().toString)
  }

  val input = new Scanner(System.in)

  /* Prompt for the number of supplies. */
  print("Enter the number of supplies: ")
  val suppliesNum = input.nextInt
  println()
  
  /* Obtain the supplies vector. */
  print("Enter the " + suppliesNum.toString + " supplies: ")
  val supplies = new Array[Int](suppliesNum)
  for (i <- (0 until supplies.length)) {
    supplies(i) = input.nextInt
  }
  println()

  /* Prompt for the number of demands. */
  print("Enter the number of demands: ")
  val demandsNum = input.nextInt
  println()

  /* Obtain the demands vector. */
  print("Enter the " + demandsNum.toString + " demands: ")
  val demands = new Array[Int](demandsNum)
  for (j <- (0 until demands.length)) {
    demands(j) = input.nextInt
  }
  println()

  /* Prompt for the link flow cost matrix. */
  print("Enter the link-flow costs: ")
  val costs = Array.ofDim[Int](suppliesNum, demandsNum)
  for (i <- (0 until costs.length); j <- (0 until costs(i).length)) {
    costs(i)(j) = input.nextInt
  }
  println()

  /* We balance the Transportation Problem if necessary by adding a 
   * fictitious supply or demand.
   */
  //TODO: balance everything.


  /* Instantiate the TransportationTableau class used to solve the problem,
   * and perform the North-west corner rule to start with.
   */
  val tableau = new TransportationTableau(supplies, demands, costs)
  tableau.northWestCornerRule()

  /* Print the first result tableau. */
  println("The initial tableau after the North-West corner rule:")
  printTableau(tableau)
  println()

  /* Keep printing tableaux until the optimal allocation has been
   * reached.
   */
  while (!tableau.isOptimal()) {
    val starPair = tableau.starPair
    println("Star pair: " + starPair.toString)

    val cycle = tableau.cycleTraversal(starPair)
    println("Cycle: " + cycle.mkString("->"))
    println()

    tableau.adjustAllocations(cycle)

    println("Next tableau:")
    printTableau(tableau)
    println()
  }

  /* We've reached the optimal cost here at the end. */
  println("Optimal cost reached: " + tableau.cost().toString)
}
