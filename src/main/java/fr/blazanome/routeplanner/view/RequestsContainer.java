package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.observer.Observable;
import fr.blazanome.routeplanner.observer.Observer;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RequestsContainer extends FlowPane implements Observer {

    public RequestsContainer() {
        System.out.println("constructed");
    }
    @Override
    public void update(Observable observable, Object message) {
        System.out.println("fzefe");
        if(message instanceof IntersectionSelectedState intersectionState) {
            for(var request: intersectionState.getSelectedIntersection().getRequests()) {
                var container = new VBox();
                container.getChildren().add(new Text("Super texte"));
                this.getChildren().add(container);
            }
        }
    }
}
