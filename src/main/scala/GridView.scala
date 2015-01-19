/*
 * Copyright (c) 2013-2014, 2015 Russell Andrew Edson
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

import swing.{ Dimension, GridPanel }
import swing.Swing.{ LineBorder }

import java.awt.Color

/** A grid in the UI that contains the displayable cells for the link-flow
  * and allocations. The size of this grid corresponds directly to the
  * size of the Transportation problem to be solved (supplies x demands).
  *
  * @constructor Create a new grid of cells in the UI with the given number 
  *              of rows and columns.
  * @param rowCount The number of rows for the grid.
  * @param columnCount The number of columns for the grid.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.4
  */
class GridView(val rowCount: Int, val columnCount: Int) 
    extends GridPanel(rowCount, columnCount) {

  /** The indices for the displayed grid. */
  val indices = for {
      i <- 0 until rowCount
      j <- 0 until columnCount
    } yield (i,j)

  /* We surround the grid with a line border to distinguish it from the
   * rest of the tableau elements.
   */
  border = LineBorder(Color.BLACK)

  /* The cells are instantiated and placed in the grid one by one. */
  private val cells = Array.ofDim[GridCell](rowCount, columnCount)
  indices.foreach { p => cells(p._1)(p._2) = new GridCell() }
  indices.foreach { p => contents += cells(p._1)(p._2) }

  /** Clears the cycle from the cell view (if it exists.)
    *
    * We call this method once we no longer need the cycle to be displayed.
    */
  def clearCycle(): Unit = {
    indices.foreach { p => cells(p._1)(p._2).clearCycle() }
  }

  /** Clears the star pair from the cell view (if one exists.)
    *
    * We call this method once we no longer need to show the star pair.
    */
  def clearStarPair(): Unit = {
    indices.foreach { p => cells(p._1)(p._2).clearStarPair() }
  }

  /** Returns the size dimensions of the cells contained in the 
    * grid view.
    *
    * @return The dimension of the cells contained in the view.
    */
  def getCellSize(): Dimension = cells(0)(0).size

  /** Returns a 2-dimensional array containing the user-supplied values 
    * for the link-flow costs.
    *
    * (This method is provided for convenience; the output can be
    * immediately used to instantiate a TransportationTableau object.)
    *
    * @return A 2D array of integer values for the link-flow costs.
    */
  def getLinkFlowCosts(): Array[Array[Int]] = {
    // Error checking?
    val linkFlowCosts = Array.ofDim[Int](rowCount, columnCount)
    indices.foreach { 
      p => linkFlowCosts(p._1)(p._2) = cells(p._1)(p._2).getCost()
    }

    linkFlowCosts
  }

  /** Sets the displayed allocations for the grid to the values in the
    * given 2-dimensional array.
    *
    * (This method is provided for convenience; the input can simply be
    * the output of the .allocations() method of the current 
    * TransportationTableau object.)
    *
    * @param allocations A 2-dimensional integer array of allocation values.
    */
  def setAllocations(allocations: Array[Array[Int]]): Unit = {
    // Error checking?
    indices.foreach {
      p => cells(p._1)(p._2).setAllocation(allocations(p._1)(p._2))
    }
  }

  /** Displays the given cycle in the tableau grid.
    *
    * We call this method when we need the cycle displayed in the tableau.
    * For convenience, it takes a list of tuples as its argument to line up
    * exactly with the output of the .cycleTraversal() method of the 
    * TransportationTableau class.
    *
    * @param cycle The cycle to be displayed in the grid.
    */
  def setCycle(cycle: List[Tuple2[Int, Int]]): Unit = {
    val plusPairs =  cycle.zipWithIndex filter { _._2 % 2 == 0 } map { _._1 }
    val minusPairs = cycle.zipWithIndex filter { _._2 % 2 != 0 } map { _._1 }

    plusPairs.foreach  { p => cells(p._1)(p._2).setCycle("+") }
    minusPairs.foreach { p => cells(p._1)(p._2).setCycle("-") }
  }

  /** Sets the link-flow costs for the grid to the values in the given
    *  2-dimensional array.
    * 
    * @param costs A 2-dimensional integer array of link-flow costs.
    */
  def setLinkFlowCosts(costs: Array[Array[Int]]): Unit = {
    // Error checking?
    indices.foreach {
      p => cells(p._1)(p._2).setCost(costs(p._1)(p._2))
    }
  }

  /** Sets the given star pair in the grid (ie. labels it with a "*".)
    *
    * We call this method when we need to set the star pair. For convenience,
    * it takes a tuple argument -- this can immediately be the output of the
    * .starPair() method of the TransportationTableau class.
    *
    * @param pair The (star) pair to be labelled with a "*" in the grid.
    */
  def setStarPair(pair: Tuple2[Int, Int]): Unit = {
    cells(pair._1)(pair._2).setStarPair()
  }

}
