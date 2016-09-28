import javax.swing.*;
import java.awt.*;
/**
 *
 * Created by Josh on 9/25/2016.
 */
class PopOut {
    private GetResources get = new GetResources();
    private AddBracket ab = new AddBracket();
    private JFrame window;
    private JPanel mainFrame;
    private JPanel topPanel;
    private JPanel infoPanel;
    private JPanel contentPanel;
    private JPanel bottomPanel;

    void start(Bracket b,String customKey){
        window = new JFrame(b.name + " " + b.date);
        window.setBounds(0, 0, 700, 500);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setFocusable(true);
        window.setFocusTraversalKeysEnabled(false);
        window.setVisible(true);
        window.setBackground(Color.white);
        mainFrame = new JPanel();
        topPanel = new JPanel();
        infoPanel = new JPanel();
        contentPanel = new JPanel();
        bottomPanel = new JPanel();

        JButton deleteButton = new JButton("Delete this Tournament");
        JButton refreshButton = new JButton("Refresh this Tournament");
        JButton editButton = new JButton("Edit");
        JButton saveButton = new JButton("Save");

        JLabel teamName = new JLabel("Team Name: " + b.tag);
        JLabel partnerLabel = new JLabel("Doubles Partner: " + b.doublesPartner);
        JLabel seedLabel = new JLabel("Seeded: " + b.seed);
        JLabel placeLabel = new JLabel("Placed: " + b.finalRank + " out of: " + b.participants + " participants.");
        JLabel accLabel = new JLabel("Seed Accuracy: "+ b.seedAccuracy);
        int profit;
        if(GetResources.isInteger(b.winnings,10)&&GetResources.isInteger(b.bracketFee,10)&&GetResources.isInteger(b.venueFee,10)) {
            profit = Integer.parseInt(b.winnings) - (Integer.parseInt(b.bracketFee) + Integer.parseInt(b.venueFee));
        }else{
            profit = 0;
        }
        JLabel spaceLabel = new JLabel("         ");
        JLabel moneyLabel = new JLabel ("Venue Fee: $" + b.venueFee+" Bracket fee: $" + b.bracketFee + " Won: $" + b.winnings +
                " Net Profit: $" + profit);



        JLabel notesLabel = new JLabel("Notes: ");
        JTextField notes = new JTextField(b.notes);
        notes.setEditable(false);
        notes.setBorder(null);
        JTextField notesField = new JTextField(60);

        if(b.doubles){
            infoPanel.add(teamName);
            infoPanel.add(partnerLabel);
        }
        infoPanel.add(seedLabel);
        infoPanel.add(placeLabel);
        infoPanel.add(accLabel);
        infoPanel.add(spaceLabel);
        infoPanel.add(moneyLabel);

        topPanel.add(refreshButton);
        topPanel.add(deleteButton);

        contentPanel.add(notesLabel);
        contentPanel.add(notes);
        topPanel.setLayout(new FlowLayout());
        infoPanel.setLayout(new FlowLayout());
        contentPanel.setLayout(new FlowLayout());
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(editButton);
        mainFrame.add(topPanel);
        mainFrame.add(infoPanel);
        mainFrame.add(contentPanel);
        mainFrame.add(bottomPanel);
        mainFrame.setLayout(new BoxLayout(mainFrame,1));
        window.setContentPane(mainFrame);
        window.toFront();
        window.setResizable(true);
        window.pack();
        window.setBounds(0,0,700,500);

        editButton.addActionListener(e -> {
            bottomPanel.remove(editButton);
            contentPanel.remove(notes);

            bottomPanel.add(saveButton);
            contentPanel.add(notesField);
            notesField.setText(b.notes);
            repaintAll();
        });

        saveButton.addActionListener(e -> {
            b.notes = notesField.getText();
            AddBracket ab = new AddBracket();
            ab.writeBracket(b);


            bottomPanel.remove(saveButton);
            contentPanel.remove(notesField);

            bottomPanel.add(editButton);
            contentPanel.add(notes);
            notes.setText(b.notes);
            repaintAll();

        });

        refreshButton.addActionListener(e -> {

            if(!b.complete){
                ab.writeData(b);
            }
        });

        deleteButton.addActionListener(e -> {
            String path = get.getProgramPath();
            String dataPath = path + "//Brackets//" + b.code + "_data.smash";
            String smashPath = path + "//Brackets//" + b.code + ".smash";
            String metaPath = path + "//Brackets//meta.data";
            get.deleteFile(dataPath);
            get.deleteFile(smashPath);
            String content = get.getTextFromFile(metaPath);
            int index = content.indexOf(b.code);
            int codeLength = b.code.length();


            get.deleteFile(metaPath);
            if(content.length() == codeLength){
                content = "";
            }
            else if(codeLength + 8+index>content.length()){
                content = content.substring(0,index-8);
                get.writeFile(content,metaPath);
            }
            else{
                content = content.substring(0,index) + content.substring(index+codeLength+8,content.length());
                get.writeFile(content,metaPath);
            }

        });
    }

    private void repaintAll(){
        bottomPanel.revalidate();
        bottomPanel.repaint();
        contentPanel.revalidate();
        contentPanel.repaint();
        topPanel.revalidate();
        topPanel.repaint();
        infoPanel.revalidate();
        infoPanel.repaint();
        mainFrame.revalidate();
        mainFrame.repaint();
        window.revalidate();
        window.repaint();
        window.pack();
        window.setSize(700,500);
    }



}
