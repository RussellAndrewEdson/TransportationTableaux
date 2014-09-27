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

import swing.{ Dimension, GridPanel, Label, TextField }
import swing.Swing.LineBorder
import swing.event.FocusGained

import java.awt.Color

/** A displayable cell for the allocation grid of the Transportation tableau
  * view. The cell contains a current allocation value in the bottom-left, and
  * a link-flow cost in the top-right.
  *
  * @constructor Create a new grid cell with a default allocation (0) and 
  *              link-flow cost (0).
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.3
  */
class GridCell extends GridPanel(2, 2) {

  /** The default allocation value for a new cell is 0. */
  private val DefaultAllocation = 0

  /** The default cost value for a new cell is 0. */
  private val DefaultCost = 0

  /** The allocation for the grid cell is displayed in the bottom-left. */
  private val allocation = new Label(DefaultAllocation.toString)

  /** We use the top-left corner of the cell to signify the star-pair. */
  private val star = new Label("")

  /** We use the bottom-right corner of the cell to signal plus/minus. */
  private val plusMinus = new Label("")

  /** The link-flow cost for this cell is displayed in the top-right corner.
    * The user supplies this value during the problem definition, so it is
    * editable (at least to start with.)
    */
  private val linkFlowCost = new TextField(DefaultCost.toString)

  /* Cell embellishments. We have a line border to highlight the boundaries
   * of the cell, and the size of the cell is fixed in the grid.
   * TODO: The sizing may change later on if required, but for now the 
   * program seems to work best when the cells all have the same shape. 
   */
  border = LineBorder(Color.BLACK)
  minimumSize   = new Dimension(60, 40)
  maximumSize   = new Dimension(60, 40)
  preferredSize = new Dimension(60, 40)

  /* The cell is "blank" in the top-left and bottom-right corners (currently
   * we will use an empty label as a placeholder). The link-flow cost is
   * displayed in the top-right, and the allocation is displayed in the 
   * bottom-left.
   */
  contents += star            // Top-left
  contents += linkFlowCost    // Top-right
  contents += allocation      // Bottom-left
  contents += plusMinus       // Bottom-right

  /* When the text-field gains focus, we highlight the existing text so it
   * can immediately be overwritten (to be user-friendly.)
   */
  listenTo(linkFlowCost)
  reactions += {
    case FocusGained(_, _, _) => linkFlowCost.selectAll()
  }

  /** Clears the  "+" or "-" symbol used to denote this cell as
    * part of the current cycle.
    *
    * (We call this method when we no longer need to signal the cycle in the
    * tableau.)
    */
  def clearCycle(): Unit = {
    plusMinus.text = ""
  }

  /** Clears the star "*" symbol used to denote a star pair.
    *
    * (ie. We call this method when we no longer need to signal that this cell
    * is the star pair. The "*" is removed from the cell in the GUI.)
    */
  def clearStarPair(): Unit = {
    star.text = ""
  }

  /** Returns the current value of the link-flow cost assigned to this cell.
    *
    * @return The value of the link-flow cost.
    */
  def getCost(): Int = linkFlowCost.text.toInt

  /** Sets the allocation for this cell to the given integer value.
    *
    * @param value The new value for the allocation.
    */
  def setAllocation(value: Int): Unit = {
    allocation.text = value.toString
  }

  /** Labels this cell with a "+" or "-" to denote that it is part of 
    * the current tableau cycle.
    *
    * (We use this method to indicate that individual cells are part of the
    * cycle.)
    *
    * @param symbol The string to display in the bottom-right corner of the
    *               cell -- we use "+" and "-" to label the cycle pairs.
    */
  def setCycle(symbol: String): Unit = {
    plusMinus.text = symbol
  }

  /** Sets the star "*" symbol used to denote a star pair.
    *
    * (ie. We call this method when we need to signal that this cell is the
    * star pair. A "*" is placed in the top-left of this cell in the GUI.)
    */
  def setStarPair(): Unit = {
    star.text = "*"
  }

}
