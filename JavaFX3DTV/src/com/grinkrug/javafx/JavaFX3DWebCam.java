package com.grinkrug.javafx;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static javafx.geometry.Pos.CENTER;

/**
 * TODO: class com.github.sarxos.webcam.Webcam provides FPS counter. Show FPS somewhere...
 * TODO: test for a long time with 3d navigation (it seems, there are some garbage collection issues?)
 * May be it should be better when right panel will be removed...
 * May be it needs to avoid texture mapping when the face is not visible (by its normal to camera view angle...);
 * probably - just stop the camera, or to not sygnal for binding the image from the camera...
 * TODO: Some investigations are required (with memory and events monitoring... - when the program hangs...)
 *
 * Pack the application into executable jar...
 */

public class JavaFX3DWebCam extends Application {

    private GridPane topPane;
    private BorderPane webCamPane;
    private ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private FlowPane bottomCameraControlPane;
    private Button btnCamreaStop;
    private Button btnCamreaStart;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX 3D Web Camera TV");
        BorderPane root = new BorderPane();
        topPane = new GridPane();
        topPane.setPadding(new Insets(10, 10, 10, 10));
        topPane.setVgap(2);
        topPane.setHgap(20);
        topPane.setAlignment(CENTER);
        root.setTop(topPane);

        createTopPanel();

        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        webCamPane.setPrefSize(400, 400);
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        root.setCenter(webCamPane);

        bottomCameraControlPane = new FlowPane();
        bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
        bottomCameraControlPane.setAlignment(CENTER);
        bottomCameraControlPane.setDisable(true);
        //createCameraControls();
        root.setBottom(bottomCameraControlPane);

        FlowPane leftPane = create3DPane(imageProperty);
        root.setLeft(leftPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();

        Platform.runLater(this::setImageViewSize);
    }

    private FlowPane create3DPane(ObjectProperty<Image> imageProperty){
        return new JavaFX3DWorld(imageProperty).get3DWorldPane();
    }

    private void createCameraControls() {
        btnCamreaStop = new Button();
        btnCamreaStop.setOnAction(arg0 -> stopWebCamCamera());
        btnCamreaStop.setText("Stop Camera");
        btnCamreaStart = new Button();
        btnCamreaStart.setOnAction(arg0 -> startWebCamCamera());
        btnCamreaStart.setText("Start Camera");
        Button btnCameraDispose = new Button();
        btnCameraDispose.setText("Dispose Camera");
        btnCameraDispose.setOnAction(arg0 -> disposeWebCamCamera());
        bottomCameraControlPane.getChildren().add(btnCamreaStart);
        bottomCameraControlPane.getChildren().add(btnCamreaStop);
        bottomCameraControlPane.getChildren().add(btnCameraDispose);
    }

    private void setImageViewSize() {
        double height = webCamPane.getHeight();
        double width  = webCamPane.getWidth();
        imgWebCamCapturedImage.setFitHeight(height);
        imgWebCamCapturedImage.setFitWidth(width);
        imgWebCamCapturedImage.prefHeight(height);
        imgWebCamCapturedImage.prefWidth(width);
        imgWebCamCapturedImage.setPreserveRatio(true);
    }

