// Code for the UiView.

import swing._
import Swing._

import java.awt.Color

class UiView(length: Int) extends BoxPanel(Orientation.Vertical) {
  border = TitledBorder(LineBorder(Color.BLACK), "Ui")

  private val ui = new Array[ValueCell](length)
  for (i <- 0 until ui.length) ui(i) = new ValueCell()

  for (uiCell <- ui) contents += uiCell

  def setUi(uiValues: Array[Int]) {
    // Error checking?
    for (i <- 0 until ui.length) {
      ui(i).setValue(uiValues(i))
    }

    // Might need to repaint the ui view.
  }
}
