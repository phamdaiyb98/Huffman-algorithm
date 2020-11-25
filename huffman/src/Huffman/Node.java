package Huffman;

public class Node {
    //++ gia tri node
    private int v;
    //++ ki tu
    private char l;
    //++ true - root, false - leave
    private boolean root;
    //++ index
    private int i;
    //++ index con trai
    private int il;
    //++ index con phai
    private int ir;

    @Override
    public String toString() {
        String s = "";
        if (root) {
            s += "root::true\n";
        } else {
            if (l != '\u0000') {
                s += "l::" + l + "\n";
            }
        }
        s += "i::" + i;
        if (il != -1) {
            s += "\nil::" + il + "\nir::" + ir;
        }
        return s;
    }

    public Node() {
        this.il = -1;
        this.ir = -1;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public char getL() {
        return l;
    }

    public void setL(char l) {
        this.l = l;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getIl() {
        return il;
    }

    public void setIl(int il) {
        this.il = il;
    }

    public int getIr() {
        return ir;
    }

    public void setIr(int ir) {
        this.ir = ir;
    }
}
