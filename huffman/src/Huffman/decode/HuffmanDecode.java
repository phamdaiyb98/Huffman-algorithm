package Huffman.decode;

import Huffman.Node;

import java.util.ArrayList;
import java.util.List;

public class HuffmanDecode {

    //++ du lieu da ma hoa
    private String encodeData;
    //++ cay huffman
    private List<Node> tree = new ArrayList<>();
    //++ ma nhi phan cua du lieu
    private String binaryCodeStr;
    //++ chuoi luu ket qua
    private StringBuffer result = new StringBuffer();

    //++ set du lieu da ma hoa
    public void setHuffmanEncoded(String encodeData) {
        this.encodeData = encodeData;
    }

    //++ lay du lieu giai nen
    public String getDataText() {
        createHuffmanTree();
        HuffmanDecode();
        return result.toString();
    }

    //++ tao nut tu du lieu node
    private Node getNodeFromNodeInfo(String nodeInfo) {
        Node node = new Node();
        String[] keyValues = nodeInfo.split("\n");
        for (String keyValue : keyValues) {
            String[] kV = keyValue.split("::");
            String key = kV[0];
            String value = kV[1];
            switch (key) {
                case "root": {
                    node.setRoot(Boolean.parseBoolean(value));
                    break;
                }
                case "l": {
                    node.setL(value.toCharArray()[0]);
                    break;
                }
                case "i": {
                    node.setI(Integer.parseInt(value));
                    break;
                }
                case "il": {
                    node.setIl(Integer.parseInt(value));
                    break;
                }
                case "ir": {
                    node.setIr(Integer.parseInt(value));
                    break;
                }
            }
        }
        return node;
    }

    //++ tao cay Huffman
    private void createHuffmanTree() {
        String[] s = encodeData.split("###");
        String treeInfo = s[0];
        this.binaryCodeStr = s[1];

        String[] nodeInfos = treeInfo.split("##");
        for (String nodeInfo : nodeInfos) {
            Node node = getNodeFromNodeInfo(nodeInfo);
            tree.add(node);
        }
    }

    //++ tim ki tu voi index tuong ung
    private Character findLetterByIndex(int index) {
        Node node = findNodeByIndex(index);
        assert node != null;
        return node.getL();
    }

    //++ tim node theo index
    private Node findNodeByIndex(int index) {
        for (Node node : tree) {
            if (index == node.getI()) {
                return node;
            }
        }
        return null;
    }

    //++ tim index cua con trai
    private int getLeftChildIndex(int index) {
        Node node = findNodeByIndex(index);
        assert node != null;
        return node.getIl();
    }

    //++ tim index cua con phai
    private int getRightChildIndex(int index) {
        Node node = findNodeByIndex(index);
        assert node != null;
        return node.getIr();
    }

    //kiem tra xem co phai la hay ko
    private boolean checkIsLeave(int index) {
        return getLeftChildIndex(index) == -1;
    }

    //++ tim index node goc
    private int getRootIndex() {
        for (Node node : tree) {
            if (node.isRoot()) {
                return node.getI();
            }
        }
        return -1;
    }

    //++ ham giai ma
    private void HuffmanDecode() {
        //++ mang cac ma nhi phan roi rac
        String[] codeArr = binaryCodeStr.split("");
        //++ index node goc
        int rootIndex = getRootIndex();
        //++ luu index node dang duyet toi
        int currentIndex = rootIndex;
        for (String binaryCode : codeArr) {
            //++ neu la bit  0 di ben trai
            if ("0".equals(binaryCode)) {
                int leftChildIndex = getLeftChildIndex(currentIndex);
                //++ neu la leave thi lay ki tu ra, quay lai node goc
                if (!checkIsLeave(leftChildIndex)) {
                    currentIndex = leftChildIndex;
                } else {
                    result.append(findLetterByIndex(leftChildIndex));
                    currentIndex = rootIndex;
                }
            }
            //++ neu la bit 1 di ben phai
            if ("1".equals(binaryCode)) {
                int rightChildIndex = getRightChildIndex(currentIndex);
                //++ neu la leave thi lay ki tu ra, quay lai node goc
                if (!checkIsLeave(rightChildIndex)) {
                    currentIndex = rightChildIndex;
                } else {
                    result.append(findLetterByIndex(rightChildIndex));
                    currentIndex = rootIndex;
                }
            }
        }
    }
}
