package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.observer.Observer;

public interface View extends Observer {

    public void setDisableAddDelivery(boolean bool);

}
