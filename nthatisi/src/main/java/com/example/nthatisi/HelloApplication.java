package com.example.nthatisi;



import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    private static final int TOTAL_QUESTIONS = 6; // Total number of questions

    private int score = 0;
    private int currentQuestionIndex = 0;
    private Timeline timer;

    // Define your list of questions
    private Question[] questions = {
            new Question("What is the capital city of Lesotho?", new String[]{"Maseru", "Leribe", "Mafeteng", "Quthing"}, 0, "/maseru.jpg"),
            new Question("Lesotho is entirely surrounded by which country?", new String[]{"South Africa", "Namibia", "Zimbabwe", "Botswana"}, 0, "/lesotho.jpg"),
            new Question("Who was the first Prime Minister of Lesotho?", new String[]{"Ntsu Mokhehle", "Leabua Jonathan", "Pakalitha Mosisili", "Tom Thabane"}, 1, "/leabuajonathan.jpg"),
            new Question("What is the traditional Basotho hat called?", new String[]{"Mokorotlo", "Boshielo", "Kobo", "Litema"}, 0, "/mokorotlo.jpg"),
            new Question("Which national park in Lesotho is known for its rock art?", new String[]{"Ts'ehlanyane National Park", "Sehlabathebe National Park", "Bokong Nature Reserve", "Sehlabathebe National Park"}, 3, "/sehlabathebe.jpg"),
            new Question("Who is the current Prime Minister of Lesotho?", new String[]{"Honourable Mokhothu", "Mofumobe Machesetsa", "Monyane Molelekei", "Sam Matekane"}, 4, "/matekane.jpg"),
    };

    // Define UI elements
    private Button[] optionButtons = new Button[4];
    private ImageView imageView;
    private VBox questionBox;
    private Text timerText;
    private Text feedbackText; // Text to show correct/incorrect feedback

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lesotho Trivia");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.DARKSALMON, CornerRadii.EMPTY, Insets.EMPTY))); // Set background color
        root.setAlignment(Pos.CENTER); // Center align everything

        // Initialize UI components
        timerText = new Text("Countdown: 30");
        timerText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        timerText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(timerText);

        // Initialize feedback text
        feedbackText = new Text();
        feedbackText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        feedbackText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(feedbackText);

        questionBox = new VBox(10);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPrefWidth(600);
        questionBox.setStyle("-fx-background-color: #FFAF33; -fx-padding: 10px;");
        root.getChildren().add(questionBox);

        for (int i = 0; i < 4; i++) {
            final int optionIndex = i; // Declare effectively final variable
            optionButtons[i] = new Button();
            optionButtons[i].setPrefWidth(200);
            optionButtons[i].setStyle("-fx-background-color: #3C33FF; -fx-text-fill: white; -fx-font-size: 14px;"); // Set button style
            optionButtons[i].setOnAction(e -> handleAnswer(optionIndex)); // Use the final variable
            root.getChildren().add(optionButtons[i]);
        }

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);

        // Add restart button
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());
        root.getChildren().add(restartButton);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Display the first question
        displayQuestion(questions[currentQuestionIndex]);
    }

    private void displayQuestion(Question question) {
        // Display question text
        questionBox.getChildren().clear(); // Clear previous question
        Text questionText = new Text(question.getQuestionText());
        questionText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        questionText.setTextAlignment(TextAlignment.CENTER);
        questionBox.getChildren().add(questionText);

        optionButtons[0].setText(question.getOptions()[0]);
        optionButtons[1].setText(question.getOptions()[1]);
        optionButtons[2].setText(question.getOptions()[2]);
        optionButtons[3].setText(question.getOptions()[3]);

        // Load and display image related to the question
        imageView.setImage(question.getImage());

        // Start the timer
        startTimer();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (!timerText.getText().startsWith("Countdown: ")) {
                timerText.setText("Countdown: 30");
            } else {
                int timeLeft = Integer.parseInt(timerText.getText().substring(11)) - 1;
                if (timeLeft > 0) {
                    timerText.setText("Countdown: " + timeLeft);
                } else {
                    timer.stop();
                    handleAnswer(-1); // Time's up, consider it as no answer (-1)
                }
            }
        }));
        timer.setCycleCount(30); // 30 seconds timer
        timer.play();
    }

    private void handleAnswer(int selectedOptionIndex) {
        timer.stop(); // Stop the timer
        Question currentQuestion = questions[currentQuestionIndex];
        if (selectedOptionIndex == currentQuestion.getCorrectAnswerIndex()) {
            score++;
            // Provide feedback for correct answer
            feedbackText.setFill(Color.GREEN);
            feedbackText.setText("Correct!");
        } else {
            // Provide feedback for incorrect answer
            feedbackText.setFill(Color.RED);
            feedbackText.setText("Incorrect!");
        }

        // Move to the next question or end the game
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            displayQuestion(questions[currentQuestionIndex]);
        } else {
            endGame();
        }
    }

    private void endGame() {
        // Display final score and summary
        Text finalScore = new Text("Game Over! Your score: " + score + "/" + TOTAL_QUESTIONS);
        finalScore.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        finalScore.setTextAlignment(TextAlignment.CENTER);
        questionBox.getChildren().clear();
        questionBox.getChildren().add(finalScore);
    }

    private void restartGame() {
        // Reset the game
        score = 0;
        currentQuestionIndex = 0;
        feedbackText.setText(""); // Clear feedback text
        displayQuestion(questions[currentQuestionIndex]); // Restart the game by displaying the first question
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private Image image;

    public Question(String questionText, String[] options, int correctAnswerIndex, String imagePath) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.image = new Image(Question.class.getResourceAsStream(imagePath));
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public Image getImage() {
        return image;
    }
}
