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

import swing.{ BorderPanel, BoxPanel, Button, FlowPanel, Label }
import swing.{ MainFrame, Orientation, SimpleSwingApplication, TextField }
import swing.Swing.{ LineBorder, TitledBorder }
import swing.event.{ ButtonClicked }

import java.awt.Color

/** The GUI version of the TransportationTableaux program. The user can enter
  * the problem specification (supplies, demands, link-flow costs) at the 
  * interactive tableau, and then can click through the complete solution of
  * the Transportation problem -- every tableau, cycle and star-pair will be
  * shown. Alternatively, the user can simply solve the problem immediately.
  * The basic solution and cost are also shown for each new allocation.
  *
  * @author Russell Andrew Edson, <russell.andrew.edson@gmail.com>
  * @version 0.3
  */
object GuiDriver extends SimpleSwingApplication {
  
  /** The title of the application, including version number. */
  val ApplicationTitle = "TransportationTableaux v0.3"

  /** The statuses for the tableau during application execution. */
  object TableauStatus extends Enumeration {
    type TableauStatus = Value
    val Initial             = Value(" Enter the supplies, demands and costs. ")
    val NorthWestCorner     = Value(" Finished North-West Corner Rule.       ")
    val FoundStarPair       = Value(" Located star (*) pair (ui+vj >= cij).  ")
    val ConstructedCycle    = Value(" Constructed cycle with star pair.      ")
    val AdjustedAllocations = Value(" Adjusted allocations along the cycle.  ")
    val OptimalSolution     = Value(" Optimal solution reached.              ")
  }

  /** The text to denote that no basic solution currently exists. */
  val NoBasicSolution = "No allocations yet."

  /** We default to 3 supplies when the program starts. */
  val DefaultSupplies = 3

  /** We default to 3 demands when the program starts. */
  val DefaultDemands = 3

  /** The initial cost is 0 for any tableau. */
  val DefaultCost = 0

