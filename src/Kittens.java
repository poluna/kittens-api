import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javafx.scene.layout.GridPane.setConstraints;

public class Kittens extends Application {

    HttpStuff httpStuff = new HttpStuff();
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kittens");

        GridPane gridpane = new GridPane();

        Label labelLogin = new Label("Login: ");
        TextField loginField = new TextField();
        loginField.setPromptText("Your login");

        Label labelPassword = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your password");


        Button btnLogin = new Button("Log in");
        btnLogin.setStyle("-fx-font: 22 arial; -fx-base: #ffc0cb;");
        btnLogin.setOnAction(e -> {
            String email = loginField.getText();
            String password = passwordField.getText();
            //loginField.clear();
            passwordField.clear();
            try {
                String token = httpStuff.getToken(email, password);
                List<Map<String, String>> kittenNames = httpStuff.getKittens(token, "");
                showKitten(primaryStage, token, 0, kittenNames);
            } catch (Exception e1) {
                Label label = new Label("Wrong password!");
                label.setAlignment(Pos.CENTER);
                Button btnBack=new Button("Try again");
                btnBack.setStyle("-fx-font: 22 arial; -fx-base: #ffc0cb;");
                btnBack.setOnAction(a-> primaryStage.setScene(scene));

                VBox vBox=new VBox(label,btnBack);
                vBox.setAlignment(Pos.CENTER);
                vBox.setPadding(new Insets(20, 20, 20, 20));
                Scene sceneFail = new Scene(vBox, 600, 500);
                sceneFail.getStylesheets().add(Kittens.class.getResource("mainScene.css").toExternalForm());
                primaryStage.setScene(sceneFail);
                primaryStage.show();
            }
        });

        setConstraints(labelLogin, 1, 0);
        setConstraints(loginField, 2, 0);
        setConstraints(labelPassword, 1, 1);
        setConstraints(passwordField, 2, 1);

        gridpane.getChildren().addAll(labelLogin, loginField, labelPassword, passwordField);
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setHgap(7);
        gridpane.setVgap(7);
        gridpane.setPadding(new Insets(10, 10, 10, 10));

        VBox layout = new VBox(gridpane, btnLogin);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        scene = new Scene(layout, 600, 500);

        scene.getStylesheets().add(Kittens.class.getResource("mainScene.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showKitten(Stage stage, String token, int n, List<Map<String, String>> kittenNames) {

        Label nameLabel = new Label(kittenNames.get(n).get("name"));
        nameLabel.setFont(Font.font("Cambria", FontWeight.BOLD, 32));
        nameLabel.setTextFill(Color.web("#FF748C"));

        Label voteLabel = new Label("Votes: " + kittenNames.get(n).get("votes"));
        ImageView img = new ImageView(kittenNames.get(n).get("url"));
        img.setPreserveRatio(true);
        img.setFitHeight(300);

        int finalN = ++n;

        Button btnNext = new Button("Next kitten");
        btnNext.setStyle("-fx-font: 18 arial; -fx-base: #ffc0cb;");
        btnNext.setOnAction(e -> {
            if (finalN < 25) {
                showKitten(stage, token, finalN, kittenNames);
                //System.out.println(finalN);
            } else {
                try {
                    List<Map<String, String>> kittenNames2 = httpStuff.getKittens(token, "&page=2");
                    showKitten(stage, token, 0, kittenNames2);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Button btnLogOut = new Button("Log Out");
        btnLogOut.setOnAction(e -> {
            stage.setScene(scene);
        });

        VBox vBox = new VBox(nameLabel, voteLabel, img, btnNext, btnLogOut);
        vBox.setSpacing(6);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30, 30, 30, 30));

        Scene scene = new Scene(vBox, 600, 500);
        scene.getStylesheets().add(Kittens.class.getResource("kittens.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
}
