cd ..\out
set PATH_TO_FX=C:\distrib\JavaFX\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2
set PATH_TO_WEBCAM=..\webcamlib
java -Djava.library.path=%PATH_TO_FX%\bin --module-path %PATH_TO_FX%\lib;%PATH_TO_WEBCAM%;. --module JavaFX3DTV/com.grinkrug.javafx.JavaFX3DWebCam
