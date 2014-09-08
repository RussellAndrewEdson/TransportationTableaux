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
    title = "TransportationTableaux v0.1"

    val suppliesField = new TextField("3")
    val demandsField = new TextField("3")

    val resizeButton = new Button {
      text = "Resize"
    }

    // Statuses go here for now.
    val InitialStatus =        "Enter the supplies, demands and costs.        "
    val AfterNorthWestCorner = "Finished North-West corner rule.              "
    val AdjustedAllocations =  "Adjusted tableau allocations.                 "
    val OptimumReached =       "Optimum solution reached.                     "

    val status = new Label(InitialStatus)

    val stepButton = new Button {
      text = "Step"
    }

    val solveButton = new Button {
      text = "Solve"
    }

    val specPanel = new BoxPanel(Orientation.Horizontal) {
      border = TitledBorder(LineBorder(Color.BLACK), "Problem Size")

      contents += new Label("Supplies: ")
      contents += suppliesField
      contents += new Label("Demands: ")
      contents += demandsField
      contents += resizeButton
    }

    //val middle = new Table(5,5)
    //val middle = new GridPanel(3,3) {
    //  for (i <- 1 to 9) contents += new GridCellView()
    //}

    var middle = new TableauView(3,3)

    val controlPanel = new BorderPanel() {
      border = LineBorder(Color.BLACK)

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


    def setTableauValues(): Unit = {
      middle.setUi(tableau.ui)
      middle.setVj(tableau.vj)
      middle.setAllocations(tableau.allocations)
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
        stepButton.enabled = true
        solveButton.enabled = true
        repaint()
        tableauCreated = false

      case ButtonClicked(component) if component == stepButton =>
        if (!tableauCreated) {
          tableau = new TransportationTableau( middle.getSupplies(),
                                               middle.getDemands(),
                                               middle.getLinkFlowCosts() )
          tableauCreated = true
          tableau.northWestCornerRule()
          status.text = AfterNorthWestCorner
        }
        else {
          tableau.adjustAllocations(tableau.cycleTraversal(tableau.starPair))
          status.text = AdjustedAllocations
        }

        if (tableau.isOptimal) {
          stepButton.enabled = false
          solveButton.enabled = false
          status.text = OptimumReached
        }
        setTableauValues()

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
        stepButton.enabled = false
        solveButton.enabled = false
        status.text = OptimumReached

    }

  }
}
