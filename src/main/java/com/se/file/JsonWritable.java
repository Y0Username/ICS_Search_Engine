package com.se.file;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import org.apache.hadoop.io.Text;

import com.google.gson.Gson;

public class JsonWritable<T> extends Text {

	private byte[] clsBytes;
	private int clsLength;

	public JsonWritable(Class<T> cls, T object) {
		super(new Gson().toJson(object));
		try {
			ByteBuffer e = encode(cls.getCanonicalName(), true);
			this.clsBytes = e.array();
			this.clsLength = e.limit();
		} catch (CharacterCodingException arg2) {
			throw new RuntimeException("Should not have happened "
					+ arg2.toString());
		}

	}

	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		out.write(clsBytes, 0, clsLength);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		
	}

}
