package Huffman.compression;

import Huffman.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class HuffmanCoding {
    //++ chuoi ban dau doc tu file
    private StringBuffer s = new StringBuffer();
    //++ mang ki tu ban dau
    private char[] letters;
    //++ mang tan suat cua ki tu trong letters
    private int[] frequency;
    //++ danh sach cac node cua cay nhi phan Huffman
    private List<Node> tree = new ArrayList<>();
    //++ bang anh xa letter - code
    private Map<Character, String> encodeMap = new HashMap<>();
    //++ mang ket qua ma nhi phan
    private StringBuffer result = new StringBuffer();

    //++ set duong dan file
    public void setFileData(String path) {
        try {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                s.append(line);
            }
        } catch (IOException ignored) {
            //do nothing
        }
    }

    //++ lay ket qua nen
    public String getHuffmanCode() {
        countFrequency();
        createHuffmanTree();
        getEncodeMapHuffman();
        compressionHuffman();
        int n = letters.length;
        for (int i = 0; i < n; i++) {
            System.out.println(letters[i] + " --- " + frequency[i]);
        }
        return result.toString();
    }

    //++ lay ket qua nen
    public float getCompressionRatio() {
        float totalBit = 0;
        for (Character key : encodeMap.keySet()) {
            int frequency = getFrequencyByLetter(key);
            int countBit = encodeMap.get(key).length();
            totalBit += frequency * countBit;
        }
        return (1 - totalBit / (s.length() * 16));
    }

    //++ lay tan so voi ki tu tuong ung
    private int getFrequencyByLetter(char letter) {
        int n = letters.length;
        for (int i = 0; i < n; i++) {
            if (letter == letters[i]) {
                return frequency[i];
            }
        }
        return 0;
    }

    //++ tinh tan suat cua cac tu
    private void countFrequency() {
        //++ mang luu cac ki tu ban dau
        char[] rawLetter = s.toString().toCharArray();
        //++ mang tam luu cac ki tu
        char[] lettersTemp = new char[rawLetter.length];
        //++ mang tam luu tan suat
        int[] frequencyTemp = new int[rawLetter.length];
        //++ lap tim tan suat
        int n = rawLetter.length;
        for (int i = 0; i < n; i++) {
            boolean check = false;
            int index = 0;
            for (int j = 0; j < n; j++) {
                if (rawLetter[i] == lettersTemp[j]) {
                    check = true;
                    index = j;
                    break;
                }
            }
            if (!check) {
                lettersTemp[i] = rawLetter[i];
                frequencyTemp[i]++;
            } else {
                frequencyTemp[index]++;
            }
        }

        //++ dem phan tu khac 0
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (frequencyTemp[i] != 0) {
                count++;
            }
        }
        letters = new char[count];
        frequency = new int[count];
        //++ loc du lieu cu
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (frequencyTemp[i] != 0) {
                letters[index] = lettersTemp[i];
                frequency[index] = frequencyTemp[i];
                index++;
            }
        }
    }

    //++ ham tim min_1
    private int findMinElement(int[] a) {
        int n = a.length;
        int indexMin_1 = 0;
        //++ khoi tao phan tu ban dau chua duoc duyet
        for (int i = 0; i < n; i++) {
            if (a[i] > 0) {
                indexMin_1 = i;
                break;
            }
        }
        //++ tim phan tu nho nhat
        for (int i = 0; i < n; i++) {
            if (a[i] < a[indexMin_1] && a[i] > 0) {
                indexMin_1 = i;
            }
        }
        return indexMin_1;
    }

    //++ ham tim min_2
    private int findOtherMinElement(int[] a, int indexMin_1) {
        int n = a.length;
        int indexMin_2 = 0;
        //++ khoi tao phan tu ban dau chua duoc duyet
        for (int i = 0; i < n; i++) {
            if (a[i] > 0) {
                indexMin_2 = i;
                break;
            }
        }
        //++ tim phan tu nho nhat
        for (int i = 0; i < n; i++) {
            if (a[i] > 0 && i > indexMin_1 && a[i] <= a[indexMin_2]) {
                indexMin_2 = i;
            }
        }
        return indexMin_2;
    }

    //++ tim node theo index
    private Node findNodeByIndex(int index) {
        for (Node node : tree) {
            if (node.getI() == index) {
                return node;
            }
        }
        return null;
    }

    //++ ham tao cay nhi phan huffman
    private void createHuffmanTree() {
        //++ mang luu cac value cua tree
        int frequencyLength = frequency.length;
        int[] frequencyExtra = new int[2 * frequencyLength - 1];
        System.arraycopy(frequency, 0, frequencyExtra, 0, frequencyLength);
        int indexInsert = frequencyLength;
        //++ thuat toan se lap n - 1 lan
        for (int i = 0; i < frequencyLength - 1; i++) {
            //++ tim min_1, tao nut con left
            int min_1 = findMinElement(frequencyExtra);
            //++ tim trong tree xem co chua
            Node nodeLeft;
            if (findNodeByIndex(min_1) == null) {
                nodeLeft = new Node();
                nodeLeft.setV(frequencyExtra[min_1]);
                if (min_1 < frequencyLength) {
                    nodeLeft.setL(letters[min_1]);
                }
                nodeLeft.setI(min_1);
                tree.add(nodeLeft);
            } else {
                nodeLeft = findNodeByIndex(min_1);
            }
            //++ danh dau la da duyet qua vi tri nay
            frequencyExtra[min_1] = -1;

            //++ tim min_2
            int min_2 = findOtherMinElement(frequencyExtra, min_1);
            //++ tim trong tree xem co chua
            Node nodeRight;
            if (findNodeByIndex(min_2) == null) {
                nodeRight = new Node();
                nodeRight.setV(frequencyExtra[min_2]);
                if (min_2 < frequencyLength) {
                    nodeRight.setL(letters[min_2]);
                }
                nodeRight.setI(min_2);
                tree.add(nodeRight);
            } else {
                nodeRight = findNodeByIndex(min_2);
            }
            //++ danh dau la da duyet qua vi tri nay
            frequencyExtra[min_2] = -1;

            //++ set nut cha
            Node parent = new Node();
            assert nodeLeft != null;
            assert nodeRight != null;
            parent.setV(nodeLeft.getV() + nodeRight.getV());
            parent.setI(indexInsert);
            parent.setIl(min_1);
            parent.setIr(min_2);
            frequencyExtra[indexInsert] = nodeLeft.getV() + nodeRight.getV();
            tree.add(parent);
            //+++ danh dau da duyet qua, tang index node duoc mo rong, giam dem so phan tu != -1
            if (i == frequencyLength - 2) {
                parent.setRoot(true);
            } else {
                indexInsert++;
            }
        }
    }

    //++ lay ra node goc
    private Node getRoot() {
        for (Node node : tree) {
            if (node.isRoot()) {
                return node;
            }
        }
        return null;
    }

    //++ ham lay ma tu danh sach
    private String getCodeForLeave(List<Integer> codeList) {
        StringBuilder code = new StringBuilder();
        for (Integer i : codeList) {
            code.append(i);
        }
        codeList.remove(codeList.size() - 1);
        return code.toString();
    }

    //++ ham kiem tra da tham con trai, con phai hay chua
    private int checkVisitedChild(List<Integer> visitedIndex, Integer indexLeft, Integer indexRight) {
        //++ 0 - tham con trai
        //++ 1 - tham con phai
        //++ 2 - da tham ca 2 con
        boolean checkLeft = false;
        for (Integer i : visitedIndex) {
            if (i.equals(indexLeft)) {
                checkLeft = true;
                break;
            }
        }
        if (!checkLeft) {
            return 0;
        } else {
            boolean checkRight = false;
            for (Integer i : visitedIndex) {
                if (i.equals(indexRight)) {
                    checkRight = true;
                    break;
                }
            }
            if (!checkRight) {
                return 1;
            }
        }
        return 2;
    }

    //++ duyet cay Huffman tao bang letter - code
    private void getEncodeMapHuffman() {
        //++ stack duyet cay(push - pop)
        Stack<Node> nodeStack = new Stack<>();
        //++ danh sach luu index cac dinh da tham
        List<Integer> visitedList = new ArrayList<>();
        //++ hang doi luu gia tri ma(offer - poll)
        List<Integer> codeList = new ArrayList<>();

        //++ push index phan tu goc vao stack va danh sach da duyet
        Node root = getRoot();
        nodeStack.push(root);
        assert root != null;
        visitedList.add(root.getI());

        //tim left va right cua phan tu dau tien trong ngan xep
        while (!nodeStack.empty()) {
            //++ stack rong thi ket thuc
            //++ phan tu tren cung cua stack
            int indexLeft = nodeStack.peek().getIl();
            int indexRight = nodeStack.peek().getIr();
            //++ kiem tra xem phai leave hay ko
            if (indexLeft != -1) {
                int checkVisited = checkVisitedChild(visitedList, indexLeft, indexRight);
                //++ duyet con trai
                if (checkVisited == 0) {
                    Node node = findNodeByIndex(indexLeft);
                    nodeStack.push(node);
                    assert node != null;
                    visitedList.add(node.getI());
                    codeList.add(0);
                }
                //++ duyet con phai
                if (checkVisited == 1) {
                    Node node = findNodeByIndex(indexRight);
                    nodeStack.push(node);
                    assert node != null;
                    visitedList.add(node.getI());
                    codeList.add(1);
                }
                //++ da duyet ca 2 con
                if (checkVisited == 2) {
                    nodeStack.pop();
                    //++ xoa phan tu cuoi mang code
                    if (!codeList.isEmpty()) {
                        codeList.remove(codeList.size() - 1);
                    }
                }
            } else {
                encodeMap.put(nodeStack.peek().getL(), getCodeForLeave(codeList));
                nodeStack.pop();
            }
        }
    }

    //++ ham lay ma voi ki tu tuong ung
    private String getCodeByLetter(char letter) {
        for (Character character : encodeMap.keySet()) {
            if (character == letter) {
                return encodeMap.get(character);
            }
        }
        return "";
    }

    //++ ham lay thong tin ma hoa tu cay nhi phan
    private void getNodeTree() {
        tree.forEach(node -> {
            result.append(node);
            result.append("##");
        });
        result.append("#");
    }

    //++ ham nen chuoi ban dau thanh ma nhi phan
    private void compressionHuffman() {
        //++ them thong tin cay vao file, ngan cach voi chuoi ma hoa nhi phan bang dau '###'
        getNodeTree();
        char[] rawLetter = s.toString().toCharArray();
        for (char c : rawLetter) {
            result.append(getCodeByLetter(c));
        }
    }
}
