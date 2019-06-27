import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * 
 * @author Chanil Park
 */
public class Main {
	private static final int SEARCH_COLS = 20;
	private static final int EDITOR_ROWS = 30;
	private static final int EDITOR_COLS = 60;
	private static final int LAYOUT_GAP = 5;

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	private JFrame frame;
	private JFileChooser fileChooser;

	private JTextField searchField;
	private JTextArea textEditor;

	public Main() {
		initialise();
	}

	/**
	 * Initialize GUI.
	 */
	private void initialise() {
		fileChooser = new JFileChooser();

		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setDialogTitle("Select input file.");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					textEditor.setText(readFile(file));
				}
			}
		});

		searchField = new JTextField(SEARCH_COLS);
		searchField.setMaximumSize(new Dimension(0, 25));
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pattern = searchField.getText();
				String text = textEditor.getText();
				KMP kMPInstance = new KMP();
				kMPInstance.BFsearch(pattern, text);
				int index = kMPInstance.search(pattern, text);
				
				if (index == -1) {
					JOptionPane.showMessageDialog(frame, "Pattern not found.");
				} else {
					textEditor.requestFocus();
					textEditor.setSelectionStart(index);
					textEditor.setSelectionEnd(index + pattern.length());
					textEditor.setSelectionColor(Color.YELLOW);
				}
			}
		});

		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

		Border edge = BorderFactory.createEmptyBorder(LAYOUT_GAP, LAYOUT_GAP, LAYOUT_GAP, LAYOUT_GAP);
		controls.setBorder(edge);

		controls.add(load);
		controls.add(Box.createHorizontalGlue());
		controls.add(new JLabel("Search"));
		controls.add(Box.createRigidArea(new Dimension(LAYOUT_GAP, 0)));
		controls.add(searchField);

		textEditor = new JTextArea(EDITOR_ROWS, EDITOR_COLS);
		textEditor.setLineWrap(true);
		textEditor.setWrapStyleWord(true);
		textEditor.setEditable(true);

		textEditor.setText("Load text to text KMP search");

		JScrollPane scroll = new JScrollPane(textEditor);

		JPanel editor = new JPanel();
		editor.setBorder(edge);
		editor.setLayout(new BorderLayout());
		editor.add(controls, BorderLayout.NORTH);
		editor.add(scroll, BorderLayout.CENTER);

		frame = new JFrame("KMP");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(editor, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Reading a file into a string.
	 */
	private static String readFile(File file) {
		try {
			byte[] encoded = Files.readAllBytes(file.toPath());
			return new String(encoded, CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