    private void createTopPanel() {
        Slider spiral = new Slider();
        spiral.setMin(0);
        spiral.setMax(100);
        spiral.setValue(0);
        spiral.setShowTickLabels(true);
        spiral.setShowTickMarks(true);
        spiral.setMajorTickUnit(50);
        spiral.setMinorTickCount(5);
        spiral.setBlockIncrement(10);

        Text spiralText = new Text("Spiral percentage: 0");
        spiral.valueProperty()
                .addListener( (ov, oldVal, newVal) ->
                        spiralText.setText("Spiral percentage: " + (int)spiral.getValue()));

        Slider extension = new Slider();
        extension.setMin(0);
        extension.setMax(100);
        extension.setValue(25);
        extension.setShowTickLabels(true);
        extension.setShowTickMarks(true);
        extension.setMajorTickUnit(50);
        extension.setMinorTickCount(5);
        extension.setBlockIncrement(10);

        Text extensionText = new Text("Extension percentage: 25");
        extension.valueProperty()
                .addListener( (ov, oldVal, newVal) ->
                        extensionText.setText("Extension percentage: " + (int)extension.getValue()));


        Slider narrowing = new Slider();
        narrowing.setMin(0);
        narrowing.setMax(100);
        narrowing.setValue(50);
        narrowing.setShowTickLabels(true);
        narrowing.setShowTickMarks(true);
        narrowing.setMajorTickUnit(50);
        narrowing.setMinorTickCount(5);
        narrowing.setBlockIncrement(10);

        Text narrowingText = new Text("Narrowing percentage: 50");
        narrowing.valueProperty()
                .addListener( (ov, oldVal, newVal) ->
                        narrowingText.setText("Narrowing percentage: " + (int)narrowing.getValue()));

        Slider zigzag = new Slider();
        zigzag.setMin(0);
        zigzag.setMax(100);
        zigzag.setValue(75);
        zigzag.setShowTickLabels(true);
        zigzag.setShowTickMarks(true);
        zigzag.setMajorTickUnit(50);
        zigzag.setMinorTickCount(5);
        zigzag.setBlockIncrement(10);

        Text zigzagText = new Text("Zigzag percentage: 75");
        zigzag.valueProperty()
                .addListener( (ov, oldVal, newVal) ->
                        zigzagText.setText("Zigzag percentage: " + (int)zigzag.getValue()));

        int webCamCounter = 0;
        Label lbInfoLabel = new Label("Select Your WebCam Camera");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        GridPane.setConstraints(lbInfoLabel, 0, 0);
        GridPane.setConstraints(spiralText, 0, 1);
        GridPane.setConstraints(spiral, 1, 1);
        GridPane.setConstraints(extensionText, 0, 2);
        GridPane.setConstraints(extension, 1, 2);
        GridPane.setConstraints(narrowingText, 0, 3);
        GridPane.setConstraints(narrowing, 1, 3);
        GridPane.setConstraints(zigzagText, 0, 4);
        GridPane.setConstraints(zigzag, 1, 4);

        topPane.getChildren().addAll(
                spiralText,
                spiral,
                extensionText,
                extension,
                narrowingText,
                narrowing,
                zigzagText,
                zigzag,
                lbInfoLabel
        );

        for(Webcam webcam:Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
        ComboBox<WebCamInfo> cameraOptions = new ComboBox<>();
        cameraOptions.setItems(options);
        String cameraListPromptText = "Choose Camera";
        cameraOptions.setPromptText(cameraListPromptText);
        cameraOptions.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, arg2) -> {
            if (arg2 != null) {
//                System.out.println("WebCam Index: " + arg2.getWebCamIndex()+": WebCam Name:"+ arg2.getWebCamName());
                initializeWebCam(arg2.getWebCamIndex());
            }
        });
        topPane.getChildren().add(cameraOptions);
    }
    private void startWebCamStream() {
        stopCamera  = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webCam.getImage()) != null) {
//                        System.out.println("Captured Image height * width: " + grabbedImage.getWidth() + " * " + grabbedImage.getHeight());
                            Platform.runLater(() -> {
                                //final Image img = new Image("/cat.jpg");
                                final Image mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
                                imageProperty.set(mainiamge);
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception ignored) {
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);
    }


    public static void main(String[] args) {
        launch(args);
    }

    class WebCamInfo {
        private String webCamName ;
        private int webCamIndex ;

        String getWebCamName() {
            return webCamName;
        }

        void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }
        int getWebCamIndex() {
            return webCamIndex;
        }
        void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }
    }

    private void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (webCam != null) {
                    disposeWebCamCamera();
                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.open();
                } else {
                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.open();
                }
                startWebCamStream();
                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();
        bottomCameraControlPane.setDisable(false);
        //btnCamreaStart.setDisable(true);
    }
    private void disposeWebCamCamera() {
        stopCamera = true;
        webCam.close();
        Webcam.shutdown();
        btnCamreaStart.setDisable(true);
        btnCamreaStop.setDisable(true);
    }

    private void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
        btnCamreaStop.setDisable(false);
        btnCamreaStart.setDisable(true);
    }

    private void stopWebCamCamera() {
        stopCamera = true;
        btnCamreaStart.setDisable(false);
        btnCamreaStop.setDisable(true);
    }
}
