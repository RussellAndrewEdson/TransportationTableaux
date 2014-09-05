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

    val controlPanel = new BoxPanel(Orientation.Horizontal) {
      border = LineBorder(Color.BLACK)

      contents += status
      contents += stepButton
      contents += solveButton
    }

    contents = new BoxPanel(Orientation.Vertical) {
      contents += specPanel
      contents += middle
      contents += controlPanel
    }
  }
}
