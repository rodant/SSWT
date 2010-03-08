import org.consultar.scala.swtdsl._

object TestGUI {
  def main(args : Array[String]): Unit = {
	  object user {
      var firstName = "Bullet"
      var lastName = "Tooth"
      var student = true
      var employee = true
      var experience = 5
      var male = true
      override def toString() = {
        val str = 
          "Name: " + firstName +" "+ lastName + 
          " Gender: " + (if (male) "male" else "female") +
          " Student: " + student +
          " Employee: " + employee + 
          " Experience: " + experience
        str
      }
    }
    new SWTBuilder {
      shell(title("User Profil"), gridLayout()) {
    	composite(gridLayout(columns(2))) {
    	  group(title("Name"), gridLayout(columns(2)), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
    	    label(title("First"));		edit(bind(user.firstName_=, user.firstName _), horizontal(fill, grabExcessSpace))
            label(title("Last:"));		edit(bind(user.lastName_=, user.lastName _), horizontal(fill, grabExcessSpace))
          }
          group(title("Gender"), rowLayout(), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
    	    radioButton(bind(user.male_=, user.male _), title("Male"))
    	    radioButton(emptyBinding(), title("Female"))
          }
          group(title("Role"), rowLayout(), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
            checkBox(bind(user.student_=, user.student _), title("Student"))
            checkBox(bind(user.employee_=, user.employee _), title("Employee"))
          }
          group(title("Experience"), rowLayout(), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
            spinner(bind(user.experience_=, user.experience _));	label(title("years"))
          }
          button(title("save"), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
                   case _ => println(user)
          }
          button(title("close"), horizontal(fill, grabExcessSpace), vertical(fill, grabExcessSpace)) {
                   case _ => System.exit(0)
          }
    	}
      }
    }.run
  }
}
