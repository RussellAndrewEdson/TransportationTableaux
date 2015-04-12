/*
 * Copyright (c) 2013-2014, 2015 Russell Andrew Edson
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

/** A displayed list of values for the tableau (that are edited
  * programmatically.) 
  * 
  * @constructor Create a new displayed list in the UI with the given
  *              number of cells and orientation, with the title appearing
  *              above the list in the display.
  * @param cellCount The number of cells for the list.
  * @param orientation The orientation (vertical, horizontal) for the list.
  * @param title The title to appear with the list in the UI.
  * 
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.1
  */
class ValueListView(
    val cellCount: Int,
    val orientation: Orientation.Value,
    val title: String)
    extends BoxPanel(orientation) {

  /** The indices for the displayed list. */
  val indices = for { i <- 0 until cellCount } yield i

  /* We surround the list with a border and a title that easily indicates
   * the list's purpose.
   */
  border = TitledBorder(LineBorder(Color.BLACK), title)

  /* The cells are all instantiated and placed in the list. */
  private val cells = new Array[ValueCell](cellCount)
  indices.foreach { i => cells(i) = new ValueCell() }
  indices.foreach { i => contents += cells(i) }

  /** Sets the displayed values to those in the given array.
    * 
    * (This method is provided for convenience: the input can simply be
    * the output of the .ui() or .vj() methods of the current
    * TransportationTableau object.)
    * 
    * @param values An array for the values to be displayed.
    */
  def setValues(values: Array[Int]): Unit = {
    indices.foreach { i => cells(i).setValue(values(i)) }
  }

}
