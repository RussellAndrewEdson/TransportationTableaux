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
