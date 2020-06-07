import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Sermaya Piyasalari & Hazine Vaka Calismasi
 * Kelime & Cumle Sayaci
 * 
 * @author seren
 *
 */

public class WordSentenceCount implements Runnable {
	
    private final String buffer;
    private final ConcurrentMap<String,Integer> wordFrequency;
    
    private final static String DELIMETERS_WORD = " \t\n";
    private final static Set<Character> sentenceDelim = new HashSet<>(Arrays.asList('.', '!', '?'));
    
    public WordSentenceCount(String buffer, ConcurrentMap<String,Integer> counts) {
        this.wordFrequency = counts;
        this.buffer = buffer;
    }

    // Updating word count if exist other wise adding 1 for new word in global list
    private void updateCount(String word) {
        Integer currentCount = wordFrequency.get(word);
        
        if (currentCount == null) {
            wordFrequency.put(word, 1);
        } else {    	
        	wordFrequency.replace(word, currentCount + 1);
        }
    } 

    // Separates words in sentences
    public void run() {
        StringTokenizer st = new StringTokenizer(buffer, DELIMETERS_WORD);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            updateCount(token);
        }
    } 
    
    // Sorting by value of UnsortedMap
    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {
    	Map<String, Integer>  sortedList = unsortMap.entrySet().stream()
    	        .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
    	        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    	return sortedList;
    }

    @SuppressWarnings("resource")
	public static void main(String args[]) throws java.io.IOException {
    	
    	if(args.length == 0) {
        	System.out.println("TXT dosyasinin yolunu parametresi bos!");
        	System.exit(0);
        }
       
        //Thread Queue with default=5 Threads in pool.
    	int numThreads 	= 5;
    	ExecutorService threadPool = Executors.newFixedThreadPool(numThreads); 
    	
    	int totalWord = 0;
    	int totalSentence = 0;
    	
    	BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        ConcurrentMap<String,Integer> wordCountMap = new ConcurrentHashMap<String,Integer>();
        
        String centence = "";
        int val = 0;
        
        while ((val = reader.read()) != -1) {
        	
        	char readingChar = (char) val;
        	if(sentenceDelim.contains(readingChar)) {
        		centence = centence.trim();
        		threadPool.submit(new WordSentenceCount(centence, wordCountMap));
        		totalSentence ++;
        		centence = "";
        	} else if(val == 10 || val == 13){
        	} else {
        		centence += readingChar;
        	}
        }
        
        threadPool.shutdown();

        Map<String, Integer> sortedMap = sortByValue(wordCountMap);
        for (Map.Entry<String,Integer> entry : sortedMap.entrySet()) {
        	System.out.println(entry.getKey() + " " + entry.getValue());
            totalWord += entry.getValue();
        }
      
        System.out.println("Sentence Count  : " + totalSentence);
        System.out.println("Avg. Word Count : " + (totalWord / (totalSentence == 0 ? 1 : totalSentence)));
    }
}