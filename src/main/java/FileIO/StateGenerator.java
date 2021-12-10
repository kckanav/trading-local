package FileIO;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class StateGenerator {

    private RandomAccessFile memoryMappedFile;
    private MappedByteBuffer out;

    public StateGenerator(String file) {
        try {
            memoryMappedFile = new RandomAccessFile(file, "rw");
            out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 1000000);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public int write(String s) {
        int start = out.position();
        s += "\n";
        for (byte b: s.getBytes()) {
            out.put(b);
        }
        return out.position() - start;
    }

    public String read(int start, int end) {
        StringBuffer f = new StringBuffer();
        for (int i = start; i < end && i < out.position(); i++) {
            f.append((char)out.get(i));
        }
        return f.toString();
    }

    public String read() {
        return read(0, out.position());
    }

    public static void genTextFile(String file) {
        try {
            FileWriter wr = new FileWriter(new File("logMeanReversion" + File.separator +  file + ".txt"));
            RandomAccessFile temp  = new RandomAccessFile(file, "r");
            for (int i = 0; i < temp.length(); i++) {
                String str = temp.readLine();
                if (str.isBlank()) {
                    break;
                }
                wr.write(str + "\n");
            }
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String file) {
        try {
            StringBuffer s = new StringBuffer();
            RandomAccessFile temp  = new RandomAccessFile(file, "r");
            for (int i = 0; i < temp.length(); i++) {
                String str = temp.readLine();
                if (str.isBlank()) {
                    break;
                }
                s.append(str + "\n");
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        StateGenerator g = new StateGenerator("file.txt");
        System.out.println(g.write("Hi Baby! \nWhat is up"));
        System.out.println(g.read(0, 25));
        g.write("\nCool");
    }
}
