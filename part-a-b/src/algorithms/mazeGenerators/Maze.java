package algorithms.mazeGenerators;

public class Maze implements java.io.Serializable{

    /**
     * Represents a 2D maze where 1 represents a wall and 0 represents an empty path.
     */
    private int[][] map;
    private Position startPosition;
    private Position goalPosition;

    /**
     * Main constructor - initializes the maze with a pre-built map and start/goal positions.
     *
     * @param map A 2D integer array representing the maze layout (1 = wall, 0 = path).
     * @param startPosition The starting position in the maze.
     * @param goalPosition The goal (exit) position in the maze.
     */
    public Maze(int[][] map, Position startPosition, Position goalPosition) {
        this.map = map;
        this.startPosition = startPosition;
        this.goalPosition = goalPosition;
    }

    /**
     * Constructs a Maze object from a given byte array.
     * The byte array contains a 12-byte header with the maze dimensions
     * and the start and goal positions, followed by the actual maze layout data.
     *
     * @param bytes A byte array representing the compressed metadata and layout of the maze.
     */
    public Maze(byte[] bytes) {
        // Read rows and cols (each stored as 2 bytes)
        int rows = ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
        int cols = ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);

        // Read start position
        int startRow = ((bytes[4] & 0xFF) << 8) | (bytes[5] & 0xFF);
        int startCol = ((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF);

        // Read goal position
        int goalRow = ((bytes[8] & 0xFF) << 8) | (bytes[9] & 0xFF);
        int goalCol = ((bytes[10] & 0xFF) << 8) | (bytes[11] & 0xFF);

        // Build the maze map
        this.map = new int[rows][cols];
        int index = 12; // maze data starts at byte 12
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.map[i][j] = bytes[index] & 0xFF;
                index++;
            }
        }

        this.startPosition = new Position(startRow, startCol);
        this.goalPosition = new Position(goalRow, goalCol);
    }

    /**
     * @return The starting position of the maze.
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * @return The goal position of the maze.
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

    /**
     * @return The total number of rows in the maze.
     */
    public int getRows() {
        return map.length;
    }

    /**
     * @return The total number of columns in the maze.
     */
    public int getCols() {
        return map[0].length;
    }

    /**
     * Returns the value at a specific cell (0 = path, 1 = wall).
     *
     * @param row The row index.
     * @param col The column index.
     * @return The integer value of the cell.
     */
    public int getCellValue(int row, int col) {
        return map[row][col];
    }

    /**
     * Prints the maze to the console.
     * The start position is marked with 'S', and the goal position is marked with 'E'.
     */
    public void print() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == startPosition.getRowIndex() &&
                        j == startPosition.getColumnIndex())
                    System.out.print("S ");
                else if (i == goalPosition.getRowIndex() &&
                        j == goalPosition.getColumnIndex())
                    System.out.print("E ");
                else
                    System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    /**
     * Converts the current Maze object into a 1D byte array.
     * The first 12 bytes serve as a header (metadata) storing the rows, columns,
     * start position, and goal position. The remaining bytes store the maze grid data.
     *
     * @return A byte array representation of the maze, ready for compression or transmission.
     */
    public byte[] toByteArray() {
        int rows = map.length;
        int cols = map[0].length;

        // 12 bytes for metadata + rows*cols bytes for maze data
        byte[] result = new byte[12 + rows * cols];

        // Store rows as 2 bytes
        result[0] = (byte) ((rows >> 8) & 0xFF);
        result[1] = (byte) (rows & 0xFF);

        // Store cols as 2 bytes
        result[2] = (byte) ((cols >> 8) & 0xFF);
        result[3] = (byte) (cols & 0xFF);

        // Store start position as 2+2 bytes
        result[4] = (byte) ((startPosition.getRowIndex() >> 8) & 0xFF);
        result[5] = (byte) (startPosition.getRowIndex() & 0xFF);
        result[6] = (byte) ((startPosition.getColumnIndex() >> 8) & 0xFF);
        result[7] = (byte) (startPosition.getColumnIndex() & 0xFF);

        // Store goal position as 2+2 bytes
        result[8] = (byte) ((goalPosition.getRowIndex() >> 8) & 0xFF);
        result[9] = (byte) (goalPosition.getRowIndex() & 0xFF);
        result[10] = (byte) ((goalPosition.getColumnIndex() >> 8) & 0xFF);
        result[11] = (byte) (goalPosition.getColumnIndex() & 0xFF);

        // Store maze data
        int index = 12;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[index] = (byte) map[i][j];
                index++;
            }
        }

        return result;
    }

    /**
     * Two mazes are equal if they have the same dimensions,
     * start/goal positions, and identical grid content.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Maze)) return false;
        Maze other = (Maze) obj;
        return java.util.Arrays.deepEquals(this.map, other.map)
                && this.startPosition.getRowIndex() == other.startPosition.getRowIndex()
                && this.startPosition.getColumnIndex() == other.startPosition.getColumnIndex()
                && this.goalPosition.getRowIndex() == other.goalPosition.getRowIndex()
                && this.goalPosition.getColumnIndex() == other.goalPosition.getColumnIndex();
    }

    /**
     * Hash is based on the maze grid content and start/goal positions,
     * ensuring the same maze always maps to the same cache file.
     */
    @Override
    public int hashCode() {
        return java.util.Arrays.deepHashCode(map)
                + 31 * startPosition.getRowIndex()
                + 31 * startPosition.getColumnIndex()
                + 31 * goalPosition.getRowIndex()
                + 31 * goalPosition.getColumnIndex();
    }
}