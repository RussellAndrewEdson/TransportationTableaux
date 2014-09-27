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

import swing.{ BorderPanel }

import java.awt.{ BasicStroke, Color, geom, Graphics2D, Point }

/** The visual representation of the cycle for the Transportation tableau.
  *
  * This view draws itself on top of the grid in the tableau view. When the
  * cycle is drawn, the grid is still visible beneath the lines of the cycle.
  *
  * @constructor Create a new CycleView to draw itself on top of the given
  *              grid in the tableau view.
  * @param grid The grid that this cycle will be drawn on.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.1
  */
class CycleView(val grid: GridView) extends BorderPanel {

  /* We flag for whether or not the cycle should be shown on the screen. */
  private var paintCycle: Boolean = false

  /* We store the cycle locally so we can paint it when required. */
  private var storedCycle: List[Tuple2[Int, Int]] = List()

  /* The grid is placed behind the drawn (or not-drawn) cycle. */
  layout += grid -> BorderPanel.Position.Center

  /** Returns the midpoint of the cell with the given coordinates in the
    * grid view (so we can draw the cycle from the middle of each of its
    * included cells.)
    *
    * @param cell The cell to retrieve the midpoint for.
    * @return The midpoint of the cell.
    */
  private def getCellMidpoint(cell: Tuple2[Int, Int]): Point = {
    val cellSize = grid.getCellSize()
    return new Point( 
        (cellSize.width  / 2) + (cellSize.width  * cell._2) , 
        (cellSize.height / 2) + (cellSize.height * cell._1) )
  }

  /** Hides the cycle from view if it has been drawn.
    *
    * (That is, we call this method when we don't want the cycle to
    * be viewed anymore.)
    */
  def hideCycle(): Unit = {
    paintCycle = false
    repaint()
  }

  /** @inheritdoc
    *
    * In our case, we paint the cycle over the top of the grid view if
    * requested.
    */
  override def paint(g: Graphics2D): Unit = {
    super.paint(g)

    /* We draw the cycle to the screen here if it is to be shown. */
    if (paintCycle == true) {
      for(i <- 0 until storedCycle.length) {
        if ((i+1) == storedCycle.length) {
          val cellA = getCellMidpoint( storedCycle(i) )
          val cellB = getCellMidpoint( storedCycle(0) )
          g.setColor(Color.BLUE)
          g.setStroke(new BasicStroke(2))
          g.drawLine( cellA.x, cellA.y, cellB.x, cellB.y )
        }
        else {
          val cellA = getCellMidpoint( storedCycle(i) )
          val cellB = getCellMidpoint( storedCycle(i+1) )
          g.setColor(Color.BLUE)
          g.setStroke(new BasicStroke(2))
          g.drawLine( cellA.x, cellA.y, cellB.x, cellB.y )
        }
      }
    }
  }

  /** Draws the given cycle on top of the grid in the tableau view.
    *
    * @param cycle The cycle to be drawn on the grid.
    */
  def showCycle(cycle: List[Tuple2[Int, Int]]): Unit = {
    storedCycle = cycle
    paintCycle = true
    repaint()
  }

}

