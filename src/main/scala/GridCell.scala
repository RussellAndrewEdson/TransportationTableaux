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

// Code for the GridCell.

import swing._
import Swing._

import java.awt.Color

class GridCell extends GridPanel(2,2) {
  private val DefaultAllocation = 0
  private val DefaultCost = 0

  private val allocation = new Label(DefaultAllocation.toString)
  private val linkFlowCost = new TextField(DefaultCost.toString)

  border = LineBorder(Color.BLACK)

  contents += new Label("") // Blank in the top-left corner.
  contents += linkFlowCost
  contents += allocation
  contents += new Label("") // Blank in the bottom-right corner.

  def getCost(): Int = linkFlowCost.text.toInt

  def setAllocation(value: Int) {
    allocation.text = value.toString
  }

}