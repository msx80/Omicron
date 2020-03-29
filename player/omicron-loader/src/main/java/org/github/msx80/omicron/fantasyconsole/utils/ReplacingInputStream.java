package org.github.msx80.omicron.fantasyconsole.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Simple FilterInputStream that can replace occurrances of bytes with something else.
 */
public class ReplacingInputStream extends FilterInputStream {

    // while matching, this is where the bytes go.
    int[] buf=null;
    int matchedIndex=0;
    int unbufferIndex=0;
    int replacedIndex=0;

    private final byte[] pattern;
    private final byte[] replacement;
    private State state=State.NOT_MATCHED;

    // simple state machine for keeping track of what we are doing
    private enum State {
        NOT_MATCHED,
        MATCHING,
        REPLACING,
        UNBUFFER
    }

    /**
     * @param is input
     * @return nested replacing stream that replaces \n\r (DOS) and \r (MAC) line endings with UNIX ones "\n".
     */
    public static InputStream newLineNormalizingInputStream(InputStream is) {
        return new ReplacingInputStream(new ReplacingInputStream(is, "\n\r", "\n"), "\r", "\n");
    }

    /**
     * Replace occurances of pattern in the input. Note: input is assumed to be UTF-8 encoded. If not the case use byte[] based pattern and replacement.
     * @param in input
     * @param pattern pattern to replace.
     * @param replacement the replacement or null
     */
    public ReplacingInputStream(InputStream in, String pattern, String replacement) {
        this(in,pattern.getBytes(StandardCharsets.UTF_8), replacement==null ? null : replacement.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Replace occurances of pattern in the input.
     * @param in input
     * @param pattern pattern to replace
     * @param replacement the replacement or null
     */
    public ReplacingInputStream(InputStream in, byte[] pattern, byte[] replacement) {
        super(in);
        this.pattern = pattern;
        this.replacement = replacement;
        // we will never match more than the pattern length
        buf = new int[pattern.length];
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // copy of parent logic; we need to call our own read() instead of super.read(), which delegates instead of calling our read
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
            }
        } catch (IOException ee) {
        }
        return i;

    }

    @Override
    public int read(byte[] b) throws IOException {
        // call our own read
        return read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        // use a simple state machine to figure out what we are doing
        int next;
        switch (state) {
        case NOT_MATCHED:
            // we are not currently matching, replacing, or unbuffering
            next=super.read();
            if(pattern[0] == next) {
                // clear whatever was there
                buf=new int[pattern.length]; // clear whatever was there
                // make sure we start at 0
                matchedIndex=0;

                buf[matchedIndex++]=next;
                if(pattern.length == 1) {
                    // edgecase when the pattern length is 1 we go straight to replacing
                    state=State.REPLACING;
                    // reset replace counter
                    replacedIndex=0;
                } else {
                    // pattern of length 1
                    state=State.MATCHING;
                }
                // recurse to continue matching
                return read();
            } else {
                return next;
            }
        case MATCHING:
            // the previous bytes matched part of the pattern
            next=super.read();
            if(pattern[matchedIndex]==next) {
                buf[matchedIndex++]=next;
                if(matchedIndex==pattern.length) {
                    // we've found a full match!
                    if(replacement==null || replacement.length==0) {
                        // the replacement is empty, go straight to NOT_MATCHED
                        state=State.NOT_MATCHED;
                        matchedIndex=0;
                    } else {
                        // start replacing
                        state=State.REPLACING;
                        replacedIndex=0;
                    }
                }
            } else {
                // mismatch -> unbuffer
                buf[matchedIndex++]=next;
                state=State.UNBUFFER;
                unbufferIndex=0;
            }
            return read();
        case REPLACING:
            // we've fully matched the pattern and are returning bytes from the replacement
            next=replacement[replacedIndex++];
            if(replacedIndex==replacement.length) {
                state=State.NOT_MATCHED;
                replacedIndex=0;
            }
            return next;
        case UNBUFFER:
            // we partially matched the pattern before encountering a non matching byte
            // we need to serve up the buffered bytes before we go back to NOT_MATCHED
            next=buf[unbufferIndex++];
            if(unbufferIndex==matchedIndex) {
                state=State.NOT_MATCHED;
                matchedIndex=0;
            }
            return next;

        default:
            throw new IllegalStateException("no such state " + state);
        }
    }

    @Override
    public String toString() {
        return state.name() + " " + matchedIndex + " " + replacedIndex + " " + unbufferIndex;
    }

}
/*
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

public class ReplacingInputStream extends FilterInputStream {

    LinkedList<Integer> inQueue = new LinkedList<Integer>();
    LinkedList<Integer> outQueue = new LinkedList<Integer>();
    final byte[] search, replacement;

    public ReplacingInputStream(InputStream in,
                                   byte[] search,
                                   byte[] replacement) {
        super(in);
        this.search = search;
        this.replacement = replacement;
    }

    private boolean isMatchFound() {
        Iterator<Integer> inIter = inQueue.iterator();
        for (int i = 0; i < search.length; i++)
            if (!inIter.hasNext() || search[i] != inIter.next())
                return false;
        return true;
    }

    private void readAhead() throws IOException {
        // Work up some look-ahead.
        while (inQueue.size() < search.length) {
            int next = super.read();
            inQueue.offer(next);
            if (next == -1)
                break;
        }
    }

    @Override
    public int read() throws IOException {    
        // Next byte already determined.
        if (outQueue.isEmpty()) {
            readAhead();

            if (isMatchFound()) {
                for (int i = 0; i < search.length; i++)
                    inQueue.remove();

                for (byte b : replacement)
                    outQueue.offer((int) b);
            } else
                outQueue.add(inQueue.remove());
        }

        return outQueue.remove();
    }

    
    
    // TODO: Override the other read methods.
}*/