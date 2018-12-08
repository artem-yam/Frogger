package frogger.model;

public class ModelChangeData {
    private ModelChangeEvents event;
    private GameObject object;
    private int[] extraValues;

    ModelChangeData(ModelChangeEvents event, GameObject object, int... extraValues) {
        super();

        this.event = event;
        this.object = object;
        this.extraValues = extraValues;
    }

    public ModelChangeEvents getEvent() {
        return event;
    }

    public GameObject getObject() {
        return object;
    }

    public int[] getExtraValues() {
        return extraValues;
    }

}
