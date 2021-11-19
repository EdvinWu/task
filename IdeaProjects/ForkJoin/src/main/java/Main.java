import java.io.*;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {

    private final static String URL = "https://sendel.ru/";  //"https://lenta.ru/";
    private static final String PATH = "src/main/resources/map.txt";

    public static void main(String[] args) throws IOException {

        Node<String> node = new Node<>(URL);
        new ForkJoinPool().invoke(new RecursiveAction(node, node));
        print(node);

    }

    private static void print(Node<String> node) throws IOException {

        if (node == null) {
            return;
        }

        int size = node.getNodeSize();
        String tab = String.join("", Collections.nCopies(size, "\t"));
        String link = tab + node.getUrl() + "\n";
        writeToFile(link);
        node.getChildren().forEach(each -> {
            try {
                print(each);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    private static void writeToFile(String data) throws IOException {
        OutputStream outputStream = new FileOutputStream((Main.PATH), true);
        outputStream.write(data.getBytes(), 0, data.length());
    }
}