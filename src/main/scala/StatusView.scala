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

import swing.{ BorderPanel, Dimension, TextArea }
import swing.Swing.{ LineBorder, TitledBorder }

import java.awt.Color

/** A status pane for the GUI.
  * 
  * This view is basically a text field that shows the current tableau
  * status, basic solution, and optimality for the problem at each stage.
  * An initial welcome greeting and brief "how-to" are also shown here 
  * when the program starts.
  * 
  * @constructor Create a new StatusPane.
  * 
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.1
  */
class StatusView extends BorderPanel {

  /** We show a welcome greeting when the program is first started. */
  private val welcomeGreeting =
    """Welcome to the TransportationTableaux program!
      |
      |Enter the supplies for the problem in the Supply column,
      |and the demands in the Demands column. The link-flow costs
      |are then entered in the top-right corner of the cells in
      |the tableau grid. (For your convenience, you can TAB across
      |the cells left-to-right.)
      |
      |If you need a different problem size (ie. more/less supplies
      |and demands), you can resize the problem by entering the 
      |new dimensions and clicking "Resize".
      |
      |Once you're done entering the values, simply hit "Solve" to
      |solve the problem outright, or you can use the "Step" button
      |to move through the entire solution process. Have fun!""".stripMargin

  /* We surround the pane with a border to separate it from the other
   * GUI elements on the left-hand side.
   */
  border = TitledBorder(LineBorder(Color.BLACK), "Status")

  private val statusText = new TextArea(welcomeGreeting)
  statusText.editable = false
  statusText.minimumSize = new Dimension(400, 300)
  statusText.maximumSize = new Dimension(400, 300)
  statusText.preferredSize = new Dimension(400, 300)
  layout += statusText -> BorderPanel.Position.Center

  /** Appends the given text to the status TextField, spacing
    * it out with newlines.
    */
  private def appendText(textString: String): Unit = {
    statusText.text += "\n" + textString
  }

  /** Updates the one-liner status for the current tableau.
    * 
    * @param status The TableauStatus to be used for the update.
    */
  def updateStatus(status: TableauStatus.Value): Unit = {
    appendText("status goes here")


    //val Initial             = Value(" Enter the supplies, demands and costs. ")
    //val NorthWestCorner     = Value(" Finished North-West Corner Rule.       ")
    //val FoundStarPair       = Value(" Located star (*) pair (ui+vj >= cij).  ")
    //val ConstructedCycle    = Value(" Constructed cycle with star pair.      ")
    //val AdjustedAllocations = Value(" Adjusted allocations along the cycle.  ")
    //val OptimalSolution     = Value(" Optimal solution reached.              ")
    //val UnbalancedProblem   = Value(" Problem is unbalanced; can't solve.    ")
    //val DoneBalancing       = Value(" Problem is now balanced.               ")
  }

  /** Updates the basic solution for the current tableau.
    * 
    * @param basicSolution The basic pairs for the current solution.
    * @param allocations The current tableau allocations.
    */
  def updateSolution(
      basicPairs: List[Tuple2[Int, Int]],
      allocations: Array[Array[Int]]): Unit = {
    appendText("solution gets listed here")
  }

  /** Updates the calculated cost for the current tableau.
    * 
    * @param cost The cost for the current tableau.
    */
  def updateCost(cost: Int): Unit = {
    appendText("cost goes here")
  }

  /** Updates the displayed optimality state for the tableau.
    * 
    * @param optimal A boolean value for the optimality of the tableau.
    */
  def updateOptimality(optimal: Boolean): Unit = {
    appendText("Optimality goes here.")
  }

}
