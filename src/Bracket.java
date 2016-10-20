import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.util.ArrayList;

/**
 * Created by Josh on 9/15/2016.
 *
 */
class Bracket implements Comparable<Bracket> {
    String code;
    String name;
    String rawDate;
    String date;
    String description;
    String url;
    String gameName;
    String state;

    JSONObject bracket;

    int participantID;
    int finalRank;
    int seed;
    int seedAccuracy;
    int participants;

    String tag;
    String venueFee;
    String bracketFee;
    String winnings;
    String mainChar;
    String secondChar;
    String pocketChar;
    Boolean doubles;
    String doublesPartner;

    Boolean complete;


    double percentile;
    int sort;

    ArrayList<JSONObject> wins;
    ArrayList<JSONObject> losses;
    String notes;


    @Override
    public String toString(){
        return code + " " + name + " " + date + " " + description + " " + url  + " " + gameName + " " + tag
                + " " + mainChar + " " + secondChar + " " + pocketChar + " " + doublesPartner;
    }


    public int compareTo(@NotNull Bracket b){
        switch (sort){
            case 0:
                //sort by date
                return -b.rawDate.compareTo(this.rawDate);
            case 1:
                //sort by placing
                return this.finalRank - b.finalRank;
            case 2:
                //sort by participants
                return b.participants-this.participants;
            case 3:
                //sort by name
                return -b.name.compareTo(this.name);
            case 4:
                //sort by percentile
                if(this.percentile == b.percentile){
                    return 0;
                }else if (this.percentile > b.percentile){
                    return -1;
                }else{
                    return 1;
                }
        }
        return 0;

    }


    void calc(){
        calcPercentile();
        convertDate();
        calcSeed();
        stringStuff();

    }

    private void calcSeed(){
        seedAccuracy = seed-finalRank;
    }

    private void calcPercentile(){
        double fR = finalRank;
        double part = participants;
        double belowX = part-fR;

        percentile = (belowX/part) * 100;
        percentile = Math.round(percentile*100.0)/100.0;
    }

    private void convertDate(){
        String[] dates = rawDate.split("T");
        String newDate = dates[0];
        String year = newDate.substring(0,4);
        String month = newDate.substring(5,7);
        String day = newDate.substring(8,10);

        switch (month) {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
        }

        date = month + "-" + day + "-" + year;

    }

    private void stringStuff(){
        sort = 0;
        url = url.toLowerCase();
    }

}
