package de.eva.md5.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Klasse zur Darstellung von MD5 Hashes. 
 * Kann genutzt werden, um einen Kollisionstest durchzufuehren.
 * 
 * @author Yves Annanias, Dan Haeberlein
 *
 */
public class Md5Hash {

	/**
	 * Der gegebene Hash wird in einem Byte-Array (mit 16 Bytes) gespeichert
	 * Der Hash repraesentiert das Ergebnis einer MD5-Operation
	 */
	private byte[] byteContent = new byte[]{};

	/**
	 * Zur Erstellung eines Hashes aus zwei long Literalen. 
	 * 
	 * @param firstPart ersten 8 Byte des Hashes als Long
	 * @param secondPart zweiten 8 Byte des Hashes als Long
	 */
	public Md5Hash(long firstPart, long secondPart) {
		super();
		/* Die Klasse ByteBuffer zur Konvertierung von zwei Long-Werten zu einem 16 Byte Array 
		 * 	und die verwendeten Methoden sind hier dokumentiert: 
		 * 	http://download.oracle.com/javase/7/docs/api/java/nio/ByteBuffer.html */
		byteContent = ByteBuffer.allocate(16)
				.putLong(firstPart)
				.putLong(secondPart)
				.array();
	}
	
	/**
	 * Zur Erstellung eines Hashes aus einem Byte array
	 * 
	 * @param byteContent Byte array mit 16 Einträgen
	 */
	public Md5Hash(byte[] byteContent) {
		this.byteContent = byteContent;
	}
	
	/** 
	 * Fuehrt den Kollisionstest durch.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if(obj instanceof Md5Hash){
			Md5Hash other = (Md5Hash) obj;
			isEqual = Arrays.equals(getByteContent(), other.getByteContent());
		}
		return isEqual;
	}

	public byte[] getByteContent() {
		return byteContent;
	}
	
}
