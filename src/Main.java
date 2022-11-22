import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.regex.Pattern;

public class Main {
    static int retryCount = 0;

    public static void main(String[] args) throws InterruptedException {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.addFlavorListener(e -> {
            try {
                processClipboard(clipboard);
            } catch (IllegalStateException ex) {
                if (retryCount >= 5) {
                    try {
                        retryCount++;
                        Thread.sleep(50);
                        processClipboard(clipboard);
                    } catch (IOException | UnsupportedFlavorException | InterruptedException exc) {
                        throw new RuntimeException(exc);
                    }
                } else {
                    retryCount = 0;
                    System.out.println("Clipboard Inaccessible");
                    throw new RuntimeException(ex);
                }
            } catch (IOException | UnsupportedFlavorException ex) {
                throw new RuntimeException(ex);
            }
        });

        // keep thread alive
        Object o = new Object();
        synchronized (o) {
            o.wait();
        }
    }

    public static void processClipboard(Clipboard clipboard) throws IOException, UnsupportedFlavorException {
        String input = clipboard.getData(DataFlavor.stringFlavor).toString();
        if (Pattern.compile("http(?:s)?:\\/\\/(?:www)?twitter\\.com\\/([a-zA-Z0-9_]+)\\/status\\/([a-zA-Z0-9_]+)(\\/.*)?").matcher(input).matches()) {
            StringSelection selection = new StringSelection(input.substring(0, input.indexOf("//") + 2) + "fx" + input.substring(input.indexOf("twitter")));
            clipboard.setContents(selection, selection);
        }
    }
}