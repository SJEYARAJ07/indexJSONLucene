package org.jey.luceneindexcreator.Index;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public final class StringNoIndexedField extends Field {
	   public static final FieldType TYPE_NOT_INDEXED = new FieldType();
	   static {
	      TYPE_NOT_INDEXED.setIndexOptions(IndexOptions.NONE);
	      TYPE_NOT_INDEXED.setStored(true);
	      TYPE_NOT_INDEXED.setTokenized(false);
	      TYPE_NOT_INDEXED.freeze();
	  }

	  public StringNoIndexedField(String name, String value) {
	    super(name, value, TYPE_NOT_INDEXED);
	  }
	}