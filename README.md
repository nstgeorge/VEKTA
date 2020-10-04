## Team Name: VEKTA Bois

## Project Name: VEKTA

## Project Description:
Vekta is a retro-style procedural space exploration game made with Java and Processing 3.

## Team Members:

Last Name       | First Name      | GitHub User Name     | Scrum Role
--------------- | --------------- | -------------------- | ---------------
Becerra         | Juan            | juanbecerra0         | Scrum Master
Lytle           | Jared           | reviveddarkness      | Developer
St George       | Nate            | nstgeorge            | Product Owner 

## :eyes: :memo: Scrum Linter Reports:
Access the [Scrum Linter Report](http://cs.boisestate.edu/~bdit/ScrumLinter/CS471F20ScrumLinterReports/CS471-F20-Team16_oGvv2wlRhfv8y0XlOLyIYUmPmwdbhPYe34kw1nN1/). A new report will be automatically generated every few hours.
- Fix all inconsistencies, which are indicated in the report with :heavy_exclamation_mark:
- Aim to obtain :thumbsup: in all the sections of the linter report

## :capital_abcd: :mega: Grade Feedback:
See the [grading rubric, team project grade and instructor feedback](https://docs.google.com/spreadsheets/d/1BIPCubrIOaV_vDfekUT-mahBOSUPFhTn5mocP2YGAUM/edit?usp=sharing). This Google spreadsheet:
- is shared **only** with the team members
- will be updated after each major project milestone (e.g., initial planning, sprint)

## Building the Code
This program is build using the build automation tool Maven. As such, Maven is currently **required** to build and
play the game. The instructions below will detail installing Maven:

1. Acquire the Maven package appropriate for your operating system at [Maven Downloads](https://maven.apache.org/download.cgi).
2. Place the extracted Maven folder in any directory. It is reccomended that you put Maven in your root or program install
directory to prevent accidentally changing or deleting it, however this is not required.
3. Add Maven to your system path:
    - For Windows 10,
        - There are several different ways to do this. The easiest way is to search path in the start menu
    where you will then recieve a result called "Edit the system environment variables".
    [Start Menu](https://imgur.com/RW0BGxn)
        - Click on this and then click on Environment Variables located at the bottom of the page that comes up.
    [Enviro Variables](https://imgur.com/ipa6q1D)
        - At the bottom of this page, click the "new" option to create a new entry called "MAVEN_HOME" under system variables and add
    the path to your Maven install folder. (e.g. C:\apache-maven-3.6.3)
        - Finally, find the Path system variable, click edit, click new in the list that comes up, and add the entry "%MAVEN_HOME%\bin"
    to the list. You should now be able to use Maven.
    - For Linux, 
        - Simply type the command in bash "export PATH="path to bin folder in Maven directory."". 
    (e.g. export PATH="$HOME\apache-maven-3.6.3\bin")
    - For MacOS, 
        - Open a terminal and type "sudo nano /etc/paths". Then create a newline at the bottom of the file and add your
    path to your bin directory. (e.g. /etc/apache-maven-3.6.3). Save this file and the path should be added to your system path.
    - **NOTE:** It is absolutely essential that you take care when adding additional system paths. Destruction of other essential
    system paths could inhibit your computer's console from doing essential native tasks.
4. Clone the project directory.
5. Build the project:
    - Run the build script provided in the project root directory. This should provide two jar executables in the "Target" folder
    which you can then run the jar "with-dependencies" to run the program. 
    - If you are using an IDE and have Maven properly configured you can also use that to run the project and build with Maven. 
    
# Running and Development Workflow

After building the project, the game’s executable can be launched at “/target/vekta-X.X.X-jar-with-dependencies”. Upon launch, the ‘W’, ‘A’, ‘S’, and ‘D’ keys cane be used to navigate the main menu.

While the “build.bat” script can fully rebuild upon any code changes, it is recommend that you use an IDE for long-term development. We recommend using the newest version of Intellij IDEA due to its useful project configurations with Maven and Ant. Here are the steps for setting up a development environment using Intellij.
1. Confirm that the project builds successfully using the “build.bat” script. If so, all dependencies should be set up correctly.
2. Open the root directory as a “File Project” (File -> Open -> *open root directory*).
3. Select the “Build Configurations bar on top of the IDE, and select “Edit Configurations”.
4. Select the JAR Application Template.
    - Set “Path to JAR” to “…\[root-directory]\target\vekta-0.1.0-jar-with-dependencies.jar”.
    - Ensure that a valid runtime environment is selected under “JRE:”.
    - Ensure that the “<whole project>” is selected under “Search sources using module’s classpath”
    - Select “Apply” and “OK”
5. Selecting “Build Project” will now build the project after code changes.
6. Selecting “Run” will now build and run the project after code changes.

    
## Code Styling

If you're using an IDE, there is an [EditorConfig](https://editorconfig.org/) file provided in the root directory that will allow you to automatically format your code as expected.
See [IntelliJ's documentation on EditorConfig files](https://www.jetbrains.com/help/idea/configuring-code-style.html#editorconfig) to see how it's used, or [install this addon for Eclipse](https://marketplace.eclipse.org/content/editorconfig-eclipse).

If you're not able to use the EditorConfig file, here are some of the more important points:

- Indents should be four spaces.
- Open brackets should be placed on the lines with keywords: `for (...) { ...`, `while (...) {...`
- Close brackets get their own line.
- All variables, methods, and classes should be camelCase (First word capitalized for classes) except for constants, which should be all caps with underscores between words (for example, `EARTH_MASS`).
- Getters and setters should be prefixed with `get` and `set` respectively, like `getVelocity()` or `setMass()`.
- Methods should generally be under 50 lines.

## Contribution Guidelines

All contributions should follow good software engineering practices. Epics, stories, and tasks should be created in Github's Issues feature, and moved accordingly within the scrum board project.
Commits should reference which task they are implementing, e.g. "Implements #7".
