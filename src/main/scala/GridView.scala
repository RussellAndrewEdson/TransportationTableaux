// Code for the GridView.

import swing._
import Swing._

import java.awt.Color

class GridView(rowCount: Int, columnCount: Int) 
    extends GridPanel(rowCount, columnCount) {
  
  border = LineBorder(Color.BLACK)

  private val linkFlow = Array.ofDim[GridCell](rowCount, columnCount)
  for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
    linkFlow(i)(j) = new GridCell()
  }

  for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
    contents += linkFlow(i)(j)
  }

  def getLinkFlowCosts(): Array[Array[Int]] = {
    // Error checking?
    val linkFlowCosts = Array.ofDim[Int](linkFlow.length, linkFlow(0).length)
    for (i <- 0 until linkFlowCosts.length) {
      for (j <- 0 until linkFlowCosts(i).length) {
        linkFlowCosts(i)(j) = linkFlow(i)(j).getCost()
      } 
    }

    linkFlowCosts
  }

  def setAllocations(allocations: Array[Array[Int]]) {
    // Error checking?
    for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
      linkFlow(i)(j).setAllocation(allocations(i)(j))
    }

    // Might need to redraw the view here.
  }
}
