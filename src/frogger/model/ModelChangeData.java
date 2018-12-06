package frogger.model;

public class ModelChangeData {
	private ModelChangeEventEnum changeType = null;
	private ObjectTypeEnum objectType = null;
	private int[] affectedValues;

	public ModelChangeData(ModelChangeEventEnum changeType, ObjectTypeEnum objectType, int... affectedValues) {
		super();

		this.changeType = changeType;
		this.objectType = objectType;
		this.affectedValues = affectedValues;
	}

	public ModelChangeEventEnum getChangeType() {
		return changeType;
	}

	public void setChangeType(ModelChangeEventEnum changeType) {
		this.changeType = changeType;
	}

	public ObjectTypeEnum getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectTypeEnum objectType) {
		this.objectType = objectType;
	}

	public int[] getAffectedValues() {
		return affectedValues;
	}

	public void setAffectedValues(int[] affectedValues) {
		this.affectedValues = affectedValues;
	}

}
