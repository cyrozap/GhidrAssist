package ghidrassist;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.Reader;
import java.io.IOException;

public class SecureParserDelegator extends ParserDelegator {
    @Override
    public void parse(Reader r, HTMLEditorKit.ParserCallback cb, boolean ignoreCharset) throws IOException {
        super.parse(r, new FilteringCallback(cb), ignoreCharset);
    }

    private static class FilteringCallback extends HTMLEditorKit.ParserCallback {
        private final HTMLEditorKit.ParserCallback original;

        public FilteringCallback(HTMLEditorKit.ParserCallback original) {
            this.original = original;
        }

        @Override
        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            if (t == HTML.Tag.IMG) {
                handleText("[Image removed by GhidrAssist]".toCharArray(), pos);
            } else {
                original.handleStartTag(t, a, pos);
            }
        }

        @Override
        public void handleEndTag(HTML.Tag t, int pos) {
            if (t != HTML.Tag.IMG) {
                original.handleEndTag(t, pos);
            }
        }

        @Override
        public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            if (t == HTML.Tag.IMG) {
                handleText("[Image removed by GhidrAssist]".toCharArray(), pos);
            } else {
                original.handleSimpleTag(t, a, pos);
            }
        }

        @Override
        public void handleText(char[] data, int pos) {
            original.handleText(data, pos);
        }
    }
}
