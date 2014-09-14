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

/** A displayed list of the vj dual variables for the tableau.
  * The size of this list corresponds directly to the number of
  * demands for the Transportation problem.
  *
  * @constructor Create a new displayed list in the UI with the
  *              given number of cells.
  * @param cellCount The number of cells for the list.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.1
  */
class VjView(val cellCount: Int) 
    extends BoxPanel(Orientation.Horizontal) {

  /** The indices for the displayed list. */
  val indices = for { j <- 0 until cellCount } yield j

  /* We surround the list of vj dual variables with a border,
   * and a title that easily indicates the list's purpose.
   */
  border = TitledBorder(LineBorder(Color.BLACK), "Vj")

  /* The cells are instantiated and placed in the list one by one. */
  private val cells = new Array[ValueCell](cellCount)
  indices.foreach { j => cells(j) = new ValueCell() }
  indices.foreach { j => contents += cells(j) }

  /** Sets the displayed vj values to those in the given array.
    *
    * (This method is provided for convenience; the input can simply be
    * the output of the .vj() method of the current TransportationTableau
    * object.)
    *
    * @param vjValues An array for the vj values.
    */
  def setVj(vjValues: Array[Int]): Unit = {
    // Error checking?
    indices.foreach { j => cells(j).setValue(vjValues(j)) }
  }

}
