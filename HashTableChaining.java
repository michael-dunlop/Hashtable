import java.util.ArrayList; 
import java.util.LinkedList;

class Pair<K,V> 
{
    /*
    The Pair class is intended to store key, value pairs. It'll be helpful
    for part 1.2 of the assignment.
    */
    public K key;
    public V value;

    public Pair(K key, V value) 
   {
        this.key = key;
        this.value = value;
   }

}

/**************PART 1.2.1*******************/

public class HashTableChaining<K,V> 
{
   /*
    Write your code for the hashtable with chaining here. You are allowed
    to use arraylists and linked lists.
    */
   ArrayList<LinkedList<Pair<String, Integer>>> hashTable;
   int capacity;
   double loadFactor = .75;
       
    public HashTableChaining(int capacity)
    {
        /*
        Initialize your hashtable with capacity equal to the input capacity.
        */
        this.hashTable = new ArrayList<>(capacity);
        this.capacity = capacity;

        for(int i = 0; i < capacity; i++){
            LinkedList<Pair<String, Integer>> list = new LinkedList<>();
            hashTable.add(i, list);
        }

    }
    public void insert(String key, int val)
    {
        /*
        Insert the key into the hashtable if it is not already in the hashtable.
        */

        int hashValue = hash(key);


        Pair<String, Integer> obj = new Pair<>(key, val);

        if(hashTable.get(hashValue).isEmpty()){
            hashTable.get(hashValue).add(obj);

            System.out.println("size: " + this.size());

            if((((double) this.size())/((double) this.capacity)) >= loadFactor) {
                rehash();
            }

        } else {

            for(int i = 0; i < (hashTable.get(hashValue)).size(); i++){

                String tempKey = hashTable.get(hashValue).get(i).key;
                if(tempKey.equals(key)){

                    int currentValue = hashTable.get(hashValue).get(i).value;
                    hashTable.get(hashValue).get(i).value = currentValue + 1;

                    if((((double) this.size())/((double) this.capacity)) >= loadFactor) {
                        rehash();
                    }

                    return;

                }
            }

            hashTable.get(hashValue).add(obj);


        }

    }
    public void remove(String key)
    {
        /*
        Remove key from the hashtable if it is present in the hashtable.
        */
        int hashValue = hash(key);

        if(hashTable.get(hashValue).isEmpty()) return;

        for(int i = 0; i < hashTable.get(hashValue).size(); i++){
            if(hashTable.get(hashValue).get(i).key.equals(key)){
                hashTable.get(hashValue).remove(i);
                return;
            }
        }
    }
    public boolean contains(String key)
    {
        /* 
        Search the hashtable for key, if found return true else
        return false
        */
        int hashValue = hash(key);

        for(int i = 0; i < hashTable.get(hashValue).size(); i++) {
            if (hashTable.get(hashValue).get(i).key.equals(key)) {
                return true;
            }
        }

        return false;
    }
    
    public int size()
    {
        /*
        return the total number of keys in the hashtable.
        */
        int size = 0;
        for(int i = 0; i < this.capacity; i++){
            size = size + hashTable.get(i).size();
        }
        return size;
    }
    
    public int hash(String key)
    {
        /*
        Use Horner's rule to compute the hashval and return it.
        */
        int hashcode = 0;
        for(int i = 0; i < key.length(); i++){
            hashcode = (37 * hashcode + key.charAt(i)) % this.capacity;
        }
        return hashcode % this.capacity;
    }

    public int getVal(String key)
    {
        
        /*
        return the value corresponding to the key in the hashtable.
        */
        int hashValue = hash(key);

        for(int i = 0; i < ((LinkedList) hashTable.get(hashValue)).size(); i++) {
            if (hashTable.get(hashValue).get(i).key.equals(key)){
                return (int) hashTable.get(hashValue).get(i).value;
            }
        }

        return -1;
    }

    
    public void rehash()
    {
        /*
        Resize the hashtable such that the new size is the first prime number
        greater than two times the current size.
        For example, if current size is 5, then the new size would be 11.
        */
        int n = this.capacity * 2;
        if(n == 1) n = 2;
        n = findNextPrime(n);

        ArrayList<Pair<String, Integer>> temp = new ArrayList<>(this.size());
        int index = 0;

        for(int i = 0; i < this.hashTable.size(); i++){
            for(int k = 0; k < this.hashTable.get(i).size(); k++){
                temp.add(index, this.hashTable.get(i).get(k));
                index++;
            }
            this.hashTable.get(i).clear();
        }
        this.hashTable.clear();

        this.capacity = n;
        this.hashTable = new ArrayList<>(n);
        for(int i = 0; i < this.capacity; i++){
            LinkedList<Pair<String, Integer>> list = new LinkedList<>();
            hashTable.add(i, list);
        }



        for(int i = 0; i < temp.size(); i++){
            String key = temp.get(i).key;
            int val = temp.get(i).value;
            this.insert(key, val);
        }



    }

    public int findNextPrime(int size){

        while(true){
            if(size % 2 == 0) size++;
            if(isPrime(size)) break;
            size = size + 2;
        }

        return size;
    }

    public boolean isPrime(int a){
        if(a <= 1) return false;
        else if (a <= 3) return true;
        else if (a % 2 == 0 || a % 3 == 0) return false;
        int i = 5;
        while(i * i <= a){
            if (a % i == 0 || a % (i + 2) == 0) return false;
            i = i + 6;
        }

        return true;
    }
    
    /**************PART 1.2*******************/

    public String[] mostFrequentStrings(String[] in)
    {
        /*
        Given an array of strings, print the five most
        frequent strings. You must use your implementation
        for hashtable with seperate chaining for this.
        */
        int initialCapacity = ((4 * in.length) / 3) + 2;
        HashTableChaining<K, V> hTable = new HashTableChaining<>(initialCapacity);
        int minVal = -1;
        ArrayList<Pair<String, Integer>> top5 = new ArrayList<>(5);
        for(int i = 0; i < 5; i++) {
            Pair<String, Integer> element = new Pair<>("", -1);
            top5.add(i, element);
        }

        for(int i = 0; i < in.length; i++){
             hTable.insert(in[i], 0);
        }

        for(int i = 0; i < hTable.size(); i++){
            if(hTable.hashTable.get(i).isEmpty()){
                continue;
            }
            for(int k = 0; k < hTable.hashTable.get(i).size(); k++){
                Pair<String, Integer> tempPair = hTable.hashTable.get(i).get(k);
                if(tempPair.value >= minVal){
                    top5.set(0, tempPair);
                    for(int j = 1; j < 5; j++){
                        if(tempPair.value > top5.get(j).value) {
                            top5.set(j - 1, top5.get(j));
                            top5.set(j, tempPair);
                        }
                    }
                    minVal = top5.get(0).value;
                }
            }
        }

        String[] mostFrequent = new String[5];
        for(int i = 0; i < 5; i++) {
            mostFrequent[i] = top5.get(i).key;
        }

        return mostFrequent;
    }
    
}
