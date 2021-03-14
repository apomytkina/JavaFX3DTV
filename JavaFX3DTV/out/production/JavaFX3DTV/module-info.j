module JavaFX3DTV {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing;
    requires bridj;
    requires slf4j.api;
    requires slf4j.nop;
    requires webcam.capture;

    exports com.grinkrug.javafx;
}

//module JavaFX3DTV {
//        requires java.desktop;
//        requires javafx.base;
//        requires javafx.controls;
//        requires transitive javafx.graphics;
//        requires javafx.swing;
//        requires webcam.capture;
//        exports com.grinkrug.javafx;
//}

