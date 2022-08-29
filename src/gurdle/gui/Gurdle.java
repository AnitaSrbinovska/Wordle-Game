package gurdle.gui;

import gurdle.CharChoice;
import gurdle.Model;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * The graphical user interface to the Wordle game model in
 * {@link Model}.
 *
 * @author Anita Srbinovska (as2950@rit.edu)
 */
public class Gurdle extends Application implements Observer<Model, String> {
    /**
     * the model (the whole data/logic and rules)
     */
    private Model model;
    /**
     * the number of rows
     */
    private final static int ROWS = 6;
    /**
     * the number of columns
     */
    private final static int COLS = 5;
    /**
     * the model being initialized
     */
    private boolean initialized;
    /**
     * the 2-D array of buttons
     */
    private final Button[][] buttons = new Button[ROWS][COLS];
    /**
     * an ArrayList of buttons
     */
    private final ArrayList<Button> lst = new ArrayList<>();
    /**
     * the vertical gap between buttons
     */
    private final static int VGAP = 3;
    /**
     * the horizontal gap between buttons
     */
    private final static int HGAP = 11;
    /**
     * the label
     */
    private final Label label = new Label();

    /**
     * This method creates the Wordle model and register this object as an
     * observer of it.
     */
    @Override
    public void init() {
        this.initialized = false;
        this.model = new Model();
        this.model.addObserver(this);
        List<String> paramStrings = super.getParameters().getRaw();
        if (paramStrings.size() == 1) {
            final String firstWord = paramStrings.get(0);
            this.model.newGame(firstWord);
        } else {
            this.model.newGame();
        }
    }

    /**
     * The start() method has the scene and the setting of the whole stage.
     *
     * @param mainStage the stage
     */
    @Override
    public void start(Stage mainStage) {
        BorderPane pane = new BorderPane();
        label.setText("Number of guesses: " + model.numAttempts());
        pane.setTop(label);
        BorderPane bottom = bottom();
        pane.setBottom(bottom);
        GridPane center = center();
        pane.setCenter(center);
        Scene scene = new Scene(pane);
        mainStage.setResizable(false);
        this.initialized = true;
        mainStage.setTitle("GURDLE");
        mainStage.setScene(scene);
        mainStage.sizeToScene();
        mainStage.maxHeightProperty();
        mainStage.maximizedProperty();
        mainStage.show();
    }

