import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Write a description of class startFrame here.
 *
 * @author (Josh Bhattarai)
 * @version (a version number or a date)
 */
class AddBracket
{
    private GetResources get = new GetResources();
    private String customKey = "~`v''~!~";
    private String bracketContent;

    void startMethod(){
        JFrame f = new JFrame("Add or View");
        f.setBounds(0,0,700,500);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setFocusable(true);
        f.setFocusTraversalKeysEnabled(false);
        f.setVisible(true);

        JLabel label1 = new JLabel("Add or View?");
        JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("View");
        JButton deleteButton = new JButton("Delete all Data");
        JButton saveButton = new JButton("Enter API key");
        JLabel label2 = new JLabel("Enter Challonge API key: ");
        JTextField apiField = new JTextField(60);
        JButton apiButton = new JButton("What's my Key?");
        JButton realDeleteButton = new JButton("Really Delete All Data");
        JButton nope = new JButton("I don't wanna delete all data");
        JButton apiSaveButton = new JButton("Save");

        nope.addActionListener(e -> {
            f.remove(realDeleteButton);
            f.remove(nope);
            reval(f);
        });

        realDeleteButton.addActionListener(e -> {
            String path = get.getProgramPath();
            get.purgeDirectory(path+"\\Brackets");
            f.remove(realDeleteButton);
            f.remove(nope);
            f.revalidate();
            f.repaint();
            f.pack();
            f.setSize(700,500);
        });

        apiButton.addActionListener(e -> {
            //
            GetResources.openWebpage("https://challonge.com/settings/developer");
        });

        saveButton.addActionListener(e -> {
            f.add(label2);
            f.add(apiField);
            f.add(apiSaveButton);
            reval(f);
        });

        apiSaveButton.addActionListener(e ->{
            String api = apiField.getText();
            if(!(api.trim().equals(""))) {
                String path = get.getProgramPath();
                path = path + "//Brackets//api_key.smash";
                get.writeFile(api, path);
            }
            f.remove(label2);
            f.remove(apiField);
            f.remove(apiSaveButton);
            reval(f);
        });

        addButton.addActionListener(e -> {
            addBracket();
            f.dispose();
        });

        viewButton.addActionListener(e -> {
            View v = new View();
            v.viewBrackets(customKey);
            f.dispose();
        });

        deleteButton.addActionListener(e -> {
            f.add(realDeleteButton);
            f.add(nope);
            reval(f);

        });


        f.add(label1);
        f.add(addButton);
        f.add(viewButton);
        f.add(deleteButton);
        f.add(saveButton);
        f.add(apiButton);

        f.setLayout(new FlowLayout());
        f.toFront();
        f.setResizable(false);
    }

    private void reval(JFrame f){
        f.revalidate();
        f.repaint();
        f.pack();
        f.setSize(700,500);
    }

