package ru.itis.semesterwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.semesterwork.client.handler.*;
import ru.itis.semesterwork.client.listener.*;
import ru.itis.semesterwork.client.panel.NameInputPanel;
import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.panel.game.in.GamePanel;
import ru.itis.semesterwork.client.panel.game.after.GameResultPanel;
import ru.itis.semesterwork.client.panel.game.in.OpponentDisconnectedPanel;
import ru.itis.semesterwork.client.Client;
import ru.itis.semesterwork.common.protocol.Message;
import ru.itis.semesterwork.client.panel.FirstPanel;
import ru.itis.semesterwork.client.panel.game.before.GameReadyPanel;
import ru.itis.semesterwork.client.panel.MenuPanel;
import ru.itis.semesterwork.client.panel.game.before.OpponentWaitingPanel;
import ru.itis.semesterwork.utils.ImageSearch;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main2 extends JPanel {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8080;

    private static Socket socket;
    private static GameReadyPanel gameReadyPanel;
    private static List<Trap> traps;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        fillImageSearch();
        JFrame frame = getFrame(Constants.TITLE + "1");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);
        try {
            Font customFont = getCustomFont();

            frame.setFont(customFont);


            FirstPanel firstPanel = new FirstPanel(mainPanel, cardLayout, customFont);
            MenuPanel menuPanel = new MenuPanel(mainPanel, frame, cardLayout, customFont);
            NameInputPanel nameInputPanel = new NameInputPanel(customFont);
            OpponentWaitingPanel opponentWaitingPanel = new OpponentWaitingPanel(cardLayout, mainPanel, customFont);
            GameResultPanel wonPanel = new GameResultPanel(mainPanel, cardLayout, customFont, "WIN");
            GameResultPanel losePanel = new GameResultPanel(mainPanel, cardLayout, customFont, "LOSE");
            GameResultPanel drawPanel = new GameResultPanel(mainPanel, cardLayout, customFont, "DRAW");
            OpponentDisconnectedPanel disconnectedPanel = new OpponentDisconnectedPanel(mainPanel, cardLayout, customFont);
            traps = loadTrapsFromJson(Main.class.getClassLoader().getResource("map/map1.json").toURI());



            mainPanel.add(firstPanel, Constants.FIRST_PANEL);
            mainPanel.add(menuPanel, Constants.MENU_PANEL);
            mainPanel.add(nameInputPanel, Constants.NAME_INPUT_PANEL);
            mainPanel.add(opponentWaitingPanel, Constants.OPPONENT_WAITING);
            mainPanel.add(wonPanel, Constants.WON_PANEL);
            mainPanel.add(losePanel, Constants.LOST_PANEL);
            mainPanel.add(drawPanel, Constants.DRAW_PANEL);
            mainPanel.add(disconnectedPanel, Constants.OPPONENT_DISCONNECTED_PANEL);

            configStartButton(nameInputPanel, frame, mainPanel, cardLayout, customFont);

            cardLayout.show(mainPanel, Constants.FIRST_PANEL);

            frame.add(mainPanel);


            frame.setVisible(true);


        } catch (IOException | FontFormatException | NullPointerException | URISyntaxException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            frame.dispose();
            throw new RuntimeException(e);
        }


    }
    public static List<Trap> loadTrapsFromJson(URI filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Trap.class));
    }

    private static void fillImageSearch() {
        List<Image> images = new ArrayList<>();
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("skin/character.png").getPath()).getImage());
        ImageSearch.putImage(Constants.PLAYER_IMAGE, images);

        images = new ArrayList<>();
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/circle_enemy1.png").getPath()).getImage());
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/circle_enemy3.png").getPath()).getImage());
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/circle_enemy2.png").getPath()).getImage());
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/circle_enemy3.png").getPath()).getImage());

        ImageSearch.putImage(Constants.CIRCLE_ENEMY_IMAGE, images);

        images = new ArrayList<>();
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/rect_enemy.png").getPath()).getImage());
        ImageSearch.putImage(Constants.RECT_ENEMY_IMAGE, images);

        images = new ArrayList<>();
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("enemy/square_enemy.png").getPath()).getImage());
        ImageSearch.putImage(Constants.SQUARE_ENEMY_IMAGE, images);

        images = new ArrayList<>();
        images.add(new ImageIcon(Main.class.getClassLoader().getResource("dead.png").getPath()).getImage());
        ImageSearch.putImage(Constants.DEAD_IMAGE, images);

    }

    private static void configStartButton(NameInputPanel nameInputPanel, JFrame frame, JPanel mainPanel, CardLayout cardLayout, Font customFont) {
        nameInputPanel.getButton().addActionListener(e -> {
            try {
                socket = Client.getSocket(SERVER_HOST, SERVER_PORT);
                String name = nameInputPanel.getTextField().getText();
                name = (name == null || name.isEmpty()) ? "Anonymous": name;
                Message message = new Message(("name:" + name).getBytes(), Message.CONNECT);
                Message.sendMessage(socket, message);
                gameReadyPanel = new GameReadyPanel(socket, cardLayout, mainPanel, customFont);
                gamePanel = new GamePanel(mainPanel, socket, cardLayout, traps, traps, customFont);
                List<ClientEventListener> clientEventListeners = getClientListeners(gameReadyPanel,
                        gamePanel, gamePanel, gamePanel, gamePanel);
                new Thread(() -> handleListeners(clientEventListeners)).start();

                mainPanel.add(gameReadyPanel, Constants.READY_PANEL);
                mainPanel.add(gamePanel, Constants.GAME_PANEL);
                cardLayout.show(mainPanel, Constants.OPPONENT_WAITING);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
                frame.dispose();
            }
        });
    }

    public static List<ClientEventListener> getClientListeners(GameReadyHandler ready,
                                                               GameStartHandler start,
                                                               PlayerStateHandler player,
                                                               GameResultHandler result,
                                                               OpponentDisconnectHandler disconnected) {
        List<ClientEventListener> list = new ArrayList<>();
        list.add(new GameReadyClientEventListener(socket, ready));
        list.add(new GameStartEventListener(socket, start));
        list.add(new PlayerStateClientEventListener(socket, player));
        list.add(new GameResultEventListener(socket, result));
        list.add(new OpponentDisconnectEventListener(socket, disconnected));
        return list;
    }

    private static void handleListeners(List<ClientEventListener> listeners) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            System.out.println("Listening messages from server");
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Message message = Message.getMessage(buffer);

                for (ClientEventListener listener : listeners) {
                    if (listener.getType() == message.getMessageType()) {
                        listener.handle(message);
                        break;
                    }
                }
            }
        } catch (IOException | WrongDataForThisMessageTypeException e) {
            throw new RuntimeException(e);
        }
    }

    public static Font getCustomFont() throws IOException, FontFormatException, URISyntaxException {
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(Main.class.getClassLoader().getResource(Constants.FONT_NAME).toURI())).deriveFont(12f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        ge.registerFont(customFont);
        return customFont;
    }

    private static JFrame getFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setSize(1300, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}