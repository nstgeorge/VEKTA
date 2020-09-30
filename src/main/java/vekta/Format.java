package vekta;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Format {
	@SuppressWarnings("unchecked")
	public static <T> T read(InputStream input) throws IOException, ClassNotFoundException {
		try(ObjectInputStream stream = new ObjectInputStream(new GZIPInputStream(input))) {
			return (T)stream.readObject();
		}
	}

	public static void write(Object object, OutputStream output) throws IOException {
		try(ObjectOutputStream stream = new ObjectOutputStream(new GZIPOutputStream(output))) {
			stream.writeObject(object);
		}
	}
}
