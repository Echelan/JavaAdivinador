/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package adivinador.data;

import adivinador.node.AnswerNode;
import adivinador.node.Possibility;
import adivinador.node.QuestionNode;
import adivinador.node.NodeList;
import adivinador.node.ScoreNode;
import adivinador.view.CasualtyWindow;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * @author Andres
 */
public abstract class NIC {
	
	//<editor-fold defaultstate="collapsed" desc="Information Lists">
		/**
		 * List of Questions.
		 */
		public static NodeList<QuestionNode> LIST_QUESTIONS;
		/**
		 * List of Images.
		 */
		public static List<String> RAW_LIST_IMAGES;
		/**
		 * Formatted List of Scores.
		 */
		public static NodeList<ScoreNode> LIST_SCORES;
		/**
		 * Formatted List of Answers.
		 */
		public static NodeList<Possibility> LIST_ANSWERS;
		/**
		 * Maximum number of score entries to save.
		 */
		public static final int MAX_SCORES = 10;
	//</editor-fold>

	public static void loadAllData() {
		
		try {
			buildQuestions();
			buildScores();
			buildAnswers();
		} catch (IOException ex) {
			
		}
		
	}
	
	public static boolean checkCasualty(int a, int b) {
		boolean similar = false;
		for (int i = 0; i < LIST_ANSWERS.get(a).getName().split(" ").length; i++) {
			if (LIST_ANSWERS.get(a).getName().split(" ")[i].length() > 3) {
				if (LIST_ANSWERS.get(b).getName().contains(LIST_ANSWERS.get(a).getName().split(" ")[i])) {
					similar = true;
				}
			}
		}
		return similar;
	}
	
	public static void checkAllCasualties() {
		int id1 = 0; 
		int id2 = 0;
		boolean casualtyFound = false;
		for (int i = 0; i < LIST_ANSWERS.size(); i++) {
			for (int j = i+1; j < LIST_ANSWERS.size(); j++) {
				if (checkCasualty(i,j)){
					id1 = i;
					id2 = j;
					casualtyFound = true;
				}
			}
		}
		if (casualtyFound) {
			new CasualtyWindow(id1,id2);
		}
	}
	
	public static void checkLastCasualty() {
		int newID = LIST_ANSWERS.size()-1; 
		for (int i = 0; i < LIST_ANSWERS.size()-1; i++) {
			if (checkCasualty(newID,i)) {
				new CasualtyWindow(i,newID);
			}
		}
	}
	
	private static void buildQuestions() throws IOException {
		List<String> readInfoQ;

		File archivo = new File("db/listQuestions.txt");
		readInfoQ = Files.readAllLines(archivo.toPath());
		
		LIST_QUESTIONS = new NodeList<>();

		for (int i = 0; i < readInfoQ.size(); i++) {
			LIST_QUESTIONS.add(new QuestionNode(readInfoQ.get(i)));
		}
	}
	
	private static void buildAnswers() throws IOException {
		List<String> readInfoA;

		File archivo = new File("db/listAnswers.txt");
		readInfoA = Files.readAllLines(archivo.toPath());
		
		LIST_ANSWERS = new NodeList<>();

		for (int i = 0; i < readInfoA.size(); i++) {
			String answerName = readInfoA.get(i).split(";")[0];
			
			LIST_ANSWERS.add(new Possibility(answerName,Possibility.TYPE_GUESS_POSSIBLE));
			
			for (int j = 1; j < readInfoA.get(i).split(";").length; j++) {
				float value = Float.parseFloat(readInfoA.get(i).split(";")[j]);
				
				if (Float.isNaN(value)) {
					value = -1.0f;
				}
				LIST_ANSWERS.get(i).add(new AnswerNode(value));
			}
			LIST_ANSWERS.get(i).fillAnswers(LIST_QUESTIONS.size());
		}
	}
	
	private static void buildScores() throws IOException {
		List<String> readInfoS;

		File archivo = new File("db/listScores.txt");
		readInfoS = Files.readAllLines(archivo.toPath());
		
		LIST_SCORES = new NodeList<>();
		for (int i = 0; i < MAX_SCORES; i++) {
			String playerName;
			int score;
			if (i < readInfoS.size()) {
				playerName = readInfoS.get(i).split(";")[0];
				score = Integer.parseInt(readInfoS.get(i).split(";")[1]);
			} else {
				playerName = "AAAAAAA";
				score = 0;
			}
			LIST_SCORES.add(new ScoreNode(playerName, score));
		}
	}
	
	public static void saveAll(){
		try {
			updateQuestionsFile();
			updateAnswersFile();
			updateScoreFile();
		} catch (Exception e) {
			
		}
	}
	
	public static void updateQuestionsFile() throws IOException {
		File archivo = new File("db/listQuestions.txt");
		FileWriter fw = new FileWriter(archivo.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for(int i = 0; i < LIST_QUESTIONS.size(); i++){
			bw.write(LIST_QUESTIONS.get(i).getQuestion());
			bw.newLine();
		}
		bw.close();

		fw.close();
	}
	
	public static void updateAnswersFile() throws IOException {
		File archivo = new File("db/listAnswers.txt");
		FileWriter fw = new FileWriter(archivo.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for(int i = 0; i < LIST_ANSWERS.size(); i++){
			String entry = "";
			
			entry = entry + LIST_ANSWERS.get(i).getName();
			entry = entry + ";";
			
			for (int j = 0; j < LIST_ANSWERS.get(i).size(); j++) {
				entry = entry + LIST_ANSWERS.get(i).get(j).getAnswer();
				entry = entry + ";";
			}
			
			bw.write(entry);
			bw.newLine();
		}
		bw.close();

		fw.close();
	}
	
	public static void updateScoreFile() throws IOException {
		File archivo = new File("db/listScores.txt");
		FileWriter fw = new FileWriter(archivo.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		sortScores();
		
		for(int i = 0; i < LIST_SCORES.size(); i++){
			String entry = "";
			entry = entry + LIST_SCORES.get(i).getName();
			entry = entry + ";";
			entry = entry + LIST_SCORES.get(i).getScore();
			
			bw.write(entry);
			bw.newLine();
		}
		
		bw.close();

		fw.close();
	}
	
	public static void updateImagesFile() throws IOException {
		File archivo = new File("db/listImages.txt");
		FileWriter fw = new FileWriter(archivo.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for(int i = 0; i < RAW_LIST_IMAGES.size(); i++){
			bw.write(RAW_LIST_IMAGES.get(i));
			bw.newLine();
		}
		bw.close();

		fw.close();
	}
	
	private static void sortScores() {
		NodeList<ScoreNode> scoreList = new NodeList<>();
		
		while (scoreList.size() < MAX_SCORES && !LIST_SCORES.isEmpty()) {
			int highestScore = 0;
			int highestID = 0;
			
			for (int i = 0; i < LIST_SCORES.size(); i++) {
				if (LIST_SCORES.get(i).getScore() > highestScore) {
					highestScore = LIST_SCORES.get(i).getScore();
					highestID = i;
				}
			}
			
			scoreList.add(new ScoreNode(LIST_SCORES.get(highestID).getName(),LIST_SCORES.get(highestID).getScore()));
			LIST_SCORES.remove(highestID);
		}
		
		LIST_SCORES = scoreList;
	}
	
}
