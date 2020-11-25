import Huffman.compression.HuffmanCoding;
import Huffman.decode.HuffmanDecode;

public class Main {

    public static void main(String[] args) {
        HuffmanCoding huffmanCoding = new HuffmanCoding();
        huffmanCoding.setFileData("C:\\Users\\ADMIN\\OneDrive\\Desktop\\data.txt");
        String result = huffmanCoding.getHuffmanCode();
        float compressionRatio = huffmanCoding.getCompressionRatio();
        System.out.println(result);
        System.out.println("COMPRESSION RATIO = " + compressionRatio * 100);

        HuffmanDecode decode = new HuffmanDecode();
        decode.setHuffmanEncoded(result);
        String rawData = decode.getDataText();
        System.out.println("RAW DATA = " + rawData);
    }

}
