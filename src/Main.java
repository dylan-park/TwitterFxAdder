import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.addFlavorListener(e -> {
            try {
                String input = clipboard.getData(DataFlavor.stringFlavor).toString();
                if (Pattern.compile("http(?:s)?:\\/\\/(?:www)?twitter\\.com\\/([a-zA-Z0-9_]+)\\/status\\/([a-zA-Z0-9_]+)(\\/.*)?").matcher(input).matches()) {
                    StringSelection selection = new StringSelection(input.substring(0, input.indexOf("//") + 2) + "fx" + input.substring(input.indexOf("twitter")));
                    clipboard.setContents(selection, selection);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // keep thread alive
        Object o = new Object();
        synchronized (o) {
            o.wait();
        }
    }
}