package com.grinkrug.javafx;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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
import java.awt.image.BufferedImage;

import static javafx.geometry.Pos.CENTER;

public class JavaFX3DWebCam extends Application {
    static private int percentage;
    static private String deformationType;

    static FlowPane leftPane;
    static BorderPane root;

    private GridPane topPane;
    private BorderPane webCamPane;
    private ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private FlowPane bottomCameraControlPane;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    Slider horizontalExtension;
    Slider verticalExtension;
    Slider verticalNarrowing;
    Slider horizontalNarrowing;
    Slider convexity;
    Slider concavity;
    Slider verticalZigzag;
    Slider horizontalZigzag;

    Text horizontalExtensionText;
    Text verticalExtensionText;
    Text verticalNarrowingText;
    Text horizontalNarrowingText;
    Text convexityText;
    Text concavityText;
    Text verticalZigzagText;
    Text horizontalZigzagText;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX 3D Web Camera TV");
        root = new BorderPane();
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
        root.setBottom(bottomCameraControlPane);

        leftPane = create3DPane(imageProperty);
        root.setLeft(leftPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();

        Platform.runLater(this::setImageViewSize);
    }

    private FlowPane create3DPane(ObjectProperty<Image> imageProperty){
        return new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
    }

//    private void createCameraControls() {
//        btnCamreaStop = new Button();
//        btnCamreaStop.setOnAction(arg0 -> stopWebCamCamera());
//        btnCamreaStop.setText("Stop Camera");
//        btnCamreaStart = new Button();
//        btnCamreaStart.setOnAction(arg0 -> startWebCamCamera());
//        btnCamreaStart.setText("Start Camera");
//        Button btnCameraDispose = new Button();
//        btnCameraDispose.setText("Dispose Camera");
//        btnCameraDispose.setOnAction(arg0 -> disposeWebCamCamera());
//        bottomCameraControlPane.getChildren().add(btnCamreaStart);
//        bottomCameraControlPane.getChildren().add(btnCamreaStop);
//        bottomCameraControlPane.getChildren().add(btnCameraDispose);
//    }

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
        horizontalExtension = new Slider();
        verticalExtension = new Slider();
        verticalNarrowing = new Slider();
        horizontalNarrowing = new Slider();
        convexity = new Slider();
        concavity = new Slider();
        verticalZigzag = new Slider();
        horizontalZigzag = new Slider();

        horizontalExtensionText = new Text("Horizontal Extension percentage: 0\t\t");
        verticalExtensionText = new Text("Vertical Extension percentage: 0\t\t");
        verticalNarrowingText = new Text("Vertical Narrowing percentage: 0\t\t");
        horizontalNarrowingText = new Text("Horizontal Narrowing percentage: 0\t\t");
        convexityText = new Text("Convexity percentage: 0\t\t");
        concavityText = new Text("Concavity percentage: 0\t\t");
        verticalZigzagText = new Text("Vertical Zigzag percentage: 0\t\t");
        horizontalZigzagText = new Text("Horizontal Zigzag percentage: 0\t\t");

        horizontalExtension.setMin(0);
        horizontalExtension.setMax(100);
        horizontalExtension.setValue(0);
        horizontalExtension.setShowTickLabels(true);
        horizontalExtension.setShowTickMarks(true);
        horizontalExtension.setMajorTickUnit(50);
        horizontalExtension.setMinorTickCount(5);
        horizontalExtension.setBlockIncrement(10);

        verticalExtension.setMin(0);
        verticalExtension.setMax(100);
        verticalExtension.setValue(0);
        verticalExtension.setShowTickLabels(true);
        verticalExtension.setShowTickMarks(true);
        verticalExtension.setMajorTickUnit(50);
        verticalExtension.setMinorTickCount(5);
        verticalExtension.setBlockIncrement(10);

        verticalNarrowing.setMin(0);
        verticalNarrowing.setMax(100);
        verticalNarrowing.setValue(0);
        verticalNarrowing.setShowTickLabels(true);
        verticalNarrowing.setShowTickMarks(true);
        verticalNarrowing.setMajorTickUnit(50);
        verticalNarrowing.setMinorTickCount(5);
        verticalNarrowing.setBlockIncrement(10);

        horizontalNarrowing.setMin(0);
        horizontalNarrowing.setMax(100);
        horizontalNarrowing.setValue(0);
        horizontalNarrowing.setShowTickLabels(true);
        horizontalNarrowing.setShowTickMarks(true);
        horizontalNarrowing.setMajorTickUnit(50);
        horizontalNarrowing.setMinorTickCount(5);
        horizontalNarrowing.setBlockIncrement(10);

        convexity.setMin(0);
        convexity.setMax(100);
        convexity.setValue(0);
        convexity.setShowTickLabels(true);
        convexity.setShowTickMarks(true);
        convexity.setMajorTickUnit(50);
        convexity.setMinorTickCount(5);
        convexity.setBlockIncrement(10);

        concavity.setMin(0);
        concavity.setMax(100);
        concavity.setValue(0);
        concavity.setShowTickLabels(true);
        concavity.setShowTickMarks(true);
        concavity.setMajorTickUnit(50);
        concavity.setMinorTickCount(5);
        concavity.setBlockIncrement(10);

        verticalZigzag.setMin(0);
        verticalZigzag.setMax(100);
        verticalZigzag.setValue(0);
        verticalZigzag.setShowTickLabels(true);
        verticalZigzag.setShowTickMarks(true);
        verticalZigzag.setMajorTickUnit(50);
        verticalZigzag.setMinorTickCount(5);
        verticalZigzag.setBlockIncrement(10);

