package my.test.project;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloController
{
  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

  @FXML private TextField jvmArg;
  @FXML private TextField userJVMArg;
  @FXML private TextField jvmProperty;
  @FXML private TextField selectedFile;
  @FXML private Button fileChooser;
  @FXML private Button directoryChooser;

  @FXML
  public void fileChooserAction(ActionEvent aEvent)
  {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select a PST File");

    File file = fileChooser.showOpenDialog(this.directoryChooser.getScene().getWindow());
    if (file != null)
    {
       this.selectedFile.textProperty().set(file.getAbsolutePath());
    }
  }
  
  @FXML
  public void directoryChooser(ActionEvent aEvent)
  {
    DirectoryChooser folderChooser = new DirectoryChooser();
    folderChooser.setTitle("Select a Folder");

    File file = folderChooser.showDialog(this.directoryChooser.getScene().getWindow());
    if (file != null)
    {
       this.selectedFile.textProperty().set(file.getAbsolutePath());
    }
  }
  
  public TextField getJVMArg()
  {
    return this.jvmArg;
  }
  
  public TextField getUserJVMArg()
  {
    return this.userJVMArg;
  }
  
  public TextField getJVMProperty()
  {
    return this.jvmProperty;
  }
}
