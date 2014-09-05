// Code for the EditableCell.

import swing._
import Swing._

import java.awt.Color

class EditableCell extends BorderPanel {
  private val DefaultValue = 0

  private val value = new TextField(DefaultValue.toString)

  border = LineBorder(Color.BLACK)

  layout += value -> BorderPanel.Position.Center

  def getValue(): Int = value.text.toInt

}
