package br.com.fiap.models.dto.Request;

public class CandidateSurveyDTO {

    private Question[] questions;

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public String formatQuestionsToString() {
        if (questions == null || questions.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Question q : questions) {
            if (q.getTheAsk() != null && q.getAnswer() != null) {
                sb.append(q.getTheAsk())
                        .append(" ")
                        .append(q.getAnswer())
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Classe interna (ou coloque em arquivo separado)
    public static class Question {
        private String theAsk;
        private String answer;

        public String getTheAsk() {
            return theAsk;
        }

        public void setTheAsk(String theAsk) {
            this.theAsk = theAsk;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
