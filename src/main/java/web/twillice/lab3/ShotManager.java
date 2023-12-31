package web.twillice.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import web.twillice.lab3.model.Shot;
import web.twillice.lab3.repository.ShotRepository;
import web.twillice.lab3.util.MessageManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named @SessionScoped
public class ShotManager implements Serializable {
    @Inject
    private ShotRepository repository;
    @Getter
    private final Shot shot = new Shot();
    @Getter
    private final List<Shot> shots = new ArrayList<>();
    @Getter
    private final List<Double> availableX = List.of(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);
    @Getter @Setter
    private Double selectedR = 2.0;

    @PostConstruct
    public void init() {
        shot.setX(-2.0);
        shot.setR(2.0);
    }

    public void shoot() {
        shot.setR(selectedR);
        saveShot(shot);
    }

    public void shootPlot() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String error = requestParameters.get("error");
        if (error != null) {
            MessageManager.error(error);
            return;
        }
        Shot shot = new Shot();
        try {
            shot.setX(Double.parseDouble(requestParameters.get("x")));
            shot.setY(Double.parseDouble(requestParameters.get("y")));
            shot.setR(Double.parseDouble(requestParameters.get("r")));
        } catch (NumberFormatException | NullPointerException e) {
            MessageManager.error("Invalid values.");
            return;
        }
        saveShot(shot);
    }

    private void saveShot(Shot shot) {
        Shot newShot = repository.create(shot);
        shots.add(newShot);
    }
}
