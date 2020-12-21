# ![VEKTA](/docs/header.PNG)

Vekta is a procedural space exploration game with vector-style graphics. It features an infinitely generated universe to explore with unique characters scattered across the planets.

**VEKTA is in early development, and a stable, polished experience should not be expected.**

## Building the Code

1. If not already installed, [install Maven](https://maven.apache.org/install.html).
2. Clone the project directory.
3. Build the project:
    - Run the build script (`build.bat`) provided in the project root directory. This should provide two jar executables in the `target` folder
    which you can then run the jar "with-dependencies" to run the program. 
    - If you are using an IDE and have Maven properly configured you can also use that to build and run the project.
    
# Running the Game

After building the project, the game’s executable can be launched at “/target/vekta-X.X.X-jar-with-dependencies”. Upon launch, the ‘W’, ‘A’, ‘S’, and ‘D’ keys cane be used to navigate the main menu.

# Development Workflow

While the “build.bat” script can fully rebuild upon any code changes, it is recommend that you use an IDE for long-term development. We recommend using the newest version of Intellij IDEA due to its useful project configurations with Maven and Ant. Here are the steps for setting up a development environment using IntelliJ.
1. Confirm that the project builds successfully using the “build.bat” script. If so, all dependencies should be set up correctly.
2. Open the root directory as a “File Project” (File -> Open -> *open root directory*).
3. Select the “Build Configurations bar on top of the IDE, and select “Edit Configurations”.
4. Select the JAR Application Template.
    - Set “Path to JAR” to “…\[root-directory]\target\vekta-0.1.0-jar-with-dependencies.jar”.
    - Ensure that a valid runtime environment is selected under “JRE:”.
    - Ensure that the “<whole project>” is selected under “Search sources using module’s classpath”
    - Select “Apply” and “OK”
5. Selecting “Build Project” will now build the project without requiring a full rebuild.
6. Selecting “Run” will now build and run the project's output JAR file.
    
## Code Styling

If you're using an IDE, there is an [EditorConfig](https://editorconfig.org/) file provided in the root directory that will allow you to automatically format your code as expected.
See [IntelliJ's documentation on EditorConfig files](https://www.jetbrains.com/help/idea/configuring-code-style.html#editorconfig) to see how it's used, or [install this addon for Eclipse](https://marketplace.eclipse.org/content/editorconfig-eclipse).
