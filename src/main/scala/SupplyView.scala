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

/** A displayed collection for the supply input values of the tableau.
  * The user determines the size of this list when specifying the problem,
  * and the values for the supply are entered into the text fields.
  *
  * @constructor Create a new displayed list in the UI with the given
  *              number of cells.
  * @param cellCount The number of cells for the list.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.2
  */
class SupplyView(val cellCount: Int) 
    extends BoxPanel(Orientation.Vertical) {

  /** The indices for the displayed list. */
  val indices = for { i <- 0 until cellCount } yield i

  /* We surround the list of supply text fields with a border,
   * and a title that easily indicates the list's purpose.
   */
  border = TitledBorder(LineBorder(Color.BLACK), "Supply")

  /* The cells are instantiated and placed in the display list one by one. */
  private val cells = new Array[EditableCell](cellCount)
  indices.foreach { i => cells(i) = new EditableCell() }
  indices.foreach { i => contents += cells(i) }

  /** Returns an array containing the entered values for the supplies.
    *
    * (This method is provided for convenience; the output of this method
    * can be piped straight into the constructor for the new
    * TransportationTableau object.)
    *
    * @return An array containing the user-provided supply values.
    */
  def getSupplies(): Array[Int] = {
    // Error checking (here, or in EditableCell?)
    val supplies = new Array[Int](cellCount)
    indices.foreach { i => supplies(i) = cells(i).getValue() }

    supplies
  }

}
