import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuGame {
    private JFrame frame;
    private SudokuBoard board;
    private Timer gameTimer;
    private JLabel timerLabel;
    private int timeLimit = 600; // 10 minutes in seconds

    public SudokuGame() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize Sudoku board
        board = new SudokuBoard();
        frame.add(board.getPanel(), BorderLayout.CENTER);

        // Initialize difficulty selection panel
        JPanel difficultyPanel = new JPanel();
        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        JComboBox<String> difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard", "Very Hard"});
        JButton startButton = new JButton("Start");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                startGame(selectedDifficulty);
            }
        });

        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultyComboBox);
        difficultyPanel.add(startButton);

        frame.add(difficultyPanel, BorderLayout.NORTH);

        // Initialize timer label
        timerLabel = new JLabel("Time left: 10:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(timerLabel, BorderLayout.SOUTH);

        // Set up frame
        frame.pack();
        frame.setVisible(true);
    }

    private void startGame(String difficulty) {
        board.setupGame(difficulty); // Setup the game based on selected difficulty

        // Initialize game timer
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--;
                updateTimerLabel();

                if (timeLimit == 0) {
                    gameTimer.stop();
                    showSkipOrSolutionDialog();
                }
            }
        });
        gameTimer.start();
    }

    private void updateTimerLabel() {
        int minutes = timeLimit / 60;
        int seconds = timeLimit % 60;
        timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));
    }

    private void showSkipOrSolutionDialog() {
        int option = JOptionPane.showOptionDialog(frame,
                "Time's up! Do you want to skip or watch the solution?",
                "Time's Up",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Skip", "Watch Solution"},
                "Skip");

        if (option == JOptionPane.YES_OPTION) {
            // Handle skip action
            JOptionPane.showMessageDialog(frame, "You chose to skip the solution.");
        } else {
            // Handle watch solution action
            board.solve(); // Solve the Sudoku board
            JOptionPane.showMessageDialog(frame, "Here is the solution:");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGame());
    }
}

class SudokuBoard {
    private JPanel panel;
    private JTextField[][] grid;
    private int[][] solution; // Store the solution for validation

    public SudokuBoard() {
        panel = new JPanel(new GridLayout(9, 9));
        grid = new JTextField[9][9];

        // Initialize Sudoku grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                panel.add(grid[i][j]);
            }
        }

        // Add borders around 3x3 blocks
        addBlockBorders();
    }

    private void addBlockBorders() {
        // Add thicker borders around 3x3 blocks
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int top = (i % 3 == 0 && i != 0) ? 2 : 1;
                int left = (j % 3 == 0 && j != 0) ? 2 : 1;
                int bottom = ((i + 1) % 3 == 0 && i != 8) ? 2 : 1;
                int right = ((j + 1) % 3 == 0 && j != 8) ? 2 : 1;

                grid[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setupGame(String difficulty) {
        // Generate Sudoku puzzle based on difficulty
        generatePuzzle(difficulty);

        // Copy the initial state for later reset
        solution = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!grid[i][j].getText().isEmpty()) {
                    solution[i][j] = Integer.parseInt(grid[i][j].getText());
                }
            }
        }
    }

    private void generatePuzzle(String difficulty) {
        // Placeholder method for generating Sudoku puzzles
        // You can implement your Sudoku generation algorithm here
        Random random = new Random();
        // Example: Generate a random starting puzzle
        // For simplicity, here we just fill a few cells randomly
        int[][] puzzle = new int[9][9];

        // Clear the grid
        clearGrid();

        // Fill some cells based on difficulty
        int numberOfCellsToFill = 0;
        switch (difficulty) {
            case "Easy":
                numberOfCellsToFill = 30; // Adjust as per your difficulty levels
                break;
            case "Medium":
                numberOfCellsToFill = 25;
                break;
            case "Hard":
                numberOfCellsToFill = 20;
                break;
            case "Very Hard":
                numberOfCellsToFill = 15;
                break;
        }

        while (numberOfCellsToFill > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            int value = random.nextInt(9) + 1; // 1-9

            if (isValidPlacement(row, col, value)) {
                grid[row][col].setText(Integer.toString(value));
                puzzle[row][col] = value;
                numberOfCellsToFill--;
            }
        }
    }

    private boolean isValidPlacement(int row, int col, int value) {
        // Placeholder method for validating cell placement
        // Check row, column, and 3x3 box constraints
        return true; // Replace with actual validation logic
    }

    private void clearGrid() {
        // Clear all text fields in the grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText("");
            }
        }
    }

    public void solve() {
        // Placeholder method for solving the Sudoku puzzle
        // For simplicity, let's just fill in the solution
        // Replace with actual solving algorithm if needed
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText(Integer.toString(solution[i][j]));
            }
        }
    }
}
