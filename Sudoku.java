
/**
 *
 * @author Tariqul Islam,IIT,JU
 */
import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components

/**
 * The Sudoku game solver. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class Sudoku extends JFrame {
	// Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

	// Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
	// Board width/height in pixels
	public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
	private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

	private int[][] puzzle = new int[9][9];
	JButton bt, reset, edit, exit;
	Container cp;
	public Sudoku() {
		cp = getContentPane();
		cp.setLayout(new GridLayout(GRID_SIZE + 1, GRID_SIZE));
		InputListener listener = new InputListener();
		bt = new JButton("SLV");
		reset = new JButton("RST");
		edit = new JButton("EDT");
		exit = new JButton("EXIT");
		bt.addActionListener(listener);
		bt.addActionListener(listener);
		reset.addActionListener(listener);
		edit.addActionListener(listener);
		exit.addActionListener(listener);
		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				tfCells[row][col] = new JTextField(); // Allocate element of
														// array
				//if(col==3 ||col==6)add a bar
				cp.add(tfCells[row][col]);
				tfCells[row][col].setText(""); // set to empty string
				tfCells[row][col].setEditable(true);
				tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
				tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
				tfCells[row][col].setFont(FONT_NUMBERS);
			}
			//if(row==3||row==6) add a horizontal line line
		}
		cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		pack();
		add(bt);
		add(edit);
		add(reset);
		add(exit);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		setVisible(true);

	}
	
	public static void main(String[] args) {
		new Sudoku();
		System.out.println("return");
	}
	boolean disable(int r, int c, boolean l[]) {
		int i, j;
		int x[] = new int[10];
		for (i = 1; i <= 9; i++)
			x[i] = 0;
		for (i = (r / 3) * 3; i < (r / 3) * 3 + 3; i++) {
			for (j = (c / 3) * 3; j < (c / 3) * 3 + 3; j++) {
				if (puzzle[i][j] != 0) {
					x[puzzle[i][j]]++;
					l[puzzle[i][j]] = false;
					if (x[puzzle[i][j]] == 2)
						return false;
				}
			}
		}
		for (i = 1; i <= 9; i++)
			x[i] = 0;
		for (i = 0; i < 9; i++) {
			if (puzzle[i][c] != 0) {
				x[puzzle[i][c]]++;
				l[puzzle[i][c]] = false;
				if (x[puzzle[i][c]] == 2)
					return false;
			}
		}
		for (i = 1; i <= 9; i++)
			x[i] = 0;
		for (i = 0; i < 9; i++) {
			if (puzzle[r][i] != 0) {
				x[puzzle[r][i]]++;
				l[puzzle[r][i]] = false;
				if (x[puzzle[r][i]] == 2)
					return false;
			}
		}
		return true;
	}

	boolean done = false;

	void solve(int r, int c)  {
		 //wait(1000);

		if (r == 9) {
			done = true;
			return;
		}

		if (puzzle[r][c] != 0) {
			if (c < 8)
				solve(r, c + 1);
			else
				solve(r + 1, 0);
			return;
		}
		boolean p[] = new boolean[10];
		for (int i = 1; i <= 9; i++)
			p[i] = true;
		if (!disable(r, c, p))
			return;
		for (int k = 1; k <= 9; k++) {
			if (p[k]) {
				puzzle[r][c] = k;
				 
				tfCells[r][c].setText(k + "");
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (c < 8)
					solve(r, c + 1);
				else
					solve(r + 1, 0);
			}
			if (done)
				break;
		}
		if (!done) {
			puzzle[r][c] = 0;
			tfCells[r][c].setText("");
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	private class InputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			if (src == bt) {
				done = false;
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (tfCells[row][col].getText().length() != 0) {
							puzzle[row][col] = Integer.parseInt(tfCells[row][col].getText());
						}

						else {
							puzzle[row][col] = 0;

							tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
							tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
						}
						tfCells[row][col].setEditable(false);
					}

				}

				solve(0, 0);
			} else if (src == edit) {
				// TODO make sudoku editable
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						tfCells[row][col].setEditable(true);
						tfCells[row][col].setEditable(true);
						tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
						tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
						tfCells[row][col].setFont(FONT_NUMBERS);
					}

				}

			} else if (src == reset) {
				// TODO reset the board make all editable and blank
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						puzzle[row][col] = 0;
						tfCells[row][col].setText(""); // set to empty string
						// tfCells[row][col].addActionListener(listener);
						tfCells[row][col].setEditable(true);
						tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
						tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
						tfCells[row][col].setFont(FONT_NUMBERS);
					}
				}
			} else if (src == exit) {
				System.exit(0);
			}

		}



	}
}