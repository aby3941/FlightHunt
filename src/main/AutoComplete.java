package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.TST;

public class AutoComplete {
	public List<String> getMatchingList(TST<Integer> citiesTST, String pattern) {
		Iterable <String> departureCitiesMatchItr = citiesTST.prefixMatch(pattern);
		List<String> departureCitiesMatchList = new ArrayList<String>();
		departureCitiesMatchItr.forEach(departureCitiesMatchList::add);
		
		return departureCitiesMatchList;
	}
	
	public TST<Integer> generateTST(HashMap<String, AirportDetails> airportsDetails) {
		TST<Integer> citiesTST = new TST<Integer>();
		int cityNumber = 0;
		for(String city: airportsDetails.keySet()) {
			citiesTST.put(city, cityNumber);
			cityNumber++;
		}
		return citiesTST;
	}
}
