import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Site {
  final int siteId;
  final int x;
  final int y;
  final int radius;
  Integer structureType;

  public Site(int siteId, int x, int y, int radius) {
    this.siteId = siteId;
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
}

class Unit {
  int x;
  int y;
  Boolean isMine;
  int unitType;
  int health;

  public Unit(int x, int y, Boolean isMine, int unitType, int health) {
    this.x = x;
    this.y = y;
    this.isMine = isMine;
    this.unitType = unitType;
    this.health = health;
  }
}

class Player {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int numSites = in.nextInt();

    List<Site> sites = new ArrayList<>();

    for (int i = 0; i < numSites; i++) {
      int siteId = in.nextInt();
      int x = in.nextInt();
      int y = in.nextInt();
      int radius = in.nextInt();
      sites.add(new Site(siteId, x, y, radius));
    }


    // game loop
    while (true) {
      int gold = in.nextInt();
      int touchedSite = in.nextInt(); // -1 if none
      for (int i = 0; i < numSites; i++) {
        int siteId = in.nextInt();
        int ignore1 = in.nextInt(); // used in future leagues
        int ignore2 = in.nextInt(); // used in future leagues
        int structureType = in.nextInt(); // -1 = No structure, 2 = Barracks
        int owner = in.nextInt(); // -1 = No structure, 0 = Friendly, 1 = Enemy
        int param1 = in.nextInt();
        int param2 = in.nextInt();

        sites.stream()
            .filter(site -> site.siteId == siteId)
            .findFirst()
            .ifPresent(site -> {
              if (structureType == -1) {
                site.structureType = null;
              } else {
                site.structureType = structureType;
              }
            });
      }
      int numUnits = in.nextInt();
      int tmpX = 0;
      int tmpY = 0;

      List<Unit> units = new ArrayList<>();
      for (int i = 0; i < numUnits; i++) {
        int x = in.nextInt();
        int y = in.nextInt();
        int owner = in.nextInt();
        int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
        int health = in.nextInt();
        if (owner == 0 && unitType == -1) {
          tmpX = x;
          tmpY = y;
        }
        units.add(new Unit(x, y, owner == 0, unitType, health));
      }

      // Write an action using System.out.println()
      // To debug: System.err.println("Debug messages...");


      final int qY = tmpY;
      final int qX = tmpX;
      int closest = 0;
      try {
        double min = minDist(qX, qY, sites.get(0).x, sites.get(0).y);
        for (Site s : sites) {
          double tmp = minDist(qX, qY, s.x, s.y);
          if (tmp < min) {
            closest = s.siteId;
            min = tmp;
          }
        }
        System.err.println(closest);

        // znalezienie najbliższego miejsca i pójście tam
      } catch (Exception e) {
        e.printStackTrace();
        closest = 0;
      }

      // First line: A valid queen action
      // Second line: A set of training instructions
      System.out.println("BUILD " + closest + " BARRACKS-KNIGHT");
      System.out.println("TRAIN " + closest);
    }
  }

  static double minDist(int x1, int y1, int x2, int y2) {
    int dx = x1 - x2;
    int dy = y1 - y2;

    return Math.sqrt(dx * dx + dy * dy);
  }
}