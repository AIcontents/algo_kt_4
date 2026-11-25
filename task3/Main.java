import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 3. Медиана потока чисел.
 * Идея: две кучи. В «нижней» (max-куча) лежит меньшая половина чисел, в «верхней» (min-куча) — большая.
 * Размеры отличаются не более чем на 1 (в нижней не меньше). Медиана — вершина нижней кучи (нечётное количество)
 * или среднее двух вершин (чётное). Каждое число обрабатывается за O(log n).
 * Значение печатается с одним знаком после точки: его удвоенное значение целое, поэтому обходимся без double.
 */
public class Main {
    /** Печатает twice/2 с ровно одним знаком после десятичной точки (twice — удвоенное значение). */
    static void appendHalf(StringBuilder sb, long twice) {
        if (twice < 0) { sb.append('-'); twice = -twice; }
        sb.append(twice / 2).append('.').append(twice % 2 == 0 ? '0' : '5');
    }

    public static void main(String[] args) throws IOException {
        int n = nextInt();
        PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> hi = new PriorityQueue<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int x = nextInt();
            if (lo.isEmpty() || x <= lo.peek()) lo.add(x); else hi.add(x);
            if (lo.size() > hi.size() + 1) hi.add(lo.poll());
            else if (hi.size() > lo.size()) lo.add(hi.poll());

            long twice = lo.size() > hi.size() ? 2L * lo.peek() : (long) lo.peek() + hi.peek();
            if (i > 0) sb.append(' ');
            appendHalf(sb, twice);
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
