package riddlewave;

public class Question {
    private final String text;
    private final String[] options;
    private final String correctAnswer;

    public Question(String text, String[] options, String correctAnswer) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
