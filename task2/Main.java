import java.io.*;
import java.util.*;

/**
 * КТ_4, задача 2. k самых частых чисел (при равной частоте — меньшее число раньше).
 * Идея: сортируем числа, считаем длины серий одинаковых значений, затем сортируем пары (частота, значение).
 * Пара упакована в long: старшая часть = (n - частота), младшая = значение + 10^9, поэтому обычная сортировка
 * по возрастанию даёт «частота по убыванию, значение по возрастанию». Время O(n log n).
 */
public class Main {
    public static void main(String[] args) throws IOException {
        final long OFF = 1_000_000_000L;
        int n = nextInt(), k = nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = nextInt();
        Arrays.sort(a);

        long[] keys = new long[n];
        int d = 0;
        for (int i = 0; i < n; ) {
            int j = i;
            while (j < n && a[j] == a[i]) j++;
            int cnt = j - i;
            keys[d++] = ((long) (n - cnt) << 32) | ((long) a[i] + OFF);
            i = j;
        }
        Arrays.sort(keys, 0, d);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(' ');
            sb.append((keys[i] & 0xFFFFFFFFL) - OFF);
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