  def top = new MainFrame {
    title = ApplicationTitle
    
    /* The user enters the number of supplies for the problem. */
    val suppliesField = new TextField(DefaultSupplies.toString)

    /* The user enters the number of demands for the problem. */
    val demandsField = new TextField(DefaultDemands.toString)

    /* The user can resize (and reset) the tableau by clicking a button. */
    val resizeButton = new Button("Resize")

    /* The tableau is on display in the middle of the application. */
    var tableauDisplay = new TableauView(DefaultSupplies, DefaultDemands)

    /* We show the current transport cost for the tableau at the bottom. */
    val costDisplay = new Label(DefaultCost.toString) 

    /* The basic solution is shown at the bottom of the tableau. */
    val basicSolutionDisplay = new Label(NoBasicSolution)

    /* The current execution status of the program is shown at all times. */
    val statusDisplay = new Label(TableauStatus.Initial.toString)

    /* The user steps through the solution by clicking a button. */
    val stepButton = new Button("Step")

    /* The user can also jump right to the solution by clicking a button. */
    val solveButton = new Button("Solve")

    
    /* We layout the problem specification controls (supplies, demands,
     * resizing) at the top of the window.
     */
    val specificationPanel = new BoxPanel(Orientation.Horizontal) {
      border = TitledBorder(LineBorder(Color.BLACK), "Problem Size")

      contents += new Label("Supplies: ")
      contents += suppliesField
      contents += new Label("  ")         // spacer
      contents += new Label("Demands: ")
      contents += demandsField
      contents += new Label("     ")      // spacer
      contents += resizeButton
    }

    /* We layout the current tableau information (cost, basic solution)
     * together immediately beneath the tableau itself.
     */
    val informationPanel = new BoxPanel(Orientation.Vertical) {
      contents += new FlowPanel(FlowPanel.Alignment.Left) (
          new Label("Cost: "),
          costDisplay)
      contents += new FlowPanel(FlowPanel.Alignment.Left) (
          new Label("Basic Solution: "))
      contents += new FlowPanel(FlowPanel.Alignment.Center) (
          basicSolutionDisplay)
    }

    /* We layout the tableau control mechanisms (stepping, solving, and the
     * status) together at the very bottom of the window.
     */
    val controlPanel = new BorderPanel() {
      border = LineBorder(Color.BLACK)

      layout += statusDisplay -> BorderPanel.Position.West
      layout += new Label("       ") -> BorderPanel.Position.Center  // spacer
      layout += new BoxPanel(Orientation.Horizontal) {
          contents += stepButton
          contents += solveButton
        } -> BorderPanel.Position.East
    }

    /* Finally, we align everything properly in the main layout. */
    contents = new BorderPanel() {
      layout += specificationPanel -> BorderPanel.Position.North
      layout += tableauDisplay -> BorderPanel.Position.Center
      layout += new BoxPanel(Orientation.Vertical) {
          contents += informationPanel
          contents += controlPanel
        } -> BorderPanel.Position.South
    }


    /* We instantiate a TransportationTableau to solve the given problem
     * whenever the user clicks the "Step" or "Solve" button.
     */
    private def makeTransportationTableau(): TransportationTableau = {
      return new TransportationTableau(
          tableauDisplay.getSupplies(),
          tableauDisplay.getDemands(),
          tableauDisplay.getLinkFlowCosts() )
    }

    /* We initialise the tableau variable with the default problem. */
    private var tableau = makeTransportationTableau()

    /* We also use boolean flags to keep track of the current state of
     * the solution process.
     */
    var tableauCreated = false
    var starPairFound = false
    var cycleConstructed = false

    /* We update the solution display whenever we've made an adjustment
     * to the tableau allocations.
     */
    private def updateSolution(): Unit = {
      /* We first update the tableau view. */
      tableauDisplay.setUi(tableau.ui)
      tableauDisplay.setVj(tableau.vj)
      tableauDisplay.setAllocations(tableau.allocations)

      /* Next, we update the cost. */
      costDisplay.text = tableau.cost.toString

      /* Finally, we update the basic solution (with some HTML for nice 
       * subscripting of the variables).
       */
      def formatSolution(pair: Tuple2[Int, Int]): String = 
          ("x<sub>%d, %d</sub>".format((pair._1 + 1), (pair._2 + 1)) +
          " = " + tableau.allocations(pair._1)(pair._2).toString)

      val solutionIndices = tableau.basicSolution.sorted
      val solutionString = solutionIndices.map {
          p => formatSolution(p)
      } mkString(", &nbsp;")

      basicSolutionDisplay.text = "<html>" + solutionString + "</html>"
    }

    /* Finally, we set up the button listening/reacting actions. */
    listenTo(resizeButton)
    listenTo(stepButton)
    listenTo(solveButton)

    /* The reactions for the button clicks control the main program flow. */
    reactions += {
      /* When the 'Resize' button is clicked, we refresh the tableau view
       * with a new tableau of the given size.
       */
      case ButtonClicked(component) if component == resizeButton =>
        tableauDisplay = new TableauView(
            suppliesField.text.toInt,
            demandsField.text.toInt)

        //TODO We apparently need to recreate the layout to get it to view
        // properly -- check if there is a better way to do this.
        contents = new BorderPanel() {
          layout += specificationPanel -> BorderPanel.Position.North
          layout += tableauDisplay -> BorderPanel.Position.Center
          layout += new BoxPanel(Orientation.Vertical) {
            contents += informationPanel
            contents += controlPanel
          } -> BorderPanel.Position.South
        }

        statusDisplay.text = TableauStatus.Initial.toString
        costDisplay.text = DefaultCost.toString
        basicSolutionDisplay.text = NoBasicSolution
        stepButton.enabled = true
        solveButton.enabled = true
        tableauCreated = false
        starPairFound = false
        cycleConstructed = false

        repaint()

      /* When the 'Step' button is clicked, We work through the complete
       * solution to the problem in the tableau.
       */
      case ButtonClicked(component) if component == stepButton =>
        /* If we have a new problem, we instantiate the tableau and
         * perform the North-West corner rule.
         */
        if (!tableauCreated) {
          tableau = makeTransportationTableau()
          tableauCreated = true
          starPairFound = false
          cycleConstructed = false

          tableau.northWestCornerRule()
          updateSolution()
          statusDisplay.text = TableauStatus.NorthWestCorner.toString
        }

        /* If we haven't yet found the star pair, we find it.
         */
        else if (!starPairFound) {
          tableauDisplay.setStarPair(tableau.starPair)
          starPairFound = true
          statusDisplay.text = TableauStatus.FoundStarPair.toString
        }

        /* If the star pair has been found, we use it to construct the
         * cycle and display it on the screen.
         */
        else if (!cycleConstructed) {
          tableauDisplay.setCycle(tableau.cycleTraversal(tableau.starPair))
          cycleConstructed = true
          statusDisplay.text = TableauStatus.ConstructedCycle.toString
        }

        /* If the cycle has been shown, we then adjust the allocations
         * to get to the next tableau. We then repeat the process by
         * finding a new star pair.
         */
        else {
          tableau.adjustAllocations(tableau.cycleTraversal(tableau.starPair))
          statusDisplay.text = TableauStatus.AdjustedAllocations.toString

          updateSolution()
          tableauDisplay.clearStarPair()
          tableauDisplay.clearCycle()
          starPairFound = false
          cycleConstructed = false
        }

        /* If at any point the tableau is optimal, we stop and display
         * the optimal solution to the user.
         */
        if (tableau.isOptimal) {
          updateSolution()
          stepButton.enabled = false
          solveButton.enabled = false
          statusDisplay.text = TableauStatus.OptimalSolution.toString
          tableauDisplay.clearStarPair()
          tableauDisplay.clearCycle()
          starPairFound = false
          cycleConstructed = false
        }

        repaint()

      /* If the 'Solve' button is clicked at any point, we simply solve
       * the Transportation problem to completion in a single step (ie.
       * without showing any intermediate results.)
       */
      case ButtonClicked(component) if component == solveButton =>
        if (!tableauCreated) {
          tableau = makeTransportationTableau()
          tableauCreated = true
          
          tableau.northWestCornerRule()
        }

        while (!tableau.isOptimal) {
          tableau.adjustAllocations(tableau.cycleTraversal(tableau.starPair))
        }
        
        updateSolution()
        stepButton.enabled = false
        solveButton.enabled = false
        statusDisplay.text = TableauStatus.OptimalSolution.toString
        tableauDisplay.clearStarPair()
        tableauDisplay.clearCycle()
        starPairFound = false
        cycleConstructed = false

        repaint()
    }
  }

}

