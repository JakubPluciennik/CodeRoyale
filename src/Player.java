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
  int goldRemaining = 0;
  int maxMineSize = 0;

  Integer structureType;
  Boolean isOurs = true;
  int param1;
  int param2;


  @Override
  public String toString() {
    return "Site{ siteId: " + siteId + "| isOurs: " + isOurs + "| x: " + x + "| y: " + y + "| radius: " + radius + "| type: " + structureType +
        "| param1: " + param1 + "}\n";
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
        int goldRemaining = in.nextInt(); // -1 if unknown
        int maxMineSize = in.nextInt(); // -1 if unknown
        int structureType = in.nextInt(); // -1 = No structure, 0 = Goldmine, 1 = Tower, 2 = Barracks
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
              site.goldRemaining = goldRemaining;
              site.maxMineSize = maxMineSize;

              site.isOurs = owner == 0;
              site.param1 = param1;
              site.param2 = param2;
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

        //współrzędne tymczasowe naszej królowej
        if (owner == 0 && unitType == -1) {
          tmpX = x;
          tmpY = y;
        }
        units.add(new Unit(x, y, owner == 0, unitType, health));
      }

      //współrzędnie finalne królowej
      final int qY = tmpY;
      final int qX = tmpX;

      //id baraku
      Integer ourBarrackId = ourBarrackIdMethod(sites);
      System.err.println("barrack id: " + ourBarrackId);

      //rozwijanie wieży
      Integer ourGrowTowerId = ourGrowTowerIdMethod(sites, qX, qY);
      System.err.println("growing tower id: " + ourGrowTowerId);

      //rozwijanie kopalni
      Integer ourGrowMineId = ourGrowMineIdMethod(sites, qX, qY);
      System.err.println("growing mine id: " + ourGrowMineId);

      //najbliższe puste miejsce
      int closestId = closestNullIdMethod(sites, qX, qY);
      System.err.println("closest id: " + closestId);

      //dochód z kopalni
      int ourIncome = ourIncomeMethod(sites);
      System.err.println("income: " + ourIncome);

      System.out.println(getQueenAction(ourGrowTowerId, ourBarrackId, closestId, ourIncome, ourGrowMineId));

      if (ourBarrackId != null) {
        System.out.println("TRAIN " + ourBarrackId);
      } else {
        System.out.println("TRAIN");
      }
    }
  }

  private static int ourIncomeMethod(List<Site> sites) {
    int ourIncome = 0;
    try {
      List<Site> incomeSites = sites.stream()
          .filter(site -> site.isOurs && site.structureType == 0)
          .collect(Collectors.toList());
      for (Site s : incomeSites) {
        ourIncome += s.param1;
      }
      return ourIncome;
    } catch (Exception e) {
      return 0;
    }
  }

  static int closestNullIdMethod(List<Site> sites, int qX, int qY) {
    //zostawienie sites które nie mają budowli
    List<Site> closestSites = sites.stream()
        .filter(site -> site.structureType == null)
        .collect(Collectors.toList());

    // znalezienie najbliższego miejsca
    try {
      return closestSiteId(closestSites, qX, qY);
    } catch (Exception e) { //wszystkie sites nie puste
      return 0; //brak najbliższego pustego
    }
  }

  static int closestSiteId(List<Site> sites, int qX, int qY) throws Exception {
    int closest = sites.get(0).siteId;
    double min = dist(qX, qY, sites.get(0).x, sites.get(0).y);
    for (Site s : sites) {
      double tmp = dist(qX, qY, s.x, s.y);
      if (tmp < min) {
        closest = s.siteId;
        min = tmp;
      }
    }
    return closest;
  }

  static Integer ourGrowMineIdMethod(List<Site> sites, int qX, int qY) {
    try {
      List<Site> tmp = sites.stream()
          .filter(site -> site.isOurs && site.structureType == 0 && site.param1 < site.maxMineSize && dist(qX, qY, site.x, site.y) < 300)
          .collect(Collectors.toList());
      return closestSiteId(tmp, qX, qY);
    } catch (Exception e) {
      return null;
    }
  }

  static Integer ourBarrackIdMethod(List<Site> sites) {
    try {
      return sites.stream()
          .filter(site -> site.isOurs && site.structureType == 2)
          .collect(Collectors.toList())
          .get(0).siteId;
    } catch (Exception e) {
      return null;
    }
  }

  static Integer ourGrowTowerIdMethod(List<Site> sites, int qX, int qY) {
    try {
      List<Site> tmp = sites.stream()
          .filter(site -> site.isOurs && site.structureType == 1 && site.param1 < 350 && dist(qX, qY, site.x, site.y) < 300)
          .collect(Collectors.toList());

      return closestSiteId(tmp, qX, qY);
    } catch (Exception e) {
      return null;
    }
  }

  static double dist(int x1, int y1, int x2, int y2) {
    int dx = x1 - x2;
    int dy = y1 - y2;
    return Math.sqrt(dx * dx + dy * dy);
  }

  static String getQueenAction(Integer ourGrowTowerId, Integer ourBarrackId, int closestId, Integer ourIncome, Integer ourGrowMineId) {
    if (ourGrowTowerId != null) {
      return "BUILD " + ourGrowTowerId + " TOWER";
    }
    if (ourGrowMineId != null) {
      return "BUILD " + ourGrowMineId + " MINE";
    }
    if (ourIncome < 5) {
      return "BUILD " + closestId + " MINE";
    }
    if (ourBarrackId == null) {
      return "BUILD " + closestId + " BARRACKS-KNIGHT";
    }
    return "BUILD " + closestId + " TOWER";
  }
}
