package ru.itis.semesterwork.client.panel.game.in;

import ru.itis.semesterwork.client.panel.game.trap.TrapsPanel;
import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.Main;
import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.GameResultHandler;
import ru.itis.semesterwork.client.handler.GameStartHandler;
import ru.itis.semesterwork.client.handler.OpponentDisconnectHandler;
import ru.itis.semesterwork.client.handler.PlayerStateHandler;
import ru.itis.semesterwork.client.panel.game.trap.TrapPanel;
import ru.itis.semesterwork.common.protocol.Message;
import ru.itis.semesterwork.utils.ImageSearch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class GamePanel extends JPanel implements GameStartHandler, PlayerStateHandler, GameResultHandler, OpponentDisconnectHandler {
    private static final int PLAYER_FIELD_X = 100;
    private static final int PLAYER_FIELD_Y = 100;
    private static final int BOUNDS = 400;
    private static final int OPPONENT_FIELD_X = 800;
    private static final int OPPONENT_FIELD_Y = 100;
    private static final int TIME_X = 650;
    private static final int TIME_Y = 50;

    private CardLayout cardLayout;
    private TrapsPanel map;
    private TrapsPanel opponentMap;
    private Timer gameTimer;
    private Timer timer;
    private long startTime;
    private int remainTime = 30;
    private Font font;
    private Socket socket;
    private PlayerPanel playerPanel;
    private PlayerFieldPanel playerFieldPanel, opponentFieldPanel;
    private OpponentPanel opponentPanel;
    private JPanel mainPanel;

    private DeadMarkPanel deadMarkPanel;
    private DeadMarkPanel opponentDeadMarkPanel;
    private JLayeredPane jLayeredPane;

    private boolean playerIsAlive = true;
    private boolean opponentIsAlive = true;

    public GamePanel(JPanel mainPanel, Socket socket, CardLayout cardLayout, List<Trap> traps, List<Trap> opponentTraps, Font font) {
        this.cardLayout = cardLayout;
        this.socket = socket;
        this.mainPanel = mainPanel;
        this.font = font;
        setLayout(null);
        setBackground(Color.BLACK);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(0, 0, screenSize.width, screenSize.height);

        jLayeredPane = new JLayeredPane();
        jLayeredPane.setBounds(0, 0, screenSize.width, screenSize.height);

        JLabel you = new JLabel("You");
        you.setFont(font.deriveFont(24f));
        you.setForeground(Color.WHITE);
        you.setBounds(PLAYER_FIELD_X, PLAYER_FIELD_Y - 24, BOUNDS, BOUNDS);
        you.setVerticalAlignment(SwingConstants.TOP);

        JLabel opponent = new JLabel("Opponent");
        opponent.setFont(font.deriveFont(24f));
        opponent.setForeground(Color.WHITE);
        opponent.setBounds(OPPONENT_FIELD_X, OPPONENT_FIELD_Y - 24, BOUNDS, BOUNDS);
        opponent.setVerticalAlignment(SwingConstants.TOP);

        jLayeredPane.add(you, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.add(opponent, JLayeredPane.DEFAULT_LAYER);


        playerPanel = new PlayerPanel(ImageSearch.getImages(Constants.PLAYER_IMAGE).get(0),PLAYER_FIELD_X, PLAYER_FIELD_Y, BOUNDS);
        playerPanel.setBounds(0, 0, screenSize.width, screenSize.height);
        playerFieldPanel = new PlayerFieldPanel(traps, PLAYER_FIELD_X, PLAYER_FIELD_Y, BOUNDS, playerPanel);
        playerFieldPanel.setBounds(0, 0, screenSize.width, screenSize.height);
        jLayeredPane.add(playerFieldPanel, 1);

        deadMarkPanel = new DeadMarkPanel(ImageSearch.getImages(Constants.DEAD_IMAGE).get(0), PLAYER_FIELD_X, PLAYER_FIELD_Y, BOUNDS);

        map = playerFieldPanel.getTraps();

        opponentPanel = new OpponentPanel(ImageSearch.getImages(Constants.PLAYER_IMAGE).get(0),
                OPPONENT_FIELD_X, OPPONENT_FIELD_Y, BOUNDS);
        opponentPanel.setBounds(0, 0, screenSize.width, screenSize.height);
        opponentFieldPanel = new PlayerFieldPanel(opponentTraps, OPPONENT_FIELD_X, OPPONENT_FIELD_Y, BOUNDS, opponentPanel);
        opponentFieldPanel.setBounds(0, 0, screenSize.width, screenSize.height);

        opponentDeadMarkPanel = new DeadMarkPanel(ImageSearch.getImages(Constants.DEAD_IMAGE).get(0), OPPONENT_FIELD_X, OPPONENT_FIELD_Y, BOUNDS);

        jLayeredPane.add(opponentFieldPanel, 1);



        opponentMap = opponentFieldPanel.getTraps();
        setTimer(playerPanel, playerFieldPanel);

        add(jLayeredPane);


    }
    private void setTimer(PlayerPanel playerPanel, PlayerFieldPanel playerFieldPanel) {
        gameTimer = new Timer(8, e -> {
            playerPanel.actionPerformed(e);
            StringBuilder stringBuilder = new StringBuilder();
            startTime = startTime();
            playerIsAlive = map.moveTraps(jLayeredPane, startTime, playerFieldPanel, deadMarkPanel, playerIsAlive, playerPanel);
            opponentMap.moveTraps(jLayeredPane, startTime, opponentFieldPanel, opponentDeadMarkPanel, opponentIsAlive);
            stringBuilder.append("alive:").append(playerIsAlive ? "1" : "0").append(";");
            stringBuilder.append("x:").append(playerPanel.getPlayerX()).append(";");
            stringBuilder.append("y:").append(playerPanel.getPlayerY());
            Message message = new Message(stringBuilder.toString().getBytes(), Message.PLAYER_STATE);
            Message.sendMessage(socket, message);

        });
        timer = new Timer(1000, e -> {
            repaint();
            remainTime--;
            if (remainTime < 0) {
                timer.stop();
            }
        });
    }

    private long startTime() {
        if (startTime == 0) {
            startTime = System.nanoTime();
        }
        return startTime;
    }

    @Override
    public void onGameStart() {
        cardLayout.show(mainPanel, Constants.GAME_PANEL);
        playerPanel.requestFocusInWindow();
        startTime = startTime();
        gameTimer.start();
        timer.start();
    }

    @Override
    public void handle(Message message) throws WrongDataForThisMessageTypeException {
        Map<String, String> map = Message.toMap(message);
        if (map.get("x") == null || map.get("y") == null || map.get("alive") == null) {
            throw new WrongDataForThisMessageTypeException();
        }
        try {
            if (Integer.parseInt(map.get("alive")) == 1) {
                opponentPanel.updatePosition(Integer.parseInt(map.get("x")), Integer.parseInt(map.get("y")));
            } else {
                opponentIsAlive = false;
                jLayeredPane.add(opponentDeadMarkPanel, 0);
                opponentDeadMarkPanel.startAnimation();
            }
        } catch (NumberFormatException e) {
            throw new WrongDataForThisMessageTypeException();
        }

    }

    @Override
    public void onGameResult(Message message) throws WrongDataForThisMessageTypeException {
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
        Map<String, String> mapMessage = Message.toMap(message);
        if (!mapMessage.containsKey("win") || !(Objects.equals(mapMessage.get("win"), "-1") ||
                !Objects.equals(mapMessage.get("win"), "0") ||
                !Objects.equals(mapMessage.get("win"), "1"))) {
            throw new WrongDataForThisMessageTypeException();
        } else {
            String gameResult = switch (Integer.parseInt(mapMessage.get("win"))) {
                case -1 -> Constants.DRAW_PANEL;
                case 0 -> Constants.LOST_PANEL;
                case 1 -> Constants.WON_PANEL;
                default -> throw new WrongDataForThisMessageTypeException();
            };
            cardLayout.show(mainPanel, gameResult);
        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(PLAYER_FIELD_X, PLAYER_FIELD_Y, BOUNDS, BOUNDS);
        g.fillRect(OPPONENT_FIELD_X, OPPONENT_FIELD_Y, BOUNDS, BOUNDS);
        g.setFont(font.deriveFont(36f));
        g.drawString("0:" + (remainTime >= 10 ? "" : "0") + remainTime, TIME_X - 36, TIME_Y);
    }

    @Override
    public void onOpponentDisconnected() {
        cardLayout.show(mainPanel, Constants.OPPONENT_DISCONNECTED_PANEL);
    }
}
