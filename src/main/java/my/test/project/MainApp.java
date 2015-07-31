package my.test.project;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jdk.packager.services.UserJvmOptionsService;
        
public class MainApp extends Application
{
  public final static Logger MainAppLogger = LoggerFactory.getLogger(MainApp.class);

  public static void main(String[] aArgs) throws Exception
  {
    UserJvmOptionsService ujo = UserJvmOptionsService.getUserJVMDefaults();
   
    ConsoleAppender console = new ConsoleAppender(new PatternLayout("%d %-5p [%t] %C{2} (%l) - %m%n"));
    console.setName("CONSOLE");
    console.setThreshold(Level.INFO);
    console.setWriter(new OutputStreamWriter(System.out));
    console.activateOptions();

    org.apache.log4j.Logger.getRootLogger().addAppender(console);
    org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger("my.test.project");

    try
    {
        DailyRollingFileAppender daily = new DailyRollingFileAppender(new PatternLayout("%d %-5p [%t] %C{2} (%l) - %m%n"),
                                                                      new File(Paths.get(System.getProperty("user.home"), ".tcr").toFile(), "MainApp.log").getAbsolutePath(),
                                                                      "'.'yyyy-MM-dd-a");

        daily.setName("DAILY");
        daily.setThreshold(Level.INFO);
        daily.setAppend(true);
        daily.activateOptions();
        daily.setImmediateFlush(true);

        // Display the logging content to the Console

        log4jLogger.setAdditivity(true);
        log4jLogger.addAppender(daily);

        log4jLogger.info("MainApp log file location = " + daily.getFile());
    }
    catch (IOException e)
    {
        log4jLogger.error("Error creating the Daily Rolling Log", e);
    }

    MainApp.MainAppLogger.info("MainApp Logger Initialized");
    MainApp.MainAppLogger.info("Current Memory = {}", MainApp.getMemoryStatistics());
    MainApp.MainAppLogger.info("MyProperty = {}", System.getProperty("MyProperty"));
    MainApp.MainAppLogger.info("Number of Command Line Arguments = {}", aArgs.length);
    MainApp.MainAppLogger.info("UserJvmOptionsService.getUserJVMOptions: Argument3 > " + ujo.getUserJVMOptions().get("Argument3") );
    
    launch(MainApp.class, aArgs);
  }
    
  public final static String getMemoryStatistics()
  {
    StringBuilder builder = new StringBuilder();

    Runtime rt = Runtime.getRuntime();

    long maxMemory = rt.maxMemory();
    long allocatedMemory = rt.totalMemory();
    long freeMemory = rt.freeMemory();

    builder.append("\n");
    builder.append("\n");

    builder.append("Free Memory......: ");
    builder.append(freeMemory / 1024);
    builder.append("\n");

    builder.append("Allocated Memory.: ");
    builder.append(allocatedMemory / 1024);
    builder.append("\n");

    builder.append("Maximum Memory...: ");
    builder.append(maxMemory / 1024);
    builder.append("\n");

    builder.append("Total Free Memory: ");
    builder.append((freeMemory + (maxMemory - allocatedMemory)) / 1024);
    builder.append("\n");

    return builder.toString();
  }
  
  public void start(Stage aStage) throws Exception
  {
    MainApp.MainAppLogger.info("JavaFX Start Method");
    
    super.getParameters().getRaw().forEach((String value) -> 
    {
      MainApp.MainAppLogger.info("Application Start Method:  Argument {}", value);
    });   

    String fxmlFile = "/fxml/hello.fxml";
    MainApp.MainAppLogger.info("Loading FXML for main view from: {}", fxmlFile);
    FXMLLoader loader = new FXMLLoader();
    Parent rootNode = (Parent)loader.load(getClass().getResourceAsStream(fxmlFile));
    HelloController controller = loader.getController();
    controller.getJVMArg().textProperty().set(System.getProperty("MyProperty"));
    if (super.getParameters().getRaw().isEmpty() == false)
    {
       controller.getUserJVMArg().textProperty().set(super.getParameters().getRaw().get(0));
    }
    controller.getJVMProperty().textProperty().set(System.getProperty("UserProperty"));

    Scene scene = new Scene(rootNode);
    scene.getStylesheets().add("/styles/styles.css");

    aStage.setTitle("Hello JavaFX and Maven");
    aStage.setScene(scene);
    aStage.show();
  }
}
