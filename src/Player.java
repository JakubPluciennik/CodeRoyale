import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/


class Queen {
  final int x;
  final int y;
  final int health;

  public Queen(int x, int y, int health) {
    this.x = x;
    this.y = y;
    this.health = health;
  }
}

class Site {
  final int id;
  final int x;
  final int y;
  final int radius;
  int goldRemaining = 0;
  int maxMineSize = 0;

  Integer structureType;
  Boolean isOurs = true;
  int param1;
  int param2;

  boolean isAccessible = true;
  int Atimer = 0;


  @Override
  public String toString() {
    return "Site{ sID: " + id + "| Our: " + isOurs + "| x: " + x + "| y: " + y + "| r: " + radius + "| type: " + structureType + "| p1: " + param1 +
        "| p2: " + param2 + "| rG: " + goldRemaining + "| Access: " + isAccessible + "| At: " + Atimer + "}\n";
  }

  public Site(int id, int x, int y, int radius) {
    this.id = id;
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
  static Integer startingX;
  static Integer startingY;

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
      int gold = in.nextInt();  //obecna wartość złota
      int touchedSite = in.nextInt(); // -1 if none

      int ourIncome = 0;  //przychód z kopalni
      int numberOfEnemyTowers = 0; //liczba wrogich wież
      int numberOfFriendlyTowers = 0; //liczba przyjaznych wież
      boolean enemyBarrack = false;
      int numberOfFriendlyMines = 0;
      List<Site> ourKnightBarrack = new ArrayList<>();  //lista z barakami tworzących rycerzy
      List<Site> ourGiantBarrack = new ArrayList<>();  //lista baraków tworzących giganty
      List<Site> ourArcherBarrack = new ArrayList<>();  //lista z barakami tworzących rycerzy
      Queen q = null;

      for (int i = 0; i < numSites; i++) {
        int siteId = in.nextInt();
        int goldRemaining = in.nextInt(); // -1 if unknown
        int maxMineSize = in.nextInt(); // -1 if unknown
        int structureType = in.nextInt(); // -1 = No structure, 0 = Goldmine, 1 = Tower, 2 = Barracks
        int owner = in.nextInt(); // -1 = No structure, 0 = Friendly, 1 = Enemy
        int param1 = in.nextInt();
        int param2 = in.nextInt();

        //aktualizacja danych w obiektach
        sites.stream()
            .filter(site -> site.id == siteId)
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

              if (!site.isAccessible) {
                if (site.Atimer > 0) {
                  site.Atimer--;
                } else {
                  site.isAccessible = true;
                }
              }
            });

