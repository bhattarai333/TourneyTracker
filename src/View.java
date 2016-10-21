import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import org.json.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Write a description of class view here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

class View
{
    private final int SORT_BY_DATE = 0;
    private final int SORT_BY_PLACE = 1;
    private final int SORT_BY_SIZE = 2;
    private final int SORT_BY_NAME = 3;
    private final int SORT_BY_PERCENT = 4;
    private ArrayList<Bracket> allBrackets = new ArrayList<>();
    private ArrayList<Bracket> bracketsToDisplay = new ArrayList<>();
    private String customKey;
    private GetResources get = new GetResources();
    private JFrame window;
    private JPanel mainPanel;
    private JPanel bracketPanel = new JPanel();
    private boolean isSearching = false;
    private boolean filterInReverse = false;
    private ImageIcon magnifyingGlassIcon;

    void viewBrackets(String cK) {
        customKey = cK;
        window = new JFrame("Bracket Info");
        mainPanel = new JPanel();
        magnifyingGlassIcon = new ImageIcon(get.getSizedImg(get.getImg("search"),16,16));

        readJSON();
        display();
        displayBrackets();
    }


    private void displayBrackets(){
        boolean color = true;
        if(isSearching){
            Collections.sort(bracketsToDisplay);
        }else{
            bracketsToDisplay.addAll(allBrackets);
            Collections.sort(allBrackets);
        }
        bracketPanel.setBackground(Color.white);
        if(filterInReverse){
            Collections.reverse(bracketsToDisplay);
        }
        for (Bracket b : bracketsToDisplay) {

            JPanel bPanel = new JPanel();
            bPanel.setBackground(Color.white);

            String nameContent = b.name;
            if(b.doubles){
                nameContent = "<html>"+b.name+"<font size=3>    *Doubles";
            }
            JLabel label1 = new JLabel(nameContent);
            label1.setFont(new Font("SansSerif", Font.BOLD,42));
            JLabel label2 = new JLabel(b.tag + " placed " + b.finalRank + " out of: " + b.participants + " participants.");
            label2.setFont(new Font("Serif",Font.PLAIN,28));
            JLabel label3 = new JLabel("Beat " + b.percentile + "% of the competition.");
            label3.setFont(new Font("Serif",Font.PLAIN,28));
            JLabel label4 = new JLabel(b.tag + " was seeded " + b.seed);// + " " + b.seedAccuracy + " change.");
            label4.setFont(new Font("Serif",Font.PLAIN,28));

            JPanel tempPanel = new JPanel();
            JButton infoButton = new JButton("More Info");
            JLabel label5 = new JLabel(b.date);
            label5.setFont(new Font("Serif",Font.PLAIN,28));

            JLabel labelnull = new JLabel("");

            tempPanel.add(labelnull);

            infoButton.addActionListener(e ->{
                PopOut p = new PopOut();
                p.start(b,customKey,this);
            });

            label1.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    GetResources.openWebpage(b.url);
                }
            });




            bPanel.add(label1);
            bPanel.add(label5);
            bPanel.add(label2);
            bPanel.add(label3);
            bPanel.add(label4);
            bPanel.add(infoButton);

            tempPanel.setLayout(new FlowLayout());
            bPanel.setLayout(new BoxLayout(bPanel,1));

            if(color){
                bPanel.setBackground(Color.decode("#ffff66"));
                tempPanel.setBackground(Color.decode("#ffff66"));
                bPanel.add(tempPanel);
                color = false;
            }else{
                bPanel.setBackground(Color.decode("#99ccff"));
                tempPanel.setBackground(Color.decode("#99ccff"));
                bPanel.add(tempPanel);
                color = true;
            }

            bPanel.setBorder(BorderFactory.createMatteBorder(1,5,1,1,Color.black));
            bracketPanel.add(bPanel);
        }

        

        bracketPanel.setLayout(new BoxLayout(bracketPanel,1));
        mainPanel.add(bracketPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel,1));
        JScrollPane jsp = new JScrollPane(mainPanel);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        window.setContentPane(jsp);
        window.toFront();
        window.setResizable(true);
        window.pack();
        window.setSize(800,600);
    }

    private void display() {
        window.setBounds(0, 0, 800, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setFocusable(true);
        window.setFocusTraversalKeysEnabled(false);
        window.setVisible(true);
        window.setBackground(Color.white);

        mainPanel.setBackground(Color.white);

        JPanel sortPanel = new JPanel();
        JLabel sortLabel = new JLabel("Sort by: ");
        String[] sorts = {"Date", "Placing", "Participants","Name","Percentile"};
        JComboBox<String> box = new JComboBox<>(sorts);
        JButton enterButton = new JButton("Enter");
        JButton addButton = new JButton("Add new Brackets");
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton();
        searchButton.setIcon(magnifyingGlassIcon);
        JButton switchButton = new JButton("↑");
        switchButton.setBorder(null);
        switchButton.setFont(new Font("Arial",Font.PLAIN,24));


        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortPanel.add(addButton);
        sortPanel.add(sortLabel);
        sortPanel.add(box);
        sortPanel.add(switchButton);
        sortPanel.add(enterButton);
        sortPanel.add(searchField);
        sortPanel.add(searchButton);
        sortPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        sortPanel.setBorder(BorderFactory.createMatteBorder(1,5,1,1,Color.black));
        mainPanel.add(sortPanel);
        switchButton.addActionListener(e -> {
            if(filterInReverse){
                switchButton.setText("↑");
                filterInReverse = false;
            }else{
                switchButton.setText("↓");
                filterInReverse = true;
            }
        });

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim().toLowerCase();
            bracketsToDisplay.clear();
            if(searchTerm.equals("")){
                isSearching = false;
            }else{
                isSearching = true;
                for(Bracket b : allBrackets){
                    if(b.toString().toLowerCase().contains(searchTerm)){
                        bracketsToDisplay.add(b);
                    }
                }
            }

            redisplayBrackets();
        });

        addButton.addActionListener(e ->{
            AddBracket ab = new AddBracket();
            ab.addBracket();
            window.dispose();
        });



        enterButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();


            if(searchTerm.equals("")){
                isSearching = false;
                bracketsToDisplay.clear();
            }
            String sortString = box.getSelectedItem().toString();
            switch(sortString){
                case "Date":
                    for(Bracket b: allBrackets){
                        b.sort = SORT_BY_DATE;
                    }
                    break;
                case "Placing":
                    for(Bracket b: allBrackets){
                        b.sort = SORT_BY_PLACE;
                    }
                    break;
                case "Participants":
                    for(Bracket b: allBrackets){
                        b.sort = SORT_BY_SIZE;
                    }
                    break;
                case "Name":
                    for(Bracket b: allBrackets){
                        b.sort = SORT_BY_NAME;
                    }
                    break;
                case "Percentile":
                    for(Bracket b: allBrackets){
                        b.sort= SORT_BY_PERCENT;
                    }
            }


            Collections.sort(allBrackets);
            redisplayBrackets();
        });

    }





    private void readJSON(){
        String programPath = get.getProgramPath();
        File f = new File(programPath + "\\Brackets\\meta.data");
        String metaData = get.getTextFromFile(f);

        String[] codes = metaData.split(customKey);

        for (String code : codes) {
            File f1 = new File(programPath + "\\Brackets\\" + code + "_data.smash");
            Bracket b = new Bracket();
            String data = get.getTextFromFile(f1);
            String[] dataStuff = data.split(customKey);

            b.tag = dataStuff[0];
            b.bracketFee = dataStuff[1];
            b.venueFee = dataStuff[2];
            b.winnings = dataStuff[3];
            b.mainChar = dataStuff[4];
            b.secondChar = dataStuff[5];
            b.pocketChar = dataStuff[6];
            b.notes = dataStuff[9];
            if (dataStuff[7].equals("true")) {
                b.doubles = true;
                b.doublesPartner = dataStuff[8];
            } else {
                b.doubles = false;
                b.doublesPartner = "null";
            }


            File f2 = new File(programPath + "\\Brackets\\" + code + ".smash");
            String challongeData = get.getTextFromFile(f2);

            if(challongeData.equals("Fail")){
                String path = programPath +"\\Brackets\\" + code + ".smash";
                get.deleteFile(path);
                path = programPath +"\\Brackets\\" + code + "_data.smash";
                get.deleteFile(path);
                break;
            }

            JSONObject jsonStuff = new JSONObject(challongeData);
            b.bracket = jsonStuff;
            b.code = code;
            jsonStuff = new JSONObject(jsonStuff.getJSONObject("tournament").toString());
            b.name = jsonStuff.getString("name");
            b.rawDate = jsonStuff.getString("started_at");
            b.url = jsonStuff.getString("full_challonge_url");
            b.description = jsonStuff.getString("description");
            b.gameName = jsonStuff.get("game_name").toString();
            b.state = jsonStuff.getString("state");


            JSONArray participantArr = jsonStuff.getJSONArray("participants");
            b.participants = participantArr.length();

            for (int j = 0; j < participantArr.length(); j++) {
                JSONObject participant = participantArr.getJSONObject(j);
                participant = participant.getJSONObject("participant");

                if (b.tag.equals(participant.getString("name"))) {
                    b.seed = participant.getInt("seed");
                    b.participantID = participant.getInt("id");
                    if (b.state.equals("complete")) {
                        String s = participant.get("final_rank").toString();
                        b.complete = true;
                        if(isInt(s)){
                            b.finalRank = Integer.parseInt(s);
                        }else{
                            b.finalRank = 0;
                        }
                    } else {
                        b.complete = false;
                        b.finalRank = getFinalRankFromStandings(b.url);
                    }
                    b.calc();
                }
            }


            JSONArray matches = jsonStuff.getJSONArray("matches");
            ArrayList<JSONObject> wins = new ArrayList<>();
            ArrayList<JSONObject> losses = new ArrayList<>();

            for(int i = 0; i < matches.length(); i++){
                JSONObject match = matches.getJSONObject(i);
                match = match.getJSONObject("match");
                int winner = 0;
                int loser = 0;
                if(isInt(match.get("loser_id").toString())&&isInt(match.get("winner_id").toString())) {
                    winner = match.getInt("winner_id");
                    loser = match.getInt("loser_id");
                }
                if(winner == b.participantID){
                    wins.add(match);
                }
                if(loser == b.participantID){
                    losses.add(match);
                }
                b.wins = wins;
                b.losses = losses;
            }

            allBrackets.add(b);


        }
    }

    private boolean isInt(String s){
        return(GetResources.isInteger(s,10));
    }

    private int getFinalRankFromStandings(String url){
        return 0/url.length();//temp
    }

    private void redisplayBrackets(){
        redisplayBrackets(false);
    }

    void redisplayBrackets(Boolean clear){
        if(clear){
            bracketsToDisplay.clear();
        }
        bracketPanel.removeAll();
        bracketPanel.revalidate();
        bracketPanel.repaint();
        displayBrackets();
    }
    void deleteBracket(Bracket b){
        allBrackets.remove(b);
    }
}