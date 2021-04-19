package com.grinkrug.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

class JavaFX3DWorld {
    private final Group root = new Group();
    private final Xform world = new Xform();

    private final Xform contentGroup = new Xform();

    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();

    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 5.0; //70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 190.0; //320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    // for texture map:
    private final BooleanProperty diffuseMap = new SimpleBooleanProperty(true);
    private final ObjectProperty<Image> imageProperty;

    JavaFX3DWorld(ObjectProperty<Image> imageProperty){
        this.imageProperty = imageProperty;
    }

    FlowPane get3DWorldPane(int percentage, String deformationType){
        FlowPane pane = new FlowPane();

        root.getChildren().add(world);
        buildCamera();
        buildContent(percentage, deformationType);

        SubScene subScene = new SubScene(root, 500, 400);
        subScene.setFill(Color.GREY);
        handleKeyboard(subScene);
        handleMouse(subScene);

        subScene.setCamera(camera);
        pane.getChildren().add(subScene);

        return pane;
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildContent(int percentage, String deformationType) {
        float xPercantage = 0;
        float yPercentage = 0;
        if (deformationType == "vertical narrowing")
            yPercentage = (float) 0.1 * percentage;
        else if (deformationType == "vertical extension")
            yPercentage = (float) -0.1 * percentage;
        else if (deformationType == "horizontal narrowing")
            xPercantage = (float) -percentage;
        else if (deformationType == "horizontal extension")
            xPercantage = (float) percentage;

        Xform contentXform = new Xform();
        TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);

        triangleMesh.getPoints().addAll(
                -100, 100, 0,
                -80, 100, 0,
                -60, 100, 0,
                -40, 100, 0,
                -20 - (float)0.01 * xPercantage, 100, 0,
                0, 100, 0,
                20 + (float)0.01 * xPercantage, 100, 0,
                40, 100, 0,
                60, 100, 0,
                80, 100, 0,
                100, 100, 0,

                -100, 80, 0,
                -80, 80, 0,
                -60, 80, 0,
                -40, 80, 0,
                -20 - (float)0.02 * xPercantage, 80, 0,
                0, 80, 0,
                20 + (float)0.02 * xPercantage, 80, 0,
                40, 80, 0,
                60, 80, 0,
                80, 80, 0,
                100, 80, 0,

                -100, 60 + yPercentage, 0,
                -80, 60 + yPercentage, 0,
                -60, 60 + yPercentage, 0,
                -40, 60 + yPercentage, 0,
                -20 - (float)0.03 * xPercantage, 60 + yPercentage, 0,
                0, 60 + yPercentage, 0,
                20 + (float)0.03 * xPercantage, 60 + yPercentage, 0,
                40, 60 + yPercentage, 0,
                60, 60 + yPercentage, 0,
                80, 60 + yPercentage, 0,
                100, 60 + yPercentage, 0,

                -100, 40 + yPercentage, 0,
                -80, 40 + yPercentage, 0,
                -60, 40 + yPercentage, 0,
                -40, 40 + yPercentage, 0,
                -20 - (float)0.04 * xPercantage, 40 + yPercentage, 0,
                0, 40 + yPercentage, 0,
                20 + (float)0.04 * xPercantage, 40 + yPercentage, 0,
                40, 40 + yPercentage, 0,
                60, 40 + yPercentage, 0,
                80, 40 + yPercentage, 0,
                100, 40 + yPercentage, 0,

                -100, 20 + yPercentage, 0,
                -80, 20 + yPercentage, 0,
                -60, 20 + yPercentage, 0,
                -40, 20 + yPercentage, 0,
                -20 - (float)0.05 * xPercantage, 20 + yPercentage, 0,
                0, 20 + yPercentage, 0,
                20 + (float)0.05 * xPercantage, 20 + yPercentage, 0,
                40, 20 + yPercentage, 0,
                60, 20 + yPercentage, 0,
                80, 20 + yPercentage, 0,
                100, 20 + yPercentage, 0,

                -100, 0, 0,
                -80, 0, 0,
                -60, 0, 0,
                -40, 0, 0,
                -20 - (float)0.06 * xPercantage, 0, 0,
                0, 0, 0,
                20 + (float)0.06 * xPercantage, 0, 0,
                40, 0, 0,
                60, 0, 0,
                80, 0, 0,
                100, 0, 0,

                -100, -20 - yPercentage, 0,
                -80, -20 - yPercentage, 0,
                -60, -20 - yPercentage, 0,
                -40, -20 - yPercentage, 0,
                -20 - (float)0.05 * xPercantage, -20 - yPercentage, 0,
                0, -20 - yPercentage, 0,
                20 + (float)0.05 * xPercantage, -20 - yPercentage, 0,
                40, -20 - yPercentage, 0,
                60, -20 - yPercentage, 0,
                80, -20 - yPercentage, 0,
                100, -20 - yPercentage, 0,

                -100, -40 - yPercentage, 0,
                -80, -40 - yPercentage, 0,
                -60, -40 - yPercentage, 0,
                -40, -40 - yPercentage, 0,
                -20 - (float)0.04 * xPercantage, -40 - yPercentage, 0,
                0, -40 - yPercentage, 0,
                20 + (float)0.04 * xPercantage, -40 - yPercentage, 0,
                40, -40 - yPercentage, 0,
                60, -40 - yPercentage, 0,
                80, -40 - yPercentage, 0,
                100, -40 - yPercentage, 0,

                -100, -60, 0,
                -80, -60, 0,
                -60, -60, 0,
                -40, -60, 0,
                -20 - (float)0.03 * xPercantage, -60, 0,
                0, -60, 0,
                20 + (float)0.03 * xPercantage, -60, 0,
                40, -60, 0,
                60, -60, 0,
                80, -60, 0,
                100, -60, 0,

                -100, -80 - yPercentage, 0,
                -80, -80 - yPercentage, 0,
                -60, -80 - yPercentage, 0,
                -40, -80 - yPercentage, 0,
                -20 - (float)0.02 * xPercantage, -80 - yPercentage, 0,
                0, -80 - yPercentage, 0,
                20 + (float)0.02 * xPercantage, -80 - yPercentage, 0,
                40, -80 - yPercentage, 0,
                60, -80 - yPercentage, 0,
                80, -80 - yPercentage, 0,
                100, -80 - yPercentage, 0,

                -100, -100, 0,
                -80, -100, 0,
                -60, -100, 0,
                -40, -100, 0,
                -20 - (float)0.01 * xPercantage, -100, 0,
                0, -100, 0,
                20 + (float)0.01 * xPercantage, -100, 0,
                40, -100, 0,
                60, -100, 0,
                80, -100, 0,
                100, -100, 0
        );

//        if (deformationType == "narrowing"){
//            triangleMesh.getPoints().addAll(
//                    -100, 100, 0,
//                    -80, 100, 0,
//                    -60, 100, 0,
//                    -40, 100, 0,
//                    -20, 100, 0,
//                    0, 100, 0,
//                    20, 100, 0,
//                    40, 100, 0,
//                    60, 100, 0,
//                    80, 100, 0,
//                    100, 100, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60 + currentPercentage, 0,
//                    -80, 60 + currentPercentage, 0,
//                    -60, 60 + currentPercentage, 0,
//                    -40, 60 + currentPercentage, 0,
//                    -20, 60 + currentPercentage, 0,
//                    0, 60 + currentPercentage, 0,
//                    20, 60 + currentPercentage, 0,
//                    40, 60 + currentPercentage, 0,
//                    60, 60 + currentPercentage, 0,
//                    80, 60 + currentPercentage, 0,
//                    100, 60 + currentPercentage, 0,
//
//                    -100, 40 + currentPercentage, 0,
//                    -80,  40 + currentPercentage, 0,
//                    -60,  40 + currentPercentage, 0,
//                    -40,  40 + currentPercentage, 0,
//                    -20, 40 + currentPercentage, 0,
//                    0, 40 + currentPercentage, 0,
//                    20, 40 + currentPercentage, 0,
//                    40, 40 + currentPercentage, 0,
//                    60, 40 + currentPercentage, 0,
//                    80, 40 + currentPercentage, 0,
//                    100, 40 + currentPercentage, 0,
//
//                    -100, 20 + currentPercentage, 0,
//                    -80,  20 + currentPercentage, 0,
//                    -60,  20 + currentPercentage, 0,
//                    -40,  20 + currentPercentage, 0,
//                    -20, 20 + currentPercentage, 0,
//                    0, 20 + currentPercentage, 0,
//                    20, 20 + currentPercentage, 0,
//                    40, 20 + currentPercentage, 0,
//                    60, 20 + currentPercentage, 0,
//                    80, 20 + currentPercentage, 0,
//                    100, 20 + currentPercentage, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 0,
//                    -20, 0, 0,
//                    0, 0, 0,
//                    20, 0, 0,
//                    40, 0, 0,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20 - currentPercentage, 0,
//                    -80, -20 - currentPercentage, 0,
//                    -60, -20 - currentPercentage, 0,
//                    -40, -20 - currentPercentage, 0,
//                    -20, -20 - currentPercentage, 0,
//                    0, -20 - currentPercentage, 0,
//                    20, -20 - currentPercentage, 0,
//                    40, -20 - currentPercentage, 0,
//                    60, -20 - currentPercentage, 0,
//                    80, -20 - currentPercentage, 0,
//                    100, -20 - currentPercentage, 0,
//
//                    -100, -40 - currentPercentage, 0,
//                    -80,  -40 - currentPercentage, 0,
//                    -60,  -40 - currentPercentage, 0,
//                    -40,  -40 - currentPercentage, 0,
//                    -20, -40 - currentPercentage, 0,
//                    0, -40 - currentPercentage, 0,
//                    20, -40 - currentPercentage, 0,
//                    40, -40 - currentPercentage, 0,
//                    60, -40 - currentPercentage, 0,
//                    80, -40 - currentPercentage, 0,
//                    100, -40 - currentPercentage, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80 - currentPercentage, 0,
//                    -80,  -80 - currentPercentage, 0,
//                    -60,  -80 - currentPercentage, 0,
//                    -40,  -80 - currentPercentage, 0,
//                    -20, -80 - currentPercentage, 0,
//                    0, -80 - currentPercentage, 0,
//                    20, -80 - currentPercentage, 0,
//                    40, -80 - currentPercentage, 0,
//                    60, -80 - currentPercentage, 0,
//                    80, -80 - currentPercentage, 0,
//                    100, -80 - currentPercentage, 0,
//
//                    -100, -100, 0,
//                    -80,  -100, 0,
//                    -60,  -100, 0,
//                    -40,  -100, 0,
//                    -20, -100, 0,
//                    0, -100, 0,
//                    20, -100, 0,
//                    40, -100, 0,
//                    60, -100, 0,
//                    80, -100, 0,
//                    100, -100, 0
//            );
//        }
//        else if (deformationType == "extension"){
//                        triangleMesh.getPoints().addAll(
//                    -100, 100, 0,
//                    -80, 100, 0,
//                    -60, 100, 0,
//                    -40, 100, 0,
//                    -20, 100, 0,
//                    0, 100, 0,
//                    20, 100, 0,
//                    40, 100, 0,
//                    60, 100, 0,
//                    80, 100, 0,
//                    100, 100, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60, 0,
//                    -80, 60, 0,
//                    -60, 60, 0,
//                    -40, 60, 0,
//                    -20, 60, 0,
//                    0, 60, 0,
//                    20, 60, 0,
//                    40, 60, 0,
//                    60, 60, 0,
//                    80, 60, 0,
//                    100, 60, 0,
//
//                    -100, 40, 0,
//                    -80,  40, 0,
//                    -60,  40, 0,
//                    -40,  40, 0,
//                    -20, 40, 0,
//                    0, 40, 0,
//                    20, 40, 0,
//                    40, 40, 0,
//                    60, 40, 0,
//                    80, 40, 0,
//                    100, 40, 0,
//
//                    -100, 20, 0,
//                    -80,  20, 0,
//                    -60,  20, 0,
//                    -40,  20, 0,
//                    -20, 20, 0,
//                    0, 20, 0,
//                    20, 20, 0,
//                    40, 20, 0,
//                    60, 20, 0,
//                    80, 20, 0,
//                    100, 20, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 40,
//                    -20, 0, 10,
//                    0, 0, 25,
//                    20, 0, 15,
//                    40, 0, 60,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20, 0,
//                    -80, -20, 0,
//                    -60, -20, 0,
//                    -40, -20, 0,
//                    -20, -20, 0,
//                    0, -20, 0,
//                    20, -20, 0,
//                    40, -20, 0,
//                    60, -20, 0,
//                    80, -20, 0,
//                    100, -20, 0,
//
//                    -100, -40, 0,
//                    -80,  -40, 0,
//                    -60,  -40, 0,
//                    -40,  -40, 0,
//                    -20, -40, 0,
//                    0, -40, 0,
//                    20, -40, 0,
//                    40, -40, 0,
//                    60, -40, 0,
//                    80, -40, 0,
//                    100, -40, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80, 0,
//                    -80,  -80, 0,
//                    -60,  -80, 0,
//                    -40,  -80, 0,
//                    -20, -80, 0,
//                    0, -80, 0,
//                    20, -80, 0,
//                    40, -80, 0,
//                    60, -80, 0,
//                    80, -80, 0,
//                    100, -80, 0,
//
//                    -100, -100, 0,
//                    -80,  -100, 0,
//                    -60,  -100, 0,
//                    -40,  -100, 0,
//                    -20, -100, 0,
//                    0, -100, 0,
//                    20, -100, 0,
//                    40, -100, 0,
//                    60, -100, 0,
//                    80, -100, 0,
//                    100, -100, 0
//            );
//            triangleMesh.getPoints().addAll(
//                    -100, 100, 0,
//                    -80, 100, 0,
//                    -60, 100, 0,
//                    -40, 100, 0,
//                    -20, 100, 0,
//                    0, 100, 0,
//                    20, 100, 0,
//                    40, 100, 0,
//                    60, 100, 0,
//                    80, 100, 0,
//                    100, 100, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60 + currentPercentage, 0,
//                    -80, 60 + currentPercentage, 0,
//                    -60, 60 + currentPercentage, 0,
//                    -40, 60 + currentPercentage, 0,
//                    -20, 60 + currentPercentage, 0,
//                    0, 60 + currentPercentage, 0,
//                    20, 60 + currentPercentage, 0,
//                    40, 60 + currentPercentage, 0,
//                    60, 60 + currentPercentage, 0,
//                    80, 60 + currentPercentage, 0,
//                    100, 60 + currentPercentage, 0,
//
//                    -100, 40 + currentPercentage, 0,
//                    -80,  40 + currentPercentage, 0,
//                    -60,  40 + currentPercentage, 0,
//                    -40,  40 + currentPercentage, 0,
//                    -20, 40 + currentPercentage, 0,
//                    0, 40 + currentPercentage, 0,
//                    20, 40 + currentPercentage, 0,
//                    40, 40 + currentPercentage, 0,
//                    60, 40 + currentPercentage, 0,
//                    80, 40 + currentPercentage, 0,
//                    100, 40 + currentPercentage, 0,
//
//                    -100, 20 + currentPercentage, 0,
//                    -80,  20 + currentPercentage, 0,
//                    -60,  20 + currentPercentage, 0,
//                    -40,  20 + currentPercentage, 0,
//                    -20, 20 + currentPercentage, 0,
//                    0, 20 + currentPercentage, 0,
//                    20, 20 + currentPercentage, 0,
//                    40, 20 + currentPercentage, 0,
//                    60, 20 + currentPercentage, 0,
//                    80, 20 + currentPercentage, 0,
//                    100, 20 + currentPercentage, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 0,
//                    -20, 0, 0,
//                    0, 0, 0,
//                    20, 0, 0,
//                    40, 0, 0,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20 - currentPercentage, 0,
//                    -80, -20 - currentPercentage, 0,
//                    -60, -20 - currentPercentage, 0,
//                    -40, -20 - currentPercentage, 0,
//                    -20, -20 - currentPercentage, 0,
//                    0, -20 - currentPercentage, 0,
//                    20, -20 - currentPercentage, 0,
//                    40, -20 - currentPercentage, 0,
//                    60, -20 - currentPercentage, 0,
//                    80, -20 - currentPercentage, 0,
//                    100, -20 - currentPercentage, 0,
//
//                    -100, -40 - currentPercentage, 0,
//                    -80,  -40 - currentPercentage, 0,
//                    -60,  -40 - currentPercentage, 0,
//                    -40,  -40 - currentPercentage, 0,
//                    -20, -40 - currentPercentage, 0,
//                    0, -40 - currentPercentage, 0,
//                    20, -40 - currentPercentage, 0,
//                    40, -40 - currentPercentage, 0,
//                    60, -40 - currentPercentage, 0,
//                    80, -40 - currentPercentage, 0,
//                    100, -40 - currentPercentage, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80 - currentPercentage, 0,
//                    -80,  -80 - currentPercentage, 0,
//                    -60,  -80 - currentPercentage, 0,
//                    -40,  -80 - currentPercentage, 0,
//                    -20, -80 - currentPercentage, 0,
//                    0, -80 - currentPercentage, 0,
//                    20, -80 - currentPercentage, 0,
//                    40, -80 - currentPercentage, 0,
//                    60, -80 - currentPercentage, 0,
//                    80, -80 - currentPercentage, 0,
//                    100, -80 - currentPercentage, 0,
//
//                    -100, -100, 0,
//                    -80,  -100, 0,
//                    -60,  -100, 0,
//                    -40,  -100, 0,
//                    -20, -100, 0,
//                    0, -100, 0,
//                    20, -100, 0,
//                    40, -100, 0,
//                    60, -100, 0,
//                    80, -100, 0,
//                    100, -100, 0
//            );
//
// }
//            triangleMesh.getPoints().addAll(
//                    -100, 100, 0,
//                    -80, 100, 0,
//                    -60, 100, 0,
//                    -40, 100, 0,
//                    -20, 100, 0,
//                    0, 100, 0,
//                    20, 100, 0,
//                    40, 100, 0,
//                    60, 100, 0,
//                    80, 100, 0,
//                    100, 100, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60, 0,
//                    -80, 60, 0,
//                    -60, 60, 0,
//                    -40, 60, 0,
//                    -20, 60, 0,
//                    0, 60, 0,
//                    20, 60, 0,
//                    40, 60, 0,
//                    60, 60, 0,
//                    80, 60, 0,
//                    100, 60, 0,
//
//                    -100, 40, 0,
//                    -80,  40, 0,
//                    -60,  40, 0,
//                    -40,  40, 0,
//                    -20, 40, 0,
//                    0, 40, 0,
//                    20, 40, 0,
//                    40, 40, 0,
//                    60, 40, 0,
//                    80, 40, 0,
//                    100, 40, 0,
//
//                    -100, 20, 0,
//                    -80,  20, 0,
//                    -60,  20, 0,
//                    -40,  20, 0,
//                    -20, 20, 0,
//                    0, 20, 0,
//                    20, 20, 0,
//                    40, 20, 0,
//                    60, 20, 0,
//                    80, 20, 0,
//                    100, 20, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 40,
//                    -20, 0, 10,
//                    0, 0, 25,
//                    20, 0, 15,
//                    40, 0, 60,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20, 0,
//                    -80, -20, 0,
//                    -60, -20, 0,
//                    -40, -20, 0,
//                    -20, -20, 0,
//                    0, -20, 0,
//                    20, -20, 0,
//                    40, -20, 0,
//                    60, -20, 0,
//                    80, -20, 0,
//                    100, -20, 0,
//
//                    -100, -40, 0,
//                    -80,  -40, 0,
//                    -60,  -40, 0,
//                    -40,  -40, 0,
//                    -20, -40, 0,
//                    0, -40, 0,
//                    20, -40, 0,
//                    40, -40, 0,
//                    60, -40, 0,
//                    80, -40, 0,
//                    100, -40, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80, 0,
//                    -80,  -80, 0,
//                    -60,  -80, 0,
//                    -40,  -80, 0,
//                    -20, -80, 0,
//                    0, -80, 0,
//                    20, -80, 0,
//                    40, -80, 0,
//                    60, -80, 0,
//                    80, -80, 0,
//                    100, -80, 0,
//
//                    -100, -100, 0,
//                    -80,  -100, 0,
//                    -60,  -100, 0,
//                    -40,  -100, 0,
//                    -20, -100, 0,
//                    0, -100, 0,
//                    20, -100, 0,
//                    40, -100, 0,
//                    60, -100, 0,
//                    80, -100, 0,
//                    100, -100, 0
//            );


//        if (deformationType == "narrowing"){
//            triangleMesh.getPoints().addAll(
//                    -100, 100 - currentPercentage, 0,
//                    -80, 100 - currentPercentage, 0,
//                    -60, 100 - currentPercentage, 0,
//                    -40, 100 - currentPercentage, 0,
//                    -20, 100 - currentPercentage, 0,
//                    0, 100 - currentPercentage, 0,
//                    20, 100 - currentPercentage, 0,
//                    40, 100 - currentPercentage, 0,
//                    60, 100 - currentPercentage, 0,
//                    80, 100 - currentPercentage, 0,
//                    100, 100 - currentPercentage, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60 - currentPercentage, 0,
//                    -80,  60 - currentPercentage, 0,
//                    -60,  60 - currentPercentage, 0,
//                    -40,  60 - currentPercentage, 0,
//                    -20, 60 - currentPercentage, 0,
//                    0, 60 - currentPercentage, 0,
//                    20, 60 - currentPercentage, 0,
//                    40, 60 - currentPercentage, 0,
//                    60, 60 - currentPercentage, 0,
//                    80, 60 - currentPercentage, 0,
//                    100, 60 - currentPercentage, 0,
//
//                    -100, 40 - currentPercentage, 0,
//                    -80,  40 - currentPercentage, 0,
//                    -60,  40 - currentPercentage, 0,
//                    -40,  40 - currentPercentage, 0,
//                    -20, 40 - currentPercentage, 0,
//                    0, 40 - currentPercentage, 0,
//                    20, 40 - currentPercentage, 0,
//                    40, 40 - currentPercentage, 0,
//                    60, 40 - currentPercentage, 0,
//                    80, 40 - currentPercentage, 0,
//                    100, 40 - currentPercentage, 0,
//
//                    -100, 20 - currentPercentage, 0,
//                    -80,  20 - currentPercentage, 0,
//                    -60,  20 - currentPercentage, 0,
//                    -40,  20 - currentPercentage, 0,
//                    -20, 20 - currentPercentage, 0,
//                    0, 20 - currentPercentage, 0,
//                    20, 20 - currentPercentage, 0,
//                    40, 20 - currentPercentage, 0,
//                    60, 20 - currentPercentage, 0,
//                    80, 20 - currentPercentage, 0,
//                    100, 20 - currentPercentage, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 0,
//                    -20, 0, 0,
//                    0, 0, 0,
//                    20, 0, 0,
//                    40, 0, 0,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20 + currentPercentage, 0,
//                    -80,  -20 + currentPercentage, 0,
//                    -60,  -20 + currentPercentage, 0,
//                    -40,  -20 + currentPercentage, 0,
//                    -20, -20 + currentPercentage, 0,
//                    0, -20 + currentPercentage, 0,
//                    20, -20 + currentPercentage, 0,
//                    40, -20 + currentPercentage, 0,
//                    60, -20 + currentPercentage, 0,
//                    80, -20 + currentPercentage, 0,
//                    100, -20 + currentPercentage, 0,
//
//                    -100, -40 + currentPercentage, 0,
//                    -80,  -40 + currentPercentage, 0,
//                    -60,  -40 + currentPercentage, 0,
//                    -40,  -40 + currentPercentage, 0,
//                    -20, -40 + currentPercentage, 0,
//                    0, -40 + currentPercentage, 0,
//                    20, -40 + currentPercentage, 0,
//                    40, -40 + currentPercentage, 0,
//                    60, -40 + currentPercentage, 0,
//                    80, -40 + currentPercentage, 0,
//                    100, -40 + currentPercentage, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80 + currentPercentage, 0,
//                    -80,  -80 + currentPercentage, 0,
//                    -60,  -80 + currentPercentage, 0,
//                    -40,  -80 + currentPercentage, 0,
//                    -20, -80 + currentPercentage, 0,
//                    0, -80 + currentPercentage, 0,
//                    20, -80 + currentPercentage, 0,
//                    40, -80 + currentPercentage, 0,
//                    60, -80 + currentPercentage, 0,
//                    80, -80 + currentPercentage, 0,
//                    100, -80 + currentPercentage, 0,
//
//                    -100, -100 + currentPercentage, 0,
//                    -80,  -100 + currentPercentage, 0,
//                    -60,  -100 + currentPercentage, 0,
//                    -40,  -100 + currentPercentage, 0,
//                    -20, -100 + currentPercentage, 0,
//                    0, -100 + currentPercentage, 0,
//                    20, -100 + currentPercentage, 0,
//                    40, -100 + currentPercentage, 0,
//                    60, -100 + currentPercentage, 0,
//                    80, -100 + currentPercentage, 0,
//                    100, -100 + currentPercentage, 0
//            );
//        }
//        else if (deformationType == "extension") {
//            triangleMesh.getPoints().addAll(
//                    -100, 100 + currentPercentage, 0,
//                    -80, 100 + currentPercentage, 0,
//                    -60, 100 + currentPercentage, 0,
//                    -40, 100 + currentPercentage, 0,
//                    -20, 100 + currentPercentage, 0,
//                    0, 100 + currentPercentage, 0,
//                    20, 100 + currentPercentage, 0,
//                    40, 100 + currentPercentage, 0,
//                    60, 100 + currentPercentage, 0,
//                    80, 100 + currentPercentage, 0,
//                    100, 100 + currentPercentage, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60, currentPercentage, 0,
//                    -80, 60 + currentPercentage, 0,
//                    -60, 60 + currentPercentage, 0,
//                    -40, 60 + currentPercentage, 0,
//                    -20, 60 + currentPercentage, 0,
//                    0, 60 + currentPercentage, 0,
//                    20, 60 + currentPercentage, 0,
//                    40, 60 + currentPercentage, 0,
//                    60, 60 + currentPercentage, 0,
//                    80, 60 + currentPercentage, 0,
//                    100, 60 + currentPercentage, 0,
//
//                    -100, 40 + currentPercentage, 0,
//                    -80,  40 + currentPercentage, 0,
//                    -60,  40 + currentPercentage, 0,
//                    -40,  40 + currentPercentage, 0,
//                    -20, 40 + currentPercentage, 0,
//                    0, 40 + currentPercentage, 0,
//                    20, 40 + currentPercentage, 0,
//                    40, 40 + currentPercentage, 0,
//                    60, 40 + currentPercentage, 0,
//                    80, 40 + currentPercentage, 0,
//                    100, 40 + currentPercentage, 0,
//
//                    -100, 20 + currentPercentage, 0,
//                    -80,  20 + currentPercentage, 0,
//                    -60,  20 + currentPercentage, 0,
//                    -40,  20 + currentPercentage, 0,
//                    -20, 20 + currentPercentage, 0,
//                    0, 20 + currentPercentage, 0,
//                    20, 20 + currentPercentage, 0,
//                    40, 20 + currentPercentage, 0,
//                    60, 20 + currentPercentage, 0,
//                    80, 20 + currentPercentage, 0,
//                    100, 20 + currentPercentage, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 0,
//                    -20, 0, 0,
//                    0, 0, 0,
//                    20, 0, 0,
//                    40, 0, 0,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20 - currentPercentage, 0,
//                    -80, -20 - currentPercentage, 0,
//                    -60, -20 - currentPercentage, 0,
//                    -40, -20 - currentPercentage, 0,
//                    -20, -20 - currentPercentage, 0,
//                    0, -20 - currentPercentage, 0,
//                    20, -20 - currentPercentage, 0,
//                    40, -20 - currentPercentage, 0,
//                    60, -20 - currentPercentage, 0,
//                    80, -20 - currentPercentage, 0,
//                    100, -20 - currentPercentage, 0,
//
//                    -100, -40 - currentPercentage, 0,
//                    -80,  -40 - currentPercentage, 0,
//                    -60,  -40 - currentPercentage, 0,
//                    -40,  -40 - currentPercentage, 0,
//                    -20, -40 - currentPercentage, 0,
//                    0, -40 - currentPercentage, 0,
//                    20, -40 - currentPercentage, 0,
//                    40, -40 - currentPercentage, 0,
//                    60, -40 - currentPercentage, 0,
//                    80, -40 - currentPercentage, 0,
//                    100, -40 - currentPercentage, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80 - currentPercentage, 0,
//                    -80,  -80 - currentPercentage, 0,
//                    -60,  -80 - currentPercentage, 0,
//                    -40,  -80 - currentPercentage, 0,
//                    -20, -80 - currentPercentage, 0,
//                    0, -80 - currentPercentage, 0,
//                    20, -80 - currentPercentage, 0,
//                    40, -80 - currentPercentage, 0,
//                    60, -80 - currentPercentage, 0,
//                    80, -80 - currentPercentage, 0,
//                    100, -80 - currentPercentage, 0,
//
//                    -100, -100 - currentPercentage, 0,
//                    -80,  -100 - currentPercentage, 0,
//                    -60,  -100 - currentPercentage, 0,
//                    -40,  -100 - currentPercentage, 0,
//                    -20, -100 - currentPercentage, 0,
//                    0, -100 - currentPercentage, 0,
//                    20, -100 - currentPercentage, 0,
//                    40, -100 - currentPercentage, 0,
//                    60, -100 - currentPercentage, 0,
//                    80, -100 - currentPercentage, 0,
//                    100, -100 - currentPercentage, 0
//            );
//        } else {
//            triangleMesh.getPoints().addAll(
//                    -100, 100 + currentPercentage, 0,
//                    -80, 100 + currentPercentage, 0,
//                    -60, 100 + currentPercentage, 0,
//                    -40, 100 + currentPercentage, 0,
//                    -20, 100 + currentPercentage, 0,
//                    0, 100 + currentPercentage, 0,
//                    20, 100 + currentPercentage, 0,
//                    40, 100 + currentPercentage, 0,
//                    60, 100 + currentPercentage, 0,
//                    80, 100 + currentPercentage, 0,
//                    100, 100 + currentPercentage, 0,
//
//                    -100, 80, 0,
//                    -80,  80, 0,
//                    -60,  80, 0,
//                    -40,  80, 0,
//                    -20, 80, 0,
//                    0, 80, 0,
//                    20, 80, 0,
//                    40, 80, 0,
//                    60, 80, 0,
//                    80, 80, 0,
//                    100, 80, 0,
//
//                    -100, 60, currentPercentage, 0,
//                    -80, 60 + currentPercentage, 0,
//                    -60, 60 + currentPercentage, 0,
//                    -40, 60 + currentPercentage, 0,
//                    -20, 60 + currentPercentage, 0,
//                    0, 60 + currentPercentage, 0,
//                    20, 60 + currentPercentage, 0,
//                    40, 60 + currentPercentage, 0,
//                    60, 60 + currentPercentage, 0,
//                    80, 60 + currentPercentage, 0,
//                    100, 60 + currentPercentage, 0,
//
//                    -100, 40 + currentPercentage, 0,
//                    -80,  40 + currentPercentage, 0,
//                    -60,  40 + currentPercentage, 0,
//                    -40,  40 + currentPercentage, 0,
//                    -20, 40 + currentPercentage, 0,
//                    0, 40 + currentPercentage, 0,
//                    20, 40 + currentPercentage, 0,
//                    40, 40 + currentPercentage, 0,
//                    60, 40 + currentPercentage, 0,
//                    80, 40 + currentPercentage, 0,
//                    100, 40 + currentPercentage, 0,
//
//                    -100, 20 + currentPercentage, 0,
//                    -80,  20 + currentPercentage, 0,
//                    -60,  20 + currentPercentage, 0,
//                    -40,  20 + currentPercentage, 0,
//                    -20, 20 + currentPercentage, 0,
//                    0, 20 + currentPercentage, 0,
//                    20, 20 + currentPercentage, 0,
//                    40, 20 + currentPercentage, 0,
//                    60, 20 + currentPercentage, 0,
//                    80, 20 + currentPercentage, 0,
//                    100, 20 + currentPercentage, 0,
//
//                    -100, 0, 0,
//                    -80,  0, 0,
//                    -60,  0, 0,
//                    -40,  0, 0,
//                    -20, 0, 0,
//                    0, 0, 0,
//                    20, 0, 0,
//                    40, 0, 0,
//                    60, 0, 0,
//                    80, 0, 0,
//                    100, 0, 0,
//
//                    -100, -20 - currentPercentage, 0,
//                    -80, -20 - currentPercentage, 0,
//                    -60, -20 - currentPercentage, 0,
//                    -40, -20 - currentPercentage, 0,
//                    -20, -20 - currentPercentage, 0,
//                    0, -20 - currentPercentage, 0,
//                    20, -20 - currentPercentage, 0,
//                    40, -20 - currentPercentage, 0,
//                    60, -20 - currentPercentage, 0,
//                    80, -20 - currentPercentage, 0,
//                    100, -20 - currentPercentage, 0,
//
//                    -100, -40 - currentPercentage, 0,
//                    -80,  -40 - currentPercentage, 0,
//                    -60,  -40 - currentPercentage, 0,
//                    -40,  -40 - currentPercentage, 0,
//                    -20, -40 - currentPercentage, 0,
//                    0, -40 - currentPercentage, 0,
//                    20, -40 - currentPercentage, 0,
//                    40, -40 - currentPercentage, 0,
//                    60, -40 - currentPercentage, 0,
//                    80, -40 - currentPercentage, 0,
//                    100, -40 - currentPercentage, 0,
//
//                    -100, -60, 0,
//                    -80,  -60, 0,
//                    -60,  -60, 0,
//                    -40,  -60, 0,
//                    -20, -60, 0,
//                    0, -60, 0,
//                    20, -60, 0,
//                    40, -60, 0,
//                    60, -60, 0,
//                    80, -60, 0,
//                    100, -60, 0,
//
//                    -100, -80 - currentPercentage, 0,
//                    -80,  -80 - currentPercentage, 0,
//                    -60,  -80 - currentPercentage, 0,
//                    -40,  -80 - currentPercentage, 0,
//                    -20, -80 - currentPercentage, 0,
//                    0, -80 - currentPercentage, 0,
//                    20, -80 - currentPercentage, 0,
//                    40, -80 - currentPercentage, 0,
//                    60, -80 - currentPercentage, 0,
//                    80, -80 - currentPercentage, 0,
//                    100, -80 - currentPercentage, 0,
//
//                    -100, -100 - currentPercentage, 0,
//                    -80,  -100 - currentPercentage, 0,
//                    -60,  -100 - currentPercentage, 0,
//                    -40,  -100 - currentPercentage, 0,
//                    -20, -100 - currentPercentage, 0,
//                    0, -100 - currentPercentage, 0,
//                    20, -100 - currentPercentage, 0,
//                    40, -100 - currentPercentage, 0,
//                    60, -100 - currentPercentage, 0,
//                    80, -100 - currentPercentage, 0,
//                    100, -100 - currentPercentage, 0
//            );
//        }

        triangleMesh.getTexCoords().addAll(
                0,0, 0.1F,0, 0.2F,0, 0.3F,0, 0.4F,0, 0.5F,0,
                0.6F,0, 0.7F,0, 0.8F,0, 0.9F,0, 1F,0,
                0,0.1F, 0.1F,0.1F, 0.2F,0.1F, 0.3F,0.1F, 0.4F,0.1F, 0.5F,0.1F,
                0.6F,0.1F, 0.7F,0.1F, 0.8F,0.1F, 0.9F,0.1F, 1,0.1F,
                0,0.2F, 0.1F,0.2F, 0.2F,0.2F, 0.3F,0.2F, 0.4F,0.2F, 0.5F,0.2F,
                0.6F,0.2F, 0.7F,0.2F, 0.8F,0.2F, 0.9F,0.2F, 1,0.2F,
                0,0.3F, 0.1F,0.3F, 0.2F,0.3F, 0.3F,0.3F, 0.4F,0.3F, 0.5F,0.3F,
                0.6F,0.3F, 0.7F,0.3F, 0.8F,0.3F, 0.9F,0.3F, 1,0.3F,
                0,0.4F, 0.1F,0.4F, 0.2F,0.4F, 0.3F,0.4F, 0.4F,0.4F, 0.5F,0.4F,
                0.6F,0.4F, 0.7F,0.4F, 0.8F,0.4F, 0.9F,0.4F, 1,0.4F,
                0,0.5F, 0.1F,0.5F, 0.2F,0.5F, 0.3F,0.5F, 0.4F,0.5F, 0.5F,0.5F,
                0.6F,0.5F, 0.7F,0.5F, 0.8F,0.5F, 0.9F,0.5F, 1,0.5F,
                0,0.6F, 0.1F,0.6F, 0.2F,0.6F, 0.3F,0.6F, 0.4F,0.6F, 0.5F,0.6F,
                0.6F,0.6F, 0.7F,0.6F, 0.8F,0.6F, 0.9F,0.6F, 1,0.6F,
                0,0.7F, 0.1F,0.7F, 0.2F,0.7F, 0.3F,0.7F, 0.4F,0.7F, 0.5F,0.7F,
                0.6F,0.7F, 0.7F,0.7F, 0.8F,0.7F, 0.9F,0.7F, 1,0.7F,
                0,0.8F, 0.1F,0.8F, 0.2F,0.8F, 0.3F,0.8F, 0.4F,0.8F, 0.5F,0.8F,
                0.6F,0.8F, 0.7F,0.8F, 0.8F,0.8F, 0.9F,0.8F, 1,0.8F,
                0,0.9F, 0.1F,0.9F, 0.2F,0.9F, 0.3F,0.9F, 0.4F,0.9F, 0.5F,0.9F,
                0.6F,0.9F, 0.7F,0.9F, 0.8F,0.9F, 0.9F,0.9F, 1,0.9F,
                0,1F, 0.1F,1F, 0.2F,1F, 0.3F,1F, 0.4F,1F, 0.5F,1F,
                0.6F,1F, 0.7F,1F, 0.8F,1F, 0.9F,1F, 1,1F

//                0,0, 0,0.1F, 0,0.2F, 0,0.3F, 0,0.4F, 0,0.5F,
//                0,0.6F, 0,0.7F, 0,0.8F, 0,0.9F, 0,1,
//                0.1F,0, 0.1F,0.1F, 0.1F,0.2F, 0.1F,0.3F, 0.1F,0.4F,
//                0.1F,0.5F, 0.1F,0.6F, 0.1F,0.7F, 0.1F,0.8F, 0.1F,0.9F, 0.1F,1,
//                0.2F,0, 0.2F,0.1F, 0.2F,0.2F, 0.2F,0.3F, 0.2F,0.4F,
//                0.2F,0.5F, 0.2F,0.6F, 0.2F,0.7F, 0.2F,0.8F, 0.2F,0.9F, 0.2F,1,
//                0.3F,0, 0.3F,0.1F, 0.3F,0.2F, 0.3F,0.3F, 0.3F,0.4F,
//                0.3F,0.5F, 0.3F,0.6F, 0.3F,0.7F, 0.3F,0.8F, 0.3F,0.9F, 0.3F,1,
//                0.4F,0, 0.4F,0.1F, 0.4F,0.2F, 0.4F,0.3F, 0.4F,0.4F,
//                0.4F,0.5F, 0.4F,0.6F, 0.4F,0.7F, 0.4F,0.8F, 0.4F,0.9F, 0.4F,1,
//                0.5F,0, 0.5F,0.1F, 0.5F,0.2F, 0.5F,0.3F, 0.5F,0.4F,
//                0.5F,0.5F, 0.5F,0.6F, 0.5F,0.7F, 0.5F,0.8F, 0.5F,0.9F, 0.5F,1,
//                0.6F,0, 0.6F,0.1F, 0.6F,0.2F, 0.6F,0.3F, 0.6F,0.4F,
//                0.6F,0.5F, 0.6F,0.6F, 0.6F,0.7F, 0.6F,0.8F, 0.6F,0.9F, 0.6F,1,
//                0.7F,0, 0.7F,0.1F, 0.7F,0.2F, 0.7F,0.3F, 0.7F,0.4F,
//                0.7F,0.5F, 0.7F,0.6F, 0.7F,0.7F, 0.7F,0.8F, 0.7F,0.9F, 0.7F,1,
//                0.8F,0, 0.8F,0.1F, 0.8F,0.2F, 0.8F,0.3F, 0.8F,0.4F,
//                0.8F,0.5F, 0.8F,0.6F, 0.8F,0.7F, 0.8F,0.8F, 0.8F,0.9F, 0.8F,1,
//                0.9F,0, 0.9F,0.1F, 0.9F,0.2F, 0.9F,0.3F, 0.9F,0.4F,
//                0.9F,0.5F, 0.9F,0.6F, 0.9F,0.7F, 0.9F,0.9F, 0.9F,0.9F, 0.9F,1,
//                1,0, 1,0.1F, 1,0.2F, 1,0.3F, 1,0.4F, 1,0.5F,
//                1,0.6F, 1,0.7F, 1,0.8F, 1,0.9F, 1,1
                );
        triangleMesh.getFaces().addAll(
                 0,0,  11,11,  12,12,
                        0,0,  12,12,  1,1,
                        12,12,  2,2,  1,1,
                        12,12,  13,13,  2,2,
                        2,2,  13,13,  14,14,
                        2,2,  14,14,  3,3,
                        14,14,  4,4,  3,3,
                        14,14,  15,15,  4,4,
                        4,4,  15,15,  16,16,
                        4,4,  16,16,  5,5,
                        16,16,  6,6,  5,5,
                        16,16,  17,17,  6,6,
                        6,6,  17,17,  18,18,
                        6,6,  18,18,  7,7,
                        18,18,  8,8,  7,7,
                        18,18,  19,19,  8,8,
                        8,8,  19,19,  20,20,
                        8,8,  20,20,  9,9,
                        20,20,  10,10,  9,9,
                        20,20,  21,21,  10,10,

                        11,11,  22,22,  23,23,
                        11,11,  23,23,  12,12,
                        23,23,  13,13,  12,12,
                        23,23,  24,24,  13,13,
                        13,13,  24,24,  25,25,
                        13,13,  25,25,  14,14,
                        25,25,  15,15,  14,14,
                        25,25,  26,26,  15,15,
                        15,15,  26,26,  27,27,
                        15,15,  27,27,  16,16,
                        27,27,  17,17,  16,16,
                        27,27,  28,28,  17,17,
                        17,17,  28,28,  29,29,
                        17,17,  29,29,  18,18,
                        29,29,  19,19,  18,18,
                        29,29,  30,30,  19,19,
                        19,19,  30,30,  31,31,
                        19,19,  31,31,  20,20,
                        31,31,  21,21,  20,20,
                        31,31,  32,32,  21,21,

                        22,22,  33,33,  34,34,
                        22,22,  34,34,  23,23,
                        34,34,  24,24,  23,23,
                        34,34,  35,35,  24,24,
                        24,24,  35,35,  36,36,
                        24,24,  36,36,  25,25,
                        36,36,  26,26,  25,25,
                        36,36,  37,37,  26,26,
                        26,26,  37,37,  38,38,
                        26,26,  38,38,  27,27,
                        38,38,  28,28,  27,27,
                        38,38,  39,39,  28,28,
                        28,28,  39,39,  40,40,
                        28,28,  40,40,  29,29,
                        40,40,  30,30,  29,29,
                        40,40,  41,41,  30,30,
                        30,30,  41,41,  42,42,
                        30,30,  42,42,  31,31,
                        42,42,  32,32,  31,31,
                        42,42,  43,43,  32,32,

                        33,33,  44,44,  45,45,
                        33,33,  45,45,  34,34,
                        45,45,  35,35,  34,34,
                        45,45,  46,46,  35,35,
                        35,35,  46,46,  47,47,
                        35,35,  47,47,  36,36,
                        47,47,  37,37,  36,36,
                        47,47,  48,48,  37,37,
                        37,37,  48,48,  49,49,
                        37,37,  49,49,  38,38,
                        49,49,  39,39,  38,38,
                        49,49,  50,50,  39,39,
                        39,39,  50,50,  51,51,
                        39,39,  51,51,  40,40,
                        51,51,  41,41,  40,40,
                        51,51,  52,52,  41,41,
                        41,41,  52,52,  53,53,
                        41,41,  53,53,  42,42,
                        53,53,  43,43,  42,42,
                        53,53,  54,54,  43,43,

                        44,44,  55,55,  56,56,
                        44,44,  56,56,  45,45,
                        56,56,  46,46,  45,45,
                        56,56,  57,57,  46,46,
                        46,46,  57,57,  58,58,
                        46,46,  58,58,  47,47,
                        58,58,  48,48,  47,47,
                        58,58,  59,59,  48,48,
                        48,48,  59,59,  60,60,
                        48,48,  60,60,  49,49,
                        60,60,  50,50,  49,49,
                        60,60,  61,61,  50,50,
                        50,50,  61,61,  62,62,
                        50,50,  62,62,  51,51,
                        62,62,  52,52,  51,51,
                        62,62,  63,63,  52,52,
                        52,52,  63,63,  64,64,
                        52,52,  64,64,  53,53,
                        64,64,  54,54,  53,53,
                        64,64,  65,65,  54,54,

                        55,55,  66,66,  67,67,
                        55,55,  67,67,  56,56,
                        67,67,  57,57,  56,56,
                        67,67,  68,68,  57,57,
                        57,57,  68,68,  69,69,
                        57,57,  69,69,  58,58,
                        69,69,  59,59,  58,58,
                        69,69,  70,70,  59,59,
                        59,59,  70,70,  71,71,
                        59,59,  71,71,  60,60,
                        71,71,  61,61,  60,60,
                        71,71,  72,72,  61,61,
                        61,61,  72,72,  73,73,
                        61,61,  73,73,  62,62,
                        73,73,  63,63,  62,62,
                        73,73,  74,74,  63,63,
                        63,63,  74,74,  75,75,
                        63,63,  75,75,  64,64,
                        75,75,  65,65,  64,64,
                        75,75,  76,76,  65,65,

                        66,66,  77,77,  78,78,
                        66,66,  78,78,  67,67,
                        78,78,  68,68,  67,67,
                        78,78,  79,79,  68,68,
                        68,68,  79,79,  80,80,
                        68,68,  80,80,  69,69,
                        80,80,  70,70,  69,69,
                        80,80,  81,81,  70,70,
                        70,70,  81,81,  82,82,
                        70,70,  82,82,  71,71,
                        82,82,  72,72,  71,71,
                        82,82,  83,83,  72,72,
                        72,72,  83,83,  84,84,
                        72,72,  84,84,  73,73,
                        84,84,  74,74,  73,73,
                        84,84,  85,85,  74,74,
                        74,74,  85,85,  86,86,
                        74,74,  86,86,  75,75,
                        86,86,  76,76,  75,75,
                        86,86,  87,87,  76,76,

                        77,77,  88,88,  89,89,
                        77,77,  89,89,  78,78,
                        89,89,  79,79,  78,78,
                        89,89,  90,90,  79,79,
                        79,79,  90,90,  91,91,
                        79,79,  91,91,  80,80,
                        91,91,  81,81,  80,80,
                        91,91,  92,92,  81,81,
                        81,81,  92,92,  93,93,
                        81,81,  93,93,  82,82,
                        93,93,  83,83,  82,82,
                        93,93,  94,94,  83,83,
                        83,83,  94,94,  95,95,
                        83,83,  95,95,  84,84,
                        95,95,  85,85,  84,84,
                        95,95,  96,96,  85,85,
                        85,85,  96,96,  97,97,
                        85,85,  97,97,  86,86,
                        97,97,  87,87,  86,86,
                        97,97,  98,98,  87,87,

                        88,88,  99,99,  100,100,
                        88,88,  100,100,  89,89,
                        100,100,  90,90,  89,89,
                        100,100,  101,101,  90,90,
                        90,90,  101,101,  102,102,
                        90,90,  102,102,  91,91,
                        102,102,  92,92,  91,91,
                        102,102,  103,103,  92,92,
                        92,92,  103,103,  104,104,
                        92,92,  104,104,  93,93,
                        104,104,  94,94,  93,93,
                        104,104,  105,105,  94,94,
                        94,94,  105,105,  106,106,
                        94,94,  106,106,  95,95,
                        106,106,  96,96,  95,95,
                        106,106,  107,107,  96,96,
                        96,96,  107,107,  108,108,
                        96,96,  108,108,  97,97,
                        108,108,  98,98,  97,97,
                        108,108,  109,109,  98,98,

                        99,99,  110,110,  111,111,
                        99,99,  111,111,  100,100,
                        111,111,  101,101,  100,100,
                        111,111,  112,112,  101,101,
                        101,101,  112,112,  113,113,
                        101,101,  113,113,  102,102,
                        113,113,  103,103,  102,102,
                        113,113,  114,114,  103,103,
                        103,103,  114,114,  115,115,
                        103,103,  115,115,  104,104,
                        115,115,  105,105,  104,104,
                        115,115,  116,116,  105,105,
                        105,105,  116,116,  117,117,
                        105,105,  117,117,  106,106,
                        117,117,  107,107,  106,106,
                        117,117,  118,118,  107,107,
                        107,107,  118,118,  119,119,
                        107,107,  119,119,  108,108,
                        119,119,  109,109,  108,108,
                        119,119,  120,120,  109,109
        );
        MeshView meshView = new MeshView(triangleMesh);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        material.diffuseMapProperty().bind(Bindings.when(diffuseMap).then(imageProperty).otherwise((Image) null));

        meshView.setMaterial(material);

        contentXform.getChildren().addAll(meshView);
        contentGroup.getChildren().addAll(contentXform);
        world.getChildren().addAll(contentGroup);

//        Xform contentXform = new Xform();
//        TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
//        triangleMesh.getPoints().addAll(
//                -100F,   100F,    0F, //p0
//                -100F,  -100F,    0F, //p1
//                100F,  -100F,    0F, //p2
//                100F,   100F,    0F  //p3
//        );
//        triangleMesh.getTexCoords().addAll(0,0, 1,0, 0,1, 1,1);
//        triangleMesh.getFaces().addAll(0,0,   1,2,    2,3,  0,0,   2,3,    3,1);
//        MeshView meshView = new MeshView(triangleMesh);
//
//        PhongMaterial material = new PhongMaterial();
//        material.setDiffuseColor(Color.WHITE);
//        material.diffuseMapProperty().bind(Bindings.when(diffuseMap).then(imageProperty).otherwise((Image) null));
//
//        meshView.setMaterial(material);
//
//        contentXform.getChildren().add(meshView);
//        contentGroup.getChildren().add(contentXform);
//        world.getChildren().addAll(contentGroup);
    }

    private void handleKeyboard(SubScene scene /*, final Node root */) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Z:
                    cameraXform2.t.setX(0.0);
                    cameraXform2.t.setY(0.0);
                    camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                    cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                    cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                    break;
            }
        });
    }

    private void handleMouse(SubScene scene /*, final Node root */) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;

            if (me.isControlDown()) {
                modifier = CONTROL_MULTIPLIER;
            }
            if (me.isShiftDown()) {
                modifier = SHIFT_MULTIPLIER;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                camera.setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
            }
        });
        scene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            double currentDistance = camera.getTranslateZ();
            double newCameraDistance = currentDistance + delta;
            camera.setTranslateZ(newCameraDistance);
        });
    }
}
