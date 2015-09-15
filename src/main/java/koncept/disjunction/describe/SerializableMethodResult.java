package koncept.disjunction.describe;

import java.io.NotSerializableException;
import java.io.Serializable;

public class SerializableMethodResult implements MethodResult, Serializable {

	final Serializable value;
	final boolean isException;
	
	public SerializableMethodResult(Object value, boolean isException) throws NotSerializableException {
		if (value == null || value instanceof Serializable)
			this.value = (Serializable)value;
		else throw new NotSerializableException();
		this.isException = isException;
	}
	
	@Override
	public Object value() {
		return value;
	}

	@Override
	public boolean isException() {
		return isException;
	}

}
