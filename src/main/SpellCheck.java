package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.Sequences;
import lib.TST;

public class SpellCheck {
	public List<String> findSimilar(List<String> words, String indexWord, int topK){
		List<String> topSimilarWords = new ArrayList<String>();
//		String topSimilarWords[] = new String[topK];
		indexWord = indexWord.toLowerCase();
		int k = 0;
		while(k<topK) {
			int min_edit_dist = 1000, distance=0;
			String nearestWord = "";
			for(String word: words)
			{
				String str = word; 
				distance = Sequences.editDistance(indexWord, str);
				if(distance<min_edit_dist)
				{
				min_edit_dist = distance;
				nearestWord = str;
				}
			}
			topSimilarWords.add(nearestWord);
			words.remove(nearestWord);
			k++;
		}
		return(topSimilarWords);
	}
	
	public List<String> generateList(HashMap<String, AirportDetails> airportsDetails) {
		List<String> citiesList = new ArrayList<String>();
		for(String city: airportsDetails.keySet()) {
			citiesList.add(city);
		}
		return citiesList;
	}
}
