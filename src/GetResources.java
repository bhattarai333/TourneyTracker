import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.Font;
import java.net.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Write a description of class getResources here.
 *
 * @author Josh Bhattarai and Friends
 * @version 1?
 */
class GetResources
{

    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }

    public BufferedImage getImg(String path){
        //returns BufferedImage of a png file with the file name path in sprites folder
        BufferedImage img = null;
        try{
            img = ImageIO.read(this.getClass().getResource("/Resources/Sprites/" + path + ".png"));//gets image from file path
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public ArrayList<BufferedImage> cutSheet(String path, int cols, int rows){
        BufferedImage spriteSheet = getImg(path);
        double sheetHeight = spriteSheet.getHeight();
        double sheetWidth = spriteSheet.getWidth();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        double width = sheetWidth/cols;
        double height = sheetHeight/rows;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                images.add ( spriteSheet.getSubimage(
                        (int)(j * width + 1),
                        (int)(i * height + 1),
                        (int)(width - 1),
                        (int)(height - 1)
                ));
            }
        }
        return images;
    }


    public Font getFont(String filename, float fontSize) {
        Font myfont = null;
        Font myfontReal = null;
        try {
            InputStream is = new BufferedInputStream(this.getClass().getResourceAsStream("/Resources/Fonts/" + filename+".ttf"));

            myfont = Font.createFont(Font.TRUETYPE_FONT, is);
            myfontReal = myfont.deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            System.out.println(e.getMessage());
        }
        return myfontReal;
    }

    public BufferedImage getScreenShot(Component c) {
        //jframe.getContentPane()
        BufferedImage img = new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_RGB);
        c.paint( img.getGraphics() );
        return img;
    }

    public void saveImg(BufferedImage img){
        String path = null;
        String path2 = null;
        path = "./Screens/screenshot0t.png";
        try{
            new File("./Screens").mkdir();

            path2 = iterate(path,0);
            File outputfile = new File(path2);
            ImageIO.write(img, "png", outputfile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public String iterate(String path,int d){
        //part of saveImg
        File f = new File(path);
        String path2 = path;
        int i = d;
        if(f.exists() && !f.isDirectory()) {
            String[] tokens = path2.split("t");
            i=i+1;
            path2 = tokens[0]+"t" + i + "t"+tokens[2];
            return (iterate(path2, i));
        }else{
            return path2;
        }
    }

    public int randomWithRange(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    public BufferedImage getSizedImg(BufferedImage otherImage,int width,int height){
        BufferedImage outputImg = otherImage;
        try{
            outputImg = resizeUsingJavaAlgo(otherImage,width,height);
        }catch(IOException e){
            e.printStackTrace();
        }
        return outputImg;
    }

    public BufferedImage resizeUsingJavaAlgo(BufferedImage image, int width, int height) throws IOException {
        //part of getSizedImg thanks to StackOverflow user Mladen Adamovic
        BufferedImage sourceImage = image;
        double ratio = (double) sourceImage.getWidth()/sourceImage.getHeight();
        if (width < 1) {
            width = (int) (height * ratio + 0.4);
        } else if (height < 1) {
            height = (int) (width /ratio + 0.4);
        }

        Image scaled = sourceImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        BufferedImage bufferedScaled = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedScaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(scaled, 0, 0, width, height, null);
        return bufferedScaled;
    }

    public String getTextFromFile(String path){
        File f = new File(path);
        return getTextFromFile(f);
    }

    public String getTextFromFile(File f){
        BufferedReader br = null;
        String output = "";
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(f.toPath().toString()));

            while ((sCurrentLine = br.readLine()) != null) {
                output = output + sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return output;
    }

    public String getProgramPath(){
        File f = new File("./Resources/Sprites/blank.png");
        String s = f.getAbsolutePath();
        String[] stringArr = s.split("Resources");
        String s2 = stringArr[0];
        s2 = s2.substring(0, s2.length() - 3);
        return s2;
    }

    public void writeFile(String content, String path){
        try{
            File file = new File(path);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void makeDirectory(String path){
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }
    }

    public void deleteFile(String path){
        File f = new File(path);
        if(!f.isDirectory()){
            f.delete();
        }
    }

    public void purgeDirectory(String path){
        File dir = new File(path);
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file.toPath().toString());//recursively delete all folders in the folder
            file.delete();
        }
    }

    public String httpGet(String url,String userAgent){
        try{
            return(httpGett(url,userAgent));
        }catch(Exception e){
            return "Fail";
        }
    }

    private String httpGett(String url,String userAgent) throws Exception{
        String USER_AGENT = userAgent;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Complete");
        return(response.toString());
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(String urlString) {
        try {
            URL url = new URL(urlString);
            openWebpage(url.toURI());
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getUnicodeSymbol(int codePoint) {
        return new String(Character.toChars(codePoint));
    }


}
