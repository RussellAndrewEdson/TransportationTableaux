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

/** The visual representation of the Transportation Tableau in the GUI.
  *
  * This view displays the main allocation grid, surrounded by the lists
  * containing the supplies, demands, and ui/vj dual variables. The user
  * sets the problem specifications through this view, and the results of the
  * problem solution at each stage can be displayed through this view.
  * 
  * @constructor Create a new TableauView with the given number of supplies
  *              and demands.
  * @param supplyCount The number of supplies for the tableau.
  * @param demandCount The number of demands for the tableau.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.2
  */
class TableauView(val supplyCount: Int, val demandCount: Int) 
    extends BorderPanel {

  /* We define a spacer so that we can align the demands list and the
   * vj variables list in an aesthetically pleasing way.
   */
  private val SpacerDimensions = new Dimension(70, 40)
  private def spacer = new Label() {
    minimumSize   = SpacerDimensions
    maximumSize   = SpacerDimensions
    preferredSize = SpacerDimensions
  }

  /* The supplies and demands are displayed in their own lists. */
  private val supplies = new SupplyView(supplyCount)
  private val demands = new DemandView(demandCount)

  /* The ui and vj dual variables are displayed in their own lists. */
  private val ui = new UiView(supplyCount)
  private val vj = new VjView(demandCount)

  /* The link-flow allocation grid is displayed in the center. */
  private val linkFlow = new GridView(supplyCount, demandCount)

  /* We align everything properly in the border panel. */
  layout += supplies -> BorderPanel.Position.East
  layout += ui -> BorderPanel.Position.West
  layout += linkFlow -> BorderPanel.Position.Center

  /* The demands view is spaced out so that it aligns more with
   * the link-flow grid boundaries in the center.
   */
  layout += new BorderPanel {
    layout += spacer -> BorderPanel.Position.West
    layout += demands -> BorderPanel.Position.Center
    layout += spacer -> BorderPanel.Position.East
  } -> BorderPanel.Position.South

  /* The vj variable view is spaced out so that it aligns more with
   * the link-flow grid boundaries in the center.
   */
  layout += new BorderPanel {
    layout += spacer -> BorderPanel.Position.West
    layout += vj -> BorderPanel.Position.Center
    layout += spacer -> BorderPanel.Position.East
  } -> BorderPanel.Position.North

  /** Clears the star pair from the tableau grid view (if it exists.)
    *
    * This method allows us to "clean up" the tableau after the star
    * pair has been used to adjust the allocations already.
    */
  def clearStarPair(): Unit = {
    linkFlow.clearStarPair()
  }

  /** Returns an array containing the values for the demand.
    *
    * (This method is designed for its output to be piped straight into
    * the constructor for the TransportationTableau to solve the problem.)
    *
    * @return An integer array containing the demand values.
    */
  def getDemands(): Array[Int] = demands.getDemands()

  /** Returns a 2-dimensional array containing the link-flow costs.
    *
    * (This method is designed for its output to be piped straight into
    * the constructor for the TransportationTableau to solve the problem.)
    *
    * @return A 2D array containing the link-flow costs.
    */
  def getLinkFlowCosts(): Array[Array[Int]] = linkFlow.getLinkFlowCosts()

  /** Returns an array containing the values for the supply.
    *
    * (This method is designed for its output to be piped straight into
    * the constructor for the TransportationTableau to solve the problem.)
    *
    * @return An integer array containing the supply values.
    */
  def getSupplies(): Array[Int] = supplies.getSupplies()

  /** Sets the displayed allocation values to those in the given array.
    *
    * (This method is designed so that the allocations array of the 
    * TransportationTableau instance can be sent to this method at any stage
    * and have its current values displayed.)
    *
    * @param allocations The 2D array containing the new allocation values.
    */
  def setAllocations(allocations: Array[Array[Int]]): Unit = {
    linkFlow.setAllocations(allocations)
  }

  /** Sets the star pair as the given pair in the grid view.
    *
    * For convenience, this method takes in a tuple argument; the output of
    * the .starPair() method of the TransportationTableau can be piped straight
    * into this method.
    *
    * @param pair The pair that will be shown as the star pair.
    */
  def setStarPair(pair: Tuple2[Int, Int]): Unit = {
    linkFlow.setStarPair(pair)
  }

  /** Sets the displayed ui dual variables to those in the given array.
    *
    * (This method is designed so that the ui array of the
    * TransportationTableau instance can be sent to this method at any stage
    * and have its current values displayed.)
    *
    * @param uiValues An integer array containing the new ui values.
    */
  def setUi(uiValues: Array[Int]): Unit = {
    ui.setUi(uiValues)
  }

  /** Sets the displayed vj dual variables to those in the given array.
    *
    * (This method is designed so that the vj array of the
    * TransportationTableau instance can be sent to this method at any stage
    * and have its current values displayed.)
    *
    * @param vjValues An integer array containing the new vj values.
    */
  def setVj(vjValues: Array[Int]): Unit = {
    vj.setVj(vjValues)
  }

}
