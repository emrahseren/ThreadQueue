## ThreadQueue Project

Sermaya Piyasalari & Hazine Vaka Calismasi
Kelime & Cumle Sayaci

## Clone

> git clone https://github.com/emrahseren/ThreadQueue.git

> cd ThreadQueue

> javac WordSentenceCount.java

Then you shoud pass txt file path to the application to read and parse with word and centences.
Like below;

> java WordSentenceCount C:\dev\javagit\ThreadQueue\src\textExample.txt

![](https://github.com/emrahseren/ThreadQueue/blob/master/run.JPG)

# Explanation 
Used ExecutorService for thread pool 
Ex : Executors.newFixedThreadPool(5);

Used ConcurrentHashMap for ThreadSafe Map <String(word), Integer(count)>
Ex : ConcurrentHashMap<String,Integer>();

Used Stream.sorted.collection to order by value of Map
Ex : UnsortedMap.stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).collect...
