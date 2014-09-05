// Code for the ValueCell.

import swing._
import Swing._

import java.awt.Color

class ValueCell extends BorderPanel {
  private val DefaultValue = 0

  private val value = new Label(DefaultValue.toString)

  border = LineBorder(Color.BLACK)

  layout += value -> BorderPanel.Position.Center

  def setValue(newValue: Int) {
    value.text = newValue.toString
  }
}
