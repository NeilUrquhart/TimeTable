package personalityHelpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import ontology.elements.Personality;

public class PersonalityFactory {
	
	//Singleton
	private static PersonalityFactory instance;
	
	private PersonalityFactory() {
		init();
	}
	
	public static PersonalityFactory getInstance() {
		if (instance == null) {
			instance = new PersonalityFactory();
		}
		return instance;
	}
	
	//end singleton
	private static  ArrayList<Personality> personalities = new ArrayList<Personality>();
	private static Random rnd = new Random();
	
	public static void main(String[] args) {
		//Test
		init();
	}
	
	private static void init() {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader("survey.csv");
			br = new BufferedReader(fr);

			String sCurrentLine;

			br.readLine();//skip header
			while ((sCurrentLine = br.readLine()) != null) {
				String[] data = sCurrentLine.split(",");
				if (!data[9].equals("1")){
					Personality p = new Personality();
					//Check that user confirmed that they answered yes to consent question 
					//System.out.println(sCurrentLine);
					//Get unusable slots
					for (int x=16; x<=20;x++)
						getUnacceptable(p,data[x]);
					
					//Get awkward slots
					String[] awkward = data[21].split("/");
					for(String slot: awkward) {
						try {
							int nSlot = Integer.parseInt(slot);
							nSlot--;
							if (nSlot>44)
								System.out.println("Invalid slot");
							else
								p.addAwkward(nSlot);
						}catch(Exception e) {
							//Exception raised by non-numeric string ...  ignore
						}
					}
					personalities.add(p);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

	private static void getUnacceptable(Personality p, String slot) {
		try {
			int nSlot = Integer.parseInt(slot);
			nSlot--;//Questionaire starts from 1, this programme starts from 0
			
			if (nSlot>44)
				System.out.println("Invalid slot");
			else
				p.addUnacceptable(nSlot);
		}catch (Exception e){
			//Ignore exception - they occur due to blank cells being left in Moodle
		}
	}
	
	public  Personality getPersonality() {
		return personalities.get(rnd.nextInt(personalities.size()));
	}
	
	public  Personality getPersonality(int indx) {
		return personalities.get(indx);
	}
	
	public void getSize() {
		System.out.println(personalities.size());
	}
}
