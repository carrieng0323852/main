package com.algosenpai.app.logic.command;

import com.algosenpai.app.logic.models.QuestionModel;
import com.algosenpai.app.stats.UserStats;
import com.algosenpai.app.storage.Storage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizTestCommand extends QuizCommand {

    ArrayList<QuestionModel> quizList;

    AtomicInteger questionNumber;

    AtomicBoolean isQuizMode;

    AtomicBoolean isNewQuiz;

    int chapterNumber;

    private UserStats userStats;

    /**
     * Create new command.
     * @param inputs input from user.
     */
    private QuizTestCommand(ArrayList<String> inputs) {
        super(inputs);
    }

    /**
     * Initializes quiz command to start quiz.
     * @param inputs user inputs.
     * @param quizList quiz.
     * @param questionNumber question number.
     * @param isQuizMode is quiz mode.
     * @param isNewQuiz is quiz initialize.
     */
    public QuizTestCommand(ArrayList<String> inputs, ArrayList<QuestionModel> quizList,
                           AtomicInteger questionNumber, AtomicBoolean isQuizMode, AtomicBoolean isNewQuiz,
                           int chapterNumber,UserStats userStats) {
        this(inputs);
        this.quizList = quizList;
        this.isQuizMode = isQuizMode;
        this.questionNumber = questionNumber;
        this.isNewQuiz = isNewQuiz;
        this.chapterNumber = chapterNumber;
        this.userStats = userStats;
    }


    @Override
    public String execute() {
        if (quizList.size() == 0) {
            return "You need to select a chapter first: select <chapter name>";
        } else {
            isNewQuiz.set(false);
            isQuizMode.set(true);
            questionNumber.incrementAndGet();

            if (questionNumber.get() < 10) {
                //add the userinput that has been parsed as his answer.
                if (inputs.size() > 1) {
                    //to end the quiz in the quizmode
                    if (inputs.get(1).equals("end")) {
                        isQuizMode.set(false);
                        return calculateScore();
                    }
                    QuestionModel currQuestionDisplayed = quizList.get(questionNumber.get() - 1);
                    String userAnswer = extractUserAnswerFromInput();
                    System.out.println(userAnswer);
                    currQuestionDisplayed.setUserAnswer(userAnswer);
                }
                return quizList.get(questionNumber.get()).getQuestion()
                        + "\n Your answer: "
                        + quizList.get(questionNumber.get()).getUserAnswer();
            } else {
                reset();
                return calculateScore();
            }
        }
    }

    /**
     * Parses the user input into a string format.
     * @return The string containing the user's answer.
     */
    private String extractUserAnswerFromInput() {
        StringBuilder answer = new StringBuilder();
        for (int i = 1; i < inputs.size() - 1; i++) {
            answer.append(inputs.get(i)).append(" ");
        }
        answer.append(inputs.get(inputs.size() - 1));
        return answer.toString();
    }


    private void reset() {
        questionNumber.set(0);
        isQuizMode.set(false);
        isNewQuiz.set(true);
    }

    private String calculateScore() {
        int userQuizScore = 0;
        for (QuestionModel question : quizList) {
            if (question.checkAnswer()) {
                userQuizScore++;
            }
        }

        // Updating all the user stats one shot in here
        userStats.updateChapter(chapterNumber,10,userQuizScore);
        userStats.setUserExp(userStats.getUserExp() + userQuizScore);
        // Update level, each level is double of previous, so we use log base 2.
        if (userStats.getUserExp() == 0) {
            userStats.setUserLevel(0);
        } else {
            userStats.setUserLevel((int)(Math.log(userStats.getUserExp() / 10.0) / Math.log(2) + 1));
        }
        userStats.saveUserStats("UserData.txt");
        // End of updating

        return "You got " + userQuizScore + "/10 questions correct!\n"
                + "You have gained " + userQuizScore + " EXP points!";
    }
}
