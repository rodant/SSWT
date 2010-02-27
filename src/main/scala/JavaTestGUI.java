import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class JavaTestGUI {

	static class User {
		private String firstName = "Bullet";
		private String lastName = "Tooth";
		private boolean male = true;
		private boolean student = true;
		private boolean employee = true;
		private int experience = 5;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public boolean isMale() {
			return male;
		}

		public void setMale(boolean male) {
			this.male = male;
		}

		public boolean isStudent() {
			return student;
		}

		public void setStudent(boolean student) {
			this.student = student;
		}

		public boolean isEmployee() {
			return employee;
		}

		public void setEmployee(boolean employee) {
			this.employee = employee;
		}

		public int getExperience() {
			return experience;
		}

		public void setExperience(int experience) {
			this.experience = experience;
		}

		@Override
		public String toString() {
			return "Name: " + firstName + " " + lastName + " Gender: "
					+ (male ? "male" : "female") + " Student: " + student
					+ " Employee: " + employee + " Experience: " + experience;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final User user = new User();
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			@Override
			public void run() {
				buildGui(shell, user);
				shell.pack();
				shell.open();
				while (!shell.isDisposed())
					if (!display.readAndDispatch())
						display.sleep();
			}

		});
	}

	static private void buildGui(Shell shell, final User user) {
		shell.setText("User Profil");
		shell.setLayout(new GridLayout());
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		DataBindingContext dbc = new DataBindingContext();

		Group nameGroup = new Group(composite, SWT.NONE);
		nameGroup.setText("Name");
		nameGroup.setLayout(new GridLayout(2, false));
		nameGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		new Label(nameGroup, SWT.NONE).setText("First:");
		Text firstNameField = new Text(nameGroup, SWT.BORDER);
		firstNameField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		dbc.bindValue(SWTObservables.observeText(firstNameField, SWT.Modify),
				PojoObservables.observeValue(user, "firstName"), null, null);
		new Label(nameGroup, SWT.NONE).setText("Last:");
		Text lastNameField = new Text(nameGroup, SWT.BORDER);
		lastNameField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		dbc.bindValue(SWTObservables.observeText(lastNameField, SWT.Modify),
				PojoObservables.observeValue(user, "lastName"), null, null);

		Group genderGroup = new Group(composite, SWT.NONE);
		genderGroup.setText("Gender");
		genderGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		genderGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Button maleRadio = new Button(genderGroup, SWT.RADIO);
		maleRadio.setText("Male");
		dbc.bindValue(SWTObservables.observeSelection(maleRadio),
				PojoObservables.observeValue(user, "male"), null, null);
		Button femaleRadio = new Button(genderGroup, SWT.RADIO);
		femaleRadio.setText("Female");

		Group roleGroup = new Group(composite, SWT.NONE);
		roleGroup.setText("Role");
		roleGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		roleGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Button studentCheck = new Button(roleGroup, SWT.CHECK);
		studentCheck.setText("Student");
		dbc.bindValue(SWTObservables.observeSelection(studentCheck),
				PojoObservables.observeValue(user, "student"), null, null);
		Button employeeCheck = new Button(roleGroup, SWT.CHECK);
		employeeCheck.setText("Employee");
		dbc.bindValue(SWTObservables.observeSelection(employeeCheck),
				PojoObservables.observeValue(user, "employee"), null, null);

		Group experienceGroup = new Group(composite, SWT.NONE);
		experienceGroup.setText("Experience");
		experienceGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		experienceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		Spinner experienceSpinner = new Spinner(experienceGroup, SWT.NONE);
		dbc.bindValue(SWTObservables.observeSelection(experienceSpinner),
				PojoObservables.observeValue(user, "experience"), null, null);
		new Label(experienceGroup, SWT.NONE).setText("years");

		Button saveButton = new Button(composite, SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		saveButton.setText("save");
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(user.toString());
			}
		});

		Button closeButton = new Button(composite, SWT.PUSH);
		closeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		closeButton.setText("close");
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
	}
}
