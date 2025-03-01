import ui.JModernButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 600;
    public static JFrame MainWindow;
    public static JComponent RootComponent;
    public static HashMap<Card, Image> CardAssets = new HashMap<>();
    public static Image BackOfCardImage;

    public static void main(String[] args) {

        MainWindow = new JFrame();

        Game.init();

        initWindow(MainWindow);
        loadCardAssets();

        Game.resetGame();

        renderGame();

        MainWindow.setVisible(true);

    }

    public static void initWindow(JFrame frame) {

        MainWindow.setTitle("Poker");
        MainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        MainWindow.setResizable(false);
        MainWindow.setLayout(null);

        frame.getContentPane().setBackground(Color.BLACK);

        try {
            BufferedImage tablePhoto = ImageIO.read(new File("assets/table2.jpg"));
            var scaledPhoto = tablePhoto.getScaledInstance((int)(WINDOW_WIDTH*1.4), WINDOW_HEIGHT, Image.SCALE_DEFAULT);
            JLabel tablePhotoLabel = new JLabel(new ImageIcon(scaledPhoto));
            tablePhotoLabel.setBounds(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.add(tablePhotoLabel);
            RootComponent = tablePhotoLabel;
        }
        catch (IOException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
            RootComponent = MainWindow.getRootPane();
            return;
        }
    }

    public static void loadCardAssets() {

        File cardsDir = new File("assets/cards");
        for (var f : Objects.requireNonNull(cardsDir.listFiles())) {
            var name = f.getName().replace(".png", "");
            if (name.equals("back_of_card")) {
                continue;
            }

            var chunks = name.split("_");
            String rankString = chunks[0];
            String suitString = chunks[2];

            CardRank rank;
            CardSuit suit;

            try {
                int rankId = Integer.parseInt(rankString, 10);
                rank = CardRank.fromInt(rankId);
                suit = CardSuit.fromString(suitString);

            } catch (NumberFormatException exception) {
                rank = CardRank.fromString(rankString);
                suit = CardSuit.fromString(suitString);
            }

            var card = new Card(rank, suit);

            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            var tempImage = new ImageIcon(bufferedImage);
            var cardPhoto = tempImage.getImage().getScaledInstance(96, 128, Image.SCALE_DEFAULT);

            CardAssets.put(card, cardPhoto);
        }

        BufferedImage bufferedBackOfCardPhoto;
        try {
            bufferedBackOfCardPhoto = ImageIO.read(new File("assets/cards/back_of_card.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var tempImage = new ImageIcon(bufferedBackOfCardPhoto);
        BackOfCardImage = tempImage.getImage().getScaledInstance(96, 128, Image.SCALE_DEFAULT);
    }

    public static void renderGame() {
        var segoeUiFont_Size26 = new Font("Segoe UI", Font.PLAIN,26);
        var segoeUiFont_Size16 = new Font("Segoe UI", Font.PLAIN,16);

        DecimalFormat formatter = new DecimalFormat("#,###");

        JLabel potLabel = new JLabel(String.format("Pot: $%s", formatter.format(Game.getPot())));
        potLabel.setBounds(10,10,160,24);
        potLabel.setFont(segoeUiFont_Size26);
        potLabel.setForeground(Color.WHITE);
        Rectangle potLabelBounds = potLabel.getBounds();

        String yourBalanceLabelText = String.format("You: $%s", formatter.format(Game.getLocalPlayer().getBalance()));
        JLabel yourBalanceLabel = new JLabel(yourBalanceLabelText);
        yourBalanceLabel.setBounds(potLabelBounds.x,potLabelBounds.y + potLabelBounds.height + 10,160,24);
        yourBalanceLabel.setFont(segoeUiFont_Size26);
        yourBalanceLabel.setForeground(Color.WHITE);


        JLabel enemyCard1Label = new JLabel();
        enemyCard1Label.setBounds(330,10, 96, 128);
        Rectangle enemyCard1Bounds = enemyCard1Label.getBounds();

        JLabel enemyCard2Label = new JLabel();
        enemyCard2Label.setBounds(enemyCard1Bounds.x + enemyCard1Bounds.width + 12,10, 96, 128);

        if (Game.getState() != GameState.Showdown) {
            enemyCard1Label.setIcon(new ImageIcon(BackOfCardImage));
            enemyCard2Label.setIcon(new ImageIcon(BackOfCardImage));
        }
        else {
            var enemyPlayerCard1Image = getCardAsset(Game.getEnemyPlayer().getCard1());
            var enemyPlayerCard2Image = getCardAsset(Game.getEnemyPlayer().getCard2());

            enemyCard1Label.setIcon(new ImageIcon(enemyPlayerCard1Image));
            enemyCard2Label.setIcon(new ImageIcon(enemyPlayerCard2Image));
        }


        JLabel communityCard1Label = new JLabel(new ImageIcon(BackOfCardImage));
        JLabel communityCard2Label = new JLabel(new ImageIcon(BackOfCardImage));
        JLabel communityCard3Label = new JLabel(new ImageIcon(BackOfCardImage));
        JLabel communityCard4Label = new JLabel(new ImageIcon(BackOfCardImage));
        JLabel communityCard5Label = new JLabel(new ImageIcon(BackOfCardImage));


        var communityCard1Image = getCardAsset(Game.getCommunityCards()[0]);
        var communityCard2Image = getCardAsset(Game.getCommunityCards()[1]);
        var communityCard3Image = getCardAsset(Game.getCommunityCards()[2]);
        var communityCard4Image = getCardAsset(Game.getCommunityCards()[3]);
        var communityCard5Image = getCardAsset(Game.getCommunityCards()[4]);


        communityCard1Label.setBounds(164,222, 96, 128);
        Rectangle communityCard1LabelBounds = communityCard1Label.getBounds();

        communityCard2Label.setBounds(communityCard1LabelBounds.x + communityCard1LabelBounds.width + 12,222, 96, 128);
        Rectangle communityCard2LabelBounds = communityCard2Label.getBounds();

        communityCard3Label.setBounds(communityCard2LabelBounds.x + communityCard2LabelBounds.width + 12,222, 96, 128);
        Rectangle communityCard3LabelBounds = communityCard3Label.getBounds();

        communityCard4Label.setBounds(communityCard3LabelBounds.x + communityCard3LabelBounds.width + 12,222, 96, 128);
        Rectangle communityCard4LabelBounds = communityCard4Label.getBounds();

        communityCard5Label.setBounds(communityCard4LabelBounds.x + communityCard4LabelBounds.width + 12,222, 96, 128);

        switch (Game.getState()) {
            case River:
                communityCard5Label.setIcon(new ImageIcon(communityCard5Image));
            case Turn:
                communityCard4Label.setIcon(new ImageIcon(communityCard4Image));
            case Flop:
                communityCard3Label.setIcon(new ImageIcon(communityCard3Image));
                communityCard2Label.setIcon(new ImageIcon(communityCard2Image));
                communityCard1Label.setIcon(new ImageIcon(communityCard1Image));
            default:
                break;
        }


        var localPlayerCard1Image = getCardAsset(Game.getLocalPlayer().getCard1());
        var localPlayerCard2Image = getCardAsset(Game.getLocalPlayer().getCard2());

        JLabel localPlayerCard1Label = new JLabel(new ImageIcon(localPlayerCard1Image));
        localPlayerCard1Label.setBounds(330,422, 96, 128);
        Rectangle localPlayerCard1Bounds = localPlayerCard1Label.getBounds();

        JLabel localPlayerCard2Label = new JLabel(new ImageIcon(localPlayerCard2Image));
        localPlayerCard2Label.setBounds(localPlayerCard1Bounds.x + localPlayerCard1Bounds.width + 12,422, 96, 128);
        Rectangle localPlayerCard2Bounds = localPlayerCard2Label.getBounds();

        final int ActionButtonWidth = 108;
        final int ActionButtonHeight = 32;
        final int ActionButtonPadding = 8;

        var actionButtonsX = localPlayerCard2Bounds.x + localPlayerCard2Bounds.width + 24;


        JModernButton checkOrCallButton = new JModernButton();
        if (Game.canCheck()) {
            checkOrCallButton.setText("Check");
        }
        else {
            checkOrCallButton.setText("Call");
        }
        checkOrCallButton.setFont(segoeUiFont_Size16);
        checkOrCallButton.setBounds(actionButtonsX, 430, ActionButtonWidth, ActionButtonHeight);
        checkOrCallButton.setColor(Color.BLACK);
        checkOrCallButton.setColorOver(Color.DARK_GRAY);
        checkOrCallButton.setForeground(Color.WHITE);
        checkOrCallButton.setBorderColor(Color.WHITE);
        Rectangle checkOrCallButtonBounds = checkOrCallButton.getBounds();


        JModernButton foldButton = new JModernButton();
        foldButton.setText("Fold");
        foldButton.setFont(segoeUiFont_Size16);
        foldButton.setBounds(actionButtonsX, checkOrCallButtonBounds.y + checkOrCallButtonBounds.height + ActionButtonPadding, ActionButtonWidth, ActionButtonHeight);
        foldButton.setColor(Color.BLACK);
        foldButton.setColorOver(Color.DARK_GRAY);
        foldButton.setForeground(Color.WHITE);
        foldButton.setBorderColor(Color.RED);
        Rectangle foldButtonBounds = foldButton.getBounds();


        JModernButton raiseButton = new JModernButton();
        raiseButton.setText("Raise");
        raiseButton.setFont(segoeUiFont_Size16);
        raiseButton.setBounds(actionButtonsX, foldButtonBounds.y + foldButtonBounds.height + ActionButtonPadding, ActionButtonWidth, ActionButtonHeight);
        raiseButton.setColor(Color.BLACK);
        raiseButton.setColorOver(Color.DARK_GRAY);
        raiseButton.setForeground(Color.WHITE);
        raiseButton.setBorderColor(Color.GREEN);
        raiseButton.addActionListener(e -> {
            String raiseAmountString = JOptionPane.showInputDialog(MainWindow, "What are you raising to?", "Raise the Bet", JOptionPane.QUESTION_MESSAGE);
            if (raiseAmountString == null || raiseAmountString.isEmpty()) {
                return;
            }

            try {
                int raiseAmount = Integer.parseInt(raiseAmountString, 10);
            }
            catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(MainWindow, "Amount provided is not a valid number.","CANNOT RAISE", JOptionPane.ERROR_MESSAGE);
            }
        });


        RootComponent.add(potLabel);
        RootComponent.add(yourBalanceLabel);

        RootComponent.add(enemyCard1Label);
        RootComponent.add(enemyCard2Label);

        RootComponent.add(communityCard1Label);
        RootComponent.add(communityCard2Label);
        RootComponent.add(communityCard3Label);
        RootComponent.add(communityCard4Label);
        RootComponent.add(communityCard5Label);

        RootComponent.add(localPlayerCard1Label);
        RootComponent.add(localPlayerCard2Label);

        RootComponent.add(checkOrCallButton);
        RootComponent.add(foldButton);
        RootComponent.add(raiseButton);
    }

    public static Image getCardAsset(Card card) {
        if (card == null)
            return BackOfCardImage;

        for (var cardAsset : CardAssets.entrySet()) {
            var key = cardAsset.getKey();
            var value = cardAsset.getValue();

            if (card.Rank == key.Rank && card.Suit == key.Suit) {
                return value;
            }
        }
        return BackOfCardImage;
    }
}