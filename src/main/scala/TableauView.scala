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

// Code for the TableauView.

import swing._
import Swing._

class TableauView(supplyCount: Int, demandCount: Int) extends BorderPanel {
  private val supplies = new SuppliesView(supplyCount)
  private val demands = new DemandsView(demandCount)

  private val ui = new UiView(supplyCount)
  private val vj = new VjView(demandCount)

  private val linkFlow = new GridView(supplyCount, demandCount)

  layout += supplies -> BorderPanel.Position.East
  layout += demands -> BorderPanel.Position.South
  layout += ui -> BorderPanel.Position.West
  layout += vj -> BorderPanel.Position.North
  layout += linkFlow -> BorderPanel.Position.Center

  def getDemands(): Array[Int] = demands.getDemands()

  def getLinkFlowCosts(): Array[Array[Int]] = linkFlow.getLinkFlowCosts()

  def getSupplies(): Array[Int] = supplies.getSupplies()

  def setAllocations(allocations: Array[Array[Int]]) {
    linkFlow.setAllocations(allocations)
  }

  def setUi(uiValues: Array[Int]) {
    ui.setUi(uiValues)
  }

  def setVj(vjValues: Array[Int]) {
    vj.setVj(vjValues)
  }
}
