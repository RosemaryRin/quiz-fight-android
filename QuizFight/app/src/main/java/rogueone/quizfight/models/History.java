package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the user's history saved in Google Saved Games. It implements
 * <tt>Serializable</tt> to be easily cast in a <tt>byte[]</tt>. It stores a <tt>List</tt> of duels.
 *
 * @author Matteo Di Pirro
 * @see Serializable
 */

public class History implements Serializable {
    private static final long serialVersionUID = 6770709444772834353L;

    private List<Duel> duels = new ArrayList<>();

    public List<Duel> getCompletedDuels() {
        List<Duel> inProgress = new ArrayList<>();
        for(Duel duel : duels) {
            if (duel.isCompleted()) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public List<Duel> getCompletedDuels(int n) {
        List<Duel> completedDuels = getCompletedDuels();
        int size = completedDuels.size();
        return completedDuels.subList(size - n, size);
    }

    public List<Duel> getInProgressDuels() {
        List<Duel> inProgress = new ArrayList<>();
        for(Duel duel : duels) {
            if (!duel.isCompleted()) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public List<Duel> getInProgressDuels(int n) {
        List<Duel> inProgress = new ArrayList<>();
        Iterator<Duel> iterator = duels.iterator();
        while (iterator.hasNext() && inProgress.size() < n) {
            Duel duel = iterator.next();
            if (!duel.isCompleted()) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public boolean isEmpty() {
        return duels.isEmpty();
    }

    public void addDuel(@NonNull Duel duel) {
        duels.add(duel);
    }

    public Duel getDuelByID(@NonNull String id) {
        int index = getDuelIndex(id);
        return (index != -1) ? duels.get(index) : null;
    }

    /**
     * Duel setter. The duel ID is automatically retrieved using `duel`.
     * @param duel The duel to be saved.
     */
    public void setDuelByID(@NonNull Duel duel) {
        int index = getDuelIndex(duel.getDuelID());
        if (index != -1) {
            duels.set(index, duel);
        } else {
            duels.add(duel);
        }
    }

    private int getDuelIndex(@NonNull String id) {
        boolean found = false;
        int index = 0;
        while (index < duels.size() && !found) {
            Duel duel = duels.get(index);
            if (duel != null && duel.getDuelID().equals(id)) {
                found = true;
            } else {
                index++;
            }
        }
        return (index < duels.size()) ? index : -1;
    }

    public int howManyWonDuels() {
        int k = 0;
        Iterator<Duel> iterator = getCompletedDuels().iterator();
        while (iterator.hasNext()) {
            Duel duel = iterator.next();
            Score score = duel.getScore();
            if (score.getPlayerScore() > score.getOpponentScore()) {
                k++;
            }
        }
        return k;
    }

    public int howManyWonRounds() {
        int k = 0;
        Iterator<Duel> iterator = getCompletedDuels().iterator();
        while (iterator.hasNext()) {
            Duel duel = iterator.next();
            List<Quiz> rounds = duel.getQuizzes();
            Iterator<Quiz> quizIterator = rounds.iterator();
            while (quizIterator.hasNext()) {
                Quiz quiz = quizIterator.next();
                Score score = quiz.getScore();
                if (score.getPlayerScore() > score.getOpponentScore()) {
                    k++;
                }
            }
        }
        return k;
    }

    public int howManyCorrectAnswers() {
        int k = 0;
        Iterator<Duel> iterator = getCompletedDuels().iterator();
        while (iterator.hasNext()) {
            Duel duel = iterator.next();
            List<Quiz> rounds = duel.getQuizzes();
            Iterator<Quiz> quizIterator = rounds.iterator();
            while (quizIterator.hasNext()) {
                Quiz quiz = quizIterator.next();
                List<Question> questions = quiz.getQuestions();
                Iterator<Question> questionsIterator = questions.iterator();
                while (questionsIterator.hasNext()) {
                    Question question = questionsIterator.next();
                    if (question.getPlayerAnswer()) {
                        k++;
                    }
                }
            }
        }
        return k;
    }

    public int getTotalPlayerScore() {
        int sumOfScores = 0;
        Iterator<Duel> iterator = duels.iterator();
        while (iterator.hasNext()) {
            Duel duel = iterator.next();
            Iterator<Quiz> quizIterator = duel.getQuizzes().iterator();
            while (quizIterator.hasNext()) {
                Quiz quiz = quizIterator.next();
                Score score = quiz.getScore();
                sumOfScores += score.getPlayerScore();
            }
        }
        return sumOfScores;
    }
}
