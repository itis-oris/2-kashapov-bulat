package ru.itis.semesterwork.client.panel.game.in;

import lombok.Getter;
import ru.itis.semesterwork.client.panel.game.trap.TrapsPanel;
import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.client.panel.game.trap.CircleTrapPanel;
import ru.itis.semesterwork.client.panel.game.trap.RectTrapPanel;
import ru.itis.semesterwork.client.panel.game.trap.TrapPanel;
import ru.itis.semesterwork.utils.ImageSearch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerFieldPanel extends JPanel {
    private int x ;
    private int y;
    private int bounds;
    @Getter
    private TrapsPanel traps;

    public PlayerFieldPanel(List<Trap> traps, int x, int y, int bounds, JPanel player) {
        this.x = x;
        this.y = y;
        this.bounds = bounds;
        setLayout(null);

        this.traps = addTrapPanels(traps);

        setOpaque(false);

        add(this.traps);
        add(player);

    }

    private TrapsPanel addTrapPanels(List<Trap> traps) {
        List<TrapPanel> trapPanels = new ArrayList<>();
        for (Trap trap : traps) {
            List<Image> images = ImageSearch.getImages(trap.getType());
            TrapPanel trapPanel = switch (trap.getType()) {
                case "circle" -> new CircleTrapPanel(trap, images, x, y, bounds);
                case "rect" -> new RectTrapPanel(trap, images, x, y, bounds);
                default -> throw new RuntimeException();
            };
            trapPanels.add(trapPanel);

        }
        return new TrapsPanel(trapPanels, x, y, bounds);
    }
}
