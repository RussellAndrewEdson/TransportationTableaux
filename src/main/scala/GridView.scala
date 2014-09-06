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

// Code for the GridView.

import swing._
import Swing._

import java.awt.Color

class GridView(rowCount: Int, columnCount: Int) 
    extends GridPanel(rowCount, columnCount) {
  
  border = LineBorder(Color.BLACK)

  private val linkFlow = Array.ofDim[GridCell](rowCount, columnCount)
  for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
    linkFlow(i)(j) = new GridCell()
  }

  for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
    contents += linkFlow(i)(j)
  }

  def getLinkFlowCosts(): Array[Array[Int]] = {
    // Error checking?
    val linkFlowCosts = Array.ofDim[Int](linkFlow.length, linkFlow(0).length)
    for (i <- 0 until linkFlowCosts.length) {
      for (j <- 0 until linkFlowCosts(i).length) {
        linkFlowCosts(i)(j) = linkFlow(i)(j).getCost()
      } 
    }

    linkFlowCosts
  }

  def setAllocations(allocations: Array[Array[Int]]) {
    // Error checking?
    for (i <- 0 until linkFlow.length; j <- 0 until linkFlow(i).length) {
      linkFlow(i)(j).setAllocation(allocations(i)(j))
    }

    // Might need to redraw the view here.
  }
}
