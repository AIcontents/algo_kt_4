import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 4. Максимальная сумма маршрута в бинарном дереве (маршрут не обязан проходить через корень).
 * Идея: для каждой вершины v считаем gain[v] — лучшую сумму пути, который начинается в v и идёт вниз.
 *   gain[v] = val[v] + max(0, gain[левого], gain[правого]).
 * Лучший маршрут, у которого v — самая верхняя вершина: val[v] + max(0, gain[l]) + max(0, gain[r]).
 * Ответ — максимум по всем v. Обход без рекурсии (глубина дерева может быть 200 000): порядок BFS, затем с конца.
 * Время O(n), суммы в long.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int n = nextInt();
        long[] val = new long[n];
        int[] left = new int[n], right = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = nextLong();
            left[i] = nextInt();
            right[i] = nextInt();
        }

        int[] order = new int[n];                  // порядок BFS: родитель всегда раньше детей
        int head = 0, tail = 0;
        order[tail++] = 0;
        while (head < tail) {
            int v = order[head++];
            if (left[v] >= 0) order[tail++] = left[v];
            if (right[v] >= 0) order[tail++] = right[v];
        }

        long[] gain = new long[n];
        long best = Long.MIN_VALUE;
        for (int i = tail - 1; i >= 0; i--) {      // сначала дети, потом родители
            int v = order[i];
            long lg = left[v] >= 0 ? Math.max(0, gain[left[v]]) : 0;
            long rg = right[v] >= 0 ? Math.max(0, gain[right[v]]) : 0;
            best = Math.max(best, val[v] + lg + rg);
            gain[v] = val[v] + Math.max(lg, rg);
        }
        System.out.println(best);
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
