// Code for the DemandsView.

import swing._
import Swing._

import java.awt.Color

class DemandsView(length: Int) extends BoxPanel(Orientation.Horizontal) {
  border = TitledBorder(LineBorder(Color.BLACK), "Demands")

  private val demands = new Array[EditableCell](length)
  for (j <- 0 until demands.length) demands(j) = new EditableCell()

  for (demandCell <- demands) contents += demandCell

  def getDemands(): Array[Int] = {
    // Error checking here, or in EditableCell?
    val demandValues = new Array[Int](demands.length)
    for (j <- 0 until demandValues.length) {
      demandValues(j) = demands(j).getValue()
    }

    demandValues
  }
}
