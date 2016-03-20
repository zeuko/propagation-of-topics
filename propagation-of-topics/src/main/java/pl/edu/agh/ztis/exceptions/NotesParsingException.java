package pl.edu.agh.ztis.exceptions;

import java.io.IOException;

public class NotesParsingException extends RuntimeException {

	private static final long serialVersionUID = -7067848857193215066L;

	public NotesParsingException(String string, IOException e) {
		super(string, e);
	}

}
