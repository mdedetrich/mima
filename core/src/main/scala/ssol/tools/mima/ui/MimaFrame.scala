package ssol.tools.mima.ui

import scala.swing._
import Swing._
import wizard._
import ssol.tools.mima.{ Config, MiMaLib, Fix, Problem, IncompatibleResultTypeProblem }
import event.Event

import scala.tools.nsc.{ util, io }
import util._
import ClassPath._

case object Exit extends Event

class MimaFrame extends MainFrame with Centered {

  object ScalaInstall extends ImagePanel(images.Icons.install)

  title = "Scala Migration Manager"
  preferredSize = (800, 600)
  minimumSize = preferredSize
  centerFrame
  resizable = true

  private val mainContainer = new BorderPanel {
    border = EmptyBorder(10)
    add(ScalaInstall, BorderPanel.Position.West)

    def setContent(c: Component): Unit = {
      add(c, BorderPanel.Position.Center)
      revalidate()
      repaint()
    }
  }

  contents = mainContainer

  val welcome = new WelcomePage

  mainContainer.setContent(welcome)

  listenTo(welcome)

  reactions += {
    case WelcomePage.MigrateProject => ()

    case WelcomePage.MigrateLibrary =>
      val wizard = new MimaLibWizard
      listenTo(wizard)
      wizard.start()
      mainContainer.setContent(wizard)
  }

  reactions += {
    case Exit =>
      Dialog.showConfirmation(parent = null,
        title = "Exit Mimalib",
        message = "Are you sure you want to quit?") match {
          case Dialog.Result.Ok => exit(0)
          case _                => ()
        }
  }
}