        horizontalZigzag.setMin(0);
        horizontalZigzag.setMax(100);
        horizontalZigzag.setValue(0);
        horizontalZigzag.setShowTickLabels(true);
        horizontalZigzag.setShowTickMarks(true);
        horizontalZigzag.setMajorTickUnit(50);
        horizontalZigzag.setMinorTickCount(5);
        horizontalZigzag.setBlockIncrement(10);

        horizontalExtension.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)horizontalExtension.getValue();
                deformationType = "horizontal extension";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                horizontalExtensionText.setText("Horizontal Extension percentage: " + (int)horizontalExtension.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        verticalExtension.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)verticalExtension.getValue();
                deformationType = "vertical extension";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                verticalExtensionText.setText("Vertical Extension percentage: " + (int)verticalExtension.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        verticalNarrowing.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)verticalNarrowing.getValue();
                deformationType = "vertical narrowing";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                verticalNarrowingText.setText("Vertical Narrowing percentage: " + (int)verticalNarrowing.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        horizontalNarrowing.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)horizontalNarrowing.getValue();
                deformationType = "horizontal narrowing";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: " + (int)horizontalNarrowing.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        convexity.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)convexity.getValue();
                deformationType = "convexity";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                convexityText.setText("Convexity percentage: " + (int)convexity.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        concavity.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)concavity.getValue();
                deformationType = "concavity";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                concavityText.setText("Concavity percentage: " + (int)concavity.getValue());

                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        verticalZigzag.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)verticalZigzag.getValue();
                deformationType = "vertical zigzag";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                verticalZigzagText.setText("Vertical Zigzag percentage " + (int)verticalZigzag.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                horizontalZigzag.setValue(0);
                horizontalZigzagText.setText("Horizontal Zigzag percentage: 0\t\t");
            }
        });

        horizontalZigzag.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                percentage = (int)horizontalZigzag.getValue();
                deformationType = "horizontal zigzag";
                leftPane = new JavaFX3DWorld(imageProperty).get3DWorldPane(percentage, deformationType);
                root.setLeft(leftPane);
                horizontalZigzagText.setText("Horizontal Zigzag percentage " + (int)horizontalZigzag.getValue());

                concavity.setValue(0);
                concavityText.setText("Concavity percentage: 0\t\t");
                convexity.setValue(0);
                convexityText.setText("Convexity percentage: 0\t\t");
                verticalExtension.setValue(0);
                verticalExtensionText.setText("Vertical Extension percentage: 0\t\t");
                verticalNarrowing.setValue(0);
                verticalNarrowingText.setText("Vertical Narrowing percentage: 0\t\t");
                horizontalExtension.setValue(0);
                horizontalExtensionText.setText("Horizontal Extension percentage: 0\t\t");
                horizontalNarrowing.setValue(0);
                horizontalNarrowingText.setText("Horizontal Narrowing percentage: 0\t\t");
                verticalZigzag.setValue(0);
                verticalZigzagText.setText("Vertical Zigzag percentage: 0\t\t");
            }
        });

        int webCamCounter = 0;
        Label lbInfoLabel = new Label("Select Your WebCam Camera");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        GridPane.setConstraints(lbInfoLabel, 0, 0);
        GridPane.setConstraints(horizontalExtensionText, 0, 1);
        GridPane.setConstraints(horizontalExtension, 1, 1);
        GridPane.setConstraints(horizontalNarrowingText, 0, 2);
        GridPane.setConstraints(horizontalNarrowing, 1, 2);
        GridPane.setConstraints(horizontalZigzagText, 0, 3);
        GridPane.setConstraints(horizontalZigzag, 1, 3);
        GridPane.setConstraints(concavityText, 0, 4);
        GridPane.setConstraints(concavity, 1, 4);
        GridPane.setConstraints(verticalExtensionText, 2, 1);
        GridPane.setConstraints(verticalExtension, 3, 1);
        GridPane.setConstraints(verticalNarrowingText, 2, 2);
        GridPane.setConstraints(verticalNarrowing, 3, 2);
        GridPane.setConstraints(verticalZigzagText, 2, 3);
        GridPane.setConstraints(verticalZigzag, 3, 3);
        GridPane.setConstraints(convexityText, 2, 4);
        GridPane.setConstraints(convexity, 3, 4);

        topPane.getChildren().addAll(
                horizontalExtensionText,
                horizontalExtension,
                verticalExtensionText,
                verticalExtension,
                verticalNarrowingText,
                verticalNarrowing,
                horizontalNarrowingText,
                horizontalNarrowing,
                convexityText,
                convexity,
                concavityText,
                concavity,
                verticalZigzagText,
                verticalZigzag,
                horizontalZigzagText,
                horizontalZigzag,
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
                            Platform.runLater(() -> {
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

//        String getWebCamName() {
//            return webCamName;
//        }

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
                    //disposeWebCamCamera();
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
    }
//    private void disposeWebCamCamera() {
//        stopCamera = true;
//        webCam.close();
//        Webcam.shutdown();
//        //btnCamreaStart.setDisable(true);
//        //btnCamreaStop.setDisable(true);
//    }
//
//    private void startWebCamCamera() {
//        stopCamera = false;
//        startWebCamStream();
//        btnCamreaStop.setDisable(false);
//        btnCamreaStart.setDisable(true);
//    }
//
//    private void stopWebCamCamera() {
//        stopCamera = true;
//        btnCamreaStart.setDisable(false);
//        btnCamreaStop.setDisable(true);
//    }
}
