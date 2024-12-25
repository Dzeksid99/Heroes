package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        //сложность метода O(n*M), что быстрее требуемых O((WIDTH*HEIGHT)log(WIDTH*HEIGHT))

        // Создаем матрицу поля
        int[][] field = new int[WIDTH][HEIGHT];

        // Заполняем препятствия O(k), где k - количество юнитов
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                field[unit.getxCoordinate()][unit.getyCoordinate()] = -1;
            }
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        // Используем Queue для волнового алгоритма
        Queue<Edge> queue = new LinkedList<>();
        queue.offer(new Edge(startX, startY));
        field[startX][startY] = 1;


        //Сложность BFS O(n*m)
        boolean targetFound = false;
        while (!queue.isEmpty() && !targetFound) {
            Edge current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && field[newX][newY] == 0) {
                    field[newX][newY] = field[x][y] + 1;
                    queue.offer(new Edge(newX, newY));

                    if (newX == targetX && newY == targetY) {
                        targetFound = true;
                        break;
                    }
                }
            }
        }

        // Если путь не найден
        if (field[targetX][targetY] == 0) {
            return new ArrayList<>();
        }

        // Восстанавливаем путь
        return reconstructPath(field, startX, startY, targetX, targetY);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    //Восстановление быстрее, чем BFS
    private List<Edge> reconstructPath(int[][] field, int startX, int startY, int targetX, int targetY) {
        List<Edge> path = new ArrayList<>();
        int x = targetX;
        int y = targetY;

        while (!(x == startX && y == startY)) {
            path.addFirst(new Edge(x, y));

            // Ищем следующую клетку с меньшим значением
            int currentValue = field[x][y];
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && field[newX][newY] > 0 && field[newX][newY] < currentValue) {
                    x = newX;
                    y = newY;
                    break;
                }
            }
        }

        path.addFirst(new Edge(startX, startY));
        return path;
    }
}