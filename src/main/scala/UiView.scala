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

import swing.{ BoxPanel, Orientation }
import swing.Swing.{ LineBorder, TitledBorder }

import java.awt.Color

/** A displayed list of the ui dual variables for the tableau.
  * The size of this list corresponds directly to the number of
  * supplies to the Transportation problem.
  *
  * @constructor Create a new displayed list in the UI with the
  *              given number of cells.
  * @param cellCount The number of cells for the list.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.2
  */
class UiView(val cellCount: Int) 
    extends BoxPanel(Orientation.Vertical) {

  /** The indices for the displayed list. */
  val indices = for { i <- 0 until cellCount } yield i

  /* We surround the list of ui dual variables with a border,
   * and a title that easily indicates the list's purpose.
   */
  border = TitledBorder(LineBorder(Color.BLACK), "Ui")

  /* The cells are instantiated and placed in the list one by one. */
  private val cells = new Array[ValueCell](cellCount)
  indices.foreach { i => cells(i) = new ValueCell() }
  indices.foreach { i => contents += cells(i) }

  /** Sets the displayed ui values to those in the given array.
    *
    * (This method is provided for convenience; the input can simply be
    * the output of the .ui() method of the current TransportationTableau
    * object.)
    *
    * @param uiValues An array for the ui values.
    */
  def setUi(uiValues: Array[Int]): Unit = {
    // Error checking?
    indices.foreach { i => cells(i).setValue(uiValues(i)) }
  }

}
