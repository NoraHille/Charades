import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App extends Application {

    static List<String> words = new ArrayList<>();
    static List<String> tutoringEasy = new ArrayList<>();
    static List<String> normalWords = new ArrayList<>();
    static double maxWordLength = 150;
    static int scalingThreshold = 5;
    String listToUse = "";
    List<String> defaultList = tutoringEasy; // this list will be loaded when the application is started and list to use is not set
    String pathBeginning = "C:\\Users\\norah\\IdeaProjects\\Charades\\";
    String pathEnd = ".txt";

    TextField listTextfield;
    String listTextfieldText = "What words do you want?";
    Label textFieldInformationLabel;
    Label word;
    String shuffelMode = "ordered";
    // ordered means that the list will go all the way through before a word can repeat.
    // random means in each itereation the word is truely random, meaniing that the same word might after next was clicked.


    @Override
    public void start(Stage stage) throws Exception {
        listTextfield = new TextField(listTextfieldText);
        textFieldInformationLabel = new Label();

        setWordList();

        word = new Label("");

       // word.setMaxSize(100, 100);

        word.setAlignment(Pos.CENTER);


       word.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double scalingValue =  maxWordLength / newValue.length();
                if(scalingValue> scalingThreshold){
                    scalingValue = scalingThreshold;
                }
               word.setScaleX(scalingValue);
               word.setScaleY(scalingValue);

            }
        });






        Button button = new Button("Start");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        word.setText(newWord(shuffelMode));
                        button.setText("Next");
                    }
                });

            }
        });



        listTextfield.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listToUse = listTextfield.getText();
                setWordList();
                button.requestFocus();
            }
        });
        listTextfield.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                listTextfield.setText("");
            }
        });
        listTextfield.setMaxWidth(350);
        listTextfield.setPrefWidth(200);
        listTextfield.setMinWidth(200);

        HBox hbox = new HBox(listTextfield, textFieldInformationLabel);
        hbox.setSpacing(40);
        hbox.setAlignment(Pos.CENTER);



        VBox vbox = new VBox(word, button, hbox);
        vbox.setSpacing(80);
        vbox.setAlignment(Pos.CENTER);

        vbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER)){
                    word.setText(newWord(shuffelMode));
                    button.setText("Next");
                }
            }
        });

        Scene scene = new Scene(vbox, Screen.getPrimary().getVisualBounds().getWidth()*2/3, Screen.getPrimary().getVisualBounds().getHeight()*2/3);

        stage.setTitle("Charades");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String... args) {
        launch(args);
    }

    public static String newWord(String shuffelMode){
        if(words.size()>0){
            if(shuffelMode.equals("random") )
            return words.get((int)(Math.random()*words.size()));
            if(shuffelMode.equals("ordered")){
                String result = words.get(0);
                words.add(result);
                words.remove(0);
                return result;
            }

        }
        return "";


    }

    public static void fillWords(List<String> chosenList){
        words.clear();
        // add in random order
        int chosenListSize = chosenList.size();
        for (int i = 0; i <chosenListSize ; i++) {
            int randomIndex = (int) (chosenList.size()*Math.random());
            words.add(chosenList.get(randomIndex));
            chosenList.remove(randomIndex);
        }


    }

    public void fillWordLists(){

        readFileIntoList("C:\\Users\\norah\\IdeaProjects\\Charades\\TutoringEasy.txt", tutoringEasy);

        readFileIntoList("C:\\Users\\norah\\IdeaProjects\\Charades\\NormalWords.txt", normalWords);






    }

    public boolean readFileIntoList(String pathname, List<String> list){
        try {
            Scanner s = new Scanner(new File(pathname));
            while (s.hasNextLine()){
                list.add(s.nextLine());
            }
            s.close();

        }
        catch(FileNotFoundException e){
            System.out.println(pathname);

            return false;
        }

        listTextfield.setText(listTextfieldText);
        return true;

    }

    public void setWordList(){

        String pathname = pathBeginning + listToUse + pathEnd;
        List<String> list = new ArrayList<>();
        if(readFileIntoList(pathname, list)){
            fillWords(list);
            textFieldInformationLabel.setText("Current list: "+ listToUse);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(!word.getText().equals(""))
                    word.setText(newWord(shuffelMode));

                }
            });

        }
        else{
            listTextfield.setText(listTextfieldText);
        }
        if(words.size()<= 0){
            fillWordLists();
            fillWords(defaultList);
            textFieldInformationLabel.setText("Default list was loaded." );
        }





    }

}

//TODO
// figure out a nive way to make the