        if (structureType == 0 && owner == 0) { //dochód z kopalni
          ourIncome += param1;
          numberOfFriendlyMines++;
        }
        if (structureType == 2 && owner == 0) {  //lista z barakami
          switch (param2) {
            case 0:
              ourKnightBarrack.addAll(sites.stream()
                  .filter(site -> site.id == siteId)
                  .collect(Collectors.toList()));
              break;
            case 1:
              ourArcherBarrack.addAll(sites.stream()
                  .filter(site -> site.id == siteId)
                  .collect(Collectors.toList()));
              break;
            case 2:
              ourGiantBarrack.addAll(sites.stream()
                  .filter(site -> site.id == siteId)
                  .collect(Collectors.toList()));
              break;
          }
        }
        if (owner == 1 && structureType == 2) {
          enemyBarrack = true;
        }
        if (structureType == 1) { //liczba wież
          if (owner == 1) {
            numberOfEnemyTowers++;
          }
          if (owner == 0) {
            numberOfFriendlyTowers++;
          }
        }
      }
      int numUnits = in.nextInt();

      int giantUnits = 0;
      int archerUnits = 0;
      int enemyUnits = 0;
      int knightUnits = 0;
      List<Unit> units = new ArrayList<>();
      for (int i = 0; i < numUnits; i++) {
        int x = in.nextInt();
        int y = in.nextInt();
        int owner = in.nextInt();
        int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER, 2 = GIANT
        int health = in.nextInt();

        if (owner == 0) {
          switch (unitType) {
            case -1: //przypisanie danych królowej
              if (startingX == null && startingY == null) {
                startingX = x;
                startingY = y;
              }
              q = new Queen(x, y, health);
              break;
            case 1:
              archerUnits++;
              break;
            case 2:
              giantUnits++;
              break;
            default:
              knightUnits++;
              break;
          }
        }
        if (owner == 1) { //wszystkie wrogie jednostki
          enemyUnits++;
        }
        units.add(new Unit(x, y, owner == 0, unitType, health));
      }

      //lista baraków z rycerzami
      System.err.println("barracks: " + ourKnightBarrack);
      //----------------------------------------DECYZJA----------------------------------------

      String action = "WAIT";

      try {
        if (q != null) {
          action = queenAction(sites, units, q, ourKnightBarrack, ourArcherBarrack, ourIncome, numberOfEnemyTowers, numberOfFriendlyTowers,
              ourGiantBarrack, knightUnits, enemyUnits, gold, touchedSite, numberOfFriendlyMines, archerUnits, enemyBarrack);
        }
      } catch (Exception e) {
        action = "WAIT";
      }


      String train = "TRAIN";
      Site readyBarrack = readyBarrack(sites);

      if (numberOfEnemyTowers > 2 && !ourGiantBarrack.isEmpty() && giantUnits == 0) {
        train += " " + ourGiantBarrack.get(0).id;
      } else if (readyBarrack != null && readyBarrack.param2 == 0) {
        train += " " + readyBarrack.id;
      }

      System.out.println(action);
      System.out.println(train);
      //---------------------------------------------------------------------------------------
    }

  }

  static Integer closestSiteId(List<Site> sites, final int x, final int y) {
    if (sites.isEmpty()) {
      return null;
    }
    int closest = sites.get(0).id;

    if (sites.size() == 1) {
      return closest;
    }
    double min = dist(x, y, sites.get(0).x, sites.get(0).y);

    for (Site s : sites) {
      double tmp = dist(x, y, s.x, s.y);
      if (tmp < min) {
        closest = s.id;
        min = tmp;
      }
    }
    return closest;
  }

  static Site closestNullIdMethod(List<Site> sites, final int x, final int y) {
    //zostawienie sites które nie mają budowli
    List<Site> closestSites = sites.stream()
        .filter(site -> site.isAccessible && site.structureType == null)
        .collect(Collectors.toList());

    // znalezienie najbliższego miejsca
    return closestSites.stream()
        .filter(site -> site.id == closestSiteId(closestSites, x, y))
        .collect(Collectors.toList())
        .get(0);
  }

  private static Site closestTowerIdMethod(List<Site> sites, final int x, final int y, boolean isOurs) {
    try {
      List<Site> towerSites = sites.stream()
          .filter(site -> site.isAccessible && site.structureType != null && site.structureType == 1 && site.isOurs == isOurs)
          .collect(Collectors.toList());
      return towerSites.stream()
          .filter(site -> site.id == closestSiteId(towerSites, x, y))
          .collect(Collectors.toList())
          .get(0);
    } catch (Exception e) {
      return null;
    }
  }

  static Site closestEnemyTowerMethod(List<Site> sites, Queen q) { //id wieży, dla której królowa jest w polu rażenia
    try {
      List<Site> enemyTowerSites = sites.stream()
          .filter(site -> site.structureType != null && site.structureType == 1 && !site.isOurs) //wrogie wieże
          .filter(site -> site.param2 + 50 > dist(q.x, q.y, site.x, site.y)) //wrogie wieże które mają zasięg rażenia + 90 większy od dystansu do nich
          .collect(Collectors.toList());

      double max = Math.abs(enemyTowerSites.get(0).param2 - dist(q.x, q.y, enemyTowerSites.get(0).x, enemyTowerSites.get(0).y));
      int towerId = enemyTowerSites.get(0).id;
      for (int i = 1; i < enemyTowerSites.size(); i++) {
        double tmp = Math.abs(enemyTowerSites.get(i).param2 - dist(q.x, q.y, enemyTowerSites.get(i).x, enemyTowerSites.get(i).y));
        if (tmp > max) {
          max = tmp;
          towerId = enemyTowerSites.get(i).id;
        }
      }
      final int finaltowerId = towerId; //Id wieży wroga o zasięgu
      return sites.stream() //obiekt wieży w zasięgu
          .filter(site -> site.id == finaltowerId)
          .collect(Collectors.toList())
          .get(0);
    } catch (Exception e) {
      return null;
    }
  }

  private static boolean inDangerMethod(List<Unit> units, Queen q) {
    int dangerZone = (400 - q.health * 4) + 100;
    return units.stream()
        .anyMatch(unit -> !unit.isMine && dist(q.x, q.y, unit.x, unit.y) < dangerZone);
  }

  static Integer ourGrowMineIdMethod(List<Site> sites, final int x, final int y) {
    try {
      List<Site> tmp = sites.stream()
          .filter(site -> site.isOurs && site.isAccessible && site.structureType == 0 && site.param1 < site.maxMineSize &&
              dist(x, y, site.x, site.y) < 300)
          .collect(Collectors.toList());
      return closestSiteId(tmp, x, y);
    } catch (Exception e) {
      return null;
    }
  }

  private static Integer closestMineCandidateId(List<Site> sites, final int x, final int y) {
    List<Site> mineCandidates = sites.stream()
        .filter(site -> site.isAccessible && site.structureType == null && site.goldRemaining > 0)
        .collect(Collectors.toList());
    System.err.println(mineCandidates);

    return closestSiteId(mineCandidates, x, y);
  }

  static Integer ourGrowTowerIdMethod(List<Site> sites, final int x, final int y) {
    try {
      List<Site> tmp = sites.stream()
          .filter(site -> site.isOurs && site.isAccessible && site.structureType == 1 && site.param1 < 500 && dist(x, y, site.x, site.y) < 300)
          .collect(Collectors.toList());

      return closestSiteId(tmp, x, y);
    } catch (Exception e) {
      return null;
    }
  }

  static Site readyBarrack(List<Site> sites) {
    List<Site> readyBarrackSites = sites.stream()
        .filter(site -> site.isOurs && site.structureType == 2 && site.param2 == 0)
        .collect(Collectors.toList());
    if (!readyBarrackSites.isEmpty()) {
      return readyBarrackSites.get(0);
    } else {
      return null;
    }
  }

  static Site closestAnyMethod(List<Site> sites, final int x, final int y) {
    try {
      List<Site> closestNonBarrack = sites.stream()
          .filter(site -> site.structureType == null || (site.structureType == 0))
          .collect(Collectors.toList());
      return closestNonBarrack.stream()
          .filter(site -> site.id == closestSiteId(closestNonBarrack, x, y))
          .collect(Collectors.toList())
          .get(0);
    } catch (Exception e) {
      return null;
    }
  }

  static double dist(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  private static Unit dangerousEnemyCreepMethod(List<Unit> units, Queen q) {
    int dangerZone = (700 - q.health * 7) + 200;
    try {
      return units.stream()
          .filter(unit -> !unit.isMine && dist(q.x, q.y, unit.x, unit.y) < dangerZone)
          .collect(Collectors.toList())
          .get(0);
    } catch (Exception e) {
      return null;
    }

  }

  static String queenAction(List<Site> sites, List<Unit> units, Queen q, List<Site> ourKnightBarrack, List<Site> ourArcherBarrack, int ourIncome,
                            int numberOfEnemyTowers, int numberOfFriendlyTowers, List<Site> ourGiantBarrack, int friendlyUnits, int enemyUnits,
                            int gold, int touchedSite, int numberOfFriendlyMines, int archerUnits, boolean enemyBarrack) {
    //rozwijanie wieży
    Integer ourGrowTowerId = ourGrowTowerIdMethod(sites, q.x, q.y);

    //rozwijanie kopalni
    Integer ourGrowMineId = ourGrowMineIdMethod(sites, q.x, q.y);
    Site closest = null;
    //najbliższe puste miejsce
    if (closestNullIdMethod(sites, q.x, q.y) != null) {
      closest = closestNullIdMethod(sites, q.x, q.y);
    }

    Site closestAny = closestAnyMethod(sites, q.x, q.y);

    //System.err.println("closest id: " + closest.id);

    //jeśli rycerze wroga blisko, to w niebezpieczeństwie
    boolean inDanger = inDangerMethod(units, q);

    //jednostka wroga w niebezpiecznym polu
    Unit dangerousEnemyCreep = dangerousEnemyCreepMethod(units, q);

    //najbliższa przyjazna wieża
    Site ourClosestTower = closestTowerIdMethod(sites, q.x, q.y, true);

    //wroga wieża w zasięgu
    Site enemyClosestTower = closestEnemyTowerMethod(sites, q);

    Integer mineCandidate = closestMineCandidateId(sites, q.x, q.y);

    //-----LOGIKA GRY-----

    int startingTargetY = startingY + 500;
    if (startingY > 500) {
      startingTargetY = startingY - 500;
    }
    int targetIncome = 10;
    if (q.health < 50) {
      targetIncome = 5;
    }
    if (closest == null) {
      if (ourGrowTowerId != null) {
        return "BUILD " + ourGrowTowerId + " TOWER";
      } else {
        return "MOVE " + startingX + " " + startingTargetY;
      }
    }
    if (ourGrowTowerId != null && ourClosestTower != null && dist(q.x, q.y, ourClosestTower.x, ourClosestTower.y) < 120) {
      if (ourClosestTower.id == ourGrowTowerId) {
        return "BUILD " + ourClosestTower.id + " TOWER";
      }
    }
    System.err.println("enemy barrack: " + enemyBarrack);
    if (gold > 160 && ourKnightBarrack.isEmpty()) {
      return "BUILD " + closest.id + " BARRACKS-KNIGHT";
    }
    if (dangerousEnemyCreep != null) {
      if (ourKnightBarrack.isEmpty()) {
        return "BUILD " + closestSiteId(sites, q.x, q.y) + " BARRACKS-KNIGHT";
      }
      if (ourGrowTowerId != null) {
        return "BUILD " + ourGrowTowerId + " TOWER";
      }
      if (numberOfFriendlyTowers <= 3) {
        if (dist(q.x, q.y, closest.x, closest.y) < closest.radius + 90) {
          return "BUILD " + closest.id + " TOWER";
        }
        if (dist(startingX, startingTargetY, closest.x, closest.y) < 200) {
          return "BUILD " + closest.id + " TOWER";
        }
        if (ourClosestTower != null && ourClosestTower.param2 < 500 && dist(startingX, startingTargetY, ourClosestTower.x, ourClosestTower.y) < 200) {
          return "BUILD " + ourClosestTower.id + " TOWER";
        }
        return "MOVE " + startingX + " " + startingTargetY;
      }

      if (dist(q.x, q.y, closest.x, closest.y) < closest.radius + 70) {
        return "BUILD " + closest.id + " TOWER";
      }
      return "MOVE " + startingX + " " + startingTargetY;
    }
    if (numberOfEnemyTowers > 3 && ourGiantBarrack.isEmpty()) {
      return "BUILD " + closest.id + " BARRACKS-GIANT";
    }
    if (ourIncome <= targetIncome && ourGrowMineId != null) {
      return "BUILD " + ourGrowMineId + " MINE";
    }
    if (ourIncome <= targetIncome && mineCandidate != null) {
      return "BUILD " + mineCandidate + " MINE";
    }
    if (enemyClosestTower != null) {
      int dx = q.x - enemyClosestTower.x;
      int dy = q.y - enemyClosestTower.y;

      int targetX = q.x + dx * 10;
      int targetY = q.y + dy * 10;

      if (dist(q.x, q.y, closest.x, closest.y) < closest.radius + 90) {
        return "BUILD " + closest.id + " TOWER";
      }
      closest.isAccessible = false;
      closest.Atimer = 15;
      return "MOVE " + targetX + " " + targetY;
    }
    if (ourGrowTowerId != null) {
      return "BUILD " + ourGrowTowerId + " TOWER";
    }
    if (ourGrowMineId != null) {
      return "BUILD " + ourGrowMineId + " MINE";
    }
    if (ourKnightBarrack.isEmpty()) {
      return "BUILD " + closest.id + " BARRACKS-KNIGHT";
    }
    return "BUILD " + closest.id + " TOWER";
  }
}