    void addBracket(){
        JFrame f = new JFrame("Tourney Tracker");
        f.setBounds(0,0,700,500);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setFocusable(true);
        f.setFocusTraversalKeysEnabled(false);
        f.setVisible(true);



        JLabel label1 = new JLabel("Add Bracket URL: ");
        TextField bracketField = new TextField(78);
        JLabel label2 = new JLabel("Your Name/Tag: ");
        TextField tagField = new TextField(10);
        JLabel label3 = new JLabel("Venue fee: ");
        TextField venueField = new TextField(10);
        JLabel label4 = new JLabel("Bracket fee: ");
        TextField singlesField = new TextField(10);
        JLabel label6 = new JLabel("Winnings: ");
        TextField winningsField = new TextField(10);
        JButton enterButton = new JButton("Enter");
        JLabel label7 = new JLabel("Main Character: ");
        TextField mainField = new TextField(10);
        JLabel label8 = new JLabel("Secondary Character: ");
        TextField secondaryField = new TextField(10);
        JLabel label9 = new JLabel("Pocket Character: ");
        TextField pocketField = new TextField(10);
        JLabel label10 = new JLabel("Doubles? ");
        JCheckBox doublesBox = new JCheckBox();
        JLabel label11 = new JLabel ("Partner: ");
        TextField partnerField = new TextField(10);

        /*if(tutorial){
            bracketField.setText("http://challonge.com/Smash4Evo2016");
            tagField.setText("TSM | Zero");
            venueField.setText("100");
            singlesField.setText("100");
            winningsField.setText("1000");
            mainField.setText("Diddy Kong");
            secondaryField.setText("Sheik");
            pocketField.setText("Shulk");
        }*/


        JButton viewButton = new JButton("View");

        viewButton.addActionListener(e ->{
            View v = new View();
            v.viewBrackets(customKey);
            f.dispose();
        });

        enterButton.addActionListener(e -> {
            bracketContent = "";
            String doubles;
            String doublesPartner;
            if(doublesBox.isSelected()){
                doubles = "true";
                doublesPartner = partnerField.getText();
            }else{
                doubles = "false";
                doublesPartner = "null";
            }

            Bracket b = new Bracket();
            b.url = bracketField.getText();
            b.tag = tagField.getText();
            b.bracketFee = singlesField.getText();
            b.venueFee = venueField.getText();
            b.winnings = winningsField.getText();
            b.mainChar = mainField.getText();
            b.secondChar = secondaryField.getText();
            b.pocketChar = pocketField.getText();
            b.doublesPartner = doublesPartner;
            b.doubles = doubles.equals("true");
            writeData(b);

            bracketField.setText(bracketContent);
        });

        doublesBox.addActionListener(e ->{
            if(doublesBox.isSelected()){
                f.remove(enterButton);
                f.remove(viewButton);
                f.add(label11);
                f.add(partnerField);
                f.add(viewButton);
                f.add(enterButton);
                f.revalidate();
            }else{
                f.remove(label11);
                f.remove(partnerField);
                f.revalidate();
            }

        });

        f.add(label1);
        f.add(bracketField);
        f.add(label2);
        f.add(tagField);
        f.add(label3);
        f.add(venueField);
        f.add(label4);
        f.add(singlesField);
        f.add(label6);
        f.add(winningsField);
        f.add(label7);
        f.add(mainField);
        f.add(label8);
        f.add(secondaryField);
        f.add(label9);
        f.add(pocketField);
        f.add(label10);
        f.add(doublesBox);

        f.add(enterButton);

        f.add(viewButton);

        f.setLayout(new FlowLayout());
        f.toFront();
        f.setResizable(false);
    }

    private String getCode(String challongeLink){
        String[] initArr = challongeLink.split("challonge.com/");
        String code = initArr[1];
        String org = initArr[0];
        boolean organization = false;

        if(org.length() > 0){
            char c = org.charAt(org.length() - 1);
            if(c == '.'){
                org = org.replace("www","");
                org = org.replace("http","");
                org  = org.replace("/","");
                org = org.replace(":","");
                org = org.replace(".","");
                organization = true;
            }
        }
        String[] initArr2 = code.split("/");
        if(organization){
            return org + "-" + initArr2[0];
        }else{

            return initArr2[0];
        }
    }

