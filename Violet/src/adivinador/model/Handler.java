/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.model;

import adivinador.node.AnswerNode;
import adivinador.node.Possibility;
import adivinador.node.QuestionNode;
import adivinador.view.AddScoreWindow;
import adivinador.view.LearnWindow;
import adivinador.view.MainWindow;

/**
 *
 * @author Andres
 */
public class Handler {
	
    private static float MAX_GUESS_DELTA = 0.4f;
	private static int MIN_QUESTIONS = 8;
	private static float MAX_RIGHT_DELTA = 0.2f;
	
	private int currentQuestionID;
	private Possibility userGuess;
	private int bestGuess;
	
	private long startTime;
	private long endTime;
	
	private int wrongGuessQuestion;
	private int currentQuestionNumber;
	private final MainWindow mainWindow;
	
	public Handler() {
		adivinador.data.NIC.loadAllData();
		mainWindow = new adivinador.view.MainWindow(this);
		this.start();
	}
	
	public void start() {
//		print ("# NEW GAME #");
		userGuess = new Possibility(Possibility.TYPE_USER);
		userGuess.fillAnswers(adivinador.data.NIC.LIST_QUESTIONS.size());
		for (int i = 0; i < adivinador.data.NIC.LIST_ANSWERS.size(); i++) {
			adivinador.data.NIC.LIST_ANSWERS.get(i).setType(Possibility.TYPE_GUESS_POSSIBLE);
		}
		wrongGuessQuestion = 0;
		currentQuestionNumber = 0;
		mainWindow.setGuessing(false);
		endTime = -1;
		startTime = System.nanoTime();
		nextStep();
	}
	
	public long getElapsedTime() {
		long elapsedTime;
		if (endTime == -1) {
			elapsedTime =  System.nanoTime();
		} else {
			elapsedTime =  endTime;
		}
		elapsedTime = elapsedTime - startTime;
		return elapsedTime;
	}
	
	public void setAnswer(float value) {
		userGuess.get(currentQuestionID).setAnswer(value);
	}
	
	public void learn(String newQuestion, float answerValue, String guessAnswer) {
		adivinador.data.NIC.LIST_QUESTIONS.add(new QuestionNode(newQuestion));
		userGuess.add(new AnswerNode(answerValue));
		
		userGuess.setName(guessAnswer);
		for (int i = 0; i < adivinador.data.NIC.LIST_ANSWERS.size(); i++) {
			adivinador.data.NIC.LIST_ANSWERS.get(i).fillAnswers(userGuess.size());
		}
		adivinador.data.NIC.LIST_ANSWERS.add(userGuess);
		adivinador.data.NIC.checkLastCasualty();
		adivinador.data.NIC.saveAll();
	}
	
	public void learn(float answerValue, String guessAnswer) {
		userGuess.setName(guessAnswer);
		for (int i = 0; i < adivinador.data.NIC.LIST_ANSWERS.size(); i++) {
			adivinador.data.NIC.LIST_ANSWERS.get(i).fillAnswers(userGuess.size());
		}
		adivinador.data.NIC.LIST_ANSWERS.add(userGuess);
		adivinador.data.NIC.checkLastCasualty();
		adivinador.data.NIC.saveAll();
	}
	
	private void saveAnswers() {
		adivinador.data.NIC.LIST_ANSWERS.get(bestGuess).revisePossibility(userGuess);
//		adivinador.data.NIC.checkLastCasualty();
		adivinador.data.NIC.saveAll();
	}
	
	private int findFirstAvailableQuestion() {
		int questionID = adivinador.data.NIC.LIST_QUESTIONS.size()-1;
		boolean foundQuestion = false;
		
		while(!foundQuestion && questionID >= 0) {
			if (!userGuess.get(questionID).isAnswered()) {
				foundQuestion = true;
			} else {
				questionID--;
			}
		}
		
		return questionID;
	}
	
	private int findBestQuestion() {
		int bestQuestionID = -1;
		float bestQuestionDelta = 1.1f;
		
		for (int i = 0; i < adivinador.data.NIC.LIST_QUESTIONS.size(); i++) {
			if (!userGuess.get(i).isAnswered()) {
				float delta = getQuestionDelta(i);
				if (delta < bestQuestionDelta) {
					bestQuestionDelta = delta;
					bestQuestionID = i;
				}
			}
		}
		
		return bestQuestionID;
	}
	
	private float getQuestionDelta(int index) {
		int validGuesses = 0;
		int yesAnswers = 0;
		for (int j = 0; j < adivinador.data.NIC.LIST_ANSWERS.size(); j++) {
			if (adivinador.data.NIC.LIST_ANSWERS.get(j).get(index).isAnswered()) {
				validGuesses++;
				if (adivinador.data.NIC.LIST_ANSWERS.get(j).get(index).isPositive()){
					yesAnswers++;
				}
			}
		}
		float percent = ((float)yesAnswers/validGuesses);
		if (percent > 0.5f) {
			percent = 1.0f - percent;
		}
		float delta = Math.abs(0.5f - percent);
		return delta;
	}
	
