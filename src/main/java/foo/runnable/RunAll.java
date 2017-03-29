package foo.runnable;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import foo.fausets.Chicken;

public class RunAll implements Runnable {

  String usuario = "";
  String password = "";
  String key = "";
  String keyL = "";
  String huefaucet = "";
  String bituniverse = "";
  String bitlucky = "";
  String bitfun = "";
  String bandirun = "";
  String smurfgo = "";
  String gmail = "";
  String hotmail = "";
  String hotmailLogin = "";
  String urlOutlook = "";
  String mailG = "";
  String mailH = "";
  String phone = "";
  String bitFarm = "";
  String liteFarm = "";
  String bitcoFarm = "";
  String wall = "";
  String fb = "";

  static int dimX = 450, dimX1 = 372;// 331 dimX
  static int dimY = 379, dimY1 = 600;

  public static Dimension SIZE = new Dimension(dimX, dimY);
  public static Dimension SIZE1 = new Dimension(dimX1, dimY);
  public static Dimension SIZE2 = new Dimension(dimX, dimY1);
  int[] x = {0, dimX, 2 * dimX, 3 * dimX};
  int[] y = {0, dimY};
  int[] x1 = {0, dimX1, 2 * dimX1};

  public void run() {
    System.out.println("Starting...");

    (new Thread(new Chicken(bitcoFarm, usuario, password, new Point(0, 0), new Dimension(588, 720), true))).start();
    (new Thread(new Chicken(bitcoFarm, usuario, password, new Point(629, 0), new Dimension(588, 720), false))).start();
    /*
     * (new Thread(new BitFarm(bitFarm, key, new Point(x[0],y[0])))).start(); (new Thread(new
     * BuyMoreBit(bitFarm, key))).start(); (new Thread(new BuyMoreLit(liteFarm,keyL))).start(); (new
     * Thread(new Recolector(smurfgo, usuario, password, new Point(x[1],y[0])))).start(); (new
     * Thread(new Recolector(bandirun, usuario, password, new Point(x[2],y[0])))).start(); (new
     * Thread(new AppBF(bitfun, usuario.concat("@gmail.com"), password, new
     * Point(x[3],y[0])))).start();
     * 
     * //372,385 (new Thread(new App(huefaucet, key, new Point(x1[0],y[1])))).start(); (new
     * Thread(new App(bituniverse, key, new Point(x1[1],y[1])))).start(); (new Thread(new
     * App(bitlucky, key, new Point(x1[2],y[1])))).start();
     * 
     * (new Thread(new CreateAccount(hotmail, mailH, phone, "07"))).start(); (new Thread(new
     * CreateWall(wall, hotmailLogin, urlOutlook, "05"))).start(); (new Thread(new CreateFb(fb,
     * phone, "07"))).start(); (new Thread(new PrintWallIDs(wall, "02"))).start();
     */ }

  public static void main(String[] args) {
    (new Thread(new RunAll())).start();
  }
}