    void writeData(Bracket b){
        Boolean allGood = true;
        String url = b.url;
        String tag = b.tag;
        String singlesFee = b.bracketFee;
        String venueFee = b.venueFee;
        String winnings = b.winnings;
        String mainCharacter = b.mainChar;
        String secondaryCharacter = b.mainChar;
        String pocketCharacter = b.pocketChar;
        String doublesPartner = b.doublesPartner;
        String doubles;
        String notes = b.notes;
        if(b.doubles){doubles = "true";}else{doubles="false";}
        String code = getCode(url);

        String apiKey = get.getTextFromFile(get.getProgramPath() + "//Brackets//api_key.smash");

        String getRequest = "https://api.challonge.com/v1/tournaments/" +
                code + ".json?include_matches=1&include_participants=1&api" + "_key=" + apiKey;
        String tournamentInfo = get.httpGet(getRequest,"Smashbot(joshbhattarai1@gmail.com)");
        if(tournamentInfo.equals("Fail")){
            System.out.println(tournamentInfo);
            allGood = false;
        }
        String path = get.getProgramPath();
        get.makeDirectory(path+"/Brackets/");

        String bracketInfo = tournamentInfo;
        tournamentInfo = "";

        if(tag.trim().equals("")){
            tag = "null";
        }
        if(singlesFee.trim().equals("")){
            singlesFee = "0";
        }
        if(venueFee.trim().equals("")){
            venueFee = "0";
        }
        if(winnings.trim().equals("")){
            winnings = "0";
        }
        if(mainCharacter.trim().equals("")){
            mainCharacter = "null";
        }
        if(secondaryCharacter.trim().equals("")){
            secondaryCharacter = "null";
        }
        if(pocketCharacter.trim().equals("")){
            pocketCharacter = "null";
        }
        if(notes==null||notes.trim().equals("")){
            notes = "none";
        }


        tournamentInfo = tournamentInfo + tag + customKey;
        tournamentInfo = tournamentInfo + singlesFee + customKey;
        tournamentInfo = tournamentInfo + venueFee + customKey;
        tournamentInfo = tournamentInfo + winnings + customKey;
        tournamentInfo = tournamentInfo + mainCharacter + customKey;
        tournamentInfo = tournamentInfo + secondaryCharacter + customKey;
        tournamentInfo = tournamentInfo + pocketCharacter + customKey;
        tournamentInfo = tournamentInfo + doubles + customKey;
        tournamentInfo = tournamentInfo + doublesPartner + customKey;
        tournamentInfo = tournamentInfo + notes + customKey;


        String metaPath = path+"/Brackets/meta.data/";
        File f1 = new File(metaPath);
        String metaData;
        if(f1.exists() && !f1.isDirectory()){
            metaData = get.getTextFromFile(f1);
            String[] codes = metaData.split(customKey);

            String [] lowerCaseCodes;
            ArrayList<String> strings = new ArrayList<>();

            for(String c:codes){
                strings.add(c.toLowerCase());
            }
            lowerCaseCodes = strings.toArray(new String[strings.size()]);

            if (Arrays.asList(lowerCaseCodes).indexOf(code.toLowerCase()) <= -1) {
                metaData = metaData + customKey + code;
            }
        }else{
            metaData = code;
        }

        if(allGood) {
            get.writeFile(metaData, metaPath);

            get.writeFile(tournamentInfo, path + "/Brackets/" + code + "_data.smash");
            get.writeFile(bracketInfo, path + "/Brackets/" + code + ".smash");
        }else{
            bracketContent = "Fail";
        }
    }

    void writeBracket(Bracket b){
        String url = b.url;
        String tag = b.tag;
        String singlesFee = b.bracketFee;
        String venueFee = b.venueFee;
        String winnings = b.winnings;
        String mainCharacter = b.mainChar;
        String secondaryCharacter = b.mainChar;
        String pocketCharacter = b.pocketChar;
        String doublesPartner = b.doublesPartner;
        String doubles;
        String notes = b.notes;
        if(b.doubles){doubles = "true";}else{doubles="false";}
        String code = getCode(url);
        String path = get.getProgramPath();
        get.makeDirectory(path+"/Brackets/");
        String tournamentInfo = "";

        if(tag.trim().equals("")){
            tag = "null";
        }
        if(singlesFee.trim().equals("")){
            singlesFee = "0";
        }
        if(venueFee.trim().equals("")){
            venueFee = "0";
        }
        if(winnings.trim().equals("")){
            winnings = "0";
        }
        if(mainCharacter.trim().equals("")){
            mainCharacter = "null";
        }
        if(secondaryCharacter.trim().equals("")){
            secondaryCharacter = "null";
        }
        if(pocketCharacter.trim().equals("")){
            pocketCharacter = "null";
        }
        if(notes==null||notes.trim().equals("")){
            notes = "none";
        }


        tournamentInfo = tournamentInfo + tag + customKey;
        tournamentInfo = tournamentInfo + singlesFee + customKey;
        tournamentInfo = tournamentInfo + venueFee + customKey;
        tournamentInfo = tournamentInfo + winnings + customKey;
        tournamentInfo = tournamentInfo + mainCharacter + customKey;
        tournamentInfo = tournamentInfo + secondaryCharacter + customKey;
        tournamentInfo = tournamentInfo + pocketCharacter + customKey;
        tournamentInfo = tournamentInfo + doubles + customKey;
        tournamentInfo = tournamentInfo + doublesPartner + customKey;
        tournamentInfo = tournamentInfo + notes + customKey;

        get.writeFile(tournamentInfo, path + "/Brackets/" + code + "_data.smash");
    }


}
