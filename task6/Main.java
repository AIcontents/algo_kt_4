import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 6. Медиана в каждом окне длины k.
 * Идея: дерево Фенвика по сжатым значениям хранит, сколько раз каждое значение есть в текущем окне.
 * Окно сдвигается: добавили новый элемент, убрали вышедший. Нужный по порядку элемент (k-й по возрастанию)
 * ищем спуском по дереву Фенвика за O(log n). Для чётного k берём два средних элемента.
 * Время O(n log n), память O(n).
 */
public class Main {
    static int m;
    static int[] tree;

    static void add(int pos, int delta) {
        for (; pos <= m; pos += pos & -pos) tree[pos] += delta;
    }

    /** Номер (1..m) сжатого значения, которое стоит на t-м месте по возрастанию среди элементов окна. */
    static int kth(int t, int topBit) {
        int pos = 0;
        for (int pw = topBit; pw > 0; pw >>= 1) {
            int np = pos + pw;
            if (np <= m && tree[np] < t) { pos = np; t -= tree[np]; }
        }
        return pos + 1;
    }

    /** Печатает twice/2 с ровно одним знаком после десятичной точки (twice — удвоенное значение). */
    static void appendHalf(StringBuilder sb, long twice) {
        if (twice < 0) { sb.append('-'); twice = -twice; }
        sb.append(twice / 2).append('.').append(twice % 2 == 0 ? '0' : '5');
    }

    public static void main(String[] args) throws IOException {
        int n = nextInt(), k = nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = nextInt();

        int[] sorted = a.clone();
        Arrays.sort(sorted);
        int[] vals = new int[n];                           // различные значения по возрастанию
        m = 0;
        for (int i = 0; i < n; i++) if (i == 0 || sorted[i] != sorted[i - 1]) vals[m++] = sorted[i];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[i] = Arrays.binarySearch(vals, 0, m, a[i]) + 1;

        tree = new int[m + 1];
        int topBit = Integer.highestOneBit(m);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            add(rank[i], 1);
            if (i >= k) add(rank[i - k], -1);
            if (i >= k - 1) {
                long twice;
                if ((k & 1) == 1) {
                    twice = 2L * vals[kth((k + 1) / 2, topBit) - 1];
                } else {
                    twice = (long) vals[kth(k / 2, topBit) - 1] + vals[kth(k / 2 + 1, topBit) - 1];
                }
                if (sb.length() > 0) sb.append(' ');
                appendHalf(sb, twice);
            }
        }
        System.out.println(sb);
    }

    // ===================== быстрый ввод =====================
    private static final InputStream IN = System.in;
    private static final byte[] BUF = new byte[1 << 16];
    private static int bufLen = 0, bufPtr = 0;
    private static byte[] tok = new byte[64];   // последний прочитанный токен
    private static int tokLen = 0;

    private static int readByte() throws IOException {
        if (bufPtr == bufLen) {
            bufLen = IN.read(BUF, 0, BUF.length);
            bufPtr = 0;
            if (bufLen <= 0) { bufLen = 0; return -1; }
        }
        return BUF[bufPtr++] & 0xFF;
    }

    private static long nextLong() throws IOException {
        int c = readByte();
        while (c != '-' && (c < '0' || c > '9')) {
            if (c == -1) throw new EOFException("неожиданный конец ввода");
            c = readByte();
        }
        boolean neg = false;
        if (c == '-') { neg = true; c = readByte(); }
        long r = 0;
        while (c >= '0' && c <= '9') { r = r * 10 + (c - '0'); c = readByte(); }
        return neg ? -r : r;
    }

    private static int nextInt() throws IOException { return (int) nextLong(); }

    /** Читает токен (последовательность непробельных символов) в tok[0..tokLen). false — конец ввода. */
    private static boolean readToken() throws IOException {
        int c = readByte();
        while (c != -1 && c <= ' ') c = readByte();
        if (c == -1) return false;
        tokLen = 0;
        while (c > ' ') {
            if (tokLen == tok.length) tok = Arrays.copyOf(tok, tokLen * 2);
            tok[tokLen++] = (byte) c;
            c = readByte();
        }
        return true;
    }

    private static String next() throws IOException {
        return readToken() ? new String(tok, 0, tokLen, java.nio.charset.StandardCharsets.ISO_8859_1) : null;
    }
}
