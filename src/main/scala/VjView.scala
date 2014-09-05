// Code for the VjView.

import swing._
import Swing._

import java.awt.Color

class VjView(length: Int) extends BoxPanel(Orientation.Horizontal) {
  border = TitledBorder(LineBorder(Color.BLACK), "Vj")

  private val vj = new Array[ValueCell](length)
  for (j <- 0 until vj.length) vj(j) = new ValueCell()

  for (vjCell <- vj) contents += vjCell

  def setVj(vjValues: Array[Int]) {
    // Error checking?
    for (j <- 0 until vj.length) {
      vj(j).setValue(vjValues(j))
    }

    // Might need to redraw.
  }
}
