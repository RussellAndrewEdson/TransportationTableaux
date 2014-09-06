// Class to test the GUI components.
// (This will be rewritten properly once we figure out how everything
// needs to look.)

//TODO: Find out exactly what is needed from the imports...
import swing._
import Swing._
import java.awt.Color

object TestGUI extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title = "Test for TransportationTableaux"

    val suppliesField = new TextField()
    val demandsField = new TextField()

    val resetButton = new Button {
      text = "Reset"
    }

    val status = new Label("Status goes here")

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
      contents += resetButton
    }

    //val middle = new Table(5,5)
    //val middle = new GridPanel(3,3) {
    //  for (i <- 1 to 9) contents += new GridCellView()
    //}

    val middle = new TableauView(3,3)

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
  }
}
