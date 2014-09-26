// Class to test the GUI components.
// (This will be rewritten properly once we figure out how everything
// needs to look.)

//TODO: Find out exactly what is needed from the imports...
import swing._
import Swing._
import java.awt.Color
import event._

object TestGUI extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title = "TransportationTableaux v0.2"

    val suppliesField = new TextField("3")
    val demandsField = new TextField("3")

    val resizeButton = new Button {
      text = "Resize"
    }

    // Statuses go here for now.
    val InitialStatus =        " Enter the supplies, demands and costs.        "
    val AfterNorthWestCorner = " Finished North-West corner rule.              "
    val FoundStarPair =        " Found star pair.                              "
    val FoundCycle =           " Found cycle.                                  "
    val AdjustedAllocations =  " Adjusted tableau allocations.                 "
    val OptimumReached =       " Optimal solution reached.                     "

    val status = new Label(InitialStatus)

    val stepButton = new Button {
      text = "Step"
    }

    val solveButton = new Button {
      text = "Solve"
    }

    val costField = new Label("0")
    val solutionField = new Label("No allocations yet.")

    val specPanel = new BoxPanel(Orientation.Horizontal) {
      border = TitledBorder(LineBorder(Color.BLACK), "Problem Size")

      contents += new Label("Supplies: ")
      contents += suppliesField
      contents += new Label("Demands: ")
      contents += demandsField
      contents += resizeButton
    }

    var middle = new TableauView(3,3)

    val solutionInfo = new BoxPanel(Orientation.Vertical) {
      contents += new FlowPanel(FlowPanel.Alignment.Left) (
          new Label("Cost: "),
          costField)
      contents += new FlowPanel(FlowPanel.Alignment.Left) (
          new Label("Basic Solution: "))
      contents += new FlowPanel(FlowPanel.Alignment.Center) (
          solutionField)
    }

    //val controlPanel = new BoxPanel(Orientation.Vertical) {
    //  contents += solutionInfo
    //  contents += new FlowPanel(FlowPanel.Alignment.Left) (
    //      status,
    //      stepButton,
    //      solveButton) {
    //    border = LineBorder(Color.BLACK)
    //  }
    //}

    val controlPanel = new BorderPanel() {
      //border = LineBorder(Color.BLACK)

      layout += solutionInfo -> BorderPanel.Position.North
      layout += status -> BorderPanel.Position.West
      layout += new BoxPanel(Orientation.Horizontal) {
        contents += stepButton
        contents += solveButton
      } -> BorderPanel.Position.East
    }

    contents = new BorderPanel() {
      layout += specPanel -> BorderPanel.Position.North
      layout += middle -> BorderPanel.Position.Center
      layout += controlPanel -> BorderPanel.Position.South
    }

    listenTo(resizeButton)
    listenTo(stepButton)
    listenTo(solveButton)

    var tableau = new TransportationTableau( middle.getSupplies(), 
                                             middle.getDemands(),
                                             middle.getLinkFlowCosts() )
    
    var tableauCreated = false
    var starPairFound = false
    var cycleFound = false


    def setTableauValues(): Unit = {
      middle.setUi(tableau.ui)
      middle.setVj(tableau.vj)
      middle.setAllocations(tableau.allocations)
    }

    def setCost(): Unit = {
      costField.text = tableau.cost.toString
    }

    def setBasicSolution(): Unit = {
      def solnFormat(pair: Tuple2[Int, Int]): String = {
        ("x<sub>%d, %d</sub>".format((pair._1 + 1), (pair._2 + 1)) + 
        " = " + tableau.allocations(pair._1)(pair._2).toString)
      }

      // We sort the basic solution first.
      val solnIndices = tableau.basicSolution.sorted

      val soln = (solnIndices.map(p => solnFormat(p))).mkString(", &nbsp;&nbsp;")
      solutionField.text = "<html><body>" + soln + "</body></html>"
    }

    reactions += {
      case ButtonClicked(component) if component == resizeButton =>
        middle = new TableauView(suppliesField.text.toInt, demandsField.text.toInt)
        contents = new BorderPanel() {
          layout += specPanel -> BorderPanel.Position.North
          layout += middle -> BorderPanel.Position.Center
          layout += controlPanel -> BorderPanel.Position.South
        }
        status.text = InitialStatus
        costField.text = "0"
        solutionField.text = "No allocations yet."

        stepButton.enabled = true
        solveButton.enabled = true
        repaint()
        tableauCreated = false
        starPairFound = false
        cycleFound = false

      case ButtonClicked(component) if component == stepButton =>
        if (!tableauCreated) {
          tableau = new TransportationTableau( middle.getSupplies(),
                                               middle.getDemands(),
                                               middle.getLinkFlowCosts() )
          tableauCreated = true
          starPairFound = false
          cycleFound = false
          tableau.northWestCornerRule()
          status.text = AfterNorthWestCorner
          setCost()
          setBasicSolution()
          setTableauValues()
        }
        else if (!starPairFound) {
          middle.setStarPair(tableau.starPair)
          status.text = FoundStarPair
          starPairFound = true
        }
        else if (!cycleFound) {
          middle.setCycle(tableau.cycleTraversal(tableau.starPair))
          status.text = FoundCycle
          cycleFound = true
        }
        else {
          tableau.adjustAllocations(tableau.cycleTraversal(tableau.starPair))
          status.text = AdjustedAllocations
          setTableauValues()
          setCost()
          setBasicSolution()
          middle.clearStarPair()
          middle.clearCycle()
          starPairFound = false
          cycleFound = false
        }

        if (tableau.isOptimal) {
          stepButton.enabled = false
          solveButton.enabled = false
          status.text = OptimumReached
          setTableauValues()
          setCost()
          setBasicSolution()
          middle.clearStarPair()
          middle.clearCycle()
          starPairFound = false
          cycleFound = false
        }
        //setTableauValues()

      case ButtonClicked(component) if component == solveButton =>
        if (!tableauCreated) {
          tableau = new TransportationTableau( middle.getSupplies(),
                                               middle.getDemands(),
                                               middle.getLinkFlowCosts() )
          tableauCreated = true
          tableau.northWestCornerRule()
        }
        while (!tableau.isOptimal) {
          tableau.adjustAllocations(tableau.cycleTraversal(tableau.starPair))
        }
        setTableauValues()
        setCost()
        setBasicSolution()
        stepButton.enabled = false
        solveButton.enabled = false
        status.text = OptimumReached

    }

  }
}
