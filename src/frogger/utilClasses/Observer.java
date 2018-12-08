package frogger.utilClasses;

import frogger.model.ModelChangeData;

public interface Observer {

    void handleEvent(ModelChangeData changeData);

}
