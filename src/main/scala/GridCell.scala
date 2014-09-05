// Code for the GridCell.

import swing._
import Swing._

import java.awt.Color

class GridCell extends GridPanel(2,2) {
  private val DefaultAllocation = 0
  private val DefaultCost = 0

  private val allocation = new Label(DefaultAllocation.toString)
  private val linkFlowCost = new TextField(DefaultCost.toString)

  border = LineBorder(Color.BLACK)

  contents += new Label("") // Blank in the top-left corner.
  contents += linkFlowCost
  contents += allocation
  contents += new Label("") // Blank in the bottom-right corner.

  def getCost(): Int = linkFlowCost.text.toInt

  def setAllocation(value: Int) {
    allocation.text = value.toString
  }

}
