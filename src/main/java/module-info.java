module com.example.projectsem2 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
            
    opens com.example.projectsem2 to javafx.fxml;
    exports com.example.projectsem2;
}