    /**
     * The method for creating the BorderPane.
     *
     * @return the border pane
     */
    private BorderPane bottom() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(left());
        borderPane.setRight(right());
        return borderPane;
    }

    /**
     * This method creates the keyboard at the left of the window.
     *
     * @return the keyboard
     */
    private GridPane left() {
        GridPane result = new GridPane();
        char ch = 'A';
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 10; ++c) {
                String label = String.valueOf(ch);
                Button button = new Button();
                button.setText(label);
                lst.add(button);
                result.add(button, c, r);
                button.setBackground(new Background(new BackgroundFill(
                        Color.WHITE, null, null)));
                button.setStyle("""
                                    -fx-padding: 2;
                                    -fx-border-style: solid inside;
                                    -fx-border-width: 1;
                                    -fx-border-insets: 5;
                                    -fx-border-radius: 2;
                                    -fx-border-color: black;
                        """);
                char finalCh = ch;
                button.setOnAction(event -> model.enterNewGuessChar(finalCh));
                int num = ch;
                ++num;
                ch = (char) num;
                if (ch == '[') {
                    break;
                }
            }
        }
        result.setStyle("-fx-font: 18px Menlo");
        result.setStyle("""
                            -fx-padding: 2;
                            -fx-border-style: solid inside;
                            -fx-border-width: 1;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                """);
        result.setVgap(VGAP);
        result.setHgap(HGAP);
        result.setAlignment(Pos.BOTTOM_LEFT);
        return result;
    }

    /**
     * This method sets Enter, New Game and Cheat buttons.
     *
     * @return the VBox of buttons
     */
    private VBox right() {
        VBox vBox = new VBox();
        Button button1 = new Button("ENTER");
        button1.setOnAction(actionEvent -> model.confirmGuess());
        Button button2 = new Button("NEW GAME");
        button2.setOnAction(actionEvent -> model.newGame());
        Button button3 = new Button("CHEAT");
        button3.setOnAction(actionEvent -> {
            label.setText("Number of guesses: " + model.numAttempts() +
                    "\tYou cheated! The secret word is: " + model.secret());
            model.secret();
        });
        vBox.getChildren().addAll(button1, button2, button3);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        return vBox;
    }

    /**
     * This method calls the whole center of GridPane (buttons).
     *
     * @return the GridPane in the center
     */
    private GridPane center() {
        return this.makeCenter();
    }

    /**
     * This method makes the whole center of GridPane (buttons).
     *
     * @return the grid
     */
    private GridPane makeCenter() {
        GridPane makeGrid = new GridPane();
        for (int row = 0; row < Gurdle.ROWS; ++row) {
            for (int col = 0; col < Gurdle.COLS; ++col) {
                Button button1 = new Button();
                makeGrid.add(button1, col, row);
                button1.setBackground(new Background(new BackgroundFill(
                        Color.WHITE, null, null)));
                button1.setStyle("""
                                    -fx-padding: 2;
                                    -fx-border-style: solid inside;
                                    -fx-border-width: 1;
                                    -fx-border-insets: 5;
                                    -fx-border-radius: 2;
                                    -fx-border-color: black;
                        """);
                buttons[row][col] = button1;
            }
        }
        makeGrid.setStyle("-fx-font: 18px Menlo");
        makeGrid.setStyle("""
                            -fx-padding: 2;
                            -fx-border-style: solid inside;
                            -fx-border-width: 1;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                """);
        makeGrid.setAlignment(Pos.CENTER);
        makeGrid.setGridLinesVisible(true);
        return makeGrid;
    }

    /**
     * The update() method is called in the model, and it displays certain
     * messages depending on what word the user enters, and it changes colors on
     * the grid and the keyboard depending on the position of the letters.
     *
     * @param model   the whole model with the data
     * @param message the messages the user gets when a word is entered in the
     *                grid
     */
    @Override
    public void update(Model model, String message) {
        String messages;
        if (message.equals("Make a new guess!")) {
            messages = message;
        } else if (message.equals("Illegal word.")) {
            messages = message;
        } else if (message.equals("You won!")) {
            messages = message;
        } else if (message.equals("You lost 😥.")) {
            messages = message;
        }
        label.setText("Number of guesses: " + model.numAttempts() + "\t" +
                message);

        if (initialized) {
            for (int i = 0; i < ROWS; ++i) {
                for (int j = 0; j < COLS; ++j) {
                    CharChoice charChoice = model.get(i, j);
                    Button button = buttons[i][j];
                    button.setText(String.valueOf(charChoice.getChar()));

                    for (Button val : lst) {
                        if (model.numAttempts() == 0) {
                            val.setBackground(new Background(new BackgroundFill(
                                    Color.WHITE, null, null)));
                        }
                    }

                    for (Button value : lst) {
                        if (value.getText().equals(button.getText())) {
                            if (charChoice.getStatus() ==
                                    CharChoice.Status.RIGHT_POS) {
                                value.setBackground(new Background(new
                                        BackgroundFill(Color.LIGHTGREEN,
                                        null, null)));
                            } else if (charChoice.getStatus() ==
                                    CharChoice.Status.WRONG_POS) {
                                value.setBackground(new Background(new
                                        BackgroundFill(Color.BURLYWOOD,
                                        null, null)));
                            } else if (model.usedLetter(charChoice.getChar())) {
                                value.setBackground(new Background(new
                                        BackgroundFill(Color.LIGHTGREY,
                                        null, null)));
                            }
                        }
                    }

                    if (charChoice.getStatus() == CharChoice.Status.RIGHT_POS) {
                        buttons[i][j].setBackground(new Background(new
                                BackgroundFill(Color.LIGHTGREEN,
                                null, null)));
                    } else if (charChoice.getStatus() ==
                            CharChoice.Status.WRONG_POS) {
                        buttons[i][j].setBackground(new Background(new
                                BackgroundFill(Color.BURLYWOOD,
                                null, null)));
                    }
                    if (charChoice.getStatus() == CharChoice.Status.EMPTY) {
                        buttons[i][j].setBackground(new Background(new
                                BackgroundFill(Color.WHITE,
                                null, null)));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: java Gurdle [1st-secret-word]");
        }
        Application.launch(args);
    }
}
