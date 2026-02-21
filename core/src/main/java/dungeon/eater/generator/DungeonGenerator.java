package dungeon.eater.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;

public class DungeonGenerator {
    private final int minRoomSize = 5;
    private final int maxIterations = 4;

    @Getter
    private Array<Room> rooms;
    @Getter
    private Array<Room> corridors;

    public static class Room {
        public int x, y, width, height;

        public Room (int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private static class BSPNode {
        int x, y, width, height;
        BSPNode left;
        BSPNode right;
        Room room;

        BSPNode (int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public DungeonGenerator () {
        this.rooms = new Array<>();
        this.corridors = new Array<>();
    }

    public void generate () {
        rooms.clear();
        corridors.clear();
        BSPNode root = new BSPNode(0, 0, 50, 50);
        split(root, 0);
        createRooms(root);
        createCorridors(root);
    }

    private void split (BSPNode node, int iteration) {
        if (iteration >= maxIterations) return;

        boolean splitH = MathUtils.randomBoolean();
        if (node.width > node.height && node.width / node.height >= 1.25)
            splitH = false;
        else if (node.height > node.width && node.height / node.width >= 1.25)
            splitH = true;

        int max = (splitH ? node.height : node.width) - minRoomSize;
        if (max <= minRoomSize) return;

        int split = MathUtils.random(minRoomSize, max);

        if (splitH) {
            node.left = new BSPNode(node.x, node.y, node.width, split);
            node.right = new BSPNode(node.x, node.y + split, node.width, node.height - split);
        } else {
            node.left = new BSPNode(node.x, node.y, split, node.height);
            node.right = new BSPNode(node.x + split, node.y, node.width - split, node.height);
        }

        split(node.left, iteration + 1);
        split(node.right, iteration + 1);
    }

    private void createRooms (BSPNode node) {
        if (node.left != null || node.right != null) {
            if (node.left != null) createRooms(node.left);
            if (node.right != null) createRooms(node.right);
            return;
        }

        // Ensure minimum dimensions
        int maxWidth = Math.max(minRoomSize, node.width - 2);
        int maxHeight = Math.max(minRoomSize, node.height - 2);

        int roomWidth = MathUtils.random(minRoomSize, maxWidth);
        int roomHeight = MathUtils.random(minRoomSize, maxHeight);

        // Ensure positive ranges for position calculation
        int xRange = Math.max(1, node.width - roomWidth);
        int yRange = Math.max(1, node.height - roomHeight);

        int roomX = node.x + MathUtils.random(0, xRange);
        int roomY = node.y + MathUtils.random(0, yRange);

        node.room = new Room(roomX, roomY, roomWidth, roomHeight);
        rooms.add(node.room);
    }

    private void createCorridors (BSPNode node) {
        if (node.left != null && node.right != null) {
            Room leftRoom = getRoomFromNode(node.left);
            Room rightRoom = getRoomFromNode(node.right);

            connectRooms(leftRoom, rightRoom);

            createCorridors(node.left);
            createCorridors(node.right);
        }
    }

    private Room getRoomFromNode (BSPNode node) {
        if (node.room != null) return node.room;

        Room leftRoom = node.left != null ? getRoomFromNode(node.left) : null;
        Room rightRoom = node.right != null ? getRoomFromNode(node.right) : null;

        return leftRoom != null ? leftRoom : rightRoom;
    }

    private void connectRooms (Room room1, Room room2) {
        int x1 = room1.x + room1.width / 2;
        int y1 = room1.y + room1.height / 2;
        int x2 = room2.x + room2.width / 2;
        int y2 = room2.y + room2.height / 2;

        if (MathUtils.randomBoolean()) {
            createHorizontalCorridor(x1, x2, y1);
            createVerticalCorridor(y1, y2, x2);
        } else {
            createVerticalCorridor(y1, y2, x1);
            createHorizontalCorridor(x1, x2, y2);
        }
    }

    private void createHorizontalCorridor (int x1, int x2, int y) {
        int start = Math.min(x1, x2);
        int end = Math.max(x1, x2);
        corridors.add(new Room(start, y, end - start + 1, 1));
    }

    private void createVerticalCorridor (int y1, int y2, int x) {
        int start = Math.min(y1, y2);
        int end = Math.max(y1, y2);
        corridors.add(new Room(x, start, 1, end - start + 1));
    }
}
