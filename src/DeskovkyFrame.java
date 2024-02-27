import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DeskovkyFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField name;
    private JCheckBox boughtCheckBox;
    private JRadioButton worstRadioButton;
    private JRadioButton middleRadioButton;
    private JRadioButton bestRadioButton;
    private JButton previousButton;
    private JButton nextButton;
    int index = 0;
    String soubor = "deskovky.txt";

    public DeskovkyFrame() {
        loadFile(soubor);
        setContentPane(mainPanel);
        setTitle("Deskovky");
        setSize(400, 220);
        setToTheMiddle(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(Model.getSize() == 0) {
            addBlankPage();
        }
        zobrazDeskovku(index);
        iniMenu();
        worstRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        });
        middleRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worstRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        });
        bestRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index > 0) {
                    index--;
                    zobrazDeskovku(index);
                } else {
                    index = Model.getSize() - 1;
                    zobrazDeskovku(index);
                    JOptionPane.showMessageDialog(null, "Dosáhli jste začátku seznamu.\nByli jste přesunuti na konec seznamu.");
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < Model.getSize() - 1) {
                    index++;
                    zobrazDeskovku(index);
                } else {
                    index = 0;
                    zobrazDeskovku(index);
                    JOptionPane.showMessageDialog(null, "Dosáhli jste konce seznamu.\nByli jste přesunuti na začátek seznamu.");
                }
            }
        });
    }

    private void loadFile(String soubor) {
        try (BufferedReader reader = new BufferedReader(new FileReader(soubor))) {
            String radek;
            while ((radek = reader.readLine()) != null) {
                String[] polozky = radek.split(";");
                Model.addDeskovka(new Deskovka(polozky[0], Boolean.parseBoolean(polozky[1]), Integer.parseInt(polozky[2])));
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Soubor "+soubor+" nenalezen!\n"+e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při čtení ze souboru "+soubor+":\n"+e.getMessage());
        }
    }

    public void setToTheMiddle(JFrame frame) {
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        Rectangle effectiveScreenArea = new Rectangle();

        effectiveScreenArea.x = bounds.x + screenInsets.left;
        effectiveScreenArea.y = bounds.y + screenInsets.top;
        effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;
        effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;

        // Center:
        int middleX = effectiveScreenArea.x + (effectiveScreenArea.width - frame.getWidth()) / 2;
        int middleY = effectiveScreenArea.y + (effectiveScreenArea.height - frame.getHeight()) / 2;
        frame.setLocation(middleX, middleY);
    }

    private void iniMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem menuItem = new JMenuItem("Uložit");
        file.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model.setDeskovka(index, new Deskovka(name.getText(), boughtCheckBox.isSelected(), worstRadioButton.isSelected() ? 1
                        : middleRadioButton.isSelected() ? 2 : bestRadioButton.isSelected() ? 3 : 0));
                updateFile(soubor);
                JOptionPane.showMessageDialog(null, "Deskovka byla uložena.");
            }
        });
        menuItem = new JMenuItem("Načíst");
        file.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zobrazDeskovku(index);
            }
        });
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        menuItem = new JMenuItem("Přidat");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model.addDeskovka(new Deskovka("Nová deskovka", false, 0));
                index = Model.getSize() - 1;
                zobrazDeskovku(index);
                updateFile(soubor);
            }
        });
        menuItem = new JMenuItem("Odebrat");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model.removeDeskovka(index);
                if (index > 0) {
                    index--;
                }
                if (Model.getSize() > 0) {
                    zobrazDeskovku(index);
                } else {
                    addBlankPage();
                    zobrazDeskovku(index);
                }
                updateFile(soubor);
            }
        });
    }

    private void addBlankPage() {
        Model.addDeskovka(new Deskovka("", false, 0));
    }

    public void zobrazDeskovku(int index) {
        Deskovka d = Model.getDeskovka(index);
        name.setText(d.getName());
        boughtCheckBox.setSelected(d.isBought());
        switch (d.getRating()) {
            case 1 -> {
                worstRadioButton.setSelected(true);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
            case 2 -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(true);
                bestRadioButton.setSelected(false);
            }
            case 3 -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(true);
            }
            default -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        }
    }

    public void updateFile(String soubor) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(soubor)))) {
            for (int i = 0; i < Model.getSize(); i++) {
                if (i > Main.zaklad-1) {
                    Deskovka d = Model.getDeskovka(i);
                    writer.println(d.getName() + ";" + d.isBought() + ";" + d.getRating());
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Soubor "+soubor+" nenalezen!\n"+e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při zápisu do souboru "+soubor+":\n"+e.getMessage());
        }
    }
}
