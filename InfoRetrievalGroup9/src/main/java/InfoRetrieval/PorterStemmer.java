package InfoRetrieval;

import java.io.*;

//A word is turned to its root form with the porter stemmer class
class PorterStemmer {

    private static final int INCR = 50;
    private int z, last, j, i;
    private char[] ch;

    //Unit of size where ch is increased
    public PorterStemmer() {
        ch = new char[INCR];
        z = 0;
        last = 0;
    }

    public void addChar(char ch) {
        if (z == this.ch.length) {
            char[] new_b = new char[z + INCR];
            for (int c = 0; c < z; c++) new_b[c] = this.ch[c];
            this.ch = new_b;
        }
        this.ch[z++] = ch;
    }

    public void addChar(char[] w, int wLen) {
        if (z + wLen >= ch.length) {
            char[] new_b = new char[z + wLen + INCR];
            for (int c = 0; c < z; c++) new_b[c] = ch[c];
            ch = new_b;
        }
        for (int c = 0; c < wLen; c++) ch[z++] = w[c];
    }


    private final boolean stemVowel() {
        int i;
        for (i = 0; i <= j; i++) if (!checkCons(i)) return true;
        return false;
    }

    private final boolean checkCons(int i) {
        switch (ch[i]) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return false;
            case 'y':
                return i == 0 || !checkCons(i - 1);
            default:
                return true;
        }
    }

    private final boolean cvc_pattern(int i) {
        if (i < 2 || !checkCons(i) || checkCons(i - 1) || !checkCons(i - 2)) return false;
        {
            int ch = this.ch[i];
            if (ch == 'w' || ch == 'x' || ch == 'y') return false;
        }
        return true;
    }

    private final int m() {
        int n = 0;
        int i = 0;
        while (true) {
            if (i > j) return n;
            if (!checkCons(i)) break;
            i++;
        }
        i++;
        while (true) {
            while (true) {
                if (i > j) return n;
                if (checkCons(i)) break;
                i++;
            }
            i++;
            n++;
            while (true) {
                if (i > j) return n;
                if (!checkCons(i)) break;
                i++;
            }
            i++;
        }
    }


    private final boolean doublec(int j) {
        if (j < 1) return false;
        if (ch[j] != ch[j - 1]) return false;
        return checkCons(j);
    }


    private final boolean checkEnd(String s) {
        int l = s.length();
        int o = i - l + 1;
        if (o < 0) return false;
        for (int i = 0; i < l; i++) if (ch[o + i] != s.charAt(i)) return false;
        j = i - l;
        return true;
    }

    private final void setChar(String s) {
        int l = s.length();
        int o = j + 1;
        for (int i = 0; i < l; i++) ch[o + i] = s.charAt(i);
        i = j + l;
    }

    private final void r(String s) {
        if (m() > 0) setChar(s);
    }

    private final void stem_step1() {
        if (ch[i] == 's') {
            if (checkEnd("sses")) i -= 2;
            else if (checkEnd("ies")) setChar("i");
            else if (ch[i - 1] != 's') i--;
        }
        if (checkEnd("eed")) {
            if (m() > 0) i--;
        } else if ((checkEnd("ed") || checkEnd("ing")) && stemVowel()) {
            i = j;
            if (checkEnd("at")) setChar("ate");
            else if (checkEnd("bl")) setChar("ble");
            else if (checkEnd("iz")) setChar("ize");
            else if (doublec(i)) {
                i--;
                {
                    int ch = this.ch[i];
                    if (ch == 'l' || ch == 's' || ch == 'z') i++;
                }
            } else if (m() == 1 && cvc_pattern(i)) setChar("e");
        }
    }

    private final void stem_step2() {
        if (checkEnd("y") && stemVowel()) ch[i] = ' ';
    }

    private final void stem_step3() {
        if (i == 0) return;
        switch (ch[i - 1]) {
            case 'a':
                if (checkEnd("ational")) {
                    r("ate");
                    break;
                }
                if (checkEnd("tional")) {
                    r("tion");
                    break;
                }
                break;
            case 'c':
                if (checkEnd("enci")) {
                    r("ence");
                    break;
                }
                if (checkEnd("anci")) {
                    r("ance");
                    break;
                }
                break;
            case 'e':
                if (checkEnd("izer")) {
                    r("ize");
                    break;
                }
                break;
            case 'l':
                if (checkEnd("bli")) {
                    r("ble");
                    break;
                }
                if (checkEnd("alli")) {
                    r("al");
                    break;
                }
                if (checkEnd("entli")) {
                    r("ent");
                    break;
                }
                if (checkEnd("eli")) {
                    r("e");
                    break;
                }
                if (checkEnd("ousli")) {
                    r("ous");
                    break;
                }
                break;
            case 'o':
                if (checkEnd("ization")) {
                    r("ize");
                    break;
                }
                if (checkEnd("ation")) {
                    r("ate");
                    break;
                }
                if (checkEnd("ator")) {
                    r("ate");
                    break;
                }
                break;
            case 's':
                if (checkEnd("alism")) {
                    r("al");
                    break;
                }
                if (checkEnd("iveness")) {
                    r("ive");
                    break;
                }
                if (checkEnd("fulness")) {
                    r("ful");
                    break;
                }
                if (checkEnd("ousness")) {
                    r("ous");
                    break;
                }
                break;
            case 't':
                if (checkEnd("aliti")) {
                    r("al");
                    break;
                }
                if (checkEnd("iviti")) {
                    r("ive");
                    break;
                }
                if (checkEnd("biliti")) {
                    r("ble");
                    break;
                }
                break;
            case 'g':
                if (checkEnd("logi")) {
                    r("log");
                    break;
                }
        }
    }


    private final void stem_step4() {
        switch (ch[i]) {
            case 'e':
                if (checkEnd("icate")) {
                    r("ic");
                    break;
                }
                if (checkEnd("ative")) {
                    r("");
                    break;
                }
                if (checkEnd("alize")) {
                    r("al");
                    break;
                }
                break;
            case 'i':
                if (checkEnd("iciti")) {
                    r("ic");
                    break;
                }
                break;
            case 'l':
                if (checkEnd("ical")) {
                    r("ic");
                    break;
                }
                if (checkEnd("ful")) {
                    r("");
                    break;
                }
                break;
            case 's':
                if (checkEnd("ness")) {
                    r("");
                    break;
                }
                break;
        }
    }


    private final void stem_step5() {
        if (i == 0) return; /* for Bug 1 */
        switch (ch[i - 1]) {
            case 'a':
                if (checkEnd("al")) break;
                return;
            case 'c':
                if (checkEnd("ance")) break;
                if (checkEnd("ence")) break;
                return;
            case 'e':
                if (checkEnd("er")) break;
                return;
            case 'i':
                if (checkEnd("ic")) break;
                return;
            case 'l':
                if (checkEnd("able")) break;
                if (checkEnd("ible")) break;
                return;
            case 'n':
                if (checkEnd("ant")) break;
                if (checkEnd("ement")) break;
                if (checkEnd("ment")) break;
                if (checkEnd("ent")) break;
                return;
            case 'o':
                if (checkEnd("ion") && j >= 0 && (ch[j] == 's' || ch[j] == 't')) break;
                if (checkEnd("ou")) break;
                return;
            case 's':
                if (checkEnd("ism")) break;
                return;
            case 't':
                if (checkEnd("ate")) break;
                if (checkEnd("iti")) break;
                return;
            case 'u':
                if (checkEnd("ous")) break;
                return;
            case 'v':
                if (checkEnd("ive")) break;
                return;
            case 'z':
                if (checkEnd("ize")) break;
                return;
            default:
                return;
        }
        if (m() > 1) i = j;
    }

    private final void stem_step6() {
        j = i;
        if (ch[i] == 'e') {
            int a = m();
            if (a > 1 || a == 1 && !cvc_pattern(i - 1)) i--;
        }
        if (ch[i] == 'l' && doublec(i) && m() > 1) i--;
    }

    public void stem() {
        i = z - 1;
        if (i > 1) {
            stem_step1();
            stem_step2();
            stem_step3();
            stem_step4();
            stem_step5();
            stem_step6();
        }
        last = i + 1;
        z = 0;
    }

    public String toString() {
        return new String(ch, 0, last);
    }

    public static void main(String[] args) {
        char[] word = new char[501];
        PorterStemmer stemmer = new PorterStemmer();
        try {
            FileInputStream inFile = new FileInputStream("InfoRetrievalGroup9/src/main/resources/sample file.txt");

            try {
                while (true) {
                    int temp_ch = inFile.read();
                    if (Character.isLetter((char) temp_ch)) {
                        int j = 0;
                        while (true) {
                            temp_ch = Character.toLowerCase((char) temp_ch);
                            word[j] = (char) temp_ch;
                            if (j < 500) j++;
                            temp_ch = inFile.read();
                            if (!Character.isLetter((char) temp_ch)) {

                                for (int c = 0; c < j; c++) stemmer.addChar(word[c]);

                                stemmer.stem();
                                {
                                    String u;
                                    u = stemmer.toString();
                                    System.out.print(u);
                                }
                                break;
                            }
                        }
                    }
                    if (temp_ch < 0) break;
                    System.out.print((char) temp_ch);
                }
            } catch (IOException e) {
                System.err.println("File Reading Error");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Invalid File");

        }
    }
}