package org.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/layout/applicationview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("အကောင့်ဝင်ရန်။");
        stage.setScene(scene);
        stage.show();

    }


}
