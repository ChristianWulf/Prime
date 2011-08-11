package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.math.BigInteger;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import prime.metric.Metric;
import view.helper.CheckResult;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/*
 *  n2  = (n - a)(n + a) + a2<br>
 *  p prim iff (p-1)! = -1 mod p<br>
 *  fmax² - f² = smax² - s²<br>
 *  (fmax²-f² == smax²-s² == 0) mod 16<br>
 *  (smax-s) mod (fmax-f) = aPrimFactor-1<br>
 *  d ist ein (zusammengesetzter) Faktor<br>
 *
 */
public class GUI extends JFrame implements ActionListener {

	private DefaultTableModel	model;
	private JButton				btnShow;
	private JButton				btnPrint;
	private JTable				table;
	JTextField					txfMaxInt;

	private JTextField			txfTestPrime;
	private JLabel				lblOutput;
	private JButton				btnCheckValue;
	JCheckBox					cbxFilterDiff0;
	JCheckBox					cbxFilterDiffMax_fi;
	private final Controller	controller;
	JCheckBox					cbxFilterSquares;
	public static JCheckBox		cbxFilterNonFermats;
	public static JCheckBox		cbxFilterPrimes;
	JCheckBox					cbxFilterNonSquares;
	JCheckBox					cbxFilterNon1Mod8;
	JCheckBox					cbxFilterLastDigit2378;
	JTextField					txfMinInt;
	private JPanel				testBar_1;
	private JLabel				lblAmount;

	public GUI() {
		controller = new Controller(this);
		createGUI();
	}

	private void createGUI() {
		getContentPane().setLayout(new BorderLayout());
		createComponents();
		this.pack();

		// set window preferences
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Prime");
		this.setMinimumSize(this.getPreferredSize());
		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
	}

	private void createComponents() {
		model = getNewModel();
		table = new JTable(model);
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				setAmount(table.getRowCount());
			}
		});
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(false);
		// set renderer for all column classes
		table.setDefaultRenderer(Object.class, new PrimeCellBackgroundRenderer());
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);

		JPanel pnlBottom = new JPanel(new BorderLayout());
		getContentPane().add(pnlBottom, BorderLayout.PAGE_END);

		testBar_1 = new JPanel();
		testBar(testBar_1);
		pnlBottom.add(testBar_1, BorderLayout.PAGE_START);

		lblAmount = new JLabel();
		setAmount(0);
		testBar_1.add(lblAmount, "10, 2");

		JPanel controlBar = new JPanel();
		buildControlBar(controlBar);
		pnlBottom.add(controlBar, BorderLayout.PAGE_END);
	}

	private void setAmount(int amount) {
		lblAmount.setText("Amount: " + amount);
	}

	private void testBar(JPanel testBar) {
		btnCheckValue = new JButton("Check");
		btnCheckValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = txfTestPrime.getText();
				BigInteger testValue = new BigInteger(text);
				CheckResult result = controller.check(testValue);
				lblOutput.setText("= " + result.f + "² - " + result.s + "² ("
						+ result.getEleapsedTime() + ")");
			}
		});
		testBar_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.GLUE_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GLUE_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));
		testBar.add(btnCheckValue, "2, 2, left, top");

		txfTestPrime = new JTextField(10);
		txfTestPrime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnCheckValue.doClick();
			}
		});
		testBar.add(txfTestPrime, "4, 2, left, center");

		lblOutput = new JLabel("Ready");
		testBar.add(lblOutput, "6, 2, left, center");
	}

	private void buildControlBar(JPanel controlBar) {
		cbxFilterDiffMax_fi = new JCheckBox("filter diff_max(fi)");
		controlBar.add(cbxFilterDiffMax_fi);

		cbxFilterLastDigit2378 = new JCheckBox("filter 2,3,7,8");
		controlBar.add(cbxFilterLastDigit2378);

		cbxFilterNon1Mod8 = new JCheckBox("filter x mod 8 != 1");
		controlBar.add(cbxFilterNon1Mod8);

		cbxFilterDiff0 = new JCheckBox("filter diff_min(fmax) = 0");
		controlBar.add(cbxFilterDiff0);

		cbxFilterNonFermats = new JCheckBox("filter non-fermats");
		controlBar.add(cbxFilterNonFermats);

		cbxFilterPrimes = new JCheckBox("filter primes", true);
		controlBar.add(cbxFilterPrimes);

		cbxFilterNonSquares = new JCheckBox("filter non-squares");
		controlBar.add(cbxFilterNonSquares);

		cbxFilterSquares = new JCheckBox("filter squares");
		controlBar.add(cbxFilterSquares);

		txfMinInt = new JTextField(5);
		txfMinInt.setText("3");
		txfMinInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShow.doClick();
			}
		});
		controlBar.add(txfMinInt);

		txfMaxInt = new JTextField(5);
		txfMaxInt.setText("300");
		txfMaxInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShow.doClick();
			}
		});
		controlBar.add(txfMaxInt);

		btnShow = new JButton("show");
		btnShow.addActionListener(this);
		controlBar.add(btnShow);

		btnPrint = new JButton("print");
		btnPrint.addActionListener(this);
		controlBar.add(btnPrint);
	}

	private DefaultTableModel getNewModel() {
		model = new DefaultTableModel();
		for (Metric m : controller.getMetrics()) {
			model.addColumn(m.getColumnName());
		}
		return model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnShow) {
			btnShow.setEnabled(false);
			txfMaxInt.setEnabled(false);

			model.setRowCount(0);
			for (Metric m : controller.getMetrics()) {
				m.reset();
			}

			boolean timeout = controller.show();
			if (timeout){
				System.out.println("t/o");
			}

			// add statistics row
			Vector<Object> statistics = new Vector<Object>();
			for (Metric m : controller.getMetrics()) {
				statistics.add(m.getMin() + "->" + m.getMax());
			}
			model.addRow(statistics);

			txfMaxInt.setEnabled(true);
			btnShow.setEnabled(true);
		} else if (e.getSource() == btnPrint) {
			try {
				table.print();
			} catch (PrinterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public DefaultTableModel getModel() {
		return model;
	}

}
