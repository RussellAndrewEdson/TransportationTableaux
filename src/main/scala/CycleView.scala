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

