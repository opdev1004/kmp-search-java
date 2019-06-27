/**
 * 
 * Current implementation is limited to returning the index of first character in searching query in text.
 * This can be modified to return list of array index for all the matching result.
 * It is very simple modification therefore, leave that part to people who would like to do it.
 */
public class KMP {
	private int[] partialMatchTable;

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		initPatialTable(pattern);
		
		long start = System.nanoTime();
		
		int textPos = 0; // start of current match in text
		int patternPos = 0; // position of current character in pattern
		
		while( patternPos + textPos < text.length()){
			if(pattern.charAt(patternPos) == text.charAt(patternPos + textPos)){ // match
				patternPos++;
				if(patternPos == pattern.length()){ // found string
					System.out.println("The time taken for Knuth Morris Pratt (KMP) search: " + (System.nanoTime() - start));
					return textPos;
				}
			}
			else if(partialMatchTable[patternPos] == -1){ // mismatch, no self overlap
				textPos = textPos + patternPos + 1;
				patternPos = 0;
			}
			else{ // mismatch, with self overlap
				textPos = textPos + patternPos - partialMatchTable[patternPos]; // match position jumps forward
				patternPos = partialMatchTable[patternPos];
			}
		}
		return -1;		
	}
	
	
	public void initPatialTable(String pattern) {
		partialMatchTable = new int[pattern.length()];
		// Special cases
		partialMatchTable[0] = -1;
		partialMatchTable[1] = 0;
		int prefixPos = 0; // position in prefix
		int pos = 2;	//position in table
		while(pos < pattern.length()){
			if(pattern.charAt(pos - 1) == pattern.charAt(prefixPos)){
				partialMatchTable[pos] = prefixPos + 1;
				pos++;
				prefixPos++;
			}
			else if(prefixPos > 0){
				prefixPos = partialMatchTable[prefixPos];
			}
			else{
				partialMatchTable[pos] = 0;
				pos++;
			}
		}
	}
	
	/**
	 * Perform brute force search
	 * 
	 * This should return the starting index of the first substring match if it exists, or -1 if it doesn't.
	 *
	 * This is just given to show the difference between KMP Search and BF Search
	 *
	 */
	public int BFsearch(String pattern, String text) {
		long start = System.nanoTime();
		for(int i = 0; i < text.length() - pattern.length(); i++){
			boolean found = true;
			for(int j = 0; j < pattern.length() - 1; j++){
				if(pattern.charAt(j) != text.charAt(i + j)){
					found = false;
					break;
				}
			}
			if(found){ // found match
				System.out.println("The time taken for Brute Force Search: " + (System.nanoTime() - start));
				return i;
			}
		}
		return -1;
	}	
}



