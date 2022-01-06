import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
  Boolean isOurs = true;
  int param1;

  @Override
  public String toString() {
    return "Site{ siteId: " + siteId + "| isOurs: " + isOurs + "| x: " + x + "| y: " + y + "| radius: " + radius + "| type: " + structureType +
        "| param1: " + param1 + "}";
  }

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
  public static void main(String[] args) {
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

    // Write an action using System.out.println()
    // To debug: System.err.println("Debug messages...");

    // game loop
    while (true) {
      int gold = in.nextInt();
      int touchedSite = in.nextInt(); // -1 if none
      for (int i = 0; i < numSites; i++) {
        int siteId = in.nextInt();
        int ignore1 = in.nextInt(); // used in future leagues
        int ignore2 = in.nextInt(); // used in future leagues
        int structureType = in.nextInt(); // -1 = No structure, 1 = Tower, 2 = Barracks
        int owner = in.nextInt(); // -1 = No structure, 0 = Friendly, 1 = Enemy
        int param1 = in.nextInt();
        int param2 = in.nextInt();

        //dla structureType = -1 ustawienie null w instancjach
        sites.stream()
            .filter(site -> site.siteId == siteId)
            .findFirst()
            .ifPresent(site -> {
              if (structureType == -1) {
                site.structureType = null;
              } else {
                site.structureType = structureType;
              }
              site.isOurs = owner == 0;
              site.param1 = param1;
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
        int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER, 2 = GIANT
        int health = in.nextInt();

        //współrzędne naszej królowej
        if (owner == 0 && unitType == -1) {
          tmpX = x;
          tmpY = y;
        }
        units.add(new Unit(x, y, owner == 0, unitType, health));
      }

      System.err.println(sites);
      final int qY = tmpY;
      final int qX = tmpX;
      Integer ourBarrackId;
      try {
        ourBarrackId = sites.stream()
            .filter(site -> site.isOurs && site.structureType == 2)
            .collect(Collectors.toList())
            .get(0).siteId;

      } catch (Exception e) {
        ourBarrackId = null;
      }
      //////////////
      System.err.println("barrack id: " + ourBarrackId);

      Integer ourClosestTowerId;
      try {
        ourClosestTowerId = sites.stream()
            .filter(site -> site.isOurs && site.structureType == 1 && site.param1 < 400)
            .collect(Collectors.toList())
            .get(0).siteId;

      } catch (Exception e) {
        ourClosestTowerId = null;
      }
      System.err.println("closest tower id: " + ourClosestTowerId);

      //zostawienie sites które nie mają budowli
      List<Site> closestSites = sites.stream()
          .filter(site -> site.structureType == null)
          .collect(Collectors.toList());

      int closestId = 0;
      // znalezienie najbliższego miejsca
      try {
        double min = minDist(qX, qY, closestSites.get(0).x, closestSites.get(0).y);
        for (Site s : closestSites) {
          double tmp = minDist(qX, qY, s.x, s.y);
          if (tmp < min) {
            closestId = s.siteId;
            min = tmp;
          }
        }
        System.err.println("closest Id: " + closestId);  //siteId najbliższego miejsca

      } catch (Exception e) { //wszystkie sites nie puste
        closestId = 0; //brak najbliższego pustego
      }

      /*  ukrycie w kącie i atakowanie jedną wieżą
        int targetX;
        int targetY;
        if (qX < 1920 / 2) targetX = 0;
         else targetX = 1920;

        if (qY < 1000 / 2) targetY = 0;
         else targetY = 1000;

        System.out.println("MOVE "+ targetX+" "+targetY);
        System.out.println("TRAIN " + ourBarrackId);
        */


      if (ourClosestTowerId != null) {
        System.out.println("BUILD " + ourClosestTowerId + " TOWER");
      } else {
        if (ourBarrackId == null) {
          System.out.println("BUILD " + closestId + " BARRACKS-KNIGHT");
        } else {
          System.out.println("BUILD " + closestId + " TOWER");
        }
      }

      if (ourBarrackId != null) {
        System.out.println("TRAIN " + ourBarrackId);
      } else {
        System.out.println("TRAIN");
      }
    }
  }

  static double minDist(int x1, int y1, int x2, int y2) {
    int dx = x1 - x2;
    int dy = y1 - y2;
    return Math.sqrt(dx * dx + dy * dy);
  }
}