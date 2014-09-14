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

import swing._
import Swing._

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
  * @version 0.1
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
  def setAllocations(allocations: Array[Array[Int]]) {
    // Error checking?
    indices.foreach {
      p => cells(p._1)(p._2).setAllocation(allocations(p._1)(p._2))
    }
  }

}
