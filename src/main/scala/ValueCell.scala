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

import swing._
import Swing._

import java.awt.Color

/** A displayable cell used for the cells of the ui and vj dual
  * variable lists. The cell contains a single uneditable value.
  *
  * @constructor Create a new cell with a default value (0).
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.1
  */
class ValueCell extends BorderPanel {

  /** The default (ui/vj) value for a new cell is 0. */
  private val DefaultValue = 0

  /** The value of this cell is displayed in the center. */
  private val value = new Label(DefaultValue.toString)

  /* Cell embellishments. We have a line border to highlight the boundaries
   * of the cell, and the size of the cell is fixed in the list.
   * TODO: The sizing may change later on if required, but for now the 
   * program seems to work best when the cells all have the same shape.
   */
  border = LineBorder(Color.BLACK)
  minimumSize = new Dimension(60,40)
  //maximumSize = new Dimension(60,40)
  preferredSize = new Dimension(60,40)

  /* The cell contains only the single value, displayed in the center. */
  layout += value -> BorderPanel.Position.Center

  /** Sets the displayed value for this cell to the given integer.
    *
    * @param newValue The new value for the cell to display.
    */
  def setValue(newValue: Int): Unit = {
    value.text = newValue.toString
  }

}
