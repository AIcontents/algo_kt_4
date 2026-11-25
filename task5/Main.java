import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 5. k-я наименьшая сумма A[i] + B[j] среди всех пар.
 * Идея: массивы отсортированы, поэтому суммы в строке i (A[i] + B[0..m-1]) уже упорядочены.
 * Держим min-кучу, в которой для каждой строки i лежит её текущая (наименьшая ещё не взятая) сумма.
 * k раз достаём минимум и двигаем указатель в его строке. Достаточно взять первые min(n, k) строк.
 * Время O(k log k), память O(k). Равные суммы разных пар считаются отдельно — естественно получается.
 * Элемент кучи упакован в long: (сумма + 2·10^9) в старших битах, номер строки — в младших 18 битах.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        final long OFF = 2_000_000_000L;
        final int BITS = 18, MASK = (1 << BITS) - 1;
        int n = nextInt(), m = nextInt(), k = nextInt();
        long[] a = new long[n], b = new long[m];
        for (int i = 0; i < n; i++) a[i] = nextLong();
        for (int j = 0; j < m; j++) b[j] = nextLong();

        int rows = Math.min(n, k);
        int[] pos = new int[rows];                         // pos[i] — текущий индекс в B для строки i
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int i = 0; i < rows; i++) pq.add(((a[i] + b[0] + OFF) << BITS) | i);

        long ans = 0;
        for (int t = 0; t < k; t++) {
            long top = pq.poll();
            int i = (int) (top & MASK);
            ans = (top >>> BITS) - OFF;
            if (++pos[i] < m) pq.add(((a[i] + b[pos[i]] + OFF) << BITS) | i);
        }
        System.out.println(ans);
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
