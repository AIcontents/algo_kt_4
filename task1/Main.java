import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 1. Сумма значений на каждом уровне бинарного дерева.
 * Идея: обход в ширину. Очередь хранит вершины текущего и следующего уровней;
 * размер очереди в начале шага = количество вершин текущего уровня. Время и память O(n).
 * Суммы могут не помещаться в int — используем long.
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

        int[] queue = new int[n];
        int head = 0, tail = 0;
        queue[tail++] = 0;                         // корень — вершина 0
        StringBuilder sb = new StringBuilder();
        while (head < tail) {
            int size = tail - head;                // вершины одного уровня
            long sum = 0;
            for (int i = 0; i < size; i++) {
                int v = queue[head++];
                sum += val[v];
                if (left[v] >= 0) queue[tail++] = left[v];
                if (right[v] >= 0) queue[tail++] = right[v];
            }
            if (sb.length() > 0) sb.append(' ');
            sb.append(sum);
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
