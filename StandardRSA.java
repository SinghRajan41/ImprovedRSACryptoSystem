import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StandardRSA {
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
        int n = blocks.size();
        for(int i=0;i<n;i++){
            blocks.set(i , blocks.get(i).modPow(key , MOD));
        }
        System.out.println("Excrypted Plain Text Successfully\n");
    }

}
