package lib;

public class Task2 {

	public static void main(String[] args) {
		//cRead file
		In in = new In("protein.txt");
		int value = 0;
		String protienTxt = in.readAll();
		
		
		while (!in.isEmpty()) {
            String s = in.readLine();
            protienTxt = protienTxt + s;
        }
		String[] keys = protienTxt.split(" ");
		TST<Integer> st = new TST<Integer>();
	    for (int i = 0; i < keys.length; i++) {
//	         value = st.get(keys[i]);
	         if(st.get(keys[i]) == null) {	        	 
	        	 st.put(keys[i], 1);
	         } else {
	        	 st.put(keys[i], st.get(keys[i]) + 1);
	         }
	    }
	    
	    System.out.println("Freq : protein     :" + st.get("protein"));
	    System.out.println("Freq : complex     :" + st.get("complex"));
	    System.out.println("Freq : PPI         :" + st.get("PPI"));
	    System.out.println("Freq : prediction  :" + st.get("prediction"));
	    
	    System.out.println("\n");
  	    
	}
	
}
