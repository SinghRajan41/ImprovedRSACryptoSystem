import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
class Calculate implements Runnable{
    ArrayList<BigInteger> arr = null;
    BigInteger key = null;
    BigInteger MOD = null;
    Calculate(ArrayList<BigInteger> arr , BigInteger key , BigInteger MOD){
        this.arr = arr;
        this.key = key;
        this.MOD = MOD;
    }
    public void run(){
        for(int i=0;i<arr.size();i++){
            arr.set(i , arr.get(i).modPow(key , MOD));
        }
    }
    public void display(){
        for(int i=0;i<arr.size();i++){
            System.out.print(arr.get(i));
        }
    }
}
public class RSA {
    static void initialiseEncoding(Map<Character, Integer> encode, Map<Integer, Character> decode) {
        int encodeValue = 10;
        for (char x = 'a'; x <= 'z'; x++) {
            encode.put(x, encodeValue);
            decode.put(encodeValue, x);
            encodeValue += 1; 
        }
        for (char x = 'A'; x <= 'Z'; x++) {
            encode.put(x, encodeValue);
            decode.put(encodeValue, x);
            encodeValue += 1; 
        }
        int spaceValue = 10;
        encode.put(' ', spaceValue);
        decode.put(spaceValue, ' ');
        encodeValue += 1; 
        int newlineValue = 12;
        encode.put('\n', newlineValue);
        decode.put(newlineValue, '\n');
        encodeValue += 1;
        String commonCharacters = ".,!?;:'\"()-[]{}<>\\/"; 
        for (int i = 0; i < commonCharacters.length(); i++) {
            char c = commonCharacters.charAt(i);
            encode.put(c, encodeValue);
            decode.put(encodeValue, c);
            encodeValue += 1; 
        }
    }
    
    
    static final int BLOCK_SIZE = 4;

    static Vector<BigInteger> generateMessageBlocks(String filePath, Map<Character, Integer> encoding) throws IOException {
        String message = "";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            message += line;
        }
        reader.close();
        Vector<BigInteger> blocks = new Vector<>();
        for (int i = 0; i < message.length(); i += BLOCK_SIZE) {
            String temp = message.substring(i, Math.min(i + BLOCK_SIZE, message.length()));
            StringBuilder num = new StringBuilder();
            for(int j=0; j<temp.length(); j++){
                Character c = temp.charAt(j);
                if (encoding.containsKey(c)) {
                    num.append(encoding.get(c));
                } else {
                    continue;
                }
            }
            if (num.length() > 0) {
                blocks.add(new BigInteger(num.toString()));
            }
        }
        return blocks;
    }
    

    public static void main(String[] args) throws IOException, InterruptedException {
        Map<Character, Integer> encode = new HashMap<>();
        Map<Integer , Character> decode = new HashMap<>();
        initialiseEncoding(encode , decode);
        String filePath = args[0];
        BigInteger key = new BigInteger(args[1]);
        BigInteger MOD = new BigInteger(args[2]);
        Vector<BigInteger> blocks = generateMessageBlocks(filePath, encode);
        int cores = 36;
        int n = blocks.size();
        if(n > cores){
            ArrayList<BigInteger> batch[] = new ArrayList[cores];
            for (int i = 0; i < cores; i++) {
                batch[i] = new ArrayList<>();
            }
            for (int i = 0; i < n; i++) {
                int batchIndex = i % cores;
                batch[batchIndex].add(blocks.get(i));
            }
            Calculate workers[] = new Calculate[cores];
            for(int i=0;i<cores;i++){
                workers[i] = new Calculate(batch[i], key, MOD);
            }
            Thread workerThread[] = new Thread[cores];
            for(int i=0;i<cores;i++){
                workerThread[i] = new Thread(workers[i]);
            }
            for(int i=0;i<cores;i++){
                workerThread[i].start();
            }
            for(int i=0;i<cores;i++){
                workerThread[i].join();
            }
            System.out.println("Encrypted Plain Text Successfully\n");
        }else{
            for(int i=0;i<n;i++){
                blocks.set(i , blocks.get(i).modPow(key , MOD));
            }
        }
    }
}
// Private Key (d): 787419476756586885783369021508771
// Public Key (e): 65537
// Modulus (n): 1369757475261915175030872765549261
