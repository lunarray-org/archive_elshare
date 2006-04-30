package org.lunarray.lshare.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.lunarray.lshare.protocol.state.sharing.ShareSettings;

/**
 * A hash class for handling all hash related functions.
 * @author Pal Hargitai
 */
public class Hash implements Comparable<Hash> {
    /**
     * The hash if it is unset.
     */
    public final static byte[] UNSET = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00
    };

    /**
     * The hash represented here.
     */
    private byte[] hash;

    /**
     * Constructs a hash by a given byte array.
     * @param b The hash to represent.
     */
    public Hash(byte[] b) {
        hash = b;
    }

    /**
     * Constracts a hash by a given string.
     * @param s The string whose hash to represent.
     */
    public Hash(String s) {
        hash = hashName(s);
    }

    /**
     * Constructs a hash by a given file.
     * @param f The file whose hash to represent.
     */
    public Hash(File f) {
        hash = hash(f);
    }

    /**
     * Given a normal unset hash.
     * @return A representation of an unset hash.
     */
    public static Hash getUnset() {
        return new Hash(UNSET);
    }

    /**
     * The length of a hash.
     * @return The length of (any) hash.
     */
    public static int length() {
        return UNSET.length;
    }

    /**
     * Gets the byte representation of the hash.
     * @return The bytes that represent this hash.
     */
    public byte[] getBytes() {
        return hash;
    }

    /**
     * Gives a string representation of this hash.
     * @return The string representation of this hash.
     */
    public String toString() {
        return hashToString(hash);
    }

    /**
     * Checks wether the hash is an empty hash.
     * @return True if the given hash is an empty hash. False if not.
     */
    public boolean isEmpty() {
        return equals(new Hash(UNSET));
    }

    /**
     * Checks if both hashes are equal.
     * @param o The hash to compare to.
     * @return True if both hashes are equal, false if not.
     */
    public boolean equals(Hash o) {
        if (o.hash.length == hash.length) {
            for (int i = 0; i < hash.length; i++) {
                if (o.hash[i] != hash[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    public int compareTo(Hash o) {
        if (o.hash.length == hash.length) {
            for (int i = 0; i < hash.length; i++) {
                if (o.hash[i] != hash[i]) {
                    return hash[i] - o.hash[i];
                }
            }
            return 0;
        } else {
            return hash.length - o.hash.length;
        }
    }

    /**
     * Gets a hash of the given name.
     * @param name The name to hash.
     * @return The hash of the name.
     */
    private byte[] hashName(String name) {
        byte[] md5 = UNSET;
        try {
            byte[] nbyte = name.getBytes();
            MessageDigest md = MessageDigest
                    .getInstance(ShareSettings.HASH_ALGO);
            md.update(nbyte);
            md5 = md.digest();
        } catch (NoSuchAlgorithmException nse) {
            Controls.getLogger().fine("Hashing not supported!");
        }
        return md5;
    }

    /**
     * Gets the hash of a specified file.
     * @param f The file to get the hash of.
     * @return The hash of the file.
     */
    private byte[] hash(File f) {
        byte[] md5 = UNSET;
        try {
            FileInputStream fi = new FileInputStream(f);
            MessageDigest md = MessageDigest
                    .getInstance(ShareSettings.HASH_ALGO);
            byte[] data = new byte[1000];
            int done = 0;
            while (fi.available() > 0) {
                done = fi.read(data);
                md.update(data, 0, done);
            }
            md5 = md.digest();
        } catch (FileNotFoundException e) {
            Controls.getLogger().fine("File not found!");
        } catch (NoSuchAlgorithmException nse) {
            Controls.getLogger().fine("Hashing not supported!");
        } catch (IOException ie) {
            Controls.getLogger().fine("File error!");
        }
        return md5;
    }

    /**
     * Converts a given hash to a readable string.
     * @param dat The hash to convert.
     * @return The string representation of the hash.
     */
    private String hashToString(byte[] dat) {
        String ret = "";
        for (byte b : dat) {
            ret += quadBitToString(b >> 4) + quadBitToString(b);
        }
        return ret;
    }

    /**
     * Converts 4-bits to a certain string.
     * @param b The int of which to get the representation of.
     * @return The string representation of the first 4-bits of the int.
     */
    private String quadBitToString(int b) {
        switch (b & 0x0F) {
        case 0x0:
            return "0";
        case 0x1:
            return "1";
        case 0x2:
            return "2";
        case 0x3:
            return "3";
        case 0x4:
            return "4";
        case 0x5:
            return "5";
        case 0x6:
            return "6";
        case 0x7:
            return "7";
        case 0x8:
            return "8";
        case 0x9:
            return "9";
        case 0xA:
            return "A";
        case 0xB:
            return "B";
        case 0xC:
            return "C";
        case 0xD:
            return "D";
        case 0xE:
            return "E";
        case 0xF:
            return "F";
        default:
            return "";
        }
    }
}