	public void nextStep() {
		if (canGuess()) {
			currentQuestionNumber++;
			if (currentQuestionNumber > 15 && wrongGuessQuestion == 0) {
				wrongGuessQuestion = 15;
                                setEnd();
			}
//			print ("QUESTION NUMBER: "+currentQuestionNumber);
//			print ("BEST: "+adivinador.data.NIC.LIST_ANSWERS.get(bestGuess).getName());
//			print ("POSSIBILITIES: "+getPossibleAnswersLeft());
			if (isValidGuess()) {
				doGuess();
			} else {
				mainWindow.setGuessing(false);
				nextQuestion();
			}
		} else {
			endGame();
		}
	}
	
	private boolean isValidGuess() {
		return validGuessDelta() && validGuessQuestions();
	}
	
	private boolean validGuessDelta() {
		return (narrowDown() < MAX_RIGHT_DELTA);
	}
	
	private boolean validGuessQuestions() {
		return (getValidQuestions(bestGuess) >= MIN_QUESTIONS);
	}
	
	private void doNextGame() {
		mainWindow.setNewGameCheck(true);
		mainWindow.setDisplayText("Desea jugar de nuevo?");
	}
	
	private void doGuess() {
		setEnd();
		mainWindow.setGuessing(true);
		mainWindow.setDisplayText(getDisplayGuess());
	}
	
	private int getNextQuestion() {
		int nextID = findBestQuestion();
		if (nextID == -1) {
			nextID = findFirstAvailableQuestion();	
		}
		
		return nextID;
	}
	
	private void nextQuestion() {
		currentQuestionID = getNextQuestion();
		
		if (currentQuestionID != -1) {
			mainWindow.setDisplayText(getDisplayQuestion());
		} else {
			if (validGuessDelta()){
				doGuess();
			} else {
				endGame();
			}
		}
	}
	
	private void endGame() {
		setEnd();
		doNextGame();
		new AddScoreWindow(this,getScore(),getElapsedTime());
		new LearnWindow(this);
	}
	
	private String getQuestion() {
		return adivinador.data.NIC.LIST_QUESTIONS.get(currentQuestionID).getQuestion();
	}
	
	private String getDisplayQuestion() {
		return ("Pregunta No. " + currentQuestionNumber + ": \n" + getQuestion());
	}
	
	private String getGuess() {
		return adivinador.data.NIC.LIST_ANSWERS.get(bestGuess).getName();
	}
	
	private String getDisplayGuess() {
		return ("Pensaba en " + getGuess()+ "?");
	}
	
	public void wrongGuess() {
		adivinador.data.NIC.LIST_ANSWERS.get(bestGuess).setType(Possibility.TYPE_GUESS_IMPOSSIBLE);
		wrongGuessQuestion = currentQuestionNumber;
		nextStep();
	}
	
	private int getPossibleAnswersLeft() {
		int possibilities = 0;
        for (int i = 0; i < adivinador.data.NIC.LIST_ANSWERS.size(); i++) {
			if (adivinador.data.NIC.LIST_ANSWERS.get(i).isPossible()) {
				possibilities++;
			}
        }
		return possibilities;
	}
	
	private boolean canGuess() {
		return (getPossibleAnswersLeft() > 0);
	}
	
	public void correctGuess() {
		saveAnswers();
		if (playerWin()) {
			new AddScoreWindow(this,getScore(),getElapsedTime());
		}
		doNextGame();
	}
	
	private void setEnd() {
		if (endTime == -1) {
			endTime = System.nanoTime();
		}
	}
	
	public int getScore() {
		return (currentQuestionNumber-wrongGuessQuestion)+1;
	}
	
	public boolean playerWin() {
		return (wrongGuessQuestion != 0);
	}
	
	public float narrowDown() {
		float smallestDelta = 1.1f;
		int smallestID = -1;
        for (int i = 0; i < adivinador.data.NIC.LIST_ANSWERS.size(); i++) {
			if (adivinador.data.NIC.LIST_ANSWERS.get(i).isPossible()) {
				float averageDelta = getPossibilityDelta(i);
				
				if (getValidQuestions(i) > 0) {
					if (averageDelta > MAX_GUESS_DELTA) {
						adivinador.data.NIC.LIST_ANSWERS.get(i).setType(Possibility.TYPE_GUESS_IMPOSSIBLE);
					} else if (averageDelta <= smallestDelta) {
						smallestDelta =  averageDelta;
						smallestID = i;	
					}
				}
			}
        }
		
		bestGuess = smallestID;
		
		return smallestDelta;
	}
	
	private float getPossibilityDelta(int index) {
		float totalDelta = 0f;
		int validQuestions = 0;
		for (int j = 0; j < adivinador.data.NIC.LIST_QUESTIONS.size(); j++) {
			if (adivinador.data.NIC.LIST_ANSWERS.get(index).get(j).isAnswered() && userGuess.get(j).isAnswered()) {
			   totalDelta = totalDelta + Math.abs(adivinador.data.NIC.LIST_ANSWERS.get(index).get(j).getAnswer() - userGuess.get(j).getAnswer());
			   validQuestions++;
		   }
		}
		if (validQuestions > 0) {
			totalDelta = totalDelta/validQuestions;
		} else {
			totalDelta = 2.0f;
		}
		return totalDelta;
	}
	
	private float getValidQuestions(int index) {
		int validQuestions = 0;
		for (int j = 0; j < adivinador.data.NIC.LIST_QUESTIONS.size(); j++) {
			if (adivinador.data.NIC.LIST_ANSWERS.get(index).get(j).isAnswered() && userGuess.get(j).isAnswered()) {
			   validQuestions++;
		   }
		}
		return validQuestions;
	}
	
}
