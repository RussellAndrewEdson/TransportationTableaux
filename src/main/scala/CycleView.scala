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

// Code for the CycleView.

import swing._
import Swing._

import java.awt.{Color, Graphics2D, Point, BasicStroke, geom}

class CycleView(val grid: GridView) extends BorderPanel {

  private var paintCycle: Boolean = false
  private var storedCycle: List[Tuple2[Int, Int]] = List()

  opaque = false
  layout += grid -> BorderPanel.Position.Center

  private def getCellMidpoint(cell: Tuple2[Int, Int]): Point = {
    // TODO: Fix this up with relative values once we know it works!
    val cellSize = grid.getCellSize()

    return new Point( (cellSize.width  / 2) + cellSize.width*(cell._2) , 
                      (cellSize.height / 2)+ cellSize.height*(cell._1) )
  }

  def hideCycle(): Unit = {
    paintCycle = false
    //grid.visible = true
    repaint()
  }

  override def paint(g: Graphics2D): Unit = {
    super.paint(g)

    // Draw cycle if we want to.
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

  def showCycle(cycle: List[Tuple2[Int, Int]]): Unit = {
    storedCycle = cycle
    println("Stored cycle")
    //grid.visible = false
    paintCycle = true
    repaint()
  }

}

