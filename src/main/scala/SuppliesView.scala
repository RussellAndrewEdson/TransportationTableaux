// Code for the SuppliesView.

import swing._
import Swing._

import java.awt.Color

class SuppliesView(length: Int) extends BoxPanel(Orientation.Vertical) {
  border = TitledBorder(LineBorder(Color.BLACK), "Supplies")

  private val supplies = new Array[EditableCell](length)
  for (i <- 0 until supplies.length) supplies(i) = new EditableCell()

  for (supplyCell <- supplies) contents += supplyCell

  def getSupplies(): Array[Int] = {
    // Error checking (here, or in EditableCell?)
    val supplyValues = new Array[Int](supplies.length)
    for (i <- 0 until supplyValues.length) {
      supplyValues(i) = supplies(i).getValue()
    }

    supplyValues
  }

}
